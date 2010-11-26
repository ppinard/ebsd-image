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

import static java.lang.Math.ceil;
import rmlimage.core.ByteMap;

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
 */
public class HoughMap extends ByteMap {
    /** Key representing the deltaR value in the prop file. */
    public static final String DELTA_R = "Hough.dr";



    /** Key representing the deltaTheta value in the prop file. */
    public static final String DELTA_THETA = "Hough.dtheta";



    /** Header key to identify a file as a HoughMap. */
    public static final String FILE_HEADER = "HoughMap1";

    /** Key representing the height value in the prop file. */
    public static final String HEIGHT = "Hough.height";

    /** Key representing the width value in the prop file. */
    public static final String WIDTH = "Hough.width";

    /**
     * Calculate the height that a <code>HoughMap</code> will have according to
     * the specified distance scale and increment.
     * 
     * @param rMax
     *            distance axis (vertical) range
     * @param deltaR
     *            distance increment (height of a pixel)
     * @return the height
     */
    protected static int calculateHeight(double rMax, double deltaR) {
        if (rMax <= 0)
            throw new IllegalArgumentException("rMax ( " + rMax
                    + ") must be > 0");

        if (deltaR <= 0)
            throw new IllegalArgumentException("deltaR (" + deltaR
                    + ") must be > 0");

        if (deltaR >= rMax)
            throw new IllegalArgumentException("deltaR (" + deltaR
                    + ") must be lower than rMax (" + rMax + ')');

        return 2 * (int) ceil(rMax / deltaR) + 1;
    }

    /**
     * Calculate the width that a <code>HoughMap</code> will have according to
     * the specified angle increment.
     * 
     * @param deltaTheta
     *            distance increment (width of a pixel)
     * @return the width
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

    /** Distance increment (pixel width) in pixel. */
    public final double deltaR;

    /** Angle increment (pixel height) in radians. */
    public final double deltaTheta;

    /** Maximum value of the distance axis (y = 0). */
    public final double rMax;

    /** Minimum value of the distance axis (y = height-1). */
    public final double rMin;

    /** Maximum value of the angle axis (x = width-1). */
    public final double thetaMax;

    /** Minimum value of the angle axis (x = 0). */
    public final double thetaMin;

    /**
     * Creates a <code>HoughMap</code> with the specified distance and angle
     * increments and the specified distance scale.
     * <p/>
     * Since it might not be possible to get an integer number of pixels with
     * the specified distance axis scale (<code>rMax</code>) and distance
     * increment (<code>deltaR</code>), the latter will be enforced. This means
     * that the distance increment used will be the one specified in the
     * constructor but the distance axis scale might not be the one specified.
     * It will be the same or bigger. The distance scale will be symetric (
     * <code>rMin == -1*rMax</code>).
     * 
     * @param rMax
     *            distance axis (vertical) range
     * @param deltaR
     *            distance increment (Height of a pixel)
     * @param deltaTheta
     *            angle increment in radians (Width of a pixel)
     */
    public HoughMap(double rMax, double deltaR, double deltaTheta) {
        super(calculateWidth(deltaTheta), calculateHeight(rMax, deltaR));
        assert (height % 2 != 0) : "height (" + height + ") should be odd";

        this.deltaR = deltaR;
        this.rMax = height / 2 * this.deltaR;
        rMin = -1 * this.rMax;

        thetaMin = 0;
        this.deltaTheta = deltaTheta;
        thetaMax = (width - 1) * this.deltaTheta;

        setProperty(DELTA_R, this.deltaR);
        setProperty(DELTA_THETA, this.deltaTheta);
    }



    /**
     * Creates a new <code>HoughMap</code> with the specified dimensions and
     * resolutions.
     * 
     * @param width
     *            width of the map
     * @param height
     *            height of the map
     * @param deltaR
     *            distance increment (Height of a pixel)
     * @param deltaTheta
     *            angle increment in radians (Width of a pixel)
     * @throws IllegalArgumentException
     *             if the height is not odd
     * @throws IllegalArgumentException
     *             if the width calculated using the theta resolution does not
     *             match the specified width
     */
    public HoughMap(int width, int height, double deltaR, double deltaTheta) {
        super(width, height);
        if (height % 2 == 0)
            throw new IllegalArgumentException("height (" + height
                    + ") must be odd");

        this.deltaR = deltaR;
        rMax = height / 2 * this.deltaR;
        rMin = -1 * height / 2 * this.deltaR;

        thetaMin = 0;
        this.deltaTheta = deltaTheta;
        thetaMax = (width - 1) * deltaTheta;

        // NOTE: Commented out since the Hough map is still valid without this
        // restriction. The operation ThetaExpand requires to set a different
        // width which extends beyond 180 deg theta max limit.
        // if (calculateWidth(deltaTheta) != width)
        // throw new IllegalArgumentException("width (" + width
        // + ") should be " + calculateWidth(deltaTheta));

        setProperty(DELTA_R, this.deltaR);
        setProperty(DELTA_THETA, this.deltaTheta);
    }



    /**
     * Creates a new <code>HoughMap</code> with the specified dimensions,
     * resolutions and resolution limits.
     * 
     * @param width
     *            width of the map
     * @param height
     *            height of the map
     * @param rMin
     *            minimum value for rho
     * @param rMax
     *            maximum value for rho
     * @param deltaR
     *            distance increment (Height of a pixel)
     * @param thetaMin
     *            minimum value for theta
     * @param thetaMax
     *            maximum value for theta
     * @param deltaTheta
     *            angle increment in radians (Width of a pixel)
     */
    private HoughMap(int width, int height, double rMin, double rMax,
            double deltaR, double thetaMin, double thetaMax, double deltaTheta) {
        super(width, height);

        this.rMin = rMin;
        this.deltaR = deltaR;
        this.rMax = rMax;

        this.thetaMin = thetaMin;
        this.deltaTheta = deltaTheta;
        this.thetaMax = thetaMax;

        setProperty(DELTA_R, this.deltaR);
        setProperty(DELTA_THETA, this.deltaTheta);
    }



    /**
     * Creates a copy of the <code>HoughMap</code>.
     * 
     * @return a copy of the <code>HoughMap</code>
     */
    @Override
    public HoughMap duplicate() {
        HoughMap dup =
                new HoughMap(width, height, rMin, rMax, deltaR, thetaMin,
                        thetaMax, deltaTheta);

        System.arraycopy(pixArray, 0, dup.pixArray, 0, size);
        dup.setProperties(this); // Copy the properties

        return dup;
    }



    /**
     * Return the index in the pixArray of the specified point.
     * 
     * @param r
     *            rho coordinate (in pixel)
     * @param theta
     *            theta coordinate (in radians)
     * @return the index in the pixArray
     * @throws IllegalArgumentException
     *             if either <code>r</code> or <code>theta</code> are outside
     *             the <code>HougMap</code>
     */
    public int getIndex(double r, double theta) {
        return getIndex(getX(theta), getY(r));
    }



    @Override
    public String getPixelInfoLabel(int index) {
        StringBuilder str = new StringBuilder();
        str = str.append(super.getPixelInfoLabel(index));

        str.append("r = ");
        str.append(getR(index));

        str.append("  theta = ");
        str.append(rmlshared.math.Double.round(Math.toDegrees(getTheta(index)),
                1));
        str.append("\u00b0");

        str.append("  value = ");
        str.append(pixArray[index] & 0xff);

        return str.toString();
    }



    @Override
    public double getProperty(String key, double deFault) {
        if (key.equals(DELTA_R))
            return Double.longBitsToDouble(super.getProperty(DELTA_R, 0L));
        else if (key.equals(DELTA_THETA))
            return Double.longBitsToDouble(super.getProperty(DELTA_THETA, 0L));
        else
            return super.getProperty(key, deFault);
    }



    /**
     * Returns the <code>r</code> value of the specified index.
     * 
     * @param index
     *            index
     * @return the <code>r</code> value of the specified index
     * @throws IllegalArgumentException
     *             if <code>index</code> is outside the range of the
     *             <code>pixArray</code>
     */
    public double getR(int index) {
        if (index < 0 || index >= size)
            throw new IllegalArgumentException("index (" + index
                    + ") must be between 0 and " + (size - 1));

        int y = index / width;
        return (height - 1 - y) * deltaR + rMin;
    }



    /**
     * Returns the <code>r</code> value of the specified coordinates.
     * 
     * @param x
     *            x coordinate
     * @param y
     *            y coordinate
     * @return the <code>r</code> value of the specified index
     * @throws IllegalArgumentException
     *             if either <code>x</code> or <code>y</code> are outside the
     *             <code>HougMap</code>
     */
    public double getR(int x, int y) {
        return getR(getPixArrayIndex(x, y));
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
     */
    public double getTheta(int index) {
        if (index < 0 || index >= size)
            throw new IllegalArgumentException("index (" + index
                    + ") must be between 0 and " + (size - 1));

        return (index % width) * deltaTheta + thetaMin;
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
     */
    public double getTheta(int x, int y) {
        return getTheta(getPixArrayIndex(x, y));
    }



    /**
     * {@inheritDoc} The only valid file format is <code>bmp</code>.
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
    public int getX(double theta) {
        if (theta < thetaMin - deltaTheta / 2.0
                || theta > thetaMax + deltaTheta / 2.0)
            throw new IllegalArgumentException("theta (" + theta
                    + ") must be between " + thetaMin + " and " + thetaMax);

        return (int) ((theta - thetaMin) / deltaTheta + 0.5);
    }



    /**
     * Converts the specified r coordinate into the corresponding y coordinate.
     * 
     * @param r
     *            r coordinate to convert
     * @return the corresponding y coordinate
     * @throws IllegalArgumentException
     *             if <code>r</code> is not between {@link #rMin} and
     *             {@link #rMax}
     */
    public int getY(double r) {
        if (r < rMin - deltaR / 2.0 || r > rMax + deltaR / 2.0)
            throw new IllegalArgumentException("rho (" + r
                    + ") must be between " + rMin + " and " + rMax);

        // If r < 0, need to round toward the bottom (thus the - 0.5)
        if (r >= 0)
            return height / 2 - (int) (r / deltaR + 0.5);
        else
            return height / 2 - (int) (r / deltaR - 0.5);
    }



    @Override
    public void setProperty(String key, double value) {
        if (key.equals(DELTA_R))
            super.setProperty(DELTA_R, Double.doubleToLongBits(value));
        else if (key.equals(DELTA_THETA))
            super.setProperty(DELTA_THETA, Double.doubleToLongBits(value));
        else
            super.setProperty(key, value);
    }

}
