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
package org.ebsdimage.macro;

import static java.lang.Math.toRadians;

import java.io.File;

import org.ebsdimage.core.*;

import rmlimage.core.BinMap;
import rmlimage.core.ByteMap;
import rmlimage.core.Identification;
import rmlimage.core.RGB;
import rmlimage.io.IOManager;
import rmlimage.io.ResultSaver;
import rmlimage.macro.command.Desktop;

/**
 * Methods designed to be used in macros.
 * <p/>
 * Most methods in this class are static wrappers around methods in the
 * <code>org.ebsdimage.core</code> package. These methods are offered to
 * simplify writing macros.
 * 
 * @author Marin Lagac&eacute;
 */
public class EBSD {

    /**
     * Does an automatic thresholding on the specified <code>HoughMap</code> The
     * <code>BinMap</code> returned will be added to the desktop if the
     * <code>HoughMap</code> is also on the desktop.
     * 
     * @param houghMap
     *            <code>HoughMap</code> to do a thresholding on.
     * @return the resulting <code>BinMap</code>
     * @see Threshold#automaticTopHat(HoughMap)
     */
    public static BinMap houghThresholding(HoughMap houghMap) {
        BinMap binMap = Threshold.automaticTopHat(houghMap);

        // Add the BinMap to the desktop only if the original HoughMap
        // is also on the desktop
        if (Desktop.isOnDesktop(houghMap))
            Desktop.add(binMap);

        return binMap;
    }



    /**
     * Does an Hough Transform on the selected <code>ByteMap</code>. The angle
     * increment used is 0.5°. The <code>HoughMap</code> returned will be added
     * to the desktop if the <code>ByteMap</code> is also on the desktop.
     * 
     * @param byteMap
     *            <code>ByteMap</code> to do the thransform on.
     * @return the resulting <code>HoughMap</code> or <code>null</code> if an
     *         error occured.
     */
    public static HoughMap houghTransform(ByteMap byteMap) {
        return houghTransform(byteMap, toRadians(0.5));
    }



    /**
     * Does a Hough transform with the specified angle increment. The
     * <code>HoughMap</code> returned will be added to the desktop if the
     * <code>ByteMap</code> is also on the desktop.
     * 
     * @param byteMap
     *            <code>ByteMap</code> to do the transform of.
     * @param deltaTheta
     *            angle increment (in radians).
     * @return the Hough transform.
     * @see Transform#hough(ByteMap, double)
     */
    public static HoughMap houghTransform(ByteMap byteMap, double deltaTheta) {
        HoughMap houghMap = Transform.hough(byteMap, deltaTheta);

        // Add the HoughMap to the desktop only if the original ByteMap
        // is also on the desktop
        if (Desktop.isOnDesktop(byteMap))
            Desktop.add(houghMap);

        return houghMap;
    }



    /**
     * Overlays the specified line on the specified <code>ByteMap</code>.
     * 
     * @param r
     *            <code>r</code> coordinate of the line
     * @param theta
     *            <code>theta</code> coordinate of the line (in degrees)
     * @param byteMap
     *            <code>ByteMap</code> to overlay the line on.
     * @param color
     *            color index of the line
     * @see Drawing#line(ByteMap, double, double, int)
     */
    public static void qcLineOverlay(double r, double theta, ByteMap byteMap,
            int color) {
        Drawing.line(byteMap, r, toRadians(theta), color);
        byteMap.notifyListeners();
    }



    /**
     * Overlays the lines corresponding to the centroids of the objects found on
     * the specified <code>BinMap</code> to the specified <code>ByteMap</code>.
     * 
     * @param binMap
     *            <code>BinMap</code> to get the line paramters from.
     * @param byteMap
     *            <code>ByteMap</code> to overlay the line on.
     * @param red
     *            red component value of the line.
     * @param green
     *            green component value of the line.
     * @param blue
     *            blue component value of the line.
     * @see QC#overlay(ByteMap, BinMap, RGB)
     */
    public static void qcLineOverlay(BinMap binMap, ByteMap byteMap, int red,
            int green, int blue) {
        new QC().overlay(byteMap, binMap, new RGB(red, green, blue));
        byteMap.notifyListeners();
    }



    /**
     * Saves the coordinates of the peaks of a binarized <code>HoughMap</code>.
     * 
     * @param binMap
     *            binarized <code>HoughMap</code>.
     * @param fileName
     *            file name
     * @return <code>true</code> if the results were properly saved or
     *         <code>false</code> if an error occured.
     */
    public static Boolean savePeaks(BinMap binMap, String fileName) {
        return savePeaks(binMap, new File(fileName));
    }



    /**
     * Saves the coordinates of the peaks of a binarized <code>HoughMap</code>.
     * 
     * @param binMap
     *            binarized <code>HoughMap</code>.
     * @param file
     *            file name
     * @return <code>true</code> if the results were properly saved or
     *         <code>false</code> if an error occured.
     */
    public static Boolean savePeaks(BinMap binMap, File file) {
        // BADLY WRITTEN!!!!!!!
        // Duplicates code from rmlimage.plugin.wbsd.SavePeaks

        HoughPoint centroids =
                Analysis.getCentroid(Identification.identify(binMap));

        // Converts radians to degrees
        centroids.units[HoughPoint.ID_THETA] = "deg";
        for (int n = 0; n < centroids.getValueCount(); n++)
            centroids.theta[n] = (float) Math.toDegrees(centroids.theta[n]);

        return new IOManager().save(centroids, file, new ResultSaver(), false);
    }

}
