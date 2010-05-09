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

import static org.ebsdimage.io.exp.ops.pattern.post.MaskXmlTags.ATTR_CENTROIDX;
import static org.ebsdimage.io.exp.ops.pattern.post.MaskXmlTags.ATTR_CENTROIDY;
import static org.ebsdimage.io.exp.ops.pattern.post.MaskXmlTags.ATTR_RADIUS;
import static org.ebsdimage.io.exp.ops.pattern.post.MaskXmlTags.TAG_NAME;
import static org.junit.Assert.assertEquals;

import org.ebsdimage.core.exp.ops.pattern.post.Mask;
import org.ebsdimage.io.exp.ops.pattern.post.MaskXmlLoader;
import org.jdom.Element;
import org.junit.Before;
import org.junit.Test;


public class MaskXmlLoaderTest {

    private Element element;



    @Before
    public void setUp() throws Exception {
        element = new Element(TAG_NAME);

        element.setAttribute(ATTR_CENTROIDX, Integer.toString(10));
        element.setAttribute(ATTR_CENTROIDY, Integer.toString(11));
        element.setAttribute(ATTR_RADIUS, Integer.toString(8));
    }



    @Test
    public void testLoad() {
        Mask op = new MaskXmlLoader().load(element);

        assertEquals(TAG_NAME, op.getName());
        assertEquals(10, op.centroidX);
        assertEquals(11, op.centroidY);
        assertEquals(8, op.radius);
    }

}
