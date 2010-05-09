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
package org.ebsdimage.io.exp.ops.pattern.post;

import static org.ebsdimage.io.exp.ops.pattern.post.RadialNoiseXmlTags.*;
import static org.junit.Assert.assertEquals;

import org.ebsdimage.core.exp.ops.pattern.post.RadialNoise;
import org.ebsdimage.io.exp.ops.pattern.post.RadialNoiseXmlLoader;
import org.jdom.Element;
import org.junit.Before;
import org.junit.Test;


public class RadialNoiseXmlLoaderTest {

    private Element element;



    @Before
    public void setUp() throws Exception {
        element = new Element(TAG_NAME);

        element.setAttribute(ATTR_X, Integer.toString(2));
        element.setAttribute(ATTR_Y, Integer.toString(3));
        element.setAttribute(ATTR_STDDEVX, Double.toString(4.0));
        element.setAttribute(ATTR_STDDEVY, Double.toString(5.0));
        element.setAttribute(ATTR_INITIALNOISESTDDEV, Double.toString(6.0));
        element.setAttribute(ATTR_FINALNOISESTDDEV, Double.toString(7.0));
    }



    @Test
    public void testLoad() {
        RadialNoise op = new RadialNoiseXmlLoader().load(element);

        assertEquals(TAG_NAME, op.getName());
        assertEquals(2, op.x);
        assertEquals(3, op.y);
        assertEquals(4.0, op.stdDevX, 1e-7);
        assertEquals(5.0, op.stdDevY, 1e-7);
        assertEquals(6.0, op.initialNoiseStdDev, 1e-7);
        assertEquals(7.0, op.finalNoiseStdDev, 1e-7);
    }

}
