package org.ebsdimage.core.exp.ops.hough.op;

import org.ebsdimage.core.HoughMap;
import org.ebsdimage.core.Transform;
import org.ebsdimage.core.exp.Exp;
import org.simpleframework.xml.Attribute;

import rmlimage.core.ByteMap;
import rmlimage.core.Filter;
import static java.lang.Math.abs;
import static java.lang.Math.toDegrees;

/**
 * Operation to perform the Hough transform. The resolution in rho is
 * automatically calculated from the resolution in theta to ensure that most of
 * the peaks have an aspect ratio close to unity.
 * 
 * @author Philippe T. Pinard
 */
public class AutoHoughTransform extends HoughOp {

    /** Resolution in theta of the Hough transform (in radians/px). */
    @Attribute(name = "deltaTheta")
    public final double deltaTheta;

    /** Default operation. */
    public static final AutoHoughTransform DEFAULT = new AutoHoughTransform(
            Math.toRadians(0.5));



    /**
     * Creates a new Hough transform operation using the specified resolution in
     * theta. The resolution in rho is automatically calculated to ensure that
     * the aspect ratio of the peaks is close to unity (square peaks).
     * 
     * @param deltaTheta
     *            resolution of the Hough transform (in radians)
     * @throws IllegalArgumentException
     *             if the resolution is less or equal to zero
     */
    public AutoHoughTransform(@Attribute(name = "deltaTheta") double deltaTheta) {
        if (deltaTheta <= 0)
            throw new IllegalArgumentException("Resolution (" + deltaTheta
                    + ") must be > 0");

        this.deltaTheta = deltaTheta;
    }



    @Override
    public boolean equals(Object obj, Object precision) {
        if (!super.equals(obj, precision))
            return false;

        double delta = ((Number) precision).doubleValue();
        AutoHoughTransform other = (AutoHoughTransform) obj;
        if (abs(deltaTheta - other.deltaTheta) > delta)
            return false;

        return true;
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
