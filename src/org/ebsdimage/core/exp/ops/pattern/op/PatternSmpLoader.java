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

import rmlimage.core.ByteMap;

/**
 * Operation to load a pattern from a smp file.
 * 
 * @author Philippe T. Pinard
 * 
 */
public class PatternSmpLoader extends PatternOp {

    /** Directory of the smp file. */
    public final String filedir;

    /** File name to the smp file. */
    public final String filename;



    /**
     * Creates a new <code>PatternSmpLoader</code> with an empty file directory
     * and file name.
     */
    public PatternSmpLoader() {
        filename = "";
        filedir = "";
    }



    /**
     * Creates a new <code>PatternSmpLoader</code> from the specified file
     * directory and filename.
     * 
     * @param index
     *            index of the pattern in the smp file and the map
     * @param filedir
     *            directory of the smp file
     * @param filename
     *            file name of the smp file
     * 
     * @throws NullPointerException
     *             if the file directory is null
     * @throws NullPointerException
     *             if the file name is null
     * @throws IllegalArgumentException
     *             if the extension of the file name is not smp.
     */
    public PatternSmpLoader(int index, String filedir, String filename) {
        super(index);

        if (filedir == null)
            throw new NullPointerException("File directory cannot be null.");
        if (filename == null)
            throw new NullPointerException("File name cannot be null.");
        if (!filename.endsWith(".smp"))
            throw new IllegalArgumentException(
                    "The extension of the file name must be \"smp\"");

        this.filedir = filedir;
        this.filename = filename;
    }



    /**
     * Creates a new <code>PatternSmpLoader</code> from the specified file path.
     * *
     * 
     * @param index
     *            index of the pattern to load from the smp file
     * @param filepath
     *            file path of the smp file
     */
    public PatternSmpLoader(int index, File filepath) {
        super(index);

        if (filepath == null)
            throw new NullPointerException("File path cannot be null.");

        String filedir = filepath.getParent();
        if (filedir == null)
            this.filedir = "";
        else
            this.filedir = filedir;

        String filename = filepath.getName();
        if (filename == null)
            throw new NullPointerException("File name cannot be null.");

        if (!filename.endsWith(".smp"))
            throw new IllegalArgumentException(
                    "The extension of the file name must be \"smp\"");

        this.filename = filename;
    }



    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;

        PatternSmpLoader other = (PatternSmpLoader) obj;
        if (!filedir.equals(other.filedir))
            return false;
        if (!filename.equals(other.filename))
            return false;

        return true;
    }



    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + filedir.hashCode();
        result = prime * result + filename.hashCode();
        return result;
    }



    /**
     * Returns a pattern from the smp file using the specified index.
     * 
     * @param exp
     *            experiment executing this method
     * @return the pattern maps
     * @throws IOException
     *             if an error occurs while opening the smp file
     */
    @Override
    public ByteMap load(Exp exp) throws IOException {
        File file = new File(filedir, filename);
        if (!file.exists()) {
            file = new File(exp.getDir(), filename);

            if (!file.exists())
                throw new IOException("Cannot find file (" + filename
                        + ") in the specified directory or working directory.");
        }

        /* Load pattern map with index */
        SmpInputStream reader = new SmpInputStream(file);
        ByteMap patternMap = (ByteMap) reader.readMap(index);
        reader.close();

        return patternMap;
    }



    @Override
    public String toString() {
        return "Pattern Smp Loader [index=" + index + ", filedir=" + filedir
                + ", filename=" + filename + "]";
    }

}
