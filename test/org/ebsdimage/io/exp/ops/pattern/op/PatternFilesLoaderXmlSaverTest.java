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

import static org.ebsdimage.io.exp.ops.pattern.op.PatternFilesLoaderXmlTags.TAG_NAME;
import static org.ebsdimage.io.exp.ops.pattern.op.PatternOpXmlTags.ATTR_SIZE;
import static org.ebsdimage.io.exp.ops.pattern.op.PatternOpXmlTags.ATTR_START_INDEX;
import static org.junit.Assert.assertEquals;

import java.io.File;

import org.ebsdimage.core.exp.ops.pattern.op.PatternFilesLoader;
import org.jdom.Element;
import org.junit.Before;
import org.junit.Test;

import ptpshared.utility.xml.JDomUtil;
import rmlshared.io.FileUtil;

public class PatternFilesLoaderXmlSaverTest {

    private PatternFilesLoader op;

    private File[] files;



    @Before
    public void setUp() throws Exception {
        files =
                new File[] {
                        FileUtil.getFile("org/ebsdimage/testdata/patternloader.bmp"),
                        FileUtil.getFile("org/ebsdimage/testdata/patternloader.bmp") };

        op = new PatternFilesLoader(45, files);
    }



    @Test
    public void testSavePatternFileLoader() {
        Element element = new PatternFilesLoaderXmlSaver().save(op);

        assertEquals(TAG_NAME, element.getName());
        assertEquals(45, JDomUtil.getIntegerFromAttribute(element,
                ATTR_START_INDEX));
        assertEquals(2, JDomUtil.getIntegerFromAttribute(element, ATTR_SIZE));
        assertEquals(2, element.getChildren().size());
    }

}