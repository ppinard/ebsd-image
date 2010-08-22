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

import static org.ebsdimage.io.PhasesMapXmlTags.TAG_NAME;
import static org.ebsdimage.io.PhasesMapXmlTags.TAG_PHASES;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.ebsdimage.core.PhasesMap;
import org.jdom.Element;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ptpshared.utility.xml.JDomUtil;
import rmlimage.core.ByteMap;
import rmlimage.io.BasicBmpLoader;
import rmlshared.io.FileUtil;
import crystallography.core.Crystal;
import crystallography.core.crystals.IronBCC;
import crystallography.core.crystals.Silicon;

public class PhasesMapSaverTest {

    private PhasesMapSaver saver;

    private PhasesMap map;

    private File file;



    @Before
    public void setUp() throws Exception {
        Crystal[] phases = new Crystal[] { new Silicon(), new IronBCC() };
        byte[] pixArray = new byte[] { 0, 1, 2, 1 };

        map = new PhasesMap(2, 2, pixArray, phases);
        saver = new PhasesMapSaver();
        file = new File(FileUtil.getTempDirFile(), "phasesmap.bmp");
    }



    @After
    public void tearDown() throws Exception {
        if (file.exists())
            if (!file.delete())
                throw new RuntimeException("File (" + file.getAbsolutePath()
                        + ") could not be deleted.");

        File xmlFile = FileUtil.setExtension(file, "xml");
        if (xmlFile.exists())
            if (!xmlFile.delete())
                throw new RuntimeException("File (" + xmlFile.getAbsolutePath()
                        + ") could not be deleted.");
    }



    @Test
    public void testGetTaskProgress() {
        assertEquals(0.0, saver.getTaskProgress(), 1e-6);
    }



    @Test
    public void testSaveObjectFile() throws IOException {
        saver.save((Object) map, file);

        testPhasesMap();
        testPhasesMapXml();
    }



    @Test
    public void testSavePhasesMapFile() throws IOException {
        saver.save(map, file);

        testPhasesMap();
        testPhasesMapXml();
    }



    @Test
    public void testSavePhasesMap() throws IOException {
        map.setFile(file);
        saver.save(map);

        testPhasesMap();
        testPhasesMapXml();
    }



    private void testPhasesMap() throws IOException {
        ByteMap byteMap = (ByteMap) new BasicBmpLoader().load(file);

        assertEquals(0, byteMap.pixArray[0]);
        assertEquals(1, byteMap.pixArray[1]);
        assertEquals(2, byteMap.pixArray[2]);
        assertEquals(1, byteMap.pixArray[3]);
    }



    private void testPhasesMapXml() throws IOException {
        File xmlFile = FileUtil.setExtension(file, "xml");
        Element element = JDomUtil.loadXML(xmlFile).getRootElement();

        assertEquals(TAG_NAME, element.getName());
        assertTrue(JDomUtil.hasChild(element, TAG_PHASES));

        Element child = JDomUtil.getChild(element, TAG_PHASES);
        assertEquals(2, child.getChildren().size());
    }

}
