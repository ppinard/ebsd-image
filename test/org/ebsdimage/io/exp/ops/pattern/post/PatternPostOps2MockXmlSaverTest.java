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

import static org.ebsdimage.io.exp.ops.pattern.post.PatternPostOps2MockXmlTags.ATTR_VAR;
import static org.ebsdimage.io.exp.ops.pattern.post.PatternPostOps2MockXmlTags.TAG_NAME;
import static org.junit.Assert.assertEquals;

import org.ebsdimage.core.exp.ops.pattern.post.PatternPostOps2Mock;
import org.ebsdimage.core.run.Operation;
import org.jdom.Element;
import org.junit.Before;
import org.junit.Test;

import ptpshared.utility.xml.JDomUtil;

public class PatternPostOps2MockXmlSaverTest {

    private Operation op;



    @Before
    public void setUp() throws Exception {
        op = new PatternPostOps2Mock(3);
    }



    @Test
    public void testSaveObjectXml() {
        Element element = new PatternPostOps2MockXmlSaver().save(op);

        assertEquals(TAG_NAME, element.getName());
        assertEquals(3, JDomUtil.getIntegerFromAttribute(element, ATTR_VAR));
    }



    @Test
    public void testSaveDetectionOpMock() {
        Element element = new PatternPostOps2MockXmlSaver().save(op);

        assertEquals(TAG_NAME, element.getName());
        assertEquals(3, JDomUtil.getIntegerFromAttribute(element, ATTR_VAR));
    }

}
