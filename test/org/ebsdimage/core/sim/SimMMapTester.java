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

import org.apache.commons.math.geometry.Rotation;
import org.apache.commons.math.geometry.Vector3D;
import org.ebsdimage.TestCase;
import org.ebsdimage.core.*;
import org.junit.Test;

import rmlimage.core.Calibration;
import rmlimage.core.Map;
import rmlimage.module.real.core.RealMap;
import crystallography.core.Crystal;
import crystallography.core.CrystalFactory;
import crystallography.core.ScatteringFactorsEnum;

import static org.junit.Assert.assertEquals;

import static java.lang.Math.toRadians;

import static junittools.test.Assert.assertEquals;

public abstract class SimMMapTester extends TestCase {
    public static SimMMap createSimMMap() {
        Camera camera =
                new Camera(new Vector3D(1, 0, 0), new Vector3D(0, -1, 0), 0.04,
                        0.03);
        Microscope microscope =
                new Microscope("Unnamed", camera, new Vector3D(0, 1, 0));
        AcquisitionConfig acqConfig =
                new AcquisitionConfig(microscope, Math.toRadians(70), 0.015,
                        20e3, 100, Rotation.IDENTITY, 0.0, 0.0, 0.0);

        SimMetadata metadata =
                new SimMetadata(acqConfig, ScatteringFactorsEnum.ELECTRON, 3);

        HashMap<String, Map> mapList = new HashMap<String, Map>();

        RealMap q0Map = new RealMap(2, 2);
        q0Map.pixArray[0] = 1.0f;
        mapList.put(EbsdMMap.Q0, q0Map);

        RealMap q1Map = new RealMap(2, 2);
        q1Map.pixArray[1] = 1.0f;
        mapList.put(EbsdMMap.Q1, q1Map);

        RealMap q2Map = new RealMap(2, 2);
        q2Map.pixArray[2] = 1.0f;
        mapList.put(EbsdMMap.Q2, q2Map);

        RealMap q3Map = new RealMap(2, 2);
        q3Map.pixArray[3] = 1.0f;
        mapList.put(EbsdMMap.Q3, q3Map);

        HashMap<Integer, Crystal> phases = new HashMap<Integer, Crystal>();
        phases.put(1, CrystalFactory.silicon());
        phases.put(2, CrystalFactory.ferrite());
        PhaseMap phasesMap =
                new PhaseMap(2, 2, new byte[] { 0, 1, 2, 1 }, phases);
        mapList.put(EbsdMMap.PHASES, phasesMap);

        SimMMap mmap = new SimMMap(2, 2, mapList);
        mmap.setMetadata(metadata);

        mmap.setCalibration(new Calibration(1e-6, 1e-6, "m"));

        return mmap;
    }

    protected SimMMap mmap;



    @Test
    public void testCreateMapIntInt() {
        SimMMap other = mmap.createMap(2, 2);

        assertEquals(2, other.width);
        assertEquals(2, other.height);

        assertEquals(SimMetadata.DEFAULT, other.getMetadata(), 1e-6);

        RealMap q0Map = other.getQ0Map();
        assertEquals(2, q0Map.width);
        assertEquals(2, q0Map.height);

        RealMap q1Map = other.getQ1Map();
        assertEquals(2, q1Map.width);
        assertEquals(2, q1Map.height);

        RealMap q2Map = other.getQ2Map();
        assertEquals(2, q2Map.width);
        assertEquals(2, q2Map.height);

        RealMap q3Map = other.getQ3Map();
        assertEquals(2, q3Map.width);
        assertEquals(2, q3Map.height);

        PhaseMap phasesMap = other.getPhaseMap();
        assertEquals(2, phasesMap.width);
        assertEquals(2, phasesMap.height);
        assertEquals(1, phasesMap.getItems().size());

        ErrorMap errorMap = other.getErrorMap();
        assertEquals(2, errorMap.width);
        assertEquals(2, errorMap.height);
        assertEquals(1, errorMap.getItems().size());
    }



    @Test
    public void testDuplicate() {
        SimMMap other = mmap.duplicate();

        assertEquals(mmap.width, other.width);
        assertEquals(mmap.height, other.height);
        assertEquals(mmap.size, other.size);

        assertEquals(mmap.getMetadata(), other.getMetadata(), 1e-6);

        mmap.getQ0Map().assertEquals(other.getQ0Map());
        mmap.getQ1Map().assertEquals(other.getQ1Map());
        mmap.getQ2Map().assertEquals(other.getQ2Map());
        mmap.getQ3Map().assertEquals(other.getQ3Map());
        mmap.getPhaseMap().assertEquals(other.getPhaseMap());
    }



    @Test
    public void testGetMetadata() {
        AcquisitionConfig acqConfig = mmap.getAcquisitionConfig();

        assertEquals(20e3, acqConfig.beamEnergy, 1e-3);
        assertEquals(100.0, acqConfig.magnification, 1e-3);
        assertEquals(toRadians(70), acqConfig.tiltAngle, 1e-3);
        assertEquals(15, acqConfig.workingDistance * 1000, 1e-3);

        SimMetadata metadata = mmap.getMetadata();

        assertEquals(ScatteringFactorsEnum.ELECTRON,
                metadata.getScatteringFactors());
        assertEquals(3, metadata.getMaxIndex());
    }



    @Test
    public void testSimMMap() {
        assertEquals(2, mmap.width);
        assertEquals(2, mmap.height);
        assertEquals(4, mmap.size);
    }
}
