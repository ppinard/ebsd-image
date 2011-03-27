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
package crystallography.core;

import java.util.ArrayList;

import org.apache.commons.math.complex.Complex;
import org.apache.commons.math.geometry.NotARotationMatrixException;
import org.apache.commons.math.geometry.Rotation;
import org.apache.commons.math.geometry.Vector3D;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import static ptpshared.geom.Assert.assertEquals;
import static ptpshared.math.Math.acos;
import static java.lang.Math.*;

import static junittools.test.Assert.assertEquals;

public class CalculationsTest {

    private UnitCell cubic;

    private UnitCell tetragonal;

    private UnitCell orthorhombic;

    private UnitCell trigonal;

    private UnitCell hexagonal;

    private UnitCell monoclinic;

    private UnitCell triclinic;

    private ArrayList<Vector3D> planes;

    private UnitCell unitCell1;

    private AtomSite atom1;

    private AtomSite atom2;

    private AtomSite atom3;

    private UnitCell unitCell2;

    private AtomSite atom4;

    private AtomSite atom5;

    private AtomSite atom6;

    private UnitCell unitCell3;

    private UnitCell unitCell4;

    private XrayScatteringFactors scatter;

    private AtomSites atomsfcc;

    private AtomSites atomsbcc;

    private AtomSites atomshcp;



    @Before
    public void setUp() throws Exception {
        // Variables
        double alpha;
        double beta;
        double gamma;

        // Manual testing
        cubic = UnitCellFactory.cubic(2);
        tetragonal = UnitCellFactory.tetragonal(2, 3);
        orthorhombic = UnitCellFactory.orthorhombic(1, 2, 3);
        trigonal = UnitCellFactory.trigonal(2, 35.0 / 180 * PI);
        hexagonal = UnitCellFactory.hexagonal(2, 3);
        monoclinic = UnitCellFactory.monoclinic(1, 2, 3, 55.0 / 180 * PI);
        triclinic =
                UnitCellFactory.triclinic(1, 2, 3, 75.0 / 180 * PI,
                        55.0 / 180 * PI, 35.0 / 180 * PI);

        planes = new ArrayList<Vector3D>();
        planes.add(new Vector3D(1, 0, 0));
        planes.add(new Vector3D(1, 1, 0));
        planes.add(new Vector3D(1, 1, 1));
        planes.add(new Vector3D(2, 0, 2));
        planes.add(new Vector3D(1, 2, 3));
        planes.add(new Vector3D(4, 5, 6));
        planes.add(new Vector3D(0, 9, 2));

        // Example 1.13 from Mathematical Crystallography (p.31-33)
        unitCell1 = UnitCellFactory.hexagonal(4.914, 5.409);

        atom1 = new AtomSite(8, 0.4141, 0.2681, 0.1188);
        atom2 = new AtomSite(14, 0.4699, 0.0, 0.0);
        atom3 = new AtomSite(14, 0.5301, 0.5301, 0.3333);

        // Problem 1.13 from Mathematical Crystallography (p.34)
        alpha = 93.11 / 180.0 * PI;
        beta = 115.91 / 180.0 * PI;
        gamma = 91.26 / 180.0 * PI;
        unitCell2 =
                UnitCellFactory.triclinic(8.173, 12.869, 14.165, alpha, beta,
                        gamma);

        atom4 = new AtomSite(8, 0.3419, 0.3587, 0.1333);
        atom5 = new AtomSite(14, 0.5041, 0.3204, 0.1099);
        atom6 = new AtomSite(13, 0.1852, 0.3775, 0.1816);

        // Example 2.16 and Problem 2.9 from Mathematical Crystallography
        // (p.64-65)
        alpha = 114.27 / 180.0 * PI;
        beta = 82.68 / 180.0 * PI;
        gamma = 94.58 / 180.0 * PI;
        unitCell3 =
                UnitCellFactory.triclinic(6.621, 7.551, 17.381, alpha, beta,
                        gamma);

        // Problem 2.15 from Mathematical Crystallography (p.79)
        alpha = 113.97 / 180.0 * PI;
        beta = 98.64 / 180.0 * PI;
        gamma = 67.25 / 180.0 * PI;
        unitCell4 =
                UnitCellFactory.triclinic(5.148, 7.251, 5.060, alpha, beta,
                        gamma);

        // Scattering factors
        scatter = new XrayScatteringFactors();

        // Atom sites
        atomsfcc = AtomSitesFactory.atomSitesFCC(14);
        atomsbcc = AtomSitesFactory.atomSitesBCC(14);
        atomshcp = AtomSitesFactory.atomSitesHCP(14);
    }



    @Test
    public void testBondAngle() {
        double bondAngle;

        // Example 1.13
        bondAngle = Calculations.bondAngle(atom1, atom2, atom3, unitCell1);
        assertEquals(143.7, bondAngle * 180 / PI, 1e-1);

        // Problem 1.13
        bondAngle = Calculations.bondAngle(atom4, atom5, atom6, unitCell2);
        assertEquals(165.6, bondAngle * 180 / PI, 1e-1);
    }



    @Test
    public void testBondDistance() {
        double bondDistance;

        // Example 1.13
        bondDistance = Calculations.bondDistance(atom1, atom2, unitCell1);
        assertEquals(1.608, bondDistance, 1e-3);

        bondDistance = Calculations.bondDistance(atom1, atom3, unitCell1);
        assertEquals(1.611, bondDistance, 1e-3);

        // Problem 1.13
        bondDistance = Calculations.bondDistance(atom4, atom5, unitCell2);
        assertEquals(1.5828, bondDistance, 1e-3);

        bondDistance = Calculations.bondDistance(atom4, atom6, unitCell2);
        assertEquals(1.7112, bondDistance, 1e-3);
    }



    @Test
    public void testDiffractionAngleDoubleDouble() {
        double angle;

        // http://hyperphysics.phy-astr.gsu.edu/hbase/quantum/bragg.html
        angle = Calculations.diffractionAngle(1 * 10, 0.12 * 10);
        assertEquals(3.439812767515196, angle * 180 / PI, 1e-7);

        angle = Calculations.diffractionAngle(5 * 10, 0.2 * 10);
        assertEquals(1.1459919983885926, angle * 180 / PI, 1e-7);

        angle = Calculations.diffractionAngle(0.25 * 10, 0.0025 * 10);
        assertEquals(0.2864800912409137, angle * 180 / PI, 1e-7);
    }



    @Test
    public void testDiffractionAngleDoubleDoubleDouble() {
        double angle;

        // http://hyperphysics.phy-astr.gsu.edu/hbase/quantum/bragg.html
        angle = Calculations.diffractionAngle(1 * 10, 0.12 * 10, 1);
        assertEquals(3.439812767515196, angle * 180 / PI, 1e-7);

        angle = Calculations.diffractionAngle(5 * 10, 0.2 * 10, 1);
        assertEquals(1.1459919983885926, angle * 180 / PI, 1e-7);

        angle = Calculations.diffractionAngle(0.25 * 10, 0.0025 * 10, 1);
        assertEquals(0.2864800912409137, angle * 180 / PI, 1e-7);
    }



    @Test
    public void testDiffractionIntensity() {
        double intensity;
        double expected;

        // Planes
        Vector3D plane1 = new Vector3D(1, 1, 1);
        Vector3D plane2 = new Vector3D(1, 0, 1);

        // FCC
        intensity =
                Calculations.diffractionIntensity(plane1, cubic, atomsfcc,
                        scatter);
        expected = 757.78198507326738;
        assertEquals(expected, intensity, 1e-7);

        intensity =
                Calculations.diffractionIntensity(plane2, cubic, atomsfcc,
                        scatter);
        expected = 0.0;
        assertEquals(expected, intensity, 1e-7);

        // BCC
        intensity =
                Calculations.diffractionIntensity(plane1, cubic, atomsbcc,
                        scatter);
        expected = 0.0;
        assertEquals(expected, intensity, 1e-7);

        intensity =
                Calculations.diffractionIntensity(plane2, cubic, atomsbcc,
                        scatter);
        expected = 234.69367215181771;
        assertEquals(expected, intensity, 1e-7);

        // HCP
        intensity =
                Calculations.diffractionIntensity(plane1, cubic, atomshcp,
                        scatter);
        expected = 0.0;
        assertEquals(expected, intensity, 1e-7);

        intensity =
                Calculations.diffractionIntensity(plane2, cubic, atomshcp,
                        scatter);
        expected = 176.02025411386322;
        assertEquals(expected, intensity, 1e-7);
    }



    @Test
    public void testElectronWavelength() {
        // Wikipedia
        assertEquals(0.12031075249234069,
                Calculations.electronWavelength(10e3), 1e-7);
        assertEquals(0.020538907051308845,
                Calculations.electronWavelength(200e3), 1e-7);
    }



    @Test
    public void testEquivalentPositions() {
        AtomSites atoms = new AtomSites();
        atoms.add(new AtomSite(14, 0.0, 0.0, 0.0));
        AtomSites results =
                Calculations.equivalentPositions(atoms, SpaceGroups2.SG216);

        assertEquals(4, results.size());
        assertEquals(atomsfcc, results, 1e-6);
    }



    @Test
    public void testFormFactor() {
        Complex f;
        Complex expected;

        // Planes
        Vector3D plane1 = new Vector3D(1, 1, 1);
        Vector3D plane2 = new Vector3D(1, 0, 1);

        // FCC
        f = Calculations.formFactor(plane1, cubic, atomsfcc, scatter);
        expected = new Complex(27.527840181773566, 0.0);
        assertEquals(expected.getReal(), f.getReal(), 1e-7);
        assertEquals(expected.getImaginary(), f.getImaginary(), 1e-7);

        f = Calculations.formFactor(plane2, cubic, atomsfcc, scatter);
        expected = new Complex(0.0, 0.0);
        assertEquals(expected.getReal(), f.getReal(), 1e-7);
        assertEquals(expected.getImaginary(), f.getImaginary(), 1e-7);

        // BCC
        f = Calculations.formFactor(plane1, cubic, atomsbcc, scatter);
        expected = new Complex(0.0, 0.0);
        assertEquals(expected.getReal(), f.getReal(), 1e-7);
        assertEquals(expected.getImaginary(), f.getImaginary(), 1e-7);

        f = Calculations.formFactor(plane2, cubic, atomsbcc, scatter);
        expected = new Complex(15.319715145909786, 0.0);
        assertEquals(expected.getReal(), f.getReal(), 1e-7);
        assertEquals(expected.getImaginary(), f.getImaginary(), 1e-7);

        // HCP
        f = Calculations.formFactor(plane1, hexagonal, atomshcp, scatter);
        expected = new Complex(0.0, 0.0);
        assertEquals(expected.getReal(), f.getReal(), 1e-7);
        assertEquals(expected.getImaginary(), f.getImaginary(), 1e-7);

        f = Calculations.formFactor(plane2, hexagonal, atomshcp, scatter);
        expected = new Complex(11.800945464186695, -6.8132790404402881);
        assertEquals(expected.getReal(), f.getReal(), 1e-7);
        assertEquals(expected.getImaginary(), f.getImaginary(), 1e-7);
    }



    @Test
    public void testInterplanarAngle() {
        // Problem 2.15
        Vector3D plane1 = new Vector3D(1.0, 0.0, 0.0);
        Vector3D plane2 = new Vector3D(1.0, 1.0, 0.0);

        double angle = Calculations.interplanarAngle(plane1, plane2, unitCell4);
        angle *= 180 / PI;
        assertEquals(44.7387, angle, 1e-3);
    }



    @Test
    public void testInterplanarAngleCubic() {
        for (Vector3D plane1 : planes)
            for (Vector3D plane2 : planes) {
                // Interplanar angle from Calculations.interplanarAngle()
                double angle =
                        Calculations.interplanarAngle(plane1, plane2, cubic);

                // Calculation using equation
                double h1 = plane1.getX();
                double h2 = plane2.getX();
                double k1 = plane1.getY();
                double k2 = plane2.getY();
                double l1 = plane1.getZ();
                double l2 = plane2.getZ();

                double cosangle =
                        (h1 * h2 + k1 * k2 + l1 * l2)
                                / sqrt((pow(h1, 2) + pow(k1, 2) + pow(l1, 2))
                                        * (pow(h2, 2) + pow(k2, 2) + pow(l2, 2)));
                double expected = acos(cosangle);

                // Test
                assertEquals(expected, angle, 1e-7);
            }
    }



    @Test
    public void testInterplanarAngleHexagonal() {
        for (Vector3D plane1 : planes)
            for (Vector3D plane2 : planes) {
                // Interplanar angle from Calculations.interplanarAngle()
                double angle =
                        Calculations.interplanarAngle(plane1, plane2, hexagonal);

                // Calculation using equation
                double h1 = plane1.getX();
                double h2 = plane2.getX();
                double k1 = plane1.getY();
                double k2 = plane2.getY();
                double l1 = plane1.getZ();
                double l2 = plane2.getZ();

                double a = hexagonal.a;
                double c = hexagonal.c;

                double cosangle =
                        (h1 * h2 + k1 * k2 + 0.5 * (h1 * k2 + h2 * k1) + (3 * pow(
                                a, 2)) / (4 * pow(c, 2)) * l1 * l2)
                                / sqrt((pow(h1, 2) + pow(k1, 2) + h1 * k1 + (3 * pow(
                                        a, 2)) / (4 * pow(c, 2)) * pow(l1, 2))
                                        * (pow(h2, 2) + pow(k2, 2) + h2 * k2 + (3 * pow(
                                                a, 2))
                                                / (4 * pow(c, 2))
                                                * pow(l2, 2)));
                double expected = acos(cosangle);

                // Test
                assertEquals(expected, angle, 1e-7);
            }
    }



    @Test
    public void testInterplanarAngleMonoclinic() {
        for (Vector3D plane1 : planes)
            for (Vector3D plane2 : planes) {
                // Interplanar angle from Calculations.interplanarAngle()
                double angle =
                        Calculations.interplanarAngle(plane1, plane2,
                                monoclinic);

                // Calculation using equation
                double h1 = plane1.getX();
                double h2 = plane2.getX();
                double k1 = plane1.getY();
                double k2 = plane2.getY();
                double l1 = plane1.getZ();
                double l2 = plane2.getZ();

                double a = monoclinic.a;
                double b = monoclinic.b;
                double c = monoclinic.c;
                double beta = monoclinic.beta;

                double d1 = Calculations.planeSpacing(plane1, monoclinic);
                double d2 = Calculations.planeSpacing(plane2, monoclinic);

                double cosangle =
                        d1
                                * d2
                                / pow(sin(beta), 2)
                                * (h1 * h2 / pow(a, 2) + k1 * k2
                                        * pow(sin(beta), 2) / pow(b, 2) + l1
                                        * l2 / pow(c, 2) - (l1 * h2 + l2 * h1)
                                        * cos(beta) / (a * c));
                double expected = acos(cosangle);

                // Test
                assertEquals(expected, angle, 1e-7);
            }
    }



    @Test
    public void testInterplanarAngleOrthorhombic() {
        for (Vector3D plane1 : planes)
            for (Vector3D plane2 : planes) {
                // Interplanar angle from Calculations.interplanarAngle()
                double angle =
                        Calculations.interplanarAngle(plane1, plane2,
                                orthorhombic);

                // Calculation using equation
                double h1 = plane1.getX();
                double h2 = plane2.getX();
                double k1 = plane1.getY();
                double k2 = plane2.getY();
                double l1 = plane1.getZ();
                double l2 = plane2.getZ();

                double a = orthorhombic.a;
                double b = orthorhombic.b;
                double c = orthorhombic.c;

                double cosangle =
                        (h1 * h2 / pow(a, 2) + k1 * k2 / pow(b, 2) + l1 * l2
                                / pow(c, 2))
                                / sqrt((pow(h1, 2) / pow(a, 2) + pow(k1, 2)
                                        / pow(b, 2) + pow(l1, 2) / pow(c, 2))
                                        * (pow(h2, 2) / pow(a, 2) + pow(k2, 2)
                                                / pow(b, 2) + pow(l2, 2)
                                                / pow(c, 2)));
                double expected = acos(cosangle);

                // Test
                assertEquals(expected, angle, 1e-7);
            }
    }



    @Test
    public void testInterplanarAngleTetragonal() {
        for (Vector3D plane1 : planes)
            for (Vector3D plane2 : planes) {
                // Interplanar angle from Calculations.interplanarAngle()
                double angle =
                        Calculations.interplanarAngle(plane1, plane2,
                                tetragonal);

                // Calculation using equation
                double h1 = plane1.getX();
                double h2 = plane2.getX();
                double k1 = plane1.getY();
                double k2 = plane2.getY();
                double l1 = plane1.getZ();
                double l2 = plane2.getZ();

                double a = tetragonal.a;
                double c = tetragonal.c;

                double cosangle =
                        ((h1 * h2 + k1 * k2) / pow(a, 2) + l1 * l2 / pow(c, 2))
                                / sqrt(((pow(h1, 2) + pow(k1, 2)) / pow(a, 2) + pow(
                                        l1, 2) / pow(c, 2))
                                        * ((pow(h2, 2) + pow(k2, 2))
                                                / pow(a, 2) + pow(l2, 2)
                                                / pow(c, 2)));
                double expected = acos(cosangle);

                // Test
                assertEquals(expected, angle, 1e-7);
            }
    }



    @Test
    public void testInterplanarAngleTriclinic() {
        for (Vector3D plane1 : planes)
            for (Vector3D plane2 : planes) {
                // Interplanar angle from Calculations.interplanarAngle()
                double angle =
                        Calculations.interplanarAngle(plane1, plane2, triclinic);

                // Calculation using equation
                double h1 = plane1.getX();
                double h2 = plane2.getX();
                double k1 = plane1.getY();
                double k2 = plane2.getY();
                double l1 = plane1.getZ();
                double l2 = plane2.getZ();

                double a = triclinic.a;
                double b = triclinic.b;
                double c = triclinic.c;
                double alpha = triclinic.alpha;
                double beta = triclinic.beta;
                double gamma = triclinic.gamma;

                double s11 = pow(b, 2) * pow(c, 2) * pow(sin(alpha), 2);
                double s22 = pow(a, 2) * pow(c, 2) * pow(sin(beta), 2);
                double s33 = pow(a, 2) * pow(b, 2) * pow(sin(gamma), 2);
                double s12 =
                        a * b * pow(c, 2)
                                * (cos(alpha) * cos(beta) - cos(gamma));
                double s23 =
                        pow(a, 2) * b * c
                                * (cos(beta) * cos(gamma) - cos(alpha));
                double s13 =
                        a * pow(b, 2) * c
                                * (cos(gamma) * cos(alpha) - cos(beta));

                double v =
                        a
                                * b
                                * c
                                * sqrt(1 - pow(cos(alpha), 2)
                                        - pow(cos(beta), 2)
                                        - pow(cos(gamma), 2) + 2 * cos(alpha)
                                        * cos(beta) * cos(gamma));

                double d1 = Calculations.planeSpacing(plane1, triclinic);
                double d2 = Calculations.planeSpacing(plane2, triclinic);

                double cosangle =
                        d1
                                * d2
                                / pow(v, 2)
                                * (s11 * h1 * h2 + s22 * k1 * k2 + s33 * l1
                                        * l2 + s23 * (k1 * l2 + k2 * l1) + s13
                                        * (l1 * h2 + l2 * h1) + s12
                                        * (h1 * k2 + h2 * k1));
                double expected = acos(cosangle);

                // Test
                assertEquals(expected, angle, 1e-7);
            }
    }



    @Test
    public void testInterplanarAngleTrigonal() {
        for (Vector3D plane1 : planes)
            for (Vector3D plane2 : planes) {
                // Interplanar angle from Calculations.interplanarAngle()
                double angle =
                        Calculations.interplanarAngle(plane1, plane2, trigonal);

                // Calculation using equation
                double h1 = plane1.getX();
                double h2 = plane2.getX();
                double k1 = plane1.getY();
                double k2 = plane2.getY();
                double l1 = plane1.getZ();
                double l2 = plane2.getZ();

                double a = trigonal.a;
                double alpha = trigonal.alpha;

                double v =
                        pow(a, 3)
                                * sqrt(1 - 3 * pow(cos(alpha), 2) + 2
                                        * pow(cos(alpha), 3));
                double d1 = Calculations.planeSpacing(plane1, trigonal);
                double d2 = Calculations.planeSpacing(plane2, trigonal);

                double cosangle =
                        (pow(a, 4) * d1 * d2)
                                / pow(v, 2)
                                * (pow(sin(alpha), 2)
                                        * (h1 * h2 + k1 * k2 + l1 * l2) + (pow(
                                        cos(alpha), 2) - cos(alpha))
                                        * (k1 * l2 + k2 * l1 + l1 * h2 + l2
                                                * h1 + h1 * k2 + h2 * k1));
                double expected = acos(cosangle);

                // Test
                assertEquals(expected, angle, 1e-7);
            }
    }



    @Test
    public void testIsDiffracting() {
        // Planes
        Vector3D plane1 = new Vector3D(1, 1, 1);
        Vector3D plane2 = new Vector3D(1, 0, 1);

        // FCC
        assertTrue(Calculations.isDiffracting(plane1, cubic, atomsfcc, scatter,
                1e-14));
        assertFalse(Calculations.isDiffracting(plane2, cubic, atomsfcc,
                scatter, 1e-14));

        // BCC
        assertFalse(Calculations.isDiffracting(plane1, cubic, atomsbcc,
                scatter, 1e-14));
        assertTrue(Calculations.isDiffracting(plane2, cubic, atomsbcc, scatter,
                1e-14));

        // HCP
        assertFalse(Calculations.isDiffracting(plane1, hexagonal, atomshcp,
                scatter, 1e-14));
        assertTrue(Calculations.isDiffracting(plane2, hexagonal, atomshcp,
                scatter, 1e-14));
    }



    @Test
    public void testMaximumDiffractionIntensity() {
        double maximum;
        double expected;

        // FCC
        maximum =
                Calculations.maximumDiffractionIntensity(cubic, atomsfcc,
                        scatter);
        expected = pow(55.9904, 2);
        assertEquals(expected, maximum, 1e-7);

        // BCC
        maximum =
                Calculations.maximumDiffractionIntensity(cubic, atomsbcc,
                        scatter);
        expected = pow(27.9952, 2);
        assertEquals(expected, maximum, 1e-7);

        // HCP
        maximum =
                Calculations.maximumDiffractionIntensity(hexagonal, atomshcp,
                        scatter);
        expected = pow(27.9952, 2);
        assertEquals(expected, maximum, 1e-7);
    }



    @Test
    public void testMaximumFormFactor() {
        Complex maximum;
        Complex expected;

        // FCC
        maximum = Calculations.maximumFormFactor(cubic, atomsfcc, scatter);
        expected = new Complex(55.9904, 0.0);
        assertEquals(expected.getReal(), maximum.getReal(), 1e-7);
        assertEquals(expected.getImaginary(), maximum.getImaginary(), 1e-7);

        // BCC
        maximum = Calculations.maximumFormFactor(cubic, atomsbcc, scatter);
        expected = new Complex(27.9952, 0.0);
        assertEquals(expected.getReal(), maximum.getReal(), 1e-7);
        assertEquals(expected.getImaginary(), maximum.getImaginary(), 1e-7);

        // HCP
        maximum = Calculations.maximumFormFactor(cubic, atomshcp, scatter);
        expected = new Complex(27.9952, 0.0);
        assertEquals(expected.getReal(), maximum.getReal(), 1e-7);
        assertEquals(expected.getImaginary(), maximum.getImaginary(), 1e-7);
    }



    @Test
    public void testPlaneSpacing() {
        // Example 2.3
        Vector3D plane = new Vector3D(3, 1, 2);
        double planeSpacing = Calculations.planeSpacing(plane, unitCell2);
        assertEquals(1.964, planeSpacing, 1e-3);
    }



    @Test
    public void testPlaneSpacingCubic() {
        for (Vector3D plane : planes) {
            // Plane spacing from Calculations.planeSpacing()
            double spacing = Calculations.planeSpacing(plane, cubic);

            // Calculation using equation
            double h = plane.getX();
            double k = plane.getY();
            double l = plane.getZ();

            double a = cubic.a;

            double x = (pow(h, 2) + pow(k, 2) + pow(l, 2)) / pow(a, 2);
            double expected = 1.0 / sqrt(x);

            // Test
            assertEquals(expected, spacing, 1e-7);
        }
    }



    @Test
    public void testPlaneSpacingHexagonal() {
        for (Vector3D plane : planes) {
            // Plane spacing from Calculations.planeSpacing()
            double spacing = Calculations.planeSpacing(plane, hexagonal);

            // Calculation using equation
            double h = plane.getX();
            double k = plane.getY();
            double l = plane.getZ();

            double a = hexagonal.a;
            double c = hexagonal.c;

            double x =
                    4.0 / 3.0 * ((pow(h, 2) + h * k + pow(k, 2)) / pow(a, 2))
                            + pow(l, 2) / pow(c, 2);
            double expected = 1.0 / sqrt(x);

            // Test
            assertEquals(expected, spacing, 1e-7);
        }
    }



    @Test
    public void testPlaneSpacingMonoclinic() {
        for (Vector3D plane : planes) {
            // Plane spacing from Calculations.planeSpacing()
            double spacing = Calculations.planeSpacing(plane, monoclinic);

            // Calculation using equation
            double h = plane.getX();
            double k = plane.getY();
            double l = plane.getZ();

            double a = monoclinic.a;
            double b = monoclinic.b;
            double c = monoclinic.c;
            double beta = monoclinic.beta;

            double x =
                    (1.0 / pow(sin(beta), 2))
                            * (pow(h, 2) / pow(a, 2) + pow(k, 2)
                                    * pow(sin(beta), 2) / pow(b, 2) + pow(l, 2)
                                    / pow(c, 2) - 2 * h * l * cos(beta)
                                    / (a * c));
            double expected = 1.0 / sqrt(x);

            // Test
            assertEquals(expected, spacing, 1e-7);
        }
    }



    @Test
    public void testPlaneSpacingOrthorhombic() {
        for (Vector3D plane : planes) {
            // Plane spacing from Calculations.planeSpacing()
            double spacing = Calculations.planeSpacing(plane, orthorhombic);

            // Calculation using equation
            double h = plane.getX();
            double k = plane.getY();
            double l = plane.getZ();

            double a = orthorhombic.a;
            double b = orthorhombic.b;
            double c = orthorhombic.c;

            double x =
                    pow(h, 2) / pow(a, 2) + pow(k, 2) / pow(b, 2) + pow(l, 2)
                            / pow(c, 2);
            double expected = 1.0 / sqrt(x);

            // Test
            assertEquals(expected, spacing, 1e-7);
        }
    }



    @Test
    public void testPlaneSpacingTetragonal() {
        for (Vector3D plane : planes) {
            // Plane spacing from Calculations.planeSpacing()
            double spacing = Calculations.planeSpacing(plane, tetragonal);

            // Calculation using equation
            double h = plane.getX();
            double k = plane.getY();
            double l = plane.getZ();

            double a = tetragonal.a;
            double c = tetragonal.c;

            double x =
                    (pow(h, 2) + pow(k, 2)) / pow(a, 2) + pow(l, 2) / pow(c, 2);
            double expected = 1.0 / sqrt(x);

            // Test
            assertEquals(expected, spacing, 1e-7);
        }
    }



    @Test
    public void testPlaneSpacingTriclinic() {
        for (Vector3D plane : planes) {
            // Plane spacing from Calculations.planeSpacing()
            double spacing = Calculations.planeSpacing(plane, triclinic);

            // Calculation using equation
            double h = plane.getX();
            double k = plane.getY();
            double l = plane.getZ();

            double a = triclinic.a;
            double b = triclinic.b;
            double c = triclinic.c;
            double alpha = triclinic.alpha;
            double beta = triclinic.beta;
            double gamma = triclinic.gamma;

            double s11 = pow(b, 2) * pow(c, 2) * pow(sin(alpha), 2);
            double s22 = pow(a, 2) * pow(c, 2) * pow(sin(beta), 2);
            double s33 = pow(a, 2) * pow(b, 2) * pow(sin(gamma), 2);
            double s12 =
                    a * b * pow(c, 2) * (cos(alpha) * cos(beta) - cos(gamma));
            double s23 =
                    pow(a, 2) * b * c * (cos(beta) * cos(gamma) - cos(alpha));
            double s13 =
                    a * pow(b, 2) * c * (cos(gamma) * cos(alpha) - cos(beta));
            double v =
                    a
                            * b
                            * c
                            * sqrt(1 - pow(cos(alpha), 2) - pow(cos(beta), 2)
                                    - pow(cos(gamma), 2) + 2 * cos(alpha)
                                    * cos(beta) * cos(gamma));

            double x =
                    (1.0 / pow(v, 2))
                            * (s11 * pow(h, 2) + s22 * pow(k, 2) + s33
                                    * pow(l, 2) + 2 * s12 * h * k + 2 * s23 * k
                                    * l + 2 * s13 * h * l);
            double expected = 1.0 / sqrt(x);

            // Test
            assertEquals(expected, spacing, 1e-7);
        }
    }



    @Test
    public void testPlaneSpacingTrigonal() {
        for (Vector3D plane : planes) {
            // Plane spacing from Calculations.planeSpacing()
            double spacing = Calculations.planeSpacing(plane, trigonal);

            // Calculation using equation
            double h = plane.getX();
            double k = plane.getY();
            double l = plane.getZ();

            double a = trigonal.a;
            double alpha = trigonal.alpha;

            double x =
                    ((pow(h, 2) + pow(k, 2) + pow(l, 2)) * pow(sin(alpha), 2) + 2
                            * (h * k + k * l + h * l)
                            * (pow(cos(alpha), 2) - cos(alpha)))
                            / (pow(a, 2) * (1 - 3 * pow(cos(alpha), 2) + 2 * pow(
                                    cos(alpha), 3)));
            double expected = 1.0 / sqrt(x);

            // Test
            assertEquals(expected, spacing, 1e-7);
        }
    }



    @Test
    public void testReduce() throws NotARotationMatrixException {
        // From A.D. Rollett, Misorientations and Grain Boundaries, Lectures
        // Slides, 2007
        double[][] m =
                new double[][] { { -0.667, 0.333, 0.667 },
                        { 0.333, -0.667, 0.667 }, { 0.667, 0.667, 0.333 } };
        Rotation q = new Rotation(m, 1e-3);

        Rotation out = Calculations.reduce(q, LaueGroup.LGm3m);

        assertEquals(60.0 / 180 * PI, out.getAngle(), 1e-3);
        // axis should be in the {111} plane family
        Vector3D axis = out.getAxis().normalize();
        assertEquals(sqrt(3) / 3.0, abs(axis.getX()), 1e-3);
        assertEquals(sqrt(3) / 3.0, abs(axis.getY()), 1e-3);
        assertEquals(sqrt(3) / 3.0, abs(axis.getZ()), 1e-3);
    }



    @Test
    public void testZoneAxis() {
        Vector3D plane1;
        Vector3D plane2;
        Vector3D zone;
        Vector3D expectedZone;

        // Example 2.16
        plane1 = new Vector3D(-0.0963, 0.1243, 0.2018);
        plane2 = new Vector3D(0.1084, -0.0880, 0.2947);

        zone = Calculations.zoneAxis(plane1, plane2, unitCell3);
        expectedZone = new Vector3D(1.0219, 0.8478, 0.0888);

        assertEquals(expectedZone, zone, 1e-3);

        // Problem 2.9
        plane1 = new Vector3D(0.1166, 0.0968, 0.0101);
        plane2 = new Vector3D(-0.0286, 0.0369, 0.0599);

        zone = Calculations.zoneAxis(plane1, plane2, unitCell3);
        expectedZone = new Vector3D(0.0895, -0.097, -0.0030);

        assertEquals(expectedZone, zone, 1e-3);
    }
}
