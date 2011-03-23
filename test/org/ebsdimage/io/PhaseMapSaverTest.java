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
package org.ebsdimage.io;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.ebsdimage.TestCase;
import org.ebsdimage.core.PhaseMap;
import org.junit.Before;
import org.junit.Test;

import ptpshared.util.simplexml.ApacheCommonMathMatcher;
import ptpshared.util.simplexml.XmlLoader;
import rmlimage.core.ByteMap;
import rmlimage.io.BasicBmpLoader;
import rmlshared.io.FileUtil;
import crystallography.core.Crystal;
import crystallography.core.CrystalFactory;
import crystallography.io.simplexml.SpaceGroupMatcher;

import static org.junit.Assert.assertEquals;

import static junittools.test.Assert.assertEquals;

public class PhaseMapSaverTest extends TestCase {

    private PhaseMapSaver saver;

    private PhaseMap phaseMap;

    private Crystal phase1;

    private Crystal phase2;

    private File file;



    @Before
    public void setUp() throws Exception {
        phase1 = CrystalFactory.silicon();
        phase2 = CrystalFactory.ferrite();

        HashMap<Integer, Crystal> items = new HashMap<Integer, Crystal>();
        items.put(1, phase1);
        items.put(3, phase2);

        phaseMap = new PhaseMap(2, 2, new byte[] { 0, 1, 3, 1 }, items);

        saver = new PhaseMapSaver();
        file = new File(createTempDir(), "phasemap.bmp");
    }



    @Test
    public void testGetTaskProgress() {
        assertEquals(0.0, saver.getTaskProgress(), 1e-6);
    }



    private void testPhasesMap() throws IOException {
        ByteMap byteMap = (ByteMap) new BasicBmpLoader().load(file);

        assertEquals(0, byteMap.pixArray[0]);
        assertEquals(1, byteMap.pixArray[1]);
        assertEquals(3, byteMap.pixArray[2]);
        assertEquals(1, byteMap.pixArray[3]);
    }



    private void testPhasesMapXml() throws IOException {
        File xmlFile = FileUtil.setExtension(file, "xml");
        XmlLoader loader = new XmlLoader();
        loader.matchers.registerMatcher(new ApacheCommonMathMatcher());
        loader.matchers.registerMatcher(new SpaceGroupMatcher());
        Map<Integer, Crystal> items =
                loader.loadMap(Integer.class, Crystal.class, xmlFile);

        assertEquals(3, items.size());
        assertEquals(PhaseMap.NO_PHASE, items.get(0), 1e-6);
        assertEquals(phase1, items.get(1), 1e-6);
        assertEquals(phase2, items.get(3), 1e-6);
    }



    @Test
    public void testSaveObjectFile() throws IOException {
        saver.save((Object) phaseMap, file);

        testPhasesMap();
        testPhasesMapXml();
    }



    @Test
    public void testSavePhasesMap() throws IOException {
        phaseMap.setFile(file);
        saver.save(phaseMap);

        testPhasesMap();
        testPhasesMapXml();
    }



    @Test
    public void testSavePhasesMapFile() throws IOException {
        saver.save(phaseMap, file);

        testPhasesMap();
        testPhasesMapXml();
    }

}
