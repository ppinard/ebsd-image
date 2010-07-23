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
package org.ebsdimage.io.exp.ops.pattern.op;

import static org.ebsdimage.io.exp.ops.pattern.op.PatternOpMockXmlTags.TAG_NAME;
import static org.ebsdimage.io.exp.ops.pattern.op.PatternOpXmlTags.ATTR_SIZE;

import org.ebsdimage.core.exp.ops.pattern.op.PatternOpMock;
import org.jdom.Element;
import org.jdom.IllegalNameException;

import ptpshared.utility.xml.JDomUtil;
import ptpshared.utility.xml.ObjectXmlLoader;

public class PatternOpMockXmlLoader implements ObjectXmlLoader {

    @Override
    public PatternOpMock load(Element element) {
        if (!element.getName().equals(TAG_NAME))
            throw new IllegalNameException("Tag should be " + TAG_NAME
                    + ", not " + element.getName() + ".");

        int size = JDomUtil.getIntegerFromAttribute(element, ATTR_SIZE);

        return new PatternOpMock(size);
    }

}
