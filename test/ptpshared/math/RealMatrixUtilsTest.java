package ptpshared.math;

import org.apache.commons.math.linear.ArrayRealVector;
import org.apache.commons.math.linear.MatrixUtils;
import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.linear.RealVector;
import org.junit.Before;
import org.junit.Test;

import ptpshared.math.RealMatrixUtils;

import static org.junit.Assert.assertArrayEquals;

public class RealMatrixUtilsTest {

    private RealMatrix m;

    private RealVector v;



    @Before
    public void setUp() throws Exception {
        m =
                MatrixUtils.createRealMatrix(new double[][] { { 1, 2, 3 },
                        { 4, 5, 6 }, { 7, 8, 9 } });
        v = new ArrayRealVector(new double[] { 1, 2, 3 });
    }



    @Test
    public void testPostMultiply() {
        RealVector expected = new ArrayRealVector(new double[] { 14, 32, 50 });
        RealVector actual = RealMatrixUtils.postMultiply(m, v);

        assertArrayEquals(expected.toArray(), actual.toArray(), 1e-6);
    }

}
