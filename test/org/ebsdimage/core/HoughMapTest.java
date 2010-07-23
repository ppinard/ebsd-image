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

import static java.lang.Math.toRadians;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import rmlimage.core.LUT;

public class HoughMapTest {

    @Test
    public void testHoughMapDoubleDoubleDouble() {
        // Even increments
        HoughMap map = new HoughMap(400, 230 * toRadians(0.5), toRadians(0.5));
        assertEquals(360, map.width);
        assertEquals(401, map.height);
        assertEquals(-401.426, map.rMin, 0.001);
        assertEquals(401.426, map.rMax, 0.001);
        assertEquals(2.007, map.deltaR, 0.001);
        assertEquals(0.0, map.thetaMin, 0.001);
        assertEquals(toRadians(179.5), map.thetaMax, 0.001);
        assertEquals(toRadians(0.5), map.deltaTheta, 0.001);

        // Odd increments
        map = new HoughMap(400, 230 * toRadians(1.1), toRadians(1.1));
        assertEquals(164, map.width);
        assertEquals(183, map.height);
        assertEquals(-401.827, map.rMin, 0.001);
        assertEquals(401.827, map.rMax, 0.001);
        assertEquals(4.416, map.deltaR, 0.001);
        assertEquals(0.0, map.thetaMin, 0.001);
        assertEquals(toRadians(179.3), map.thetaMax, 0.001);
        assertEquals(toRadians(1.1), map.deltaTheta, 0.001);
    }



    @Test
    public void testHoughMapIntIntDoubleDouble() {
        // Even increments
        HoughMap map = new HoughMap(360, 401, 2.00712864, toRadians(0.5));
        assertEquals(360, map.width);
        assertEquals(401, map.height);
        assertEquals(-401.426, map.rMin, 0.001);
        assertEquals(401.426, map.rMax, 0.001);
        assertEquals(2.007, map.deltaR, 0.001);
        assertEquals(0.0, map.thetaMin, 0.001);
        assertEquals(toRadians(179.5), map.thetaMax, 0.001);
        assertEquals(toRadians(0.5), map.deltaTheta, 0.001);

        // Odd increments
        map = new HoughMap(164, 185, 4.415683008, toRadians(1.1));
        assertEquals(164, map.width);
        assertEquals(185, map.height);
        assertEquals(-406.243, map.rMin, 0.001);
        assertEquals(406.243, map.rMax, 0.001);
        assertEquals(4.416, map.deltaR, 0.001);
        assertEquals(0.0, map.thetaMin, 0.001);
        assertEquals(toRadians(179.3), map.thetaMax, 0.001);
        assertEquals(toRadians(1.1), map.deltaTheta, 0.001);
    }



    @Test
    public void testCalculateHeight() {
        assertEquals(401, HoughMap.calculateHeight(400, 230 * toRadians(0.5)));
        assertEquals(135, HoughMap.calculateHeight(400, 230 * toRadians(1.5)));
    }



    @Test(expected = Exception.class)
    public void testCalculateHeightException1() {
        // Negative rMax
        HoughMap.calculateHeight(-10, 0.1);
    }



    @Test(expected = Exception.class)
    public void testCalculateHeightException2() {
        // rMax = 0
        HoughMap.calculateHeight(0, 0.1);
    }



    @Test(expected = Exception.class)
    public void testCalculateHeightException3() {
        // Negative deltaTheta
        HoughMap.calculateHeight(100, -0.1);
    }



    @Test(expected = Exception.class)
    public void testCalculateHeightException5() {
        // deltaTheta = 0
        HoughMap.calculateHeight(100, 0);
    }



    @Test
    public void testCalculateWidth() {
        assertEquals(360, HoughMap.calculateWidth(toRadians(0.5)));
        assertEquals(164, HoughMap.calculateWidth(toRadians(1.1)));
    }



    @Test(expected = IllegalArgumentException.class)
    public void testCalculateWidthException1() {
        // Negative deltaTheta
        HoughMap.calculateWidth(-toRadians(0.5));
    }



    @Test(expected = IllegalArgumentException.class)
    public void testCalculateWidthException2() {
        // deltaTheta = 0
        HoughMap.calculateWidth(0);
    }



    @Test
    public void testDuplicate() {
        HoughMap map = new HoughMap(50, 1.5, toRadians(1.1));
        HoughMap dup = map.duplicate();

        assertEquals(map.rMin, dup.rMin, 0.00001);
        assertEquals(map.rMax, dup.rMax, 0.00001);
        assertEquals(map.deltaR, dup.deltaR, 0.00001);
        assertEquals(map.thetaMin, dup.thetaMin, 0.00001);
        assertEquals(map.thetaMax, dup.thetaMax, 0.00001);
        assertEquals(map.deltaTheta, dup.deltaTheta, 0.00001);

        assertEquals(map.width, dup.width);
        assertEquals(map.height, dup.height);

        dup.assertEquals(map);
    }



    @Test
    public void testGetIndex() {
        // Even increments
        HoughMap map = new HoughMap(400, 230 * toRadians(0.5), toRadians(0.5));
        assertEquals(88721, map.getIndex(-92.328, toRadians(80.5)));
        assertEquals(0, map.getIndex(401.425, toRadians(0)));
        assertEquals(144359, map.getIndex(-401.425, toRadians(179.5)));

        // Odd increments
        map = new HoughMap(400, 230 * toRadians(1.1), toRadians(1.1));
        assertEquals(2533, map.getIndex(335.592 - 2.200, toRadians(80.3)));
        assertEquals(2533, map.getIndex(335.592, toRadians(80.3 - 0.54)));
        assertEquals(2533, map.getIndex(335.592, toRadians(80.3)));
        assertEquals(2533, map.getIndex(335.592, toRadians(80.3 + 0.54)));
        assertEquals(2533, map.getIndex(335.592 + 2.200, toRadians(80.3)));

        assertEquals(0, map.getIndex(401.827, toRadians(0)));
        assertEquals(30011, map.getIndex(-401.827, toRadians(179.29)));
    }



    @Test(expected = Exception.class)
    public void testGetIndexException1() {
        // Test theta > max
        HoughMap map = new HoughMap(400, 230 * toRadians(0.5), toRadians(0.5));
        map.getIndex(25.0, toRadians(180));
    }



    @Test(expected = Exception.class)
    public void testGetIndexException2() {
        // Test theta < min
        HoughMap map = new HoughMap(400, 230 * toRadians(0.5), toRadians(0.5));
        map.getIndex(25.0, toRadians(-1));
    }



    @Test(expected = Exception.class)
    public void testGetIndexException3() {
        // Test r > max
        HoughMap map = new HoughMap(400, 230 * toRadians(0.5), toRadians(0.5));
        map.getIndex(403.0, 1.0);
    }



    @Test(expected = Exception.class)
    public void testGetIndexException4() {
        // Test negative r < min
        HoughMap map = new HoughMap(400, 230 * toRadians(0.5), toRadians(0.5));
        System.out.println(map.deltaR);
        map.getIndex(-403, 1.0);
    }



    @Test
    public void testGetLUT() {
        HoughMap map = new HoughMap(400, 230 * toRadians(0.5), toRadians(0.5));
        map.getLUT().assertEquals(LUT.createGreyscaleLUT());
    }



    @Test
    public void testGetValidFileFormats() {
        HoughMap map = new HoughMap(400, 230 * toRadians(0.5), toRadians(0.5));
        String[] validFormats = map.getValidFileFormats();
        assertEquals(1, validFormats.length);
        assertEquals("bmp", validFormats[0]);
    }



    @Test
    public void testGetR() {
        // Even increments
        HoughMap map = new HoughMap(400, 230 * toRadians(0.5), toRadians(0.5));
        assertEquals(-92.328, map.getR(88721), 0.001);

        // Odd increments
        map = new HoughMap(400, 230 * toRadians(1.1), toRadians(1.1));
        assertEquals(331.176, map.getR(2697), 0.001);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testGetRException1() {
        // Test negative index
        HoughMap map = new HoughMap(400, 230 * toRadians(0.5), toRadians(0.5));
        map.getR(-1);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testGetRException2() {
        // Test index > max
        HoughMap map = new HoughMap(400, 230 * toRadians(0.5), toRadians(0.5));
        map.getR(144360);
    }



    @Test
    public void testGetTheta() {
        // Even increments
        HoughMap map = new HoughMap(400, 230 * toRadians(0.5), toRadians(0.5));
        assertEquals(toRadians(80.5), map.getTheta(88721), 0.001);

        // Odd increments
        map = new HoughMap(400, 230 * toRadians(1.1), toRadians(1.1));
        assertEquals(toRadians(80.3), map.getTheta(2697), 0.001);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testGetThetaException1() {
        // Test negative index
        HoughMap map = new HoughMap(400, 230 * toRadians(0.5), toRadians(0.5));
        map.getTheta(-1);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testGetThetaException2() {
        // Test index > max
        HoughMap map = new HoughMap(400, 230 * toRadians(0.5), toRadians(0.5));
        map.getTheta(360360);
    }



    /*
     * @Test public void getTheta4() { // Even increments
     * assertEquals(toRadians(80.5), HoughMap.getTheta(88721, 360,
     * toRadians(0.5)), 0.001); // Odd increments assertEquals(toRadians(80.3),
     * HoughMap.getTheta(2697, 164, toRadians(1.1)), 0.001); }
     */

    @Test
    public void testGetX() {
        HoughMap map = new HoughMap(400, 230 * toRadians(0.5), toRadians(0.5));
        assertEquals(160, map.getX(toRadians(80.24)));
        assertEquals(161, map.getX(toRadians(80.26)));
        assertEquals(161, map.getX(toRadians(80.5)));
        assertEquals(161, map.getX(toRadians(80.74)));
        assertEquals(162, map.getX(toRadians(80.76)));
        assertEquals(0, map.getX(toRadians(0)));
    }



    @Test(expected = IllegalArgumentException.class)
    public void testGetXException1() {
        // Test theta < min
        HoughMap map = new HoughMap(400, 230 * toRadians(0.5), toRadians(0.5));
        map.getX(-1);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testGetXException2() {
        // Test theta > max
        HoughMap map = new HoughMap(400, 230 * toRadians(0.5), toRadians(0.5));
        map.getX(toRadians(180));
    }



    @Test
    public void testGetY() {
        HoughMap map = new HoughMap(400, 230 * toRadians(0.5), toRadians(0.5));

        assertEquals(0, map.getY(401.425));

        assertEquals(147, map.getY(107.380));
        assertEquals(146, map.getY(107.382));
        assertEquals(146, map.getY(108.385));
        assertEquals(146, map.getY(109.380));
        assertEquals(145, map.getY(109.390));

        assertEquals(253, map.getY(-107.380));
        assertEquals(254, map.getY(-107.382));
        assertEquals(254, map.getY(-108.385));
        assertEquals(254, map.getY(-109.380));
        assertEquals(255, map.getY(-109.390));

        assertEquals(400, map.getY(-401.425));
    }



    @Test(expected = IllegalArgumentException.class)
    public void testGetYException1() {
        // Test r < min
        HoughMap map = new HoughMap(400, 230 * toRadians(0.5), toRadians(0.5));
        map.getY(-405);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testGetYException2() {
        // Test r > max
        HoughMap map = new HoughMap(400, 230 * toRadians(0.5), toRadians(0.5));
        map.getY(405);
    }

}
