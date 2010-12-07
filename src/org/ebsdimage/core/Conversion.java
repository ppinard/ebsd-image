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

import static java.lang.Math.PI;
import ptpshared.core.math.Eulers;
import rmlimage.core.ByteMap;
import rmlimage.core.Calibration;
import rmlimage.core.Map;
import rmlimage.core.RGBMap;
import rmlimage.core.handler.ConversionHandler;
import edu.umd.cs.findbugs.annotations.CheckForNull;

/**
 * Convert one type of map to another.
 * 
 * @author Philippe T. Pinard
 */
public class Conversion implements ConversionHandler {

    /**
     * Converts the specified <code>HoughMap</code> to a <code>ByteMap</code>.
     * 
     * @param src
     *            <code>HoughMap</code> to convert.
     * @return the <code>ByteMap</code>.
     */
    public static ByteMap toByteMap(HoughMap src) {
        ByteMap dest = new ByteMap(src.width, src.height);
        toByteMap(src, dest);

        // Clear the MAP_CHANGED flag set by toByteMap because it is a new map
        dest.resetChanged();

        return dest;
    }



    /**
     * Converts the specified <code>HoughMap</code> to a <code>ByteMap</code>.
     * 
     * @param src
     *            <code>HoughMap</code> to convert.
     * @param dest
     *            <code>HoughMap</code> to put the result in
     */
    public static void toByteMap(HoughMap src, ByteMap dest) {
        rmlimage.core.Conversion.validate(src, dest);

        // Copy pixarray
        System.arraycopy(src.pixArray, 0, dest.pixArray, 0, src.size);

        // Copy metadata
        dest.cloneMetadataFrom(src);

        dest.setChanged(Map.MAP_CHANGED);
    }



    /**
     * Converts a <code>PhasesMap</code> to a <code>ByteMap</code>. The
     * information about the phases is lost. Only the <code>PhasesMap</code>
     * pixArray and LUT are copied.
     * 
     * @param src
     *            <code>PhasesMap</code> to convert
     * @return converted map
     */
    public static ByteMap toByteMap(PhasesMap src) {
        ByteMap dest = new ByteMap(src.width, src.height);
        toByteMap(src, dest);

        // Clear the MAP_CHANGED flag set by toByteMap because it is a new map
        dest.resetChanged();

        return dest;
    }



    /**
     * Converts a <code>PhasesMap</code> to a <code>ByteMap</code>. The
     * information about the phases is lost. Only the <code>PhasesMap</code>
     * pixArray and LUT are copied.
     * 
     * @param src
     *            <code>PhasesMap</code> to convert
     * @param dest
     *            <code>PhasesMap</code> to put the result in
     */
    public static void toByteMap(PhasesMap src, ByteMap dest) {
        rmlimage.core.Conversion.validate(src, dest);

        // Copy pixArray
        System.arraycopy(src.pixArray, 0, dest.pixArray, 0, src.size);

        // Copy metadata and LUT
        dest.cloneMetadataFrom(src);

        dest.setChanged(Map.MAP_CHANGED);
    }



    /**
     * Converts the specified <code>ByteMap</code> to a <code>HoughMap</code>.
     * The calibration (deltaTheta and deltaR) of the <code>HoughMap</code> are
     * preserved.
     * <p/>
     * The calibration of the <code>ByteMap</code> must have the same units as
     * deltaR of the <code>HoughMap</code>.
     * 
     * @param src
     *            <code>ByteMap</code> to convert.
     * @param dest
     *            destination <code>HoughMap</code>
     * @throws IllegalArgumentException
     *             if the two maps don't have the same size
     * @throws IllegalArgumentException
     *             if the calibration of the <code>ByteMap</code> does not have
     *             the same units as the deltaR of the <code>HoughMap</code>
     */
    // TODO: Fix toHoughMap with new HoughMap calibration
    public static void toHoughMap(ByteMap src, HoughMap dest) {
        // Validate maps' dimensions
        rmlimage.core.Conversion.validate(src, dest);

        // Validate units of calibration
        if (!src.getCalibration().getDX().areSameUnits(dest.getDeltaTheta()))
            throw new IllegalArgumentException("Units of the map ("
                    + src.getCalibration().getDX().getBaseUnitsLabel()
                    + ") must match the units of deltaTheta ("
                    + dest.getDeltaRho().getBaseUnitsLabel() + ").");
        if (!src.getCalibration().getDY().areSameUnits(dest.getDeltaRho()))
            throw new IllegalArgumentException("Units of the map ("
                    + src.getCalibration().getDY().getBaseUnitsLabel()
                    + ") must match the units of deltaR ("
                    + dest.getDeltaRho().getBaseUnitsLabel() + ").");

        // Copy the pixArray
        System.arraycopy(src.pixArray, 0, dest.pixArray, 0, dest.size);

        // Copy metadata except calibration
        Calibration cal = dest.getCalibration();
        dest.cloneMetadataFrom(src);
        dest.setCalibration(cal);

        dest.setChanged(Map.MAP_CHANGED);
    }



    /**
     * Converts the three Euler maps in an <code>EbsdMMap</code> into a
     * <code>RGBMap</code>.
     * 
     * @param src
     *            an <code>EbsdMMap</code>
     * @return a Eulers <RGBMap</code>
     */
    public static RGBMap toRGBMap(EbsdMMap src) {
        RGBMap dest = new RGBMap(src.width, src.height);
        toRGBMap(src, dest);

        // Clear the MAP_CHANGED flag set by toByteMap because it is a new map
        dest.resetChanged();

        return dest;
    }



    /**
     * Converts the three Euler maps in an <code>EbsdMMap</code> into a
     * <code>RGBMap</code>.
     * 
     * @param src
     *            an <code>EbsdMMap</code>
     * @param dest
     *            <code>RGBMap</code> to put the result in
     */
    public static void toRGBMap(EbsdMMap src, RGBMap dest) {
        rmlimage.core.Conversion.validate(src, dest);

        // Set pixArray values
        int red;
        int green;
        int blue;
        int size = src.size;
        for (int n = 0; n < size; n++) {
            if (src.getPhaseId(n) == 0) {
                red = 0;
                blue = 0;
                green = 0;
            } else {
                Eulers eulers = src.getRotation(n).toEuler();

                red = (int) ((eulers.theta1 + PI) / (2 * PI) * 255 + 0.5);
                assert (red >= 0 && red <= 255) : "Invalid euler1 ("
                        + eulers.theta1 + ") for index " + n + ".\n"
                        + "Should be between -PI and PI.";

                green = (int) (eulers.theta2 / PI * 255 + 0.5);
                assert (green >= 0 && green <= 255) : "Invalid euler2 ("
                        + eulers.theta2 + ") for index " + n + ".\n"
                        + "Should be between 0 and PI.";

                blue = (int) ((eulers.theta3 + PI) / (2 * PI) * 255 + 0.5);
                assert (blue >= 0 && blue <= 255) : "Invalid euler3 ("
                        + eulers.theta3 + ") for index " + n + ".\n"
                        + "Should be between -PI and PI.";
            }

            dest.pixArray[n] = (red << 16) | (green << 8) | blue;
        }

        // Copy metadata and LUT
        dest.cloneMetadataFrom(src);

        dest.setChanged(Map.MAP_CHANGED);
    }



    /**
     * The conversions implemented in this class are:
     * <ul>
     * <li><code>HoughMap</code> -> <code>ByteMap</code></li>
     * <li><code>EbsdMMap</code> (Eulers) -> <code>RGBMap</code></li>
     * </ul>
     * .
     * <p/>
     * {@inheritDoc}
     */
    @CheckForNull
    @Override
    public Map convert(Map map, Class<? extends Map> toType) {
        if (map instanceof HoughMap && toType == ByteMap.class)
            return toByteMap((HoughMap) map);
        else if (map instanceof EbsdMMap && toType == RGBMap.class)
            return toRGBMap((EbsdMMap) map);
        else if (map instanceof PhasesMap && toType == ByteMap.class)
            return toByteMap((PhasesMap) map);
        else
            return null;
    }



    @Override
    public boolean convert(Map src, Map dest) {
        if (src instanceof HoughMap && dest.getClass() == ByteMap.class) {
            toByteMap((HoughMap) src, (ByteMap) dest);
            return true;
        } else if (src instanceof EbsdMMap && dest.getClass() == RGBMap.class) {
            toRGBMap((EbsdMMap) src, (RGBMap) dest);
            return true;
        } else if (src instanceof PhasesMap && dest.getClass() == ByteMap.class) {
            toByteMap((PhasesMap) src, (ByteMap) dest);
            return true;
        } else
            return false;
    }
}
