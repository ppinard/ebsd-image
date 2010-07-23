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

import static org.ebsdimage.io.exp.ops.pattern.op.PatternFilesLoaderXmlTags.TAG_NAME;
import static org.ebsdimage.io.exp.ops.pattern.op.PatternOpXmlTags.ATTR_SIZE;
import static org.ebsdimage.io.exp.ops.pattern.op.PatternOpXmlTags.ATTR_START_INDEX;

import java.io.File;
import java.util.ArrayList;

import org.ebsdimage.core.exp.ops.pattern.op.PatternFilesLoader;
import org.jdom.Element;
import org.jdom.IllegalNameException;

import ptpshared.utility.xml.JDomUtil;
import ptpshared.utility.xml.ObjectXmlLoader;

/**
 * XML loader for a <code>PatternFilesLoader</code> operation.
 * 
 * @author Philippe T. Pinard
 */
public class PatternFilesLoaderXmlLoader implements ObjectXmlLoader {

    /**
     * Loads a <code>PatternFilesLoader</code> operation from an XML
     * <code>Element</code>.
     * 
     * @param element
     *            an XML <code>Element</code>
     * @return a <code>PatternFilesLoader</code> operation
     * @throws IllegalNameException
     *             if the <code>Element</code> tag name is incorrect.
     */
    @Override
    public PatternFilesLoader load(Element element) {
        if (!element.getName().equals(TAG_NAME))
            throw new IllegalNameException("Name of the element should be "
                    + TAG_NAME + " not " + element.getName() + ".");

        int startIndex =
                JDomUtil.getIntegerFromAttribute(element, ATTR_START_INDEX);
        int size = JDomUtil.getIntegerFromAttribute(element, ATTR_SIZE);

        ArrayList<File> files = new ArrayList<File>();
        for (Object obj : element.getChildren())
            files.add(new File(((Element) obj).getText()));

        if (files.size() != size)
            throw new IllegalArgumentException(
                    "The number of files in the XML (" + files.size()
                            + ") does not match the value " + "of the size "
                            + "attribute (" + size + ").");

        return new PatternFilesLoader(startIndex, files.toArray(new File[0]));
    }

}
