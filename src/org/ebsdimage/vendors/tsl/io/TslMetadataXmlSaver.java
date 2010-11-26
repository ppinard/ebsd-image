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
package org.ebsdimage.vendors.tsl.io;

import static org.ebsdimage.vendors.tsl.io.TslMetadataXmlTags.TAG_NAME;

import org.ebsdimage.io.EbsdMetadataXmlSaver;
import org.ebsdimage.vendors.tsl.core.TslMetadata;
import org.jdom.Element;

import ptpshared.util.xml.ObjectXml;

/**
 * XML saver for <code>TslMetadata</code>.
 * 
 * @author Philippe T. Pinard
 */
public class TslMetadataXmlSaver extends EbsdMetadataXmlSaver {
    /**
     * {@inheritDoc}
     * 
     * @see #save(TslMetadata)
     */
    @Override
    public Element save(ObjectXml obj) {
        return save((TslMetadata) obj);
    }



    /**
     * Saves a <code>TslMetadata</code> to an XML <code>Element</code>.
     * 
     * @param metadata
     *            a <code>TslMetadata</code>
     * @return an XML <code>Element</code>
     */
    public Element save(TslMetadata metadata) {
        return createElement(metadata, TAG_NAME);
    }
}
