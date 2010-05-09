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
package org.ebsdimage.core.exp.ops.identification.results;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.ebsdimage.core.HoughMath;
import org.ebsdimage.core.HoughPeak;
import org.ebsdimage.core.exp.Exp;
import org.ebsdimage.core.exp.OpResult;
import org.ebsdimage.io.HoughPeakXmlLoader;
import org.ebsdimage.io.HoughPeakXmlTags;
import org.jdom.Element;

import ptpshared.utility.xml.JDomUtil;
import rmlimage.module.real.core.RealMap;

/**
 * Operation to calculate the fit between the calculated and detected bands.
 * 
 * @author Philippe T. Pinard
 */
public class Fit extends IdentificationResultsOps {

    @Override
    public String toString() {
        return "Fit [filepath=" + filepath + "]";
    }



    /** File path of the XML file containing the theoretical Hough peaks. */
    public final File filepath;



    /**
     * Creates a new <code>Fit</code> operation with an empty file path.
     */
    public Fit() {
        filepath = new File("");
    }



    /**
     * Creates a new <code>Fit</code> operation with the specified file path.
     * 
     * @param filepath
     *            file path of the theoretical Hough peaks
     */
    public Fit(File filepath) {
        if (filepath == null)
            throw new NullPointerException("Filepath cannot be null.");

        this.filepath = filepath;
    }



    /**
     * Calculates the fit between the experimental and theoretical Hough peaks.
     * The theoretical Hough peaks are loaded from the XML file given in the
     * constructor.
     * 
     * @param exp
     *            experiment executing this method
     * @param peaks
     *            experimental Hough peaks
     * @return one entry with the fit value
     * 
     * @see HoughMath#fit(HoughPeak[], HoughPeak[])
     */
    @Override
    public OpResult[] calculate(Exp exp, HoughPeak[] peaks) {
        // Load theoretical peaks
        Element root;
        try {
            root = JDomUtil.loadXML(filepath).getRootElement();
        } catch (IOException e) {
            throw new RuntimeException("Could not load xml file ("
                    + filepath.getAbsolutePath() + ") because of "
                    + e.getMessage());
        }

        String houghPeaksXmlTag = HoughPeakXmlTags.TAG_NAME + "s";
        if (!JDomUtil.hasChild(root, houghPeaksXmlTag))
            throw new IllegalArgumentException(
                    "No Hough Peaks found in the xml file ("
                            + filepath.getAbsolutePath() + ")");

        Element element = JDomUtil.getChild(root, houghPeaksXmlTag);

        List<?> children = element.getChildren();
        HoughPeak[] theoPeaks = new HoughPeak[children.size()];
        for (int i = 0; i < children.size(); i++) {
            theoPeaks[i] =
                    new HoughPeakXmlLoader().load((Element) children.get(i));
        }

        // Calculate fit
        OpResult result =
                new OpResult(getName(), HoughMath.fit(peaks, theoPeaks),
                        RealMap.class);

        return new OpResult[] { result };
    }

}
