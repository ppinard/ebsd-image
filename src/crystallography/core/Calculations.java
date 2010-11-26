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

import static java.lang.Math.*;
import static ptpshared.core.math.Constants.C;
import static ptpshared.core.math.Constants.CHARGE_ELECTRON;
import static ptpshared.core.math.Constants.H;
import static ptpshared.core.math.Constants.MASS_ELECTRON;

import org.apache.commons.math.complex.Complex;

import ptpshared.core.math.Matrix3D;
import ptpshared.core.math.Quaternion;
import ptpshared.core.math.Vector3D;
import ptpshared.core.math.Vector3DMath;
import edu.umd.cs.findbugs.annotations.CheckReturnValue;

/**
 * Operations on <code>AtomSite</code>, <code>AtomSites</code>,
 * <code>UnitCell</code>, <code>Plane</code>. Common crystallographic
 * calculations that are used in other classes.
 * <p/>
 * References for some of these calculations were taken from:
 * <ul>
 * <li>Mathematical Crystallography</li>
 * </ul>
 * 
 * @author Philippe T. Pinard
 */
public class Calculations {

    /**
     * Returns the bond angle (in radians) between the vector from
     * <code>atom1</code> to <code>atom2</code> and the vector from
     * <code>atom1</code> to <code>atom3</code>.
     * <p/>
     * <b>References:</b>
     * <ul>
     * <li>Equation 1.13 from Mathematical Crystallography</li>
     * </ul>
     * 
     * @param atom1
     *            middle atom
     * @param atom2
     *            end atom
     * @param atom3
     *            end atom
     * @param unitCell
     *            unit cell of the atoms
     * @return bond angle (in radians)
     */
    public static double bondAngle(AtomSite atom1, AtomSite atom2,
            AtomSite atom3, UnitCell unitCell) {
        Vector3D atom1Vector = atom1.position;
        Vector3D atom2Vector = atom2.position;
        Vector3D atom3Vector = atom3.position;
        Matrix3D metricalMatrix = unitCell.metricalMatrix;

        // Inter-atoms vectors
        Vector3D vector12 = new Vector3D(atom1Vector.minus(atom2Vector));
        Vector3D vector13 = new Vector3D(atom1Vector.minus(atom3Vector));

        // Dot product
        double dotproduct = vector12.dot(metricalMatrix.multiply(vector13));

        // Bond distance
        double bonddistance12 = bondDistance(atom1, atom2, unitCell);
        double bonddistance13 = bondDistance(atom1, atom3, unitCell);

        // Cosine
        double cosine = dotproduct / (bonddistance12 * bonddistance13);

        // Angle
        double angle = acos(cosine);

        return angle;
    }



    /**
     * Returns the distance between <code>atom1</code> and <code>atom2</code> of
     * a given <code>unitCell</code>.
     * <p/>
     * <b>References:</b>
     * <ul>
     * <li>Equation 1.13 from Mathematical Crystallography</li>
     * </ul>
     * 
     * @param atom1
     *            first atom
     * @param atom2
     *            second atom
     * @param unitCell
     *            unit cell of the atoms
     * @return bond distance
     */
    public static double bondDistance(AtomSite atom1, AtomSite atom2,
            UnitCell unitCell) {
        Vector3D atom1vector = atom1.position;
        Vector3D atom2vector = atom2.position;
        Matrix3D metricalMatrix = unitCell.metricalMatrix;

        // Vector between atom1 and atom2
        Vector3D vector = new Vector3D(atom2vector.minus(atom1vector));

        // Distance square
        double distanceSquare = vector.dot(metricalMatrix.multiply(vector));

        // Distance
        double distance = sqrt(distanceSquare);

        return distance;
    }



    /**
     * Returns the first order (n=1) diffraction angle based on Bragg's Law.
     * 
     * @param planeSpacing
     *            plane spacing of diffracting plane
     * @param wavelength
     *            wavelength of wave
     * @return diffraction angle (in radians)
     */
    public static double diffractionAngle(double planeSpacing, double wavelength) {
        return diffractionAngle(planeSpacing, wavelength, 1.0);
    }



    /**
     * Returns the diffraction angle based on Bragg's Law.
     * 
     * @param planeSpacing
     *            plane spacing of diffracting plane
     * @param wavelength
     *            wavelength of wave
     * @param order
     *            diffraction order
     * @return diffraction angle (in radians)
     */
    public static double diffractionAngle(double planeSpacing,
            double wavelength, double order) {
        return asin(order * wavelength / (2 * planeSpacing));
    }



    /**
     * Returns the diffraction intensity (I) for a given plane, set of atoms and
     * scattering factors.
     * 
     * @param plane
     *            crystallography plane
     * @param unitCell
     *            unit cell containing the plane
     * @param atomSites
     *            atoms contained in the unit cell
     * @param scatteringFactors
     *            scattering factors to calculate for the form factor
     * @return diffraction intensity
     */
    public static double diffractionIntensity(Plane plane, UnitCell unitCell,
            AtomSites atomSites, ScatteringFactors scatteringFactors) {
        Complex formFactor =
                formFactor(plane, unitCell, atomSites, scatteringFactors);

        double intensity =
                formFactor.multiply(formFactor.conjugate()).getReal();

        return intensity;
    }



    /**
     * Returns the relativistic electron wavelength.
     * 
     * @param energy
     *            energy (in eV)
     * @return electron wavelength (in angstroms)
     */
    public static double electronWavelength(double energy) {
        double a = H / sqrt(2 * MASS_ELECTRON * CHARGE_ELECTRON);
        double b = 2 * CHARGE_ELECTRON / (MASS_ELECTRON * pow(C, 2));

        return a / sqrt(energy + b * pow(energy, 2)) * 1e10;
    }



    /**
     * Applies the space group symmetry on the specified atom positions and
     * returns all the possible equivalent atom positions.
     * 
     * @param atomSites
     *            positions of atoms
     * @param spaceGroup
     *            space group
     * @return all possible atom positions
     */
    @CheckReturnValue
    public static AtomSites equivalentPositions(AtomSites atomSites,
            SpaceGroup spaceGroup) {
        AtomSites newAtomSites = new AtomSites();

        AtomSite newAtom;
        for (AtomSite atom : atomSites) {
            for (Generator generator : spaceGroup.getGenerators()) {
                newAtom = generator.apply(atom);

                try {
                    newAtomSites.add(newAtom);
                } catch (AtomSitePositionException ex) {
                    continue;
                }
            }
        }

        return newAtomSites;
    }



    /**
     * Returns the form factor (F) for a given plane, set of atoms and
     * scattering factors.
     * 
     * @param plane
     *            crystallography plane
     * @param unitCell
     *            unit cell containing the plane
     * @param atomSites
     *            atoms contained in the unit cell
     * @param scatteringFactors
     *            scattering factors to calculate for the form factor
     * @return form factor (complex form)
     */
    public static Complex formFactor(Plane plane, UnitCell unitCell,
            AtomSites atomSites, ScatteringFactors scatteringFactors) {
        Complex f = new Complex(0.0, 0.0); // Form factor
        double d = planeSpacing(plane, unitCell);

        for (AtomSite atom : atomSites) {
            double fi =
                    scatteringFactors.getFromPlaneSpacing(atom.atomicNumber, d)
                            * atom.occupancy;
            Complex x = new Complex(0.0, 2 * PI * plane.dot(atom.position));

            f = f.add(x.exp().multiply(fi)); // f += fi * exp(x);
        }

        return f;
    }



    /**
     * Returns the interplanar direction cosine between <code>plane1</code> and
     * <code>plane2</code> of a unit cell.
     * 
     * @param plane1
     *            first crystallographic plane
     * @param plane2
     *            second crystallographic plane
     * @param unitCell
     *            unitCell unit cell containing the planes
     * @return angle between plane1 and plane2 (in radians)
     */
    public static double interplanarAngle(Plane plane1, Plane plane2,
            UnitCell unitCell) {
        return Vector3DMath.angle(interplanarDirectionCosine(plane1, plane2,
                unitCell));
    }



    /**
     * Returns the interplanar direction cosine between <code>plane1</code> and
     * <code>plane2</code> of a unit cell.
     * 
     * @param plane1
     *            first crystallographic plane
     * @param plane2
     *            second crystallographic plane
     * @param unitCell
     *            unitCell unit cell containing the planes
     * @return direction cosine between plane1 and plane2 (in radians)
     */
    public static double interplanarDirectionCosine(Plane plane1, Plane plane2,
            UnitCell unitCell) {
        Matrix3D cartesianMatrix = unitCell.cartesianMatrix;
        Matrix3D b = cartesianMatrix.transpose().inverse();

        Vector3D plane1C = b.multiply(plane1.toVector3D());
        Vector3D plane2C = b.multiply(plane2.toVector3D());

        return Vector3DMath.directionCosine(plane1C, plane2C);
    }



    /**
     * Check if a plane is diffracting. The intensity has to be greater than
     * fraction * (maximum intensity) to be diffracting.
     * 
     * @param intensity
     *            intensity of the plane
     * @param maxIntensity
     *            maximum intensity of a set of atoms and unit cell
     * @param fraction
     *            discriminating fraction to say that a plane is diffracting
     * @return <code>true</code> if intensity is greater than fraction *
     *         (maximum intensity)
     */
    protected static boolean isDiffracting(double intensity,
            double maxIntensity, double fraction) {
        double lowerlimit = fraction * maxIntensity;

        return intensity > lowerlimit;
    }



    /**
     * Checks if a plane is diffracting for a given set of atom sites and the
     * specified scattering factors.
     * <p/>
     * The intensity has to be greater than fraction * (maximum intensity) to be
     * diffracting.
     * 
     * @param plane
     *            crystallography plane
     * @param unitCell
     *            unit cell containing the plane
     * @param atomSites
     *            atoms contained in the unit cell
     * @param scatteringFactors
     *            scattering factors to calculate for the form factor
     * @param fraction
     *            discriminating fraction to say that a plane is diffracting
     * @return <code>true</code> plane is diffraction
     */
    public static boolean isDiffracting(Plane plane, UnitCell unitCell,
            AtomSites atomSites, ScatteringFactors scatteringFactors,
            double fraction) {
        double maxIntensity =
                maximumDiffractionIntensity(unitCell, atomSites,
                        scatteringFactors);
        double intensity =
                diffractionIntensity(plane, unitCell, atomSites,
                        scatteringFactors);

        return isDiffracting(intensity, maxIntensity, fraction);

    }



    /**
     * Returns the maximum diffraction intensity (I) for a set of atoms and
     * scattering factors.
     * 
     * @param unitCell
     *            unit cell containing the plane
     * @param atomSites
     *            atoms contained in the unit cell
     * @param scatteringFactors
     *            scattering factors to calculate for the form factor
     * @return maximum diffraction intensity
     */
    public static double maximumDiffractionIntensity(UnitCell unitCell,
            AtomSites atomSites, ScatteringFactors scatteringFactors) {
        Complex formFactor =
                maximumFormFactor(unitCell, atomSites, scatteringFactors);

        double intensity =
                formFactor.multiply(formFactor.conjugate()).getReal();

        return intensity;
    }



    /**
     * Returns the maximum value of the form factor for a given unit cell, atom
     * sites and scattering factors.
     * 
     * @param unitCell
     *            unit cell containing the plane
     * @param atomSites
     *            atoms contained in the unit cell
     * @param scatteringFactors
     *            scattering factors to calculate for the form factor
     * @return maximum form factor (complex form)
     */
    public static Complex maximumFormFactor(UnitCell unitCell,
            AtomSites atomSites, ScatteringFactors scatteringFactors) {
        // NOTE: The maximum form factor is a real number, but a Complex number
        // is used for consistency with the form factor.
        Complex f = new Complex(0.0, 0.0);

        for (AtomSite atom : atomSites) {
            int atomicnumber = atom.atomicNumber;
            double fi = scatteringFactors.getFromS(atomicnumber, 0.0);

            f = f.add(new Complex(fi, 0.0));
        }

        return f;
    }



    /**
     * Returns the plane spacing between two adjacent planes of a unit cell.
     * <p/>
     * <b>References:</b>
     * <ul>
     * <li>Mathematical Crystallography</li>
     * </ul>
     * 
     * @param plane
     *            crystallographic plane
     * @param unitCell
     *            unit cell containing the plane
     * @return plane spacing
     */
    public static double planeSpacing(Plane plane, UnitCell unitCell) {
        Matrix3D matrix = unitCell.metricalMatrix.inverse();

        // s square (d = 1/s^2)
        double sSquare = plane.dot(matrix.multiply(plane.toVector3D()));

        // plane spacing d
        double d = 1.0 / sqrt(sSquare);

        return d;
    }



    /**
     * Reduce a rotation to its fundamental group based on the given point
     * group.
     * 
     * @param q
     *            a rotation
     * @param lg
     *            a Laue group
     * @return reduced rotation
     */
    @CheckReturnValue
    public static Quaternion reduce(Quaternion q, LaueGroup lg) {
        Quaternion equiv = q.clone();

        for (Quaternion op : lg.getOperators()) {
            Quaternion out = q.multiply(op);
            if (out.scalar > equiv.scalar)
                equiv = out;
        }

        return equiv;
    }



    /**
     * Reduce a rotation to its fundamental group based on the given point
     * group.
     * 
     * @param q
     *            a rotation
     * @param sg
     *            a space group
     * @return reduced rotation
     */
    @CheckReturnValue
    public static Quaternion reduce(Quaternion q, SpaceGroup sg) {
        return reduce(q, sg.laueGroup);
    }



    /**
     * Returns the zone axis of <code>plane1</code> and <code>plane2</code> of a
     * unit cell.
     * <p/>
     * <b>References:</b>
     * <ul>
     * <li>Theorem 2.14 from Mathematical Crystallography</li>
     * </ul>
     * 
     * @param plane1
     *            first crystallographic plane
     * @param plane2
     *            second crystallographic plane
     * @param unitCell
     *            unit cell containing the planes
     * @return zone axis created by plane1 and plane2
     */
    public static Vector3D zoneAxis(Vector3D plane1, Vector3D plane2,
            UnitCell unitCell) {
        Matrix3D metricalMatrix = unitCell.metricalMatrix;
        double volumeReciprocal = unitCell.volumeR; // reciprocal volume

        Vector3D s1 = metricalMatrix.multiply(plane1);
        Vector3D s2 = metricalMatrix.multiply(plane2);

        Vector3D crossproduct = s1.cross(s2);

        Vector3D zone = new Vector3D(crossproduct.multiply(volumeReciprocal));

        return zone;
    }

}
