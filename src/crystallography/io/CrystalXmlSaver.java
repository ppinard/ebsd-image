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

import org.jdom.Element;

import ptpshared.utility.xml.ObjectXml;
import ptpshared.utility.xml.ObjectXmlSaver;
import crystallography.core.Crystal;

/**
 * XML saver for <code>Crystal</code>.
 * 
 * @author Philippe T. Pinard
 * 
 */
public class CrystalXmlSaver implements ObjectXmlSaver {

    /**
     * Saves a <code>Crystal</code> to an XML <code>Element</code>.
     * <p/>
     * The tag name of the <code>Element</code> is the name of the class.
     * 
     * @param crystal
     *            a <code>Crystal</code>
     * @return an XML <code>Element</code>
     */
    public Element save(Crystal crystal) {
        Element element = new Element(crystal.getClass().getSimpleName());

        element.setAttribute(ATTR_NAME, crystal.name.toString());

        element.addContent(new UnitCellXmlSaver().save(crystal.unitCell));
        element.addContent(new AtomSitesXmlSaver().save(crystal.atoms));
        element.addContent(new LaueGroupXmlSaver().save(crystal.laueGroup));

        return element;
    }



    /**
     * {@inheritDoc}
     * 
     * @see #save(Crystal)
     */
    @Override
    public Element save(ObjectXml obj) {
        return save((Crystal) obj);
    }

}
