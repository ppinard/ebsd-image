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
package org.ebsdimage.core;

import static java.lang.Math.abs;
import static ptpshared.core.math.Math.sign;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

import ptpshared.core.math.*;
import rmlshared.math.Stats;
import rmlshared.ui.Monitorable;
import crystallography.core.Calculations;
import crystallography.core.Reflector;
import crystallography.core.Reflectors;

/**
 * Indexing of Hough peaks to find possible solutions.
 * 
 * @author Philippe T. Pinard
 * 
 */
public class Indexing implements Monitorable {

    /**
     * Static method for {@link #doIndex(Reflectors[], HoughPeak[], Camera)}.
     * 
     * @param reflsArray
     *            array containing the reflectors of all the phases to be
     *            evaluated during the indexing
     * @param peaks
     *            array of Hough peaks to be used in the indexing
     * @param calibration
     *            calibration
     * @return array of solution
     * 
     * @throws NullPointerException
     *             if the reflectors array is null
     * @throws NullPointerException
     *             if the Hough peaks array is null
     * @throws NullPointerException
     *             if the calibration is null
     * @throws IllegalArgumentException
     *             if no reflectors (phase) is defined
     * @throws IllegalArgumentException
     *             if there is less than 3 Hough peaks defined
     */
    public static Solution[] index(Reflectors[] reflsArray, HoughPeak[] peaks,
            Camera calibration) {
        return new Indexing().doIndex(reflsArray, peaks, calibration);
    }



    /** Progress value. */
    protected double progress = 0.0;

    /** Progress status. */
    protected String status = "";

    /** Logger. */
    private final Logger logger = Logger.getLogger("ebsd");



    /**
     * Evaluates a solution based on the angular deviation between the
     * experimental normal calculated from a Hough peak and the theoretical
     * normals calculated from the proposed lattice orientation and proposed
     * phase's reflectors.
     * <p/>
     * The value of the angular deviation is inversely related to how good is
     * the solution.
     * 
     * @param exp
     *            experimental normal from a Hough peak
     * @param refls
     *            reflectors from the propose phase
     * @param rotation
     *            proposed lattice orientation
     * @return minimal angular deviation
     */
    private double angularDev(Vector3D exp, Reflectors refls,
            Quaternion rotation) {
        double angularDev = Double.POSITIVE_INFINITY;

        for (Reflector refl : refls) {
            Vector3D u = refl.plane.toVector3D();

            // Positive refl
            double tmpAngularDev =
                    Vector3DMath.angle(exp, QuaternionMath.rotate(u, rotation));

            if (tmpAngularDev < angularDev)
                angularDev = tmpAngularDev;

            // Negative refl
            tmpAngularDev =
                    Vector3DMath.angle(exp, QuaternionMath.rotate(u.negate(),
                            rotation));

            if (tmpAngularDev < angularDev)
                angularDev = tmpAngularDev;
        }

        return angularDev;
    }



    /**
     * Performs indexing with the given array of reflectors (i.e. phases) and
     * Hough peaks. The calibration is defined by the camera. At least 3 Hough
     * peaks are required to perform the indexing.
     * <p/>
     * The returned solutions are unordered.
     * 
     * @param reflsArray
     *            array containing the reflectors of all the phases to be
     *            evaluated during the indexing
     * @param peaks
     *            array of Hough peaks to be used in the indexing
     * @param calibration
     *            calibration
     * @return array of solution
     * 
     * @throws NullPointerException
     *             if the reflectors array is null
     * @throws NullPointerException
     *             if the Hough peaks array is null
     * @throws NullPointerException
     *             if the calibration is null
     * @throws IllegalArgumentException
     *             if no reflectors (phase) is defined
     * @throws IllegalArgumentException
     *             if there is less than 3 Hough peaks defined
     * 
     * @see Reflectors
     * @see HoughPeak
     * @see Camera
     */
    public Solution[] doIndex(Reflectors[] reflsArray, HoughPeak[] peaks,
            Camera calibration) {
        if (reflsArray == null)
            throw new NullPointerException("Reflectors array cannot be null.");
        if (peaks == null)
            throw new NullPointerException("Hough peaks array cannot be null.");
        if (calibration == null)
            throw new NullPointerException("Calibration cannot be null.");

        if (reflsArray.length < 1)
            throw new IllegalArgumentException(
                    "At least one phase must be defined.");
        if (peaks.length < 3)
            throw new IllegalArgumentException(
                    "At least 3 peaks must be defined.");

        // HashMaps to store solutions
        HashMap<String, Solution> solutions = new HashMap<String, Solution>();
        // HashMap<String, Integer> counts = new HashMap<String, Integer>();

        // Calculate Hough peaks pairs (experimental)
        HoughPeakPairs expPairs = new HoughPeakPairs(peaks, calibration);

        // Select most intense Hough peak pair
        HoughPeakPair expPair = expPairs.get(0);
        setStatus("Most intense Hough peak pair: " + expPair.toString());
        setStatus(expPair.normal0.toString());
        setStatus(expPair.normal1.toString());

        // Loop through all the reflectors (i.e. crystals)
        for (Reflectors refls : reflsArray) {
            String crystalName = refls.crystal.name;

            setStatus("Inspecting crystal: " + crystalName);

            // Calculate theoretical pairs
            InterplanarAnglePairs theoPairs = new InterplanarAnglePairs(refls);
            setStatus("..Number of theoretical pairs: " + theoPairs.size());

            // Find the closest matching angles between the two first Hough
            // peaks and the theoretical pairs
            InterplanarAnglePair[] matches =
                    theoPairs.findClosestMatches(expPair.directionCosine, 1e-1);
            setStatus("..Number of matches (theoretical and experimental): "
                    + matches.length);

            // Loop through all the matches
            for (InterplanarAnglePair match : matches) {
                setStatus("....Inspecting match: " + match);

                // Generate all the possible combinations of normals for the
                // match
                setStatus("....Generating all possible combinations "
                        + "of normals for the match");
                InterplanarAnglePair[] possibilities =
                        generatorPossibilities(match);

                // Reduce the amount of possibilities by checking the dot
                // product
                setStatus("....Reduce the amount of possibilities by "
                        + "checking the dot product");
                possibilities = reducePosibilities(possibilities, expPair);

                setStatus("....Number of possibilities to evaluate: "
                        + possibilities.length);

                for (InterplanarAnglePair possibility : possibilities) {
                    setStatus("......Inspecting possibility: " + possibility);

                    Quaternion rotation =
                            latticeOrientation(expPair, possibility);
                    rotation =
                            Calculations.reduce(rotation,
                                    refls.crystal.laueGroup);
                    String key = crystalName + rotation.hashCode(1e-6);
                    if (solutions.containsKey(key))
                        continue;
                    // if (counts.containsKey(key)) {
                    // counts.put(key, counts.get(key) + 1);
                    // continue;
                    // }

                    setStatus(rotation.toAxisAngle().toString());

                    double[] fits = new double[peaks.length];
                    // double mad[] = new double[peaks.length];

                    for (int i = 0; i < peaks.length; i++) {
                        Vector3D normal = expPairs.get(i).normal0;

                        fits[i] = fit(normal, refls, rotation);
                        // mad[i] = angularDev(normal, refls, rotation);
                    }

                    // counts.put(key, 0);
                    solutions.put(key, new Solution(refls.crystal, rotation,
                            Stats.average(fits)));
                }
            }
        }

        // Solution[] tmpArray = new Solution[solutions.size()];
        // int i = 0;
        // for (Entry<String, Solution> entry : solutions.entrySet()) {
        // Solution solution = entry.getValue();
        // int count = counts.get(entry.getKey());
        // tmpArray[i] =
        // new Solution(solution.phase, solution.rotation,
        // solution.fit / (count * count));
        // i++;
        // }

        return solutions.values().toArray(new Solution[0]);
    }



    /**
     * Evaluates the solution based on the fit between the experimental normal
     * calculated from a Hough peak and the theoretical normals calculated from
     * the proposed lattice orientation and proposed phase's reflectors.
     * <p/>
     * The fit is equal to the direction cosine between the experimental and
     * theoretical normal.
     * 
     * @param exp
     *            experimental normal from a Hough peak
     * @param refls
     *            reflectors from the propose phase
     * @param rotation
     *            proposed lattice orientation
     * @return minimal fit value
     */
    private double fit(Vector3D exp, Reflectors refls, Quaternion rotation) {
        double fit = 0.0;

        for (Reflector refl : refls) {
            Vector3D u = refl.plane.toVector3D();

            double tmpFit =
                    abs(Vector3DMath.directionCosine(exp, QuaternionMath
                            .rotate(u, rotation)));

            if (tmpFit > fit)
                fit = tmpFit;
        }

        return fit;
    }



    /**
     * Generates eight possibilities from a matching angle pair. For one given
     * angle, there are eight possible arrangements of the reflectors and
     * normals that all gives the same angle. All these possibilities need to be
     * consider in the indexing.
     * 
     * @param match
     *            an angle pair
     * @return an array of possibilities
     */
    private InterplanarAnglePair[] generatorPossibilities(
            InterplanarAnglePair match) {
        Vector3D normal0 = match.normal0;
        Vector3D normal1 = match.normal1;

        // Eight possibilities
        InterplanarAnglePair[] possibilities = new InterplanarAnglePair[8];

        // refl0 and refl1
        possibilities[0] = match;
        possibilities[1] =
                new InterplanarAnglePair(normal0.negate(), normal1,
                        match.directionCosine);
        possibilities[2] =
                new InterplanarAnglePair(normal0, normal1.negate(),
                        match.directionCosine);
        possibilities[3] =
                new InterplanarAnglePair(normal0.negate(), normal1.negate(),
                        match.directionCosine);

        // normal1 and normal0
        possibilities[4] =
                new InterplanarAnglePair(normal1, normal0,
                        match.directionCosine);
        possibilities[5] =
                new InterplanarAnglePair(normal1.negate(), normal0,
                        match.directionCosine);
        possibilities[6] =
                new InterplanarAnglePair(normal1, normal0.negate(),
                        match.directionCosine);
        possibilities[7] =
                new InterplanarAnglePair(normal1.negate(), normal0.negate(),
                        match.directionCosine);

        return possibilities;
    }



    @Override
    public double getTaskProgress() {
        return progress;
    }



    @Override
    public String getTaskStatus() {
        return status;
    }



    /**
     * Calculates the lattice orientation (i.e. rotation) between the
     * experimental and theoretical angle pairs.
     * 
     * @param expPair
     *            experimental angle pair
     * @param theoPair
     *            theoretical angle pair
     * @return lattice orientation
     */
    private Quaternion latticeOrientation(HoughPeakPair expPair,
            InterplanarAnglePair theoPair) {
        Vector3D es0 = expPair.normal0.plus(expPair.normal1).normalize();
        Vector3D es1 = expPair.normal0.minus(expPair.normal1).normalize();
        Vector3D es2 = es0.cross(es1);

        Vector3D ec0 = theoPair.normal0.plus(theoPair.normal1).normalize();
        Vector3D ec1 = theoPair.normal0.minus(theoPair.normal1).normalize();
        Vector3D ec2 = ec0.cross(ec1);

        double[][] es =
                new double[][] { es0.toArray(), es1.toArray(), es2.toArray() };
        double[][] ec =
                new double[][] { ec0.toArray(), ec1.toArray(), ec2.toArray() };

        double[][] g = new double[3][3];

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                g[i][j] =
                        ec[0][i] * es[0][j] + ec[1][i] * es[1][j] + ec[2][i]
                                * es[2][j];
            }
        }

        return new Quaternion(new Matrix3D(g));
    }



    /**
     * Eliminates possibilities where the sign of their direction cosine does
     * not match the sign of the experimental direction cosine.
     * 
     * @param possibilities
     *            possible angle pair from a match
     * @param expPair
     *            experimental angle pair calculated by the Hough peak
     * @return remaining possibilities
     */
    private InterplanarAnglePair[] reducePosibilities(
            InterplanarAnglePair[] possibilities, HoughPeakPair expPair) {
        ArrayList<InterplanarAnglePair> tmpPoss =
                new ArrayList<InterplanarAnglePair>();

        int expSign = sign(expPair.normal0.dot(expPair.normal1));

        for (int i = 0; i < possibilities.length; i++) {
            int theoSign =
                    sign(possibilities[i].normal0.dot(possibilities[i].normal1));

            if (expSign == theoSign || expSign == 0 || theoSign == 0)
                tmpPoss.add(possibilities[i]);
        }

        return tmpPoss.toArray(new InterplanarAnglePair[tmpPoss.size()]);
    }



    /**
     * Sets the status for the monitorable implementation and log the status in
     * the logger.
     * 
     * @param status
     *            descriptive text
     */
    private void setStatus(String status) {
        logger.info(status);
        this.status = status;
    }
}
