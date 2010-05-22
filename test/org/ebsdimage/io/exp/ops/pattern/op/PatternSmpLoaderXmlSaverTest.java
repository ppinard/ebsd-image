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

import static org.ebsdimage.io.exp.ops.pattern.op.PatternFileLoaderXmlTags.ATTR_FILEDIR;
import static org.ebsdimage.io.exp.ops.pattern.op.PatternFileLoaderXmlTags.ATTR_FILENAME;
import static org.ebsdimage.io.exp.ops.pattern.op.PatternOpXmlTags.ATTR_INDEX;
import static org.ebsdimage.io.exp.ops.pattern.op.PatternSmpLoaderXmlTags.TAG_NAME;
import static org.junit.Assert.assertEquals;

import java.io.File;

import org.ebsdimage.core.exp.ops.pattern.op.PatternSmpLoader;
import org.jdom.Element;
import org.junit.Before;
import org.junit.Test;

import ptpshared.utility.xml.JDomUtil;
import rmlshared.io.FileUtil;

public class PatternSmpLoaderXmlSaverTest {

    private PatternSmpLoader op;
    private File filepath;



    @Before
    public void setUp() throws Exception {
        filepath = FileUtil.getFile("org/ebsdimage/testdata/Project19.smp");

        if (filepath == null)
            throw new RuntimeException(
                    "File \"org/ebsdimage/testdata/Project19.smp\" "
                            + "cannot be found.");

        op = new PatternSmpLoader(45, filepath);
    }



    @Test
    public void testSave() {
        Element element = new PatternSmpLoaderXmlSaver().save(op);

        assertEquals(TAG_NAME, element.getName());
        assertEquals(45, JDomUtil.getIntegerFromAttribute(element, ATTR_INDEX));
        assertEquals(filepath.getParent(), JDomUtil.getStringFromAttribute(
                element, ATTR_FILEDIR));
        assertEquals(filepath.getName(), JDomUtil.getStringFromAttribute(
                element, ATTR_FILENAME));
    }

}
