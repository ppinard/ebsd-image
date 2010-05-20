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
package org.ebsdimage.io.exp.ops.detection.op;

import static org.ebsdimage.io.exp.ops.detection.op.AutomaticStdDevXmlTags.ATTR_SIGMAFACTOR;
import static org.ebsdimage.io.exp.ops.detection.op.AutomaticStdDevXmlTags.TAG_NAME;
import static org.junit.Assert.assertEquals;

import org.ebsdimage.core.exp.ops.detection.op.AutomaticStdDev;
import org.jdom.Element;
import org.junit.Before;
import org.junit.Test;

public class AutomaticStdDevXmlLoaderTest {

    private Element element;



    @Before
    public void setUp() throws Exception {
        element = new Element(TAG_NAME);

        element.setAttribute(ATTR_SIGMAFACTOR, Double.toString(1.5));
    }



    @Test
    public void testLoad() {
        AutomaticStdDev op = new AutomaticStdDevXmlLoader().load(element);

        assertEquals(TAG_NAME, op.getName());
        assertEquals(1.5, op.sigmaFactor, 1e-6);
    }

}
