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

import ptpshared.core.math.Quaternion;
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

    /** Header for the zip file containing an EbsdMMap. */
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

    /** Energy of the electron beam in eV. */
    public final double beamEnergy;

    /** Magnification of the EBSD acquisition. */
    public final double magnification;

    /** Angle of sample's tilt in radians. */
    public final double tiltAngle;

    /** Working distance of the EBSD acquisition. */
    public final double workingDistance;

    /** Calibration for the width of the pixel in meters. */
    public final double pixelWidth;

    /** Calibration for the height of the pixel in meters. */
    public final double pixelHeight;

    /** Rotation from the pattern frame (camera) into the sample frame. */
    public final Quaternion sampleRotation;

    /** Calibration of the camera. */
    public final Camera calibration;



    /**
     * Creates a new <code>EbsdMMap</code> with the specified dimensions and
     * metadata. All the required maps are created but are empty.
     * 
     * @param width
     *            width of the maps
     * @param height
     *            height of the maps
     * @param metadata
     *            data defining the EBSD acquisition
     */
    public EbsdMMap(int width, int height, EbsdMetadata metadata) {
        super(width, height);

        RealMap q0 = new RealMap(width, height);
        q0.clear(Float.NaN);
        add(Q0, q0);

        RealMap q1 = new RealMap(width, height);
        q1.clear(Float.NaN);
        add(Q1, q1);

        RealMap q2 = new RealMap(width, height);
        q2.clear(Float.NaN);
        add(Q2, q2);

        RealMap q3 = new RealMap(width, height);
        q3.clear(Float.NaN);
        add(Q3, q3);

        PhasesMap phasesMap = new PhasesMap(width, height);
        phasesMap.clear((byte) 0);
        add(PHASES, phasesMap);

        // Import metadata
        beamEnergy = metadata.beamEnergy;
        magnification = metadata.magnification;
        tiltAngle = metadata.tiltAngle;
        workingDistance = metadata.workingDistance;
        pixelWidth = metadata.pixelWidth;
        pixelHeight = metadata.pixelHeight;
        sampleRotation = metadata.sampleRotation;
        calibration = metadata.calibration;
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
     * @param metadata
     *            data defining the EBSD acquisition
     */
    public EbsdMMap(int width, int height, HashMap<String, Map> mapList,
            EbsdMetadata metadata) {
        super(width, height);

        // Verify that all the needed Maps are present in the HashMap
        if (!mapList.containsKey(Q0))
            throw new IllegalArgumentException(
                    "Undefined first quaternion component map");
        if (!mapList.get(Q0).getClass().equals(RealMap.class))
            throw new IllegalArgumentException(
                    "First quaternion component map must be a RealMap.");

        if (!mapList.containsKey(Q1))
            throw new IllegalArgumentException(
                    "Undefined second quaternion component map");
        if (!mapList.get(Q1).getClass().equals(RealMap.class))
            throw new IllegalArgumentException(
                    "Second quaternion component map must be a RealMap.");

        if (!mapList.containsKey(Q2))
            throw new IllegalArgumentException(
                    "Undefined third quaternion component map");
        if (!mapList.get(Q2).getClass().equals(RealMap.class))
            throw new IllegalArgumentException(
                    "Third quaternion component map must be a RealMap.");

        if (!mapList.containsKey(Q3))
            throw new IllegalArgumentException(
                    "Undefined fourth quaternion component map");
        if (!mapList.get(Q3).getClass().equals(RealMap.class))
            throw new IllegalArgumentException(
                    "Fourth quaternion component map must be a RealMap.");

        if (!mapList.containsKey(PHASES))
            throw new IllegalArgumentException("Undefined phases map");
        if (!mapList.get(PHASES).getClass().equals(PhasesMap.class))
            throw new IllegalArgumentException(
                    "Phases map must be a PhasesMap.");

        // Put all the Maps in the underlying MultiMap
        for (Entry<String, Map> entry : mapList.entrySet())
            add(entry.getKey(), entry.getValue());

        // Import metadata
        beamEnergy = metadata.beamEnergy;
        magnification = metadata.magnification;
        tiltAngle = metadata.tiltAngle;
        workingDistance = metadata.workingDistance;
        pixelWidth = metadata.pixelWidth;
        pixelHeight = metadata.pixelHeight;
        sampleRotation = metadata.sampleRotation;
        calibration = metadata.calibration;
    }



    /**
     * Replaces all the pixels that are NaN to the specify value in all the
     * <code>RealMap</code> inside this <code>EbsdMMap</code>.
     * 
     * @param value
     *            new value
     */
    public void clearNaN(float value) {
        for (Map map : getMaps())
            if (map instanceof RealMap)
                ((RealMap) map).clearNaN(value);

        setChanged(Map.MAP_CHANGED);
    }



    @Override
    public abstract EbsdMMap createMap(int width, int height);



    @Override
    public abstract EbsdMMap duplicate();



    /**
     * Returns the metadata.
     * 
     * @return metadata
     */
    public abstract EbsdMetadata getMetadata();



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
        if (index < 0 || index >= size)
            throw new IllegalArgumentException(
                    "The index has to be between [0," + size + "[");

        int phaseId = getPhaseId(index);

        if (phaseId == 0)
            return null;
        else
            return getPhases()[phaseId - 1];
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

        return getPhasesMap().pixArray[index];
    }



    /**
     * Returns the phases defined in the phases map.
     * 
     * @return phases
     */
    public Crystal[] getPhases() {
        return getPhasesMap().getPhases();
    }



    /**
     * Returns the map for the phases.
     * 
     * @return phases map
     */
    public PhasesMap getPhasesMap() {
        return (PhasesMap) getMap(PHASES);
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
    public Quaternion getRotation(int index) {
        if (index < 0 || index >= size)
            throw new IllegalArgumentException(
                    "The index has to be between [0," + size + "[");

        return new Quaternion(getQ0Map().pixArray[index],
                getQ1Map().pixArray[index], getQ2Map().pixArray[index],
                getQ3Map().pixArray[index]);
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
}
