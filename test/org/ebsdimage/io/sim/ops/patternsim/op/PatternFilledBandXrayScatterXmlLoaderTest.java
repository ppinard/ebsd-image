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
import org.ebsdimage.io.sim.ops.patternsim.op.PatternFilledBandXrayScatterXmlLoader;
import org.jdom.Element;
import org.junit.Before;
import org.junit.Test;


public class PatternFilledBandXrayScatterXmlLoaderTest {

    private Element element;



    @Before
    public void setUp() throws Exception {
        element = new Element(TAG_NAME);

        element.setAttribute(ATTR_WIDTH, Integer.toString(336));
        element.setAttribute(ATTR_HEIGHT, Integer.toString(256));
        element.setAttribute(ATTR_MAX_INDEX, Integer.toString(5));
    }



    @Test
    public void testLoad() {
        PatternFilledBandXrayScatter op = new PatternFilledBandXrayScatterXmlLoader()
                .load(element);

        assertEquals(TAG_NAME, op.getName());
        assertEquals(336, op.width);
        assertEquals(256, op.height);
        assertEquals(5, op.maxIndex);
    }

}
