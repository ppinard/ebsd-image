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
package org.ebsdimage.core.exp.ops.positioning.results;

import java.io.File;
import java.io.IOException;

import org.ebsdimage.core.HoughPeak;
import org.ebsdimage.core.exp.Exp;
import org.ebsdimage.core.exp.OpResult;

import ptpshared.util.simplexml.XmlSaver;

/**
 * Operation to save the identified Hough peaks to a XML file.
 * 
 * @author Philippe T. Pinard
 */
public class PeaksXml extends PositioningResultsOps {

    /** Default operation. */
    public static final PeaksXml DEFAULT = new PeaksXml();



    @Override
    public OpResult[] calculate(Exp exp, HoughPeak[] srcPeaks) {
        HoughPeak[] peaks = srcPeaks.clone();
        File file =
                new File(exp.getDir(), exp.getName() + "_"
                        + exp.getCurrentIndex() + "_peaks.xml");

        try {
            save(peaks, file);
        } catch (IOException e) {
        }

        return new OpResult[0];
    }



    /**
     * Saves the identified peaks to a XML file.
     * 
     * @param peaks
     *            array of Hough peaks to save
     * @param file
     *            location where to save
     * @throws IOException
     *             if an error occurs while saving the file
     */
    public void save(HoughPeak[] peaks, File file) throws IOException {
        sortDescending(peaks);

        // Save XML
        new XmlSaver().saveArray(peaks, file);
    }



    @Override
    public String toString() {
        return "XML";
    }

}
