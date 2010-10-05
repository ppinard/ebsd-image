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
package org.ebsdimage.core.exp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.ebsdimage.core.exp.ops.pattern.op.PatternFilesLoader;
import org.ebsdimage.core.exp.ops.pattern.op.PatternSmpLoader;
import org.junit.Before;
import org.junit.Test;

import rmlshared.io.FileUtil;

public class ExpUtilTest {

    @Before
    public void setUp() throws Exception {
    }



    @Test
    public void testCreateExps() {
        // TODO: Test create Exps in ExpUtil
        assertTrue(true);
    }



    @Test
    public void testCreatePatternOpFromSmp() throws IOException {
        File smpFile = FileUtil.getFile("org/ebsdimage/testdata/Project19.smp");

        PatternSmpLoader op = ExpUtil.createPatternOpFromSmp(smpFile);

        assertEquals(0, op.startIndex);
        assertEquals(4, op.size);
        assertEquals(smpFile.getParent(), op.filedir);
        assertEquals(smpFile.getName(), op.filename);
    }



    @Test
    public void testCreatePatternOpsFromDir() {
        File dir = FileUtil.getFile("org/ebsdimage/testdata/Project19/");

        PatternFilesLoader op = ExpUtil.createPatternOpFromDir(dir);

        assertEquals(0, op.startIndex);
        assertEquals(4, op.size);
        assertEquals(4, op.getFiles().length);
    }

}
