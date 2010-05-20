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
package org.ebsdimage.core.exp;

import org.ebsdimage.core.HoughMap;
import org.ebsdimage.core.Solution;
import org.ebsdimage.core.run.Operation;
import org.ebsdimage.core.sim.Energy;
import org.ebsdimage.core.sim.PatternBandCenter;

import ptpshared.utility.xml.ObjectXml;
import rmlimage.core.*;
import crystallography.core.Reflectors;
import crystallography.core.XrayScatteringFactors;

/**
 * Interface for saving current maps of an experiment.
 * 
 * @author Philippe T. Pinard
 * 
 */
public abstract class CurrentMapsSaver implements ObjectXml {

    /**
     * Saves the pattern map.
     * 
     * @param exp
     *            experiment executing this method
     * @param map
     *            pattern map
     */
    public abstract void savePatternMap(Exp exp, ByteMap map);



    /**
     * Saves the Hough map.
     * 
     * @param exp
     *            experiment executing this method
     * @param map
     *            Hough map
     */
    public abstract void saveHoughMap(Exp exp, HoughMap map);



    /**
     * Saves the peaks map.
     * 
     * @param exp
     *            experiment executing this method
     * @param map
     *            peaks map
     */
    public abstract void savePeaksMap(Exp exp, BinMap map);



    /**
     * Saves any map.
     * 
     * @param exp
     *            experiment executing this method
     * @param op
     *            operation that created the map
     * @param map
     *            a map
     */
    public abstract void saveMap(Exp exp, Operation op, Map map);



    /**
     * Saves a map with the solution is overlaid on top of the source pattern
     * map.
     * 
     * @param exp
     *            experiment executing this method
     * @param sln
     *            solution
     */
    public abstract void saveSolutionOverlay(Exp exp, Solution sln);



    /**
     * Creates a map with the solution is overlaid on top of the source pattern
     * map.
     * 
     * @param exp
     *            experiment executing this method
     * @param sln
     *            solution
     * @return solution map
     */
    protected ByteMap createSolutionOverlay(Exp exp, Solution sln) {
        // Get source pattern map
        ByteMap patternMap = exp.getSourcePatternMap();
        int width = patternMap.width;
        int height = patternMap.height;

        // Draw simulation pattern
        Reflectors refls =
                new Reflectors(sln.phase, new XrayScatteringFactors(), 1);

        PatternBandCenter patternBandCenter =
                new PatternBandCenter(width, height, refls, -1,
                        exp.mmap.calibration, new Energy(exp.mmap.beamEnergy),
                        sln.rotation);

        ByteMap simPatternMap = patternBandCenter.getPatternMap();
        Contrast.expansion(simPatternMap);

        // Combine simulation pattern with source pattern map
        ByteMap slnMap = new ByteMap(width, height);
        MapMath.addition(patternMap, simPatternMap, 1.0, 0.0, slnMap);

        // Draw pattern center
        int pcH = (int) ((exp.mmap.calibration.patternCenterH + 0.5) * width);
        int pcV =
                height
                        - (int) ((exp.mmap.calibration.patternCenterV + 0.5) * height);
        for (int x = pcH - width / 50; x <= pcH + width / 50; x++)
            slnMap.setPixValue(x, pcV, 254);
        for (int y = pcV - height / 50; y <= pcV + height / 50; y++)
            slnMap.setPixValue(pcH, y, 254);

        // Change color for solution and pattern center
        slnMap.lut.setLUT(255, 255, 0, 0);
        slnMap.lut.setLUT(254, 0, 255, 0);

        return slnMap;
    }
}
