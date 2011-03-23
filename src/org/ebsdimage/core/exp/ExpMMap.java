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
package org.ebsdimage.core.exp;

import java.util.HashMap;
import java.util.Map.Entry;

import org.ebsdimage.core.EbsdMMap;
import org.ebsdimage.core.EbsdMetadata;

import rmlimage.core.Map;

/**
 * <code>EbsdMMap</code> holding all the results from an <code>Exp</code>.
 * 
 * @author Philippe T. Pinard
 * @see EbsdMMap
 * @see EbsdMetadata
 * @see ExpMetadata
 */
public class ExpMMap extends EbsdMMap {

    /** Header for the ZIP file containing an EbsdMMap. */
    public static final String FILE_HEADER = EbsdMMap.FILE_HEADER + "-EXP";

    /** Version of the ZIP file. */
    public static final int VERSION = 1;



    /**
     * Creates a new <code>ExpMMap</code> with the specified dimensions and
     * metadata. All the required maps are created but are empty.
     * 
     * @param width
     *            width of the maps
     * @param height
     *            height of the maps
     */
    public ExpMMap(int width, int height) {
        super(width, height);
    }



    /**
     * Creates a new <code>ExpMMap</code>.
     * 
     * @param width
     *            width of the map
     * @param height
     *            height of the map
     * @param mapList
     *            list of <code>Map</code> containing all the required maps
     */
    public ExpMMap(int width, int height, HashMap<String, Map> mapList) {
        super(width, height, mapList);
    }



    @Override
    public ExpMMap createMap(int width, int height) {
        return new ExpMMap(width, height);
    }



    @Override
    public ExpMMap duplicate() {
        HashMap<String, Map> mapList = new HashMap<String, Map>();

        for (Entry<String, Map> entry : getEntrySet())
            mapList.put(entry.getKey(), entry.getValue().duplicate());

        ExpMMap dup = new ExpMMap(width, height, mapList);

        dup.setMetadata(getMetadata());
        cloneMetadataFrom(this);

        return dup;
    }

}
