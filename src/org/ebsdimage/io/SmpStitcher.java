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
package org.ebsdimage.io;

import java.awt.Point;
import java.io.File;
import java.io.IOException;

import rmlimage.core.ByteMap;
import rmlimage.core.ROI;
import rmlimage.module.stitch.Tessera;
import rmlshared.ui.Monitorable;

/**
 * Stitch smp files together.
 * 
 * @author Philippe T. Pinard
 */
public class SmpStitcher implements Monitorable {

    /** Progress tracking variable. */
    private double progress = 0;

    /** Progress status. */
    private String status = "";

    /** Flag indicating if the operation should be interrupted. */
    protected boolean isInterrupted = false;

    /** Regions of interest. */
    private final ROI[] rois;

    /** Smps. */
    private final SmpInputStream[] smps;

    /** Width of the final map. */
    public final int width;

    /** Height of the final map. */
    public final int height;



    /**
     * Creates a new <code>SmpMerge</code> to stitch the specified smp files
     * into one smp file according to the defined regions of interest.
     * 
     * @param tesserae
     *            array of <code>Tessera</code> containing each smp file and roi
     *            of the stitch map
     * @param width
     *            width of the final stitch map
     * @param height
     *            height of the final stitch map
     * @throws IOException
     *             if an error occurs while loading the smp file
     */
    public SmpStitcher(Tessera[] tesserae, int width, int height)
            throws IOException {
        if (tesserae.length == 0)
            throw new IllegalArgumentException(
                    "Specifigy at least one project.");
        if (width == 0)
            throw new IllegalArgumentException("Width cannot be zero.");
        if (height == 0)
            throw new IllegalArgumentException("Height cannot be zero.");

        this.width = width;
        this.height = height;

        rois = new ROI[tesserae.length];
        smps = new SmpInputStream[tesserae.length];

        for (int i = 0; i < tesserae.length; i++) {
            smps[i] = new SmpInputStream(tesserae[i].file);
            rois[i] = tesserae[i].roi;
        }
    }



    /**
     * Closes all smp input streams.
     * 
     * @throws IOException
     *             if a stream cannot be close
     */
    public void close() throws IOException {
        for (SmpInputStream smp : smps)
            smp.close();
    }



    /**
     * Returns an empty pattern for pixels that don't correspond to any region
     * of interest.
     * 
     * @return <code>ByteMap</code> of an empty pattern
     */
    private ByteMap getEmptyPattern() {
        int width = smps[0].getMapWidth();
        int height = smps[0].getMapHeight();

        ByteMap map = new ByteMap(width, height);
        map.clear(0);

        return map;
    }



    /**
     * Returns the index of map containing the coordinate x and y. A value of
     * <code>-1</code> is returned if the coordinate doesn't belong to any map.
     * Note that the first map containing x and y is returned.
     * 
     * @param x
     *            absolute x position of the desired pattern in the stitched map
     * @param y
     *            absolute y position of the desired pattern in the stitched map
     * @return index of map containing x and y, or -1 if no map is found
     */
    private int getMapIndex(int x, int y) {
        for (int i = 0; i < rois.length; i++) {
            if (rois[i].isInside(x, y))
                return i;
        }

        return -1;
    }



    /**
     * Returns the pattern at position x, y of the specified map.
     * 
     * @param x
     *            absolute x position of the desired pattern in the stitched map
     * @param y
     *            absolute y position of the desired pattern in the stitched map
     * @param mapIndex
     *            index of the map where to extract the pattern
     * @return <code>ByteMap</code> of the pattern
     * @throws IOException
     *             if an error occurs while loading the specified pattern
     */
    private ByteMap getPattern(int x, int y, int mapIndex) throws IOException {
        Point relPos = getRelativePosition(x, y, rois[mapIndex]);
        int index = relPos.y * rois[mapIndex].width + relPos.x;

        SmpInputStream smp = smps[mapIndex];

        ByteMap map = (ByteMap) smp.readMap(index);

        return map;
    }



    /**
     * Returns the relation position of the absolute coordinate x and y with
     * respect to their region of interest.
     * 
     * @param x
     *            absolute x position of the desired pattern in the stitched map
     * @param y
     *            absolute y position of the desired pattern in the stitched map
     * @param roi
     *            region of interest which contains coordinates x and y
     * @return relative position of x and y with respect to the ROI
     * @throws IllegalArgumentException
     *             if the x and y are outside the region of interest
     */
    private Point getRelativePosition(int x, int y, ROI roi) {
        int newX = x - roi.x;
        int newY = y - roi.y;

        if (newX < 0)
            throw new IllegalArgumentException("Relative x position (" + newX
                    + ") is less than zero.");
        if (newY < 0)
            throw new IllegalArgumentException("Relative y position (" + newY
                    + ") is less than zero.");

        return new Point(newX, newY);
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
     * Interrupts the experiment.
     */
    public synchronized void interrupt() {
        isInterrupted = true;
    }



    /**
     * Checks if the experiment should be interrupted. This method must be
     * synchronized because interrupt() may be called from any thread.
     * 
     * @return <code>true</code> if the operation is interrupted,
     *         <code>false</code> otherwise
     */
    protected synchronized boolean isInterrupted() {
        return isInterrupted;
    }



    /**
     * Stitches the smp files into one smp file.
     * 
     * @param outputFile
     *            output smp file
     * @throws IOException
     *             if an error occurs while loading or saving smp files
     */
    public void stitch(File outputFile) throws IOException {
        SmpOutputStream output = new SmpOutputStream(outputFile);

        int size = width * height;
        int count = 0;

        for (int y = 0; y < height; y++) {
            // Update progress
            progress = (double) count / size;
            status = "Stitching image " + count + " out of " + size;

            for (int x = 0; x < width; x++) {
                // Interrupt
                if (isInterrupted())
                    break;

                // Coordinates corresponds to which map
                int mapIndex = getMapIndex(x, y);

                // Get corresponding map
                ByteMap map;
                if (mapIndex < 0)
                    map = getEmptyPattern();
                else
                    map = getPattern(x, y, mapIndex);

                // Put map in output smp
                output.writeMap(map);

                count++;
            }

            // Interrupt
            if (isInterrupted())
                break;
        }

        output.close();
    }
}