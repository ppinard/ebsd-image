/*
 * EBSD-Image
 * Copyright (C) 2010-2011 Philippe T. Pinard
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.ebsdimage.vendors.hkl.io;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.math.geometry.Rotation;
import org.apache.commons.math.geometry.RotationOrder;
import org.ebsdimage.core.*;
import org.ebsdimage.vendors.hkl.core.HklMMap;
import org.ebsdimage.vendors.hkl.core.HklMetadata;

import rmlimage.core.ByteMap;
import rmlimage.core.Calibration;
import rmlimage.core.Map;
import rmlimage.module.real.core.RealMap;
import rmlshared.io.FileUtil;
import rmlshared.io.TsvReader;
import rmlshared.ui.Monitorable;
import crystallography.core.Crystal;
import static java.lang.Double.parseDouble;
import static java.lang.Float.parseFloat;
import static java.lang.Integer.parseInt;

/**
 * Parser for HKL CTF file.
 * 
 * @author Philippe T. Pinard
 */
public class CtfLoader implements Monitorable {

    /** Error code in CTF file. */
    public static final ErrorCode ERROR_LOW_BAND_CONTRAST = new ErrorCode(
            "Low band contrast");

    /** Error code in CTF file. */
    public static final ErrorCode ERROR_LOW_BAND_SLOPE = new ErrorCode(
            "Low band slope");

    /** Error code in CTF file. */
    public static final ErrorCode ERROR_NO_SOLUTION = new ErrorCode(
            "No solution");

    /** Error code in CTF file. */
    public static final ErrorCode ERROR_HIGH_MAD = new ErrorCode("High MAD");

    /** Error code in CTF file. */
    public static final ErrorCode ERROR_NOT_YET_ANALYSED = new ErrorCode(
            "Not yet analysed", "job cancelled before point");

    /** Error code in CTF file. */
    public static final ErrorCode ERROR_UNEXPECTED = new ErrorCode(
            "Unexpected error");

    /** Progress value. */
    private double progress;

    /** Progress status. */
    private String status;

    /** Flag indicating if the operation should be interrupted. */
    private boolean isInterrupted = false;



    /**
     * Verify that the specified file is a CTF file.
     * 
     * @param file
     *            a file
     * @return <code>true</code> if the file is a CTF file, <code>false</code>
     *         otherwise
     */
    public boolean canLoad(File file) {
        return getValidationMessage(file).isEmpty();
    }



    /**
     * Returns the calibration (step size) saved in the CTF header.
     * 
     * @param headerLines
     *            array list containing all the lines in the header
     * @return calibration
     * @throws IOException
     *             if an error occurs while parsing the CTF file
     */
    private Calibration getCalibration(ArrayList<String[]> headerLines)
            throws IOException {
        double dx = parseHorizontalStepSize(headerLines);
        double dy = parseVerticalStepSize(headerLines);

        return new Calibration(dx, dy, "um");
    }



    /**
     * Return an array list containing only the lines of the header. The file
     * reader stops reading when it encounters a non-header line. Empty lines
     * are ignored.
     * 
     * @param file
     *            CTF file
     * @return array list of lines
     * @throws IOException
     *             if an error occurs while reading the CTF file
     */
    private ArrayList<String[]> getHeaderLines(File file) throws IOException {
        TsvReader reader = new TsvReader(file);

        // Separate lines in to header and data
        ArrayList<String[]> headerLines = new ArrayList<String[]>();

        while (true) {
            String[] line = reader.readLine();
            if (line == null) // end of file
                throw new IOException(
                        "End of file without finding end of header.");

            // Remove empty lines
            if (line.length == 0)
                continue;

            // End when column header line is read
            if (isDataHeaderLine(line))
                break;

            headerLines.add(line);
        }

        reader.close();

        return headerLines;
    }



    @Override
    public double getTaskProgress() {
        return progress;
    }



    @Override
    public String getTaskStatus() {
        return status;
    }



    /**
     * Returns the validation message. An empty string is the file is valid; a
     * error message otherwise.
     * 
     * @param file
     *            a file
     * @return error message or empty string
     * @throws NullPointerException
     *             if the file is null
     */
    private String getValidationMessage(File file) {
        if (file == null)
            throw new NullPointerException("File cannot be null.");

        // Check extension
        String ext = FileUtil.getExtension(file);
        if (!ext.equalsIgnoreCase("CTF"))
            return "The extension of the file must be CTF, not " + ext + ".";

        return "";
    }



    /**
     * Interrupts the operation.
     */
    public synchronized void interrupt() {
        isInterrupted = true;
    }



    /**
     * Checks that the line is the header of the columns for the data.
     * 
     * @param columns
     *            line values
     * @return <code>true</code> if the line is the columns header line,
     *         <code>false</code> otherwise
     */
    private boolean isDataHeaderLine(String[] columns) {
        if (columns.length != 11)
            return false;
        if (!"Phase".equals(columns[0]))
            return false;
        if (!"BC".equals(columns[9]))
            return false;

        return true;
    }



    /**
     * Checks if the operation should be interrupted. This method must be
     * synchronized because interrupt() may be called from any thread.
     * 
     * @return <code>true</code> if the operation is interrupted,
     *         <code>false</code> otherwise
     */
    private synchronized boolean isInterrupted() {
        return isInterrupted;
    }



    /**
     * Loads the CTF file in a <code>HklMMap</code>. The user can specify the
     * missing metadata.
     * 
     * @param file
     *            CTF file
     * @param metadata
     *            metadata loaded from {@link #loadMetadata(File, Microscope)}
     *            or from a CPR file
     * @param phases
     *            phases of the acquisition
     * @return a <code>HklMMap</code>
     * @throws IOException
     *             if an error occurs while parsing the CTF file
     * @throws NullPointerException
     *             if the phases is null
     * @throws IllegalArgumentException
     *             if the working distance is invalid
     * @throws NullPointerException
     *             if the camera is null
     */
    public HklMMap load(File file, HklMetadata metadata, Crystal[] phases)
            throws IOException {
        if (phases == null)
            throw new NullPointerException("Phases cannot be null.");

        // Validate the file to be a CTF
        status = "Validating CTF file.";
        String message = getValidationMessage(file);
        if (message.length() > 0)
            throw new IOException(message);

        // Parse header
        status = "Parsing header.";
        ArrayList<String[]> headerLines = getHeaderLines(file);
        Calibration cal = getCalibration(headerLines);

        // Read maps' dimensions
        int width = parseWidth(headerLines);
        int height = parseHeight(headerLines);

        // Check number of phases
        int phasesCount = parsePhaseNames(headerLines).length;
        if (phases.length < phasesCount)
            throw new IOException("At least " + phasesCount
                    + " needs to be defined.");
        if (phasesCount > 255)
            throw new IOException("A maximum of 255 phases can be defined.");

        // Read data
        HashMap<String, Map> mapList = readData(file, width, height, phases);

        // Set calibration
        for (Map map : mapList.values())
            map.setCalibration(cal);

        // Create the MultiMap
        HklMMap mmap = new HklMMap(width, height, mapList);
        mmap.setMetadata(metadata);

        // Set its file name to the same as the CTF file
        // but with the proper extension
        String[] validExtensions = mmap.getValidFileFormats();
        file = FileUtil.setExtension(file, validExtensions[0]);
        mmap.setFile(file);

        return mmap;
    }



    /**
     * Returns a <code>HklMetadata</code> from parsing the header of the CTF
     * file.
     * 
     * @param file
     *            a CTF file
     * @param microscope
     *            microscope configuration
     * @return a <code>HklMetadata</code>
     * @throws IOException
     *             if an error occurs while parsing the CTF file
     * @throws NullPointerException
     *             if the file is null
     */
    public HklMetadata loadMetadata(File file, Microscope microscope)
            throws IOException {
        ArrayList<String[]> headerLines = getHeaderLines(file);

        double[] acquisitionEulers = parseAcquisitionEulers(headerLines);
        Rotation sampleRotation =
                new Rotation(RotationOrder.ZXZ,
                        Math.toRadians(acquisitionEulers[0]),
                        Math.toRadians(acquisitionEulers[1]),
                        Math.toRadians(acquisitionEulers[2]));
        microscope.setSampleRotation(sampleRotation);

        double magnification = parseMagnification(headerLines);
        microscope.setMagnification(magnification);

        double beamEnergy = parseBeamEnergy(headerLines);
        microscope.setBeamEnergy(beamEnergy * 1000.0);

        double tiltAngle = parseTiltAngle(headerLines);
        microscope.setTiltAngle(Math.toRadians(tiltAngle));

        // Read project path
        String projectName = parseProjectName(headerLines);
        File projectPath = parseProjectPath(headerLines);

        return new HklMetadata(microscope, projectName, projectPath);
    }



    /**
     * Returns the name of the phases as defined in the CTF file.
     * 
     * @param file
     *            CTF file
     * @return name of the phases
     * @throws IOException
     *             if an error occurs while parsing the CTF file
     * @throws NullPointerException
     *             if the file is null
     */
    public String[] loadPhaseNames(File file) throws IOException {
        if (file == null)
            throw new NullPointerException("File cannot be null.");

        return parsePhaseNames(getHeaderLines(file));
    }



    /**
     * Returns the acquisition eulers.
     * 
     * @param headerLines
     *            all the header lines
     * @return acquisition eulers in degrees
     * @throws IOException
     *             if an error occurs while parsing
     */
    private double[] parseAcquisitionEulers(ArrayList<String[]> headerLines)
            throws IOException {
        double theta1 = Double.NaN;
        double theta2 = Double.NaN;
        double theta3 = Double.NaN;

        for (String[] line : headerLines) {
            if ("AcqE1".equals(line[0])) {
                if (line.length != 2)
                    throw new IOException("Expected 2 items on line 'AcqE1'");
                theta1 = Double.parseDouble(line[1]);
            } else if ("AcqE2".equals(line[0])) {
                if (line.length != 2)
                    throw new IOException("Expected 2 items on line 'AcqE2'");
                theta2 = Double.parseDouble(line[1]);
            } else if ("AcqE3".equals(line[0])) {
                if (line.length != 2)
                    throw new IOException("Expected 2 items on line 'AcqE3'");
                theta3 = Double.parseDouble(line[1]);
            }
        }

        if (Double.isNaN(theta1))
            throw new IOException("First acquisition angle could not be found.");
        if (Double.isNaN(theta2))
            throw new IOException(
                    "Second acquisition angle could not be found.");
        if (Double.isNaN(theta3))
            throw new IOException("Third acquisition angle could not be found.");

        return new double[] { theta1, theta2, theta3 };
    }



    /**
     * Returns the beam energy of the acquisition.
     * 
     * @param headerLines
     *            all the header lines
     * @return beam energy in keV
     * @throws IOException
     *             if an error occurs while parsing
     */
    private double parseBeamEnergy(ArrayList<String[]> headerLines)
            throws IOException {
        double beamEnergy = Double.NaN;

        for (String[] line : headerLines) {
            if ("Euler angles refer to Sample Coordinate system (CS0)!".equals(line[0])) {
                if (!"KV".equals(line[7]))
                    throw new IOException(
                            "Expected KV on line 'Euler angles refer to...'");
                beamEnergy = parseDouble(line[8]);
                break;
            }
        }

        if (Double.isNaN(beamEnergy))
            throw new NullPointerException("Beam energy cannot be found.");

        return beamEnergy;
    }



    /**
     * Returns the height of the map.
     * 
     * @param headerLines
     *            all the header lines
     * @return height of the map
     * @throws IOException
     *             if an error occurs while parsing
     */
    private int parseHeight(ArrayList<String[]> headerLines) throws IOException {
        int height = -1;

        for (String[] line : headerLines) {
            if ("YCells".equals(line[0])) {
                if (line.length != 2)
                    throw new IOException("Expected 2 items on line 'YCells'");
                height = Integer.parseInt(line[1]);
                break;
            }
        }

        if (height < 0)
            throw new IOException("Height could not be found.");

        return height;
    }



    /**
     * Returns the step size in the x direction.
     * 
     * @param headerLines
     *            all the header lines
     * @return horizontal step size in microns
     * @throws IOException
     *             if an error occurs while parsing
     */
    private double parseHorizontalStepSize(ArrayList<String[]> headerLines)
            throws IOException {
        double xstep = -1;

        for (String[] line : headerLines) {
            if ("XStep".equals(line[0])) {
                if (line.length != 2)
                    throw new IOException("Expected 2 items on line 'XStep'");
                xstep = parseDouble(line[1]);
                break;
            }
        }

        if (xstep < 0)
            throw new IOException("Horizontal step size could not be found.");

        return xstep;
    }



    /**
     * Returns the magnification of the acquisition.
     * 
     * @param headerLines
     *            all the header lines
     * @return magnification
     * @throws IOException
     *             if an error occurs while parsing
     */
    private double parseMagnification(ArrayList<String[]> headerLines)
            throws IOException {
        double mag = Double.NaN;

        for (String[] line : headerLines) {
            if ("Euler angles refer to Sample Coordinate system (CS0)!".equals(line[0])) {
                if (!"Mag".equals(line[1]))
                    throw new IOException(
                            "Expected Mag on line 'Euler angles refer to...'");
                mag = parseDouble(line[2]);
                break;
            }
        }

        if (Double.isNaN(mag))
            throw new NullPointerException("Magnification cannot be found.");

        return mag;
    }



    /**
     * Returns the name of all the defined phases.
     * 
     * @param headerLines
     *            all the header lines
     * @return names of defined phases
     * @throws IOException
     *             if an error occurs while parsing
     */
    private String[] parsePhaseNames(ArrayList<String[]> headerLines)
            throws IOException {
        ArrayList<String> phaseNames = new ArrayList<String>();

        for (String[] line : headerLines) {
            if (line.length == 8 && line[0].split(";").length == 3
                    && line[1].split(";").length == 3)
                phaseNames.add(line[2].trim());
        }

        return phaseNames.toArray(new String[phaseNames.size()]);
    }



    /**
     * Returns the project's name.
     * 
     * @param headerLines
     *            all the header lines
     * @return name of project
     * @throws IOException
     *             if an error occurs while parsing
     */
    private String parseProjectName(ArrayList<String[]> headerLines)
            throws IOException {
        String projectName = null;

        for (String[] line : headerLines) {
            if ("Prj".equals(line[0])) {
                if (line.length != 2)
                    throw new IOException("Expected 2 items on line 'Prj'");

                String[] paths = line[1].split("\\\\");
                projectName =
                        FileUtil.getBaseName(new File(paths[paths.length - 1]));
                break;
            }
        }

        if (projectName == null)
            throw new NullPointerException("Project name cannot be found.");

        return projectName;
    }



    /**
     * Returns the project's path.
     * 
     * @param headerLines
     *            all the header lines
     * @return path of the project
     * @throws IOException
     *             if an error occurs while parsing
     */
    private File parseProjectPath(ArrayList<String[]> headerLines)
            throws IOException {
        File projectPath = null;

        for (String[] line : headerLines) {
            if ("Prj".equals(line[0])) {
                if (line.length != 2)
                    throw new IOException("Expected 2 items on line 'Prj'");
                projectPath = FileUtil.getDirFile(new File(line[1]));
                break;
            }
        }

        if (projectPath == null)
            throw new NullPointerException("Project path cannot be found.");

        return projectPath;
    }



    /**
     * Returns the angle of the tilt during the acquisition.
     * 
     * @param headerLines
     *            all the header lines
     * @return tilt angle in degrees
     * @throws IOException
     *             if an error occurs while parsing
     */
    private double parseTiltAngle(ArrayList<String[]> headerLines)
            throws IOException {
        double tiltAngle = Double.NaN;

        for (String[] line : headerLines) {
            if ("Euler angles refer to Sample Coordinate system (CS0)!".equals(line[0])) {
                if (!"TiltAngle".equals(line[9]))
                    throw new IOException(
                            "Expected TiltAngle on line 'Euler angles refer to...'");
                tiltAngle = parseDouble(line[10]);
                break;
            }
        }

        if (Double.isNaN(tiltAngle))
            throw new NullPointerException("Tilt angle cannot be found.");

        return tiltAngle;
    }



    /**
     * Returns the step size in the y direction.
     * 
     * @param headerLines
     *            all the header lines
     * @return vertical step size in microns
     * @throws IOException
     *             if an error occurs while parsing
     */
    private double parseVerticalStepSize(ArrayList<String[]> headerLines)
            throws IOException {
        double ystep = -1;

        for (String[] line : headerLines) {
            if ("YStep".equals(line[0])) {
                if (line.length != 2)
                    throw new IOException("Expected 2 items on line 'YStep'");
                ystep = parseDouble(line[1]);
                break;
            }
        }

        if (ystep < 0)
            throw new IOException("Vertical step size could not be found.");

        return ystep;
    }



    /**
     * Returns the width of the map.
     * 
     * @param headerLines
     *            all the header lines
     * @return width of the map
     * @throws IOException
     *             if an error occurs while parsing
     */
    private int parseWidth(ArrayList<String[]> headerLines) throws IOException {
        int width = -1;

        for (String[] line : headerLines) {
            if ("XCells".equals(line[0])) {
                if (line.length != 2)
                    throw new IOException("Expected 2 items on line 'XCells'");
                width = Integer.parseInt(line[1]);
                break;
            }
        }

        if (width < 0)
            throw new IOException("Width could not be found.");

        return width;
    }



    /**
     * Reads the data in the CTF file and save it in the appropriate maps.
     * 
     * @param file
     *            CTF file
     * @param width
     *            width of the maps
     * @param height
     *            height of the maps
     * @param phases
     *            phases
     * @return list of maps to be saved in the <code>HklMMap</code>
     * @throws IOException
     *             if an error occurs while parsing the CTF file
     */
    private HashMap<String, Map> readData(File file, int width, int height,
            Crystal[] phases) throws IOException {
        // Find first data line
        TsvReader reader = new TsvReader(file);

        while (true) {
            String[] line = reader.readLine();
            if (line == null) // end of file
                throw new IOException(
                        "End of file without finding end of header.");

            // End when column header line is read
            if (isDataHeaderLine(line))
                break;
        }

        // Read data
        RealMap q0Map = new RealMap(width, height);
        RealMap q1Map = new RealMap(width, height);
        RealMap q2Map = new RealMap(width, height);
        RealMap q3Map = new RealMap(width, height);

        PhaseMap phaseMap = new PhaseMap(width, height);
        for (Crystal phase : phases)
            phaseMap.register(phase);

        ErrorMap errorMap = new ErrorMap(width, height);
        errorMap.register(1, ERROR_LOW_BAND_CONTRAST);
        errorMap.register(2, ERROR_LOW_BAND_SLOPE);
        errorMap.register(3, ERROR_NO_SOLUTION);
        errorMap.register(4, ERROR_HIGH_MAD);
        errorMap.register(5, ERROR_NOT_YET_ANALYSED);
        errorMap.register(6, ERROR_UNEXPECTED);

        ByteMap bcMap = new ByteMap(width, height);
        ByteMap bsMap = new ByteMap(width, height);
        ByteMap bandsMap = new ByteMap(width, height);
        RealMap madMap = new RealMap(width, height);

        status = "Reading data and creating maps.";
        Rotation rotation;
        int size = width * height;
        for (int n = 0; n < size; n++) {
            // Update progress
            progress = (double) n / size;

            // Interrupt
            if (isInterrupted())
                break;

            // Get line
            String[] line = reader.readLine();
            if (line == null) // end of file
                throw new IOException(
                        "End of file while still data left to read");
            if (line.length != 11)
                throw new IOException("Line " + reader.getLineReadCount()
                        + " does not have the right number of columns");

            // Phase id
            phaseMap.pixArray[n] = (byte) parseInt(line[0]);

            // Number of bands
            bandsMap.pixArray[n] = (byte) parseInt(line[3]);

            // Error code
            errorMap.pixArray[n] = (byte) parseInt(line[4]);

            // Orientation
            rotation =
                    new Rotation(RotationOrder.ZXZ,
                            Math.toRadians(parseDouble(line[5])),
                            Math.toRadians(parseDouble(line[6])),
                            Math.toRadians(parseDouble(line[7])));

            q0Map.pixArray[n] = (float) rotation.getQ0();
            q1Map.pixArray[n] = (float) rotation.getQ1();
            q2Map.pixArray[n] = (float) rotation.getQ2();
            q3Map.pixArray[n] = (float) rotation.getQ3();

            // Mean angular deviation
            madMap.pixArray[n] = parseFloat(line[8]);

            // Band contrast
            bcMap.pixArray[n] = (byte) parseInt(line[9]);

            // Band slope
            bsMap.pixArray[n] = (byte) parseInt(line[10]);
        }

        // Close reader
        reader.close();

        // Validate PhaseMap and ErrorMap
        phaseMap.validate();
        errorMap.validate();

        // Set the maps
        HashMap<String, Map> mapList = new HashMap<String, Map>();

        mapList.put(EbsdMMap.Q0, q0Map);
        mapList.put(EbsdMMap.Q1, q1Map);
        mapList.put(EbsdMMap.Q2, q2Map);
        mapList.put(EbsdMMap.Q3, q3Map);
        mapList.put(EbsdMMap.PHASES, phaseMap);
        mapList.put(EbsdMMap.ERRORS, errorMap);

        mapList.put(HklMMap.BAND_COUNT, bandsMap);
        mapList.put(HklMMap.MEAN_ANGULAR_DEVIATION, madMap);
        mapList.put(HklMMap.BAND_CONTRAST, bcMap);
        mapList.put(HklMMap.BAND_SLOPE, bsMap);

        return mapList;
    }

}
