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
package org.ebsdimage.io.exp.ops.pattern.op;

import static org.ebsdimage.io.exp.ops.pattern.op.PatternOpMockXmlTags.TAG_NAME;
import static org.ebsdimage.io.exp.ops.pattern.op.PatternOpXmlTags.ATTR_SIZE;
import static org.ebsdimage.io.exp.ops.pattern.op.PatternOpXmlTags.ATTR_START_INDEX;
import static org.junit.Assert.assertEquals;

import org.ebsdimage.core.exp.ops.pattern.op.PatternOpMock;
import org.jdom.Element;
import org.junit.Before;
import org.junit.Test;

public class PatternOpMockXmlLoaderTest {

    private Element element;



    @Before
    public void setUp() throws Exception {
        element = new Element(TAG_NAME);

        element.setAttribute(ATTR_START_INDEX, Integer.toString(0));
        element.setAttribute(ATTR_SIZE, Integer.toString(1));
    }



    @Test
    public void testLoad() {
        PatternOpMock op = new PatternOpMockXmlLoader().load(element);

        assertEquals(TAG_NAME, op.getName());
        assertEquals(0, op.startIndex);
        assertEquals(1, op.size);
    }

}
