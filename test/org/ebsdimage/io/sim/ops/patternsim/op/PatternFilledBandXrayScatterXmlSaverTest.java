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
import static org.junit.Assert.assertEquals;

import org.ebsdimage.core.sim.ops.patternsim.op.PatternFilledBandXrayScatter;
import org.ebsdimage.io.sim.ops.patternsim.op.PatternFilledBandXrayScatterXmlSaver;
import org.jdom.Element;
import org.junit.Before;
import org.junit.Test;


import ptpshared.utility.xml.JDomUtil;

public class PatternFilledBandXrayScatterXmlSaverTest {

    private PatternFilledBandXrayScatter op;



    @Before
    public void setUp() throws Exception {
        op = new PatternFilledBandXrayScatter(336, 256, 5);
    }



    @Test
    public void testSavePatternFilledBandXrayScatter() {
        Element element = new PatternFilledBandXrayScatterXmlSaver().save(op);

        assertEquals(TAG_NAME, element.getName());
        assertEquals(336, JDomUtil.getIntegerFromAttribute(element, ATTR_WIDTH));
        assertEquals(256, JDomUtil
                .getIntegerFromAttribute(element, ATTR_HEIGHT));
        assertEquals(5, JDomUtil.getIntegerFromAttribute(element,
                ATTR_MAX_INDEX));
    }

}
