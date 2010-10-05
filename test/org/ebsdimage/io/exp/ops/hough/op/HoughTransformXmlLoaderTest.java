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
package org.ebsdimage.io.exp.ops.hough.op;

import static org.ebsdimage.io.exp.ops.hough.op.HoughTransformXmlTags.ATTR_DELTA_RHO;
import static org.ebsdimage.io.exp.ops.hough.op.HoughTransformXmlTags.ATTR_DELTA_THETA;
import static org.ebsdimage.io.exp.ops.hough.op.HoughTransformXmlTags.TAG_NAME;
import static org.junit.Assert.assertEquals;

import org.ebsdimage.core.exp.ops.hough.op.HoughTransform;
import org.jdom.Element;
import org.junit.Before;
import org.junit.Test;

public class HoughTransformXmlLoaderTest {

    private Element element;



    @Before
    public void setUp() throws Exception {
        element = new Element(TAG_NAME);

        element.setAttribute(ATTR_DELTA_THETA, Double.toString(2.0));
        element.setAttribute(ATTR_DELTA_RHO, Double.toString(1.0));
    }



    @Test
    public void testLoad() {
        HoughTransform op = new HoughTransformXmlLoader().load(element);

        assertEquals(TAG_NAME, op.getName());
        assertEquals(2.0, op.deltaTheta, 1e-7);
        assertEquals(1.0, op.deltaRho, 1e-7);
    }

}
