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
package org.ebsdimage.gui.exp;

import org.ebsdimage.core.HoughMap;
import org.ebsdimage.core.HoughPeak;
import org.ebsdimage.core.Solution;
import org.ebsdimage.core.exp.*;
import org.ebsdimage.core.exp.ops.detection.op.DetectionOp;
import org.ebsdimage.core.exp.ops.detection.post.DetectionPostOps;
import org.ebsdimage.core.exp.ops.detection.pre.DetectionPreOps;
import org.ebsdimage.core.exp.ops.detection.results.DetectionResultsOps;
import org.ebsdimage.core.exp.ops.hough.op.HoughOp;
import org.ebsdimage.core.exp.ops.hough.post.HoughPostOps;
import org.ebsdimage.core.exp.ops.hough.pre.HoughPreOps;
import org.ebsdimage.core.exp.ops.hough.results.HoughResultsOps;
import org.ebsdimage.core.exp.ops.identification.op.IdentificationOp;
import org.ebsdimage.core.exp.ops.identification.post.IdentificationPostOps;
import org.ebsdimage.core.exp.ops.identification.pre.IdentificationPreOps;
import org.ebsdimage.core.exp.ops.identification.results.IdentificationResultsOps;
import org.ebsdimage.core.exp.ops.indexing.op.IndexingOp;
import org.ebsdimage.core.exp.ops.indexing.post.IndexingPostOps;
import org.ebsdimage.core.exp.ops.indexing.pre.IndexingPreOps;
import org.ebsdimage.core.exp.ops.indexing.results.IndexingResultsOps;
import org.ebsdimage.core.exp.ops.pattern.op.PatternOp;
import org.ebsdimage.core.exp.ops.pattern.post.PatternPostOps;
import org.ebsdimage.core.exp.ops.pattern.results.PatternResultsOps;
import org.simpleframework.xml.Root;

import rmlimage.RMLImage;
import rmlimage.core.BinMap;
import rmlimage.core.ByteMap;
import rmlimage.core.Map;

/**
 * Listener to display the maps of an experiment in the graphical interface.
 * 
 * @author Philippe T. Pinard
 */
@Root
public class MapsGUIListener implements ExpListener {

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
        return name + "_" + exp.getCurrentIndex();
    }



    @Override
    public void detectionOpPerformed(Exp exp, DetectionOp op, BinMap peaksMap) {
        showMap(exp, op, peaksMap);
    }



    @Override
    public void detectionPostPerformed(Exp exp, DetectionPostOps op,
            BinMap peaksMap) {
        showMap(exp, op, peaksMap);
    }



    @Override
    public void detectionPrePerformed(Exp exp, DetectionPreOps op,
            HoughMap houghMap) {
        showMap(exp, op, houghMap);
    }



    @Override
    public void detectionResultsPerformed(Exp exp, DetectionResultsOps op,
            OpResult result) {
    }



    @Override
    public void houghOpPerformed(Exp exp, HoughOp op, HoughMap houghMap) {
        showMap(exp, op, houghMap);
    }



    @Override
    public void houghPostPerformed(Exp exp, HoughPostOps op, HoughMap houghMap) {
        showMap(exp, op, houghMap);
    }



    @Override
    public void houghPrePerformed(Exp exp, HoughPreOps op, ByteMap patternMap) {
        showMap(exp, op, patternMap);
    }



    @Override
    public void houghResultsPerformed(Exp exp, HoughResultsOps op,
            OpResult result) {
    }



    @Override
    public void identificationOpPerformed(Exp exp, IdentificationOp op,
            HoughPeak[] peaks) {
        HoughMap houghMap = exp.getSourceHoughMap();
        ExpListenerUtil.drawHoughPeaksOverlay(houghMap, peaks);
        showMap(exp, op, houghMap);
    }



    @Override
    public void identificationPostPerformed(Exp exp, IdentificationPostOps op,
            HoughPeak[] peaks) {
        HoughMap houghMap = exp.getSourceHoughMap();
        ExpListenerUtil.drawHoughPeaksOverlay(houghMap, peaks);
        showMap(exp, op, houghMap);
    }



    @Override
    public void identificationPrePerformed(Exp exp, IdentificationPreOps op,
            BinMap peaksMap) {
        showMap(exp, op, peaksMap);
    }



    @Override
    public void identificationResultsPerformed(Exp exp,
            IdentificationResultsOps op, OpResult result) {
    }



    @Override
    public void indexingOpPerformed(Exp exp, IndexingOp op, Solution[] solutions) {
        // FIXME: Draw solution overlay when simulated patterns are fixed
        // Arrays.sort(solutions, new SolutionFitComparator(), true);
        // ByteMap slnMap = exp.getSourcePatternMap();
        //
        // ExpListenerUtil.drawSolutionOverlay(slnMap, exp.getMetadata().camera,
        // exp.getMetadata().beamEnergy, solutions[0]);
        // showMap(exp, op, slnMap);
    }



    @Override
    public void indexingPostPerformed(Exp exp, IndexingPostOps op,
            Solution[] solutions) {
        // FIXME: Draw solution overlay when simulated patterns are fixed
        // Arrays.sort(solutions, new SolutionFitComparator(), true);
        // ByteMap slnMap = exp.getSourcePatternMap();
        //
        // ExpListenerUtil.drawSolutionOverlay(slnMap, exp.getMetadata().camera,
        // exp.getMetadata().beamEnergy, solutions[0]);
        // showMap(exp, op, slnMap);
    }



    @Override
    public void indexingPrePerformed(Exp exp, IndexingPreOps op,
            HoughPeak[] peaks) {
        HoughMap houghMap = exp.getSourceHoughMap();
        ExpListenerUtil.drawHoughPeaksOverlay(houghMap, peaks);
        showMap(exp, op, houghMap);
    }



    @Override
    public void indexingResultsPerformed(Exp exp, IndexingResultsOps op,
            OpResult result) {
    }



    @Override
    public void patternOpPerformed(Exp exp, PatternOp op, ByteMap patternMap) {
        showMap(exp, op, patternMap);
    }



    @Override
    public void patternPostPerformed(Exp exp, PatternPostOps op,
            ByteMap patternMap) {
        showMap(exp, op, patternMap);
    }



    @Override
    public void patternResultsPerformed(Exp exp, PatternResultsOps op,
            OpResult result) {
    }



    /**
     * Shows a map in the graphical interface.
     * 
     * @param exp
     *            current experiment
     * @param op
     *            operation that created the map
     * @param map
     *            map to display
     */
    protected void showMap(Exp exp, ExpOperation op, Map map) {
        map.setName(createName(exp, op.getName()));
        map.shouldSave(false);
        RMLImage.getDesktop().add(map);
    }
}
