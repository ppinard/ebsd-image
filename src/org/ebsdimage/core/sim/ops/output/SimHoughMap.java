package org.ebsdimage.core.sim.ops.output;

import java.io.File;
import java.io.IOException;

import magnitude.core.Magnitude;

import org.ebsdimage.core.HoughMap;
import org.ebsdimage.core.HoughPeak;
import org.ebsdimage.core.HoughPeakIntensityComparator;
import org.ebsdimage.core.sim.Band;
import org.ebsdimage.core.sim.Sim;
import org.ebsdimage.core.sim.ops.patternsim.PatternSimOp;
import org.ebsdimage.io.HoughMapSaver;

import ptpshared.utility.Arrays;
import static java.lang.Math.ceil;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

/**
 * Operation to save simulated peaks in a Hough map. Each peak is represented by
 * a white pixel in the Hough map.
 * 
 * @author ppinard
 */
public class SimHoughMap extends OutputOps {

    /** Default operation. */
    public static final SimHoughMap DEFAULT = new SimHoughMap(1344, 1024,
            Math.toRadians(1.0), 1.0, 5);

    /** Theta resolution of the Hough map. */
    public final double deltaTheta;

    /** Rho resolution of the Hough map. */
    public final double deltaRho;

    /** Width of the original diffraction pattern. */
    public final int width;

    /** Height of the original diffraction pattern. */
    public final int height;

    /** Number of peaks to save. */
    public final int count;



    /**
     * Creates a new <code>SimHoughMap</code> operation.
     * 
     * @param width
     *            width of the original diffraction pattern
     * @param height
     *            height of the original diffraction pattern
     * @param deltaTheta
     *            theta resolution of the Hough map
     * @param deltaRho
     *            rho resolution of the Hough map
     * @param count
     *            number of peaks to save
     */
    public SimHoughMap(int width, int height, double deltaTheta,
            double deltaRho, int count) {
        this.width = width;
        this.height = height;
        this.deltaTheta = deltaTheta;
        this.deltaRho = deltaRho;
        this.count = count;
    }



    @Override
    public void save(Sim sim, PatternSimOp patternSimOp) throws IOException {
        // Convert pattern's bands to Hough peaks
        Band[] bands = patternSimOp.getBands();

        // TODO: Fix when calibration is fully implemented
        // HoughPeak[] peaks = HoughMath.bandsToHoughPeaks(bands);
        HoughPeak[] peaks = new HoughPeak[0];

        Arrays.sort(peaks, new HoughPeakIntensityComparator(), true);

        double rhoMax = ceil(sqrt(pow(width, 2) + pow(height, 2)) / 2);
        HoughMap houghMap = new HoughMap(deltaTheta, deltaRho, rhoMax);

        double thresholdIntensity = peaks[count - 1].intensity;

        int index;
        Magnitude rho;
        Magnitude theta;
        for (HoughPeak peak : peaks) {
            rho = peak.rho;
            theta = peak.theta;

            if (peak.intensity < thresholdIntensity)
                break;

            if (rho.compareTo(houghMap.getRhoMax()) > 0
                    || rho.compareTo(houghMap.getRhoMin()) < 0)
                continue;
            if (theta.compareTo(houghMap.getThetaMax()) > 0
                    || theta.compareTo(houghMap.getThetaMin()) < 0)
                continue;

            index = houghMap.getIndex(rho, theta);

            houghMap.pixArray[index] = (byte) 255;
        }

        houghMap.setFile(new File(sim.getDir(), sim.getName() + "_houghmap_"
                + +sim.getCurrentIndex() + ".bmp"));
        new HoughMapSaver().save(houghMap);
    }
}
