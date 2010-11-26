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
 * @author ppinard
 */
public class PeaksHoughMap extends IdentificationResultsOps {

    /** Default operation. */
    public static final PeaksHoughMap DEFAULT = new PeaksHoughMap(-1);

    /** Number of peaks to save. The most intense peaks are selected. */
    @Attribute(name = "count")
    public final int count;



    /**
     * Creates a new <code>PeaksHoughMap</code> operation. The identified peaks
     * are represented by a pixel in a Hough map. Before the threshold, the
     * peaks are sorted so that only the most intense peaks are selected. If the
     * specified value is -1, all the identified peaks will appear in the Hough
     * map.
     * 
     * @param count
     *            number of peaks to save
     */
    public PeaksHoughMap(@Attribute(name = "count") int count) {
        if (count < -1)
            throw new IllegalArgumentException(
                    "The number of peaks must be greater or equal to zero, or equal to -1.");

        this.count = count;
    }



    @Override
    public String toString() {
        return "Peaks HoughMap [count=" + count + "]";
    }



    @Override
    public OpResult[] calculate(Exp exp, HoughPeak[] srcPeaks) {
        HoughPeak[] peaks = srcPeaks.clone();

        HoughMap houghMap = exp.getSourceHoughMap().duplicate();
        houghMap.clear();

        File file =
                new File(exp.getDir(), exp.getName() + "_"
                        + exp.getCurrentIndex() + ".bmp");

        try {
            create(houghMap, peaks, file);
        } catch (IOException e) {
        }

        return new OpResult[0];
    }



    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + count;
        return result;
    }



    @Override
    public boolean equals(Object obj, double precision) {
        if (!super.equals(obj, precision))
            return false;

        PeaksHoughMap other = (PeaksHoughMap) obj;
        if (Math.abs(count - other.count) >= precision)
            return false;

        return true;
    }



    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj))
            return false;

        PeaksHoughMap other = (PeaksHoughMap) obj;
        if (count != other.count)
            return false;

        return true;
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
        if (count < 0)
            thresholdIntensity = peaks[peaks.length - 1].intensity;
        else if (count >= peaks.length)
            thresholdIntensity = peaks[peaks.length - 1].intensity;
        else
            thresholdIntensity = peaks[count - 1].intensity;

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
            if (rho > map.rMax || rho < map.rMin)
                continue;
            if (theta > map.thetaMax || theta < map.thetaMin)
                continue;

            // Get index of the peak and assign a white pixel
            index = map.getIndex(rho, theta);
            map.pixArray[index] = (byte) 255;
        }

        map.setFile(file);
        new HoughMapSaver().save(map);
    }
}
