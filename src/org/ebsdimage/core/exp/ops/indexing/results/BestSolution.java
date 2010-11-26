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
package org.ebsdimage.core.exp.ops.indexing.results;

import org.ebsdimage.core.EbsdMMap;
import org.ebsdimage.core.PhasesMap;
import org.ebsdimage.core.Solution;
import org.ebsdimage.core.exp.Exp;
import org.ebsdimage.core.exp.OpResult;

import rmlimage.module.real.core.RealMap;
import crystallography.core.Crystal;

/**
 * Operation to save the best solution.
 * 
 * @author Philippe T. Pinard
 */
public class BestSolution extends IndexingResultsOps {

    /** Default operation. */
    public static final BestSolution DEFAULT = new BestSolution();



    /**
     * Saves the solution with the lowest fit value. The lattice orientation,
     * the phase and the fit value are saved.
     * 
     * @param exp
     *            experiment executing this method
     * @param solutions
     *            indexing solutions
     * @return result(s)
     */
    @Override
    public OpResult[] calculate(Exp exp, Solution[] solutions) {
        if (solutions.length > 0) {
            // Best result
            Solution sln = solutions[0];

            // Save orientation
            OpResult q0Result =
                    new OpResult(EbsdMMap.Q0, sln.rotation.getQ0(),
                            RealMap.class);
            OpResult q1Result =
                    new OpResult(EbsdMMap.Q1, sln.rotation.getQ1(),
                            RealMap.class);
            OpResult q2Result =
                    new OpResult(EbsdMMap.Q2, sln.rotation.getQ2(),
                            RealMap.class);
            OpResult q3Result =
                    new OpResult(EbsdMMap.Q3, sln.rotation.getQ3(),
                            RealMap.class);

            // Save phase
            int phaseId = -1;
            Crystal[] phases = exp.mmap.getPhases();

            for (int i = 0; i < phases.length; i++)
                if (phases[i].equals(sln.phase)) {
                    phaseId = i;
                    break;
                }

            if (phaseId < 0)
                throw new RuntimeException("The solution phase (" + sln.phase
                        + ") cannot be found in the EbsdMMap's phases.");

            OpResult phaseResult =
                    new OpResult(EbsdMMap.PHASES, phaseId + 1, PhasesMap.class);

            // Save fit
            OpResult fitResult =
                    new OpResult("SolutionFit", sln.fit, RealMap.class);

            return new OpResult[] { q0Result, q1Result, q2Result, q3Result,
                    phaseResult, fitResult };

        } else
            return new OpResult[0];
    }



    @Override
    public String toString() {
        return "Best Solution";
    }
}
