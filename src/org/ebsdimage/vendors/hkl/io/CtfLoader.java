/*
 * EBSD-Image
 * Copyright (C) 2010 Philippe T. Pinard
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

import static java.lang.Double.parseDouble;
import static java.lang.Float.parseFloat;
import static java.lang.Integer.parseInt;
import static java.lang.Math.toRadians;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.ebsdimage.core.Camera;
import org.ebsdimage.core.PhasesMap;
import org.ebsdimage.vendors.hkl.core.HklMMap;
import org.ebsdimage.vendors.hkl.core.HklMetadata;

import ptpshared.core.math.Eulers;
import ptpshared.core.math.Quaternion;
import ptpshared.utility.DataFile;
import rmlimage.core.ByteMap;
import rmlimage.core.Map;
import rmlimage.module.real.core.RealMap;
import rmlshared.io.FileUtil;
import rmlshared.io.TsvReader;
import rmlshared.ui.Monitorable;
import crystallography.core.Crystal;

/**
 * Parser for HKL ctf file.
 * 
 * @author Philippe T. Pinard
 */
public class CtfLoader implements Monitorable {

    /**
     * Returns a <code>HklMetadata</code> from parsing the header of the ctf
     * file. Since the working distance and the calibration is not defined, it
     * needs to be specified by the user.
     * 
     * @param file
     *            ctf file
     * @param workingDistance
     *            working distance of the acquisition in meters
     * @param calibration
     *            calibration of the camera
     * @return a <code>HklMetadata</code>
     * @throws IOException
     *             if an error occurs while parsing the ctf file
     * @throws NullPointerException
     *             if the file is null
     */
    public static HklMetadata getMetadata(File file, double workingDistance,
            Camera calibration) throws IOException {
        if (file == null)
            throw new NullPointerException("File cannot be null.");

        CtfLoader loader = new CtfLoader();

        DataFile dataFile = loader.readFile(file);

        return loader.readHeader(dataFile, workingDistance, calibration);
    }



    /**
     * Returns the name of the phases as defined in the ctf file.
     * 
     * @param file
     *            ctf file
     * @return name of the phases
     * @throws IOException
     *             if an error occurs while parsing the ctf file
     * @throws NullPointerException
     *             if the file is null
     */
    public static String[] getPhaseNames(File file) throws IOException {
        if (file == null)
            throw new NullPointerException("File cannot be null.");

        CtfLoader loader = new CtfLoader();

        DataFile dataFile = loader.readFile(file);

        return loader.parsePhaseNames(dataFile.getHeaderLines());
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
    private static String getValidationMessage(File file) {
        if (file == null)
            throw new NullPointerException("File cannot be null.");

        // Check extension
        String ext = FileUtil.getExtension(file);
        if (!ext.equalsIgnoreCase("ctf"))
            return "The extension of the file must be ctf, not " + ext + ".";

        return "";
    }



    /**
     * Checks if the file is a valid ctf file.
     * 
     * @param file
     *            a file
     * @return <code>true</code> if the file is valid, <code>false</code>
     *         otherwise
     * @throws NullPointerException
     *             if the file is null
     */
    public static boolean isCtf(File file) {
        if (file == null)
            throw new NullPointerException("File cannot be null.");

        return (getValidationMessage(file).length() == 0) ? true : false;
    }



    /**
     * Validates the file to be a valid ctf file.
     * 
     * @param file
     *            a file
     * @throws IOException
     *             if the file is not valid
     */
    private static void validate(File file) throws IOException {
        String message = getValidationMessage(file);
        if (message.length() > 0)
            throw new IOException(message);
    }

    /** Progress value. */
    protected double progress;

    /** Progress status. */
    protected String status;

    /** Flag indicating if the operation should be interrupted. */
    private boolean isInterrupted = false;



    @Override
    public double getTaskProgress() {
        return progress;
    }



    @Override
    public String getTaskStatus() {
        return status;
    }



    /**
     * Checks that the line is the header of the columns for the data.
     * 
     * @param columns
     *            line values
     * @return <code>true</code> if the line is the columns header line,
     *         <code>false</code> otherwise
     */
    private boolean isColumnsHeaderLine(String[] columns) {
        if (columns.length != 11)
            return false;
        if (!"Phase".equals(columns[0]))
            return false;
        if (!"BC".equals(columns[9]))
            return false;

        return true;
    }



    /**
     * Loads the ctf file in a <code>HklMMap</code>. The user can specify the
     * missing metadata.
     * 
     * @param file
     *            ctf file
     * @param workingDistance
     *            working distance of the EBSD acquisition in meters
     * @param calibration
     *            calibration of the camera
     * @param phases
     *            phases of the acquisition
     * @return a <code>HklMMap</code>
     * @throws IOException
     *             if an error occurs while parsing the ctf file
     * @throws NullPointerException
     *             if the phases is null
     * @throws IllegalArgumentException
     *             if the working distance is invalid
     * @throws NullPointerException
     *             if the camera is null
     */
    public HklMMap load(File file, double workingDistance, Camera calibration,
            Crystal[] phases) throws IOException {
        if (phases == null)
            throw new NullPointerException("Phases cannot be null.");

        // Validate the file to be a ctf
        status = "Validating ctf file.";
        validate(file);

        // Read lines
        status = "Reading ctf file.";
        DataFile dataFile = readFile(file);

        // Parse header
        status = "Parsing header.";
        HklMetadata metadata =
                readHeader(dataFile, workingDistance, calibration);

        // Read width
        int width = parseWidth(dataFile.getHeaderLines());

        // Read height
        int height = parseHeight(dataFile.getHeaderLines());

        // Check number of phases
        int phasesCount = parsePhaseNames(dataFile.getHeaderLines()).length;
        if (phases.length < phasesCount)
            throw new IOException("At least " + phasesCount
                    + " needs to be defined.");
        if (phasesCount > 255)
            throw new IOException("A maximum of 255 phases can be defined.");

        RealMap q0Map = new RealMap(width, height);
        RealMap q1Map = new RealMap(width, height);
        RealMap q2Map = new RealMap(width, height);
        RealMap q3Map = new RealMap(width, height);
        ByteMap bcMap = new ByteMap(width, height);
        RealMap euler1Map = new RealMap(width, height);
        RealMap euler2Map = new RealMap(width, height);
        RealMap euler3Map = new RealMap(width, height);
        ByteMap bandsMap = new ByteMap(width, height);
        ByteMap errorMap = new ByteMap(width, height);
        RealMap madMap = new RealMap(width, height);
        ByteMap bsMap = new ByteMap(width, height);

        // Read data
        int size = width * height;

        float[] q0 = q0Map.pixArray;
        float[] q1 = q1Map.pixArray;
        float[] q2 = q2Map.pixArray;
        float[] q3 = q3Map.pixArray;
        byte[] phasesArray = new byte[size];
        byte[] bc = bcMap.pixArray;
        float[] euler1 = euler1Map.pixArray;
        float[] euler2 = euler2Map.pixArray;
        float[] euler3 = euler3Map.pixArray;
        byte[] bands = bandsMap.pixArray;
        byte[] error = errorMap.pixArray;
        float[] mad = madMap.pixArray;
        byte[] bs = bsMap.pixArray;

        status = "Reading data and creating maps.";
        ArrayList<String[]> dataLines = dataFile.getDataLines();

        for (int n = 0; n < size; n++) {
            // Get line
            String[] line = dataLines.get(n);

            // Update progress
            progress = (double) n / size;

            // Interrupt
            if (isInterrupted())
                break;

            // Phase id
            phasesArray[n] = (byte) parseInt(line[0]);

            // Number of bands
            bands[n] = (byte) parseInt(line[3]);

            // Error code
            error[n] = (byte) parseInt(line[4]);

            // Orientation
            Eulers eulers =
                    new Eulers(toRadians(parseDouble(line[5])),
                            toRadians(parseDouble(line[6])),
                            toRadians(parseDouble(line[7])));

            euler1[n] = (float) eulers.theta1;
            euler2[n] = (float) eulers.theta2;
            euler3[n] = (float) eulers.theta3;

            Quaternion rotation = new Quaternion(eulers);

            q0[n] = (float) rotation.getQ0();
            q1[n] = (float) rotation.getQ1();
            q2[n] = (float) rotation.getQ2();
            q3[n] = (float) rotation.getQ3();

            // Mean angular deviation
            mad[n] = parseFloat(line[8]);

            // Band contrast
            bc[n] = (byte) parseInt(line[9]);

            // Band slope
            bs[n] = (byte) parseInt(line[10]);
        }

        // Set the maps
        HashMap<String, Map> mapList = new HashMap<String, Map>();
        mapList.put(HklMMap.Q0, q0Map);
        mapList.put(HklMMap.Q1, q1Map);
        mapList.put(HklMMap.Q2, q2Map);
        mapList.put(HklMMap.Q3, q3Map);
        mapList.put(HklMMap.PHASES, new PhasesMap(width, height, phasesArray,
                phases));

        mapList.put(HklMMap.BAND_COUNT, bandsMap);
        mapList.put(HklMMap.ERROR_NUMBER, errorMap);
        mapList.put(HklMMap.EULER1, euler1Map);
        mapList.put(HklMMap.EULER2, euler2Map);
        mapList.put(HklMMap.EULER3, euler3Map);
        mapList.put(HklMMap.MEAN_ANGULAR_DEVIATION, madMap);
        mapList.put(HklMMap.BAND_CONTRAST, bcMap);
        mapList.put(HklMMap.BAND_SLOPE, bsMap);

        // Create the MultiMap
        HklMMap mmap = new HklMMap(width, height, mapList, metadata);

        // Set its file name to the same as the ctf file
        // but with the proper extension
        String[] validExtensions = mmap.getValidFileFormats();
        file = FileUtil.setExtension(file, validExtensions[0]);
        mmap.setFile(file);

        return mmap;
    }



    /**
     * Returns the acquisition eulers.
     * 
     * @param headerLines
     *            all the header lines
     * @return acquisition eulers
     * @throws IOException
     *             if an error occurs while parsing
     */
    private Eulers parseAcquisitionEulers(ArrayList<String[]> headerLines)
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

        return new Eulers(theta1, theta2, theta3);
    }



    /**
     * Returns the beam energy of the acquisition.
     * 
     * @param headerLines
     *            all the header lines
     * @return beam energy
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
                beamEnergy = parseDouble(line[8]) * 1000; // in eV
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
     * @return horizontal step size
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
                xstep = parseDouble(line[1]) * 1e-6; // in meters
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
     * @return tilt angle
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
                tiltAngle = toRadians(parseDouble(line[10])); // in rad
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
     * @return vertical step size
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
                ystep = parseDouble(line[1]) * 1e-6; // in meters
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
            }
        }

        if (width < 0)
            throw new IOException("Width could not be found.");

        return width;
    }



    /**
     * Reads the ctf file, separates the header and data and returns a
     * <code>DataFile</code> containing the lines of the header and data.
     * 
     * @param file
     *            ctf file
     * @return <code>DataFile</code> containing the lines of the header and data
     * @throws IOException
     *             if an error occurs while reading the file
     */
    private DataFile readFile(File file) throws IOException {
        TsvReader reader = new TsvReader(file);

        // Separate lines in to header and data
        ArrayList<String[]> headerLines = new ArrayList<String[]>();
        ArrayList<String[]> dataLines = new ArrayList<String[]>();

        boolean header = true;
        while (true) {
            String[] line = reader.readLine();
            if (line == null) // end of file
                break;

            // Remove empty lines
            if (line.length == 0)
                continue;

            // Separate between header and data list
            if (header)
                headerLines.add(line);
            else
                dataLines.add(line);

            // Switch between header to data
            if (isColumnsHeaderLine(line))
                header = false;
        }

        reader.close();

        return new DataFile(headerLines, dataLines);
    }



    /**
     * Parses the header and returns the associated <code>HklMetadata</code>.
     * 
     * @param dataFile
     *            data file containing the header lines
     * @param workingDistance
     *            working distance of the EBSD acquisition in meters
     * @param calibration
     *            calibration of the camera
     * @return a <code>HklMetadata</code> from the information given in the
     *         header
     * @throws IOException
     *             if an error occurs while parsing
     * @throws IllegalArgumentException
     *             if the working distance is invalid
     * @throws NullPointerException
     *             if the camera is null
     * @throws NullPointerException
     *             if the data file is null
     */
    private HklMetadata readHeader(DataFile dataFile, double workingDistance,
            Camera calibration) throws IOException {
        if (dataFile == null)
            throw new NullPointerException("Data file cannot be null.");

        ArrayList<String[]> headerLines = dataFile.getHeaderLines();

        // Read project path
        String projectName = parseProjectName(headerLines);
        File projectPath = parseProjectPath(headerLines);

        // Read horizontal step size
        double xstep = parseHorizontalStepSize(headerLines);

        // Read vertical step size
        double ystep = parseVerticalStepSize(headerLines);

        // Read acquisition eulers
        Eulers acquisitionEulers = parseAcquisitionEulers(headerLines);

        // Read magnification, beam energy, tilt angle
        double magnification = parseMagnification(headerLines);
        double beamEnergy = parseBeamEnergy(headerLines);
        double tiltAngle = parseTiltAngle(headerLines);

        return new HklMetadata(beamEnergy, magnification, tiltAngle,
                workingDistance, xstep, ystep,
                new Quaternion(acquisitionEulers), calibration, projectName,
                projectPath);
    }



    /**
     * Interrupts the operation.
     */
    public synchronized void interrupt() {
        isInterrupted = true;
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

}
