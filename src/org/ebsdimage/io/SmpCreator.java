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
package org.ebsdimage.io;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Arrays;

import rmlimage.core.Map;
import rmlshared.io.FileUtil;
import rmlshared.ui.Monitorable;

/**
 * Utility class used to easily create <code>SMP</code> files.
 * 
 * @author Marin Lagac&eacute;
 */
public class SmpCreator implements Monitorable {

    /** Progress status. */
    private String status = "";

    /** Progress value. */
    private double progress = 0;

    /** Flag indicating if the operation should be interrupted. */
    private boolean isInterrupted = false;



    /**
     * Create an <code>SMP</code> file from <b>all</b> the files in the
     * specified directory. The <code>Map</code>s will be saved by alphabetical
     * order in the file.
     * 
     * @param smpFile
     *            name of the <code>SMP</code> file to create
     * @param directory
     *            directory holding all the files to save to the
     *            <code>SMP</code> file
     * @throws IOException
     *             if <code>directory</code> does not exist or any of the files
     *             in the directory does not hold a <code>Map</code>.
     * @throws IllegalArgumentException
     *             if the <code>Map</code>'s dimensions of any of the files are
     *             not the same as the other <code>Map</code>s previously saved
     *             to the <code>SMP</code> file.
     * @throws IllegalArgumentException
     *             if the <code>Map</code> of any of the files is not of the
     *             same type as the other <code>Map</code>s previously saved to
     *             the <code>SMP</code> file.
     * @throws IOException
     *             if an error occurred while saving any of the <code>Map</code>
     *             to disk.
     */
    public void create(File smpFile, File directory) throws IOException {
        // File[] files = directory.listFiles();
        FileFilter filter = new ImagesFileFilter();
        File[] files = FileUtil.listFiles(directory, filter);
        Arrays.sort(files);
        create(smpFile, files);
    }



    /**
     * Create an <code>SMP</code> file from a list of <code>Map</code> files.
     * The order of the <code>Map</code>s in the file will be the same as the
     * one in the list.
     * 
     * @param smpFile
     *            name of the <code>SMP</code> file to create
     * @param files
     *            list of files to save to the <code>SMP</code> file
     * @throws IOException
     *             if any of the files does not hold a <code>Map</code>.
     * @throws IllegalArgumentException
     *             if the <code>Map</code>'s dimensions of any of the files are
     *             not the same as the other <code>Map</code>s previously saved
     *             to the <code>SMP</code> file.
     * @throws IllegalArgumentException
     *             if the <code>Map</code> of any of the files is not of the
     *             same type as the other <code>Map</code>s previously saved to
     *             the <code>SMP</code> file.
     * @throws IOException
     *             if an error occurred while saving any of the <code>Map</code>
     *             to disk.
     */
    public void create(File smpFile, File[] files) throws IOException {
        smpFile = FileUtil.setExtension(smpFile, "smp");

        SmpOutputStream outStream = new SmpOutputStream(smpFile);

        Map map;
        for (int n = 0; n < files.length; n++) {
            // Progress
            status = "Saving " + files[n].getName();
            progress = (double) n / files.length;

            // Interrupt
            if (isInterrupted())
                break;

            map = rmlimage.io.IO.load(files[n]);
            outStream.writeMap(map);
        }

        outStream.close();

        status = "";
        progress = 0;
    }



    /**
     * Extracts a series of contiguous <code>Map</code> from a <code>SMP</code>
     * file and save them in another <code>SMP</code> file.
     * 
     * @param srcSmpFile
     *            <code>SMP</code> file to extract the <code>Map</code>s from
     * @param startIndex
     *            index of the first <code>Map</code> to extract from
     *            <code>srcSmpFile</code>
     * @param endIndex
     *            index of the last <code>Map</code> to extract from
     *            <code>srcSmpFile</code> (inclusive)
     * @param destSmpFile
     *            <code>SMP</code> file in which to put the extracted
     *            <code>Map</code>s. If the file already exists, it will be
     *            overwritten without warning.
     * @throws IOException
     *             if an error occurred during the extraction
     */
    public void extract(File srcSmpFile, int startIndex, int endIndex,
            File destSmpFile) throws IOException {
        SmpInputStream inStream = new SmpInputStream(srcSmpFile);
        SmpOutputStream outStream =
                new SmpOutputStream(destSmpFile, startIndex);

        // Transfert the first map to get the proper map type and size
        Map map = inStream.readMap(startIndex);
        outStream.writeMap(map);

        // Transfert the rest of the maps
        for (int n = startIndex + 1; n <= endIndex; n++) {
            inStream.readMap(n, map);
            outStream.writeMap(map);
        }

        inStream.close();
        outStream.close();
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
