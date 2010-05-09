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
package org.ebsdimage.core;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.ebsdimage.TestCase;
import org.ebsdimage.io.HoughMapLoader;
import org.junit.Test;

import rmlimage.core.BinMap;
import rmlshared.io.FileUtil;
import rmlshared.util.Properties;
import crystallography.core.Crystal;
import crystallography.core.crystals.IronBCC;
import crystallography.core.crystals.Silicon;

public class ThresholdTest extends TestCase {

    @Test
    public void testAutomaticTopHat() throws IOException {
        // Do the operation
        HoughMap houghMap =
                new HoughMapLoader()
                        .load(FileUtil
                                .getFile("org/ebsdimage/testdata/houghmap_cropped.bmp"));

        BinMap destMap = Threshold.automaticTopHat(houghMap);

        // Test the pixArray
        BinMap expected =
                (BinMap) load("org/ebsdimage/testdata/automatic_top_hat.bmp");
        destMap.assertEquals(expected);

        // Test the properties
        Properties props = destMap.getProperties();
        assertEquals(10, props.getProperty("threshold.min", -1));
    }



    @Test
    public void testAutomaticStdDev() throws IOException {
        // Do the operation
        HoughMap houghMap =
                new HoughMapLoader()
                        .load(FileUtil
                                .getFile("org/ebsdimage/testdata/houghmap_cropped.bmp"));
        BinMap destMap = Threshold.automaticStdDev(houghMap);

        // Test the pixArray
        BinMap expected =
                (BinMap) load("org/ebsdimage/testdata/automatic_stddev.bmp");
        destMap.assertEquals(expected);

        // Test the properties
        Properties props = destMap.getProperties();
        assertEquals(128, props.getProperty("threshold.min", -1));
        assertEquals("2*sigma", props.getProperty(
                "EBSD.threshold.automatic.mode", ""));
        assertEquals(112.0010, props.getProperty(
                "EBSD.threshold.automatic.average", Double.NaN), 0.001);
        assertEquals(8.14176, props.getProperty(
                "EBSD.threshold.automatic.stdDev", Double.NaN), 0.001);
    }



    @Test
    public void testPhasePhasesMapIntBinMap() {
        PhasesMap srcMap = createPhasesMap();
        BinMap destMap = new BinMap(2, 2);

        // Test (phaseId = 0)
        Threshold.phase(srcMap, 0, destMap);
        assertEquals(2, destMap.width);
        assertEquals(2, destMap.height);
        assertEquals(1, destMap.pixArray[0]);
        assertEquals(0, destMap.pixArray[1]);
        assertEquals(0, destMap.pixArray[2]);
        assertEquals(0, destMap.pixArray[3]);

        // Test (phaseId = 1)
        Threshold.phase(srcMap, 1, destMap);
        assertEquals(2, destMap.width);
        assertEquals(2, destMap.height);
        assertEquals(0, destMap.pixArray[0]);
        assertEquals(1, destMap.pixArray[1]);
        assertEquals(0, destMap.pixArray[2]);
        assertEquals(1, destMap.pixArray[3]);

        // Test (phaseId = 2)
        Threshold.phase(srcMap, 2, destMap);
        assertEquals(2, destMap.width);
        assertEquals(2, destMap.height);
        assertEquals(0, destMap.pixArray[0]);
        assertEquals(0, destMap.pixArray[1]);
        assertEquals(1, destMap.pixArray[2]);
        assertEquals(0, destMap.pixArray[3]);
    }



    @Test
    public void testPhasePhasesMapInt() {
        PhasesMap srcMap = createPhasesMap();

        // Test (phaseId = 0)
        BinMap destMap = Threshold.phase(srcMap, 0);
        assertEquals(2, destMap.width);
        assertEquals(2, destMap.height);
        assertEquals(1, destMap.pixArray[0]);
        assertEquals(0, destMap.pixArray[1]);
        assertEquals(0, destMap.pixArray[2]);
        assertEquals(0, destMap.pixArray[3]);

        // Test (phaseId = 1)
        destMap = Threshold.phase(srcMap, 1);
        assertEquals(2, destMap.width);
        assertEquals(2, destMap.height);
        assertEquals(0, destMap.pixArray[0]);
        assertEquals(1, destMap.pixArray[1]);
        assertEquals(0, destMap.pixArray[2]);
        assertEquals(1, destMap.pixArray[3]);

        // Test (phaseId = 2)
        destMap = Threshold.phase(srcMap, 2);
        assertEquals(2, destMap.width);
        assertEquals(2, destMap.height);
        assertEquals(0, destMap.pixArray[0]);
        assertEquals(0, destMap.pixArray[1]);
        assertEquals(1, destMap.pixArray[2]);
        assertEquals(0, destMap.pixArray[3]);
    }



    private PhasesMap createPhasesMap() {
        Crystal[] phases = new Crystal[] { new Silicon(), new IronBCC() };
        byte[] pixArray = new byte[] { 0, 1, 2, 1 };

        return new PhasesMap(2, 2, pixArray, phases);
    }

}
