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

import static org.ebsdimage.io.sim.ops.patternsim.PatternBandEdgesXmlTags.TAG_NAME;
import static org.ebsdimage.io.sim.ops.patternsim.PatternSimOpXmlTags.ATTR_HEIGHT;
import static org.ebsdimage.io.sim.ops.patternsim.PatternSimOpXmlTags.ATTR_MAXINDEX;
import static org.ebsdimage.io.sim.ops.patternsim.PatternSimOpXmlTags.ATTR_SCATTER_TYPE;
import static org.ebsdimage.io.sim.ops.patternsim.PatternSimOpXmlTags.ATTR_WIDTH;
import static org.junit.Assert.assertEquals;

import org.ebsdimage.core.sim.ops.patternsim.PatternBandEdges;
import org.ebsdimage.io.sim.ops.patternsim.PatternBandEdgesXmlSaver;
import org.jdom.Element;
import org.junit.Before;
import org.junit.Test;

import ptpshared.utility.xml.JDomUtil;
import crystallography.core.ScatteringFactorsEnum;

public class PatternBandEdgesXmlSaverTest {

    private PatternBandEdges op;



    @Before
    public void setUp() throws Exception {
        op = new PatternBandEdges(336, 256, 5, ScatteringFactorsEnum.XRAY);
    }



    @Test
    public void testSavePatternBandEdgesXrayScatter() {
        Element element = new PatternBandEdgesXmlSaver().save(op);

        assertEquals(TAG_NAME, element.getName());
        assertEquals(336, JDomUtil.getIntegerFromAttribute(element, ATTR_WIDTH));
        assertEquals(256,
                JDomUtil.getIntegerFromAttribute(element, ATTR_HEIGHT));
        assertEquals(5,
                JDomUtil.getIntegerFromAttribute(element, ATTR_MAXINDEX));
        assertEquals(ScatteringFactorsEnum.XRAY.toString(),
                JDomUtil.getStringFromAttribute(element, ATTR_SCATTER_TYPE));
    }

}
