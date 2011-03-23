package org.ebsdimage.core;

import java.io.File;
import java.io.IOException;

import magnitude.core.Magnitude;

import org.apache.commons.math.geometry.Rotation;
import org.apache.commons.math.geometry.Vector3D;
import org.ebsdimage.TestCase;
import org.junit.Before;
import org.junit.Test;

import ptpshared.geom.EuclideanSpace;
import ptpshared.geom.Plane;
import ptpshared.util.simplexml.ApacheCommonMathMatcher;
import ptpshared.util.simplexml.XmlLoader;
import ptpshared.util.simplexml.XmlSaver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import static ptpshared.geom.Assert.assertEquals;

import static junittools.test.Assert.assertEquals;

public class MicroscopeTest extends TestCase {

    private Microscope microscope;

    private Camera camera;

    private Vector3D tiltAxis;



    @Before
    public void setUp() throws Exception {
        camera =
                new Camera(new Vector3D(1, 0, 0), new Vector3D(0, -1, 0), 0.04,
                        0.03);
        tiltAxis = new Vector3D(0, 1, 0);
        microscope = new Microscope(camera, tiltAxis);

        microscope.setName("Microscope1");

        microscope.setBeamEnergy(15e3);

        microscope.setCameraDistance(0.02);

        microscope.setPatternCenterX(0.5);
        microscope.setPatternCenterY(0.1);

        Rotation r = new Rotation(new Vector3D(0, 0, 1), Math.toRadians(90.0));
        microscope.setSampleRotation(r);

        microscope.setTiltAngle(Math.toRadians(45));

        microscope.setWorkingDistance(0.015);

        microscope.setMagnification(1000);
    }



    @Test
    public void testEqualsObjectObject() {
        assertTrue(microscope.equals(microscope, 1e-3));
        assertFalse(microscope.equals(null, 1e-3));
        assertFalse(microscope.equals(new Object(), 1e-3));
    }



    @Test
    public void testGetAcquisitionPositionCS() {
        double S2 = Math.sqrt(2) / 2;

        EuclideanSpace cs = microscope.getAcquisitionPositionCS();

        assertEquals(new Vector3D(0, 1, 0), cs.i, 1e-6);
        assertEquals(new Vector3D(-S2, 0, S2), cs.j, 1e-6);
        assertEquals(new Vector3D(S2, 0, S2), cs.k, 1e-6);
        assertEquals(new Vector3D(0, 0, -microscope.getWorkingDistance()),
                cs.translation, 1e-6);
    }



    @Test
    public void testGetBeamEnergy() {
        assertEquals(15e3, microscope.getBeamEnergy(), 1e-6);
    }



    @Test
    public void testGetCamera() {
        assertEquals(camera, microscope.getCamera(), 1e-6);
    }



    @Test
    public void testGetCameraCS() {
        EuclideanSpace cs = microscope.getCameraCS();

        assertEquals(new Vector3D(0, -1, 0), cs.i, 1e-6);
        assertEquals(Vector3D.crossProduct(cs.k, cs.i), cs.j, 1e-6);
        assertEquals(new Vector3D(1, 0, 0), cs.k, 1e-6);
        assertEquals(new Vector3D(0.02, 0.02, -0.01), cs.translation, 1e-2);
    }



    @Test
    public void testGetCameraDistance() {
        assertEquals(0.02, microscope.getCameraDistance(), 1e-6);
    }



    @Test
    public void testGetCameraPlane() {
        Plane expected =
                new Plane(new Vector3D(0.02, 0.02, -0.01),
                        new Vector3D(1, 0, 0));
        assertEquals(expected, microscope.getCameraPlane(), 1e-2);
    }



    @Test
    public void testGetMagnitude() {
        assertEquals(1000, microscope.getMagnification(), 1e-6);
    }



    @Test
    public void testGetMicroscopeCS() {
        assertEquals(EuclideanSpace.ORIGIN, microscope.getMicroscopeCS(), 1e-6);
    }



    @Test
    public void testGetName() {
        assertEquals("Microscope1", microscope.getName());
    }



    @Test
    public void testGetPatternCenterX() {
        assertEquals(0.5, microscope.getPatternCenterX(), 1e-6);
    }



    @Test
    public void testGetPatternCenterY() {
        assertEquals(0.1, microscope.getPatternCenterY(), 1e-6);
    }



    @Test
    public void testGetSampleRotation() {
        Rotation expected =
                new Rotation(new Vector3D(0, 0, 1), Math.toRadians(90.0));
        assertEquals(expected, microscope.getSampleRotation(), 1e-6);
    }



    @Test
    public void testGetTiltAngle() {
        assertEquals(Math.toRadians(45.0), microscope.getTiltAngle(), 1e-6);
    }



    @Test
    public void testGetTiltAxis() {
        assertEquals(new Vector3D(0, 1, 0), microscope.getTiltAxis(), 1e-6);
    }



    @Test
    public void testGetWorkingDistance() {
        assertEquals(0.015, microscope.getWorkingDistance(), 1e-6);
    }



    @Test
    public void testMicroscope() {
        Microscope microscope = new Microscope();

        assertEquals("Unnamed", microscope.getName());

        assertEquals(Camera.NO_CAMERA, microscope.getCamera());

        assertEquals(new Vector3D(0, 1, 0), microscope.getTiltAxis(), 1e-6);

        assertEquals(0.0, microscope.getCameraDistance(), 1e-6);
        assertEquals(0.0, microscope.getPatternCenterX(), 1e-6);
        assertEquals(0.0, microscope.getPatternCenterY(), 1e-6);

        assertEquals(Rotation.IDENTITY, microscope.getSampleRotation(), 1e-6);

        assertEquals(0.0, microscope.getTiltAngle(), 1e-6);
        assertEquals(0.0, microscope.getWorkingDistance(), 1e-6);
    }



    @Test
    public void testMicroscopeCameraVector3D() {
        Microscope microscope = new Microscope(camera, tiltAxis);

        assertEquals("Unnamed", microscope.getName());

        assertEquals(new Vector3D(1, 0, 0), microscope.getCamera().n);
        assertEquals(new Vector3D(0, -1, 0), microscope.getCamera().x);
        assertEquals(0.04, microscope.getCamera().width, 1e-6);
        assertEquals(0.03, microscope.getCamera().height, 1e-6);

        assertEquals(new Vector3D(0, 1, 0), microscope.getTiltAxis(), 1e-6);

        assertEquals(0.0, microscope.getCameraDistance(), 1e-6);
        assertEquals(0.0, microscope.getPatternCenterX(), 1e-6);
        assertEquals(0.0, microscope.getPatternCenterY(), 1e-6);

        assertEquals(Rotation.IDENTITY, microscope.getSampleRotation(), 1e-6);

        assertEquals(0.0, microscope.getTiltAngle(), 1e-6);
        assertEquals(0.0, microscope.getWorkingDistance(), 1e-6);
    }



    @Test
    public void testSetBeamEnergyDouble() {
        microscope.setBeamEnergy(10e3);
        assertEquals(10e3, microscope.getBeamEnergy(), 1e-3);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testSetBeamEnergyDoubleException() {
        microscope.setBeamEnergy(-1);
    }



    @Test
    public void testSetBeamEnergyMagnitude() {
        microscope.setBeamEnergy(new Magnitude(10e3, "eV"));
        assertEquals(10e3, microscope.getBeamEnergy(), 1e-3);

        microscope.setBeamEnergy(new Magnitude(10, "keV"));
        assertEquals(10e3, microscope.getBeamEnergy(), 1e-3);
    }



    @Test
    public void testSetCamera() {
        microscope.setCamera(Camera.NO_CAMERA);
        assertEquals(Camera.NO_CAMERA, microscope.getCamera(), 1e-6);
    }



    @Test
    public void testSetCameraDistanceDouble() {
        microscope.setCameraDistance(0.05);
        assertEquals(0.05, microscope.getCameraDistance(), 1e-6);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testSetCameraDistanceDoubleException() {
        microscope.setCameraDistance(-1);
    }



    @Test
    public void testSetCameraDistanceMagnitude() {
        microscope.setCameraDistance(new Magnitude(5, "cm"));
        assertEquals(0.05, microscope.getCameraDistance(), 1e-6);
    }



    @Test
    public void testSetMagnitude() {
        microscope.setMagnification(500);
        assertEquals(500, microscope.getMagnification(), 1e-6);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testSetMagnitudeException() {
        microscope.setMagnification(0);
    }



    @Test
    public void testSetName() {
        microscope.setName("DIFF");
        assertEquals("DIFF", microscope.getName());
    }



    @Test(expected = IllegalArgumentException.class)
    public void testSetNameException() {
        microscope.setName("");
    }



    @Test
    public void testSetPatternCenterX() {
        microscope.setPatternCenterX(0.25);
        assertEquals(0.25, microscope.getPatternCenterX(), 1e-6);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testSetPatternCenterXException1() {
        // less than zero
        microscope.setPatternCenterX(-1);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testSetPatternCenterXException2() {
        // greater than 1
        microscope.setPatternCenterX(2);
    }



    @Test
    public void testSetPatternCenterY() {
        microscope.setPatternCenterY(0.25);
        assertEquals(0.25, microscope.getPatternCenterY(), 1e-6);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testSetPatternCenterYException1() {
        // less than zero
        microscope.setPatternCenterY(-1);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testSetPatternCenterYException2() {
        // greater than 1
        microscope.setPatternCenterY(2);
    }



    @Test
    public void testSetSampleRotation() {
        Rotation r = new Rotation(new Vector3D(0, 0, 1), Math.PI / 3);
        microscope.setSampleRotation(r);
        assertEquals(r, microscope.getSampleRotation(), 1e-6);
    }



    @Test
    public void testSetTiltAngleDouble() {
        microscope.setTiltAngle(Math.toRadians(60.0));
        assertEquals(Math.toRadians(60.0), microscope.getTiltAngle(), 1e-6);
    }



    @Test
    public void testSetTiltAngleMagnitude() {
        microscope.setTiltAngle(new Magnitude(60.0, "deg"));
        assertEquals(Math.toRadians(60.0), microscope.getTiltAngle(), 1e-6);
    }



    @Test
    public void testSetTiltAxis() {
        microscope.setTiltAxis(new Vector3D(1, 0, 0));
        assertEquals(new Vector3D(1, 0, 0), microscope.getTiltAxis(), 1e-6);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testSetTiltAxisException() {
        microscope.setTiltAxis(new Vector3D(0, 0, 0));
    }



    @Test
    public void testSetWorkingDistanceDouble() {
        microscope.setWorkingDistance(0.02);
        assertEquals(0.02, microscope.getWorkingDistance(), 1e-6);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testSetWorkingDistanceDoubleException() {
        microscope.setWorkingDistance(-1);
    }



    @Test
    public void testSetWorkingDistanceMagnitude() {
        microscope.setWorkingDistance(new Magnitude(20, "mm"));
        assertEquals(0.02, microscope.getWorkingDistance(), 1e-6);
    }



    @Test
    public void testXML() throws IOException {
        File file = createTempFile();

        XmlSaver saver = new XmlSaver();
        saver.matchers.registerMatcher(new ApacheCommonMathMatcher());
        saver.save(microscope, file);

        XmlLoader loader = new XmlLoader();
        loader.matchers.registerMatcher(new ApacheCommonMathMatcher());
        Microscope other = loader.load(Microscope.class, file);

        assertEquals(new Vector3D(1, 0, 0), other.getCamera().n);
        assertEquals(new Vector3D(0, -1, 0), other.getCamera().x);
        assertEquals(0.04, other.getCamera().width, 1e-6);
        assertEquals(0.03, other.getCamera().height, 1e-6);

        assertEquals(new Vector3D(0, 1, 0), other.getTiltAxis(), 1e-6);

        assertEquals(0.02, other.getCameraDistance(), 1e-6);
        assertEquals(0.5, other.getPatternCenterX(), 1e-6);
        assertEquals(0.1, other.getPatternCenterY(), 1e-6);

        Rotation expected =
                new Rotation(new Vector3D(0, 0, 1), Math.toRadians(90.0));
        assertEquals(expected, other.getSampleRotation(), 1e-6);

        assertEquals(Math.toRadians(45.0), other.getTiltAngle(), 1e-6);
        assertEquals(0.015, other.getWorkingDistance(), 1e-6);
    }
}
