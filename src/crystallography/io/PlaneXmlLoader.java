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
package crystallography.io;

import static crystallography.io.PlaneXmlTags.ATTR_H;
import static crystallography.io.PlaneXmlTags.ATTR_K;
import static crystallography.io.PlaneXmlTags.ATTR_L;
import static crystallography.io.PlaneXmlTags.TAG_NAME;

import org.jdom.Element;
import org.jdom.IllegalNameException;

import ptpshared.utility.xml.JDomUtil;
import ptpshared.utility.xml.ObjectXmlLoader;
import crystallography.core.Plane;

/**
 * XML loader for <code>Plane</code>.
 * 
 * @author Philippe T. Pinard
 * 
 */
public class PlaneXmlLoader implements ObjectXmlLoader {

    /**
     * Loads a <code>Plane</code> from an XML <code>Element</code>.
     * 
     * @param element
     *            an XML <code>Element</code>
     * @return a <code>Plane</code>
     * @throws IllegalNameException
     *             if the <code>Element</code> tag name is incorrect
     */
    @Override
    public Plane load(Element element) {
        if (!element.getName().equals(TAG_NAME))
            throw new IllegalNameException("Name of the element should be "
                    + TAG_NAME + " not " + element.getName() + ".");

        return new Plane(JDomUtil.getIntegerFromAttribute(element, ATTR_H),
                JDomUtil.getIntegerFromAttribute(element, ATTR_K), JDomUtil
                        .getIntegerFromAttribute(element, ATTR_L));
    }

}
