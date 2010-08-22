package org.ebsdimage.core.sim.ops.output;

import java.io.File;
import java.io.IOException;

import org.ebsdimage.core.Camera;
import org.ebsdimage.core.exp.ExpMMap;
import org.ebsdimage.core.exp.ExpMetadata;
import org.ebsdimage.core.run.Run;
import org.ebsdimage.core.sim.Sim;
import org.ebsdimage.core.sim.ops.patternsim.PatternSimOp;
import org.ebsdimage.io.SmpOutputStream;
import org.ebsdimage.io.exp.ExpMMapSaver;

import ptpshared.core.math.Quaternion;
import rmlimage.module.real.core.RealMap;

/**
 * Saves the simulated pattern in a SMP file and the parameters in a
 * <code>ExpMMap</code>.
 * 
 * @author ppinard
 */
public class ExpMMapSmpFile extends OutputOps {

    /** Key for the detector distance map. */
    public static final String DD = "dd";

    /** Key for the energy map. */
    public static final String ENERGY = "energy";

    /** Key for the pattern centre horizontal map. */
    public static final String PCH = "pcH";

    /** Key for the pattern centre vertical map. */
    public static final String PCV = "pcV";

    /** Experimental multi-map. */
    private ExpMMap mmap;

    /** SMP file to store simulated pattern. */
    private SmpOutputStream smp;



    @Override
    public void save(Sim sim, PatternSimOp patternSimOp) throws IOException {
        // Save parameters
        int index = sim.getCurrentIndex();
        RealMap map;

        map = ((RealMap) mmap.getMap(ENERGY));
        map.pixArray[index] = (float) sim.getCurrentEnergy().value;

        map = ((RealMap) mmap.getMap(PCH));
        map.pixArray[index] = (float) sim.getCurrentCamera().patternCenterH;

        map = ((RealMap) mmap.getMap(PCV));
        map.pixArray[index] = (float) sim.getCurrentCamera().patternCenterV;

        map = ((RealMap) mmap.getMap(DD));
        map.pixArray[index] = (float) sim.getCurrentCamera().detectorDistance;

        map = mmap.getQ0Map();
        map.pixArray[index] = (float) sim.getCurrentRotation().getQ0();

        map = mmap.getQ1Map();
        map.pixArray[index] = (float) sim.getCurrentRotation().getQ1();

        map = mmap.getQ2Map();
        map.pixArray[index] = (float) sim.getCurrentRotation().getQ2();

        map = mmap.getQ3Map();
        map.pixArray[index] = (float) sim.getCurrentRotation().getQ3();

        int phaseId = mmap.getPhasesMap().getPhaseId(sim.getCurrentCrystal());
        mmap.getPhasesMap().pixArray[index] = (byte) (phaseId & 0xff);

        // Save simulated pattern in SMP
        smp.writeMap(patternSimOp.getPatternMap());
    }



    @Override
    public void setUp(Run run) throws IOException {
        super.setUp(run);

        Sim sim = (Sim) run;

        // Set-up metadata
        double beamEnergy = sim.getEnergies()[0].value;
        double magnification = 1;
        double tiltAngle = 0.0;
        double workingDistance = Double.NaN;
        double pixelWidth = 1.0e-6;
        double pixelHeight = 1.0e-6;
        Quaternion sampleRotation = Quaternion.IDENTITY;
        Camera calibration = sim.getCameras()[0];

        ExpMetadata metadata =
                new ExpMetadata(beamEnergy, magnification, tiltAngle,
                        workingDistance, pixelWidth, pixelHeight,
                        sampleRotation, calibration);

        // Calculate width
        int width =
                sim.getCameras().length * sim.getEnergies().length
                        * sim.getReflectors().length
                        * sim.getRotations().length;
        int height = 1;

        // Init multimap
        mmap = new ExpMMap(width, height, metadata);

        // Set phases
        mmap.getPhasesMap().setPhases(sim.getCrystals());

        // Create new maps
        RealMap map = new RealMap(width, height);
        map.clear(Float.NaN);
        mmap.add(ENERGY, map);

        map = new RealMap(width, height);
        map.clear(Float.NaN);
        mmap.add(PCH, map);

        map = new RealMap(width, height);
        map.clear(Float.NaN);
        mmap.add(PCV, map);

        map = new RealMap(width, height);
        map.clear(Float.NaN);
        mmap.add(DD, map);

        // SMP
        smp =
                new SmpOutputStream(new File(run.getDir(), run.getName()
                        + ".smp"));
    }



    @Override
    public void tearDown(Run run) throws IOException {
        super.tearDown(run);

        // Save multi-map
        new ExpMMapSaver().save(mmap, new File(run.getDir(), run.getName()
                + ".zip"));

        // Save smp;
        smp.close();
    }

}
