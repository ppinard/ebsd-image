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

import static crystallography.io.AtomSiteXmlTags.ATTR_ATOMICNUMBER;
import static crystallography.io.AtomSiteXmlTags.CHILD_ELEMENT;
import static crystallography.io.AtomSiteXmlTags.CHILD_POSITION;
import static crystallography.io.AtomSiteXmlTags.TAG_NAME;

import org.jdom.Element;
import org.jdom.IllegalNameException;

import ptpshared.core.math.Vector3D;
import ptpshared.io.math.Vector3DXmlLoader;
import ptpshared.utility.xml.JDomUtil;
import ptpshared.utility.xml.ObjectXmlLoader;
import crystallography.core.AtomSite;

/**
 * XML loader for <code>AtomSite</code>.
 * 
 * @author Philippe T. Pinard
 * 
 */
public class AtomSiteXmlLoader implements ObjectXmlLoader {

    /**
     * Loads an <code>AtomSite</code> from an XML <code>Element</code>.
     * 
     * @param element
     *            an XML <code>Element</code>
     * @return an <code>AtomSite</code>
     * @throws IllegalNameException
     *             if the <code>Element</code> tag name is incorrect.
     */
    @Override
    public AtomSite load(Element element) {
        if (!element.getName().equals(TAG_NAME))
            throw new IllegalNameException("Name of the element should be "
                    + TAG_NAME + " not " + element.getName() + ".");

        Vector3D position = new Vector3DXmlLoader().load(JDomUtil.getChild(
                element, CHILD_POSITION), CHILD_POSITION);
        int atomicNumber = JDomUtil.getIntegerFromAttribute(element,
                CHILD_ELEMENT, ATTR_ATOMICNUMBER);

        return new AtomSite(atomicNumber, position);
    }

}
