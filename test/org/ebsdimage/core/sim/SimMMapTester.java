package org.ebsdimage.core.sim;

import java.util.HashMap;

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
    protected SimMMap mmap;



    public static SimMMap createSimMMap() {
        Camera camera =
                new Camera(new Vector3D(1, 0, 0), new Vector3D(0, -1, 0), 0.04,
                        0.03);
        Microscope microscope = new Microscope(camera, new Vector3D(0, 1, 0));
        microscope.setBeamEnergy(20e3);
        microscope.setMagnification(100);
        microscope.setTiltAngle(Math.toRadians(70));
        microscope.setWorkingDistance(0.015);

        SimMetadata metadata =
                new SimMetadata(microscope, ScatteringFactorsEnum.ELECTRON, 3);

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
    public void testSimMMap() {
        assertEquals(2, mmap.width);
        assertEquals(2, mmap.height);
        assertEquals(4, mmap.size);
    }



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
    public void testGetMetadata() {
        Microscope microscope = mmap.getMicroscope();

        assertEquals(20e3, microscope.getBeamEnergy(), 1e-3);
        assertEquals(100.0, microscope.getMagnification(), 1e-3);
        assertEquals(toRadians(70), microscope.getTiltAngle(), 1e-3);
        assertEquals(15, microscope.getWorkingDistance() * 1000, 1e-3);

        SimMetadata metadata = mmap.getMetadata();

        assertEquals(ScatteringFactorsEnum.ELECTRON, metadata.scatteringFactors);
        assertEquals(3, metadata.maxIndex);
    }
}
