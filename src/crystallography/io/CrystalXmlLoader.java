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
import rmlshared.thread.Reflection;
import crystallography.core.AtomSites;
import crystallography.core.Crystal;
import crystallography.core.PointGroup;
import crystallography.core.UnitCell;

/**
 * XML loader for <code>Crystal</code>.
 * 
 * @author Philippe T. Pinard
 * 
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
        String className = element.getName();

        String name;
        UnitCell unitCell;
        AtomSites atoms;
        PointGroup pointGroup;

        if (className.equals(TAG_NAME)) { // Generic crystal
            name = JDomUtil.getStringFromAttribute(element, ATTR_NAME);
            unitCell = new UnitCellXmlLoader().load(JDomUtil.getChild(element,
                    UnitCellXmlTags.TAG_NAME));
            atoms = new AtomSitesXmlLoader().load(JDomUtil.getChild(element,
                    AtomSitesXmlTags.TAG_NAME));
            pointGroup = new PointGroupXmlLoader().load(JDomUtil.getChild(
                    element, PointGroupXmlTags.TAG_NAME));
        } else { // Specific crystal (already defined)
            String classPath = "crystallography.core.crystals" + "."
                    + className;

            // Temporary load crystal
            Crystal tmpCrystal;
            try {
                tmpCrystal = (Crystal) Reflection.newInstance(classPath);
            } catch (IllegalArgumentException ex) {
                throw new IllegalNameException(
                        "No crystal with element's name (" + className
                                + ") was found.");
            }

            // Override tmpCrystal name, unit cell or atom sites if they are
            // specified

            // If given, use name from XML element; if not, use name from
            // tmpCrystal
            if (JDomUtil.hasAttribute(element, ATTR_NAME))
                name = JDomUtil.getStringFromAttribute(element, ATTR_NAME);
            else
                name = tmpCrystal.name;

            // If given, use unit cell from XML element; if not, use unit cell
            // from tmpCrystal
            if (JDomUtil.hasChild(element, UnitCellXmlTags.TAG_NAME))
                unitCell = new UnitCellXmlLoader().load(JDomUtil.getChild(
                        element, UnitCellXmlTags.TAG_NAME));
            else
                unitCell = tmpCrystal.unitCell;

            // If given, use atom sites from XML element; if not, use atom sites
            // from tmpCrystal
            if (JDomUtil.hasChild(element, AtomSitesXmlTags.TAG_NAME))
                atoms = new AtomSitesXmlLoader().load(JDomUtil.getChild(
                        element, AtomSitesXmlTags.TAG_NAME));
            else
                atoms = tmpCrystal.atoms;

            // If given, use point group from XML element; if not, use point
            // group
            // from tmpCrystal
            if (JDomUtil.hasChild(element, PointGroupXmlTags.TAG_NAME))
                pointGroup = new PointGroupXmlLoader().load(JDomUtil.getChild(
                        element, PointGroupXmlTags.TAG_NAME));
            else
                pointGroup = tmpCrystal.pointGroup;
        }

        return new Crystal(name, unitCell, atoms, pointGroup);
    }
}
