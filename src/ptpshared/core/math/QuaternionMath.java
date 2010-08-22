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
package ptpshared.core.math;

import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;

import java.util.Random;

import edu.umd.cs.findbugs.annotations.CheckReturnValue;

/**
 * Common operations on quaternions and rotations.
 * 
 * @author Philippe T. Pinard
 */
public class QuaternionMath {

    /**
     * Returns a <code>Quaternion</code> representing the misorientation (in
     * radians) between two quaternions.
     * <p/>
     * <code>DeltaQ = (q1*)q2</code>
     * 
     * @param q1
     *            a quaternion
     * @param q2
     *            a quaternion
     * @return misorientation
     */
    @CheckReturnValue
    public static Quaternion misorientation(Quaternion q1, Quaternion q2) {
        return q1.conjugate().multiply(q2);
    }



    /**
     * Returns a new <code>Vector3D</code> resulting from the rotation of the
     * input <code>vector</code> by all the rotation quaternions in
     * <code>qrotations</code>. Order of rotation: <code>qrotations[0]</code>,
     * <code>qrotations[1]</code> , <code>qrotations[2]</code>, ...
     * 
     * @param vector
     *            a vector to be rotated
     * @param qrotations
     *            a list of quaternions defining rotations
     * @return rotated vector
     */
    @CheckReturnValue
    public static Vector3D rotate(Vector3D vector, Quaternion... qrotations) {
        Quaternion qout = new Quaternion(vector);

        for (Quaternion qrotation : qrotations) {
            // Rotation has to be a unit quaternion
            qrotation = qrotation.normalize();

            // Have to prevent quaternion to be automatically made positive
            // since it prevents 180 deg rotation
            qout =
                    qrotation.multiply(qout, false).multiply(
                            qrotation.conjugate(false), false);
        }

        return qout.vector;
    }



    /**
     * Returns a new <code>Vector3D</code> resulting from the rotation of the
     * input <code>vector</code> by the rotation quaternion.
     * 
     * @param vector
     *            a vector to be rotated
     * @param rotation
     *            rotation quaternion
     * @return rotated vector
     */
    @CheckReturnValue
    public static Vector3D rotate(Vector3D vector, Quaternion rotation) {
        Quaternion q = rotation.normalize();
        Quaternion in = new Quaternion(vector);

        // Have to prevent quaternion to be automatically made positive since it
        // prevents 180 deg rotation
        Quaternion out =
                q.multiply(in, false).multiply(q.conjugate(false), false);

        return out.vector;
    }



    /**
     * Returns a <code>Quaternion</code> representing the rotation between two
     * vectors.
     * <p/>
     * <b>References:</b>
     * <ul>
     * <li>Algorithm taken from Martin Baker's website</li>
     * </ul>
     * 
     * @param v1
     *            first vector
     * @param v2
     *            second vector
     * @return quaternion representing the rotation between <code>v1</code> and
     *         <code>v2</code>
     */
    @CheckReturnValue
    public static Quaternion rotation(Vector3D v1, Vector3D v2) {
        Vector3D axis = v1.cross(v2);
        double scalar = v1.norm() * v2.norm() + v1.dot(v2);

        if (scalar < 1e-7) // Vectors are 180 deg apart
            return new Quaternion(0, -v1.get(2), v1.get(1), v1.get(0)).normalize();

        return new Quaternion(scalar, axis).normalize();
    }



    /**
     * Returns a <code>Quaternion</code> of a random rotation.
     * <p/>
     * <b>References:</b>
     * <ul>
     * <li><a href="http://neon.materials.cmu.edu/rollett/27750/lecture2.pdf">
     * Introduction of Group Theory and Rotations in Microstrucutral
     * Analysis</a>, J. Gruber, CArnegie Mellon University</li>
     * </ul>
     * 
     * @param seed
     *            seed for the random number generator
     * @return a random rotation quaternion
     */
    @CheckReturnValue
    public static Quaternion randomRotation(long seed) {
        Random random = new Random(seed);

        double r1 = random.nextDouble();
        double r2 = random.nextDouble();
        double r3 = random.nextDouble();

        double a = cos(2 * PI * r1) / sqrt(r3);
        double b = sin(2 * PI * r2) / sqrt(1 - r3);
        double c = cos(2 * PI * r2) / sqrt(1 - r3);
        double d = sin(2 * PI * r1) / sqrt(r3);

        return new Quaternion(a, b, c, d);
    }



    /**
     * Returns a <code>Quaternion</code> of a random rotation. The current time
     * in milliseconds is used as the seed generator.
     * <p/>
     * <b>References:</b>
     * <ul>
     * <li><a href="http://neon.materials.cmu.edu/rollett/27750/lecture2.pdf">
     * Introduction of Group Theory and Rotations in Microstrucutral
     * Analysis</a>, J. Gruber, CArnegie Mellon University</li>
     * </ul>
     * 
     * @return a random rotation quaternion
     */
    @CheckReturnValue
    public static Quaternion randomRotation() {
        return randomRotation(System.currentTimeMillis());
    }

}
