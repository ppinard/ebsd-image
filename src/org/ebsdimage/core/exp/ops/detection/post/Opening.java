package org.ebsdimage.core.exp.ops.detection.post;

import org.ebsdimage.core.exp.Exp;

import rmlimage.core.BinMap;
import rmlimage.core.MathMorph;

/**
 * Operation to remove small false peaks in the peaks map.
 * 
 * @author ppinard
 * 
 */
public class Opening extends DetectionPostOps {

    @Override
    public BinMap process(Exp exp, BinMap srcMap) {
        BinMap destMap = srcMap.duplicate();

        MathMorph.opening(destMap, 2, 8);

        return destMap;
    }



    @Override
    public String toString() {
        return "Opening";
    }

}
