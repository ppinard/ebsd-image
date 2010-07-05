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
import static java.lang.Math.cos;
import static java.lang.Math.sin;

import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

import rmlimage.core.BasicDrawing;
import rmlimage.core.ByteMap;
import rmlshared.geom.LineUtil;

/**
 * Drawing line using Hough space coordinates.
 * 
 * @author Philippe T. Pinard
 */
public class Drawing {

    /**
     * Returns a line specified by the polar coordinates and cropped to the
     * specified <code>ByteMap</code> frame.
     * 
     * @param map
     *            <code>ByteMap</code> to crop the line to.
     * @param r
     *            <code>r</code> coordinates of the line.
     * @param theta
     *            <code>theta</code> coordinates of the line.
     * @return the line
     */
    public static Line2D getLine(ByteMap map, double r, double theta) {
        return getLine(r, theta, map.width, map.height);
    }



    /**
     * Returns a line specified by the polar coordinates and cropped to the
     * specified frame.
     * 
     * @param r
     *            <code>r</code> coordinates of the line.
     * @param theta
     *            <code>theta</code> coordinates of the line.
     * @param width
     *            width of the frame used to crop the line to.
     * @param height
     *            height of the frame used to crop the line to.
     * @return the line
     * @throws IllegalArgumentException
     *             if <code>width</code> or <code>height</code> are <= 0.
     */
    public static Line2D getLine(double r, double theta, int width, int height) {
        if (width <= 0)
            throw new IllegalArgumentException("width (" + width
                    + ") must be > " + 0 + '.');
        if (height <= 0)
            throw new IllegalArgumentException("height (" + height
                    + ") must be > " + 0 + '.');

        // Create the line perpendicular to the specified vector
        double slope = -cos(theta) / sin(theta);
        Point2D.Double center =
                new Point2D.Double(r * cos(theta), r * sin(theta));
        Line2D.Double line = new Line2D.Double();
        LineUtil.setAnalyticalLine(line, slope, center, 100.0);

        // Translate the origin from the center of the image
        // to the upper left corner
        // and invert the y axis to have the positive going down
        LineUtil.translate(line, width / 2, height / 2);
        line.y1 = height - 1 - line.y1;
        line.y2 = height - 1 - line.y2;

        // Extends the line to fit the whole map
        Rectangle frame = new Rectangle(0, 0, width - 1, height - 1);
        LineUtil.extendTo(line, frame);

        return line;
    }



    /**
     * Draws a line of the specified polar coordinates and color on the
     * specified <code>ByteMap</code>.
     * 
     * @param map
     *            <code>ByteMap</code> to draw the line on.
     * @param r
     *            <code>r</code> coordinate of the line to draw
     * @param theta
     *            <code>theta</code> coordinate of the line to draw
     * @param color
     *            index of the color to draw
     */
    public static void line(ByteMap map, double r, double theta, int color) {
        if (color < 0 || color > 255)
            throw new IllegalArgumentException("color (" + color
                    + ") must be between 0 and 255");

        BasicDrawing.line(map, getLine(map, r, theta), color);
    }



    /**
     * Draws a line of the specified color distribution along its thickness. The
     * line will be clipped at the <code>ByteMap</code>'s border.
     * <p/>
     * A line of thickness higher than one is created by drawing a series of
     * single pixel wide lines parallel to each other. The color distribution is
     * specified as an array of color indexes. Each color in the array represent
     * the color of one of the line in the series of lines to draw to get the
     * final thick line. The length of the color distribution array will set the
     * thickness of the line.
     * 
     * @param map
     *            <code>ByteMap</code> to draw the line on.
     * @param x1
     *            x coordinate of the first endpoint.
     * @param y1
     *            y coordinate of the first endpoint.
     * @param x2
     *            x coordinate of the second endpoint.
     * @param y2
     *            y coordinate of the second endpoint.
     * @param colors
     *            color distribution array
     * @throws IllegalArgumentException
     *             if any color in the array is < 0 or > 255
     */
    public static void line(ByteMap map, int x1, int y1, int x2, int y2,
            int[] colors) {
        double slope = (double) (y1 - y2) / (x1 - x2);
        double perpendicularSlope = -1 / slope;

        int thickness = colors.length;

        // Draw the center line
        BasicDrawing.line(map, x1, y1, x2, y2, colors[thickness / 2]);

        if (abs(slope) <= 1) { // If line <= 45deg
            int xx1 = x1;
            int xx2 = x2;
            int yy1 = y1;
            int yy2 = y2;
            double dx = 0;
            for (int n = thickness / 2 - 1; n >= 0; n--) { // Do upper part
                dx += -1 / perpendicularSlope;
                // System.out.println("dx = " + dx);
                if (dx >= 0.5) {
                    xx1++;
                    xx2++;
                    dx--;
                    BasicDrawing.line(map, xx1, yy1, xx2, yy2, colors[n]);
                }
                if (dx <= -0.5) {
                    xx1--;
                    xx2--;
                    dx++;
                    BasicDrawing.line(map, xx1, yy1, xx2, yy2, colors[n]);
                }
                yy1--;
                yy2--;
                BasicDrawing.line(map, xx1, yy1, xx2, yy2, colors[n]);
            }

            xx1 = x1;
            xx2 = x2;
            yy1 = y1;
            yy2 = y2;
            dx = 0;
            for (int n = thickness / 2 + 1; n < thickness; n++) // Do lower part
            {
                dx += 1 / perpendicularSlope;
                if (dx >= 0.5) {
                    xx1++;
                    xx2++;
                    dx--;
                    BasicDrawing.line(map, xx1, yy1, xx2, yy2, colors[n]);
                }
                if (dx <= -0.5) {
                    xx1--;
                    xx2--;
                    dx++;
                    BasicDrawing.line(map, xx1, yy1, xx2, yy2, colors[n]);
                }
                yy1++;
                yy2++;
                BasicDrawing.line(map, xx1, yy1, xx2, yy2, colors[n]);
            }
        } else {
            int xx1 = x1;
            int xx2 = x2;
            int yy1 = y1;
            int yy2 = y2;
            double dy = 0;
            for (int n = thickness / 2 - 1; n >= 0; n--) { // Do left part
                dy += -1 * perpendicularSlope;
                // System.out.println("dy = " + dy);
                if (dy >= 0.5) {
                    yy1++;
                    yy2++;
                    dy--;
                    BasicDrawing.line(map, xx1, yy1, xx2, yy2, colors[n]);
                }
                if (dy <= -0.5) {
                    yy1--;
                    yy2--;
                    dy++;
                    BasicDrawing.line(map, xx1, yy1, xx2, yy2, colors[n]);
                }
                xx1--;
                xx2--;
                BasicDrawing.line(map, xx1, yy1, xx2, yy2, colors[n]);
            }

            xx1 = x1;
            xx2 = x2;
            yy1 = y1;
            yy2 = y2;
            dy = 0;
            for (int n = thickness / 2 + 1; n < thickness; n++) // Do lower part
            {
                dy += perpendicularSlope;
                if (dy >= 0.5) {
                    yy1++;
                    yy2++;
                    dy--;
                    BasicDrawing.line(map, xx1, yy1, xx2, yy2, colors[n]);
                }
                if (dy <= -0.5) {
                    yy1--;
                    yy2--;
                    dy++;
                    BasicDrawing.line(map, xx1, yy1, xx2, yy2, colors[n]);
                }
                xx1++;
                xx2++;
                BasicDrawing.line(map, xx1, yy1, xx2, yy2, colors[n]);
            }
        }

        map.setChanged(ByteMap.MAP_CHANGED);
    }
}
