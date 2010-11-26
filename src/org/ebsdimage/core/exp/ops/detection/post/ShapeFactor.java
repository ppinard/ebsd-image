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
package org.ebsdimage.core.exp.ops.detection.post;

import static java.lang.Math.abs;

import java.util.ArrayList;

import org.ebsdimage.core.exp.Exp;
import org.simpleframework.xml.Attribute;

import rmlimage.core.*;

/**
 * Operation to remove peaks which have an aspect ratio greater than a critical
 * aspect ratio.
 * 
 * @author Philippe T. Pinard
 */
public class ShapeFactor extends DetectionPostOps {

    /** Critical aspect ratio of the peaks. */
    @Attribute(name = "aspectRatio")
    public final double aspectRatio;

    /** Default operation. */
    public static final ShapeFactor DEFAULT = new ShapeFactor(2.0);



    /**
     * Creates a new <code>ShapeFactor</code> operation.
     * 
     * @param aspectRatio
     *            critical aspect ratio of the peaks
     */
    public ShapeFactor(@Attribute(name = "aspectRatio") double aspectRatio) {
        if (aspectRatio < 1.0)
            throw new IllegalArgumentException(
                    "Aspect ratio must be greater or equal to 1.0.");

        this.aspectRatio = aspectRatio;
    }



    @Override
    public boolean equals(Object obj, double precision) {
        if (!super.equals(obj, precision))
            return false;

        ShapeFactor other = (ShapeFactor) obj;
        if (abs(aspectRatio - other.aspectRatio) >= precision)
            return false;

        return true;
    }



    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj))
            return false;

        ShapeFactor other = (ShapeFactor) obj;
        if (Double.doubleToLongBits(aspectRatio) != Double.doubleToLongBits(other.aspectRatio))
            return false;

        return true;
    }



    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        long temp;
        temp = Double.doubleToLongBits(aspectRatio);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        return result;
    }



    /**
     * Process the peaks map to remove peaks with a greater aspect ratio than
     * the specified critical one.
     * 
     * @param exp
     *            experiment executing this method
     * @param srcMap
     *            peaks map
     * @return result of operation
     */
    @Override
    public BinMap process(Exp exp, BinMap srcMap) {
        IdentMap identMap = Identification.identify(srcMap);

        Feret ferets = Analysis.getFeret(identMap);

        double aspectRatio;
        ArrayList<Integer> keepObjects = new ArrayList<Integer>();
        for (int i = 0; i < identMap.getObjectCount(); i++) {
            aspectRatio = ferets.max[i] / ferets.min[i];

            if (aspectRatio <= this.aspectRatio)
                keepObjects.add(i + 1);
        }

        Identification.keepObjects(identMap,
                keepObjects.toArray(new Integer[0]));

        return Conversion.toBinMap(identMap);
    }



    @Override
    public String toString() {
        return "ShapeFactor [aspectRatio=" + aspectRatio + "]";
    }
}
