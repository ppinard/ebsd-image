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
package org.ebsdimage.io.exp.ops.detection.post;

import static org.ebsdimage.io.exp.ops.detection.post.CleanEdgeXmlTags.TAG_NAME;
import static org.junit.Assert.assertEquals;

import org.ebsdimage.core.run.Operation;
import org.ebsdimage.io.exp.ops.detection.post.CleanEdgeXmlLoader;
import org.jdom.Element;
import org.junit.Before;
import org.junit.Test;


public class CleanEdgeXmlLoaderTest {

    private Element element;



    @Before
    public void setUp() throws Exception {
        element = new Element(TAG_NAME);
    }



    @Test
    public void testLoad() {
        Operation op = new CleanEdgeXmlLoader().load(element);

        assertEquals(TAG_NAME, op.getName());
    }

}