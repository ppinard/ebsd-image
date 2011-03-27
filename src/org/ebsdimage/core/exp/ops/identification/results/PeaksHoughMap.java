package org.ebsdimage.core.exp.ops.identification.results;

import java.io.File;
import java.io.IOException;

import org.ebsdimage.core.HoughMap;
import org.ebsdimage.core.HoughPeak;
import org.ebsdimage.core.exp.Exp;
import org.ebsdimage.core.exp.OpResult;
import org.ebsdimage.io.HoughMapSaver;
import org.simpleframework.xml.Attribute;

/**
 * Saves the identified peaks as a Hough map where each peak is represented by a
 * pixel. The size and resolution of the original Hough map of the experiment is
 * used as the canvas.
 * 
 * @author Philippe T. Pinard
 */
public class PeaksHoughMap extends IdentificationResultsOps {

    /** Default operation. */
    public static final PeaksHoughMap DEFAULT = new PeaksHoughMap(-1);

    /** Maximum number of peaks to save. The most intense peaks are selected. */
    @Attribute(name = "max")
    public final int max;



    /**
     * Creates a new <code>PeaksHoughMap</code> operation. The identified peaks
     * are represented by a pixel in a Hough map. Before the threshold, the
     * peaks are sorted so that only the most intense peaks are selected. If the
     * specified value is -1, all the identified peaks will appear in the Hough
     * map.
     * 
     * @param max
     *            number of peaks to save
     */
    public PeaksHoughMap(@Attribute(name = "max") int max) {
        if (max < -1)
            throw new IllegalArgumentException(
                    "The number of peaks must be greater or equal to zero, "
                            + "or equal to -1.");

        this.max = max;
    }



    @Override
    public OpResult[] calculate(Exp exp, HoughPeak[] srcPeaks) {
        HoughPeak[] peaks = srcPeaks.clone();

        HoughMap houghMap = exp.getSourceHoughMap().duplicate();
        houghMap.clear();

        File file =
                new File(exp.getDir(), exp.getName() + "_"
                        + exp.getCurrentIndex() + "_peaks_houghmap.bmp");

        try {
            create(houghMap, peaks, file);
        } catch (IOException e) {
        }

        return new OpResult[0];
    }



    /**
     * Creates the Hough map from the specified peaks and saves it.
     * 
     * @param map
     *            empty Hough map
     * @param peaks
     *            array of peaks
     * @param file
     *            file where to save the Hough map
     * @throws IOException
     *             if an error occurs while saving the Hough map
     */
    public void create(HoughMap map, HoughPeak[] peaks, File file)
            throws IOException {
        sortDescending(peaks);

        double thresholdIntensity;
        if (max < 0)
            thresholdIntensity = peaks[peaks.length - 1].intensity;
        else if (max >= peaks.length)
            thresholdIntensity = peaks[peaks.length - 1].intensity;
        else
            thresholdIntensity = peaks[max - 1].intensity;

        int index;
        double rho;
        double theta;
        for (HoughPeak peak : peaks) {
            rho = peak.rho;
            theta = peak.theta;

            // If peak intensity is less than the threshold value, exits
            if (peak.intensity < thresholdIntensity)
                break;

            // Check that the peak is located inside the map
            if (rho > map.getRhoMax() || rho < map.getRhoMin())
                continue;
            if (theta > map.getThetaMax() || theta < map.getThetaMin())
                continue;

            // Get index of the peak and assign a white pixel
            index = map.getIndex(theta, rho);
            map.pixArray[index] = (byte) 255;
        }

        map.setFile(file);
        new HoughMapSaver().save(map);
    }



    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj))
            return false;

        PeaksHoughMap other = (PeaksHoughMap) obj;
        if (max != other.max)
            return false;

        return true;
    }



    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + max;
        return result;
    }



    @Override
    public String toString() {
        return "HoughMap [max=" + max + "]";
    }
}
