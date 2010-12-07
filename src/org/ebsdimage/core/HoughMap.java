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
 * @author ppinard
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
     * @param height
     *            height of the map
     * @return the calibration
     */
    protected static Calibration calculateCalibration(Magnitude deltaTheta,
            Magnitude deltaRho, int height) {
        Magnitude x0 = deltaTheta.multiply(0);
        Magnitude y0 = deltaRho.multiply(height / 2);
        return new Calibration(x0, deltaTheta, false, y0, deltaRho, true);
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
    protected static int calculateHeight(Magnitude rhoMax, Magnitude deltaRho) {
        if (!rhoMax.areSameUnits(deltaRho))
            throw new IllegalArgumentException("rMax ("
                    + rhoMax.getBaseUnitsLabel()
                    + ") must have the same units as deltaR ("
                    + deltaRho.getBaseUnitsLabel() + ").");

        if (rhoMax.getBaseUnitsValue() <= 0)
            throw new IllegalArgumentException("rMax ( " + rhoMax
                    + ") must be > 0");

        if (deltaRho.getBaseUnitsValue() <= 0)
            throw new IllegalArgumentException("deltaR (" + deltaRho
                    + ") must be > 0");

        if (deltaRho.compareTo(rhoMax) > 0)
            throw new IllegalArgumentException("deltaR (" + deltaRho
                    + ") must be lower than rMax (" + rhoMax + ')');

        // Division of rMax by deltaR is unitless because they have the same
        // units
        return 2 * (int) ceil(rhoMax.div(deltaRho).getValue("")) + 1;
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
    protected static int calculateWidth(Magnitude deltaTheta) {
        if (!deltaTheta.areUnits("rad"))
            throw new IllegalArgumentException("Delta theta units ("
                    + deltaTheta.getBaseUnitsLabel()
                    + ") cannot be expressed as \"rad\".");

        if (deltaTheta.getBaseUnitsValue() <= 0)
            throw new IllegalArgumentException("deltaTheta (" + deltaTheta
                    + ") must be > 0");

        // Calculate the width
        double widthDouble = Math.PI / deltaTheta.getValue("rad") + 1;
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
     * @return the <code>rho</code> coordinate
     * @throws IllegalArgumentException
     *             if the index is outside the map
     * @throws IllegalArgumentException
     *             if the map incorrectly calibrated
     */
    public static Magnitude getRho(Map map, int index) {
        if (index < 0 || index >= map.size)
            throw new IllegalArgumentException("index (" + index
                    + ") must be between 0 and " + (map.size - 1));

        if (!map.getCalibration().getDX().areUnits("rad"))
            throw new IllegalArgumentException("Invalid map, delta x units ("
                    + map.getCalibration().getDX().getBaseUnitsLabel()
                    + ") cannot be expressed as \"rad\".");

        Magnitude theta = map.getCalibratedX(index);
        Magnitude rho = map.getCalibratedY(index);

        // factor = -1 ^ (theta / PI)
        Magnitude piMag = magnitude.core.Math.PI;
        double factor = Math.pow(-1, (int) (theta.div(piMag).getValue("")));

        return rho.multiply(factor);
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
     * @return the <code>theta</code> coordinate
     * @throws IllegalArgumentException
     *             if the index is outside the map
     * @throws IllegalArgumentException
     *             if the map incorrectly calibrated
     */
    public static Magnitude getTheta(Map map, int index) {
        if (index < 0 || index >= map.size)
            throw new IllegalArgumentException("index (" + index
                    + ") must be between 0 and " + (map.size - 1));

        if (!map.getCalibration().getDX().areUnits("rad"))
            throw new IllegalArgumentException("Invalid map, delta x units ("
                    + map.getCalibration().getDX().getBaseUnitsLabel()
                    + ") cannot be expressed as \"rad\".");

        Magnitude theta = map.getCalibratedX(index);

        return theta.modulo(magnitude.core.Math.PI);
    }

    /** Maximum value of the distance axis (y = 0). */
    protected Magnitude rhoMax;

    /** Minimum value of the distance axis (y = height-1). */
    protected Magnitude rhoMin;

    /** Maximum value of the angle axis (x = width-1). */
    protected Magnitude thetaMax;

    /** Minimum value of the angle axis (x = 0). */
    protected Magnitude thetaMin;



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
     *            distance axis (vertical) range in pixel
     * @param deltaRho
     *            distance increment in pixel (i.e. height of a pixel)
     * @param deltaTheta
     *            angle increment in radians (i.e. width of a pixel)
     */
    public HoughMap(double deltaTheta, double deltaRho, double rhoMax) {
        this(new Magnitude(deltaTheta, "rad"), new Magnitude(deltaRho, "px"),
                new Magnitude(rhoMax, "px"));
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
     *            distance increment in pixel (i.e. height of a pixel)
     * @param deltaTheta
     *            angle increment in radians (i.e. width of a pixel)
     * @throws IllegalArgumentException
     *             if the height is not odd
     * @throws IllegalArgumentException
     *             if the width calculated using the theta resolution does not
     *             match the specified width
     */
    public HoughMap(int width, int height, double deltaTheta, double deltaRho) {
        this(width, height, new Magnitude(deltaTheta, "rad"), new Magnitude(
                deltaRho, "px"));
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

        setCalibration(calculateCalibration(deltaTheta, deltaRho, height));
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
        super(calculateWidth(deltaTheta), calculateHeight(rMax, deltaRho));
        assert (height % 2 != 0) : "height (" + height + ") should be odd";

        setCalibration(calculateCalibration(deltaTheta, deltaRho, height));
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
     * Returns the distance increment.
     * 
     * @return height of the pixels in this <code>HoughMap</code>
     */
    public Magnitude getDeltaRho() {
        return getCalibration().getDY();
    }



    /**
     * Returns the angle increment in radians.
     * 
     * @return width of the pixels in this <code>HoughMap</code>
     */
    public Magnitude getDeltaTheta() {
        return getCalibration().getDX();
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
        str = str.append(super.getPixelInfoLabel(index));

        str.append("  \u03c1 = ");
        Magnitude rho = getRho(index);
        str.append(rho.toString(4, rho.getPreferredUnitsLabel()));

        str.append("  \u03b8 = ");
        str.append(getTheta(index).toString(1, "deg"));

        return str.toString();
    }



    /**
     * Returns the <code>rho</code> value of the specified index.
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
     * Returns the <code>rho</code> value of the specified coordinates.
     * 
     * @param x
     *            x coordinate
     * @param y
     *            y coordinate
     * @return the <code>rho</code> value of the specified index
     * @throws IllegalArgumentException
     *             if either <code>x</code> or <code>y</code> are outside the
     *             <code>HougMap</code>
     * @see #getRho(int)
     */
    public Magnitude getRho(int x, int y) {
        return getRho(getPixArrayIndex(x, y));
    }



    /**
     * Returns the maximum rho value of this <code>HoughMap</code>.
     * 
     * @return maximum rho
     */
    public Magnitude getRhoMax() {
        return rhoMax;
    }



    /**
     * Returns the minimum rho value of this <code>HoughMap</code>.
     * 
     * @return minimum rho
     */
    public Magnitude getRhoMin() {
        return rhoMin;
    }



    /**
     * Returns the <code>theta</code> value of the specified index.
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
     * Returns the <code>theta</code> value of the specified coordinates.
     * 
     * @param x
     *            x coordinate
     * @param y
     *            y coordinate
     * @return the <code>theta</code> value in radians
     * @throws IllegalArgumentException
     *             if either <code>x</code> or <code>y</code> are outside the
     *             <code>HougMap</code>
     * @see #getTheta(int)
     */
    public Magnitude getTheta(int x, int y) {
        return getTheta(getPixArrayIndex(x, y));
    }



    /**
     * Returns the maximum theta value of this <code>HoughMap</code>.
     * 
     * @return minimum theta
     */
    public Magnitude getThetaMax() {
        return thetaMax;
    }



    /**
     * Returns the minimum theta value of this <code>HoughMap</code>.
     * 
     * @return minimum theta
     */
    public Magnitude getThetaMin() {
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
     *            theta coordinate to convert
     * @return the corresponding x coordinate
     * @throws IllegalArgumentException
     *             if <code>theta</code> is not between {@link #thetaMin} and
     *             {@link #thetaMax}
     */
    public int getX(Magnitude theta) {
        if (!theta.areUnits("rad"))
            throw new IllegalArgumentException("Theta units ("
                    + theta.getBaseUnitsLabel()
                    + ") cannot be expressed as \"rad\".");

        Magnitude deltaTheta = getCalibration().getDX();

        if (theta.compareTo(thetaMin.minus(deltaTheta.div(2.0))) < 0
                || theta.compareTo(thetaMax.add(deltaTheta.div(2.0))) > 0)
            throw new IllegalArgumentException("theta (" + theta
                    + ") must be between " + thetaMin + " and " + thetaMax);

        // (theta - thetaMin) / deltaTheta
        // the result is unitless all magnitude have the same units
        return (int) ((theta.minus(thetaMin)).div(deltaTheta).getValue("") + 0.5);
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
        Magnitude deltaRho = getDeltaRho();

        if (!rho.areSameUnits(deltaRho))
            throw new IllegalArgumentException("r (" + rho.getBaseUnitsLabel()
                    + ") must have the same units as deltaR ("
                    + deltaRho.getBaseUnitsLabel() + ").");

        if (rho.compareTo(rhoMin.minus(deltaRho.div(2.0))) < 0
                || rho.compareTo(rhoMax.add(deltaRho.div(2.0))) > 0)
            throw new IllegalArgumentException("rho (" + rho
                    + ") must be between " + rhoMin + " and " + rhoMax);

        // If r < 0, need to round toward the bottom (thus the - 0.5)
        if (rho.getBaseUnitsValue() >= 0)
            // r / deltaR + 0.5
            // the result is unitless since deltaR and r have the same units
            return height / 2 - (int) (rho.div(deltaRho).getValue("") + 0.5);
        else
            // r / deltaR - 0.5
            // the result is unitless since deltaR and r have the same units
            return height / 2 - (int) (rho.div(deltaRho).getValue("") - 0.5);
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
        // TODO: Check if it could also be deltaRho.multiply((height-1) / 2)
        rhoMax = deltaRho.multiply(height / 2);
        rhoMin = rhoMax.multiply(-1);

        thetaMin = deltaTheta.multiply(0);
        thetaMax = deltaTheta.multiply(width - 1);
    }

}
