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
package org.ebsdimage.core.sim;

import java.util.HashMap;
import java.util.Map.Entry;

import org.ebsdimage.core.EbsdMMap;

import rmlimage.core.Map;

/**
 * <code>EbsdMMap</code> holding all the results from a <code>Sim</code>.
 * 
 * @author Philippe T. Pinard
 * @see EbsdMMap
 */
public class SimMMap extends EbsdMMap {

    /** Header for the ZIP file containing an EbsdMMap. */
    public static final String FILE_HEADER = EbsdMMap.FILE_HEADER + "-SIM";

    /** Version of the ZIP file. */
    public static final int VERSION = 1;



    /**
     * Creates a new <code>SimMMap</code> with the specified dimensions and
     * metadata. All the required maps are created but are empty.
     * 
     * @param width
     *            width of the maps
     * @param height
     *            height of the maps
     */
    public SimMMap(int width, int height) {
        this(width, height, new HashMap<String, Map>());
    }



    /**
     * Creates a new <code>SimMMap</code>.
     * 
     * @param width
     *            width of the map
     * @param height
     *            height of the map
     * @param mapList
     *            list of <code>Map</code> containing all the required maps
     */
    public SimMMap(int width, int height, HashMap<String, Map> mapList) {
        super(width, height, mapList);

        // Override metadata
        setMetadata(SimMetadata.DEFAULT);
    }



    @Override
    public SimMMap createMap(int width, int height) {
        return new SimMMap(width, height);
    }



    @Override
    public SimMMap duplicate() {
        HashMap<String, Map> mapList = new HashMap<String, Map>();

        for (Entry<String, Map> entry : getEntrySet())
            mapList.put(entry.getKey(), entry.getValue().duplicate());

        SimMMap dup = new SimMMap(width, height, mapList);

        dup.setMetadata(getMetadata());
        cloneMetadataFrom(this);

        return dup;
    }



    @Override
    public SimMetadata getMetadata() {
        return (SimMetadata) super.getMetadata();
    }

}
