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
package org.ebsdimage.vendors.tsl.io;

import static java.lang.Double.parseDouble;
import static ptpshared.utility.Arrays.concatenate;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.ebsdimage.core.Camera;
import org.ebsdimage.core.PhasesMap;
import org.ebsdimage.vendors.tsl.core.TslMMap;
import org.ebsdimage.vendors.tsl.core.TslMetadata;

import ptpshared.core.math.Eulers;
import ptpshared.core.math.Quaternion;
import ptpshared.utility.DataFile;
import rmlimage.core.Map;
import rmlimage.module.real.core.RealMap;
import rmlshared.io.FileUtil;
import rmlshared.io.SsvReader;
import rmlshared.ui.Monitorable;
import crystallography.core.Crystal;

/**
 * Parser for TSL ang file.
 * 
 * @author Philippe T. Pinard
 * 
 */
public class AngLoader implements Monitorable {

    /**
     * Returns a <code>TslMetadata</code> from parsing the header of the ang
     * file. Since the beam energy, magnification, tilt angle, sample's rotation
     * are not defined, they need to be specified by the user.
     * 
     * @param file
     *            ang file
     * @param beamEnergy
     *            energy of the electron beam in eV
     * @param magnification
     *            magnification of the EBSD acquisition
     * @param tiltAngle
     *            angle of sample's tilt in radians
     * @param sampleRotation
     *            rotation from the pattern frame (camera) into the sample frame
     * @return a <code>TslMetadata</code>
     * @throws IOException
     *             if an error occurs while parsing the ang file
     */
    public static TslMetadata getMetadata(File file, double beamEnergy,
            double magnification, double tiltAngle, Quaternion sampleRotation)
            throws IOException {
        AngLoader loader = new AngLoader();

        DataFile dataFile = loader.readFile(file);

        return loader.readHeader(dataFile, beamEnergy, magnification,
                tiltAngle, sampleRotation);
    }



    /**
     * Returns the name of the phases as defined in the ang file.
     * 
     * @param file
     *            ang file
     * @return name of the phases
     * @throws IOException
     *             if an error occurs while parsing the ang file
     */
    public static String[] getPhaseNames(File file) throws IOException {
        AngLoader loader = new AngLoader();

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
     */
    private static String getValidationMessage(File file) {
        // Check extension
        String ext = FileUtil.getExtension(file);
        if (!ext.equalsIgnoreCase("ang"))
            return "The extension of the file must be ang, not " + ext + ".";

        return "";
    }



    /**
     * Checks if the file is a valid ang file.
     * 
     * @param file
     *            a file
     * @return <code>true</code> if the file is valid, <code>false</code>
     *         otherwise
     */
    public static boolean isAng(File file) {
        return (getValidationMessage(file).length() == 0) ? true : false;
    }



    /**
     * Validates the file to be a valid ang file.
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



    /**
     * Loads the ang file in a <code>TslMMap</code>. The user can specify the
     * missing metadata.
     * 
     * @param file
     *            ang file
     * @param beamEnergy
     *            energy of the electron beam in eV
     * @param magnification
     *            magnification of the EBSD acquisition
     * @param tiltAngle
     *            angle of sample's tilt in radians
     * @param sampleRotation
     *            rotation from the pattern frame (camera) into the sample frame
     * @param phases
     *            phases of the acquisition
     * @return a <code>TslMMap</code>
     * @throws IOException
     *             if an error occurs while parsing the ang file
     * @throws NullPointerException
     *             if the phases array is null
     * @throws IllegalArgumentException
     *             if the beam energy is invalid
     * @throws IllegalArgumentException
     *             if the magnification is invalid
     * @throws IllegalArgumentException
     *             if the tilt angle is invalid
     * @throws NullPointerException
     *             if the sample rotation is null
     */
    public TslMMap load(File file, double beamEnergy, double magnification,
            double tiltAngle, Quaternion sampleRotation, Crystal[] phases)
            throws IOException {
        if (phases == null)
            throw new NullPointerException("Phases cannot be null.");

        // Validate the file to be a ang
        status = "Validating ang file.";
        validate(file);

        // Read lines
        status = "Reading ang file.";
        DataFile dataFile = readFile(file);

        // Parse header
        status = "Parsing header.";
        TslMetadata metadata =
                readHeader(dataFile, beamEnergy, magnification, tiltAngle,
                        sampleRotation);

        // Read width
        int width = parseWidth(dataFile.getHeaderLines());

        // Read height
        int height = parseHeight(dataFile.getHeaderLines());

        // Read grid
        String grid = parseGrid(dataFile.getHeaderLines());

        // Check number of phases
        int phasesCount = parsePhaseNames(dataFile.getHeaderLines()).length;
        if (phases.length < phasesCount)
            throw new IOException("At least " + phasesCount
                    + " needs to be defined.");
        if (phasesCount > 255)
            throw new IOException("A maximum of 255 phases can be defined.");

        // Data
        HashMap<String, Map> mapList;
        if ("HexGrid".equals(grid))
            mapList = readDataHexGrid(dataFile, width, height, phases);
        else if ("SqrGrid".equals(grid))
            mapList = readDataSqrGrid(dataFile, width, height, phases);
        else
            throw new IOException("Invalid grid type (+ " + grid + ").");

        // Create the MultiMap
        TslMMap mmap = new TslMMap(width, height, mapList, metadata);

        // Set its file name to the same as the ctf file
        // but with the proper extension
        String[] validExtensions = mmap.getValidFileFormats();
        file = FileUtil.setExtension(file, validExtensions[0]);
        mmap.setFile(file);

        return mmap;
    }



    /**
     * Returns the calibration.
     * 
     * @param headerLines
     *            all the header lines
     * @return calibration
     * @throws IOException
     *             if an error occurs while parsing
     */
    private Camera parseCalibration(ArrayList<String[]> headerLines)
            throws IOException {
        double calX = Double.NaN;
        double calY = Double.NaN;
        double calZ = Double.NaN;

        for (String[] line : headerLines) {
            if ("x-star".equals(line[0])) {
                if (line.length != 2)
                    throw new IOException("Expected 2 items on line 'x-star'");
                calX = Double.parseDouble(line[1]);
            } else if ("y-star".equals(line[0])) {
                if (line.length != 2)
                    throw new IOException("Expected 2 items on line 'y-star'");
                calY = Double.parseDouble(line[1]);
            } else if ("z-star".equals(line[0])) {
                if (line.length != 2)
                    throw new IOException("Expected 2 items on line 'z-star'");
                calZ = Double.parseDouble(line[1]);
            }
        }

        if (Double.isNaN(calX))
            throw new IOException(
                    "X coordinate of the calibration could not be found.");
        if (Double.isNaN(calY))
            throw new IOException(
                    "Y coordinate of the calibration could not be found.");
        if (Double.isNaN(calZ))
            throw new IOException(
                    "Z coordinate of the calibration could not be found.");

        return new Camera(calX - 0.5, calY - 0.5, calZ);
    }



    /**
     * Returns the grid type (HexGrid or ).
     * 
     * @param headerLines
     *            all the header lines
     * @return grid type
     * @throws IOException
     *             if an error occurs while parsing
     */
    private String parseGrid(ArrayList<String[]> headerLines)
            throws IOException {
        String grid = null;

        for (String[] line : headerLines) {
            if ("GRID:".equals(line[0])) {
                if (line.length != 2)
                    throw new IOException("Expected 2 items on line 'GRID:'");
                grid = line[1];
            }
        }

        if (grid == null)
            throw new IOException("The grid type could not be found.");

        return grid;
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
            if ("NROWS:".equals(line[0])) {
                if (line.length != 2)
                    throw new IOException("Expected 2 items on line 'NROWS:'");
                height = Integer.parseInt(line[1]);
            }
        }

        if (height < 0)
            throw new IOException("Height is not properly defined.");

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
        double xstep = Double.NaN;

        for (String[] line : headerLines) {
            if ("XSTEP:".equals(line[0])) {
                if (line.length != 2)
                    throw new IOException("Expected 2 items on line 'XSTEP:'");
                xstep = Double.parseDouble(line[1]) * 1e-6;
            }
        }

        if (Double.isNaN(xstep))
            throw new IOException("The x step size could not be found.");

        return xstep;
    }



    /**
     * Returns the name of the defined phases.
     * 
     * @param headerLines
     *            all the header lines
     * @return names of the phases
     * @throws IOException
     *             if an error occurs while parsing
     */
    private String[] parsePhaseNames(ArrayList<String[]> headerLines)
            throws IOException {
        ArrayList<Integer> ids = new ArrayList<Integer>();
        ArrayList<String> tmpNames = new ArrayList<String>();

        // Get data from header lines
        for (String[] line : headerLines) {
            if ("Phase".equals(line[0])) {
                if (line.length != 2)
                    throw new IOException("Expected 2 items on line 'Phase'");
                ids.add(Integer.parseInt(line[1]));
            } else if ("MaterialName".equals(line[0])) {
                if (line.length < 2)
                    throw new IOException(
                            "Expected 2 or more items on line 'MaterialName'");
                tmpNames.add(concatenate(Arrays.copyOfRange(line, 1,
                        line.length)));
            }
        }

        // Check data
        if (ids.size() == 0)
            throw new IOException("No phase are defined.");
        if (ids.size() != tmpNames.size())
            throw new IOException("Number of phase ids (" + ids.size()
                    + ") does not match the number of phase names ("
                    + tmpNames.size() + ").");

        // Associate id with name
        HashMap<Integer, String> mapNames = new HashMap<Integer, String>();

        for (int i = 0; i < ids.size(); i++)
            mapNames.put(ids.get(i), tmpNames.get(i));

        // Sort ids
        String[] names = new String[tmpNames.size()];

        Integer[] keys =
                mapNames.keySet().toArray(new Integer[mapNames.size()]);
        Arrays.sort(keys);

        for (int i = 0; i < keys.length; i++)
            names[i] = mapNames.get(keys[i]);

        return names;
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
        double ystep = Double.NaN;

        for (String[] line : headerLines) {
            if ("YSTEP:".equals(line[0])) {
                if (line.length != 2)
                    throw new IOException("Expected 2 items on line 'YSTEP:'");
                ystep = Double.parseDouble(line[1]) * 1e-6;
            }
        }

        if (Double.isNaN(ystep))
            throw new IOException("The y step size could not be found.");

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
        int widthEven = -1;

        for (String[] line : headerLines) {
            if ("NCOLS_EVEN:".equals(line[0])) {
                if (line.length != 2)
                    throw new IOException(
                            "Expected 2 items on line 'NCOLS_EVEN:'");
                widthEven = Integer.parseInt(line[1]);
            }
        }

        if (widthEven > 0)
            return widthEven;
        else
            throw new IOException("Width is not properly defined.");
    }



    /**
     * Returns the calibration's working distance.
     * 
     * @param headerLines
     *            all the header lines
     * @return working distance
     * @throws IOException
     *             if an error occurs while parsing
     */
    private double parseWorkingDistance(ArrayList<String[]> headerLines)
            throws IOException {
        double workingDistance = Double.NaN;

        for (String[] line : headerLines) {
            if ("WorkingDistance".equals(line[0])) {
                if (line.length != 2)
                    throw new IOException(
                            "Expected 2 items on line 'WorkingDistance'");
                workingDistance = Double.parseDouble(line[1]) * 1e-3;
            }
        }

        if (Double.isNaN(workingDistance))
            throw new IOException("The working distance could not be found.");

        return workingDistance;
    }



    /**
     * Returns a list of <code>Map</code> from reading the data from an
     * hexagonal grid. Since an hexagonal grid cannot be represented in
     * RML-Image, the first pixel of odd rows are shifted by a half pixel to the
     * left and the last pixel of even rows are discarded.
     * 
     * @param dataFile
     *            data file containing the header lines
     * @param width
     *            width of the maps
     * @param height
     *            height of the maps
     * @param phases
     *            defined phases
     * @return a list of <code>Map</code> to build a <code>TslMMap</code>
     * @throws IOException
     *             if an error occurs while parsing
     * @throws NullPointerException
     *             if the data file is null
     * @throws NullPointerException
     *             if the phases array is null
     */
    private HashMap<String, Map> readDataHexGrid(DataFile dataFile, int width,
            int height, Crystal[] phases) throws IOException {
        RealMap q0Map = new RealMap(width, height);
        RealMap q1Map = new RealMap(width, height);
        RealMap q2Map = new RealMap(width, height);
        RealMap q3Map = new RealMap(width, height);
        RealMap euler1Map = new RealMap(width, height);
        RealMap euler2Map = new RealMap(width, height);
        RealMap euler3Map = new RealMap(width, height);
        RealMap iqMap = new RealMap(width, height);
        RealMap ciMap = new RealMap(width, height);

        int size = width * height;

        float[] q0 = q0Map.pixArray;
        float[] q1 = q1Map.pixArray;
        float[] q2 = q2Map.pixArray;
        float[] q3 = q3Map.pixArray;
        byte[] phasesArray = new byte[size];
        float[] euler1 = euler1Map.pixArray;
        float[] euler2 = euler2Map.pixArray;
        float[] euler3 = euler3Map.pixArray;
        float[] iq = iqMap.pixArray;
        float[] ci = ciMap.pixArray;

        ArrayList<String[]> dataLines = dataFile.getDataLines();

        int rowCount = 0;
        int colCount = -1;
        int n = 0;
        for (int iLine = 0; iLine < dataLines.size(); iLine++) {
            colCount++;

            // Skip last entry of even rows
            if (rowCount % 2 == 0 && colCount == width) {
                colCount = -1;
                rowCount++;
                continue;
            } else if (rowCount % 2 != 0 && colCount == width) {
                colCount = 0;
                rowCount++;
            }

            // Get line
            String[] line = dataLines.get(iLine);

            // Update progress
            progress = (double) iLine / dataLines.size();

            // Interrupt
            if (isInterrupted())
                break;

            // Image quality
            iq[n] = (float) parseDouble(line[5]);

            // Confidence index
            ci[n] = (float) parseDouble(line[6]);

            // Rotation and phase
            double theta1 = parseDouble(line[0]);
            double theta2 = parseDouble(line[1]);
            double theta3 = parseDouble(line[2]);

            Eulers eulers;
            // Not indexed pixel have Euler angles greater than 2PI
            if (theta1 > 6.3 || theta2 > 6.3 || theta3 > 6.3) {
                phasesArray[n] = 0;
                eulers = new Eulers(0.0, 0.0, 0.0);
            } else {
                if (phases.length == 1)
                    phasesArray[n] = 1;
                else
                    phasesArray[n] = (byte) Integer.parseInt(line[7]);

                eulers = new Eulers(theta1, theta2, theta3);
            }

            euler1[n] = (float) eulers.theta1;
            euler2[n] = (float) eulers.theta2;
            euler3[n] = (float) eulers.theta3;

            Quaternion rotation = new Quaternion(eulers);

            q0[n] = (float) rotation.getQ0();
            q1[n] = (float) rotation.getQ1();
            q2[n] = (float) rotation.getQ2();
            q3[n] = (float) rotation.getQ3();

            n++;
        }

        // Set the maps
        HashMap<String, Map> mapList = new HashMap<String, Map>();

        mapList.put(TslMMap.Q0, q0Map);
        mapList.put(TslMMap.Q1, q1Map);
        mapList.put(TslMMap.Q2, q2Map);
        mapList.put(TslMMap.Q3, q3Map);
        mapList.put(TslMMap.PHASES, new PhasesMap(width, height, phasesArray,
                phases));

        mapList.put(TslMMap.EULER1, euler1Map);
        mapList.put(TslMMap.EULER2, euler2Map);
        mapList.put(TslMMap.EULER3, euler3Map);

        mapList.put(TslMMap.IMAGE_QUALITY, iqMap);
        mapList.put(TslMMap.CONFIDENCE_INDEX, ciMap);

        return mapList;
    }



    /**
     * Returns a list of <code>Map</code> from reading the data from a squared
     * grid.
     * 
     * @param dataFile
     *            data file containing the header lines
     * @param width
     *            width of the maps
     * @param height
     *            height of the maps
     * @param phases
     *            defined phases
     * @return a list of <code>Map</code> to build a <code>TslMMap</code>
     * @throws IOException
     *             if an error occurs while parsing
     */
    private HashMap<String, Map> readDataSqrGrid(DataFile dataFile, int width,
            int height, Crystal[] phases) throws IOException {
        RealMap q0Map = new RealMap(width, height);
        RealMap q1Map = new RealMap(width, height);
        RealMap q2Map = new RealMap(width, height);
        RealMap q3Map = new RealMap(width, height);
        RealMap euler1Map = new RealMap(width, height);
        RealMap euler2Map = new RealMap(width, height);
        RealMap euler3Map = new RealMap(width, height);
        RealMap iqMap = new RealMap(width, height);
        RealMap ciMap = new RealMap(width, height);

        int size = width * height;

        float[] q0 = q0Map.pixArray;
        float[] q1 = q1Map.pixArray;
        float[] q2 = q2Map.pixArray;
        float[] q3 = q3Map.pixArray;
        byte[] phasesArray = new byte[size];
        float[] euler1 = euler1Map.pixArray;
        float[] euler2 = euler2Map.pixArray;
        float[] euler3 = euler3Map.pixArray;
        float[] iq = iqMap.pixArray;
        float[] ci = ciMap.pixArray;

        ArrayList<String[]> dataLines = dataFile.getDataLines();
        if (dataLines.size() != size)
            throw new IOException("The number of data lines ("
                    + dataLines.size() + ") does not match the size "
                    + "of the maps (" + size + ").");

        for (int n = 0; n < size; n++) {
            // Get line
            String[] line = dataLines.get(n);

            // Update progress
            progress = (double) n / dataLines.size();

            // Interrupt
            if (isInterrupted())
                break;

            // Image quality
            iq[n] = (float) parseDouble(line[5]);

            // Confidence index
            ci[n] = (float) parseDouble(line[6]);

            // Rotation and phase
            double theta1 = parseDouble(line[0]);
            double theta2 = parseDouble(line[1]);
            double theta3 = parseDouble(line[2]);

            Eulers eulers;
            // Not indexed pixel have Euler angles greater than 2PI
            if (theta1 > 6.3 || theta2 > 6.3 || theta3 > 6.3) {
                phasesArray[n] = 0;
                eulers = new Eulers(0.0, 0.0, 0.0);
            } else {
                if (phases.length == 1)
                    phasesArray[n] = 1;
                else
                    phasesArray[n] = (byte) Integer.parseInt(line[7]);

                eulers = new Eulers(theta1, theta2, theta3);
            }

            euler1[n] = (float) eulers.theta1;
            euler2[n] = (float) eulers.theta2;
            euler3[n] = (float) eulers.theta3;

            Quaternion rotation = new Quaternion(eulers);

            q0[n] = (float) rotation.getQ0();
            q1[n] = (float) rotation.getQ1();
            q2[n] = (float) rotation.getQ2();
            q3[n] = (float) rotation.getQ3();
        }

        // Set the maps
        HashMap<String, Map> mapList = new HashMap<String, Map>();

        mapList.put(TslMMap.Q0, q0Map);
        mapList.put(TslMMap.Q1, q1Map);
        mapList.put(TslMMap.Q2, q2Map);
        mapList.put(TslMMap.Q3, q3Map);
        mapList.put(TslMMap.PHASES, new PhasesMap(width, height, phasesArray,
                phases));

        mapList.put(TslMMap.EULER1, euler1Map);
        mapList.put(TslMMap.EULER2, euler2Map);
        mapList.put(TslMMap.EULER3, euler3Map);

        mapList.put(TslMMap.IMAGE_QUALITY, iqMap);
        mapList.put(TslMMap.CONFIDENCE_INDEX, ciMap);

        return mapList;
    }



    /**
     * Reads the ang file, separates the header and data and returns a
     * <code>DataFile</code> containing the lines of the header and data.
     * 
     * @param file
     *            ang file
     * @return <code>DataFile</code> containing the lines of the header and data
     * @throws IOException
     *             if an error occurs while reading the file
     */
    private DataFile readFile(File file) throws IOException {
        SsvReader reader = new SsvReader(file);

        // Separate lines in to header and data
        ArrayList<String[]> headerLines = new ArrayList<String[]>();
        ArrayList<String[]> dataLines = new ArrayList<String[]>();

        while (true) {
            String[] line = reader.readLine();
            if (line == null) // end of file
                break;

            // Remove empty lines
            if (line.length == 0)
                continue;

            // Remove empty columns
            line = removeEmptyColumns(line);

            // Separate between header and data list
            if ("#".equals(line[0])) {
                line = Arrays.copyOfRange(line, 1, line.length);
                if (line.length > 0)
                    headerLines.add(line);
            } else
                dataLines.add(line);
        }

        reader.close();

        return new DataFile(headerLines, dataLines);
    }



    /**
     * Parses the header and returns the associated <code>TslMetadata</code>.
     * 
     * @param dataFile
     *            data file containing the header lines
     * @param beamEnergy
     *            energy of the electron beam in eV
     * @param magnification
     *            magnification of the EBSD acquisition
     * @param tiltAngle
     *            angle of sample's tilt in radians
     * @param sampleRotation
     *            rotation from the pattern frame (camera) into the sample frame
     * @return a <code>TslMetadata</code> from the information given in the
     *         header
     * @throws IOException
     *             if an error occurs while parsing
     * @throws IllegalArgumentException
     *             if the beam energy is invalid
     * @throws IllegalArgumentException
     *             if the magnification is invalid
     * @throws IllegalArgumentException
     *             if the tilt angle is invalid
     * @throws NullPointerException
     *             if the sample rotation is null
     * @throws NullPointerException
     *             if the data file is null
     */
    private TslMetadata readHeader(DataFile dataFile, double beamEnergy,
            double magnification, double tiltAngle, Quaternion sampleRotation)
            throws IOException {
        if (dataFile == null)
            throw new NullPointerException("Data file cannot be null.");

        ArrayList<String[]> headerLines = dataFile.getHeaderLines();

        Camera calibration = parseCalibration(headerLines);
        double workingDistance = parseWorkingDistance(headerLines);

        double xstep = parseHorizontalStepSize(headerLines);
        double ystep = parseVerticalStepSize(headerLines);

        return new TslMetadata(beamEnergy, magnification, tiltAngle,
                workingDistance, xstep, ystep, sampleRotation, calibration);
    }



    /**
     * Removes the empty columns corresponding to many white spaces between two
     * values.
     * 
     * @param columns
     *            a line from the reader
     * @return an array with only values
     */
    private String[] removeEmptyColumns(String[] columns) {
        ArrayList<String> newColumns = new ArrayList<String>();

        for (String column : columns)
            if (column.length() > 0)
                newColumns.add(column.trim());

        return newColumns.toArray(new String[newColumns.size()]);
    }

}
