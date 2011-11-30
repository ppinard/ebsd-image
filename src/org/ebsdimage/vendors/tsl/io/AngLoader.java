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
package org.ebsdimage.vendors.tsl.io;

import static java.lang.Double.parseDouble;
import static ptpshared.util.Arrays.concatenate;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.apache.commons.math.geometry.Rotation;
import org.apache.commons.math.geometry.RotationOrder;
import org.ebsdimage.core.AcquisitionConfig;
import org.ebsdimage.core.EbsdMMap;
import org.ebsdimage.core.ErrorMap;
import org.ebsdimage.core.Microscope;
import org.ebsdimage.core.PhaseMap;
import org.ebsdimage.vendors.tsl.core.TslMMap;
import org.ebsdimage.vendors.tsl.core.TslMetadata;

import rmlimage.core.Calibration;
import rmlimage.core.Map;
import rmlimage.module.real.core.RealMap;
import rmlshared.io.FileUtil;
import rmlshared.io.SsvReader;
import rmlshared.ui.Monitorable;
import crystallography.core.Crystal;

/**
 * Parser for TSL ANG file.
 * 
 * @author Philippe T. Pinard
 */
public class AngLoader implements Monitorable {

    /** Progress value. */
    private double progress;

    /** Progress status. */
    private String status;

    /** Flag indicating if the operation should be interrupted. */
    private boolean isInterrupted = false;



    /**
     * Checks if the file is a valid ANG file.
     * 
     * @param file
     *            a file
     * @return <code>true</code> if the file is valid, <code>false</code>
     *         otherwise
     */
    public boolean canLoad(File file) {
        return (getValidationMessage(file).length() == 0) ? true : false;
    }



    /**
     * Returns the calibration (step size) saved in the ANG header.
     * 
     * @param headerLines
     *            array list containing all the lines in the header
     * @return calibration
     * @throws IOException
     *             if an error occurs while parsing the ANG file
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
     *            ANG file
     * @return array list of lines
     * @throws IOException
     *             if an error occurs while reading the ANG file
     */
    private ArrayList<String[]> getHeaderLines(File file) throws IOException {
        SsvReader reader = new SsvReader(file);

        ArrayList<String[]> headerLines = new ArrayList<String[]>();

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
            if (!"#".equals(line[0]))
                break;

            line = Arrays.copyOfRange(line, 1, line.length);
            if (line.length > 0)
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
     */
    private String getValidationMessage(File file) {
        // Check extension
        String ext = FileUtil.getExtension(file);
        if (!ext.equalsIgnoreCase("ANG"))
            return "The extension of the file must be ANG, not " + ext + ".";

        return "";
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
     * Loads the ANG file in a <code>TslMMap</code>. The user can specify the
     * missing metadata.
     * 
     * @param file
     *            ANG file
     * @param metadata
     *            metadata loaded from {@link #loadMetadata(File, Microscope)}
     *            or from another source
     * @param phases
     *            phases of the acquisition
     * @return a <code>TslMMap</code>
     * @throws IOException
     *             if an error occurs while parsing the ANG file
     * @throws NullPointerException
     *             if the phases array is null
     */
    public TslMMap load(File file, TslMetadata metadata, Crystal[] phases)
            throws IOException {
        if (phases == null)
            throw new NullPointerException("Phases cannot be null.");

        // Validate the file to be a ANG
        status = "Validating ANG file.";
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

        // Read grid

        // Check number of phases
        int phasesCount = parsePhaseNames(headerLines).length;
        if (phases.length < phasesCount)
            throw new IOException("At least " + phasesCount
                    + " needs to be defined.");
        if (phasesCount > 255)
            throw new IOException("A maximum of 255 phases can be defined.");

        // Read data
        String grid = parseGrid(headerLines);
        if (grid.equals("SprGrid"))
            throw new IOException("Parsing of " + grid + " is not supported.");
        HashMap<String, Map> mapList =
                readDataSqrGrid(file, width, height, phases);

        // Set calibration
        for (Map map : mapList.values())
            map.setCalibration(cal);

        // Create the MultiMap
        TslMMap mmap = new TslMMap(width, height, mapList);
        mmap.setMetadata(metadata);

        // Set its file name to the same as the ANG file
        // but with the proper extension
        String[] validExtensions = mmap.getValidFileFormats();
        file = FileUtil.setExtension(file, validExtensions[0]);
        mmap.setFile(file);

        return mmap;
    }



    /**
     * Parses the header and returns the associated <code>TslMetadata</code>.
     * 
     * @param file
     *            an ANG file
     * @param microscope
     *            microscope configuration
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
    public TslMetadata loadMetadata(File file, Microscope microscope)
            throws IOException {
        ArrayList<String[]> headerLines = getHeaderLines(file);

        double pcX = parsePatternCenterX(headerLines);

        // In TSL the pattern center Y is calculated from the bottom of the
        // image instead from the top
        double pcY = 1.0 - parsePatternCenterY(headerLines);

        double cameraDistance =
                parseCameraDistance(headerLines) * microscope.camera.width;

        double workingDistance = parseWorkingDistance(headerLines) * 1e-3;

        AcquisitionConfig acqConfig =
                new AcquisitionConfig(microscope,
                        AcquisitionConfig.DEFAULT.tiltAngle, workingDistance,
                        AcquisitionConfig.DEFAULT.beamEnergy,
                        AcquisitionConfig.DEFAULT.magnification,
                        AcquisitionConfig.DEFAULT.sampleRotation, pcX, pcY,
                        cameraDistance);
        return new TslMetadata(acqConfig);
    }



    /**
     * Returns the name of the phases as defined in the ANG file.
     * 
     * @param file
     *            ANG file
     * @return name of the phases
     * @throws IOException
     *             if an error occurs while parsing the ANG file
     */
    public String[] loadPhaseNames(File file) throws IOException {
        if (file == null)
            throw new NullPointerException("File cannot be null.");

        return parsePhaseNames(getHeaderLines(file));
    }



    /**
     * Returns the camera distance.
     * 
     * @param headerLines
     *            all the header lines
     * @return camera distance
     * @throws IOException
     *             if an error occurs while parsing
     */
    private double parseCameraDistance(ArrayList<String[]> headerLines)
            throws IOException {
        double value = Double.NaN;

        for (String[] line : headerLines) {
            if ("z-star".equals(line[0])) {
                if (line.length != 2)
                    throw new IOException("Expected 2 items on line 'z-star'");
                value = Double.parseDouble(line[1]);
            }
        }

        if (Double.isNaN(value))
            throw new IOException("Camera distance could not be found.");

        return value;
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
                break;
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
                break;
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
     * @return horizontal step size in microns
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
                xstep = Double.parseDouble(line[1]);
                break;
            }
        }

        if (Double.isNaN(xstep))
            throw new IOException("The x step size could not be found.");

        return xstep;
    }



    /**
     * Returns the pattern center X coordinate.
     * 
     * @param headerLines
     *            all the header lines
     * @return pattern center X
     * @throws IOException
     *             if an error occurs while parsing
     */
    private double parsePatternCenterX(ArrayList<String[]> headerLines)
            throws IOException {
        double value = Double.NaN;

        for (String[] line : headerLines) {
            if ("x-star".equals(line[0])) {
                if (line.length != 2)
                    throw new IOException("Expected 2 items on line 'x-star'");
                value = Double.parseDouble(line[1]);
            }
        }

        if (Double.isNaN(value))
            throw new IOException("Pattern center X could not be found.");

        return value;
    }



    /**
     * Returns the pattern center Y coordinate.
     * 
     * @param headerLines
     *            all the header lines
     * @return pattern center Y
     * @throws IOException
     *             if an error occurs while parsing
     */
    private double parsePatternCenterY(ArrayList<String[]> headerLines)
            throws IOException {
        double value = Double.NaN;

        for (String[] line : headerLines) {
            if ("y-star".equals(line[0])) {
                if (line.length != 2)
                    throw new IOException("Expected 2 items on line 'y-star'");
                value = Double.parseDouble(line[1]);
            }
        }

        if (Double.isNaN(value))
            throw new IOException("Pattern center Y could not be found.");

        return value;
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
     * @return vertical step size in microns
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
                ystep = Double.parseDouble(line[1]);
                break;
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
                break;
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
     * @return working distance in mm
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
                workingDistance = Double.parseDouble(line[1]);
            }
        }

        if (Double.isNaN(workingDistance))
            throw new IOException("The working distance could not be found.");

        return workingDistance;
    }



    /**
     * Returns a list of <code>Map</code> from reading the data from a squared
     * grid.
     * 
     * @param file
     *            ANG file
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
    private HashMap<String, Map> readDataSqrGrid(File file, int width,
            int height, Crystal[] phases) throws IOException {
        SsvReader reader = new SsvReader(file);

        RealMap q0Map = new RealMap(width, height);
        RealMap q1Map = new RealMap(width, height);
        RealMap q2Map = new RealMap(width, height);
        RealMap q3Map = new RealMap(width, height);

        PhaseMap phaseMap = new PhaseMap(width, height);
        for (Crystal phase : phases)
            phaseMap.register(phase);

        ErrorMap errorMap = new ErrorMap(width, height);

        RealMap iqMap = new RealMap(width, height);
        RealMap ciMap = new RealMap(width, height);

        status = "Reading data and creating maps.";

        Rotation rotation;
        int size = width * height;
        int n = 0;

        while (true) {
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

            // Remove empty lines
            if (line.length == 0)
                continue;

            // Remove empty columns
            line = removeEmptyColumns(line);

            // Skip header line
            if ("#".equals(line[0]))
                continue;

            // Check for a valid data line
            if (line.length < 8)
                throw new IOException("Line " + reader.getLineReadCount()
                        + " must have at least 8 columns");

            // Image quality
            iqMap.pixArray[n] = (float) parseDouble(line[5]);

            // Confidence index
            ciMap.pixArray[n] = (float) parseDouble(line[6]);

            // Rotation and phase
            double theta1 = parseDouble(line[0]);
            double theta2 = parseDouble(line[1]);
            double theta3 = parseDouble(line[2]);

            // Not indexed pixel have Euler angles greater than 2PI
            if (theta1 > 6.3 || theta2 > 6.3 || theta3 > 6.3) {
                rotation = Rotation.IDENTITY;
                phaseMap.pixArray[n] = (byte) 0;
            } else {
                if (phases.length == 1)
                    phaseMap.pixArray[n] = (byte) 1;
                else
                    phaseMap.pixArray[n] = (byte) Integer.parseInt(line[7]);

                rotation =
                        new Rotation(RotationOrder.ZXZ, theta1, theta2, theta3);
            }

            q0Map.pixArray[n] = (float) rotation.getQ0();
            q1Map.pixArray[n] = (float) rotation.getQ1();
            q2Map.pixArray[n] = (float) rotation.getQ2();
            q3Map.pixArray[n] = (float) rotation.getQ3();

            n++;

            if (n == size) // All data is read
                break;
        }

        reader.close();

        // Set the maps
        HashMap<String, Map> mapList = new HashMap<String, Map>();

        mapList.put(EbsdMMap.Q0, q0Map);
        mapList.put(EbsdMMap.Q1, q1Map);
        mapList.put(EbsdMMap.Q2, q2Map);
        mapList.put(EbsdMMap.Q3, q3Map);
        mapList.put(EbsdMMap.PHASES, phaseMap);
        mapList.put(EbsdMMap.ERRORS, errorMap);

        mapList.put(TslMMap.IMAGE_QUALITY, iqMap);
        mapList.put(TslMMap.CONFIDENCE_INDEX, ciMap);

        return mapList;
    }



    // /**
    // * Returns a list of <code>Map</code> from reading the data from an
    // * hexagonal grid. Since an hexagonal grid cannot be represented in
    // * RML-Image, the first pixel of odd rows are shifted by a half pixel to
    // the
    // * left and the last pixel of even rows are discarded.
    // *
    // * @param file
    // * ANG file
    // * @param width
    // * width of the maps
    // * @param height
    // * height of the maps
    // * @param phases
    // * defined phases
    // * @return a list of <code>Map</code> to build a <code>TslMMap</code>
    // * @throws IOException
    // * if an error occurs while parsing
    // * @throws NullPointerException
    // * if the data file is null
    // * @throws NullPointerException
    // * if the phases array is null
    // */
    // private HashMap<String, Map> readDataHexGrid(File file, int width,
    // int height, Crystal[] phases) throws IOException {
    // SsvReader reader = new SsvReader(file);
    //
    // RealMap q0Map = new RealMap(width, height);
    // RealMap q1Map = new RealMap(width, height);
    // RealMap q2Map = new RealMap(width, height);
    // RealMap q3Map = new RealMap(width, height);
    // RealMap euler1Map = new RealMap(width, height);
    // RealMap euler2Map = new RealMap(width, height);
    // RealMap euler3Map = new RealMap(width, height);
    // RealMap iqMap = new RealMap(width, height);
    // RealMap ciMap = new RealMap(width, height);
    //
    // int size = width * height;
    //
    // float[] q0 = q0Map.pixArray;
    // float[] q1 = q1Map.pixArray;
    // float[] q2 = q2Map.pixArray;
    // float[] q3 = q3Map.pixArray;
    // byte[] phasesArray = new byte[size];
    // float[] euler1 = euler1Map.pixArray;
    // float[] euler2 = euler2Map.pixArray;
    // float[] euler3 = euler3Map.pixArray;
    // float[] iq = iqMap.pixArray;
    // float[] ci = ciMap.pixArray;
    //
    // int rowCount = 0;
    // int colCount = -1;
    // int n = 0;
    // int iLine = 0;
    //
    // while (true) {
    // // Get line
    // String[] line = reader.readLine();
    //
    // if (line == null) // end of file
    // throw new IOException(
    // "End of file while still data left to read");
    //
    // // Remove empty lines
    // if (line.length == 0)
    // continue;
    //
    // // Remove empty columns
    // line = removeEmptyColumns(line);
    //
    // // Skip header line
    // if ("#".equals(line[0]))
    // continue;
    //
    // colCount++;
    //
    // // Skip last entry of even rows
    // if (rowCount % 2 == 0 && colCount == width) {
    // colCount = -1;
    // rowCount++;
    // continue;
    // } else if (rowCount % 2 != 0 && colCount == width) {
    // colCount = 0;
    // rowCount++;
    // }
    //
    // // Check that line is a valid data line
    // if (line.length != 10)
    // throw new IOException("Line " + reader.getLineReadCount()
    // + " does not have the right number of columns ("
    // + line.length + ")");
    //
    // // Update progress
    // progress = (double) n / size;
    //
    // // Interrupt
    // if (isInterrupted())
    // break;
    //
    // // Image quality
    // iq[n] = (float) parseDouble(line[5]);
    //
    // // Confidence index
    // ci[n] = (float) parseDouble(line[6]);
    //
    // // Rotation and phase
    // double theta1 = parseDouble(line[0]);
    // double theta2 = parseDouble(line[1]);
    // double theta3 = parseDouble(line[2]);
    //
    // Eulers eulers;
    // // Not indexed pixel have Euler angles greater than 2PI
    // if (theta1 > 6.3 || theta2 > 6.3 || theta3 > 6.3) {
    // phasesArray[n] = 0;
    // eulers = new Eulers(0.0, 0.0, 0.0);
    // } else {
    // if (phases.length == 1)
    // phasesArray[n] = 1;
    // else
    // phasesArray[n] = (byte) Integer.parseInt(line[7]);
    //
    // eulers = new Eulers(theta1, theta2, theta3);
    // }
    //
    // euler1[n] = (float) eulers.theta1;
    // euler2[n] = (float) eulers.theta2;
    // euler3[n] = (float) eulers.theta3;
    //
    // Quaternion rotation = new Quaternion(eulers);
    //
    // q0[n] = (float) rotation.getQ0();
    // q1[n] = (float) rotation.getQ1();
    // q2[n] = (float) rotation.getQ2();
    // q3[n] = (float) rotation.getQ3();
    //
    // n++;
    // iLine++;
    //
    // if (n == size) // All data is read
    // break;
    // }
    //
    // reader.close();
    //
    // // Set the maps
    // HashMap<String, Map> mapList = new HashMap<String, Map>();
    //
    // mapList.put(EbsdMMap.Q0, q0Map);
    // mapList.put(EbsdMMap.Q1, q1Map);
    // mapList.put(EbsdMMap.Q2, q2Map);
    // mapList.put(EbsdMMap.Q3, q3Map);
    // mapList.put(EbsdMMap.PHASES, new PhaseMap(width, height, phasesArray,
    // phases));
    //
    // mapList.put(TslMMap.EULER1, euler1Map);
    // mapList.put(TslMMap.EULER2, euler2Map);
    // mapList.put(TslMMap.EULER3, euler3Map);
    //
    // mapList.put(TslMMap.IMAGE_QUALITY, iqMap);
    // mapList.put(TslMMap.CONFIDENCE_INDEX, ciMap);
    //
    // return mapList;
    // }

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
