package org.ebsdimage.core;

import java.awt.Polygon;
import java.awt.Rectangle;

import rmlimage.core.Map;

/**
 * Region of interest shaped as a polygon.
 * 
 * @author ppinard
 */
public class ROI2 extends Polygon {

    /**
     * Creates a new region of interest with no defined points.
     */
    public ROI2() {
    }



    /**
     * Creates a new region of interest with the specified points.
     * 
     * @param xpoints
     *            x positions of the points
     * @param ypoints
     *            y positions of the points
     * @param npoints
     *            total number of points
     */
    public ROI2(int[] xpoints, int[] ypoints, int npoints) {
        super(xpoints, ypoints, npoints);
    }



    /**
     * Creates a new region of interest from an existing region of interest.
     * Equivalent to cloning.
     * 
     * @param roi
     *            existing region of interest
     */
    public ROI2(ROI2 roi) {
        this(roi.xpoints.clone(), roi.ypoints.clone(), roi.npoints);
    }



    /**
     * Tests if the region of interest is completely inside the map.
     * 
     * @param map
     *            a map
     * @return <code>true</code> if the map is completely inside the region of
     *         interest, <code>false</code> otherwise
     */
    public boolean contains(Map map) {
        if (isEmpty())
            throw new IllegalArgumentException("Empty ROI.");

        Rectangle bounds = getBounds();
        int xmin = bounds.x;
        int ymin = bounds.y;
        int xmax = xmin + bounds.width;
        int ymax = ymin + bounds.height;

        // System.out.println(xmin + "," + ymin + "," + xmax + "," + ymax);

        return (xmin >= 0 && xmax <= map.width && ymin >= 0 && ymax <= map.height);
    }



    /**
     * Returns a label expressing the boundaries of the region of interest.
     * 
     * @return a label
     */
    public String getExtentLabel() {
        if (isEmpty())
            throw new IllegalArgumentException("Empty ROI.");

        Rectangle bounds = getBounds();

        int x = bounds.x;
        int y = bounds.y;
        int width = bounds.width;
        int height = bounds.height;

        return "(" + x + ";" + y + ")->(" + (x + width - 1) + ";"
                + (y + height - 1) + ")";
    }



    /**
     * Tests if the region of interest is partially inside the map.
     * 
     * @param map
     *            a map
     * @return <code>true</code> if the region of interest is partially inside
     *         the map, <code>false</code> otherwise
     */
    public boolean intersects(Map map) {
        return intersects(0, 0, map.width, map.height);
    }



    /**
     * Tests if the region of interest is empty (i.e. contains no points).
     * 
     * @return <code>true</code> if the region of interest contains no points,
     *         <code>false</code> otherwise
     */
    public boolean isEmpty() {
        return npoints == 0;
    }

}
