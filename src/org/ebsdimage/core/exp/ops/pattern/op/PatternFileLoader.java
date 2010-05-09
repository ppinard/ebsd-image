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

import rmlimage.core.ByteMap;
import rmlimage.core.Map;
import rmlimage.core.RGBMap;
import rmlimage.core.Transform;
import rmlimage.io.IO;

/**
 * Operation to load a pattern from a file.
 * 
 * @author Philippe T. Pinard
 * 
 */
public class PatternFileLoader extends PatternOp {

    /** Directory of the pattern file. */
    public final String filedir;

    /** File name to the pattern file. */
    public final String filename;



    /**
     * Creates a new <code>PatternLoader</code> with an empty file directory and
     * name.
     */
    public PatternFileLoader() {
        filedir = "";
        filename = "";
    }



    /**
     * Creates a new <code>PatternLoad</code> from the specified file directory
     * and file name.
     * 
     * @param index
     *            index of the pattern in a map
     * @param filedir
     *            directory of the pattern file
     * @param filename
     *            file name of the pattern file
     * 
     * @throws NullPointerException
     *             if the file directory is null
     * @throws NullPointerException
     *             if the file name is null
     */
    public PatternFileLoader(int index, String filedir, String filename) {
        super(index);

        if (filedir == null)
            throw new NullPointerException("File directory cannot be null.");
        if (filename == null)
            throw new NullPointerException("File name cannot be null.");

        this.filedir = filedir;
        this.filename = filename;
    }



    /**
     * Creates a new <code>PatternFileLoader</code> from the specified file
     * directory and name.
     * 
     * @param index
     *            index of the pattern in a map
     * @param filepath
     *            file path of the pattern
     * 
     * @throws NullPointerException
     *             if the file path is null
     * @throws NullPointerException
     *             if the name in the file path is null
     */
    public PatternFileLoader(int index, File filepath) {
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
        else
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

        PatternFileLoader other = (PatternFileLoader) obj;
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
     * Load a pattern from the specified file path.
     * 
     * @param exp
     *            experiment executing this method
     * @return the pattern map
     * @throws IOException
     *             if an error occurs while loading the pattern from the file
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

        /* Load pattern map */
        Map map = IO.load(file);

        if (map instanceof ByteMap)
            return (ByteMap) map;
        else if (map instanceof RGBMap)
            return Transform.getBlueLayer((RGBMap) map);
        else
            throw new RuntimeException("Cannot convert map to ByteMap.");
    }



    @Override
    public String toString() {
        return "Pattern File Loader [index=" + index + ", filedir=" + filedir
                + ", filename=" + filename + "]";
    }

}
