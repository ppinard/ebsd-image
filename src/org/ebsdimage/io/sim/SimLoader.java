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
package org.ebsdimage.io.sim;

import java.io.File;
import java.io.IOException;

import org.ebsdimage.core.sim.Sim;

import ptpshared.util.xml.XmlLoader;
import rmlshared.io.FileUtil;
import rmlshared.io.Loader;
import rmlshared.io.TextFileReader;

/**
 * Loads a <code>Sim</code> from an XML file.
 * 
 * @author Philippe T. Pinard
 */
public class SimLoader implements Loader {

    /** XML loader. */
    private final XmlLoader loader = new XmlLoader();



    @Override
    public boolean canLoad(File file) {
        return getValidationMessage(file).length() == 0;
    }



    @Override
    public double getTaskProgress() {
        return loader.getTaskProgress();
    }



    /**
     * Returns the validation message. An empty string is the file is valid; a
     * error message otherwise.
     * 
     * @param file
     *            a file
     * @return error message or empty string
     */
    protected String getValidationMessage(File file) {
        // Check extension
        String ext = FileUtil.getExtension(file);
        if (!ext.equalsIgnoreCase("xml"))
            return "The extension of the file must be xml, not " + ext + ".";

        // Check header line
        String header = null;
        try {
            TextFileReader reader = new TextFileReader(file);
            reader.readLine(); // Skip XML document line
            header = reader.readLine();
            reader.close();
        } catch (IOException ex) {
            return ex.getMessage();
        }

        if (header == null || !header.startsWith("<Sim"))
            return "Line 2 of the XML file should start wiht '<Sim'.";

        return "";
    }



    @Override
    public Sim load(File file) throws IOException {
        return loader.load(Sim.class, file);
    }



    @Override
    public Sim load(File file, Object obj) throws IOException {
        return load(file);
    }

}
