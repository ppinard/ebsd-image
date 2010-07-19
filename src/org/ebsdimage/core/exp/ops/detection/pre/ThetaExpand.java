package org.ebsdimage.core.exp.ops.detection.pre;

import org.ebsdimage.core.HoughMap;
import org.ebsdimage.core.exp.Exp;

import rmlimage.core.ByteMap;
import rmlimage.core.Edit;
import rmlimage.core.ROI;
import rmlimage.core.Transform;

/**
 * Operation to increase the theta range of the Hough by copying the left side
 * of the Hough to the right side. The left side appears as a mirror on the
 * right side of the Hough map. The result is a expanded Hough ranging from 0
 * deg to 180 deg + the increment.
 * 
 * @author ppinard
 */
public class ThetaExpand extends DetectionPreOps {

    /** Angular increment (in radians) to the Hough theta range. */
    public final double increment;

    /** Default value of the increment. */
    public static final double DEFAULT_INCREMENT = Math.toRadians(2);



    /**
     * Creates a new <code>ThetaExpand</code> with the default increment.
     */
    public ThetaExpand() {
        this(DEFAULT_INCREMENT);
    }



    /**
     * Creates a new <code>ThetaExpand</code> with the specified increment.
     * 
     * @param increment
     *            angular increment (in radians). This increment is added to the
     *            theta range of the Hough map.
     */
    public ThetaExpand(double increment) {
        this.increment = increment;
    }



    /**
     * The process performs the following steps:
     * <ul>
     * <li>Create a new Hough map with a new width equals to the width of the
     * original Hough plus the angular increment</li>
     * <li>Copy old Hough map in new Hough map</li>
     * <li>Select a slice from the left side of the original Hough map</li>
     * <li>Copy and performs a vertical flip of the slice</li>
     * <li>Paste the slice in the new Hough map</li>.
     * </ul> {@inheritDoc}
     */
    @Override
    public HoughMap process(Exp exp, HoughMap srcMap) {
        int incrementWidth = (int) Math.floor(increment / srcMap.deltaTheta);
        int width = srcMap.width;
        int height = srcMap.height;

        // Destination
        int destWidth = width + incrementWidth;
        HoughMap destMap =
                new HoughMap(destWidth, height, srcMap.deltaR,
                        srcMap.deltaTheta);

        // Copy srcMap in destMap
        Edit.copy(srcMap, new ROI(0, 0, width - 1, height - 1), destMap, 0, 0);

        // Slice
        ROI roi = new ROI(0, 0, incrementWidth, height - 1);

        ByteMap slice = Edit.crop(srcMap, roi);
        Transform.verticalFlip(slice);

        Edit.copy(slice, new ROI(0, 0, slice.width - 1, slice.height - 1),
                destMap, width - 1, 0);

        return destMap;
    }



    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        long temp;
        temp = Double.doubleToLongBits(increment);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        return result;
    }



    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;

        ThetaExpand other = (ThetaExpand) obj;
        if (Double.doubleToLongBits(increment) != Double.doubleToLongBits(other.increment))
            return false;

        return true;
    }



    @Override
    public String toString() {
        return "ThetaExpand [increment=" + increment + "]";
    }
}
