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
package org.ebsdimage.io.exp.ops.detection.post;

import static org.ebsdimage.io.exp.ops.detection.post.ShapeFactorXmlTags.ATTR_ASPECT_RATIO;
import static org.ebsdimage.io.exp.ops.detection.post.ShapeFactorXmlTags.TAG_NAME;

import org.ebsdimage.core.exp.ops.detection.post.ShapeFactor;
import org.jdom.Element;
import org.jdom.IllegalNameException;

import ptpshared.utility.xml.JDomUtil;
import ptpshared.utility.xml.ObjectXmlLoader;

/**
 * XML loader for a <code>ShapeFactor</code> operation.
 * 
 * @author Philippe T. Pinard
 */
public class ShapeFactorXmlLoader implements ObjectXmlLoader {

    /**
     * Loads an <code>ShapeFactor</code> operation from an XML
     * <code>Element</code> .
     * 
     * @param element
     *            an XML <code>Element</code>
     * @return an <code>ShapeFactor</code> operation
     * @throws IllegalNameException
     *             if the <code>Element</code> tag name is incorrect.
     */
    @Override
    public ShapeFactor load(Element element) {
        if (!element.getName().equals(TAG_NAME))
            throw new IllegalNameException("Name of the element should be "
                    + TAG_NAME + " not " + element.getName() + ".");

        double aspectRatio =
                JDomUtil.getDoubleFromAttribute(element, ATTR_ASPECT_RATIO,
                        ShapeFactor.DEFAULT_ASPECT_RATIO);

        return new ShapeFactor(aspectRatio);
    }

}
