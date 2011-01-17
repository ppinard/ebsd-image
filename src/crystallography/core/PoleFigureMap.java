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
package crystallography.core;

import ptpshared.core.math.Quaternion;
import ptpshared.core.math.QuaternionMath;
import ptpshared.core.math.Vector3D;
import rmlimage.module.real.core.RealMap;
import static ptpshared.core.math.Math.acos;
import static java.lang.Math.*;

/**
 * Stereographic pole figure. Draw rotation(s) on a stereographic pole figure.
 * The <code>PoleFigureMap</code> extends a <code>RealMap</code>, therefore it
 * can be saved and loaded as a <code>RealMap</code> as well as used in any
 * <code>RealMap</code> operations. <b>References:</b>
 * <ul>
 * <li>Orilib</li>
 * </ul>
 * 
 * @author Philippe T. Pinard
 */
public class PoleFigureMap extends RealMap {

    /** Pole of interest. */
    public final Vector3D pole;

    /** Crystal. */
    public final Crystal crystal;



    /**
     * Creates a new <code>PoleFigureMap</code>. The pole figure gives a
     * stereographic representation of the operations ({@link #drawRotation}).
     * The pole figure map is an extension of a <code>RealMap</code>.
     * 
     * @param width
     *            width of the map (the height is equal to the width)
     * @param pole
     *            pole of interest
     * @param crystal
     *            crystal (for symmetry operators)
     */
    public PoleFigureMap(int width, Vector3D pole, Crystal crystal) {
        super(width, width);
        this.pole = pole.normalize();
        this.crystal = crystal;
    }



    /**
     * Increments the pixel at the given angle and distance.
     * 
     * @param beta
     *            angle
     * @param dist
     *            distance
     */
    protected void drawPoint(double beta, double dist) {
        int x = (int) ((dist * cos(beta)) * ((width - 1) / 2) + width / 2);
        int y = (int) ((dist * sin(beta)) * ((height - 1) / 2) + height / 2);

        setPixValue(x, y, getPixValue(x, y) + 1);
    }



    /**
     * Draws the specified rotation in the pole figure. All the equivalent
     * symmetrical rotations are also drawn.
     * 
     * @param rotation
     *            a rotation
     */
    public void drawRotation(Quaternion rotation) {
        for (Quaternion op : crystal.spaceGroup.getOperators()) {
            Quaternion equiv = rotation.multiply(op);

            Vector3D out = QuaternionMath.rotate(pole, equiv);

            double alpha = acos(out.get(2));

            double beta;
            if (alpha < 1e-7)
                beta = 0;
            else
                beta = atan2(out.get(1) / sin(alpha), out.get(0) / sin(alpha));

            if (alpha > PI / 2) {
                alpha = PI - alpha;
                beta += PI;
            }

            double dist = tan(alpha / 2);

            drawPoint(beta, dist);
        }
    }
}
