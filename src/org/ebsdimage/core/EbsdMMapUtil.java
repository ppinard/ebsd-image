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

import static rmlimage.utility.MicroscopeConstants.CLASS;
import static rmlimage.utility.MicroscopeConstants.ENERGY;
import static rmlimage.utility.MicroscopeConstants.MAGNIFICATION;
import static rmlimage.utility.MicroscopeConstants.WORKING_DISTANCE;

import java.util.Map.Entry;

import rmlimage.core.*;
import rmlimage.core.Conversion;
import rmlimage.core.Transform;
import rmlimage.module.multi.core.BasicMultiMap;
import rmlimage.utility.MicronBar;

/**
 * Utilities applying on <code>EbsdMMap</code>.
 * 
 * @author Philippe T. Pinard
 */
public class EbsdMMapUtil {

    /**
     * Pastes a micron bar on all the maps of the <code>EbsdMMap</code>. The
     * micron bar is pasted under the maps without overwriting any pixels. The
     * maps that are not <code>ByteMap</code> or <code>RGBMap</code> are
     * automatically converted to <code>ByteMap</code>. The scaling factor
     * allows the user to increase the size of the maps to have a more well
     * defined micron bar.
     * 
     * @param mmap
     *            an <code>EbsdMMap</code>
     * @param scaleFactor
     *            scaling factor
     * @return a <code>BasicMultiMap</code> containing the maps with a micron
     *         bar
     */
    public static BasicMultiMap pasteMicronBar(EbsdMMap mmap, int scaleFactor) {
        // Micron bar
        MicronBar micronBar = new MicronBar();
        micronBar.showEnergy(true);
        micronBar.showMag(true);
        micronBar.showWorkingDistance(true);
        int micronBarHeight =
                micronBar.getDimension(new NullMap(mmap.width * scaleFactor,
                        mmap.height * scaleFactor)).height;

        // Destination multimap
        BasicMultiMap dest =
                new BasicMultiMap(mmap.width * scaleFactor, mmap.height
                        * scaleFactor + micronBarHeight);
        dest.setProperties(mmap);
        dest.setName(mmap.getName() + "(MicronBar)");

        // Create micron bar maps
        Map map;
        for (Entry<String, Map> entry : mmap.getEntrySet()) {
            if (entry.getValue() instanceof ByteMap) {
                map = entry.getValue().duplicate();
                Contrast.expansion((ByteMap) map);
            } else if (entry.getValue() instanceof RGBMap)
                map = entry.getValue().duplicate();
            else
                map = Conversion.toByteMap(entry.getValue());

            // Scale up
            if (map instanceof ByteMap)
                map =
                        Transform.unbinning((ByteMap) map, scaleFactor,
                                scaleFactor);
            else if (map instanceof RGBMap)
                map =
                        Transform.unbinning((RGBMap) map, scaleFactor,
                                scaleFactor);

            // Set properties
            map.setProperty(CLASS, mmap.getName());
            // map.setProperty(DETECTOR, alias);
            EbsdMetadata metadata = mmap.getMetadata();
            map.setProperty(ENERGY, metadata.beamEnergy);
            map.setProperty(MAGNIFICATION, metadata.magnification);
            map.setProperty(WORKING_DISTANCE, metadata.workingDistance);

            // Paste micron bar
            if (map instanceof ByteMap)
                map = micronBar.pasteUnder((ByteMap) map);
            else if (map instanceof RGBMap)
                map = micronBar.pasteUnder((RGBMap) map);

            // Add map
            dest.add(entry.getKey(), map);
        }

        return dest;
    }
}
