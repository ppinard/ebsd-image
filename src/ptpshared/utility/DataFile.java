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
package ptpshared.utility;

import java.util.ArrayList;

/**
 * Store the data and header from a data file.
 * 
 * @author Philippe T. Pinard
 */
public class DataFile {

    /** Lines from the header. */
    private ArrayList<String[]> headerLines;

    /** Lines from the data. */
    private ArrayList<String[]> dataLines;



    /**
     * Creates a new data file from the header and data lines.
     * 
     * @param headerLines
     *            lines of the header
     * @param dataLines
     *            lines of the data
     */
    public DataFile(ArrayList<String[]> headerLines,
            ArrayList<String[]> dataLines) {
        if (headerLines == null)
            throw new NullPointerException("Header lines cannot be null.");
        if (dataLines == null)
            throw new NullPointerException("Data lines cannot be null.");

        this.headerLines = headerLines;
        this.dataLines = dataLines;
    }



    /**
     * Returns the header lines.
     * 
     * @return header lines
     */
    public ArrayList<String[]> getHeaderLines() {
        return headerLines;
    }



    /**
     * Returns the data lines.
     * 
     * @return data lines
     */
    public ArrayList<String[]> getDataLines() {
        return dataLines;
    }



    /**
     * Removes the empty columns corresponding to many white spaces between two
     * values.
     * 
     * @param columns
     *            a line from the reader
     * @return an array with only values
     */
    public static String[] removeEmptyColumns(String[] columns) {
        ArrayList<String> newColumns = new ArrayList<String>();

        for (String column : columns)
            if (column.length() > 0)
                newColumns.add(column.trim());

        return newColumns.toArray(new String[newColumns.size()]);
    }

}
