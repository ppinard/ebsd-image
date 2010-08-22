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
package org.ebsdimage.core.sim.ops.output;

import java.io.File;
import java.io.IOException;

import org.ebsdimage.core.HoughMath;
import org.ebsdimage.core.HoughPeak;
import org.ebsdimage.core.sim.Band;
import org.ebsdimage.core.sim.Sim;
import org.ebsdimage.core.sim.ops.patternsim.PatternSimOp;
import org.ebsdimage.io.HoughPeakXmlSaver;
import org.ebsdimage.io.HoughPeakXmlTags;
import org.jdom.Element;

import ptpshared.utility.xml.JDomUtil;

/**
 * Operation to save the Hough peaks of the simulated pattern. These theoretical
 * Hough peaks are calculated from the bands of the simulated pattern.
 * 
 * @author Philippe T. Pinard
 */
public class HoughPeaksXmlFile extends OutputOps {

    /**
     * Saves Hough peaks from the simulated pattern in an XML file.
     * 
     * @param sim
     *            simulation executing this method
     * @param patternSimOp
     *            pattern simulation operation
     * @throws IOException
     *             if an error occurs while saving
     */
    @Override
    public void save(Sim sim, PatternSimOp patternSimOp) throws IOException {
        Element element = new Element(HoughPeakXmlTags.TAG_NAME + "s");

        // Convert pattern's bands to Hough peaks
        Band[] bands = patternSimOp.getBands();
        HoughPeak[] peaks = HoughMath.bandsToHoughPeaks(bands);

        // Save Hough peaks
        for (HoughPeak peak : peaks)
            element.addContent(new HoughPeakXmlSaver().save(peak));

        // Save element
        File file =
                new File(sim.getDir(), sim.getName() + "_"
                        + sim.getCurrentIndex() + ".xml");
        JDomUtil.saveXML(element, file);
    }

}
