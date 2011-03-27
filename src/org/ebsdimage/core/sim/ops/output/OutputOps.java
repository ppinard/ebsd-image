/*
 * EBSD-Image
 * Copyright (C) 2010-2011 Philippe T. Pinard
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

import java.io.IOException;

import org.ebsdimage.core.HoughMath;
import org.ebsdimage.core.HoughPeak;
import org.ebsdimage.core.sim.Band;
import org.ebsdimage.core.sim.Sim;
import org.ebsdimage.core.sim.SimOperation;
import org.ebsdimage.core.sim.ops.patternsim.PatternSimOp;

import rmlimage.core.Map;

/**
 * Superclass of operation to save the output(s).
 * 
 * @author Philippe T. Pinard
 */
public abstract class OutputOps extends SimOperation {

    /**
     * Converts bands calculated in the <code>PatternSimOp</code> to Hough
     * peaks.
     * 
     * @param patternSimOp
     *            pattern simulation operation
     * @return array of <code>HoughPeak</code>
     */
    protected HoughPeak[] bandsToHoughPeaks(PatternSimOp patternSimOp) {
        Band[] bands = patternSimOp.getBands();
        Map patternMap = patternSimOp.getPatternRealMap();

        HoughPeak[] peaks = new HoughPeak[bands.length];

        for (int i = 0; i < bands.length; i++)
            peaks[i] =
                    HoughMath.getHoughPeak(bands[i].middle, patternMap,
                            bands[i].reflector.normalizedIntensity);

        return peaks;
    }



    /**
     * Saves results, operations or parameters of the <code>Sim</code>.
     * 
     * @param sim
     *            simulation executing this method
     * @param patternSimOp
     *            pattern simulation operation
     * @throws IOException
     *             if an error occurs while saving
     */
    public abstract void save(Sim sim, PatternSimOp patternSimOp)
            throws IOException;

}
