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

import rmlimage.core.BinMap;
import rmlimage.core.ByteMap;

/**
 * Listener on the experiment after each category of operations is performed.
 * 
 * @author ppinard
 */
@Root
public interface ExpListener {

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
     * Action fired after an identification pre-operation is performed.
     * 
     * @param exp
     *            experiment calling this method
     * @param op
     *            operation performed
     * @param peaksMap
     *            resultant peaks map
     */
    public void identificationPrePerformed(Exp exp, IdentificationPreOps op,
            BinMap peaksMap);



    /**
     * Action fired after the identification operation is performed.
     * 
     * @param exp
     *            experiment calling this method
     * @param op
     *            operation performed
     * @param peaks
     *            resultant peaks
     */
    public void identificationOpPerformed(Exp exp, IdentificationOp op,
            HoughPeak[] peaks);



    /**
     * Action fired after an identification post-operation is performed.
     * 
     * @param exp
     *            experiment calling this method
     * @param op
     *            operation performed
     * @param peaks
     *            resultant peaks
     */
    public void identificationPostPerformed(Exp exp, IdentificationPostOps op,
            HoughPeak[] peaks);



    /**
     * Action fired after the calculation of an identification result.
     * 
     * @param exp
     *            experiment calling this method
     * @param op
     *            operation performed
     * @param result
     *            result of the operation
     */
    public void identificationResultsPerformed(Exp exp,
            IdentificationResultsOps op, OpResult result);



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
}