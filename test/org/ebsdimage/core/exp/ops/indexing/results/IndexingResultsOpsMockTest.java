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
package org.ebsdimage.core.exp.ops.indexing.results;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.ebsdimage.TestCase;
import org.ebsdimage.core.Solution;
import org.ebsdimage.core.exp.OpResult;
import org.junit.Before;
import org.junit.Test;

import ptpshared.core.math.Quaternion;
import ptpshared.util.xml.XmlLoader;
import ptpshared.util.xml.XmlSaver;
import crystallography.core.Crystal;
import crystallography.core.CrystalFactory;

public class IndexingResultsOpsMockTest extends TestCase {

    private IndexingResultsOpsMock op;

    private Solution[] srcSlns;



    @Before
    public void setUp() throws Exception {
        op = new IndexingResultsOpsMock();

        Crystal phase = CrystalFactory.silicon();
        Quaternion rotation = Quaternion.IDENTITY;
        srcSlns =
                new Solution[] { new Solution(phase, rotation, 0.0),
                        new Solution(phase, rotation, 1.0 / 6.0),
                        new Solution(phase, rotation, 1.0 / 3.0) };
    }



    @Test
    public void testCalculate() {
        double expected = 0.5;
        OpResult result = op.calculate(null, srcSlns)[0];

        assertEquals(expected, result.value.doubleValue(), 1e-6);
    }



    @Test
    public void testToString() {
        assertEquals(op.toString(), "IndexingResultsOpsMock []");
    }



    @Test
    public void testEqualsObject() {
        assertTrue(op.equals(op));
        assertFalse(op.equals(null));
        assertFalse(op.equals(new Object()));

        assertTrue(op.equals(new IndexingResultsOpsMock()));
    }



    @Test
    public void testEqualsObjectDouble() {
        assertTrue(op.equals(op, 1e-2));
        assertFalse(op.equals(null, 1e-2));
        assertFalse(op.equals(new Object(), 1e-2));

        assertTrue(op.equals(new IndexingResultsOpsMock(), 1e-2));
    }



    @Test
    public void testHashCode() {
        assertEquals(-1473789771, op.hashCode());
    }



    @Test
    public void testXML() throws Exception {
        File file = createTempFile();
        new XmlSaver().save(op, file);

        IndexingResultsOpsMock other =
                new XmlLoader().load(IndexingResultsOpsMock.class, file);
        assertAlmostEquals(op, other, 1e-6);
    }

}
