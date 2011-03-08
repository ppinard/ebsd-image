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
import org.junit.Before;
import org.junit.Test;

import rmlimage.core.ByteMap;
import rmlimage.core.Calibration;
import rmlimage.core.Map;

import static org.junit.Assert.assertEquals;

import static java.lang.Math.toRadians;

import static junittools.test.Assert.assertEquals;

public class HoughMapTest extends TestCase {

    // Even increments
    private HoughMap mapEven;

    // Odd increments
    private HoughMap mapOdd;



    @Before
    public void setUp() {
        mapEven = new HoughMap(toRadians(0.5), 230 * toRadians(0.5), 400);
        mapOdd = new HoughMap(toRadians(1.1), 230 * toRadians(1.1), 400);
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



    @Test(expected = Exception.class)
    public void testCalculateHeightException6() {
        // deltaRho > rhoMax
        HoughMap.calculateHeight(0.1, 100);
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
        HoughMap.calculateWidth(0.0);
    }



    @Test
    public void testDuplicate() {
        HoughMap dup = mapEven.duplicate();

        assertEquals(mapEven.getRhoMin(), dup.getRhoMin(), 1e-5);
        assertEquals(mapEven.getRhoMax(), dup.getRhoMax(), 1e-5);
        assertEquals(mapEven.getDeltaRho(), dup.getDeltaRho(), 1e-5);
        assertEquals(mapEven.getThetaMin(), dup.getThetaMin(), 1e-5);
        assertEquals(mapEven.getThetaMax(), dup.getThetaMax(), 1e-5);
        assertEquals(mapEven.getDeltaTheta(), dup.getDeltaTheta(), 1e-5);

        assertEquals(mapEven.width, dup.width);
        assertEquals(mapEven.height, dup.height);

        dup.assertEquals(mapEven);
    }



    @Test
    public void testGetIndexDoubleDouble() {
        // Even increments
        assertEquals(88721, mapEven.getIndex(toRadians(80.5), -92.328));
        assertEquals(0, mapEven.getIndex(0, 401.425));
        assertEquals(144359, mapEven.getIndex(toRadians(179.5), -401.425));

        // Odd increments
        assertEquals(2533, mapOdd.getIndex(toRadians(80.3), 335.592 - 2.200));
        assertEquals(2533, mapOdd.getIndex(toRadians(80.3 - 0.54), 335.592));
        assertEquals(2533, mapOdd.getIndex(toRadians(80.3), 335.592));
        assertEquals(2533, mapOdd.getIndex(toRadians(80.3 + 0.54), 335.592));
        assertEquals(2533, mapOdd.getIndex(toRadians(80.3), 335.592 + 2.200));
        assertEquals(0, mapOdd.getIndex(0.0, 401.827));
        assertEquals(30011, mapOdd.getIndex(toRadians(179.29), -401.827));
    }



    @Test
    public void testGetIndexMagnitudeMagnitude() {
        // Even increments
        Magnitude theta = new Magnitude(80.5, "deg");
        Magnitude rho = new Magnitude(-92.328, "px");
        assertEquals(88721, mapEven.getIndex(theta, rho));

        theta = new Magnitude(0, "deg");
        rho = new Magnitude(401.425, "px");
        assertEquals(0, mapEven.getIndex(theta, rho));

        theta = new Magnitude(179.5, "deg");
        rho = new Magnitude(-401.425, "px");
        assertEquals(144359, mapEven.getIndex(theta, rho));

        // Odd increments
        theta = new Magnitude(80.3, "deg");
        rho = new Magnitude(335.592 - 2.200, "px");
        assertEquals(2533, mapOdd.getIndex(theta, rho));

        theta = new Magnitude(80.3 - 0.54, "deg");
        rho = new Magnitude(335.592, "px");
        assertEquals(2533, mapOdd.getIndex(theta, rho));

        theta = new Magnitude(80.3, "deg");
        rho = new Magnitude(335.592, "px");
        assertEquals(2533, mapOdd.getIndex(theta, rho));

        theta = new Magnitude(80.3 + 0.54, "deg");
        rho = new Magnitude(335.592, "px");
        assertEquals(2533, mapOdd.getIndex(theta, rho));

        theta = new Magnitude(80.3, "deg");
        rho = new Magnitude(335.592 + 2.200, "px");
        assertEquals(2533, mapOdd.getIndex(theta, rho));

        theta = new Magnitude(0, "deg");
        rho = new Magnitude(401.827, "px");
        assertEquals(0, mapOdd.getIndex(theta, rho));

        theta = new Magnitude(179.29, "deg");
        rho = new Magnitude(-401.827, "px");
        assertEquals(30011, mapOdd.getIndex(theta, rho));
    }



    @Test(expected = IllegalArgumentException.class)
    public void testGetRException1() {
        // Test negative index
        mapEven.getRho(-1);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testGetRException2() {
        // Test index > max
        mapEven.getRho(144360);
    }



    @Test
    public void testGetPixelInfoLabel() {
        String expected = "  \u03b8=0.0\u00b0  \u03c1=401.4257px  value=0";
        assertEquals(expected, mapEven.getPixelInfoLabel(0));
    }



    @Test
    public void testGetRho() {
        // Even increments
        assertEquals(-92.328, mapEven.getRho(88721).getValue("px"), 1e-3);

        // Odd increments
        assertEquals(331.176, mapOdd.getRho(2697).getValue("px"), 1e-3);
    }



    @Test
    public void testGetRhoStatic() {
        Map map = new ByteMap(360, 500);
        Calibration cal =
                HoughMap.calculateCalibration(toRadians(1.0), 1.0, "cm", 500);
        map.setCalibration(cal);

        assertEquals(250, map.getCalibratedY(179).getValue("cm"), 1e-3);
        assertEquals(250, HoughMap.getRho(map, 179), 1e-3);

        assertEquals(250, map.getCalibratedY(180).getValue("cm"), 1e-3);
        // commented out since it is affected by rounding errors
        // assertEquals(-250, HoughMap.getRho(map, 180), 1e-3);

        // flip rho because theta > 180
        assertEquals(250, map.getCalibratedY(181).getValue("cm"), 1e-3);
        assertEquals(-250, HoughMap.getRho(map, 181), 1e-3);

        assertEquals(250, map.getCalibratedY(182).getValue("cm"), 1e-3);
        assertEquals(-250, HoughMap.getRho(map, 182), 1e-3);

        assertEquals(250, map.getCalibratedY(359).getValue("cm"), 1e-3);
        assertEquals(-250, HoughMap.getRho(map, 359), 1e-3);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testGetRhoStaticException1() {
        // index < 0
        HoughMap.getRho(new ByteMap(360, 500), -1);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testGetRhoStaticException2() {
        // index > size
        HoughMap.getRho(new ByteMap(360, 500), 360 * 500);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testGetRhoStaticException3() {
        // calibration dx units != rad
        Map map = new ByteMap(360, 500);
        Calibration cal =
                new Calibration(0, 0.5, "px", false, 200, 1.0, "px", true);
        map.setCalibration(cal);

        HoughMap.getRho(map, 1);
    }



    @Test
    public void testGetTheta() {
        // Even increments
        assertEquals(toRadians(80.5), mapEven.getTheta(88721).getValue("rad"),
                1e-3);

        // Odd increments
        assertEquals(toRadians(80.3), mapOdd.getTheta(2697).getValue("rad"),
                1e-3);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testGetThetaException1() {
        // Test negative index
        mapEven.getTheta(-1);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testGetThetaException2() {
        // Test index > max
        mapEven.getTheta(360360);
    }



    @Test
    public void testGetThetaStatic() {
        Map map = new ByteMap(360, 500);
        Calibration cal =
                HoughMap.calculateCalibration(toRadians(1.0), 1.0, "cm", 500);
        map.setCalibration(cal);

        assertEquals(179, map.getCalibratedX(179).getValue("deg"), 1e-3);
        assertEquals(179, Math.toDegrees(HoughMap.getTheta(map, 179)), 1e-3);

        assertEquals(180, map.getCalibratedX(180).getValue("deg"), 1e-3);
        // commented out since it is affected by rounding errors
        // assertEquals(0, Math.toDegrees(HoughMap.getTheta(map, 180)), 1e-3);

        // theta always between [0, PI[
        assertEquals(181, map.getCalibratedX(181).getValue("deg"), 1e-3);
        assertEquals(1, Math.toDegrees(HoughMap.getTheta(map, 181)), 1e-3);

        assertEquals(182, map.getCalibratedX(182).getValue("deg"), 1e-3);
        assertEquals(2, Math.toDegrees(HoughMap.getTheta(map, 182)), 1e-3);

        assertEquals(359, map.getCalibratedX(359).getValue("deg"), 1e-3);
        assertEquals(179, Math.toDegrees(HoughMap.getTheta(map, 359)), 1e-3);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testGetThetaStaticException1() {
        // index < 0
        HoughMap.getTheta(new ByteMap(360, 500), -1);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testGetThetaStaticException2() {
        // index > size
        HoughMap.getTheta(new ByteMap(360, 500), 360 * 500);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testGetThetaStaticException3() {
        // calibration dx units != rad
        Map map = new ByteMap(360, 500);
        Calibration cal =
                new Calibration(0, 0.5, "px", false, 200, 1.0, "px", true);
        map.setCalibration(cal);

        HoughMap.getTheta(map, 1);
    }



    @Test
    public void testGetValidFileFormats() {
        String[] validFormats = mapEven.getValidFileFormats();
        assertEquals(1, validFormats.length);
        assertEquals("bmp", validFormats[0]);
    }



    @Test
    public void testGetXMagnitude() {
        assertEquals(160, mapEven.getX(new Magnitude(80.24, "deg")));
        assertEquals(161, mapEven.getX(new Magnitude(80.26, "deg")));
        assertEquals(161, mapEven.getX(new Magnitude(80.5, "deg")));
        assertEquals(161, mapEven.getX(new Magnitude(80.74, "deg")));
        assertEquals(162, mapEven.getX(new Magnitude(80.76, "deg")));
        assertEquals(0, mapEven.getX(new Magnitude(0, "deg")));
    }



    @Test
    public void testGetXDouble() {
        assertEquals(160, mapEven.getX(toRadians(80.24)));
        assertEquals(161, mapEven.getX(toRadians(80.26)));
        assertEquals(161, mapEven.getX(toRadians(80.5)));
        assertEquals(161, mapEven.getX(toRadians(80.74)));
        assertEquals(162, mapEven.getX(toRadians(80.76)));
        assertEquals(0, mapEven.getX(0.0));
    }



    @Test(expected = IllegalArgumentException.class)
    public void testGetXException1() {
        // Test theta < thetaMin
        mapEven.getX(-1.0);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testGetXException2() {
        // Test theta > thetaMax
        mapEven.getX(180.0);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testGetXMagnitudeException() {
        // Incorrect units
        mapEven.getX(new Magnitude(1.0, "m"));
    }



    @Test
    public void testGetYDouble() {
        assertEquals(0, mapEven.getY(401.425));

        assertEquals(147, mapEven.getY(107.380));
        assertEquals(146, mapEven.getY(107.382));
        assertEquals(146, mapEven.getY(108.385));
        assertEquals(146, mapEven.getY(109.380));
        assertEquals(145, mapEven.getY(109.390));

        assertEquals(253, mapEven.getY(-107.380));
        assertEquals(254, mapEven.getY(-107.382));
        assertEquals(254, mapEven.getY(-108.385));
        assertEquals(254, mapEven.getY(-109.380));
        assertEquals(255, mapEven.getY(-109.390));

        assertEquals(400, mapEven.getY(-401.425));
    }



    @Test(expected = IllegalArgumentException.class)
    public void testGetYDoubleException1() {
        // Test r < rhoMin
        mapEven.getY(-405.0);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testGetYDoubleException2() {
        // Test r > rhoMax
        mapEven.getY(405.0);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testGetYMagnitudeException() {
        // Incorrect units
        mapEven.getY(new Magnitude(401.25, "cm"));
    }



    @Test
    public void testHoughMapDoubleDoubleDouble() {
        // Even increments
        assertEquals(360, mapEven.width);
        assertEquals(401, mapEven.height);
        assertEquals(-401.426, mapEven.getRhoMin(), 1e-3);
        assertEquals(401.426, mapEven.getRhoMax(), 1e-3);
        assertEquals(2.007, mapEven.getDeltaRho().getValue("px"), 1e-3);
        assertEquals(0.0, mapEven.getThetaMin(), 1e-3);
        assertEquals(toRadians(179.5), mapEven.getThetaMax(), 1e-3);
        assertEquals(toRadians(0.5), mapEven.getDeltaTheta().getValue("rad"),
                1e-3);

        // Odd increments
        assertEquals(164, mapOdd.width);
        assertEquals(183, mapOdd.height);
        assertEquals(-401.827, mapOdd.getRhoMin(), 1e-3);
        assertEquals(401.827, mapOdd.getRhoMax(), 1e-3);
        assertEquals(4.416, mapOdd.getDeltaRho().getValue("px"), 1e-3);
        assertEquals(0.0, mapOdd.getThetaMin(), 1e-3);
        assertEquals(toRadians(179.3), mapOdd.getThetaMax(), 1e-3);
        assertEquals(toRadians(1.1), mapOdd.getDeltaTheta().getValue("rad"),
                1e-3);
    }



    @Test
    public void testHoughMapIntIntDoubleDouble() {
        // Even increments
        HoughMap mapEven = new HoughMap(360, 401, toRadians(0.5), 2.00712864);
        assertEquals(360, mapEven.width);
        assertEquals(401, mapEven.height);
        assertEquals(-401.426, mapEven.getRhoMin(), 1e-3);
        assertEquals(401.426, mapEven.getRhoMax(), 1e-3);
        assertEquals(2.007, mapEven.getDeltaRho().getValue("px"), 1e-3);
        assertEquals(0.0, mapEven.getThetaMin(), 1e-3);
        assertEquals(toRadians(179.5), mapEven.getThetaMax(), 1e-3);
        assertEquals(toRadians(0.5), mapEven.getDeltaTheta().getValue("rad"),
                1e-3);

        // Odd increments
        HoughMap mapOdd = new HoughMap(164, 185, toRadians(1.1), 4.415683008);
        assertEquals(164, mapOdd.width);
        assertEquals(185, mapOdd.height);
        assertEquals(-406.243, mapOdd.getRhoMin(), 1e-3);
        assertEquals(406.243, mapOdd.getRhoMax(), 1e-3);
        assertEquals(4.416, mapOdd.getDeltaRho().getValue("px"), 1e-3);
        assertEquals(0.0, mapOdd.getThetaMin(), 1e-3);
        assertEquals(toRadians(179.3), mapOdd.getThetaMax(), 1e-3);
        assertEquals(toRadians(1.1), mapOdd.getDeltaTheta().getValue("rad"),
                1e-3);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testHoughMapIntIntDoubleDoubleException() {
        // even height
        new HoughMap(164, 184, toRadians(1.1), 4.415683008);
    }



    @Test
    public void testHoughMapIntIntMangitudeMagnitude() {
        // Even increments
        HoughMap mapEven =
                new HoughMap(360, 401, new Magnitude(0.5, "deg"),
                        new Magnitude(2.00712864, "m"));
        assertEquals(360, mapEven.width);
        assertEquals(401, mapEven.height);
        assertEquals(-401.426, mapEven.getRhoMin(), 1e-3);
        assertEquals(401.426, mapEven.getRhoMax(), 1e-3);
        assertEquals(2.007, mapEven.getDeltaRho().getValue("m"), 1e-3);
        assertEquals(0.0, mapEven.getThetaMin(), 1e-3);
        assertEquals(toRadians(179.5), mapEven.getThetaMax(), 1e-3);
        assertEquals(toRadians(0.5), mapEven.getDeltaTheta().getValue("rad"),
                1e-3);

        // Odd increments
        HoughMap mapOdd =
                new HoughMap(164, 185, new Magnitude(1.1, "deg"),
                        new Magnitude(4.415683008, "s"));
        assertEquals(164, mapOdd.width);
        assertEquals(185, mapOdd.height);
        assertEquals(-406.243, mapOdd.getRhoMin(), 1e-3);
        assertEquals(406.243, mapOdd.getRhoMax(), 1e-3);
        assertEquals(4.416, mapOdd.getDeltaRho().getValue("s"), 1e-3);
        assertEquals(0.0, mapOdd.getThetaMin(), 1e-3);
        assertEquals(toRadians(179.3), mapOdd.getThetaMax(), 1e-3);
        assertEquals(toRadians(1.1), mapOdd.getDeltaTheta().getValue("rad"),
                1e-3);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testHoughMapIntIntMangitudeMagnitudeException() {
        // even height
        new HoughMap(164, 184, new Magnitude(1.1, "deg"), new Magnitude(
                4.415683008, "m"));
    }



    @Test
    public void testHoughMapMagnitudeMagnitudeMagnitude() {
        // Even increments
        HoughMap mapEven =
                new HoughMap(new Magnitude(0.5, "deg"), new Magnitude(
                        230 * toRadians(0.5), "m"), new Magnitude(400, "m"));
        assertEquals(360, mapEven.width);
        assertEquals(401, mapEven.height);
        assertEquals(-401.426, mapEven.getRhoMin(), 1e-3);
        assertEquals(401.426, mapEven.getRhoMax(), 1e-3);
        assertEquals(2.007, mapEven.getDeltaRho().getValue("m"), 1e-3);
        assertEquals(0.0, mapEven.getThetaMin(), 1e-3);
        assertEquals(toRadians(179.5), mapEven.getThetaMax(), 1e-3);
        assertEquals(toRadians(0.5), mapEven.getDeltaTheta().getValue("rad"),
                1e-3);

        // Odd increments
        HoughMap mapOdd =
                new HoughMap(new Magnitude(1.1, "deg"), new Magnitude(
                        230 * toRadians(1.1), "s"), new Magnitude(400, "s"));
        assertEquals(164, mapOdd.width);
        assertEquals(183, mapOdd.height);
        assertEquals(-401.827, mapOdd.getRhoMin(), 1e-3);
        assertEquals(401.827, mapOdd.getRhoMax(), 1e-3);
        assertEquals(4.416, mapOdd.getDeltaRho().getValue("s"), 1e-3);
        assertEquals(0.0, mapOdd.getThetaMin(), 1e-3);
        assertEquals(toRadians(179.3), mapOdd.getThetaMax(), 1e-3);
        assertEquals(toRadians(1.1), mapOdd.getDeltaTheta().getValue("rad"),
                1e-3);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testHoughMapMagnitudeMagnitudeMagnitudeException1() {
        // Incorrect units for deltaTheta
        new HoughMap(new Magnitude(0.5, "m"), new Magnitude(
                230 * toRadians(0.5), "m"), new Magnitude(400, "m"));
    }



    @Test(expected = IllegalArgumentException.class)
    public void testHoughMapMagnitudeMagnitudeMagnitudeException2() {
        // Units of deltaRho != units of rMax
        new HoughMap(new Magnitude(0.5, "deg"), new Magnitude(
                230 * toRadians(0.5), "s"), new Magnitude(400, "m"));
    }



    @Test
    public void testSetCalibration() {
        Calibration cal =
                HoughMap.calculateCalibration(toRadians(1.0), 1.0, "cm", 400);
        mapEven.setCalibration(cal);

        assertEquals(-2.0, mapEven.getRhoMin(), 1e-3); // m
        assertEquals(2.0, mapEven.getRhoMax(), 1e-3); // m
        assertEquals(1.0, mapEven.getDeltaRho().getValue("cm"), 1e-3);
        assertEquals(0.0, mapEven.getThetaMin(), 1e-3);
        assertEquals(toRadians(359), mapEven.getThetaMax(), 1e-3);
        assertEquals(1.0, mapEven.getDeltaTheta().getValue("deg"), 1e-3);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testSetCalibrationException() {
        // Incorrect deltaTheta units
        Calibration cal =
                new Calibration(0, 0.5, "px", false, 200, 1.0, "px", true);
        mapEven.setCalibration(cal);
    }
}
