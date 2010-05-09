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
package org.ebsdimage.io.exp;

import java.io.File;
import java.io.IOException;

import org.ebsdimage.core.exp.Exp;

import ptpshared.utility.xml.ObjectXmlLoader;
import ptpshared.utility.xml.XmlLoader;
import rmlshared.io.FileUtil;
import rmlshared.io.TextFileReader;

/**
 * Loads an <code>Exp</code> from an XML file.
 * 
 * @author Philippe T. Pinard
 */
public class ExpLoader extends XmlLoader {

    /**
     * Returns the validation message. An empty string is the file is valid; a
     * error message otherwise.
     * 
     * @param file
     *            a file
     * @return error message or empty string
     */
    protected static String getValidationMessage(File file) {
        // Check extension
        String ext = FileUtil.getExtension(file);
        if (!ext.equalsIgnoreCase("xml"))
            return "The extension of the file must be xml, not " + ext + ".";

        // Check header line
        String header = null;
        try {
            TextFileReader reader = new TextFileReader(file);
            reader.readLine(); // Skip xml document line
            header = reader.readLine();
            reader.close();
        } catch (IOException ex) {
            return ex.getMessage();
        }

        if (header == null || !header.startsWith("<Exp"))
            return "Line 2 of the XML file should start wiht '<Exp'.";

        return "";
    }



    /**
     * Checks if the file is a valid <code>PhasesMap</code>.
     * 
     * @param file
     *            a file
     * @return <code>true</code> if the file is valid, <code>false</code>
     *         otherwise
     */
    public static boolean isExp(File file) {
        return (getValidationMessage(file).length() == 0) ? true : false;
    }



    /**
     * Validates the file to be a valid <code>PhasesMap</code>.
     * 
     * @param file
     *            a file
     * @throws IOException
     *             if the file is not valid
     */
    private static void validate(File file) throws IOException {
        String message = getValidationMessage(file);
        if (message.length() > 0)
            throw new IOException(message);
    }



    @Override
    protected ObjectXmlLoader getXmlLoader() {
        return new ExpXmlLoader();
    }



    @Override
    public Exp load(File file) throws IOException {
        validate(file);
        return (Exp) super.load(file);
    }

}
