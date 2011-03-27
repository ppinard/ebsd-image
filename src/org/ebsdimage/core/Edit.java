/*
 * EBSD-Image
 * Copyright (C) 2010-2011 Philippe T. Pinard
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

import java.util.Map.Entry;

import rmlimage.core.ByteMap;
import rmlimage.core.Map;
import rmlimage.core.ROI;
import rmlimage.core.handler.EditHandler;
import edu.umd.cs.findbugs.annotations.CheckReturnValue;

/**
 * Methods to edit EBSD specific maps (e.g. Hough map).
 * 
 * @author Philippe T. Pinard
 */
public class Edit implements EditHandler {

    /**
     * Copy the specified region from the source <code>IndexedByteMap</code> to
     * the specified location in the destination <code>IndexedByteMap</code>.
     * The method does not do any clipping. So the source region and its
     * destination must be completely inside their respective
     * <code>IndexedByteMap</code>s or an exception will be thrown.
     * 
     * @param <Item>
     *            type of item of the <code>IndexedByteMap</code>
     * @param src
     *            source <code>IndexedByteMap</code>
     * @param roi
     *            region to copy
     * @param dest
     *            destination <code>IndexedByteMap</code>
     * @param xx1
     *            x coordinate of the upper left corner of the region where to
     *            paste the source pixels
     * @param yy1
     *            y coordinate of the upper left corner of the region where to
     *            paste the source pixels
     * @return
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
    public static <Item> void copy(IndexedByteMap<Item> src, ROI roi,
            IndexedByteMap<Item> dest, int xx1, int yy1) {
        rmlimage.core.Edit.copy(src, roi, dest, xx1, yy1);

        // Update items
        for (Entry<Integer, Item> entry : src.getItems().entrySet())
            if (!dest.isRegistered(entry.getKey()))
                dest.register(entry.getKey(), entry.getValue());

        dest.validate();
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
     *            distance above an below which to crop in rho preferred units
     * @return the cropped <code>HoughMap</code>.
     * @throws IllegalArgumentException
     *             if <code>r</code> is <= 0 or if <code>r</code> is >
     *             <code>rMax</code> of <code>map</code>.
     * @throws NullPointerException
     *             if the Hough map is null
     * @see HoughMap#rhoMax
     */
    @CheckReturnValue
    public static HoughMap crop(HoughMap map, double r) {
        if (map == null)
            throw new NullPointerException("Hough map cannot be null.");
        if (r <= 0)
            throw new IllegalArgumentException("r (" + r + ") must be > 0.");
        if (r > map.rhoMax)
            throw new IllegalArgumentException("r (" + r
                    + ") must be <= rMax of " + map.getName() + " ("
                    + map.rhoMax + ").");

        // Get the cropping coordinates
        int yMin = map.getY(r);
        int yMax = map.getY(-r);
        ROI roi = new ROI(0, yMin, map.width - 1, yMax);

        // Create the new ByteMap to store the crop results
        // Note: Cannot use a HoughMap since the calibration has to be NONE
        ByteMap croppedMap = new ByteMap(map.width, yMax - yMin + 1);

        rmlimage.core.Edit.copy(map, roi, croppedMap, 0, 0);

        // Create destination HoughMap
        HoughMap dest =
                new HoughMap(map.width, yMax - yMin + 1, map.getDeltaTheta(),
                        map.getDeltaRho());
        System.arraycopy(croppedMap.pixArray, 0, dest.pixArray, 0,
                croppedMap.size);

        return dest;
    }



    /**
     * Crops the <code>IndexedByteMap</code> to the specified <code>ROI</code>.
     * The <code>ROI</code> coordinates are inclusive. The
     * <code>Properties</code> of the original <code>IndexedByteMap</code> will
     * be copied to the cropped one.
     * 
     * @param <Item>
     *            type of item of the <code>IndexedByteMap</code>
     * @param map
     *            <code>IndexedByteMap</code> to crop
     * @param roi
     *            <code>ROI</code> to crop to
     * @return a new <code>PhasesMap</code> holding the cropped area. If the
     *         <code>ROI</code> is the whole image, a copy of the
     *         <code>IndexedByteMap</code> is returned
     * @throws IllegalArgumentException
     *             if <code>ROI</code> is not completely inside the
     *             <code>map</code>
     * @throws NullPointerException
     *             if the map or the ROI is null
     */
    @CheckReturnValue
    public static <Item> IndexedByteMap<Item> crop(IndexedByteMap<Item> map,
            ROI roi) {
        @SuppressWarnings("unchecked")
        IndexedByteMap<Item> cropMap =
                (IndexedByteMap<Item>) rmlimage.core.Edit.crop(map, roi);

        // Copy the items
        for (Entry<Integer, Item> entry : map.getItems().entrySet())
            if (!cropMap.isRegistered(entry.getKey()))
                cropMap.register(entry.getKey(), entry.getValue());

        cropMap.validate();

        return cropMap;
    }



    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public boolean copy(Map src, ROI roi, Map dest, int xx1, int yy1) {
        if (src instanceof IndexedByteMap && dest instanceof IndexedByteMap) {
            copy((IndexedByteMap) src, roi, (IndexedByteMap) dest, xx1, yy1);
            return true;
        }

        return false;
    }



    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public Map crop(Map map, ROI roi) {
        if (map instanceof IndexedByteMap)
            return crop((IndexedByteMap) map, roi);
        else
            return null;
    }
}
