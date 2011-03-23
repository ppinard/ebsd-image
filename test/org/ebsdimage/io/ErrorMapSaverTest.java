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
import org.ebsdimage.core.ErrorCode;
import org.ebsdimage.core.ErrorMap;
import org.junit.Before;
import org.junit.Test;

import ptpshared.util.simplexml.XmlLoader;
import rmlimage.core.ByteMap;
import rmlimage.io.BasicBmpLoader;
import rmlshared.io.FileUtil;

import static org.junit.Assert.assertEquals;

public class ErrorMapSaverTest extends TestCase {

    private ErrorCode errorCode1;

    private ErrorCode errorCode2;

    private ErrorMapSaver saver;

    private ErrorMap map;

    private File file;



    @Before
    public void setUp() throws Exception {
        errorCode1 = new ErrorCode("Error1");
        errorCode2 = new ErrorCode("Error3", "Desc3");

        HashMap<Integer, ErrorCode> items = new HashMap<Integer, ErrorCode>();
        items.put(1, errorCode1);
        items.put(3, errorCode2);

        map = new ErrorMap(2, 2, new byte[] { 0, 1, 3, 1 }, items);
        saver = new ErrorMapSaver();

        file = new File(createTempDir(), "errormap.bmp");
    }



    private void testErrorMapXml() throws IOException {
        File xmlFile = FileUtil.setExtension(file, "xml");
        Map<Integer, ErrorCode> items =
                new XmlLoader().loadMap(Integer.class, ErrorCode.class, xmlFile);

        assertEquals(3, items.size());
        assertEquals(ErrorMap.NO_ERROR, items.get(0));
        assertEquals(errorCode1, items.get(1));
        assertEquals(errorCode2, items.get(3));
    }



    @Test
    public void testGetTaskProgress() {
        assertEquals(0.0, saver.getTaskProgress(), 1e-6);
    }



    private void testPixArray() throws IOException {
        ByteMap byteMap = (ByteMap) new BasicBmpLoader().load(file);

        assertEquals(0, byteMap.pixArray[0]);
        assertEquals(1, byteMap.pixArray[1]);
        assertEquals(3, byteMap.pixArray[2]);
        assertEquals(1, byteMap.pixArray[3]);
    }



    @Test
    public void testSaveObjectFile() throws IOException {
        saver.save((Object) map, file);

        testPixArray();
        testErrorMapXml();
    }



    @Test
    public void testSavePhasesMap() throws IOException {
        map.setFile(file);
        saver.save(map);

        testPixArray();
        testErrorMapXml();
    }



    @Test
    public void testSavePhasesMapFile() throws IOException {
        saver.save(map, file);

        testPixArray();
        testErrorMapXml();
    }

}
