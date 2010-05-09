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
import org.ebsdimage.io.exp.ops.pattern.post.RadialNoiseXmlSaver;
import org.jdom.Element;
import org.junit.Before;
import org.junit.Test;


import ptpshared.utility.xml.JDomUtil;

public class RadialNoiseXmlSaverTest {

    private RadialNoise op;



    @Before
    public void setUp() throws Exception {
        op = new RadialNoise(2, 3, 4.0, 5.0, 6.0, 7.0);
    }



    @Test
    public void testSaveRadialNoise() {
        Element element = new RadialNoiseXmlSaver().save(op);

        assertEquals(TAG_NAME, element.getName());
        assertEquals(2, JDomUtil.getIntegerFromAttribute(element, ATTR_X));
        assertEquals(3, JDomUtil.getIntegerFromAttribute(element, ATTR_Y));
        assertEquals(4.0, JDomUtil
                .getDoubleFromAttribute(element, ATTR_STDDEVX), 1e-7);
        assertEquals(5.0, JDomUtil
                .getDoubleFromAttribute(element, ATTR_STDDEVY), 1e-7);
        assertEquals(6.0, JDomUtil.getDoubleFromAttribute(element,
                ATTR_INITIALNOISESTDDEV), 1e-7);
        assertEquals(7.0, JDomUtil.getDoubleFromAttribute(element,
                ATTR_FINALNOISESTDDEV), 1e-7);
    }

}
