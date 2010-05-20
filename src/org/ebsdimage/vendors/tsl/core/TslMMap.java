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
package org.ebsdimage.vendors.tsl.core;

import java.util.HashMap;

import org.ebsdimage.core.EbsdMMap;
import org.ebsdimage.core.EbsdMetadata;

import rmlimage.core.Map;
import rmlimage.module.real.core.RealMap;

/**
 * <code>EbsdMMap</code> holding all the data from a TSL acquisition.
 * 
 * <p/>
 * On top of the map defined in {@link EbsdMMap}, a <code>TslMMap</code> holds:
 * <ul>
 * <li>Euler1</li>
 * <li>Euler2</li>
 * <li>Euler3</li>
 * <li>Image quality</li>
 * <li>Confidence index</li>
 * </ul>
 * and the same metadata as {@link EbsdMetadata}.
 * 
 * @author Philippe T. Pinard
 * 
 * @see EbsdMMap
 * @see EbsdMetadata
 * @see TslMetadata
 */
public class TslMMap extends EbsdMMap {

    /** Header for the zip file containing a TslMMap. */
    public static final String FILE_HEADER = EbsdMMap.FILE_HEADER + "-TSL";

    /** Alias of the map of the first euler angle. */
    public static final String EULER1 = "Euler1";

    /** Alias of the map of the second euler angle. */
    public static final String EULER2 = "Euler2";

    /** Alias of the map of the third euler angle. */
    public static final String EULER3 = "Euler3";

    /** Alias of the map of the image quality. */
    public static final String IMAGE_QUALITY = "ImageQuality";

    /** Alias of the map of the confidence index. */
    public static final String CONFIDENCE_INDEX = "confidenceIndex";



    /**
     * Creates a new <code>HklMMap</code>.
     * 
     * @param width
     *            width of the map
     * @param height
     *            height of the map
     * @param mapList
     *            list of <code>Map</code> containing all the required maps
     * @param metadata
     *            metadata associated to the map
     */
    public TslMMap(int width, int height, HashMap<String, Map> mapList,
            TslMetadata metadata) {
        super(width, height, mapList, metadata);

        // Verify that all the needed maps are present in the HashMap
        if (!mapList.containsKey(EULER1))
            throw new IllegalArgumentException("Undefined euler1 map");

        if (!mapList.containsKey(EULER2))
            throw new IllegalArgumentException("Undefined euler2 map");

        if (!mapList.containsKey(EULER3))
            throw new IllegalArgumentException("Undefined euler3 map");

        if (!mapList.containsKey(IMAGE_QUALITY))
            throw new IllegalArgumentException("Undefined image quality map");

        if (!mapList.containsKey(CONFIDENCE_INDEX))
            throw new IllegalArgumentException("Undefined confidence index map");
    }



    /**
     * Creates a new <code>TslMMap</code> with the specified dimensions and
     * metadata. All the required maps are created but are empty.
     * 
     * @param width
     *            width of the map
     * @param height
     *            height of the map
     * @param metadata
     *            metadata associated to the map
     */
    public TslMMap(int width, int height, TslMetadata metadata) {
        super(width, height, metadata);

        RealMap euler1 = new RealMap(width, height);
        euler1.clear(Float.NaN);
        add(EULER1, euler1);

        RealMap euler2 = new RealMap(width, height);
        euler2.clear(Float.NaN);
        add(EULER2, euler2);

        RealMap euler3 = new RealMap(width, height);
        euler3.clear(Float.NaN);
        add(EULER3, euler3);

        RealMap iq = new RealMap(width, height);
        iq.clear(Float.NaN);
        add(IMAGE_QUALITY, iq);

        RealMap ci = new RealMap(width, height);
        ci.clear(Float.NaN);
        add(CONFIDENCE_INDEX, ci);
    }



    @Override
    public TslMMap createMap(int width, int height) {
        return new TslMMap(width, height, getMetadata());
    }



    @Override
    public TslMMap duplicate() {
        HashMap<String, Map> mapList = new HashMap<String, Map>();

        String[] aliases = getAliases();
        for (String alias : aliases)
            mapList.put(alias, getMap(alias).duplicate());

        TslMMap dup = new TslMMap(width, height, mapList, getMetadata());
        dup.setProperties(this);

        return dup;
    }



    /**
     * Returns the map for the confidence index.
     * 
     * @return confidence index map
     */
    public RealMap getConfidenceIndexMap() {
        return (RealMap) getMap(CONFIDENCE_INDEX);
    }



    /**
     * Returns the map for the first euler angle.
     * 
     * @return euler1 map
     */
    public RealMap getEuler1Map() {
        return (RealMap) getMap(EULER1);
    }



    /**
     * Returns the map for the second euler angle.
     * 
     * @return euler2 map
     */
    public RealMap getEuler2Map() {
        return (RealMap) getMap(EULER2);
    }



    /**
     * Returns the map for the third euler angle.
     * 
     * @return euler3 map
     */
    public RealMap getEuler3Map() {
        return (RealMap) getMap(EULER3);
    }



    /**
     * Returns the map for the image quality.
     * 
     * @return image quality map
     */
    public RealMap getImageQualityMap() {
        return (RealMap) getMap(IMAGE_QUALITY);
    }



    @Override
    public TslMetadata getMetadata() {
        return new TslMetadata(beamEnergy, magnification, tiltAngle,
                workingDistance, pixelWidth, pixelHeight, sampleRotation,
                calibration);
    }

}
