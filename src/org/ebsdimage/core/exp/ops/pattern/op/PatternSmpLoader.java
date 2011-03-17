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

import org.ebsdimage.core.exp.Exp;
import org.ebsdimage.io.SmpInputStream;
import org.simpleframework.xml.Attribute;

import rmlimage.core.ByteMap;
import rmlshared.io.FileUtil;

/**
 * Operation to load a pattern from a SMP file.
 * 
 * @author Philippe T. Pinard
 */
public class PatternSmpLoader extends PatternOp {

    /** Directory of the SMP file. */
    @Attribute(name = "dir")
    public final String filedir;

    /** File name to the SMP file. */
    @Attribute(name = "filename")
    public final String filename;



    /**
     * Creates a new <code>PatternSmpLoader</code> from the specified file path.
     * *
     * 
     * @param startIndex
     *            first index of the patterns
     * @param size
     *            number of patterns
     * @param filepath
     *            file path of the SMP file
     */
    public PatternSmpLoader(int startIndex, int size, File filepath) {
        super(startIndex, size);

        if (filepath == null)
            throw new NullPointerException("File path cannot be null.");

        // File dir
        String filedir = filepath.getParent();

        if (filedir == null)
            this.filedir = "";
        else
            this.filedir = filedir;

        // Filename
        String filename = filepath.getName();
        if (filename == null)
            throw new NullPointerException("File name cannot be null.");

        if (!FileUtil.getExtension(filepath).equalsIgnoreCase("smp"))
            throw new IllegalArgumentException(
                    "The extension of the file name must be \"SMP\"");

        this.filename = filename;
    }



    /**
     * Creates a new <code>PatternSmpLoader</code> from the specified file
     * directory and filename.
     * 
     * @param startIndex
     *            first index of the patterns
     * @param size
     *            number of patterns
     * @param filedir
     *            directory of the SMP file
     * @param filename
     *            file name of the SMP file
     * @throws NullPointerException
     *             if the file directory is null
     * @throws NullPointerException
     *             if the file name is null
     * @throws IllegalArgumentException
     *             if the extension of the file name is not SMP.
     */
    public PatternSmpLoader(@Attribute(name = "startIndex") int startIndex,
            @Attribute(name = "size") int size,
            @Attribute(name = "dir") String filedir,
            @Attribute(name = "filename") String filename) {
        this(startIndex, size, new File(filedir, filename));
    }



    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj))
            return false;

        PatternSmpLoader other = (PatternSmpLoader) obj;
        if (!filedir.equals(other.filedir))
            return false;
        if (!filename.equals(other.filename))
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
            throw new IllegalArgumentException("The split size (" + size
                    + ") is greater than the size of this operation ("
                    + this.size + ").");

        return new PatternSmpLoader(startIndex, size, filedir, filename);
    }



    /**
     * Searches for the SMP file in the specified directory or the working
     * directory of the experiment. Returns the file or throws an
     * <code>IOException</code> if the file cannot be found.
     * 
     * @param dir
     *            other directory to look for the SMP file
     * @return file for the SMP
     * @throws IOException
     *             if the file cannot be found
     */
    public File getFile(File dir) throws IOException {
        File file = new File(filedir, filename);

        if (!file.exists()) {
            file = new File(dir, filename);

            if (!file.exists())
                throw new IOException("Cannot find file (" + filename
                        + ") in the specified directory or working directory.");
        }

        return file;
    }



    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((filedir == null) ? 0 : filedir.hashCode());
        result =
                prime * result + ((filename == null) ? 0 : filename.hashCode());
        return result;
    }



    /**
     * Returns a pattern from the SMP file using the specified index.
     * 
     * @param exp
     *            experiment executing this method
     * @param index
     *            index of the pattern to load
     * @return the pattern maps
     */
    @Override
    public ByteMap load(Exp exp, int index) {
        File file;
        try {
            file = getFile(exp.getDir());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        SmpInputStream reader;
        try {
            reader = new SmpInputStream(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Assert index
        if (index < startIndex)
            throw new IllegalArgumentException("Index (" + index
                    + ") cannot be less than the start index (" + startIndex
                    + ").");
        if (index < reader.getStartIndex())
            throw new IllegalArgumentException("Index (" + index
                    + ") is less than the start index of the SMP file ("
                    + reader.getStartIndex() + ").");
        if (index > reader.getEndIndex())
            throw new IllegalArgumentException("Index (" + index
                    + ") is greater than the end index of the SMP file ("
                    + reader.getEndIndex() + ").");

        // Read pattern
        ByteMap patternMap;
        try {
            patternMap = (ByteMap) reader.readMap(index);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        // Calibrate diffraction pattern based on camera
        patternMap.setCalibration(exp.mmap.getMicroscope().getCamera().getCalibration(
                patternMap.width, patternMap.height));

        return patternMap;
    }



    @Override
    public String toString() {
        return "Pattern Smp Loader [startIndex=" + startIndex + ", size="
                + size + ", filedir=" + filedir + ", filename=" + filename
                + "]";
    }
}
