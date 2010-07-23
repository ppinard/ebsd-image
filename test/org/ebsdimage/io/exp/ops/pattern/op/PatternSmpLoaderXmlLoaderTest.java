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

import static org.ebsdimage.io.exp.ops.pattern.op.PatternOpXmlTags.ATTR_SIZE;
import static org.ebsdimage.io.exp.ops.pattern.op.PatternOpXmlTags.ATTR_START_INDEX;
import static org.ebsdimage.io.exp.ops.pattern.op.PatternSmpLoaderXmlTags.ATTR_FILEDIR;
import static org.ebsdimage.io.exp.ops.pattern.op.PatternSmpLoaderXmlTags.ATTR_FILENAME;
import static org.ebsdimage.io.exp.ops.pattern.op.PatternSmpLoaderXmlTags.TAG_NAME;
import static org.junit.Assert.assertEquals;

import java.io.File;

import org.ebsdimage.core.exp.ops.pattern.op.PatternSmpLoader;
import org.jdom.Element;
import org.junit.Before;
import org.junit.Test;

import rmlshared.io.FileUtil;

public class PatternSmpLoaderXmlLoaderTest {

    private Element element;

    private File filepath;



    @Before
    public void setUp() throws Exception {
        filepath = FileUtil.getFile("org/ebsdimage/testdata/Project19.smp");

        if (filepath == null)
            throw new RuntimeException(
                    "File \"org/ebsdimage/testdata/Project19.smp\" "
                            + "cannot be found.");

        element = new Element(TAG_NAME);
        element.setAttribute(ATTR_START_INDEX, Integer.toString(45));
        element.setAttribute(ATTR_SIZE, Integer.toString(4));
        element.setAttribute(ATTR_FILEDIR, filepath.getParent());
        element.setAttribute(ATTR_FILENAME, filepath.getName());
    }



    @Test
    public void testLoad() {
        PatternSmpLoader op = new PatternSmpLoaderXmlLoader().load(element);

        assertEquals(TAG_NAME, op.getName());
        assertEquals(45, op.startIndex);
        assertEquals(4, op.size);
        assertEquals(filepath.getParent(), op.filedir);
        assertEquals(filepath.getName(), op.filename);
    }

}
