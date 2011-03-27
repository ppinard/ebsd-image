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

/**
 * Listener on the experiment after each category of operations is performed.
 * 
 * @author Philippe T. Pinard
 */
@Root
public interface ExpListener {

    /**
     * Action fired after the detection operation is performed.
     * 
     * @param exp
     *            experiment calling this method
     * @param op
     *            operation performed
     * @param peaksMap
     *            resultant peaks map
     */
    public void detectionOpPerformed(Exp exp, DetectionOp op, BinMap peaksMap);



    /**
     * Action fired after a detection post-operation is performed.
     * 
     * @param exp
     *            experiment calling this method
     * @param op
     *            operation performed
     * @param peaksMap
     *            resultant peaks map
     */
    public void detectionPostPerformed(Exp exp, DetectionPostOps op,
            BinMap peaksMap);



    /**
     * Action fired after a detection pre-operation is performed.
     * 
     * @param exp
     *            experiment calling this method
     * @param op
     *            operation performed
     * @param houghMap
     *            resultant Hough map
     */
    public void detectionPrePerformed(Exp exp, DetectionPreOps op,
            HoughMap houghMap);



    /**
     * Action fired after the calculation of a detection result.
     * 
     * @param exp
     *            experiment calling this method
     * @param op
     *            operation performed
     * @param result
     *            result of the operation
     */
    public void detectionResultsPerformed(Exp exp, DetectionResultsOps op,
            OpResult result);



    /**
     * Action fired after the Hough operation is performed.
     * 
     * @param exp
     *            experiment calling this method
     * @param op
     *            operation performed
     * @param houghMap
     *            resultant Hough map
     */
    public void houghOpPerformed(Exp exp, HoughOp op, HoughMap houghMap);



    /**
     * Action fired after a Hough post-operation is performed.
     * 
     * @param exp
     *            experiment calling this method
     * @param op
     *            operation performed
     * @param houghMap
     *            resultant Hough map
     */
    public void houghPostPerformed(Exp exp, HoughPostOps op, HoughMap houghMap);



    /**
     * Action fired after a Hough pre-operation is performed.
     * 
     * @param exp
     *            experiment calling this method
     * @param op
     *            operation performed
     * @param patternMap
     *            resultant pattern map
     */
    public void houghPrePerformed(Exp exp, HoughPreOps op, ByteMap patternMap);



    /**
     * Action fired after the calculation of a Hough result.
     * 
     * @param exp
     *            experiment calling this method
     * @param op
     *            operation performed
     * @param result
     *            result of the operation
     */
    public void houghResultsPerformed(Exp exp, HoughResultsOps op,
            OpResult result);



    /**
     * Action fired after the positioning operation is performed.
     * 
     * @param exp
     *            experiment calling this method
     * @param op
     *            operation performed
     * @param peaks
     *            resultant peaks
     */
    public void positioningOpPerformed(Exp exp, PositioningOp op,
            HoughPeak[] peaks);



    /**
     * Action fired after an positioning post-operation is performed.
     * 
     * @param exp
     *            experiment calling this method
     * @param op
     *            operation performed
     * @param peaks
     *            resultant peaks
     */
    public void positioningPostPerformed(Exp exp, PositioningPostOps op,
            HoughPeak[] peaks);



    /**
     * Action fired after an positioning pre-operation is performed.
     * 
     * @param exp
     *            experiment calling this method
     * @param op
     *            operation performed
     * @param peaksMap
     *            resultant peaks map
     */
    public void positioningPrePerformed(Exp exp, PositioningPreOps op,
            BinMap peaksMap);



    /**
     * Action fired after the calculation of an positioning result.
     * 
     * @param exp
     *            experiment calling this method
     * @param op
     *            operation performed
     * @param result
     *            result of the operation
     */
    public void positioningResultsPerformed(Exp exp, PositioningResultsOps op,
            OpResult result);



    /**
     * Action fired after the indexing operation is performed.
     * 
     * @param exp
     *            experiment calling this method
     * @param op
     *            operation performed
     * @param solutions
     *            resultant solutions
     */
    public void indexingOpPerformed(Exp exp, IndexingOp op, Solution[] solutions);



    /**
     * Action fired after an indexing post-operation is performed.
     * 
     * @param exp
     *            experiment calling this method
     * @param op
     *            operation performed
     * @param solutions
     *            resultant solutions
     */
    public void indexingPostPerformed(Exp exp, IndexingPostOps op,
            Solution[] solutions);



    /**
     * Action fired after an indexing pre-operation is performed.
     * 
     * @param exp
     *            experiment calling this method
     * @param op
     *            operation performed
     * @param peaks
     *            resultant peaks
     */
    public void indexingPrePerformed(Exp exp, IndexingPreOps op,
            HoughPeak[] peaks);



    /**
     * Action fired after the calculation of an indexing result.
     * 
     * @param exp
     *            experiment calling this method
     * @param op
     *            operation performed
     * @param result
     *            result of the operation
     */
    public void indexingResultsPerformed(Exp exp, IndexingResultsOps op,
            OpResult result);



    /**
     * Action fired after the pattern operation is performed.
     * 
     * @param exp
     *            experiment calling this method
     * @param op
     *            operation performed
     * @param patternMap
     *            resultant pattern map
     */
    public void patternOpPerformed(Exp exp, PatternOp op, ByteMap patternMap);



    /**
     * Action fired after a pattern post-operation is performed.
     * 
     * @param exp
     *            experiment calling this method
     * @param op
     *            operation performed
     * @param patternMap
     *            resultant pattern map
     */
    public void patternPostPerformed(Exp exp, PatternPostOps op,
            ByteMap patternMap);



    /**
     * Action fired after the calculation of a pattern result.
     * 
     * @param exp
     *            experiment calling this method
     * @param op
     *            operation performed
     * @param result
     *            result of the operation
     */
    public void patternResultsPerformed(Exp exp, PatternResultsOps op,
            OpResult result);
}
