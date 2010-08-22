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

import static org.ebsdimage.io.PhasesMapXmlTags.HEADER;
import static org.ebsdimage.io.PhasesMapXmlTags.TAG_NAME;
import static org.ebsdimage.io.PhasesMapXmlTags.TAG_PHASES;

import java.io.File;
import java.io.IOException;

import org.ebsdimage.core.PhasesMap;
import org.jdom.Element;
import org.jdom.IllegalNameException;

import ptpshared.utility.xml.JDomUtil;
import rmlimage.core.ByteMap;
import rmlimage.io.BasicBmpLoader;
import rmlimage.io.MapLoader;
import rmlshared.io.FileUtil;
import rmlshared.io.TextFileReader;
import crystallography.core.Crystal;

/**
 * Loader for <code>PhasesMap</code>.
 * 
 * @author Philippe T. Pinard
 */
public class PhasesMapLoader extends MapLoader {

    /**
     * Returns the validation message. An empty string is the file is valid; a
     * error message otherwise.
     * 
     * @param file
     *            a file
     * @return error message or empty string
     */
    private static String getValidationMessage(File file) {
        // Check extension
        String ext = FileUtil.getExtension(file);
        if (!ext.equalsIgnoreCase("bmp"))
            return "The extension of the file must be bmp, not " + ext + ".";

        // Check xml file exists
        File xmlFile = FileUtil.setExtension(file, "xml");
        if (!xmlFile.exists())
            return "Could not find required xml file: " + xmlFile.getPath()
                    + ".";

        // Check header line
        String header = null;
        try {
            TextFileReader reader = new TextFileReader(xmlFile);
            reader.readLine(); // Skip xml document line
            header = reader.readLine();
            reader.close();
        } catch (IOException ex) {
            return ex.getMessage();
        }

        if (header == null
                || !(header.startsWith("<!--") && header.endsWith("-->")))
            return "Line 2 of the XML file should have be a comment line.";

        header = header.substring(4, header.length() - 3); // Trim comment chars

        if (!header.startsWith(HEADER))
            return "Header of XML file (" + header
                    + ") does not match expected header for PhasesMap ("
                    + HEADER + ").";

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
    public static boolean isPhasesMap(File file) {
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

    /** Bmp loader. */
    private BasicBmpLoader loader = new BasicBmpLoader();



    @Override
    public double getTaskProgress() {
        return loader.getTaskProgress();
    }



    /**
     * Loads a <code>PhasesMap</code> from a file.
     * 
     * @param file
     *            a valid <code>PhasesMap</code> file.
     * @return a <code>PhasesMap</code>
     * @throws IOException
     *             if the file is invalid
     * @throws IOException
     *             if an error occurs while loading the file
     */
    @Override
    public PhasesMap load(File file) throws IOException {
        // Validate file
        validate(file);

        // Load xml file
        File xmlFile = FileUtil.setExtension(file, "xml");
        Element element = JDomUtil.loadXML(xmlFile).getRootElement();

        // Check xml
        if (!element.getName().equals(TAG_NAME))
            throw new IOException("The root element must be " + TAG_NAME
                    + ", not " + element.getName() + ".");

        // Load phase
        Element child = element.getChild(TAG_PHASES);
        if (child == null)
            throw new IllegalNameException("The child element " + TAG_PHASES
                    + " must be defined.");
        Crystal[] phases = new PhasesMapXmlLoader().load(child);

        // Load ByteMap
        ByteMap tmpMap = (ByteMap) loader.load(file);

        // Create PhasesMap
        PhasesMap map =
                new PhasesMap(tmpMap.width, tmpMap.height, tmpMap.pixArray,
                        phases);

        map.setProperties(tmpMap);
        map.setFile(file);
        map.shouldSave(false); // No need to save the file, it was just loaded

        return map;
    }



    /**
     * Loads a <code>PhasesMap</code> from a file.
     * 
     * @param file
     *            a valid <code>PhasesMap</code> file.
     * @param obj
     *            not used
     * @return a <code>PhasesMap</code>
     * @throws IOException
     *             if the file is invalid
     * @throws IOException
     *             if an error occurs while loading the file
     */
    @Override
    public PhasesMap load(File file, Object obj) throws IOException {
        return load(file);
    }

}
