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

import static org.ebsdimage.io.exp.ops.hough.op.AutoHoughTransformXmlTags.ATTR_DELTA_THETA;
import static org.ebsdimage.io.exp.ops.hough.op.AutoHoughTransformXmlTags.TAG_NAME;
import static org.junit.Assert.assertEquals;

import org.ebsdimage.core.exp.ops.hough.op.AutoHoughTransform;
import org.jdom.Element;
import org.junit.Before;
import org.junit.Test;

import ptpshared.utility.xml.JDomUtil;

public class AutoHoughTransformXmlSaverTest {

    private AutoHoughTransform op;



    @Before
    public void setUp() throws Exception {
        op = new AutoHoughTransform(2.0);
    }



    @Test
    public void testSaveHoughTransform() {
        Element element = new AutoHoughTransformXmlSaver().save(op);

        assertEquals(TAG_NAME, element.getName());
        assertEquals(2.0,
                JDomUtil.getDoubleFromAttribute(element, ATTR_DELTA_THETA),
                1e-7);
    }

}
