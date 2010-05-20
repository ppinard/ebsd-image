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
import static org.ebsdimage.core.HoughMap.DELTA_R;
import static org.ebsdimage.core.HoughMap.DELTA_THETA;

import java.io.File;
import java.util.Properties;

import ptpshared.core.math.Eulers;
import rmlimage.core.ByteMap;
import rmlimage.core.Map;
import rmlimage.core.RGBMap;
import rmlimage.core.handler.ConversionHandler;
import rmlshared.io.FileUtil;
import edu.umd.cs.findbugs.annotations.CheckForNull;

/**
 * Convert one type of map to another.
 * 
 * @author Philippe T. Pinard
 * 
 */
public class Conversion implements ConversionHandler {

    /**
     * Converts the specified <code>HoughMap</code> to a <code>ByteMap</code>.
     * 
     * @param houghMap
     *            <code>HoughMap</code> to convert.
     * 
     * @return the <code>ByteMap</code>.
     */
    public static ByteMap toByteMap(HoughMap houghMap) {
        ByteMap byteMap = new ByteMap(houghMap.width, houghMap.height);
        System.arraycopy(houghMap.pixArray, 0, byteMap.pixArray, 0,
                houghMap.size);
        byteMap.setProperties(houghMap);
        return byteMap;
    }



    /**
     * Converts a <code>PhasesMap</code> to a <code>ByteMap</code>. The
     * information about the phases is lost. Only the <code>PhasesMap</code>
     * pixArray and LUT are copied.
     * 
     * @param map
     *            a <code>PhasesMap</code>
     * @return converted map
     */
    public static ByteMap toByteMap(PhasesMap map) {
        ByteMap byteMap = new ByteMap(map.width, map.height);

        // Set the ByteMap name to the same as the PhaseMap with the
        // suffix (ByteMap) added.
        File file = map.getFile();
        file = FileUtil.append(file, "(ByteMap)");
        file = FileUtil.setExtension(file, "bmp");
        byteMap.setFile(file);

        // Copy pixArray
        System.arraycopy(map.pixArray, 0, byteMap.pixArray, 0, map.size);

        // Copy LUT
        byteMap.setLUT(map.getLUT());

        // Copy properties
        byteMap.setProperties(map);

        return byteMap;
    }



    /**
     * Converts the specified <code>ByteMap</code> to a <code>HoughMap</code>.
     * <p/>
     * The <code>ByteMap</code> must have the following properties set:
     * <ul>
     * <li>{@link HoughMap#DELTA_R}</li>
     * <li>{@link HoughMap#DELTA_THETA}</li>
     * </ul>
     * <br/>
     * Otherwise, an exception will be thrown.
     * 
     * @param byteMap
     *            <code>ByteMap</code> to convert.
     * 
     * @return the <code>HoughMap</code>.
     * 
     * @throws IllegalArgumentException
     *             if any of the properties listed above are missing.
     * 
     * @see Map#setProperty(String, double)
     */
    public static HoughMap toHoughMap(ByteMap byteMap) {
        // Get the needed properties from the bytemap
        double deltaR =
                Double.longBitsToDouble(byteMap.getProperty(DELTA_R, -1l));
        if (deltaR < 0)
            throw new IllegalArgumentException("Incorrect value for property "
                    + DELTA_R + " (" + deltaR + ").");

        double deltaTheta =
                Double.longBitsToDouble(byteMap.getProperty(DELTA_THETA, -1l));
        if (deltaTheta < 0)
            throw new IllegalArgumentException("Incorrect value for property "
                    + DELTA_THETA + " (" + deltaTheta + ").");

        // Create the HoughMap
        HoughMap houghMap =
                new HoughMap(byteMap.width, byteMap.height, deltaR, deltaTheta);

        // Copy the pixArray
        System.arraycopy(byteMap.pixArray, 0, houghMap.pixArray, 0,
                houghMap.size);

        // Save the ByteMap's properties to the new HoughMap
        // except the one defining the HoughMap's parameters
        // They were already set by the HoughMap constructor
        Properties props = byteMap.getProperties();
        props.remove(HoughMap.DELTA_R);
        props.remove(HoughMap.DELTA_THETA);
        houghMap.setProperties(props);

        // Set the HoughMap name to the same as the ByteMap with the
        // suffix (HoughMap) added.
        File file = byteMap.getFile();
        file = FileUtil.append(file, "(HoughMap)");
        file = FileUtil.setExtension(file, "bmp");
        houghMap.setFile(file);

        return houghMap;
    }



    /**
     * Converts the three Euler maps in an <code>EbsdMMap</code> into a
     * <code>RGBMap</code>.
     * 
     * @param ebsdMMap
     *            an <code>EbsdMMap</code>
     * @return a Eulers <RGBMap</code>
     */
    public static RGBMap toRGBMap(EbsdMMap ebsdMMap) {
        RGBMap rgbMap = new RGBMap(ebsdMMap.width, ebsdMMap.height);

        // Set the RGBMap name to the same as the EbsdMMap with the
        // suffix (RGBMap) added.
        File file = ebsdMMap.getFile();
        file = FileUtil.append(file, "(RGBMap)");
        file = FileUtil.setExtension(file, "bmp");
        rgbMap.setFile(file);

        int red;
        int green;
        int blue;
        int size = ebsdMMap.size;
        for (int n = 0; n < size; n++) {
            if (ebsdMMap.getPhaseId(n) == 0) {
                red = 0;
                blue = 0;
                green = 0;
            } else {
                Eulers eulers = ebsdMMap.getRotation(n).toEuler();

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

            rgbMap.pixArray[n] = (red << 16) | (green << 8) | blue;
        }

        return rgbMap;
    }



    /**
     * The conversions implemented in this class are:
     * <ul>
     * <li><code>HoughMap</code> -> <code>ByteMap</code></li>
     * <li><code>EbsdMMap</code> (Eulers) -> <code>RGBMap</code></li>
     * </ul>
     * .
     * 
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
}
