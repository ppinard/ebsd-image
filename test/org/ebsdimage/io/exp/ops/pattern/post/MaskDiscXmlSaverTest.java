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

import static org.ebsdimage.io.exp.ops.pattern.post.MaskDiscXmlTags.ATTR_CENTROIDX;
import static org.ebsdimage.io.exp.ops.pattern.post.MaskDiscXmlTags.ATTR_CENTROIDY;
import static org.ebsdimage.io.exp.ops.pattern.post.MaskDiscXmlTags.ATTR_RADIUS;
import static org.ebsdimage.io.exp.ops.pattern.post.MaskDiscXmlTags.TAG_NAME;
import static org.junit.Assert.assertEquals;

import org.ebsdimage.core.exp.ops.pattern.post.MaskDisc;
import org.jdom.Element;
import org.junit.Before;
import org.junit.Test;

import ptpshared.utility.xml.JDomUtil;

public class MaskDiscXmlSaverTest {

    private MaskDisc op;



    @Before
    public void setUp() throws Exception {
        op = new MaskDisc(10, 11, 8);
    }



    @Test
    public void testSaveCrop() {
        Element element = new MaskDiscXmlSaver().save(op);

        assertEquals(TAG_NAME, element.getName());
        assertEquals(10,
                JDomUtil.getIntegerFromAttribute(element, ATTR_CENTROIDX));
        assertEquals(11,
                JDomUtil.getIntegerFromAttribute(element, ATTR_CENTROIDY));
        assertEquals(8, JDomUtil.getIntegerFromAttribute(element, ATTR_RADIUS));
    }

}
