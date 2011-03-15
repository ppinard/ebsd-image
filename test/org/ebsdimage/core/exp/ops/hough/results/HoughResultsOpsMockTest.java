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
package org.ebsdimage.core.exp.ops.hough.results;

import java.io.File;

import org.ebsdimage.TestCase;
import org.ebsdimage.core.HoughMap;
import org.ebsdimage.core.exp.OpResult;
import org.junit.Before;
import org.junit.Test;

import ptpshared.util.simplexml.XmlLoader;
import ptpshared.util.simplexml.XmlSaver;

import static org.junit.Assert.assertEquals;

import static junittools.test.Assert.assertEquals;

public class HoughResultsOpsMockTest extends TestCase {

    private HoughResultsOpsMock op;

    private HoughMap srcMap;



    @Before
    public void setUp() throws Exception {
        op = new HoughResultsOpsMock();

        srcMap = new HoughMap(4, 3, 1, 1);
        byte[] srcPixArray = new byte[] { 6, 10, 14, 18 };
        for (int i = 0; i < srcMap.size; i++)
            srcMap.pixArray[i] = srcPixArray[i % 4];
    }



    @Test
    public void testCalculate() {
        double expected = 144;
        OpResult result = op.calculate(null, srcMap)[0];
        assertEquals(expected, result.value.doubleValue(), 1e-6);
    }



    @Test
    public void testXML() throws Exception {
        File file = createTempFile();
        new XmlSaver().save(op, file);

        HoughResultsOpsMock other =
                new XmlLoader().load(HoughResultsOpsMock.class, file);
        assertEquals(op, other, 1e-6);
    }

}
