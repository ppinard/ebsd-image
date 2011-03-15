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
package org.ebsdimage.core.exp.ops.indexing.post;

import java.io.File;

import org.apache.commons.math.geometry.Rotation;
import org.ebsdimage.TestCase;
import org.ebsdimage.core.Solution;
import org.junit.Before;
import org.junit.Test;

import ptpshared.util.simplexml.XmlLoader;
import ptpshared.util.simplexml.XmlSaver;
import crystallography.core.Crystal;
import crystallography.core.CrystalFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import static ptpshared.geom.Assert.assertEquals;

import static junittools.test.Assert.assertEquals;

public class IndexingPostOpsMockTest extends TestCase {

    private IndexingPostOpsMock op;

    private Solution[] srcSlns;



    @Before
    public void setUp() throws Exception {
        op = new IndexingPostOpsMock();

        Crystal phase = CrystalFactory.silicon();
        Rotation rotation = Rotation.IDENTITY;
        srcSlns =
                new Solution[] { new Solution(phase, rotation, 0.0),
                        new Solution(phase, rotation, 1.0 / 3.0),
                        new Solution(phase, rotation, 2.0 / 3.0) };
    }



    @Test
    public void testEqualsObject() {
        assertTrue(op.equals(op));
        assertFalse(op.equals(null));
        assertFalse(op.equals(new Object()));

        assertTrue(op.equals(new IndexingPostOpsMock()));
    }



    @Test
    public void testEqualsObjectDouble() {
        assertTrue(op.equals(op, 1e-2));
        assertFalse(op.equals(null, 1e-2));
        assertFalse(op.equals(new Object(), 1e-2));

        assertTrue(op.equals(new IndexingPostOpsMock(), 1e-2));
    }



    @Test
    public void testHashCode() {
        assertEquals(763266731, op.hashCode());
    }



    @Test
    public void testProcess() {
        Solution[] destSlns = op.process(null, srcSlns);

        assertEquals(3, destSlns.length);

        Crystal expectedPhase = CrystalFactory.silicon();
        for (Solution sln : destSlns) {
            assertEquals(expectedPhase, sln.phase, 1e-6);
            assertEquals(Rotation.IDENTITY, sln.rotation, 1e-6);
        }

        assertEquals(0.0, destSlns[0].fit, 1e-6);
        assertEquals(1.0 / 6.0, destSlns[1].fit, 1e-6);
        assertEquals(1.0 / 3.0, destSlns[2].fit, 1e-6);
    }



    @Test
    public void testToString() {
        assertEquals(op.toString(), "IndexingPostOpsMock []");
    }



    @Test
    public void testXML() throws Exception {
        File file = createTempFile();
        new XmlSaver().save(op, file);

        IndexingPostOpsMock other =
                new XmlLoader().load(IndexingPostOpsMock.class, file);
        assertEquals(op, other, 1e-6);
    }

}
