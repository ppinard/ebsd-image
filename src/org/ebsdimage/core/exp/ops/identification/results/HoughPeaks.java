package org.ebsdimage.core.exp.ops.identification.results;

import java.io.File;
import java.io.IOException;

import org.ebsdimage.core.HoughPeak;
import org.ebsdimage.core.exp.Exp;
import org.ebsdimage.core.exp.OpResult;

import ptpshared.util.xml.XmlSaver;

/**
 * Operation to save the identified Hough peaks to a XML file.
 * 
 * @author ppinard
 */
public class HoughPeaks extends IdentificationResultsOps {

    /** Default operation. */
    public static final HoughPeaks DEFAULT = new HoughPeaks();



    @Override
    public OpResult[] calculate(Exp exp, HoughPeak[] srcPeaks) {
        HoughPeak[] peaks = srcPeaks.clone();
        File file =
                new File(exp.getDir(), exp.getName() + "_"
                        + exp.getCurrentIndex() + ".xml");

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

}
