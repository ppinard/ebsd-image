/*
 * EBSD-Image
 * Copyright (C) 2010-2011 Philippe T. Pinard
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
import rmlimage.core.ByteMap;
import rmlimage.core.Calibration;
import rmlimage.core.Map;
import static java.lang.Math.ceil;

/**
 * Map representing a Hough transform.
 * <p/>
 * The horizontal direction represents the angle (theta). The angle is always in
 * radians. It runs from <code>0</code> at the left (<code>x = 0</code>) to
 * <code>PI</code> (exclusive) at the right (<code>x = width-1</code>).
 * <p/>
 * The vertical direction represents the distance (rho). The distance is always
 * in pixels. It runs from <code>rMin</code> at the bottom (
 * <code>y = height-1</code>) to <code>rMax</code> at the top (
 * <code>y = 0</code>). The map will always have an odd number of lines so the
 * middle line will be the distance axis origin (<code>rho = 0</code> at
 * <code>y = height/2</code>).
 * 
 * @author Marin Lagac&eacute;
 * @author Philippe T. Pinard
 */
public class HoughMap extends ByteMap {

    /** Header key to identify a file as a HoughMap. */
    public static final String FILE_HEADER = "HoughMap2";



    /**
     * Returns the calibration of the Hough map.
     * <ul>
     * <li>dx = deltaTheta</li>
     * <li>dy = deltaR</li>
     * <li>x0 = 0</li>
     * <li>y0 = rMax = deltaR * height / 2</li>
     * </ul>
     * 
     * @param deltaTheta
     *            distance increment (width of a pixel)
     * @param deltaRho
     *            distance increment (height of a pixel)
     * @param unitsRho
     *            units of rho
     * @param height
     *            height of the map
     * @return the calibration
     */
    protected static Calibration calculateCalibration(double deltaTheta,
            double deltaRho, String unitsRho, int height) {
        /*
         * Important that the height division is in integer to have a row of
         * pixel at rho = 0
         */
        double y0 = deltaRho * (height / 2);
        return new Calibration(0, deltaTheta, "rad", false, y0, deltaRho,
                unitsRho, true);
    }



    /**
     * Calculate the height that a <code>HoughMap</code> will have according to
     * the specified distance scale and increment.
     * 
     * @param rhoMax
     *            distance axis (vertical) range
     * @param deltaRho
     *            distance increment (height of a pixel)
     * @return the height
     * @throws IllegalArgumentException
     *             if rMax and deltaR do not have the same units
     * @throws IllegalArgumentException
     *             if rMax or deltaR is less or equal to 0
     * @throws IllegalArgumentException
     *             if deltaR is greater than rMax
     */
    protected static int calculateHeight(double rhoMax, double deltaRho) {
        if (rhoMax <= 0)
            throw new IllegalArgumentException("rMax ( " + rhoMax
                    + ") must be > 0");

        if (deltaRho <= 0)
            throw new IllegalArgumentException("deltaR (" + deltaRho
                    + ") must be > 0");

        if (deltaRho > rhoMax)
            throw new IllegalArgumentException("deltaR (" + deltaRho
                    + ") must be lower than rMax (" + rhoMax + ')');

        return 2 * (int) ceil(rhoMax / deltaRho) + 1;
    }



    /**
     * Calculate the width that a <code>HoughMap</code> will have according to
     * the specified angle increment.
     * 
     * @param deltaTheta
     *            distance increment (width of a pixel)
     * @return the width
     * @throws IllegalArgumentException
     *             if the units of deltaTheta can not be expressed as radians
     * @throws IllegalArgumentException
     *             if deltaTheta is less or equal to zero
     */
    protected static int calculateWidth(double deltaTheta) {
        if (deltaTheta <= 0)
            throw new IllegalArgumentException("deltaTheta (" + deltaTheta
                    + ") must be > 0");

        // Calculate the width
        double widthDouble = Math.PI / deltaTheta + 1;
        double widthInteger = Math.floor(widthDouble);

        // If the width is a round number, decrease it by 1 because
        // the last pixel will be PI which is the wrap around of
        // the first one which is 0
        // Originally coded as
        // return (widthDouble == widthInteger) ? (int)widthInteger - 1
        // : (int)widthInteger;
        // but FindBugs complained about it (FS#218)
        return (widthDouble - widthInteger > 0) ? (int) widthInteger
                : (int) widthInteger - 1;
    }



    /**
     * Returns the <code>rho</code> coordinate of the specified index of the
     * specified <code>Map</code>. The <code>Map</code> must be calibrated as an
     * <code>HoughMap</code> (i.e. dx => deltaTheta, dy => deltaR).
     * <p>
     * Note: The <code>rho</code> coordinate is returned so that it always
     * corresponds to a theta position between [0, PI[. In other words, the
     * <code>rho</code> coordinate is adjusted based on the theta coordinate of
     * the specified <code>index</code>.
     * 
     * @param map
     *            a map calibrated as a <code>HoughMap</code>
     * @param index
     *            index of the pixel to get <code>rho</code> coordinate from
     * @return the <code>rho</code> coordinate in units of map's y coordinate
     * @throws IllegalArgumentException
     *             if the index is outside the map
     * @throws IllegalArgumentException
     *             if the map incorrectly calibrated
     */
    public static double getRho(Map map, int index) {
        if (index < 0 || index >= map.size)
            throw new IllegalArgumentException("index (" + index
                    + ") must be between 0 and " + (map.size - 1));

        Calibration cal = map.getCalibration();

        if (!cal.getDX().areUnits("rad"))
            throw new IllegalArgumentException("Invalid map, delta x units ("
                    + cal.getDX().getBaseUnitsLabel()
                    + ") cannot be expressed as \"rad\".");

        double theta = cal.getCalibratedX(map.getX(index));
        double rho = cal.getCalibratedY(map.getY(index));

        // factor = -1 ^ (theta / PI)
        double factor = Math.pow(-1, (int) (theta / Math.PI));

        return rho * factor;
    }



    /**
     * Returns the <code>theta</code> coordinate of the specified index of the
     * specified <code>Map</code>. The <code>Map</code> must be calibrated as an
     * <code>HoughMap</code> (i.e. dx => deltaTheta, dy => deltaR).
     * <p>
     * Note: The <code>theta</code> coordinate is returned so that it is always
     * between [0, PI[.
     * 
     * @param map
     *            a map calibrated as a <code>HoughMap</code>
     * @param index
     *            index of the pixel to get <code>theta</code> coordinate from
     * @return the <code>theta</code> coordinate in radians
     * @throws IllegalArgumentException
     *             if the index is outside the map
     * @throws IllegalArgumentException
     *             if the map incorrectly calibrated
     */
    public static double getTheta(Map map, int index) {
        if (index < 0 || index >= map.size)
            throw new IllegalArgumentException("index (" + index
                    + ") must be between 0 and " + (map.size - 1));

        Calibration cal = map.getCalibration();

        if (!cal.getDX().areUnits("rad"))
            throw new IllegalArgumentException("Invalid map, delta x units ("
                    + cal.getDX().getBaseUnitsLabel()
                    + ") cannot be expressed as \"rad\".");

        double theta = cal.getCalibratedX(map.getX(index));

        return theta % Math.PI;
    }

    /** Maximum value of the distance axis (y = 0) in rho units. */
    protected double rhoMax;

    /** Minimum value of the distance axis (y = height-1) in rho units. */
    protected double rhoMin;

    /** Maximum value of the angle axis (x = width-1) in radians. */
    protected double thetaMax;

    /** Minimum value of the angle axis (x = 0) in radians. */
    protected double thetaMin;



    /**
     * Creates a <code>HoughMap</code> with the specified distance and angle
     * increments and the specified distance scale.
     * <p/>
     * Since it might not be possible to get an integer number of pixels with
     * the specified distance axis scale (<code>rMax</code>) and distance
     * increment (<code>deltaR</code>), the latter will be enforced. This means
     * that the distance increment used will be the one specified in the
     * constructor but the distance axis scale might not be the one specified.
     * It will be the same or bigger. The distance scale will be symmetric (
     * <code>rMin == -1*rMax</code>).
     * 
     * @param rhoMax
     *            distance axis (vertical) range in pixels
     * @param deltaRho
     *            distance increment in pixel (i.e. height of a pixel)
     * @param deltaTheta
     *            angle increment in radians (i.e. width of a pixel)
     */
    public HoughMap(double deltaTheta, double deltaRho, double rhoMax) {
        super(calculateWidth(deltaTheta), calculateHeight(rhoMax, deltaRho));
        assert (height % 2 != 0) : "height (" + height + ") should be odd";

        setCalibration(calculateCalibration(deltaTheta, deltaRho, "px", height));
    }



    /**
     * Creates a new <code>HoughMap</code> by copying all the information from
     * an existing <code>HoughMap</code>.
     * 
     * @param map
     *            other Hough map
     */
    private HoughMap(HoughMap map) {
        super(map.width, map.height, map.lut);

        cloneMetadataFrom(map);
        System.arraycopy(map.pixArray, 0, pixArray, 0, size);
    }



    /**
     * Creates a new <code>HoughMap</code> with the specified dimensions and
     * resolutions.
     * 
     * @param width
     *            width of the map
     * @param height
     *            height of the map
     * @param deltaRho
     *            distance increment in pixels (i.e. height of a pixel)
     * @param deltaTheta
     *            angle increment in radians (i.e. width of a pixel)
     * @throws IllegalArgumentException
     *             if the height is not odd
     * @throws IllegalArgumentException
     *             if the width calculated using the theta resolution does not
     *             match the specified width
     */
    public HoughMap(int width, int height, double deltaTheta, double deltaRho) {
        super(width, height);
        if (height % 2 == 0)
            throw new IllegalArgumentException("height (" + height
                    + ") must be odd");

        // NOTE: Commented out since the Hough map is still valid without this
        // restriction. The operation ThetaExpand requires to set a different
        // width which extends beyond 180 deg theta max limit.
        // if (calculateWidth(deltaTheta) != width)
        // throw new IllegalArgumentException("width (" + width
        // + ") should be " + calculateWidth(deltaTheta));

        setCalibration(calculateCalibration(deltaTheta, deltaRho, "px", height));
    }



    /**
     * Creates a new <code>HoughMap</code> with the specified dimensions and
     * resolutions.
     * 
     * @param width
     *            width of the map
     * @param height
     *            height of the map
     * @param deltaRho
     *            distance increment (i.e. height of a pixel)
     * @param deltaTheta
     *            angle increment (i.e. width of a pixel)
     * @throws IllegalArgumentException
     *             if the height is not odd
     */
    public HoughMap(int width, int height, Magnitude deltaTheta,
            Magnitude deltaRho) {
        super(width, height);
        if (height % 2 == 0)
            throw new IllegalArgumentException("height (" + height
                    + ") must be odd");

        // NOTE: Commented out since the Hough map is still valid without this
        // restriction. The operation ThetaExpand requires to set a different
        // width which extends beyond 180 deg theta max limit.
        // if (calculateWidth(deltaTheta) != width)
        // throw new IllegalArgumentException("width (" + width
        // + ") should be " + calculateWidth(deltaTheta));

        setCalibration(calculateCalibration(deltaTheta.getValue("rad"),
                deltaRho.getPreferredUnitsValue(),
                deltaRho.getPreferredUnitsLabel(), height));
    }



    /**
     * Creates a <code>HoughMap</code> with the specified distance and angle
     * increments and the specified distance scale.
     * <p/>
     * Since it might not be possible to get an integer number of pixels with
     * the specified distance axis scale (<code>rMax</code>) and distance
     * increment (<code>deltaR</code>), the latter will be enforced. This means
     * that the distance increment used will be the one specified in the
     * constructor but the distance axis scale might not be the one specified.
     * It will be the same or bigger. The distance scale will be symmetric (
     * <code>rMin == -1*rMax</code>).
     * 
     * @param rMax
     *            distance axis (vertical) range
     * @param deltaRho
     *            distance increment (i.e. height of a pixel)
     * @param deltaTheta
     *            angle increment (i.e. width of a pixel)
     */
    public HoughMap(Magnitude deltaTheta, Magnitude deltaRho, Magnitude rMax) {
        super(calculateWidth(deltaTheta.getValue("rad")), calculateHeight(
                rMax.getPreferredUnitsValue(),
                deltaRho.getPreferredUnitsValue()));
        assert (height % 2 != 0) : "height (" + height + ") should be odd";

        if (!deltaRho.areSameUnits(rMax))
            throw new IllegalArgumentException("Delta rho ("
                    + deltaRho.getBaseUnitsLabel() + ") and rMax ("
                    + rMax.getBaseUnitsLabel() + ") must have the same units.");

        setCalibration(calculateCalibration(deltaTheta.getValue("rad"),
                deltaRho.getPreferredUnitsValue(),
                deltaRho.getPreferredUnitsLabel(), height));
    }



    @Override
    public HoughMap createMap(int width, int height) {
        return new HoughMap(width, height, getDeltaTheta(), getDeltaRho());
    }



    /**
     * Creates a copy of the <code>HoughMap</code>.
     * 
     * @return a copy of the <code>HoughMap</code>
     */
    @Override
    public HoughMap duplicate() {
        return new HoughMap(this);
    }



    /**
     * Returns the distance increment. Wrap around {@link Calibration#getDY()}.
     * 
     * @return height of the pixels in this <code>HoughMap</code>
     */
    public Magnitude getDeltaRho() {
        return getCalibration().getDY();
    }



    /**
     * Returns the angle increment in radians. Wrap around
     * {@link Calibration#getDX()}.
     * 
     * @return width of the pixels in this <code>HoughMap</code>
     */
    public Magnitude getDeltaTheta() {
        return getCalibration().getDX();
    }



    /**
     * Returns a <code>HoughPeak</code> for the specified index.
     * 
     * @param index
     *            index of a pixel in the map
     * @return a <code>HoughPeak</code>
     * @throws IllegalArgumentException
     *             if the index is outside the map
     */
    public HoughPeak getHoughPeak(int index) {
        if (index < 0 || index >= size)
            throw new IllegalArgumentException("index (" + index
                    + ") must be between 0 and " + (size - 1));

        Calibration cal = getCalibration();

        double theta = cal.getCalibratedX(getX(index));
        double rho = cal.getCalibratedY(getY(index));
        String rhoUnits = cal.unitsY;
        double intensity = pixArray[index] & 0xff;

        return new HoughPeak(theta, rho, rhoUnits, intensity);
    }



    /**
     * Return the index in the pixArray of the specified point.
     * 
     * @param rho
     *            rho coordinate in units of delta rho
     * @param theta
     *            theta coordinate in radians
     * @return the index in the pixArray
     * @throws IllegalArgumentException
     *             if either <code>rho</code> or <code>theta</code> are outside
     *             the <code>HougMap</code>
     */
    public int getIndex(double theta, double rho) {
        return getIndex(getX(theta), getY(rho));
    }



    /**
     * Return the index in the pixArray of the specified point.
     * 
     * @param rho
     *            rho coordinate
     * @param theta
     *            theta coordinate
     * @return the index in the pixArray
     * @throws IllegalArgumentException
     *             if either <code>rho</code> or <code>theta</code> are outside
     *             the <code>HougMap</code>
     */
    public int getIndex(Magnitude theta, Magnitude rho) {
        return getIndex(getX(theta), getY(rho));
    }



    @Override
    public String getPixelInfoLabel(int index) {
        StringBuilder str = new StringBuilder();
        Calibration cal = getCalibration();

        int x = getX(index);
        str.append("  \u03b8=");
        rmlshared.math.Double.format(Math.toDegrees(cal.getCalibratedX(x)), 1,
                str);
        str.append("\u00b0");

        int y = getY(index);
        str.append("  \u03c1=");
        rmlshared.math.Double.format(cal.getCalibratedY(y), 4, str);
        str.append(cal.unitsY);

        int value = pixArray[index] & 0xff;
        str.append("  value=");
        str.append(value);

        return str.toString();
    }



    /**
     * Returns the <code>rho</code> value of the specified index. Wrap around
     * {@link #getCalibratedY(int)}.
     * 
     * @param index
     *            index
     * @return the <code>rho</code> value of the specified index
     * @see #getCalibratedY(int)
     */
    public Magnitude getRho(int index) {
        return getCalibratedY(index);
    }



    /**
     * Returns the maximum coordinate of rho in rho units.
     * 
     * @return maximum coordinate of rho in rho units
     */
    public double getRhoMax() {
        return rhoMax;
    }



    /**
     * Returns the minimum coordinate of rho in rho units.
     * 
     * @return minimum coordinate of rho in rho units
     */
    public double getRhoMin() {
        return rhoMin;
    }



    /**
     * Returns the <code>theta</code> value of the specified index. Wrap around
     * {@link #getCalibratedX(int)}.
     * 
     * @param index
     *            index
     * @return the <code>theta</code> value in radians
     * @throws IllegalArgumentException
     *             if <code>index</code> is outside the range of the
     *             <code>pixArray</code>
     * @see #getCalibratedX(int)
     */
    public Magnitude getTheta(int index) {
        return getCalibratedX(index);
    }



    /**
     * Returns the maximum coordinate of theta in radians.
     * 
     * @return maximum coordinate of theta in radians
     */
    public double getThetaMax() {
        return thetaMax;
    }



    /**
     * Returns the minimum coordinate of theta in radians.
     * 
     * @return minimum coordinate of theta in radians
     */
    public double getThetaMin() {
        return thetaMin;
    }



    /**
     * {@inheritDoc} The only valid file format is <code>BMP</code>.
     */
    @Override
    public String[] getValidFileFormats() {
        return new String[] { "bmp" };
    }



    /**
     * Converts the specified theta coordinate into the corresponding x
     * coordinate.
     * 
     * @param theta
     *            theta coordinate to convert in radians
     * @return the corresponding x coordinate
     * @throws IllegalArgumentException
     *             if <code>theta</code> is not between {@link #thetaMin} and
     *             {@link #thetaMax}
     */
    public int getX(double theta) {
        double deltaTheta = getCalibration().dx;

        if (theta < thetaMin - deltaTheta / 2.0
                || theta > thetaMax + deltaTheta / 2.0)
            throw new IllegalArgumentException("theta (" + theta
                    + ") must be between " + thetaMin + " and " + thetaMax);

        // (theta - thetaMin) / deltaTheta
        // the result is unitless all magnitude have the same units
        return (int) ((theta - thetaMin) / deltaTheta + 0.5);
    }



    /**
     * Converts the specified theta coordinate into the corresponding x
     * coordinate.
     * 
     * @param theta
     *            theta coordinate to convert in radians
     * @return the corresponding x coordinate
     * @throws IllegalArgumentException
     *             if <code>theta</code> is not between {@link #thetaMin} and
     *             {@link #thetaMax}
     * @throws IllegalArgumentException
     *             if the units of theta are not radians
     */
    public int getX(Magnitude theta) {
        if (!theta.areUnits("rad"))
            throw new IllegalArgumentException("Theta units must be radians.");

        return getX(theta.getValue("rad"));
    }



    /**
     * Converts the specified <code>rho</code> coordinate into the corresponding
     * y coordinate.
     * 
     * @param rho
     *            <code>rho</code> coordinate to convert in units of delta rho
     * @return the corresponding y coordinate
     * @throws IllegalArgumentException
     *             if <code>rho</code> is not between {@link #rhoMin} and
     *             {@link #rhoMax}
     */
    public int getY(double rho) {
        double deltaRho = getCalibration().dy;

        if (rho < (rhoMin - deltaRho / 2.0) || rho > (rhoMax + deltaRho / 2.0))
            throw new IllegalArgumentException("rho (" + rho
                    + ") must be between " + rhoMin + " and " + rhoMax);

        // If r < 0, need to round toward the bottom (thus the - 0.5)
        if (rho >= 0)
            return height / 2 - (int) (rho / deltaRho + 0.5);
        else
            return height / 2 - (int) (rho / deltaRho - 0.5);
    }



    /**
     * Converts the specified <code>rho</code> coordinate into the corresponding
     * y coordinate.
     * 
     * @param rho
     *            <code>rho</code> coordinate to convert
     * @return the corresponding y coordinate
     * @throws IllegalArgumentException
     *             if <code>rho</code> is not between {@link #rhoMin} and
     *             {@link #rhoMax}
     * @throws IllegalArgumentException
     *             if r do not have the same units as deltaR
     */
    public int getY(Magnitude rho) {
        if (!rho.areSameUnits(getDeltaRho()))
            throw new IllegalArgumentException("r (" + rho.getBaseUnitsLabel()
                    + ") must have the same units as deltaR ("
                    + getDeltaRho().getBaseUnitsLabel() + ").");

        return getY(rho.getBaseUnitsValue());
    }



    /**
     * Sets the calibration and re-calculates thetaMin, thetaMax, rMin and rMax.
     * The deltaX of the calibration is taken as the deltaTheta and the deltaY
     * as the deltaR.
     * <p>
     * {@inheritDoc}
     * 
     * @throws IllegalArgumentException
     *             if the units of the deltaTheta can not be expressed as
     *             radians
     */
    @Override
    public void setCalibration(Calibration calibration) {
        Magnitude deltaRho = calibration.getDY();
        Magnitude deltaTheta = calibration.getDX();

        // Check delta theta units
        if (!deltaTheta.areUnits("rad"))
            throw new IllegalArgumentException("Delta theta units ("
                    + deltaTheta.getBaseUnitsLabel()
                    + ") cannot be expressed as \"rad\".");

        // Set new calibration
        super.setCalibration(calibration);

        // Re-calculate rMin, rMax, thetaMin and thetaMax
        rhoMax = deltaRho.getBaseUnitsValue() * ((height - 1) / 2);
        rhoMin = -rhoMax;

        thetaMin = 0.0;
        thetaMax = deltaTheta.getValue("rad") * (width - 1);
    }

}
