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

import java.awt.Rectangle;

import rmlimage.core.*;
import rmlimage.core.handler.EditHandler;
import rmlshared.util.SystemUtil;
import edu.umd.cs.findbugs.annotations.CheckForNull;
import edu.umd.cs.findbugs.annotations.CheckReturnValue;

/**
 * Methods to edit EBSD specific maps (e.g. Hough map).
 * 
 * @author Philippe T. Pinard
 */
public class Edit implements EditHandler {

    /**
     * Copy the specified <code>ROI</code> from the source <code>BinMap</code>
     * to the specified location in the destination <code>BinMap</code>. The
     * method does not do any clipping. So the source <code>ROI</code> and its
     * destination must be completely inside their respective
     * <code>BinMap</code>s or an exception will be thrown.
     * 
     * @param src
     *            source <code>BinMap</code>
     * @param roi
     *            region to copy
     * @param dest
     *            destination <code>BinMap</code>
     * @param xx1
     *            x coordinate of the upper left corner of the region where to
     *            paste the source pixels
     * @param yy1
     *            y coordinate of the upper left corner of the region where to
     *            paste the source pixels
     * @throws IllegalArgumentException
     *             if the region defined by <code>roi</code> is not completely
     *             inside <code>src</code>.
     * @throws IllegalArgumentException
     *             if pasting the specified region at <code>xx1</code>;
     *             <code>yy1</code> would put part of it outside
     *             <code>dest</code>.
     */
    public static void copy(BinMap src, ROI2 roi, BinMap dest, int xx1, int yy1) {
        copy((EightBitMap) src, roi, (EightBitMap) dest, xx1, yy1);
    }



    /**
     * Copy the specified <code>ROI</code> from the source <code>ByteMap</code>
     * to the specified location in the destination <code>ByteMap</code>. The
     * method does not do any clipping. So the source <code>ROI</code> and its
     * destination must be completely inside their respective
     * <code>ByteMap</code>s or an exception will be thrown.
     * 
     * @param src
     *            source <code>ByteMap</code>
     * @param roi
     *            region to copy
     * @param dest
     *            destination <code>ByteMap</code>
     * @param xx1
     *            x coordinate of the upper left corner of the region where to
     *            paste the source pixels
     * @param yy1
     *            y coordinate of the upper left corner of the region where to
     *            paste the source pixels
     * @throws IllegalArgumentException
     *             if the region defined by <code>roi</code> is not completely
     *             inside <code>src</code>.
     * @throws IllegalArgumentException
     *             if pasting the specified region at <code>xx1</code>;
     *             <code>yy1</code> would put part of it outside
     *             <code>dest</code>.
     */
    public static void copy(ByteMap src, ROI2 roi, ByteMap dest, int xx1,
            int yy1) {
        copy((EightBitMap) src, roi, (EightBitMap) dest, xx1, yy1);
    }



    /**
     * Copy the specified <code>ROI</code> from the source
     * <code>EightBitMap</code> to the specified location in the destination
     * <code>EightBitMap</code>. The method does not do any clipping. So the
     * source <code>ROI</code> and its destination must be completely inside
     * their respective <code>EightBitMap</code>s or an exception will be
     * thrown.
     * 
     * @param src
     *            source <code>EightBitMap</code>
     * @param roi
     *            region to copy
     * @param dest
     *            destination <code>EightBitMap</code>
     * @param xx1
     *            x coordinate of the upper left corner of the region where to
     *            paste the source pixels
     * @param yy1
     *            y coordinate of the upper left corner of the region where to
     *            paste the source pixels
     * @throws IllegalArgumentException
     *             if the region defined by <code>roi</code> is not completely
     *             inside <code>src</code>.
     * @throws IllegalArgumentException
     *             if pasting the specified region at <code>xx1</code>;
     *             <code>yy1</code> would put part of it outside
     *             <code>dest</code>.
     */
    private static void copy(EightBitMap src, ROI2 roi, EightBitMap dest,
            int xx1, int yy1) {
        // Check if the ROI is completely inside src
        if (!roi.contains(src))
            throw new IllegalArgumentException("roi (" + roi.getExtentLabel()
                    + ") is not completely inside src (" + src.getExtentLabel()
                    + ").");

        // Check if putting the ROI at (xx1,yy1) would put it partly outside
        // dest
        ROI2 destROI = new ROI2(roi);
        destROI.translate(xx1 - destROI.getBounds().x, yy1
                - destROI.getBounds().y);
        if (!destROI.contains(dest))
            throw new IllegalArgumentException("Putting roi ("
                    + roi.getExtentLabel() + ") at (" + xx1 + ';' + yy1
                    + ") will put it partly outside dest ("
                    + dest.getExtentLabel() + ").");

        // Copy pix values inside ROI
        byte[] srcPixArray = src.pixArray;
        byte[] destPixArray = dest.pixArray;

        Rectangle bounds = roi.getBounds();
        int xmin = bounds.x;
        int xmax = bounds.x + bounds.width - 1;
        int ymin = bounds.y;
        int ymax = bounds.y + bounds.height - 1;

        byte value;
        int oldIndex;
        int newIndex;

        for (int x = xmin; x <= xmax; x++) {
            for (int y = ymin; y <= ymax; y++) {
                if (roi.contains(x, y)) {
                    oldIndex = src.getPixArrayIndex(x, y);
                    value = srcPixArray[oldIndex];

                    newIndex =
                            dest.getPixArrayIndex(x - xmin + xx1, y - ymin
                                    + yy1);
                    destPixArray[newIndex] = value;
                }
            }
        }

        dest.setChanged(EightBitMap.MAP_CHANGED);
    }



    /**
     * Copy the specified region from the source <code>PhasesMap</code> to the
     * specified location in the destination <code>PhasesMap</code>. The method
     * does not do any clipping. So the source region and its destination must
     * be completely inside their respective <code>PhasesMap</code>s or an
     * exception will be thrown.
     * 
     * @param src
     *            source <code>PhasesMap</code>
     * @param roi
     *            region to copy
     * @param dest
     *            destination <code>PhasesMap</code>
     * @param xx1
     *            x coordinate of the upper left corner of the region where to
     *            paste the source pixels
     * @param yy1
     *            y coordinate of the upper left corner of the region where to
     *            paste the source pixels
     * @throws IllegalArgumentException
     *             if the region defined by <code>x1</code>, <code>y1</code>,
     *             <code>x2</code> and <code>y2</code> is not completely inside
     *             <code>src</code>
     * @throws IllegalArgumentException
     *             if pasting the specified region at <code>xx1</code>;
     *             <code>yy1</code> would put part of it outside
     *             <code>dest</code>
     * @throws NullPointerException
     *             if source map is null
     * @throws NullPointerException
     *             if destination map is null
     */
    public static void copy(PhasesMap src, ROI roi, PhasesMap dest, int xx1,
            int yy1) {
        if (src == null)
            throw new NullPointerException("Src map cannot be null");
        if (dest == null)
            throw new NullPointerException("Dest map cannot be null");

        // Check if the ROI is completely inside src
        if (!roi.isInside(src))
            throw new IllegalArgumentException("roi (" + roi.getExtentLabel()
                    + ") is not completely inside src (" + src.getExtentLabel()
                    + ").");

        // Check if putting the ROI at xx1;yy1 would put it partly outside dest
        ROI destROI = new ROI(roi);
        destROI.x = xx1;
        destROI.y = yy1;
        if (!destROI.isInside(dest))
            throw new IllegalArgumentException("Putting roi ("
                    + roi.getExtentLabel() + ") at (" + xx1 + ';' + yy1
                    + ") will put it partly outside dest ("
                    + dest.getExtentLabel() + ").");

        byte[] srcPixArray = src.pixArray;
        byte[] destPixArray = dest.pixArray;
        int roiWidth = roi.width;
        int roiHeight = roi.height;
        int srcStartIndex = roi.getYMin() * src.width + roi.getXMin();
        int destStartIndex = yy1 * dest.width + xx1;
        for (int n = 0; n < roiHeight; n++) {
            System.arraycopy(srcPixArray, srcStartIndex, destPixArray,
                    destStartIndex, roiWidth);
            srcStartIndex += src.width;
            destStartIndex += dest.width;
        }

        dest.setChanged(EightBitMap.MAP_CHANGED);
    }



    /**
     * Copy the specified <code>ROI</code> from the source <code>RGBMap</code>
     * to the specified location in the destination <code>RGBMap</code>. The
     * method does not do any clipping. So the source <code>ROI</code> and its
     * destination must be completely inside their respective
     * <code>RGBMap</code>s or an exception will be thrown.
     * 
     * @param src
     *            source <code>RGBMap</code>
     * @param roi
     *            region to copy
     * @param dest
     *            destination <code>RGBMap</code>
     * @param xx1
     *            x coordinate of the upper left corner of the region where to
     *            paste the source pixels
     * @param yy1
     *            y coordinate of the upper left corner of the region where to
     *            paste the source pixels
     * @throws IllegalArgumentException
     *             if the region defined by <code>roi</code> is not completely
     *             inside <code>src</code>.
     * @throws IllegalArgumentException
     *             if pasting the specified region at <code>xx1</code>;
     *             <code>yy1</code> would put part of it outside
     *             <code>dest</code>.
     */
    public static void copy(RGBMap src, ROI2 roi, RGBMap dest, int xx1, int yy1) {
        // Check if the ROI is completely inside src
        if (!roi.contains(src))
            throw new IllegalArgumentException("roi (" + roi.getExtentLabel()
                    + ") is not completely inside src (" + src.getExtentLabel()
                    + ").");

        // Check if putting the ROI at (xx1,yy1) would put it partly outside
        // dest
        ROI2 destROI = new ROI2(roi);
        destROI.translate(xx1, yy1);
        if (!destROI.contains(dest))
            throw new IllegalArgumentException("Putting roi ("
                    + roi.getExtentLabel() + ") at (" + xx1 + ';' + yy1
                    + ") will put it partly outside dest ("
                    + dest.getExtentLabel() + ").");

        // Copy pix values inside ROI
        int[] srcPixArray = src.pixArray;
        int[] destPixArray = dest.pixArray;

        Rectangle bounds = roi.getBounds();
        int xmin = bounds.x;
        int xmax = bounds.x + bounds.width - 1;
        int ymin = bounds.y;
        int ymax = bounds.y + bounds.height - 1;

        int value;
        int oldIndex;
        int newIndex;

        for (int x = xmin; x <= xmax; x++) {
            for (int y = ymin; y <= ymax; y++) {
                if (roi.contains(x, y)) {
                    oldIndex = src.getPixArrayIndex(x, y);
                    value = srcPixArray[oldIndex];

                    newIndex = dest.getPixArrayIndex(x + xx1, y + yy1);
                    destPixArray[newIndex] = value;
                }
            }
        }

        dest.setChanged(RGBMap.MAP_CHANGED);
    }



    /**
     * Crops the <code>BinMap</code> to the specified <code>ROI</code>. The
     * <code>ROI</code> coordinates are inclusive. If the <code>ROI</code> is
     * not completely included in the <code>BinMap</code>, an exception will be
     * thrown.
     * <p/>
     * The <code>Properties</code> of the original <code>BinMap</code> will be
     * copied to the cropped one.
     * 
     * @param map
     *            the <code>BinMap</code> to crop.
     * @param roi
     *            the <code>ROI</code> to crop to.
     * @return a new <code>BinMap</code> holding the cropped area. If the
     *         <code>ROI</code> is the whole image, a copy of the
     *         <code>BinMap</code> is returned.
     * @throws IllegalArgumentException
     *             if <code>roi</code> is not completely inside <code>map</code>
     *             .
     */
    public static BinMap crop(BinMap map, ROI2 roi) {
        return (BinMap) crop((EightBitMap) map, roi);
    }



    /**
     * Crops the <code>ByteMap</code> to the specified <code>ROI</code>. The
     * <code>ROI</code> coordinates are inclusive. If the <code>ROI</code> is
     * not completely included in the <code>ByteMap</code>, an exception will be
     * thrown.
     * <p/>
     * The <code>Properties</code> of the original <code>ByteMap</code> will be
     * copied to the cropped one.
     * 
     * @param map
     *            the <code>ByteMap</code> to crop.
     * @param roi
     *            the <code>ROI</code> to crop to.
     * @return a new <code>ByteMap</code> holding the cropped area. If the
     *         <code>ROI</code> is the whole image, a copy of the
     *         <code>ByteMap</code> is returned.
     * @throws IllegalArgumentException
     *             if <code>roi</code> is not completely inside <code>map</code>
     *             .
     */
    public static ByteMap crop(ByteMap map, ROI2 roi) {
        return (ByteMap) crop((EightBitMap) map, roi);
    }



    /**
     * Crops the <code>EightBitMap</code> to the specified <code>ROI</code>. The
     * <code>ROI</code> coordinates are inclusive. If the <code>ROI</code> is
     * not completely included in the <code>EightBitMap</code>, an exception
     * will be thrown.
     * <p/>
     * The <code>Properties</code> of the original <code>EightBitMap</code> will
     * be copied to the cropped one.
     * 
     * @param map
     *            the <code>EightBitMap</code> to crop.
     * @param roi
     *            the <code>ROI</code> to crop to.
     * @return a new <code>EightBitMap</code> holding the cropped area. If the
     *         <code>ROI</code> is the whole image, a copy of the
     *         <code>EightBitMap</code> is returned.
     * @throws IllegalArgumentException
     *             if <code>roi</code> is not completely inside the
     *             <code>map</code>
     */
    private static EightBitMap crop(EightBitMap map, ROI2 roi) {
        // Create an empty EightBitMap of the correct dimension
        // and with the same properties
        Rectangle bounds = roi.getBounds();

        EightBitMap cropMap =
                (EightBitMap) map.createMap(bounds.width, bounds.height);
        cropMap.setProperties(map);

        copy(map, roi, cropMap, 0, 0);

        return cropMap;
    }



    /**
     * Crops the specified <code>HoughMap</code>. The <code>HoughMap</code> is
     * only cropped in the vertical direction. The cropping will be symmetrical
     * above and below the center of the <code>HoughMap</code> (<code>r=0</code>
     * )
     * <p/>
     * This means that the cropped <code>HoughMap</code> will have the same
     * width as the original. The only pixels kept will be the one corresponding
     * to a distance lower than <code>r</code> and higher than <code>-r</code>
     * (inclusive). The distance axis being discrete, the specified distance (
     * <code>r</code>) will be rounded to the nearest distance value
     * corresponding to a pixel. For example, if <code>deltaR = 1</code>,
     * specifying a cutoff of <code>r = 5.75</code>, will give an cropped
     * <code>HoughMap</code> with <code>rMax = 6</code>. So always get the
     * <code>rMax</code> value of the cropped <code>HoughMap</code> from the
     * <code>rMax</code> instance variable.
     * 
     * @param map
     *            <code>HoughMap</code> to crop.
     * @param r
     *            distance above an below which to crop
     * @return the cropped <code>HoughMap</code>.
     * @throws IllegalArgumentException
     *             if <code>r</code> is <= 0 or if <code>r</code> is >
     *             <code>rMax</code> of <code>map</code>.
     * @throws NullPointerException
     *             if the Hough map is null
     * @see HoughMap#rMax
     */
    @CheckReturnValue
    public static HoughMap crop(HoughMap map, double r) {
        if (map == null)
            throw new NullPointerException("Hough map cannot be null.");

        if (r <= 0)
            throw new IllegalArgumentException("r (" + r + ") must be > 0.");

        if (r > map.rMax)
            throw new IllegalArgumentException("r (" + r
                    + ") must be <= rMax of " + map.getName() + " (" + map.rMax
                    + ").");

        // Get the cropping coordinates
        int yMin = map.getY(r);
        int yMax = map.getY(-r);

        // Create the new HoughMap
        HoughMap croppedMap =
                new HoughMap(map.width, yMax - yMin + 1, map.deltaR,
                        map.deltaTheta);

        // Calculate the cropping roi
        ROI roi = new ROI(0, yMin, map.width - 1, yMax);

        rmlimage.core.Edit.copy(map, roi, croppedMap, 0, 0);

        return croppedMap;
    }



    /**
     * Crops the <code>Map</code> to the specified <code>ROI</code>. The
     * <code>ROI</code> coordinates are inclusive. The <code>Properties</code>
     * of the original <code>Map</code> will be copied to the cropped one.
     * 
     * @param map
     *            <code>Map</code> to crop.
     * @param roi
     *            <code>ROI</code> to crop to.
     * @return a new <code>Map</code> holding the cropped area. If the
     *         <code>ROI</code> is the whole image, a copy of the
     *         <code>Map</code> is returned.
     * @throws IllegalArgumentException
     *             if <code>roi</code> is not completely inside the
     *             <code>map</code>
     */
    public static Map crop(Map map, ROI2 roi) {
        if (map instanceof ByteMap || map instanceof BinMap)
            return crop(map, roi);

        if (map instanceof RGBMap)
            return crop(map, roi);

        throw new OperationNotSupportedException("Croppingof a "
                + SystemUtil.getClassBaseName(map) + " is not supported.");
    }



    /**
     * Crops the <code>PhasesMap</code> to the specified <code>ROI</code>. The
     * <code>ROI</code> coordinates are inclusive. The <code>Properties</code>
     * of the original <code>PhasesMap</code> will be copied to the cropped one.
     * 
     * @param map
     *            <code>PhasesMap</code> to crop
     * @param roi
     *            <code>ROI</code> to crop to
     * @return a new <code>PhasesMap</code> holding the cropped area. If the
     *         <code>ROI</code> is the whole image, a copy of the
     *         <code>PhasesMap</code> is returned
     * @throws IllegalArgumentException
     *             if <code>ROI</code> is not completely inside the
     *             <code>map</code>
     * @throws NullPointerException
     *             if the map or the ROI is null
     */
    @CheckReturnValue
    public static PhasesMap crop(PhasesMap map, ROI roi) {
        if (map == null)
            throw new NullPointerException("Map cannot be null.");
        if (roi == null)
            throw new NullPointerException("ROI cannot be null.");

        // If the roi is global
        if (roi.isGlobal(map))
            return map.duplicate();

        // Create an empty PhasesMap of the correct dimension
        // and with the same properties
        PhasesMap cropMap = map.createMap(roi.width, roi.height);
        cropMap.setProperties(map);

        // Copy the phases
        cropMap.setPhases(map.getPhases());

        copy(map, roi, cropMap, 0, 0);

        cropMap.validate();

        return cropMap;
    }



    /**
     * Crops the <code>RGBMap</code> to the specified <code>ROI</code>. The
     * <code>ROI</code> coordinates are inclusive. If the <code>ROI</code> is
     * not completely included in the <code>RGBMap</code>, an exception will be
     * thrown.
     * <p/>
     * The <code>Properties</code> of the original <code>RGBMap</code> will be
     * copied to the cropped one.
     * 
     * @param map
     *            the <code>RGBMap</code> to crop.
     * @param roi
     *            the <code>ROI</code> to crop to.
     * @return a new <code>RGBMap</code> holding the cropped area. If the
     *         <code>ROI</code> is the whole image, a copy of the
     *         <code>RGBMap</code> is returned.
     * @throws IllegalArgumentException
     *             if <code>roi</code> is not completely inside the
     *             <code>map</code>
     */
    public static RGBMap crop(RGBMap map, ROI2 roi) {
        // Create an empty RGBMap of the correct dimension
        // and with the same properties
        Rectangle bounds = roi.getBounds();

        RGBMap cropMap = new RGBMap(bounds.width, bounds.height);
        cropMap.setProperties(map);

        copy(map, roi, cropMap, 0, 0);

        return cropMap;
    }



    @Override
    @CheckReturnValue
    @CheckForNull
    public Map crop(Map map, ROI roi) {
        if (map instanceof PhasesMap)
            return crop((PhasesMap) map, roi);
        else
            return null;
    }
}
