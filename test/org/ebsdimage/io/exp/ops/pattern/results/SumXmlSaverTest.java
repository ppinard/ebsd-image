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

import static org.ebsdimage.io.exp.ops.pattern.results.SumXmlTags.*;
import static org.junit.Assert.assertEquals;

import org.ebsdimage.core.exp.ops.pattern.results.Sum;
import org.jdom.Element;
import org.junit.Before;
import org.junit.Test;

import ptpshared.utility.xml.JDomUtil;

public class SumXmlSaverTest {

    private Sum op;



    @Before
    public void setUp() throws Exception {
        op = new Sum(0.2, 0.3, 0.5, 0.6);
    }



    @Test
    public void testSaveAverageRegion() {
        Element element = new SumXmlSaver().save(op);

        assertEquals(TAG_NAME, element.getName());
        assertEquals(0.2, JDomUtil.getDoubleFromAttribute(element, ATTR_XMIN),
                1e-6);
        assertEquals(0.3, JDomUtil.getDoubleFromAttribute(element, ATTR_YMIN),
                1e-6);
        assertEquals(0.5, JDomUtil.getDoubleFromAttribute(element, ATTR_XMAX),
                1e-6);
        assertEquals(0.6, JDomUtil.getDoubleFromAttribute(element, ATTR_YMAX),
                1e-6);
    }

}
