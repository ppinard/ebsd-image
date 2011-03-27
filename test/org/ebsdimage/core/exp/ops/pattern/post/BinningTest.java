/*
 * EBSD-Image
 * Copyright (C) 2010-2011 Philippe T. Pinard
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
package org.ebsdimage.core.exp.ops.pattern.post;

import java.io.File;

import org.ebsdimage.TestCase;
import org.junit.Before;
import org.junit.Test;

import ptpshared.util.simplexml.XmlLoader;
import ptpshared.util.simplexml.XmlSaver;
import rmlimage.core.ByteMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import static junittools.test.Assert.assertEquals;

public class BinningTest extends TestCase {

    private Binning op;



    @Before
    public void setUp() throws Exception {
        op = new Binning(2);
    }



    @Test
    public void testBinningInt() {
        assertEquals(2, op.factor);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testBinningIntException() {
        new Binning(0);
    }



    @Test
    public void testEquals() {
        Binning other = new Binning(2);
        assertFalse(op == other);
        assertEquals(op, other);
    }



    @Test
    public void testEqualsObject() {
        assertTrue(op.equals(op));
        assertFalse(op.equals(null));
        assertFalse(op.equals(new Object()));

        assertFalse(op.equals(new Binning(4)));
        assertTrue(op.equals(new Binning(2)));
    }



    @Test
    public void testHashCode() {
        assertEquals(1014002952, op.hashCode());
    }



    @Test
    public void testProcess() {
        ByteMap srcMap = (ByteMap) load("org/ebsdimage/testdata/srcMap.bmp");
        ByteMap destMap = op.process(null, srcMap);

        ByteMap expectedMap =
                (ByteMap) load("org/ebsdimage/testdata/binning.bmp");
        destMap.assertEquals(expectedMap);
        assertEquals(destMap.width, srcMap.width / 2);
    }



    @Test
    public void testToString() {
        assertEquals(op.toString(), "Binning [factor=2]");
    }



    @Test
    public void testXML() throws Exception {
        File file = createTempFile();
        new XmlSaver().save(op, file);

        Binning other = new XmlLoader().load(Binning.class, file);
        assertEquals(op, other, 1e-6);
    }

}
