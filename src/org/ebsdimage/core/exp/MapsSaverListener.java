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
package org.ebsdimage.core.exp;

import java.io.IOException;
import java.util.logging.Logger;

import org.ebsdimage.core.HoughMap;
import org.ebsdimage.core.HoughPeak;
import org.ebsdimage.core.Solution;
import org.ebsdimage.core.exp.ops.detection.op.DetectionOp;
import org.ebsdimage.core.exp.ops.detection.post.DetectionPostOps;
import org.ebsdimage.core.exp.ops.detection.pre.DetectionPreOps;
import org.ebsdimage.core.exp.ops.detection.results.DetectionResultsOps;
import org.ebsdimage.core.exp.ops.hough.op.HoughOp;
import org.ebsdimage.core.exp.ops.hough.post.HoughPostOps;
import org.ebsdimage.core.exp.ops.hough.pre.HoughPreOps;
import org.ebsdimage.core.exp.ops.hough.results.HoughResultsOps;
import org.ebsdimage.core.exp.ops.indexing.op.IndexingOp;
import org.ebsdimage.core.exp.ops.indexing.post.IndexingPostOps;
import org.ebsdimage.core.exp.ops.indexing.pre.IndexingPreOps;
import org.ebsdimage.core.exp.ops.indexing.results.IndexingResultsOps;
import org.ebsdimage.core.exp.ops.pattern.op.PatternOp;
import org.ebsdimage.core.exp.ops.pattern.post.PatternPostOps;
import org.ebsdimage.core.exp.ops.pattern.results.PatternResultsOps;
import org.ebsdimage.core.exp.ops.positioning.op.PositioningOp;
import org.ebsdimage.core.exp.ops.positioning.post.PositioningPostOps;
import org.ebsdimage.core.exp.ops.positioning.pre.PositioningPreOps;
import org.ebsdimage.core.exp.ops.positioning.results.PositioningResultsOps;
import org.simpleframework.xml.Root;

import rmlimage.core.BinMap;
import rmlimage.core.ByteMap;
import rmlimage.core.Map;
import rmlimage.io.IO;

/**
 * Listener to save maps to file.
 * 
 * @author Philippe T. Pinard
 */
@Root
public class MapsSaverListener implements ExpListener {

    /** Logger. */
    private final Logger logger = Logger.getLogger("ebsd");



    /**
     * Creates a name for a map from the experiment's name, a given name and
     * experiment's index.
     * 
     * @param exp
     *            an <code>Exp</code>
     * @param name
     *            a given name
     * @return name
     */
    private String createName(Exp exp, String name) {
        return exp.getName() + "_" + name + "_" + exp.getCurrentIndex();
    }



    @Override
    public void detectionOpPerformed(Exp exp, DetectionOp op, BinMap peaksMap) {
        saveMap(exp, op, peaksMap);
    }



    @Override
    public void detectionPostPerformed(Exp exp, DetectionPostOps op,
            BinMap peaksMap) {
        saveMap(exp, op, peaksMap);
    }



    @Override
    public void detectionPrePerformed(Exp exp, DetectionPreOps op,
            HoughMap houghMap) {
        saveMap(exp, op, houghMap);
    }



    @Override
    public void detectionResultsPerformed(Exp exp, DetectionResultsOps op,
            OpResult result) {
    }



    @Override
    public void houghOpPerformed(Exp exp, HoughOp op, HoughMap houghMap) {
        saveMap(exp, op, houghMap);
    }



    @Override
    public void houghPostPerformed(Exp exp, HoughPostOps op, HoughMap houghMap) {
        saveMap(exp, op, houghMap);
    }



    @Override
    public void houghPrePerformed(Exp exp, HoughPreOps op, ByteMap patternMap) {
        saveMap(exp, op, patternMap);

    }



    @Override
    public void houghResultsPerformed(Exp exp, HoughResultsOps op,
            OpResult result) {

    }



    @Override
    public void positioningOpPerformed(Exp exp, PositioningOp op,
            HoughPeak[] peaks) {
        HoughMap houghMap = exp.getSourceHoughMap();
        ExpListenerUtil.drawHoughPeaksOverlay(houghMap, peaks);
        saveMap(exp, op, houghMap);
    }



    @Override
    public void positioningPostPerformed(Exp exp, PositioningPostOps op,
            HoughPeak[] peaks) {
        HoughMap houghMap = exp.getSourceHoughMap();
        ExpListenerUtil.drawHoughPeaksOverlay(houghMap, peaks);
        saveMap(exp, op, houghMap);
    }



    @Override
    public void positioningPrePerformed(Exp exp, PositioningPreOps op,
            BinMap peaksMap) {
        saveMap(exp, op, peaksMap);
    }



    @Override
    public void positioningResultsPerformed(Exp exp, PositioningResultsOps op,
            OpResult result) {
    }



    @Override
    public void indexingOpPerformed(Exp exp, IndexingOp op, Solution[] solutions) {
        // Arrays.sort(solutions, new SolutionFitComparator(), true);
        // ByteMap slnMap = exp.getSourcePatternMap();

        // FIXME: Draw solution overlay when simulated patterns are fixed
        // ExpListenerUtil.drawSolutionOverlay(slnMap, exp.getMetadata().camera,
        // exp.getMetadata().beamEnergy, solutions[0]);
        // saveMap(exp, op, slnMap);
    }



    @Override
    public void indexingPostPerformed(Exp exp, IndexingPostOps op,
            Solution[] solutions) {
        // Arrays.sort(solutions, new SolutionFitComparator(), true);
        // ByteMap slnMap = exp.getSourcePatternMap();

        // FIXME: Draw solution overlay when simulated patterns are fixed
        // ExpListenerUtil.drawSolutionOverlay(slnMap, exp.getMetadata().camera,
        // exp.getMetadata().beamEnergy, solutions[0]);
        // saveMap(exp, op, slnMap);
    }



    @Override
    public void indexingPrePerformed(Exp exp, IndexingPreOps op,
            HoughPeak[] peaks) {
        HoughMap houghMap = exp.getSourceHoughMap();
        ExpListenerUtil.drawHoughPeaksOverlay(houghMap, peaks);
        saveMap(exp, op, houghMap);
    }



    @Override
    public void indexingResultsPerformed(Exp exp, IndexingResultsOps op,
            OpResult result) {
    }



    @Override
    public void patternOpPerformed(Exp exp, PatternOp op, ByteMap patternMap) {
        saveMap(exp, op, patternMap);
    }



    @Override
    public void patternPostPerformed(Exp exp, PatternPostOps op,
            ByteMap patternMap) {
        saveMap(exp, op, patternMap);
    }



    @Override
    public void patternResultsPerformed(Exp exp, PatternResultsOps op,
            OpResult result) {
    }



    /**
     * Save a map. The map is saved in the experiment's path. The name of the
     * map is taken from the name of the <code>Operation</code>.
     * 
     * @param exp
     *            current experiment
     * @param op
     *            operation that creates the map
     * @param map
     *            a map
     */
    protected void saveMap(Exp exp, ExpOperation op, Map map) {
        map.setDir(exp.getDir());
        map.setName(createName(exp, op.getName()));

        try {
            IO.save(map);
        } catch (IOException ex) {
            logger.warning(op.getName() + " map could not be "
                    + "saved because: " + ex.getMessage());
        }

        logger.info(op.getName() + " saved at " + map.getFile().getPath());
    }

}
