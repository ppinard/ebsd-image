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

import java.awt.Dimension;
import java.io.*;

import rmlimage.core.Map;
import rmlimage.io.MapLoader;
import rmlimage.module.integer.core.IntMap;
import rmlshared.io.FileUtil;

/**
 * Loader for RAW file.
 * 
 * @author ppinard
 */
public class RawLoader extends MapLoader {

    /** Track progress. */
    private double progress = 0;



    @Override
    public boolean canLoad(File file) {
        return getValidationMessage(file).isEmpty();
    }



    /**
     * Returns the validation message indication if the specified file is a
     * valid <code>RAW</code> file. An empty string if the file is valid. An
     * error message otherwise.
     * <p>
     * To be valid:
     * <ul>
     * <li>File must have a RAW extension</li>
     * <li>A RPL file must accompany the RAW file</li>
     * </ul>
     * 
     * @param file
     *            a file of a map
     * @return error message or empty string
     */
    protected String getValidationMessage(File file) {
        if (!FileUtil.getExtension(file).equalsIgnoreCase("raw"))
            return "The extension (" + FileUtil.getExtension(file)
                    + " ) must be RAW.";

        File rplFile = FileUtil.setExtension(file, "rpl");
        if (!rplFile.exists())
            return "The RAW file must be accompanied by a RPL file.";

        return "";
    }



    /**
     * Returns the dimension of the map based on the properties of the RPL file
     * attached with the RAW file.
     * 
     * @param file
     *            path to the RAW file
     * @return dimension of the map
     * @throws IOException
     *             if an error occurs while reading the RPL file
     */
    public Dimension getDimension(File file) throws IOException {
        File rplFile = FileUtil.setExtension(file, "rpl");
        RplFile rpl = new RplLoader().load(rplFile);

        return new Dimension(rpl.width, rpl.height);
    }



    @Override
    public double getTaskProgress() {
        return progress;
    }



    @Override
    public IntMap load(File file) throws IOException {
        return load(file, (IntMap) null);
    }



    /**
     * Loads a RAW file in a <code>IntMap</code>. The RAW file must be
     * accompanied by a RPL file.
     * 
     * @param file
     *            RAW file
     * @param map
     *            map where the data will be saved. Can be <code>null</code>.
     * @return a <code>IntMap</code>
     * @throws IOException
     *             if an error occurs while reading the RAW or RPL file
     */
    public IntMap load(File file, IntMap map) throws IOException {
        if (!canLoad(file))
            throw new IOException(getValidationMessage(file));

        // Load the raw image properties
        File rplFile = FileUtil.setExtension(file, "rpl");
        RplFile rpl = new RplLoader().load(rplFile);
        progress = 0.25;

        // Get the size of the raw image
        int width = rpl.width;
        int height = rpl.height;
        int size = width * height;

        // Get the number of bytes per pixel
        int dataLength = rpl.dataLength;

        // Get whether the data is signed or unsigned
        boolean isSigned = rpl.isSigned();

        // Get whether the date is coded in big or little endian
        boolean isBigEndian = rpl.isBigEndian();

        // If the map is not defined in the argument, create one
        if (map == null)
            map = new IntMap(width, height);

        // If the map specified in the argument is not the correct size
        // create a new one
        if (map.width != width || map.height != height)
            map = new IntMap(width, height);

        int[] pixArray = map.pixArray; // For performance

        // Read the map data
        InputStream in = new BufferedInputStream(new FileInputStream(file));
        if (dataLength == 1) {
            if (isSigned) {
                int pix;
                for (int n = 0; n < size; n++) {
                    pix = in.read();
                    // Propagate the sign bit if it is set
                    if (pix >= 0x80)
                        pixArray[n] = pix | 0xffffff00;
                    else
                        pixArray[n] = pix;
                }
            } else { // If unsigned
                for (int n = 0; n < size; n++)
                    pixArray[n] = in.read();
            }
        }

        if (dataLength == 2) {
            if (isBigEndian) {
                if (isSigned) {
                    int pix;
                    for (int n = 0; n < size; n++) {
                        pix = (in.read() << 8) | in.read();
                        // Propagate the sign bit if it is set
                        if (pix >= 0x8000)
                            pixArray[n] = pix | 0xffff0000;
                        else
                            pixArray[n] = pix;
                    }
                } else { // If unsigned
                    for (int n = 0; n < size; n++)
                        pixArray[n] = (in.read() << 8) | in.read();
                }
            } else { // If little endian
                if (isSigned) {
                    int pix;
                    for (int n = 0; n < size; n++) {
                        pix = in.read() | (in.read() << 8);
                        // Propagate the sign bit if it is set
                        if (pix >= 0x8000)
                            pixArray[n] = pix | 0xffff0000;
                        else
                            pixArray[n] = pix;
                    }
                } else { // If unsigned
                    for (int n = 0; n < size; n++)
                        pixArray[n] =
                                (in.read() & 0xff) | ((in.read() & 0xff) << 8);
                }
            }
        }

        in.close();

        map.setFile(file);
        map.setChanged(Map.MAP_CHANGED);

        loadProperties(map); // Load the properties if anu

        progress = 1; // Set progress as finished

        map.shouldSave(false); // No need to save the file, it was just loaded

        return map;
    }



    @Override
    public Object load(File file, Object arg) throws IOException {
        if (!(arg instanceof IntMap))
            arg = null;

        return load(file, (IntMap) arg);
    }

}
