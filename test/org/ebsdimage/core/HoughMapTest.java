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

import magnitude.core.Magnitude;

import org.ebsdimage.TestCase;
import org.junit.Test;

import rmlimage.core.ByteMap;
import rmlimage.core.Calibration;
import rmlimage.core.LUT;
import rmlimage.core.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import static java.lang.Math.toRadians;

public class HoughMapTest extends TestCase {

    @Test
    public void testHoughMapDoubleDoubleDouble() {
        // Even increments
        HoughMap map = new HoughMap(toRadians(0.5), 230 * toRadians(0.5), 400);
        assertEquals(360, map.width);
        assertEquals(401, map.height);
        assertEquals(-401.426, map.rhoMin.getValue("px"), 1e-3);
        assertEquals(401.426, map.rhoMax.getValue("px"), 1e-3);
        assertEquals(2.007, map.getDeltaRho().getValue("px"), 1e-3);
        assertEquals(0.0, map.thetaMin.getValue("rad"), 1e-3);
        assertEquals(toRadians(179.5), map.thetaMax.getValue("rad"), 1e-3);
        assertEquals(toRadians(0.5), map.getDeltaTheta().getValue("rad"), 1e-3);

        // Odd increments
        map = new HoughMap(toRadians(1.1), 230 * toRadians(1.1), 400);
        assertEquals(164, map.width);
        assertEquals(183, map.height);
        assertEquals(-401.827, map.rhoMin.getValue("px"), 1e-3);
        assertEquals(401.827, map.rhoMax.getValue("px"), 1e-3);
        assertEquals(4.416, map.getDeltaRho().getValue("px"), 1e-3);
        assertEquals(0.0, map.thetaMin.getValue("rad"), 1e-3);
        assertEquals(toRadians(179.3), map.thetaMax.getValue("rad"), 1e-3);
        assertEquals(toRadians(1.1), map.getDeltaTheta().getValue("rad"), 1e-3);
    }



    @Test
    public void testHoughMapIntIntDoubleDouble() {
        // Even increments
        HoughMap map = new HoughMap(360, 401, toRadians(0.5), 2.00712864);
        assertEquals(360, map.width);
        assertEquals(401, map.height);
        assertEquals(-401.426, map.rhoMin.getValue("px"), 1e-3);
        assertEquals(401.426, map.rhoMax.getValue("px"), 1e-3);
        assertEquals(2.007, map.getDeltaRho().getValue("px"), 1e-3);
        assertEquals(0.0, map.thetaMin.getValue("rad"), 1e-3);
        assertEquals(toRadians(179.5), map.thetaMax.getValue("rad"), 1e-3);
        assertEquals(toRadians(0.5), map.getDeltaTheta().getValue("rad"), 1e-3);

        // Odd increments
        map = new HoughMap(164, 185, toRadians(1.1), 4.415683008);
        assertEquals(164, map.width);
        assertEquals(185, map.height);
        assertEquals(-406.243, map.rhoMin.getValue("px"), 1e-3);
        assertEquals(406.243, map.rhoMax.getValue("px"), 1e-3);
        assertEquals(4.416, map.getDeltaRho().getValue("px"), 1e-3);
        assertEquals(0.0, map.thetaMin.getValue("rad"), 1e-3);
        assertEquals(toRadians(179.3), map.thetaMax.getValue("rad"), 1e-3);
        assertEquals(toRadians(1.1), map.getDeltaTheta().getValue("rad"), 1e-3);
    }



    @Test
    public void testCalculateHeight() {
        Magnitude rMax = new Magnitude(400, "px");
        Magnitude deltaR = new Magnitude(230 * toRadians(0.5), "px");
        assertEquals(401, HoughMap.calculateHeight(rMax, deltaR));

        rMax = new Magnitude(400, "px");
        deltaR = new Magnitude(230 * toRadians(1.5), "px");
        assertEquals(135, HoughMap.calculateHeight(rMax, deltaR));
    }



    @Test(expected = Exception.class)
    public void testCalculateHeightException1() {
        // Negative rMax
        Magnitude rMax = new Magnitude(-10, "px");
        Magnitude deltaR = new Magnitude(0.1, "px");
        HoughMap.calculateHeight(rMax, deltaR);
    }



    @Test(expected = Exception.class)
    public void testCalculateHeightException2() {
        // rMax = 0
        Magnitude rMax = new Magnitude(0, "px");
        Magnitude deltaR = new Magnitude(0.1, "px");
        HoughMap.calculateHeight(rMax, deltaR);
    }



    @Test(expected = Exception.class)
    public void testCalculateHeightException3() {
        // Negative deltaTheta
        Magnitude rMax = new Magnitude(100, "px");
        Magnitude deltaR = new Magnitude(-0.1, "px");
        HoughMap.calculateHeight(rMax, deltaR);
    }



    @Test(expected = Exception.class)
    public void testCalculateHeightException5() {
        // deltaTheta = 0
        Magnitude rMax = new Magnitude(100, "px");
        Magnitude deltaR = new Magnitude(0, "px");
        HoughMap.calculateHeight(rMax, deltaR);
    }



    @Test
    public void testCalculateWidth() {
        Magnitude deltaTheta = new Magnitude(toRadians(0.5), "rad");
        assertEquals(360, HoughMap.calculateWidth(deltaTheta));

        deltaTheta = new Magnitude(toRadians(1.1), "rad");
        assertEquals(164, HoughMap.calculateWidth(deltaTheta));
    }



    @Test(expected = IllegalArgumentException.class)
    public void testCalculateWidthException1() {
        // Negative deltaTheta
        Magnitude deltaTheta = new Magnitude(-toRadians(0.5), "rad");
        HoughMap.calculateWidth(deltaTheta);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testCalculateWidthException2() {
        // deltaTheta = 0
        Magnitude deltaTheta = new Magnitude(0.0, "rad");
        HoughMap.calculateWidth(deltaTheta);
    }



    @Test
    public void testDuplicate() {
        HoughMap map = new HoughMap(toRadians(1.1), 1.5, 50);
        HoughMap dup = map.duplicate();

        assertEquals(map.rhoMin.getValue("px"), dup.rhoMin.getValue("px"), 1e-5);
        assertEquals(map.rhoMax.getValue("px"), dup.rhoMax.getValue("px"), 1e-5);
        assertEquals(map.getDeltaRho().getValue("px"),
                dup.getDeltaRho().getValue("px"), 1e-5);
        assertEquals(map.thetaMin.getValue("rad"),
                dup.thetaMin.getValue("rad"), 1e-5);
        assertEquals(map.thetaMax.getValue("rad"),
                dup.thetaMax.getValue("rad"), 1e-5);
        assertTrue(map.getDeltaTheta().equals(dup.getDeltaTheta(), 1e-5));

        assertEquals(map.width, dup.width);
        assertEquals(map.height, dup.height);

        dup.assertEquals(map);
    }



    @Test
    public void testGetIndex() {
        // Even increments
        HoughMap map = new HoughMap(toRadians(0.5), 230 * toRadians(0.5), 400);

        Magnitude theta = new Magnitude(80.5, "deg");
        Magnitude rho = new Magnitude(-92.328, "px");
        assertEquals(88721, map.getIndex(theta, rho));

        theta = new Magnitude(0, "deg");
        rho = new Magnitude(401.425, "px");
        assertEquals(0, map.getIndex(theta, rho));

        theta = new Magnitude(179.5, "deg");
        rho = new Magnitude(-401.425, "px");
        assertEquals(144359, map.getIndex(theta, rho));

        // Odd increments
        map = new HoughMap(toRadians(1.1), 230 * toRadians(1.1), 400);

        theta = new Magnitude(80.3, "deg");
        rho = new Magnitude(335.592 - 2.200, "px");
        assertEquals(2533, map.getIndex(theta, rho));

        theta = new Magnitude(80.3 - 0.54, "deg");
        rho = new Magnitude(335.592, "px");
        assertEquals(2533, map.getIndex(theta, rho));

        theta = new Magnitude(80.3, "deg");
        rho = new Magnitude(335.592, "px");
        assertEquals(2533, map.getIndex(theta, rho));

        theta = new Magnitude(80.3 + 0.54, "deg");
        rho = new Magnitude(335.592, "px");
        assertEquals(2533, map.getIndex(theta, rho));

        theta = new Magnitude(80.3, "deg");
        rho = new Magnitude(335.592 + 2.200, "px");
        assertEquals(2533, map.getIndex(theta, rho));

        theta = new Magnitude(0, "deg");
        rho = new Magnitude(401.827, "px");
        assertEquals(0, map.getIndex(theta, rho));

        theta = new Magnitude(179.29, "deg");
        rho = new Magnitude(-401.827, "px");
        assertEquals(30011, map.getIndex(theta, rho));
    }



    @Test(expected = Exception.class)
    public void testGetIndexException1() {
        // Test theta > max
        HoughMap map = new HoughMap(toRadians(0.5), 230 * toRadians(0.5), 400);
        map.getIndex(new Magnitude(180, "deg"), new Magnitude(25.0, "px"));
    }



    @Test(expected = Exception.class)
    public void testGetIndexException2() {
        // Test theta < min
        HoughMap map = new HoughMap(toRadians(0.5), 230 * toRadians(0.5), 400);
        map.getIndex(new Magnitude(-1, "deg"), new Magnitude(25.0, "px"));
    }



    @Test(expected = Exception.class)
    public void testGetIndexException3() {
        // Test r > max
        HoughMap map = new HoughMap(toRadians(0.5), 230 * toRadians(0.5), 400);
        map.getIndex(new Magnitude(1.0, "deg"), new Magnitude(403, "px"));
    }



    @Test(expected = Exception.class)
    public void testGetIndexException4() {
        // Test negative r < min
        HoughMap map = new HoughMap(toRadians(0.5), 230 * toRadians(0.5), 400);
        map.getIndex(new Magnitude(1.0, "deg"), new Magnitude(-403, "px"));
    }



    @Test
    public void testGetLUT() {
        HoughMap map = new HoughMap(toRadians(0.5), 230 * toRadians(0.5), 400);
        map.getLUT().assertEquals(LUT.createGreyscaleLUT());
    }



    @Test
    public void testGetValidFileFormats() {
        HoughMap map = new HoughMap(toRadians(0.5), 230 * toRadians(0.5), 400);
        String[] validFormats = map.getValidFileFormats();
        assertEquals(1, validFormats.length);
        assertEquals("bmp", validFormats[0]);
    }



    @Test
    public void testGetRho() {
        // Even increments
        HoughMap map = new HoughMap(toRadians(0.5), 230 * toRadians(0.5), 400);
        assertEquals(-92.328, map.getRho(88721).getValue("px"), 1e-3);

        // Odd increments
        map = new HoughMap(toRadians(1.1), 230 * toRadians(1.1), 400);
        assertEquals(331.176, map.getRho(2697).getValue("px"), 1e-3);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testGetRException1() {
        // Test negative index
        HoughMap map = new HoughMap(toRadians(0.5), 230 * toRadians(0.5), 400);
        map.getRho(-1);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testGetRException2() {
        // Test index > max
        HoughMap map = new HoughMap(toRadians(0.5), 230 * toRadians(0.5), 400);
        map.getRho(144360);
    }



    @Test
    public void testGetTheta() {
        // Even increments
        HoughMap map = new HoughMap(toRadians(0.5), 230 * toRadians(0.5), 400);
        assertEquals(toRadians(80.5), map.getTheta(88721).getValue("rad"), 1e-3);

        // Odd increments
        map = new HoughMap(toRadians(1.1), 230 * toRadians(1.1), 400);
        assertEquals(toRadians(80.3), map.getTheta(2697).getValue("rad"), 1e-3);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testGetThetaException1() {
        // Test negative index
        HoughMap map = new HoughMap(toRadians(0.5), 230 * toRadians(0.5), 400);
        map.getTheta(-1);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testGetThetaException2() {
        // Test index > max
        HoughMap map = new HoughMap(toRadians(0.5), 230 * toRadians(0.5), 400);
        map.getTheta(360360);
    }



    @Test
    public void testGetX() {
        HoughMap map = new HoughMap(toRadians(0.5), 230 * toRadians(0.5), 400);
        assertEquals(160, map.getX(new Magnitude(80.24, "deg")));
        assertEquals(161, map.getX(new Magnitude(80.26, "deg")));
        assertEquals(161, map.getX(new Magnitude(80.5, "deg")));
        assertEquals(161, map.getX(new Magnitude(80.74, "deg")));
        assertEquals(162, map.getX(new Magnitude(80.76, "deg")));
        assertEquals(0, map.getX(new Magnitude(0, "deg")));
    }



    @Test(expected = IllegalArgumentException.class)
    public void testGetXException1() {
        // Test theta < min
        HoughMap map = new HoughMap(toRadians(0.5), 230 * toRadians(0.5), 400);
        map.getX(new Magnitude(-1, "rad"));
    }



    @Test(expected = IllegalArgumentException.class)
    public void testGetXException2() {
        // Test theta > max
        HoughMap map = new HoughMap(toRadians(0.5), 230 * toRadians(0.5), 400);
        map.getX(new Magnitude(180, "rad"));
    }



    @Test
    public void testGetY() {
        HoughMap map = new HoughMap(toRadians(0.5), 230 * toRadians(0.5), 400);

        assertEquals(0, map.getY(new Magnitude(401.425, "px")));

        assertEquals(147, map.getY(new Magnitude(107.380, "px")));
        assertEquals(146, map.getY(new Magnitude(107.382, "px")));
        assertEquals(146, map.getY(new Magnitude(108.385, "px")));
        assertEquals(146, map.getY(new Magnitude(109.380, "px")));
        assertEquals(145, map.getY(new Magnitude(109.390, "px")));

        assertEquals(253, map.getY(new Magnitude(-107.380, "px")));
        assertEquals(254, map.getY(new Magnitude(-107.382, "px")));
        assertEquals(254, map.getY(new Magnitude(-108.385, "px")));
        assertEquals(254, map.getY(new Magnitude(-109.380, "px")));
        assertEquals(255, map.getY(new Magnitude(-109.390, "px")));

        assertEquals(400, map.getY(new Magnitude(-401.425, "px")));
    }



    @Test(expected = IllegalArgumentException.class)
    public void testGetYException1() {
        // Test r < min
        HoughMap map = new HoughMap(toRadians(0.5), 230 * toRadians(0.5), 400);
        map.getY(new Magnitude(-405, "px"));
    }



    @Test(expected = IllegalArgumentException.class)
    public void testGetYException2() {
        // Test r > max
        HoughMap map = new HoughMap(toRadians(0.5), 230 * toRadians(0.5), 400);
        map.getY(new Magnitude(405, "px"));
    }



    @Test
    public void testGetRhoStatic() {
        Map map = new ByteMap(360, 500);
        Magnitude deltaTheta = new Magnitude(1.0, "deg");
        Magnitude deltaRho = new Magnitude(1.0, "px");
        Calibration cal =
                HoughMap.calculateCalibration(deltaTheta, deltaRho, 500);
        map.setCalibration(cal);

        assertEquals(250, map.getCalibratedY(179).getValue("px"), 1e-3);
        assertEquals(250, HoughMap.getRho(map, 179).getValue("px"), 1e-3);

        assertEquals(250, map.getCalibratedY(180).getValue("px"), 1e-3);
        assertEquals(-250, HoughMap.getRho(map, 180).getValue("px"), 1e-3);

        assertEquals(250, map.getCalibratedY(181).getValue("px"), 1e-3);
        assertEquals(-250, HoughMap.getRho(map, 181).getValue("px"), 1e-3);

        assertEquals(250, map.getCalibratedY(182).getValue("px"), 1e-3);
        assertEquals(-250, HoughMap.getRho(map, 182).getValue("px"), 1e-3);

        assertEquals(250, map.getCalibratedY(359).getValue("px"), 1e-3);
        assertEquals(-250, HoughMap.getRho(map, 359).getValue("px"), 1e-3);
    }



    @Test
    public void testGetThetaStatic() {
        Map map = new ByteMap(360, 500);
        Magnitude deltaTheta = new Magnitude(1.0, "deg");
        Magnitude deltaRho = new Magnitude(1.0, "px");
        Calibration cal =
                HoughMap.calculateCalibration(deltaTheta, deltaRho, 500);
        map.setCalibration(cal);

        assertEquals(179, map.getCalibratedX(179).getValue("deg"), 1e-3);
        assertEquals(179, HoughMap.getTheta(map, 179).getValue("deg"), 1e-3);

        assertEquals(180, map.getCalibratedX(180).getValue("deg"), 1e-3);
        assertEquals(0, HoughMap.getTheta(map, 180).getValue("deg"), 1e-3);

        assertEquals(181, map.getCalibratedX(181).getValue("deg"), 1e-3);
        assertEquals(1, HoughMap.getTheta(map, 181).getValue("deg"), 1e-3);

        assertEquals(182, map.getCalibratedX(182).getValue("deg"), 1e-3);
        assertEquals(2, HoughMap.getTheta(map, 182).getValue("deg"), 1e-3);

        assertEquals(359, map.getCalibratedX(359).getValue("deg"), 1e-3);
        assertEquals(179, HoughMap.getTheta(map, 359).getValue("deg"), 1e-3);
    }
}
