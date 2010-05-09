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

import rmlimage.io.MapLoader;
import rmlimage.module.integer.core.IntMap;
import rmlshared.io.FileUtil;

public class RawLoader extends MapLoader {

    private double progress = 0;



    public Dimension getDimension(File file) throws IOException {
        RplFile rplFile = new RplFile(FileUtil.setExtension(file, "rpl"));
        rplFile.load();

        return new Dimension(rplFile.getWidth(), rplFile.getHeight());
    }



    public double getTaskProgress() {
        return progress;
    }



    public Object load(File file) throws IOException {
        return load(file, (IntMap) null);
    }



    public Object load(File file, Object arg) throws IOException {
        if (!(arg instanceof IntMap))
            arg = null;

        return load(file, (IntMap) arg);
    }



    public IntMap load(File file, IntMap map) throws IOException {
        // Load the raw image properties
        RplFile rplFile = new RplFile(FileUtil.setExtension(file, "rpl"));
        rplFile.load();
        progress = 0.25;

        // Get the size of the raw image
        int width = rplFile.getWidth();
        int height = rplFile.getHeight();
        int size = width * height;

        // Get the number of bytes per pixel
        int dataLength = rplFile.getDataLength();

        // Get whether the data is signed or unsigned
        boolean isSigned = rplFile.isSigned();

        // Get whether the date is coded in big or little endian
        boolean isBigEndian = rplFile.isBigEndian();

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
            } else // If unsigned
            {
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
                } else // If unsigned
                {
                    for (int n = 0; n < size; n++)
                        pixArray[n] = (in.read() << 8) | in.read();
                }
            } else // If little endian
            {
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
                } else // If unsigned
                {
                    for (int n = 0; n < size; n++)
                        pixArray[n] =
                                (in.read() & 0xff) | ((in.read() & 0xff) << 8);
                }
            }
        }

        in.close();

        map.setFile(file);
        map.setChanged(IntMap.MAP_CHANGED);

        loadProperties(map); // Load the properties if anu

        progress = 1; // Set progress as finished

        map.shouldSave(false); // No need to save the file, it was just loaded

        return map;
    }

}
