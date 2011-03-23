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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import org.apache.commons.math.geometry.CardanEulerSingularityException;
import org.apache.commons.math.geometry.Rotation;
import org.ebsdimage.core.Microscope;
import org.ebsdimage.vendors.hkl.core.HklMMap;
import org.ebsdimage.vendors.hkl.core.HklMetadata;

import ptpshared.geom.RotationUtils;
import rmlimage.core.Calibration;
import rmlshared.io.FileUtil;
import rmlshared.io.Saver;
import crystallography.core.Crystal;
import crystallography.core.UnitCell;

/**
 * Export a <code>HklMMap</code> back to a ctf file.
 * 
 * @author Philippe T. Pinard
 */
public class CtfSaver implements Saver {

    /** Progress value. */
    private double progress = 0.0;



    /**
     * Creates the line for the acquisition eulers.
     * 
     * @param mmap
     *            <code>HklMMap</code> to be saved
     * @return a line
     */
    private String createAcqEulersLines(HklMMap mmap) {
        Rotation sampleRotation = mmap.getMicroscope().getSampleRotation();

        double[] eulers;
        try {
            eulers = RotationUtils.getBungeEulerAngles(sampleRotation);
        } catch (CardanEulerSingularityException e) {
            eulers = new double[] { 0.0, 0.0, 0.0 };
        }

        return "AcqE1" + "\t" + formatDouble(eulers[0]) + "\n" + "AcqE2" + "\t"
                + formatDouble(eulers[1]) + "\n" + "AcqE3" + "\t"
                + formatDouble(eulers[2]);
    }



    /**
     * Creates the line for the author.
     * 
     * @param mmap
     *            <code>HklMMap</code> to be saved
     * @return a line
     */
    private String createAuthorLine(HklMMap mmap) {
        return "Author" + "\t" + "[Unknown]";
    }



    /**
     * Creates the lines for the size of the map (width and height).
     * 
     * @param mmap
     *            <code>HklMMap</code> to be saved
     * @return two lines
     */
    private String createCellsLines(HklMMap mmap) {
        return "XCells" + "\t" + mmap.width + "\n" + "YCells" + "\t"
                + mmap.height;
    }



    /**
     * Creates the line for the header of the columns.
     * 
     * @param mmap
     *            <code>HklMMap</code> to be saved
     * @return a line
     */
    private String createDataHeaderLine(HklMMap mmap) {
        return "Phase" + "\t" + "X" + "\t" + "Y" + "\t" + "Bands" + "\t"
                + "Error" + "\t" + "Euler1" + "\t" + "Euler2" + "\t" + "Euler3"
                + "\t" + "MAD" + "\t" + "BC" + "\t" + "BS";
    }



    /**
     * Creates a data line for the specified index.
     * 
     * @param mmap
     *            <code>HklMMap</code> to be saved
     * @param index
     *            index of the data
     * @return a line
     */
    private String createDataLine(HklMMap mmap, int index) {
        // Position
        double x = mmap.getCalibratedX(index).getValue("um");
        double y = mmap.getCalibratedY(index).getValue("um");

        // Phase
        int phase = (mmap.getPhaseMap().pixArray[index] & 0xff);

        double[] eulers;
        double mad;
        int error;
        if (phase > 0) {
            try {
                eulers =
                        RotationUtils.getBungeEulerAngles(mmap.getRotation(index));
            } catch (CardanEulerSingularityException e) {
                eulers = new double[] { 0.0, 0.0, 0.0 };
            }
            mad = mmap.getMeanAngularDeviationMap().pixArray[index];
            error = (mmap.getErrorMap().pixArray[index] & 0xff);
        } else {
            eulers = new double[] { 0.0, 0.0, 0.0 };
            mad = 0.0;
            error = 6;
        }

        double e1 = Math.toDegrees(eulers[0]);
        double e2 = Math.toDegrees(eulers[1]);
        double e3 = Math.toDegrees(eulers[2]);

        // Quality metrics
        int bands = (mmap.getBandCountMap().pixArray[index] & 0xff);

        int bc = (mmap.getBandContrastMap().pixArray[index] & 0xff);
        int bs = (mmap.getBandSlopeMap().pixArray[index] & 0xff);

        return phase + "\t" + formatDouble(x) + "\t" + formatDouble(y) + "\t"
                + bands + "\t" + error + "\t" + formatDouble(e1) + "\t"
                + formatDouble(e2) + "\t" + formatDouble(e3) + "\t"
                + formatDouble(mad) + "\t" + bc + "\t" + bs;
    }



    /**
     * Creates the line for the header of the ctf file.
     * 
     * @param mmap
     *            <code>HklMMap</code> to be saved
     * @return a line
     */
    private String createHeaderLine(HklMMap mmap) {
        return "Channel Text File";
    }



    /**
     * Creates the line for the job mode.
     * 
     * @param mmap
     *            <code>HklMMap</code> to be saved
     * @return a line
     */
    private String createJobModeLine(HklMMap mmap) {
        return "JobMode" + "\t" + "Grid";
    }



    /**
     * Creates the line for the magnification, beam energy and tilt angle.
     * 
     * @param mmap
     *            <code>HklMMap</code> to be saved
     * @return a line
     */
    private String createMagEnergyTiltLine(HklMMap mmap) {
        Microscope microscope = mmap.getMicroscope();

        double energy = microscope.getBeamEnergy() / 1e3;
        double tiltAngle = Math.toDegrees(microscope.getTiltAngle());

        return "Euler angles refer to Sample Coordinate system (CS0)!" + "\t"
                + "Mag" + "\t" + formatDouble(microscope.getMagnification())
                + "\t" + "Coverage" + "\t" + "100" + "\t" + "Device" + "\t"
                + "0" + "\t" + "KV" + "\t" + formatDouble(energy) + "\t"
                + "TiltAngle" + "\t" + formatDouble(tiltAngle) + "\t"
                + "TiltAxis" + "\t" + "0";
    }



    /**
     * Creates the lines for the phases.
     * 
     * @param mmap
     *            <code>HklMMap</code> to be saved
     * @return many lines
     */
    private String createPhasesLines(HklMMap mmap) {
        StringBuilder line = new StringBuilder();

        Crystal[] phases = mmap.getPhaseMap().getPhases();

        line.append("Phases" + "\t" + phases.length + "\n");

        for (Crystal crystal : phases) {
            UnitCell u = crystal.unitCell; // for clarity
            line.append(formatDouble(u.a) + ";" + formatDouble(u.b) + ";"
                    + formatDouble(u.c) + "\t"
                    + formatDouble(Math.toDegrees(u.alpha)) + ";"
                    + formatDouble(Math.toDegrees(u.beta)) + ";"
                    + formatDouble(Math.toDegrees(u.gamma)) + "\t"
                    + crystal.name + "\t" + crystal.spaceGroup.laueGroup.index
                    + "\t" + crystal.spaceGroup.index + "\n");
        }

        line.setLength(line.length() - 1);

        return line.toString();
    }



    /**
     * Creates the line for the project name and path.
     * 
     * @param mmap
     *            <code>HklMMap</code> to be saved
     * @return a line
     */
    private String createProjectLine(HklMMap mmap) {
        HklMetadata metadata = mmap.getMetadata();

        File project =
                new File(metadata.getProjectDir(), metadata.getProjectName());
        return "Prj" + "\t" + project.getAbsolutePath();
    }



    /**
     * Creates the line for the step size (in x and y).
     * 
     * @param mmap
     *            <code>HklMMap</code> to be saved
     * @return two lines
     */
    private String createStepLines(HklMMap mmap) {
        Calibration cal = mmap.getCalibration();

        double xstep = cal.getDX().getValue("um");
        double ystep = cal.getDY().getValue("um");
        return "XStep" + "\t" + formatDouble(xstep) + "\n" + "YStep" + "\t"
                + formatDouble(ystep);
    }



    /**
     * Returns a string representation of a double value formatted with the
     * correct precision.
     * 
     * @param value
     *            value to be formatted
     * @return formatted string
     */
    private String formatDouble(double value) {
        DecimalFormatSymbols symbol = new DecimalFormatSymbols();
        symbol.setDecimalSeparator('.');

        DecimalFormat threeDecimals = new DecimalFormat("0.000", symbol);
        return threeDecimals.format(value);
    }



    @Override
    public double getTaskProgress() {
        return progress;
    }



    /**
     * Exports a <code>HklMMap</code> to a ctf file.
     * 
     * @param mmap
     *            <code>HklMMap</code> to be exported
     * @param file
     *            ctf file
     * @throws IOException
     *             if an error occurs while saving
     */
    public void save(HklMMap mmap, File file) throws IOException {
        file = FileUtil.setExtension(file, "ctf");
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));

        // Header
        writer.write(createHeaderLine(mmap));
        writer.newLine();

        // Project
        writer.write(createProjectLine(mmap));
        writer.newLine();

        // Author
        writer.write(createAuthorLine(mmap));
        writer.newLine();

        // Job Mode
        writer.write(createJobModeLine(mmap));
        writer.newLine();

        // Cells
        writer.write(createCellsLines(mmap));
        writer.newLine();

        // Steps
        writer.write(createStepLines(mmap));
        writer.newLine();

        // Acquisition Eulers
        writer.write(createAcqEulersLines(mmap));
        writer.newLine();

        // Mag, Energy, Tilt
        writer.write(createMagEnergyTiltLine(mmap));
        writer.newLine();

        // Phases
        writer.write(createPhasesLines(mmap));
        writer.newLine();

        // Data header
        writer.write(createDataHeaderLine(mmap));
        writer.newLine();

        // Data
        for (int i = 0; i < mmap.size; i++) {
            progress = (double) i / (double) mmap.size;

            writer.write(createDataLine(mmap, i));
            writer.newLine();
        }

        writer.close();
    }



    /**
     * Exports a <code>HklMMap</code> to a ctf file.
     * 
     * @param obj
     *            a <code>HklMMap</code>
     * @param file
     *            ctf file
     * @throws IOException
     *             if an error occurs while saving
     */
    @Override
    public void save(Object obj, File file) throws IOException {
        save((HklMMap) obj, file);
    }



    @Override
    public boolean canSave(Object obj, String fileFormat) {
        return (obj instanceof HklMMap) && fileFormat.equalsIgnoreCase("CTF");
    }

}
