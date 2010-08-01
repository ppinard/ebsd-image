package org.ebsdimage.core.exp.ops.detection.post;

import java.util.ArrayList;

import org.ebsdimage.core.exp.Exp;

import rmlimage.core.*;

/**
 * Operation to remove peaks which have an aspect ratio greater than a critical
 * aspect ratio.
 * 
 * @author ppinard
 */
public class ShapeFactor extends DetectionPostOps {

    /** Critical aspect ratio of the peaks. */
    public final double aspectRatio;

    /** Default value for the critical aspect ratio of the peaks. */
    public static final double DEFAULT_ASPECT_RATIO = 2.0;



    /**
     * Creates a new <code>ShapeFactor</code> operation with the default
     * critical aspect ratio.
     */
    public ShapeFactor() {
        this(DEFAULT_ASPECT_RATIO);
    }



    /**
     * Creates a new <code>ShapeFactor</code> operation.
     * 
     * @param aspectRatio
     *            critical aspect ratio of the peaks
     */
    public ShapeFactor(double aspectRatio) {
        if (aspectRatio < 1.0)
            throw new IllegalArgumentException(
                    "Aspect ratio must be greater or equal to 1.0.");

        this.aspectRatio = aspectRatio;
    }



    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;

        ShapeFactor other = (ShapeFactor) obj;
        if (Double.doubleToLongBits(aspectRatio) != Double.doubleToLongBits(other.aspectRatio))
            return false;

        return true;
    }



    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        long temp;
        temp = Double.doubleToLongBits(aspectRatio);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        return result;
    }



    /**
     * Process the peaks map to remove peaks with a greater aspect ratio than
     * the specified critical one.
     * 
     * @param exp
     *            experiment executing this method
     * @param srcMap
     *            peaks map
     * @return result of operation
     */
    @Override
    public BinMap process(Exp exp, BinMap srcMap) {
        IdentMap identMap = Identification.identify(srcMap);

        Feret ferets = Analysis.getFeret(identMap);

        double aspectRatio;
        ArrayList<Integer> keepObjects = new ArrayList<Integer>();
        for (int i = 1; i < identMap.getObjectCount(); i++) {
            aspectRatio = ferets.max[i] / ferets.min[i];

            if (aspectRatio <= this.aspectRatio)
                keepObjects.add(i);
        }

        Identification.keepObjects(identMap,
                keepObjects.toArray(new Integer[0]));

        return Conversion.toBinMap(identMap);
    }



    @Override
    public String toString() {
        return "ShapeFactor [aspectRatio=" + aspectRatio + "]";
    }
}
