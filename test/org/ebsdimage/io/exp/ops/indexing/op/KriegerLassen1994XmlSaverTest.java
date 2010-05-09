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
package org.ebsdimage.io.exp.ops.indexing.op;

import static org.ebsdimage.io.exp.ops.indexing.op.KriegerLassen1994XmlTags.ATTR_MAX_INDEX;
import static org.ebsdimage.io.exp.ops.indexing.op.KriegerLassen1994XmlTags.ATTR_SCATTER_TYPE;
import static org.ebsdimage.io.exp.ops.indexing.op.KriegerLassen1994XmlTags.TAG_NAME;
import static org.junit.Assert.assertEquals;

import org.ebsdimage.core.exp.ops.indexing.op.KriegerLassen1994;
import org.ebsdimage.core.exp.ops.indexing.op.ScatteringFactorsEnum;
import org.ebsdimage.io.exp.ops.indexing.op.KriegerLassen1994XmlSaver;
import org.jdom.Element;
import org.junit.Before;
import org.junit.Test;

import ptpshared.utility.xml.JDomUtil;
import ptpshared.utility.xml.ObjectXml;

public class KriegerLassen1994XmlSaverTest {

    private KriegerLassen1994 op;



    @Before
    public void setUp() throws Exception {
        op = new KriegerLassen1994(4, ScatteringFactorsEnum.ELECTRON);
    }



    @Test
    public void testSaveObjectXml() {
        Element element = new KriegerLassen1994XmlSaver().save((ObjectXml) op);
        testElement(element);
    }



    @Test
    public void testSaveKriegerLassen1994() {
        Element element = new KriegerLassen1994XmlSaver().save(op);
        testElement(element);
    }



    private void testElement(Element element) {
        assertEquals(TAG_NAME, element.getName());
        assertEquals(op.maxIndex, JDomUtil.getIntegerFromAttribute(element,
                ATTR_MAX_INDEX));
        assertEquals(op.scatterType, ScatteringFactorsEnum.valueOf(JDomUtil
                .getStringFromAttribute(element, ATTR_SCATTER_TYPE)));
    }
}
