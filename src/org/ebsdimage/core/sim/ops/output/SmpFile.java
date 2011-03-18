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

import org.ebsdimage.core.sim.Sim;
import org.ebsdimage.core.sim.ops.patternsim.PatternSimOp;
import org.ebsdimage.io.SmpOutputStream;

import rmlshared.io.FileUtil;

/**
 * Saves the simulated pattern in a SMP file and the parameters in a
 * <code>ExpMMap</code>.
 * 
 * @author Philippe T. Pinard
 */
public class SmpFile extends OutputOps {

    /** Default operation. */
    public static final SmpFile DEFAULT = new SmpFile();

    /** SMP file to store simulated pattern. */
    private SmpOutputStream smp;



    @Override
    public void save(Sim sim, PatternSimOp patternSimOp) throws IOException {
        smp.writeMap(patternSimOp.getPatternMap());
    }



    @Override
    public void setUp(Sim sim) {
        super.setUp(sim);

        File smpFile = new File(sim.getDir(), sim.getName());
        smpFile = FileUtil.setExtension(smpFile, "smp");

        try {
            smp = new SmpOutputStream(smpFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    @Override
    public void tearDown(Sim sim) {
        super.tearDown(sim);

        try {
            smp.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
