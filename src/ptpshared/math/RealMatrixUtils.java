package ptpshared.math;

import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.linear.RealVector;

/**
 * Utilities to use with Apache common <code>RealMatrix</code>.
 * 
 * @author Philippe T. Pinard
 */
public class RealMatrixUtils {

    /**
     * Multiplies the specified matrix by the specified COLUMN vector.
     * <p/>
     * The <code>RealMatrix</code> interface only allows multiplication of
     * matrix by ROW vector.
     * 
     * @param m
     *            matrix
     * @param v
     *            vector
     * @return resultant COLUMN vector
     */
    public static RealVector postMultiply(RealMatrix m, RealVector v) {
        return m.transpose().preMultiply(v);
    }
}
