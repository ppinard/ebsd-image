package org.ebsdimage.core.sim.ops.output;

import java.io.File;
import java.io.IOException;

import magnitude.core.Magnitude;

import org.ebsdimage.core.HoughMap;
import org.ebsdimage.core.HoughPeak;
import org.ebsdimage.core.HoughPeakIntensityComparator;
import org.ebsdimage.core.sim.Sim;
import org.ebsdimage.core.sim.ops.patternsim.PatternSimOp;
import org.ebsdimage.io.HoughMapSaver;

import ptpshared.util.Arrays;

/**
 * Operation to save simulated peaks in a Hough map. Each peak is represented by
 * a white pixel in the Hough map.
 * 
 * @author ppinard
 */
public class SimHoughMap extends OutputOps {

    /** Default operation. */
    public static final SimHoughMap DEFAULT = new SimHoughMap(
            Math.toRadians(1.0), 1.0, 5);

    /** Theta resolution of the Hough map (in radians). */
    public final double deltaTheta;

    /** Rho resolution of the Hough map. */
    public final double deltaRho;

    /** Number of peaks to save. */
    public final int count;



    /**
     * Creates a new <code>SimHoughMap</code> operation.
     * 
     * @param deltaTheta
     *            theta resolution of the Hough map (in radians)
     * @param deltaRho
     *            rho resolution of the Hough map
     * @param count
     *            number of peaks to save
     */
    public SimHoughMap(double deltaTheta, double deltaRho, int count) {
        this.deltaTheta = deltaTheta;
        this.deltaRho = deltaRho;
        this.count = count;
    }



    @Override
    public void save(Sim sim, PatternSimOp patternSimOp) throws IOException {
        HoughPeak[] peaks = bandsToHoughPeaks(patternSimOp);
        Arrays.sort(peaks, new HoughPeakIntensityComparator(), true);

        // Create a HoughMap
        Magnitude dx = patternSimOp.getPatternMap().getCalibration().getDX();

        Magnitude a = dx.multiply(patternSimOp.width);
        Magnitude b = dx.multiply(patternSimOp.height);
        // c = sqrt(a^2 + b^2)
        Magnitude c = (a.power(2).add(b.power(2))).power(0.5);

        Magnitude rMax = magnitude.core.Math.ceil(c.div(2));

        Magnitude dTheta = new Magnitude(deltaTheta, "rad");
        Magnitude dRho = dx.multiply(deltaRho);
        HoughMap houghMap = new HoughMap(dTheta, dRho, rMax);

        // Add pixels for peak in HoughMap
        double thresholdIntensity = peaks[count - 1].intensity;

        double rho;
        double theta;
        for (HoughPeak peak : peaks) {
            rho = peak.rho;
            theta = peak.theta;

            if (peak.intensity < thresholdIntensity)
                break;

            if (rho > houghMap.getRhoMax() || rho < houghMap.getRhoMin())
                continue;
            if (theta > houghMap.getThetaMax()
                    || theta < houghMap.getThetaMin())
                continue;

            houghMap.setPixValue(houghMap.getIndex(theta, rho), 255);
        }

        houghMap.setFile(new File(sim.getDir(), sim.getName() + "_houghmap_"
                + +sim.getCurrentIndex() + ".bmp"));
        new HoughMapSaver().save(houghMap);
    }
}
