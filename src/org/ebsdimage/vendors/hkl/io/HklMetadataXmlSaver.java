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
package org.ebsdimage.vendors.hkl.io;

import static org.ebsdimage.vendors.hkl.io.HklMetadataXmlTags.PROJECT_NAME_TAG;
import static org.ebsdimage.vendors.hkl.io.HklMetadataXmlTags.PROJECT_PATH_TAG;
import static org.ebsdimage.vendors.hkl.io.HklMetadataXmlTags.TAG_NAME;

import org.ebsdimage.io.EbsdMetadataXmlSaver;
import org.ebsdimage.vendors.hkl.core.HklMetadata;
import org.jdom.Element;

import ptpshared.utility.xml.ObjectXml;

/**
 * XML saver for <code>HklMetadata</code>.
 * 
 * @author Philippe T. Pinard
 * 
 */
public class HklMetadataXmlSaver extends EbsdMetadataXmlSaver {

    /**
     * {@inheritDoc}
     * 
     * @see #save(HklMetadata)
     */
    @Override
    public Element save(ObjectXml obj) {
        return save((HklMetadata) obj);
    }



    /**
     * Saves a <code>HklMetadata</code> to an XML <code>Element</code>.
     * 
     * @param metadata
     *            a <code>HklMetadata</code>
     * @return an XML <code>Element</code>
     */
    public Element save(HklMetadata metadata) {
        // Save metadata from EbsdMetadata
        Element element = createElement(metadata, TAG_NAME);

        // Project name
        Element child = new Element(PROJECT_NAME_TAG);
        child.setText(metadata.projectName);
        element.addContent(child);

        // Project path
        child = new Element(PROJECT_PATH_TAG);
        child.setText(metadata.projectPath.getAbsolutePath());
        element.addContent(child);

        return element;
    }
}
