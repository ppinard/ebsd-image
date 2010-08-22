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
package org.ebsdimage.io.exp.ops.hough.post;

import static org.ebsdimage.io.exp.ops.hough.post.HoughCropXmlTags.ATTR_RADIUS;
import static org.ebsdimage.io.exp.ops.hough.post.HoughCropXmlTags.TAG_NAME;
import static org.junit.Assert.assertEquals;

import org.ebsdimage.core.exp.ops.hough.post.HoughCrop;
import org.jdom.Element;
import org.junit.Before;
import org.junit.Test;

import ptpshared.utility.xml.JDomUtil;

public class HoughCropXmlSaverTest {

    private HoughCrop op;



    @Before
    public void setUp() throws Exception {
        op = new HoughCrop(8);
    }



    @Test
    public void testSaveHoughCrop() {
        Element element = new HoughCropXmlSaver().save(op);

        assertEquals(TAG_NAME, element.getName());
        assertEquals(8, JDomUtil.getIntegerFromAttribute(element, ATTR_RADIUS));
    }

}
