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

import static crystallography.io.CrystalXmlTags.ATTR_NAME;
import static crystallography.io.CrystalXmlTags.TAG_NAME;

import org.jdom.Element;
import org.jdom.IllegalNameException;

import ptpshared.utility.xml.JDomUtil;
import ptpshared.utility.xml.ObjectXmlLoader;
import crystallography.core.*;

/**
 * XML loader for <code>Crystal</code>.
 * 
 * @author Philippe T. Pinard
 */
public class CrystalXmlLoader implements ObjectXmlLoader {

    /**
     * Loads a <code>Crystal</code> from an XML <code>Element</code>.
     * <p/>
     * The loader can load generic crystal or specific crystal in
     * <code>crystallography.core.crystals</code>. Specific crystal information
     * can be overwritten by specifying an unit cell, atom sites or point group.
     * 
     * @param element
     *            an XML <code>Element</code>
     * @return a <code>Crystal</code>
     * @throws IllegalNameException
     *             if no crystal was found using the <code>Element</code> tag
     *             name.
     */
    @Override
    public Crystal load(Element element) {
        if (!element.getName().equals(TAG_NAME))
            throw new IllegalNameException("Name of the element should be "
                    + TAG_NAME + " not " + element.getName() + ".");

        String name = JDomUtil.getStringFromAttribute(element, ATTR_NAME);

        UnitCell unitCell =
                new UnitCellXmlLoader().load(JDomUtil.getChild(element,
                        UnitCellXmlTags.TAG_NAME));

        AtomSites atoms =
                new AtomSitesXmlLoader().load(JDomUtil.getChild(element,
                        AtomSitesXmlTags.TAG_NAME));

        SpaceGroup sg;
        Element child = element.getChild(SpaceGroupXmlTags.TAG_NAME);
        if (child != null) {
            sg = new SpaceGroupXmlLoader().load(child);
        } else { // Old crystal file with only Point group
            child = element.getChild("PointGroup");

            if (child == null)
                throw new IllegalArgumentException(
                        "Crystal must have a space group.");

            // Open Laue group
            int index = JDomUtil.getIntegerFromAttribute(child, "laueGroup");
            LaueGroup lg = LaueGroup.fromIndex(index);

            // Select the first space group of the list
            sg = SpaceGroups.list(lg)[0];
        }

        return new Crystal(name, unitCell, atoms, sg);
    }
}
