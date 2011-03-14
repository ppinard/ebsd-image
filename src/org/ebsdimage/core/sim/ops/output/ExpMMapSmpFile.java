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
package org.ebsdimage.core.sim.ops.output;

import java.io.File;
import java.io.IOException;

import org.ebsdimage.core.Camera;
import org.ebsdimage.core.EbsdMetadata;
import org.ebsdimage.core.exp.ExpMMap;
import org.ebsdimage.core.run.Run;
import org.ebsdimage.core.sim.Sim;
import org.ebsdimage.core.sim.ops.patternsim.PatternSimOp;
import org.ebsdimage.io.SmpOutputStream;
import org.ebsdimage.io.exp.ExpMMapSaver;

import ptpshared.math.old.Quaternion;
import rmlimage.module.real.core.RealMap;

/**
 * Saves the simulated pattern in a SMP file and the parameters in a
 * <code>ExpMMap</code>.
 * 
 * @author Philippe T. Pinard
 */
public class ExpMMapSmpFile extends OutputOps {

    /** Default operation. */
    public static final ExpMMapSmpFile DEFAULT = new ExpMMapSmpFile();

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

        // Calculate width
        int width =
                sim.getCameras().length * sim.getEnergies().length
                        * sim.getReflectors().length
                        * sim.getRotations().length;
        int height = 1;

        // Init multimap
        mmap = new ExpMMap(width, height);

        // Set-up metadata
        double beamEnergy = sim.getEnergies()[0].value;
        double magnification = 1;
        double tiltAngle = 0.0;
        double workingDistance = Double.NaN;
        Quaternion sampleRotation = Quaternion.IDENTITY;
        Quaternion cameraQuaternion = Quaternion.IDENTITY;
        Camera camera = sim.getCameras()[0];
        mmap.setMetadata(new EbsdMetadata(beamEnergy, magnification, tiltAngle,
                workingDistance, camera, sampleRotation, cameraQuaternion));

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
