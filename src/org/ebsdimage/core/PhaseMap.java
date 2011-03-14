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

import java.util.Map;

import crystallography.core.AtomSites;
import crystallography.core.Crystal;
import crystallography.core.SpaceGroups;
import crystallography.core.UnitCell;

/**
 * <code>Map</code> to hold the location of the different phases. To make sure
 * the map is always valid the method {@link #setPixValue(int, int)} and
 * {@link #setPixValue(int, int, int)} should be used instead of the
 * <code>pixArray</code>.
 * 
 * @author Philippe T. Pinard
 */
public class PhaseMap extends IndexedByteMap<Crystal> {

    /** Header key to identify a file as a PhaseMap. */
    public static final String FILE_HEADER = "PhaseMap2";

    /** Phase when the pixels are not indexed. */
    public static final Crystal NO_PHASE = new Crystal("Unindexed",
            new UnitCell(1, 1, 1, 1, 1, 1), new AtomSites(), SpaceGroups.SG1);



    /**
     * Creates a new <code>PhaseMap</code>.
     * 
     * @param width
     *            width of the map
     * @param height
     *            height of the map
     */
    public PhaseMap(int width, int height) {
        super(width, height, NO_PHASE);
    }



    /**
     * Creates a new <code>PhaseMap</code>.
     * 
     * @param width
     *            width of the map
     * @param height
     *            height of the map
     * @param pixArray
     *            pixels in the map
     * @param items
     *            error codes
     */
    public PhaseMap(int width, int height, byte[] pixArray,
            Map<Integer, Crystal> items) {
        super(width, height, pixArray, NO_PHASE, items);
    }



    /**
     * Creates a new <code>PhaseMap</code>.
     * 
     * @param width
     *            width of the map
     * @param height
     *            height of the map
     * @param items
     *            error codes
     */
    public PhaseMap(int width, int height, Map<Integer, Crystal> items) {
        super(width, height, NO_PHASE, items);
    }



    @Override
    public PhaseMap createMap(int width, int height) {
        return new PhaseMap(width, height);
    }



    @Override
    public PhaseMap duplicate() {
        return (PhaseMap) super.duplicate();
    }



    /**
     * Returns an array of all the phases defined in this map. The phases are
     * NOT ordered.
     * 
     * @return array of phases
     */
    public Crystal[] getPhases() {
        Map<Integer, Crystal> items = getItems();
        items.remove(0); // Remove the "No phase"
        return items.values().toArray(new Crystal[0]);
    }

}
