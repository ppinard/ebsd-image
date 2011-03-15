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
package org.ebsdimage.core.exp.ops.detection.results;

import java.io.File;

import org.ebsdimage.TestCase;
import org.ebsdimage.core.exp.OpResult;
import org.junit.Before;
import org.junit.Test;

import ptpshared.util.simplexml.XmlLoader;
import ptpshared.util.simplexml.XmlSaver;
import rmlimage.core.BinMap;

import static org.junit.Assert.assertEquals;

import static junittools.test.Assert.assertEquals;

public class CountTest extends TestCase {

    private Count op;

    private BinMap peaksMap;



    @Before
    public void setUp() throws Exception {
        op = new Count();

        peaksMap =
                (BinMap) load(getFile("org/ebsdimage/testdata/peaksMap.bmp"));
    }



    @Test
    public void testCalculate() {
        OpResult[] results = op.calculate(null, peaksMap);

        assertEquals(1, results.length);
        assertEquals(8, results[0].value.intValue());
    }



    @Test
    public void testToString() {
        assertEquals("Count", op.toString());
    }



    @Test
    public void testXML() throws Exception {
        File file = createTempFile();
        new XmlSaver().save(op, file);

        Count other = new XmlLoader().load(Count.class, file);
        assertEquals(op, other, 1e-6);
    }

}
