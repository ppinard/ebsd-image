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

import static java.lang.Math.PI;
import static java.lang.Math.pow;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class UnitCellFactoryTest {

    @Test
    public void testCubic() {
        UnitCell cubic = UnitCellFactory.cubic(2.0);

        assertEquals(cubic.a, 2.0, 1e-7);
        assertEquals(cubic.b, 2.0, 1e-7);
        assertEquals(cubic.c, 2.0, 1e-7);
        assertEquals(cubic.aR, 0.5, 1e-7);
        assertEquals(cubic.bR, 0.5, 1e-7);
        assertEquals(cubic.cR, 0.5, 1e-7);
        assertEquals(cubic.alpha, PI / 2, 1e-7);
        assertEquals(cubic.beta, PI / 2, 1e-7);
        assertEquals(cubic.gamma, PI / 2, 1e-7);
        assertEquals(cubic.alphaR, PI / 2, 1e-7);
        assertEquals(cubic.betaR, PI / 2, 1e-7);
        assertEquals(cubic.gammaR, PI / 2, 1e-7);
        assertEquals(cubic.volume, 8.0, 1e-7);
        assertEquals(cubic.volumeR, 0.125, 1e-7);

        assertEquals(cubic.aR, 1.0 / 2.0, 1e-7);
        assertEquals(cubic.bR, 1.0 / 2.0, 1e-7);
        assertEquals(cubic.cR, 1.0 / 2.0, 1e-7);
        assertEquals(cubic.volume, pow(2.0, 3), 1e-7);
    }



    @Test
    public void testHexagonal() {
        UnitCell hexagonal = UnitCellFactory.hexagonal(2.0, 3.0);

        assertEquals(hexagonal.a, 2.0, 1e-7);
        assertEquals(hexagonal.b, 2.0, 1e-7);
        assertEquals(hexagonal.c, 3.0, 1e-7);
        assertEquals(hexagonal.aR, 0.5773502691896257, 1e-7);
        assertEquals(hexagonal.bR, 0.5773502691896257, 1e-7);
        assertEquals(hexagonal.cR, 0.333333333, 1e-7);
        assertEquals(hexagonal.alpha, PI / 2, 1e-7);
        assertEquals(hexagonal.beta, PI / 2, 1e-7);
        assertEquals(hexagonal.gamma, 120.0 / 180 * PI, 1e-7);
        assertEquals(hexagonal.alphaR, PI / 2, 1e-7);
        assertEquals(hexagonal.betaR, PI / 2, 1e-7);
        assertEquals(hexagonal.gammaR, 60.0 / 180 * PI, 1e-7);
        assertEquals(hexagonal.volume, 10.392304845413264, 1e-7);
        assertEquals(hexagonal.volumeR, 0.09622504486493763, 1e-7);

        assertEquals(hexagonal.aR, 2.0 / 3.0 * sqrt(3) / 2.0, 1e-7);
        assertEquals(hexagonal.bR, 2.0 / 3.0 * sqrt(3) / 2.0, 1e-7);
        assertEquals(hexagonal.cR, 1.0 / 3.0, 1e-7);
        assertEquals(hexagonal.volume, 0.5 * sqrt(3) * pow(2.0, 2) * 3.0, 1e-7);
    }



    @Test
    public void testMonoclinic() {
        UnitCell monoclinic = UnitCellFactory.monoclinic(1.0, 2.0, 3.0,
                55.0 / 180 * PI);

        assertEquals(monoclinic.a, 1.0, 1e-7);
        assertEquals(monoclinic.b, 2.0, 1e-7);
        assertEquals(monoclinic.c, 3.0, 1e-7);
        assertEquals(monoclinic.aR, 1.220774588761456, 1e-7);
        assertEquals(monoclinic.bR, 0.5, 1e-7);
        assertEquals(monoclinic.cR, 0.40692486292048535, 1e-7);
        assertEquals(monoclinic.alpha, PI / 2, 1e-7);
        assertEquals(monoclinic.beta, 55.0 / 180 * PI, 1e-7);
        assertEquals(monoclinic.gamma, PI / 2, 1e-7);
        assertEquals(monoclinic.alphaR, PI / 2, 1e-7);
        assertEquals(monoclinic.betaR, 125.0 / 180 * PI, 1e-7);
        assertEquals(monoclinic.gammaR, PI / 2, 1e-7);
        assertEquals(monoclinic.volume, 4.914912265733951, 1e-7);
        assertEquals(monoclinic.volumeR, 0.20346243146024268, 1e-7);

        assertEquals(monoclinic.aR, 1.0 / (1 * sin(125.0 / 180 * PI)), 1e-7);
        assertEquals(monoclinic.bR, 1.0 / 2.0, 1e-7);
        assertEquals(monoclinic.cR, 1.0 / (3 * sin(125.0 / 180 * PI)), 1e-7);
        assertEquals(monoclinic.volume,
                1.0 * 2.0 * 3.0 * sin(125.0 / 180 * PI), 1e-7);
    }



    @Test
    public void testOrthorhombic() {
        UnitCell orthorhombic = UnitCellFactory.orthorhombic(1.0, 2.0, 3.0);

        assertEquals(orthorhombic.a, 1.0, 1e-7);
        assertEquals(orthorhombic.b, 2.0, 1e-7);
        assertEquals(orthorhombic.c, 3.0, 1e-7);
        assertEquals(orthorhombic.aR, 1.0, 1e-7);
        assertEquals(orthorhombic.bR, 0.5, 1e-7);
        assertEquals(orthorhombic.cR, 0.33333333, 1e-7);
        assertEquals(orthorhombic.alpha, PI / 2, 1e-7);
        assertEquals(orthorhombic.beta, PI / 2, 1e-7);
        assertEquals(orthorhombic.gamma, PI / 2, 1e-7);
        assertEquals(orthorhombic.alphaR, PI / 2, 1e-7);
        assertEquals(orthorhombic.betaR, PI / 2, 1e-7);
        assertEquals(orthorhombic.gammaR, PI / 2, 1e-7);
        assertEquals(orthorhombic.volume, 6.0, 1e-7);
        assertEquals(orthorhombic.volumeR, 0.166666666, 1e-7);

        assertEquals(orthorhombic.aR, 1.0 / 1.0, 1e-7);
        assertEquals(orthorhombic.bR, 1.0 / 2.0, 1e-7);
        assertEquals(orthorhombic.cR, 1.0 / 3.0, 1e-7);
        assertEquals(orthorhombic.volume, 1.0 * 2.0 * 3.0, 1e-7);
    }



    @Test
    public void testTetragonal() {
        UnitCell tetragonal = UnitCellFactory.tetragonal(2.0, 3.0);

        assertEquals(tetragonal.a, 2.0, 1e-7);
        assertEquals(tetragonal.b, 2.0, 1e-7);
        assertEquals(tetragonal.c, 3.0, 1e-7);
        assertEquals(tetragonal.aR, 0.5, 1e-7);
        assertEquals(tetragonal.bR, 0.5, 1e-7);
        assertEquals(tetragonal.cR, 0.333333333, 1e-7);
        assertEquals(tetragonal.alpha, PI / 2, 1e-7);
        assertEquals(tetragonal.beta, PI / 2, 1e-7);
        assertEquals(tetragonal.gamma, PI / 2, 1e-7);
        assertEquals(tetragonal.alphaR, PI / 2, 1e-7);
        assertEquals(tetragonal.betaR, PI / 2, 1e-7);
        assertEquals(tetragonal.gammaR, PI / 2, 1e-7);
        assertEquals(tetragonal.volume, 12.0, 1e-7);
        assertEquals(tetragonal.volumeR, 0.083333333, 1e-7);

        assertEquals(tetragonal.aR, 1.0 / 2.0, 1e-7);
        assertEquals(tetragonal.bR, 1.0 / 2.0, 1e-7);
        assertEquals(tetragonal.cR, 1.0 / 3.0, 1e-7);
        assertEquals(tetragonal.volume, pow(2.0, 2) * 3.0, 1e-7);
    }



    @Test
    public void testTriclinic() {
        UnitCell triclinic = UnitCellFactory.triclinic(1.0, 2.0, 3.0,
                75.0 / 180 * PI, 55.0 / 180 * PI, 35.0 / 180 * PI);

        assertEquals(triclinic.a, 1.0, 1e-7);
        assertEquals(triclinic.b, 2.0, 1e-7);
        assertEquals(triclinic.c, 3.0, 1e-7);
        assertEquals(triclinic.aR, 2.3009777700230383, 1e-7);
        assertEquals(triclinic.bR, 0.9756704877739889, 1e-7);
        assertEquals(triclinic.cR, 0.45544788689872767, 1e-7);
        assertEquals(triclinic.alpha, 75.0 / 180 * PI, 1e-7);
        assertEquals(triclinic.beta, 55.0 / 180 * PI, 1e-7);
        assertEquals(triclinic.gamma, 35.0 / 180 * PI, 1e-7);
        assertEquals(triclinic.alphaR, 1.1049925940211875, 1e-7);
        assertEquals(triclinic.betaR, 2.281813838221562, 1e-7);
        assertEquals(triclinic.gammaR, 2.582348070021294, 1e-7);
        assertEquals(triclinic.volume, 2.518735744968272, 1e-7);
        assertEquals(triclinic.volumeR, 0.3970245794929935, 1e-7);
    }



    @Test
    public void testTrigonal() {
        UnitCell trigonal = UnitCellFactory.trigonal(2.0, 35.0 / 180 * PI);

        assertEquals(trigonal.a, 2.0, 1e-7);
        assertEquals(trigonal.b, 2.0, 1e-7);
        assertEquals(trigonal.c, 2.0, 1e-7);
        assertEquals(trigonal.aR, 0.9763044673403796, 1e-7);
        assertEquals(trigonal.bR, 0.9763044673403796, 1e-7);
        assertEquals(trigonal.cR, 0.9763044673403796, 1e-7);
        assertEquals(trigonal.alpha, 35.0 / 180 * PI, 1e-7);
        assertEquals(trigonal.beta, 35.0 / 180 * PI, 1e-7);
        assertEquals(trigonal.gamma, 35.0 / 180 * PI, 1e-7);
        assertEquals(trigonal.alphaR, 2.0378901672656156, 1e-7);
        assertEquals(trigonal.betaR, 2.0378901672656156, 1e-7);
        assertEquals(trigonal.gammaR, 2.0378901672656156, 1e-7);
        assertEquals(trigonal.volume, 2.349990010446501, 1e-7);
        assertEquals(trigonal.volumeR, 0.42553372378378695, 1e-7);
    }

}
