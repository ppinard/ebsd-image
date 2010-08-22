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
package org.ebsdimage.io.exp.ops.pattern.results;

import static org.ebsdimage.io.exp.ops.pattern.results.EntropyXmlTags.TAG_NAME;

import org.ebsdimage.core.exp.ops.pattern.results.Entropy;
import org.jdom.Element;

import ptpshared.utility.xml.ObjectXml;
import ptpshared.utility.xml.ObjectXmlSaver;

/**
 * XML saver for an <code>Entropy</code> operation.
 * 
 * @author Philippe T. Pinard
 */
public class EntropyXmlSaver implements ObjectXmlSaver {
    /**
     * {@inheritDoc}
     * 
     * @see #save(Entropy)
     */
    @Override
    public Element save(ObjectXml obj) {
        return save((Entropy) obj);
    }



    /**
     * Saves an <code>Entropy</code> operation to an XML <code>Element</code>.
     * 
     * @param op
     *            an <code>Entropy</code> operation
     * @return an XML <code>Element</code>
     */
    public Element save(Entropy op) {
        return new Element(TAG_NAME);
    }

}
