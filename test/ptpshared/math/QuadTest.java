package ptpshared.math;

import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static org.junit.Assert.assertEquals;

import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.junit.Test;

import ptpshared.math.Quad;

/**
 * Test taken from the test files of the original QUADPACK implementation in
 * Fortran.
 * 
 * @author ppinard
 */
public class QuadTest {

    private class F02 implements UnivariateRealFunction {

        @Override
        public double value(double x) throws FunctionEvaluationException {
            return cos(100.0 * sin(x));
        }

    }



    @Test
    public void testIntegrateDoubleDouble1() throws FunctionEvaluationException {
        assertEquals(0.06278740, new Quad().integrate(new F02(), 0, PI), 1e-6);
    }

}