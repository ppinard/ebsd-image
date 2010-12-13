package org.ebsdimage.core;

import rmlimage.core.Result;

/**
 * Result to store the position of the centroids and their intensity.
 * 
 * @author ppinard
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
     * <code>y</code> and <code>intensity</code> values. The specified units are
     * assigned to the x and y coordinate.
     * 
     * @param nbValues
     *            number of values to store
     * @param calibUnits
     *            units for the x and y coordinate
     */
    public Centroid(int nbValues, String calibUnits) {
        this(nbValues, calibUnits, calibUnits);
    }



    /**
     * Creates a new <code>Centroid</code> to hold the <code>x</code>,
     * <code>y</code> and <code>intensity</code> values.
     * 
     * @param nbValues
     *            number of values to store
     * @param calibUnitsX
     *            units of the x coordinate
     * @param calibUnitsY
     *            units of the y coordinate
     */
    public Centroid(int nbValues, String calibUnitsX, String calibUnitsY) {
        super(3, nbValues);

        if (calibUnitsX == null)
            throw new NullPointerException("x units cannot be null.");
        if (calibUnitsY == null)
            throw new NullPointerException("y units cannot be null.");

        name[X] = "Centroid X";
        name[Y] = "Centroid Y";
        name[INTENSITY] = "Intensity";

        units[X] = calibUnitsX;
        units[Y] = calibUnitsY;
        units[INTENSITY] = "";

        x = values[X];
        y = values[Y];
        intensity = values[INTENSITY];
    }



    @Override
    public Centroid duplicate() {
        Centroid dup = new Centroid(x.length, units[X], units[Y]);
        System.arraycopy(x, 0, dup.x, 0, x.length);
        System.arraycopy(y, 0, dup.y, 0, y.length);
        System.arraycopy(intensity, 0, dup.intensity, 0, intensity.length);
        return dup;
    }
}
