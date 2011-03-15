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
package org.ebsdimage.core.exp.ops.pattern.results;

import java.io.File;

import org.ebsdimage.TestCase;
import org.ebsdimage.core.exp.OpResult;
import org.junit.Before;
import org.junit.Test;

import ptpshared.util.simplexml.XmlLoader;
import ptpshared.util.simplexml.XmlSaver;
import rmlimage.core.ByteMap;

import static org.junit.Assert.assertEquals;

import static junittools.test.Assert.assertEquals;

public class FourierTest extends TestCase {

    private Fourier op;



    @Before
    public void setUp() throws Exception {
        op = new Fourier();
    }



    @Test
    public void testCalculate() {
        ByteMap srcMap = (ByteMap) load("org/ebsdimage/testdata/srcMap.bmp");
        OpResult result = op.calculate(null, srcMap)[0];

        assertEquals(0.63938926, result.value.doubleValue(), 1e-7);
    }



    @Test
    public void testCalculate2() {
        ByteMap srcMap =
                (ByteMap) load("org/ebsdimage/testdata/pattern_masked.bmp");
        OpResult result = op.calculate(null, srcMap)[0];

        assertEquals(0.60047537134, result.value.doubleValue(), 1e-7);
    }



    @Test
    public void testToString() {
        assertEquals(op.toString(), "Fourier");
    }



    @Test
    public void testXML() throws Exception {
        File file = createTempFile();
        new XmlSaver().save(op, file);

        Fourier other = new XmlLoader().load(Fourier.class, file);
        assertEquals(op, other, 1e-6);
    }

}
