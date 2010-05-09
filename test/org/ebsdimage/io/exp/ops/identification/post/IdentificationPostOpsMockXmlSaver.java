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
package org.ebsdimage.io.exp.ops.identification.post;

import static org.ebsdimage.io.exp.ops.identification.post.IdentificationPostOpsMockXmlTags.TAG_NAME;

import org.ebsdimage.core.exp.ops.identification.post.IdentificationPostOpsMock;
import org.jdom.Element;

import ptpshared.utility.xml.ObjectXml;
import ptpshared.utility.xml.ObjectXmlSaver;

public class IdentificationPostOpsMockXmlSaver implements ObjectXmlSaver {

    @Override
    public Element save(ObjectXml obj) {
        return save((IdentificationPostOpsMock) obj);
    }



    public Element save(IdentificationPostOpsMock obj) {
        return new Element(TAG_NAME);
    }

}
