/*
 * EBSD-Image
 * Copyright (C) 2010-2011 Philippe T. Pinard
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
package ptpshared.math;

import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

/**
 * Test taken from the test files of the original QUADPACK implementation in
 * Fortran.
 * 
 * @author Philippe T. Pinard
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
