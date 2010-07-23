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

import static org.ebsdimage.io.exp.ops.pattern.op.PatternFilesLoaderXmlTags.TAG_FILE;
import static org.ebsdimage.io.exp.ops.pattern.op.PatternFilesLoaderXmlTags.TAG_NAME;
import static org.ebsdimage.io.exp.ops.pattern.op.PatternOpXmlTags.ATTR_SIZE;
import static org.ebsdimage.io.exp.ops.pattern.op.PatternOpXmlTags.ATTR_START_INDEX;
import static org.junit.Assert.assertEquals;

import java.io.File;

import org.ebsdimage.core.exp.ops.pattern.op.PatternFilesLoader;
import org.jdom.Element;
import org.junit.Before;
import org.junit.Test;

import rmlshared.io.FileUtil;

public class PatternFilesLoaderXmlLoaderTest {

    private Element element;

    private File filepath;



    @Before
    public void setUp() throws Exception {
        filepath = FileUtil.getFile("org/ebsdimage/testdata/patternloader.bmp");

        element = new Element(TAG_NAME);
        element.setAttribute(ATTR_START_INDEX, Integer.toString(45));
        element.setAttribute(ATTR_SIZE, Integer.toString(2));
        element.addContent(new Element(TAG_FILE).setText(filepath.getAbsolutePath()));
        element.addContent(new Element(TAG_FILE).setText(filepath.getAbsolutePath()));
    }



    @Test
    public void testLoad() {
        PatternFilesLoader op = new PatternFilesLoaderXmlLoader().load(element);

        assertEquals(TAG_NAME, op.getName());
        assertEquals(45, op.startIndex);
        assertEquals(2, op.size);
        assertEquals(2, op.getFiles().length);
        assertEquals(filepath.getAbsolutePath(),
                op.getFiles()[0].getAbsolutePath());
        assertEquals(filepath.getAbsolutePath(),
                op.getFiles()[1].getAbsolutePath());
    }

}
