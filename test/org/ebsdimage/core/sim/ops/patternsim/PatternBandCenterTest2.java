package org.ebsdimage.core.sim.ops.patternsim;

import java.io.IOException;

import magnitude.core.Magnitude;

import org.apache.commons.math.geometry.Rotation;
import org.apache.commons.math.geometry.RotationOrder;
import org.apache.commons.math.geometry.Vector3D;
import org.ebsdimage.core.Camera;
import org.ebsdimage.core.Microscope;
import org.junit.Before;
import org.junit.Test;

import rmlimage.core.ByteMap;
import rmlimage.io.IO;
import crystallography.core.CrystalFactory;
import crystallography.core.Reflectors;
import crystallography.core.ReflectorsFactory;
import crystallography.core.ScatteringFactorsEnum;

public class PatternBandCenterTest2 {

    private Reflectors reflectors;

    private Microscope microscope;

    private Rotation rotation;

    private PatternSimOp patternOp;



    @Before
    public void setUp() throws Exception {
        reflectors =
                ReflectorsFactory.generate(CrystalFactory.silicon(),
                        ScatteringFactorsEnum.XRAY, 3);

        Camera camera =
                new Camera(new Vector3D(1, 0, 0), new Vector3D(0, -1, 0),
                        new Magnitude(4, "cm"), new Magnitude(3, "cm"));
        microscope = new Microscope(camera, new Vector3D(0, 1, 0));
        microscope.setBeamEnergy(new Magnitude(20, "kV"));
        microscope.setCameraDistance(new Magnitude(2, "cm"));
        microscope.setPatternCenterX(0.5);
        microscope.setPatternCenterY(0.5);
        microscope.setTiltAngle(new Magnitude(-70.0 / 180 * Math.PI, "rad"));
        microscope.setWorkingDistance(new Magnitude(15, "cm"));

        rotation = new Rotation(RotationOrder.ZXZ, 0, 0, 0);

        patternOp = new PatternBandEdges(400, 300);
        patternOp = new PatternBandCenter(400, 300);
    }



    @Test
    public void testDraw() throws IOException {
        patternOp.simulate(null, microscope, reflectors, rotation);

        ByteMap byteMap = patternOp.getPatternMap();

        byteMap.setFile("/tmp/pattern.bmp");

        IO.save(byteMap);
    }
}
