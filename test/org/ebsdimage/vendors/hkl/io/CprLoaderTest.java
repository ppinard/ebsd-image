package org.ebsdimage.vendors.hkl.io;

import java.io.File;
import java.io.IOException;

import org.apache.commons.math.geometry.Rotation;
import org.apache.commons.math.geometry.RotationOrder;
import org.apache.commons.math.geometry.Vector3D;
import org.ebsdimage.TestCase;
import org.ebsdimage.core.Camera;
import org.ebsdimage.core.Microscope;
import org.ebsdimage.vendors.hkl.core.HklMetadata;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import static ptpshared.geom.Assert.assertEquals;

public class CprLoaderTest extends TestCase {

    private CprLoader loader;

    private File file;



    @Before
    public void setUp() throws Exception {
        file = getFile("org/ebsdimage/vendors/hkl/testdata/Project1.cpr");
        loader = new CprLoader();
    }



    @Test
    public void testCanLoad() {
        assertTrue(loader.canLoad(file));
        assertFalse(loader.canLoad(getFile("org/ebsdimage/vendors/hkl/testdata/Copper.xml")));
    }



    @Test
    public void testLoad() throws IOException {
        HklMetadata metadata = loader.load(file, new Microscope());

        assertEquals("Project1", metadata.getProjectName());

        Microscope microscope = metadata.getMicroscope();
        assertEquals(250, microscope.getMagnification(), 1e-6);
        assertEquals(15e3, microscope.getBeamEnergy(), 1e-6);
        assertEquals(Math.toRadians(70.0), microscope.getTiltAngle(), 1e-6);
        assertEquals(0.52451, microscope.getPatternCenterX(), 1e-6);
        assertEquals(0.58419, microscope.getPatternCenterY(), 1e-6);
        assertEquals(0.0, microscope.getCameraDistance(), 1e-6);
        assertEquals(new Rotation(RotationOrder.ZXZ, 0.0, 0.0, 0.0),
                microscope.getSampleRotation(), 1e-6);
    }



    @Test
    public void testLoad2() throws IOException {
        Camera camera =
                new Camera(new Vector3D(1, 0, 0), new Vector3D(0, -1, 0), 0.04,
                        0.03);
        Microscope microscope = new Microscope(camera, new Vector3D(0, 1, 0));
        microscope.setWorkingDistance(0.015);

        HklMetadata metadata = loader.load(file, microscope);

        assertEquals("Project1", metadata.getProjectName());

        Microscope other = metadata.getMicroscope();
        assertEquals(250, other.getMagnification(), 1e-6);
        assertEquals(15e3, other.getBeamEnergy(), 1e-6);
        assertEquals(Math.toRadians(70.0), other.getTiltAngle(), 1e-6);
        assertEquals(0.52451, other.getPatternCenterX(), 1e-6);
        assertEquals(0.58419, other.getPatternCenterY(), 1e-6);
        assertEquals(0.49695 * 0.04, other.getCameraDistance(), 1e-6);
        assertEquals(new Rotation(RotationOrder.ZXZ, 0.0, 0.0, 0.0),
                other.getSampleRotation(), 1e-6);
        assertEquals(0.015, other.getWorkingDistance(), 1e-6);
    }

}
