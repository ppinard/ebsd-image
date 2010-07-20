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
package org.ebsdimage.io.exp.ops.pattern.results;

import static org.ebsdimage.io.exp.ops.pattern.results.SumRegionXmlTags.*;
import static org.junit.Assert.assertEquals;

import org.ebsdimage.core.exp.ops.pattern.results.SumRegion;
import org.jdom.Element;
import org.junit.Before;
import org.junit.Test;

public class SumRegionXmlLoaderTest {

    private Element element;



    @Before
    public void setUp() throws Exception {
        element = new Element(TAG_NAME);

        element.setAttribute(ATTR_XMIN, Double.toString(0.2));
        element.setAttribute(ATTR_YMIN, Double.toString(0.3));
        element.setAttribute(ATTR_XMAX, Double.toString(0.5));
        element.setAttribute(ATTR_YMAX, Double.toString(0.6));
    }



    @Test
    public void testLoad() {
        SumRegion op = new SumRegionXmlLoader().load(element);

        assertEquals(TAG_NAME, op.getName());
        assertEquals(0.2, op.xmin, 1e-6);
        assertEquals(0.3, op.ymin, 1e-6);
        assertEquals(0.5, op.xmax, 1e-6);
        assertEquals(0.6, op.ymax, 1e-6);
    }

}
