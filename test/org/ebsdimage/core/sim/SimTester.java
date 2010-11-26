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
package org.ebsdimage.core.sim;

import java.io.File;

import org.ebsdimage.core.Camera;
import org.ebsdimage.core.run.Operation;
import org.ebsdimage.core.sim.ops.patternsim.PatternFilledBand;

import ptpshared.core.math.Eulers;
import ptpshared.core.math.Quaternion;
import rmlshared.io.FileUtil;
import crystallography.core.Crystal;
import crystallography.core.CrystalFactory;
import crystallography.core.ScatteringFactorsEnum;

public abstract class SimTester {

    protected Sim sim;

    public static final File simPath = new File(FileUtil.getTempDirFile(),
            "sim1");



    public static Sim createSim(Operation[] ops) {
        Crystal[] crystals = new Crystal[] { CrystalFactory.silicon() };
        Camera[] cameras = new Camera[] { new Camera(0.1, 0.2, 0.3) };
        Energy[] energies = new Energy[] { new Energy(25e3) };
        Quaternion[] rotations =
                new Quaternion[] { new Quaternion(new Eulers(0.1, 0.2, 0.3)) };

        Sim sim = new Sim(ops, cameras, crystals, energies, rotations);
        sim.setName("Sim1");
        sim.setDir(simPath);

        return sim;
    }



    public static Sim createSim() {
        return createSim(new Operation[] { createPatternSimOp() });
    }



    public static Operation createPatternSimOp() {
        return new PatternFilledBand(335, 255, 1, ScatteringFactorsEnum.XRAY);
    }
}
