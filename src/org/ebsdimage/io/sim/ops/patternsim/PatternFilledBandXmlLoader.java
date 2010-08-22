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
package org.ebsdimage.io.sim.ops.patternsim;

import static org.ebsdimage.io.sim.ops.patternsim.PatternFilledBandXmlTags.TAG_NAME;
import static org.ebsdimage.io.sim.ops.patternsim.PatternSimOpXmlTags.ATTR_HEIGHT;
import static org.ebsdimage.io.sim.ops.patternsim.PatternSimOpXmlTags.ATTR_MAXINDEX;
import static org.ebsdimage.io.sim.ops.patternsim.PatternSimOpXmlTags.ATTR_SCATTER_TYPE;
import static org.ebsdimage.io.sim.ops.patternsim.PatternSimOpXmlTags.ATTR_WIDTH;

import org.ebsdimage.core.sim.ops.patternsim.PatternFilledBand;
import org.jdom.Element;
import org.jdom.IllegalNameException;

import ptpshared.utility.BadUnitException;
import ptpshared.utility.xml.JDomUtil;
import ptpshared.utility.xml.ObjectXmlLoader;
import ptpshared.utility.xml.UnitsXmlTags;
import crystallography.core.ScatteringFactorsEnum;

/**
 * XML loader for a <code>PatternFilledBand</code> operation.
 * 
 * @author Philippe T. Pinard
 */
public class PatternFilledBandXmlLoader implements ObjectXmlLoader {

    /**
     * Loads a <code>PatternFilledBand</code> operation from an XML
     * <code>Element</code>.
     * 
     * @param element
     *            an XML <code>Element</code>
     * @return a <code>PatternFilledBand</code> operation
     * @throws IllegalNameException
     *             if the <code>Element</code> tag name is incorrect.
     */
    @Override
    public PatternFilledBand load(Element element) {
        if (!element.getName().equals(TAG_NAME))
            throw new IllegalNameException("Name of the element should be "
                    + TAG_NAME + " not " + element.getName() + ".");
        if (!JDomUtil.getStringFromAttributeDefault(element, UnitsXmlTags.ATTR,
                UnitsXmlTags.PX).equals(UnitsXmlTags.PX))
            throw new BadUnitException("The dimenions unit should be "
                    + UnitsXmlTags.PX);

        int width =
                JDomUtil.getIntegerFromAttribute(element, ATTR_WIDTH,
                        PatternFilledBand.DEFAULT_WIDTH);
        int height =
                JDomUtil.getIntegerFromAttribute(element, ATTR_HEIGHT,
                        PatternFilledBand.DEFAULT_HEIGHT);
        int maxIndex =
                JDomUtil.getIntegerFromAttribute(element, ATTR_MAXINDEX,
                        PatternFilledBand.DEFAULT_MAX_INDEX);
        ScatteringFactorsEnum scatterType =
                ScatteringFactorsEnum.valueOf(JDomUtil.getStringFromAttributeDefault(
                        element, ATTR_SCATTER_TYPE,
                        PatternFilledBand.DEFAULT_SCATTER_TYPE.toString()));

        return new PatternFilledBand(width, height, maxIndex, scatterType);
    }
}
