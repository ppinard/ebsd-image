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

import org.apache.commons.math.linear.ArrayRealVector;
import org.apache.commons.math.linear.MatrixUtils;
import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.linear.RealVector;
import org.junit.Before;
import org.junit.Test;

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
