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

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import org.ebsdimage.TestCase;
import org.ebsdimage.core.ErrorCode;
import org.ebsdimage.core.ErrorMap;
import org.junit.Before;
import org.junit.Test;

import ptpshared.util.xml.XmlLoader;
import rmlimage.core.ByteMap;
import rmlimage.io.BasicBmpLoader;
import rmlshared.io.FileUtil;

public class ErrorMapSaverTest extends TestCase {

    private ErrorMapSaver saver;

    private ErrorMap map;

    private File file;



    @Before
    public void setUp() throws Exception {
        ErrorCode[] codes =
                new ErrorCode[] {
                        new ErrorCode(1, "Error1", "First test error"),
                        new ErrorCode(2, "Error2", "Second test error") };
        byte[] pixArray = new byte[] { 0, 1, 2, 1 };

        map = new ErrorMap(2, 2, pixArray, codes);
        saver = new ErrorMapSaver();
        file = new File(createTempDir(), "errormap.bmp");
    }



    @Test
    public void testGetTaskProgress() {
        assertEquals(0.0, saver.getTaskProgress(), 1e-6);
    }



    @Test
    public void testSaveObjectFile() throws IOException {
        saver.save((Object) map, file);

        testPixArray();
        testErrorMapXml();
    }



    @Test
    public void testSavePhasesMapFile() throws IOException {
        saver.save(map, file);

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



    private void testPixArray() throws IOException {
        ByteMap byteMap = (ByteMap) new BasicBmpLoader().load(file);

        assertEquals(0, byteMap.pixArray[0]);
        assertEquals(1, byteMap.pixArray[1]);
        assertEquals(2, byteMap.pixArray[2]);
        assertEquals(1, byteMap.pixArray[3]);
    }



    private void testErrorMapXml() throws IOException {
        File xmlFile = FileUtil.setExtension(file, "xml");
        ErrorCode[] codes = new XmlLoader().loadArray(ErrorCode.class, xmlFile);

        assertEquals(3, codes.length);
    }

}