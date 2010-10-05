package org.ebsdimage.core.exp.ops.hough.op;

import static java.lang.Math.PI;
import static java.lang.Math.toDegrees;

import org.ebsdimage.core.HoughMap;
import org.ebsdimage.core.Transform;
import org.ebsdimage.core.exp.Exp;

import rmlimage.core.ByteMap;
import rmlimage.core.Filter;

/**
 * Operation to perform the Hough transform. The resolution in rho is
 * automatically calculated from the resolution in theta to ensure that most of
 * the peaks have an aspect ratio close to unity.
 * 
 * @author ppinard
 */
public class AutoHoughTransform extends HoughOp {

    /** Default resolution in theta of the Hough transform (0.5 deg). */
    public static final double DEFAULT_DELTA_THETA = 0.5 / 180 * PI;

    /** Resolution in theta of the Hough transform (in radians/px). */
    public final double deltaTheta;



    /**
     * Creates a new Hough transform operation with the default values.
     */
    public AutoHoughTransform() {
        this(DEFAULT_DELTA_THETA);
    }



    /**
     * Creates a new Hough transform operation using the specified resolution in
     * theta. The resolution in rho is automatically calculated to ensure that
     * the aspect ratio of the peaks is close to unity (square peaks).
     * 
     * @param resolution
     *            resolution of the Hough transform (in radians)
     * @throws IllegalArgumentException
     *             if the resolution is less or equal to zero
     */
    public AutoHoughTransform(double resolution) {
        if (resolution <= 0)
            throw new IllegalArgumentException("Resolution (" + resolution
                    + ") must be > 0");

        this.deltaTheta = resolution;
    }



    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;

        AutoHoughTransform other = (AutoHoughTransform) obj;
        if (Double.doubleToLongBits(deltaTheta) != Double.doubleToLongBits(other.deltaTheta))
            return false;

        return true;
    }



    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        long temp;
        temp = Double.doubleToLongBits(deltaTheta);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        return result;
    }



    @Override
    public String toString() {
        return "Auto Hough Transform [resolution=" + toDegrees(deltaTheta)
                + " deg/px]";
    }



    @Override
    public HoughMap transform(Exp exp, ByteMap srcMap) {
        HoughMap houghMap = Transform.hough(srcMap, deltaTheta);

        // Apply median to remove gap at theta = 90 deg
        Filter.median(houghMap);

        return houghMap;
    }

}
