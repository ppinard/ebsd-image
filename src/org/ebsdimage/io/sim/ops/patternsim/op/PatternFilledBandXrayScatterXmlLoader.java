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
package org.ebsdimage.io.sim.ops.patternsim.op;

import static org.ebsdimage.io.sim.PatternXmlTags.ATTR_HEIGHT;
import static org.ebsdimage.io.sim.PatternXmlTags.ATTR_WIDTH;
import static org.ebsdimage.io.sim.ops.patternsim.op.PatternFilledBandXrayScatterXmlTags.ATTR_MAX_INDEX;
import static org.ebsdimage.io.sim.ops.patternsim.op.PatternFilledBandXrayScatterXmlTags.TAG_NAME;

import org.ebsdimage.core.sim.ops.patternsim.op.PatternFilledBandXrayScatter;
import org.jdom.Element;
import org.jdom.IllegalNameException;

import ptpshared.utility.BadUnitException;
import ptpshared.utility.xml.JDomUtil;
import ptpshared.utility.xml.ObjectXmlLoader;
import ptpshared.utility.xml.UnitsXmlTags;

/**
 * XML loader for a <code>PatternFilledBandXrayScatter</code> operation.
 * 
 * @author Philippe T. Pinard
 * 
 */
public class PatternFilledBandXrayScatterXmlLoader implements ObjectXmlLoader {

    /**
     * Loads a <code>PatternFilledBandXrayScatter</code> operation from an XML
     * <code>Element</code>.
     * 
     * @param element
     *            an XML <code>Element</code>
     * @return a <code>PatternFilledBandXrayScatter</code> operation
     * @throws IllegalNameException
     *             if the <code>Element</code> tag name is incorrect.
     */
    @Override
    public PatternFilledBandXrayScatter load(Element element) {
        if (!element.getName().equals(TAG_NAME))
            throw new IllegalNameException("Name of the element should be "
                    + TAG_NAME + " not " + element.getName() + ".");
        if (!JDomUtil.getStringFromAttributeDefault(element, UnitsXmlTags.ATTR,
                UnitsXmlTags.PX).equals(UnitsXmlTags.PX))
            throw new BadUnitException("The dimenions unit should be "
                    + UnitsXmlTags.PX);

        int width = JDomUtil.getIntegerFromAttribute(element, ATTR_WIDTH,
                PatternFilledBandXrayScatter.DEFAULT_WIDTH);
        int height = JDomUtil.getIntegerFromAttribute(element, ATTR_HEIGHT,
                PatternFilledBandXrayScatter.DEFAULT_HEIGHT);
        int maxIndice = JDomUtil.getIntegerFromAttribute(element,
                ATTR_MAX_INDEX, PatternFilledBandXrayScatter.DEFAULT_MAX_INDEX);

        return new PatternFilledBandXrayScatter(width, height, maxIndice);
    }

}
