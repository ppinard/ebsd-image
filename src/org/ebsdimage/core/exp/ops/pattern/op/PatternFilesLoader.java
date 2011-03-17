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
package org.ebsdimage.core.exp.ops.pattern.op;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.ebsdimage.core.exp.Exp;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementArray;

import rmlimage.core.ByteMap;
import rmlimage.core.Map;
import rmlimage.core.RGBMap;
import rmlimage.core.Transform;
import rmlimage.io.IO;

/**
 * Operation to load a pattern from a file.
 * 
 * @author Philippe T. Pinard
 */
public class PatternFilesLoader extends PatternOp {

    /** List of files. */
    private File[] files;



    /**
     * Creates a new <code>PatternFileLoader</code> from the specified file
     * directory and name.
     * 
     * @param index
     *            index of the pattern in a map
     * @param filepath
     *            file path of the pattern
     * @throws NullPointerException
     *             if the file path is null
     * @throws NullPointerException
     *             if the name in the file path is null
     */
    public PatternFilesLoader(int index, File filepath) {
        super(index, 1);

        if (filepath == null)
            throw new NullPointerException("File path cannot be null.");

        files = new File[] { filepath };
    }



    /**
     * Creates a new <code>PatternFilesLoader</code> with the list of files. The
     * start index is assigned the first file in the array.
     * 
     * @param startIndex
     *            index of the first pattern
     * @param files
     *            list of files
     * @throws NullPointerException
     *             if a file in the array is null
     */
    public PatternFilesLoader(int startIndex, File[] files) {
        super(startIndex, files.length);

        this.files = new File[files.length];

        for (int i = 0; i < files.length; i++) {
            if (files[i] == null)
                throw new NullPointerException("File (" + i + ") is null.");
            this.files[i] = files[i];
        }
    }



    /**
     * Creates a new <code>PatternFilesLoader</code> with the list of files. The
     * start index is assigned the first file in the array.
     * 
     * @param startIndex
     *            index of the first pattern
     * @param filePaths
     *            list of file paths
     * @throws NullPointerException
     *             if a file path in the array is null
     */
    public PatternFilesLoader(@Attribute(name = "startIndex") int startIndex,
            @ElementArray(name = "files") String[] filePaths) {
        super(startIndex, filePaths.length);

        files = new File[filePaths.length];

        for (int i = 0; i < filePaths.length; i++) {
            if (filePaths[i] == null)
                throw new NullPointerException("File (" + i + ") is null.");
            files[i] = new File(filePaths[i]);
        }
    }



    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj))
            return false;

        PatternFilesLoader other = (PatternFilesLoader) obj;
        if (!Arrays.equals(files, other.files))
            return false;

        return true;
    }



    @Override
    public PatternOp extract(int startIndex, int endIndex) {
        if (startIndex < this.startIndex)
            throw new IllegalArgumentException("Specified start index ("
                    + startIndex + ") is less than the index of the"
                    + " first pattern in this operation (" + this.startIndex
                    + ").");

        int size = endIndex - startIndex + 1;

        if (size > this.size)
            throw new IllegalArgumentException("The split (" + size
                    + ") goes outside the size of this operation (" + this.size
                    + ").");

        File[] files = new File[size];
        System.arraycopy(this.files, startIndex, files, 0, size);

        return new PatternFilesLoader(startIndex, files);
    }



    /**
     * Returns the path of all the defined pattern files.
     * 
     * @return path of files
     */
    @ElementArray(name = "files")
    public String[] getFilePaths() {
        String[] filePaths = new String[files.length];

        for (int i = 0; i < files.length; i++)
            filePaths[i] = files[i].getPath();

        return filePaths;
    }



    /**
     * Returns the array of the defined files.
     * 
     * @return array of file inside the loader
     */
    public File[] getFiles() {
        return files;
    }



    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + Arrays.hashCode(files);
        return result;
    }



    /**
     * Load a pattern from the specified file path.
     * 
     * @param index
     *            index of the pattern
     * @param exp
     *            experiment executing this method
     * @return the pattern map
     */
    @Override
    public ByteMap load(Exp exp, int index) {
        // Assert index
        if (index < startIndex)
            throw new IllegalArgumentException("Index (" + index
                    + ") cannot be less than the start index (" + startIndex
                    + ").");
        if (index > startIndex + size)
            throw new IllegalArgumentException("Index (" + index
                    + ") is greater than the last index (" + startIndex + size
                    + ").");

        // Get file
        File file = files[index - startIndex];
        if (!file.exists()) {
            file = new File(exp.getDir(), file.getName());

            if (!file.exists())
                throw new IllegalArgumentException("Cannot find file ("
                        + file.getName()
                        + ") in the specified directory or working directory.");
        }

        /* Load pattern map */
        Map map;
        try {
            map = IO.load(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Calibrate diffraction pattern based on camera
        map.setCalibration(exp.mmap.getMicroscope().getCamera().getCalibration(
                map.width, map.height));

        if (map instanceof ByteMap)
            return (ByteMap) map;
        else if (map instanceof RGBMap)
            return Transform.getBlueLayer((RGBMap) map);
        else
            throw new RuntimeException("Cannot convert map to ByteMap.");
    }



    @Override
    public String toString() {
        return "Pattern Files Loader [startIndex=" + startIndex + ", size="
                + size + "]";
    }

}
