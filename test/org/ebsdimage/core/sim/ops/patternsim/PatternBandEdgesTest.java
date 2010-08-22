package org.ebsdimage.core.sim.ops.patternsim;

import static org.junit.Assert.assertEquals;

import org.ebsdimage.TestCase;
import org.ebsdimage.core.Camera;
import org.ebsdimage.core.sim.Energy;
import org.ebsdimage.core.sim.ops.patternsim.PatternBandEdges;
import org.junit.Before;
import org.junit.Test;

import ptpshared.core.math.Quaternion;
import rmlimage.core.ByteMap;
import rmlimage.module.real.core.RealMap;
import crystallography.core.Reflectors;
import crystallography.core.ScatteringFactorsEnum;
import crystallography.core.crystals.Silicon;

public class PatternBandEdgesTest extends TestCase {

    private PatternBandEdges op;

    private Reflectors reflectors;

    private Camera camera;

    private Energy energy;

    private Quaternion rotation;



    @Before
    public void setUp() throws Exception {
        op = new PatternBandEdges(336, 256, 4, ScatteringFactorsEnum.XRAY);

        reflectors = op.calculateReflectors(new Silicon());
        camera = new Camera(0.0, 0.0, 0.3);
        energy = new Energy(20e3);
        rotation = Quaternion.IDENTITY;
    }



    @Test
    public void testPatternBandEdges() {
        PatternBandEdges op = new PatternBandEdges();
        assertEquals(PatternBandEdges.DEFAULT_WIDTH, op.width);
        assertEquals(PatternBandEdges.DEFAULT_HEIGHT, op.height);
        assertEquals(PatternBandEdges.DEFAULT_MAX_INDEX, op.maxIndex);
        assertEquals(PatternBandEdges.DEFAULT_SCATTER_TYPE, op.scatterType);
    }



    @Test
    public void testPatternBandEdgesIntIntIntScatteringFactorsEnum() {
        assertEquals(336, op.width);
        assertEquals(256, op.height);
        assertEquals(4, op.maxIndex);
        assertEquals(ScatteringFactorsEnum.XRAY, op.scatterType);
    }



    @Test
    public void testCalculateReflectors() {
        assertEquals(94, reflectors.size());
    }



    @Test
    public void testGetBands() {
        op.simulate(camera, reflectors, energy, rotation);
        assertEquals(92, op.getBands().length);
    }



    @Test
    public void testGetPatternMap() {
        op.simulate(camera, reflectors, energy, rotation);

        ByteMap patternMap = op.getPatternMap();

        // patternMap.setFile(new File(FileUtil.getTempDirFile(),
        // "patternfilledband.bmp"));
        // rmlimage.io.IO.save(patternMap);

        ByteMap expectedMap =
                (ByteMap) load("org/ebsdimage/testdata/patternbandedges.bmp");

        expectedMap.assertEquals(patternMap);
    }



    @Test
    public void testGetPatternRealMap() {
        op.simulate(camera, reflectors, energy, rotation);

        RealMap patternMap = op.getPatternRealMap();

        // patternMap.setFile(new File(FileUtil.getTempDirFile(),
        // "patternfilledband.rmp"));
        // rmlimage.module.real.io.IO.save(patternMap);

        RealMap expectedMap =
                (RealMap) load("org/ebsdimage/testdata/patternbandedges.rmp");

        expectedMap.assertEquals(patternMap);
    }

}
