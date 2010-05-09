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

import java.io.File;
import java.io.IOException;

import org.ebsdimage.core.PhasesMap;
import org.jdom.Comment;
import org.jdom.Document;
import org.jdom.Element;

import ptpshared.utility.xml.JDomUtil;
import rmlimage.io.BasicBmpSaver;
import rmlshared.io.FileUtil;

/**
 * Saver for a <code>PhasesMap</code> to a bmp file and XML file containing the
 * phases definition.
 * 
 * @author Philippe T. Pinard
 */
public class PhasesMapSaver extends BasicBmpSaver {

    /**
     * {@inheritDoc}
     * 
     * @see #save(PhasesMap, File)
     */
    @Override
    public void save(Object obj, File file) throws IOException {
        save((PhasesMap) obj, file);
    }



    /**
     * Saves a <code>PhasesMap</code> to a bmp file and XML file containing the
     * phases definition. The location where to save the files is taken from
     * {@link PhasesMap#getFile()}.
     * 
     * @param map
     *            <code>PhasesMap</code> to save
     * @throws IOException
     *             if an error occurs while saving
     */
    public void save(PhasesMap map) throws IOException {
        save(map, map.getFile());
    }



    /**
     * Saves a <code>PhasesMap</code> to a bmp file and XML file containing the
     * phases definition.
     * 
     * @param map
     *            <code>PhasesMap</code> to save
     * @param file
     *            file for the bmp file
     * @throws IOException
     *             if an error occurs while saving
     */
    public void save(PhasesMap map, File file) throws IOException {
        // Save pixArray and props as a normal ByteMap
        super.save(map, file);

        // Save phases
        Element element = new Element(TAG_NAME);

        element.addContent(new PhasesMapXmlSaver().save(map.getPhases()));

        File xmlFile = FileUtil.setExtension(file, "xml");

        Document doc = new Document();
        doc.addContent(new Comment(HEADER));
        doc.addContent(element);

        JDomUtil.saveXML(doc, xmlFile);

        map.shouldSave(false); // No need to save anymore
    }

}
