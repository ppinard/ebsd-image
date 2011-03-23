package ptpshared.geom;

import java.util.Random;

import org.apache.commons.math.geometry.CardanEulerSingularityException;
import org.apache.commons.math.geometry.Rotation;
import org.apache.commons.math.geometry.RotationOrder;

import edu.umd.cs.findbugs.annotations.CheckReturnValue;
import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;

/**
 * Operations on rotations.
 * 
 * @author Philippe T. Pinard
 */
public class RotationUtils {

    /**
     * Checks whether two <code>Rotation</code> are equal to a certain
     * precision. This is done using the
     * {@link Rotation#distance(Rotation, Rotation)} method of the
     * <code>Rotation</code> object.
     * 
     * @param r1
     *            rotation 1
     * @param r2
     *            rotation 2
     * @param precision
     *            precision (angular deviation allowed)
     * @return <code>true</code> if the two rotations are equal to the specified
     *         precision, <code>false</code> otherwise
     */
    public static boolean equals(Rotation r1, Rotation r2, double precision) {
        if (precision < 0)
            throw new IllegalArgumentException(
                    "Precision cannot be less than zero.");

        if (r1 == null || r2 == null)
            return false;

        if (Rotation.distance(r1, r2) > precision)
            return false;

        return true;
    }



    /**
     * Returns the Euler angles representation of a rotation in the Bunge
     * convention.
     * <ul>
     * <li>0 < theta1 < 2PI</li>
     * <li>0 < theta2 < PI</li>
     * <li>0 < theta3 < 2PI</li>
     * </ul>
     * 
     * @param r
     *            a rotation
     * @return array of three numbers
     * @throws CardanEulerSingularityException
     *             if the rotation cannot be represented as Euler angles
     */
    public static double[] getBungeEulerAngles(Rotation r)
            throws CardanEulerSingularityException {
        double[] eulers = r.getAngles(RotationOrder.ZXZ);

        if (eulers[0] < 0)
            eulers[0] += 2 * Math.PI;
        if (eulers[2] < 0)
            eulers[2] += 2 * Math.PI;

        return eulers;
    }



    /**
     * Returns a <code>Rotation</code> of a random rotation. The current time in
     * milliseconds is used as the seed generator.
     * <p/>
     * <b>References:</b>
     * <ul>
     * <li><a href="http://neon.materials.cmu.edu/rollett/27750/lecture2.pdf">
     * Introduction of Group Theory and Rotations in Microstrucutral
     * Analysis</a>, J. Gruber, CArnegie Mellon University</li>
     * </ul>
     * 
     * @return a random rotation
     */
    @CheckReturnValue
    public static Rotation randomRotation() {
        return randomRotation(System.currentTimeMillis());
    }



    /**
     * Returns a <code>Rotation</code> of a random rotation.
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
     * @return a random rotation
     */
    @CheckReturnValue
    public static Rotation randomRotation(long seed) {
        Random random = new Random(seed);

        double r1 = random.nextDouble();
        double r2 = random.nextDouble();
        double r3 = random.nextDouble();

        double a = cos(2 * PI * r1) / sqrt(r3);
        double b = sin(2 * PI * r2) / sqrt(1 - r3);
        double c = cos(2 * PI * r2) / sqrt(1 - r3);
        double d = sin(2 * PI * r1) / sqrt(r3);

        return new Rotation(a, b, c, d, true);
    }



    /**
     * Returns a string representation of a <code>Rotation</code>.
     * 
     * @param r
     *            a rotation
     * @return string representation
     */
    public static String toString(Rotation r) {
        return "[[" + r.getQ0() + "; " + r.getQ1() + "; " + r.getQ2() + "; "
                + r.getQ3() + "]]";
    }
}
