package org.ebsdimage.core;

import rmlimage.core.Result;

/**
 * Result to store the position of the centroids and their intensity.
 * 
 * @author Philippe T. Pinard
 */
public class Centroid extends Result {

    /** Key for the x coordinate. */
    public static final int X = 0;

    /** Key for the y coordinate. */
    public static final int Y = 1;

    /** Key for the intensity results. */
    public static final int INTENSITY = 2;

    /** x coordinate of the centroids. */
    public final double[] x;

    /** y coordinate of the centroids. */
    public final double[] y;

    /** Intensity results. */
    public final double[] intensity;



    /**
     * Creates a new <code>Centroid</code> to hold the <code>x</code>,
     * <code>y</code> and <code>intensity</code> values. The units for the x and
     * y coordinate are set as pixels.
     * 
     * @param nbValues
     *            number of values to store
     */
    public Centroid(int nbValues) {
        this(nbValues, "px");
    }



    /**
     * Creates a new <code>Centroid</code> to hold the <code>x</code>,
     * <code>y</code> and <code>intensity</code> values. The units of x is fixed
     * to radians and the y coordinate is the specified units.
     * 
     * @param nbValues
     *            number of values to store
     * @param calibUnitsY
     *            units for y coordinate
     */
    public Centroid(int nbValues, String calibUnitsY) {
        super(3, nbValues);

        if (calibUnitsY == null)
            throw new NullPointerException("y units cannot be null.");

        name[X] = "Centroid X";
        name[Y] = "Centroid Y";
        name[INTENSITY] = "Intensity";

        units[X] = "rad";
        units[Y] = calibUnitsY;
        units[INTENSITY] = "";

        x = values[X];
        y = values[Y];
        intensity = values[INTENSITY];
    }



    @Override
    public Centroid duplicate() {
        Centroid dup = new Centroid(x.length, units[Y]);
        System.arraycopy(x, 0, dup.x, 0, x.length);
        System.arraycopy(y, 0, dup.y, 0, y.length);
        System.arraycopy(intensity, 0, dup.intensity, 0, intensity.length);
        return dup;
    }



    /**
     * Returns an array of <code>HoughPeak</code> based on these results.
     * 
     * @return array of <code>HoughPeak</code>
     */
    public HoughPeak[] toHoughPeakArray() {
        HoughPeak[] peaks = new HoughPeak[getValueCount()];

        for (int i = 0; i < getValueCount(); i++)
            peaks[i] = new HoughPeak(x[i], y[i], units[Y], intensity[i]);

        return peaks;
    }
}
