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
package org.ebsdimage.io.exp.ops.detection.pre;

import static org.ebsdimage.io.exp.ops.detection.pre.ThetaExpandXmlTags.ATTR_INCREMENT;
import static org.ebsdimage.io.exp.ops.detection.pre.ThetaExpandXmlTags.TAG_NAME;
import static org.junit.Assert.assertEquals;

import org.ebsdimage.core.exp.ops.detection.pre.ThetaExpand;
import org.jdom.Element;
import org.junit.Before;
import org.junit.Test;

import ptpshared.utility.xml.JDomUtil;

public class ThetaExpandXmlSaverTest {

    private ThetaExpand op;



    @Before
    public void setUp() throws Exception {
        op = new ThetaExpand(1.5);
    }



    @Test
    public void testSaveButterfly() {
        Element element = new ThetaExpandXmlSaver().save(op);

        assertEquals(TAG_NAME, element.getName());
        assertEquals(1.5, JDomUtil.getDoubleFromAttribute(element,
                ATTR_INCREMENT), 1e-6);
    }

}
