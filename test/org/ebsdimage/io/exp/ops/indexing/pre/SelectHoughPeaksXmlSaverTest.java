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
package org.ebsdimage.io.exp.ops.indexing.pre;

import static org.ebsdimage.io.exp.ops.indexing.pre.HoughPeaksSelectorXmlTags.ATTR_MAXIMUM;
import static org.ebsdimage.io.exp.ops.indexing.pre.HoughPeaksSelectorXmlTags.ATTR_MINIMUM;
import static org.ebsdimage.io.exp.ops.indexing.pre.HoughPeaksSelectorXmlTags.TAG_NAME;
import static org.junit.Assert.assertEquals;

import org.ebsdimage.core.exp.ops.indexing.pre.HoughPeaksSelector;
import org.jdom.Element;
import org.junit.Before;
import org.junit.Test;

import ptpshared.utility.xml.JDomUtil;
import ptpshared.utility.xml.ObjectXml;

public class SelectHoughPeaksXmlSaverTest {

    private HoughPeaksSelector op;



    @Before
    public void setUp() throws Exception {
        op = new HoughPeaksSelector(4, 6);
    }



    @Test
    public void testSaveObjectXml() {
        Element element = new HoughPeaksSelectorXmlSaver().save((ObjectXml) op);
        testElement(element);
    }



    @Test
    public void testSaveSelectHoughPeaks() {
        Element element = new HoughPeaksSelectorXmlSaver().save(op);
        testElement(element);
    }



    private void testElement(Element element) {
        assertEquals(TAG_NAME, element.getName());

        assertEquals(op.minimum,
                JDomUtil.getIntegerFromAttribute(element, ATTR_MINIMUM));
        assertEquals(op.maximum,
                JDomUtil.getIntegerFromAttribute(element, ATTR_MAXIMUM));
    }

}
