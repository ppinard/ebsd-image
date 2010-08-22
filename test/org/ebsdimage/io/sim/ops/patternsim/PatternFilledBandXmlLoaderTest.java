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
import static org.junit.Assert.assertEquals;

import org.ebsdimage.core.sim.ops.patternsim.PatternFilledBand;
import org.ebsdimage.io.sim.ops.patternsim.PatternFilledBandXmlLoader;
import org.jdom.Element;
import org.junit.Before;
import org.junit.Test;

import crystallography.core.ScatteringFactorsEnum;

public class PatternFilledBandXmlLoaderTest {

    private Element element;



    @Before
    public void setUp() throws Exception {
        element = new Element(TAG_NAME);

        element.setAttribute(ATTR_WIDTH, Integer.toString(336));
        element.setAttribute(ATTR_HEIGHT, Integer.toString(256));
        element.setAttribute(ATTR_MAXINDEX, Integer.toString(5));
        element.setAttribute(ATTR_SCATTER_TYPE,
                ScatteringFactorsEnum.XRAY.toString());
    }



    @Test
    public void testLoad() {
        PatternFilledBand op = new PatternFilledBandXmlLoader().load(element);

        assertEquals(TAG_NAME, op.getName());
        assertEquals(336, op.width);
        assertEquals(256, op.height);
        assertEquals(5, op.maxIndex);
        assertEquals(ScatteringFactorsEnum.XRAY, op.scatterType);
    }

}
