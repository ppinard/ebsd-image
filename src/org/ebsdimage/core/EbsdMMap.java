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

import java.util.HashMap;
import java.util.Map.Entry;

import org.apache.commons.math.geometry.Rotation;

import rmlimage.core.Calibration;
import rmlimage.core.Map;
import rmlimage.module.multi.core.MultiMap;
import rmlimage.module.real.core.RealMap;
import rmlshared.util.Arrays;
import crystallography.core.Crystal;
import edu.umd.cs.findbugs.annotations.CheckForNull;

/**
 * <code>MultiMap</code> that defines the minimal <code>Map</code>s that are
 * needed to do EBSD analysis. It also hold metadata that defines the EBSD
 * acquisition.
 * 
 * @author Philippe T. Pinard
 */
public abstract class EbsdMMap extends MultiMap {

    /** Header for the ZIP file containing an EbsdMMap. */
    public static final String FILE_HEADER = "EBSD";

    /**
     * Alias of the map for the first coordinate of the quaternion representing
     * the rotation.
     */
    public static final String Q0 = "Q0";

    /**
     * Alias of the map for the second coordinate of the quaternion representing
     * the rotation.
     */
    public static final String Q1 = "Q1";

    /**
     * Alias of the map for the third coordinate of the quaternion representing
     * the rotation.
     */
    public static final String Q2 = "Q2";

    /**
     * Alias of the map for the fourth coordinate of the quaternion representing
     * the rotation.
     */
    public static final String Q3 = "Q3";

    /** Alias of the map for the phase identification. */
    public static final String PHASES = "Phases";

    /** Alias of the error map. */
    public static final String ERRORS = "Errors";

    /** Microscope parameters. */
    private EbsdMetadata metadata;



    /**
     * Creates a new <code>EbsdMMap</code> with the specified dimensions and
     * metadata. All the required maps are created but are empty.
     * 
     * @param width
     *            width of the maps
     * @param height
     *            height of the maps
     */
    public EbsdMMap(int width, int height) {
        this(width, height, new HashMap<String, Map>());
    }



    /**
     * Creates a new <code>EbsdMMap</code>. A <code>EbsdMMap</code> is a
     * <code>MultiMap</code> holding specific maps and metadata.
     * 
     * @param width
     *            width of the maps
     * @param height
     *            height of the maps
     * @param mapList
     *            aliases and maps
     */
    public EbsdMMap(int width, int height, HashMap<String, Map> mapList) {
        super(width, height);

        // Calibration
        // Sets calibration as the calibration of the first map
        // The add(String, Map) ensures that all maps have the same calibration
        if (mapList.size() > 0)
            setCalibration(mapList.values().toArray(new Map[0])[0].getCalibration());
        else
            setCalibration(Calibration.NONE);

        // Metadata
        setMetadata(EbsdMetadata.DEFAULT);

        // Verify that all the needed Maps are present in the HashMap
        if (!mapList.containsKey(Q0)) {
            RealMap q0 = new RealMap(width, height);
            q0.clear(Float.NaN);
            q0.cloneMetadataFrom(this);
            mapList.put(Q0, q0);
        }
        if (!mapList.get(Q0).getClass().equals(RealMap.class))
            throw new IllegalArgumentException(
                    "First quaternion component map must be a RealMap.");

        if (!mapList.containsKey(Q1)) {
            RealMap q1 = new RealMap(width, height);
            q1.clear(Float.NaN);
            q1.cloneMetadataFrom(this);
            mapList.put(Q1, q1);
        }
        if (!mapList.get(Q1).getClass().equals(RealMap.class))
            throw new IllegalArgumentException(
                    "Second quaternion component map must be a RealMap.");

        if (!mapList.containsKey(Q2)) {
            RealMap q2 = new RealMap(width, height);
            q2.clear(Float.NaN);
            q2.cloneMetadataFrom(this);
            mapList.put(Q2, q2);
        }
        if (!mapList.get(Q2).getClass().equals(RealMap.class))
            throw new IllegalArgumentException(
                    "Third quaternion component map must be a RealMap.");

        if (!mapList.containsKey(Q3)) {
            RealMap q3 = new RealMap(width, height);
            q3.clear(Float.NaN);
            q3.cloneMetadataFrom(this);
            mapList.put(Q3, q3);
        }
        if (!mapList.get(Q3).getClass().equals(RealMap.class))
            throw new IllegalArgumentException(
                    "Fourth quaternion component map must be a RealMap.");

        if (!mapList.containsKey(PHASES)) {
            PhaseMap phasesMap = new PhaseMap(width, height);
            phasesMap.clear((byte) 0);
            phasesMap.cloneMetadataFrom(this);
            mapList.put(PHASES, phasesMap);
        }
        if (!mapList.get(PHASES).getClass().equals(PhaseMap.class))
            throw new IllegalArgumentException(
                    "Phases map must be a PhasesMap.");

        if (!mapList.containsKey(ERRORS)) {
            ErrorMap errorMap = new ErrorMap(width, height);
            errorMap.clear((byte) 0);
            errorMap.cloneMetadataFrom(this);
            mapList.put(ERRORS, errorMap);
        }
        if (!mapList.get(ERRORS).getClass().equals(ErrorMap.class))
            throw new IllegalArgumentException("Error map must be a ErrorMap.");

        // Put all the maps in the underlying multiMap.
        for (Entry<String, Map> entry : mapList.entrySet())
            add(entry.getKey(), entry.getValue());
    }



    @Override
    public abstract EbsdMMap createMap(int width, int height);



    @Override
    public abstract EbsdMMap duplicate();



    /**
     * Returns the map for the errors.
     * 
     * @return error map
     */
    public ErrorMap getErrorMap() {
        return (ErrorMap) getMap(ERRORS);
    }



    /**
     * Returns the metadata.
     * 
     * @return metadata
     */
    public EbsdMetadata getMetadata() {
        return metadata;
    }



    /**
     * Returns the microscope parameters from the metadata.
     * 
     * @return microscope parameters
     */
    public Microscope getMicroscope() {
        return getMetadata().getMicroscope();
    }



    /**
     * Returns the phase at the specified index. Returns <code>null</code> for
     * non-indexed pixel.
     * 
     * @param index
     *            index in the map
     * @return phase or <code>null</code> for non-indexed pixel
     */
    @CheckForNull
    public Crystal getPhase(int index) {
        return getPhaseMap().getItem(getPhaseId(index));
    }



    /**
     * Returns the phase id at the specified index.
     * 
     * @param index
     *            index in the map
     * @return phase id
     */
    public int getPhaseId(int index) {
        if (index < 0 || index >= size)
            throw new IllegalArgumentException(
                    "The index has to be between [0," + size + "[");

        return getPhaseMap().getPixValue(index);
    }



    /**
     * Returns the map for the phases.
     * 
     * @return phases map
     */
    public PhaseMap getPhaseMap() {
        return (PhaseMap) getMap(PHASES);
    }



    /**
     * Returns the phases defined in the phases map.
     * 
     * @return phases
     */
    public Crystal[] getPhases() {
        return getPhaseMap().getPhases();
    }



    /**
     * Returns the map for the first coordinate of the quaternion representing
     * the rotation.
     * 
     * @return Q0 map
     */
    public RealMap getQ0Map() {
        return (RealMap) getMap(Q0);
    }



    /**
     * Returns the map for the second coordinate of the quaternion representing
     * the rotation.
     * 
     * @return Q1 map
     */
    public RealMap getQ1Map() {
        return (RealMap) getMap(Q1);
    }



    /**
     * Returns the map for the third coordinate of the quaternion representing
     * the rotation.
     * 
     * @return Q2 map
     */
    public RealMap getQ2Map() {
        return (RealMap) getMap(Q2);
    }



    /**
     * Returns the map for the fourth coordinate of the quaternion representing
     * the rotation.
     * 
     * @return Q3 map
     */
    public RealMap getQ3Map() {
        return (RealMap) getMap(Q3);
    }



    /**
     * Returns the rotation at the specified index.
     * 
     * @param index
     *            index in the map
     * @return rotation
     */
    public Rotation getRotation(int index) {
        if (index < 0 || index >= size)
            throw new IllegalArgumentException(
                    "The index has to be between [0," + size + "[");

        return new Rotation(getQ0Map().pixArray[index],
                getQ1Map().pixArray[index], getQ2Map().pixArray[index],
                getQ3Map().pixArray[index], false);
    }



    @Override
    public void remove(Map map) {
        String alias = getAlias(map);
        remove(alias);
    }



    @Override
    public void remove(String alias) {
        String[] excluded = new String[] { Q0, Q1, Q2, Q3, PHASES };

        if (Arrays.contains(excluded, alias))
            throw new IllegalArgumentException("The map with alias " + alias
                    + " cannot be removed.");
        else
            super.remove(alias);
    }



    /**
     * Sets the metadata of the EBSD multimap.
     * 
     * @param metadata
     *            new metadata
     */
    public void setMetadata(EbsdMetadata metadata) {
        if (metadata == null)
            throw new NullPointerException("Metadata cannot be null.");

        this.metadata = metadata;
    }
}
