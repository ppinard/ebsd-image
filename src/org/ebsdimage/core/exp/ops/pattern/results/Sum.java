package org.ebsdimage.core.exp.ops.pattern.results;

import org.ebsdimage.core.exp.Exp;
import org.ebsdimage.core.exp.OpResult;

import rmlimage.core.ByteMap;
import rmlimage.core.MapStats;
import rmlimage.core.ROI;
import rmlimage.module.real.core.RealMap;

/**
 * Result operation to calculate the total intensity of a region of the original
 * diffraction pattern. The region is selected using a rectangular area.
 * 
 * @author ppinard
 */
public class Sum extends PatternResultsOps {

    /** Lower x limit as a fraction of the image width. */
    public final double xmin;

    /** Upper x limit as a fraction of the image width. */
    public final double xmax;

    /** Lower y limit as a fraction of the image height. */
    public final double ymin;

    /** Upper y limit as a fraction of the image height. */
    public final double ymax;

    /** Default value of the lower x limit. */
    public static final double DEFAULT_XMIN = 0.0;

    /** Default value of the upper x limit. */
    public static final double DEFAULT_XMAX = 1.0;

    /** Default value of the lower y limit. */
    public static final double DEFAULT_YMIN = 0.0;

    /** Default value of the upper y limit. */
    public static final double DEFAULT_YMAX = 1.0;



    /**
     * Creates a new <code>Sum</code> with the default values.
     */
    public Sum() {
        this(DEFAULT_XMIN, DEFAULT_YMIN, DEFAULT_XMAX, DEFAULT_YMAX);
    }



    /**
     * Creates a new <code>Sum</code> with the specified values.
     * 
     * @param xmin
     *            lower x limit as a fraction of the image width
     * @param xmax
     *            upper x limit as a fraction of the image width
     * @param ymin
     *            lower y limit as a fraction of the image height
     * @param ymax
     *            upper y limit as a fraction of the image height
     * @throws IllegalArgumentException
     *             if the value of one of the parameter is outside [0, 1]
     * @throws IllegalArgumentException
     *             if the lower limit is greater than the upper limit
     */
    public Sum(double xmin, double ymin, double xmax, double ymax) {
        if (xmin < 0 || xmin > 1)
            throw new IllegalArgumentException(
                    "Lower x limit must be between [0,1].");
        if (xmax < 0 || xmax > 1)
            throw new IllegalArgumentException(
                    "Upper x limit must be between [0,1].");
        if (ymin < 0 || ymin > 1)
            throw new IllegalArgumentException(
                    "Lower y limit must be between [0,1].");
        if (ymax < 0 || ymax > 1)
            throw new IllegalArgumentException(
                    "Upper y limit must be between [0,1].");

        if (xmin > xmax)
            throw new IllegalArgumentException(
                    "Lower x limit cannot be greater than the upper x limit.");
        if (ymin > ymax)
            throw new IllegalArgumentException(
                    "Lower y limit cannot be greater than the upper y limit.");

        this.xmin = xmin;
        this.xmax = xmax;
        this.ymin = ymin;
        this.ymax = ymax;
    }



    /**
     * Calculate the total intensity of the specified region of interest. The
     * original source diffraction pattern is used.
     * 
     * @param exp
     *            experiment executing this operation
     * @param srcMap
     *            pattern map
     * @return a result named "AverageRegion"
     */
    @Override
    public OpResult[] calculate(Exp exp, ByteMap srcMap) {
        double sum = calculateSum(srcMap);
        String name =
                "Pattern Sum" + "[(" + xmin + "," + ymin + ")-(" + xmax + ","
                        + ymax + ")]";

        OpResult result = new OpResult(name, sum, RealMap.class);

        return new OpResult[] { result };
    }



    /**
     * Calculate the total intensity of the specified region of interest.
     * 
     * @param originalPattern
     *            original diffraction pattern
     * @return a result named "AverageRegion"
     */
    protected double calculateSum(ByteMap originalPattern) {
        int width = originalPattern.width;
        int height = originalPattern.height;

        ROI roi =
                new ROI((int) (xmin * width), (int) (ymin * height),
                        (int) (xmax * width), (int) (ymax * height));

        return MapStats.sum(originalPattern, roi);
    }



    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        Sum other = (Sum) obj;
        if (Double.doubleToLongBits(xmax) != Double.doubleToLongBits(other.xmax))
            return false;
        if (Double.doubleToLongBits(xmin) != Double.doubleToLongBits(other.xmin))
            return false;
        if (Double.doubleToLongBits(ymax) != Double.doubleToLongBits(other.ymax))
            return false;
        if (Double.doubleToLongBits(ymin) != Double.doubleToLongBits(other.ymin))
            return false;
        return true;
    }



    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        long temp;
        temp = Double.doubleToLongBits(xmax);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(xmin);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(ymax);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(ymin);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        return result;
    }



    @Override
    public String toString() {
        return "Sum [xmin=" + xmin + ", ymin=" + ymin + ", xmax=" + xmax
                + ", ymax=" + ymax + "]";
    }
}
