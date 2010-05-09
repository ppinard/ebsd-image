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

import net.jcip.annotations.Immutable;

/**
 * Defines a diffracting plane. The difference between the object
 * <code>Plane</code> and <code>Reflector</code> is that the
 * <code>Reflector</code> stores the plane spacing and diffracting intensity of
 * the crystallographic plane. Also by definition, the diffracting intensity of
 * a reflector is greater than 0.0 (since it is diffracting) and two reflectors
 * are equal if their <code>plane</code> are equal.
 * <p/>
 * The plane, plane spacing, intensity and normalized intensity can be accessed
 * via the public final variables <code>plane</code>, <code>planeSpacing</code>,
 * <code>intensity</code> and <code>normalizedIntensity</code> respectively.
 * 
 * @author Philippe T. Pinard
 * 
 */
@Immutable
public class Reflector {

    /** Crystallographic plane. */
    public final Plane plane;

    /** Spacing between planes. */
    public final double planeSpacing;

    /** Diffraction intensity. */
    public final double intensity;

    /**
     * Normalized diffraction intensity of the reflector (i.e.
     * <code>max=1</code>)
     */
    public final double normalizedIntensity;



    /**
     * Creates a new <code>Reflector</code>.
     * 
     * @param plane
     *            crystallographic plane
     * @param crystal
     *            crystal containing the plane
     * @param scatter
     *            scattering coefficient to calculate the diffraction intensity
     * 
     * @throws NullPointerException
     *             if the plane is null
     * @throws IllegalArgumentException
     *             if the plane spacing is less or equal to 0.
     * @throws IllegalArgumentException
     *             if the intensity is less or equal to 0.
     * @throws IllegalArgumentException
     *             if the normalized intensity is less or equal to 0.
     */
    public Reflector(Plane plane, Crystal crystal, ScatteringFactors scatter) {
        this(plane, Calculations.planeSpacing(plane, crystal.unitCell),
                Calculations.diffractionIntensity(plane, crystal.unitCell,
                        crystal.atoms, scatter));
    }



    /**
     * Creates a new <code>Reflector</code>. Protected constructor. Reflectors
     * should be created using {@link Reflectors}. The normalized intensity is
     * set to 1.0.
     * 
     * @param plane
     *            crystallographic plane
     * @param planeSpacing
     *            spacing between planes
     * @param intensity
     *            diffraction intensity
     */
    protected Reflector(Plane plane, double planeSpacing, double intensity) {
        this(plane, planeSpacing, intensity, 1.0);
    }



    /**
     * Creates a new <code>Reflector</code>. Protected constructor. Reflectors
     * should be created using {@link Reflectors}. *
     * 
     * @param plane
     *            crystallographic plane
     * @param planeSpacing
     *            plane spacing
     * @param intensity
     *            diffraction intensity
     * @param normalizedIntensity
     *            normalized diffraction intensity
     * 
     * @throws NullPointerException
     *             if the plane is null
     * @throws IllegalArgumentException
     *             if the plane spacing is less or equal to 0.
     * @throws IllegalArgumentException
     *             if the intensity is less or equal to 0.
     * @throws IllegalArgumentException
     *             if the normalized intensity is less or equal to 0.
     */
    protected Reflector(Plane plane, double planeSpacing, double intensity,
            double normalizedIntensity) {
        if (plane == null)
            throw new NullPointerException("Plane cannot be null.");
        if (planeSpacing <= 0)
            throw new IllegalArgumentException(
                    "The plane spacing cannot be less or equal to 0.0.");
        if (intensity <= 0)
            throw new IllegalArgumentException(
                    "The plane spacing cannot be less or equal to 0.0.");
        if (normalizedIntensity <= 0)
            throw new IllegalArgumentException(
                    "The plane spacing cannot be less or equal to 0.0.");

        this.plane = plane;
        this.planeSpacing = planeSpacing;
        this.intensity = intensity;
        this.normalizedIntensity = normalizedIntensity;
    }



    /**
     * Creates a new <code>Reflector</code> from an old <code>Reflector</code>
     * and updates the normalized intensity.
     * 
     * @param refl
     *            old <code>Reflector</code>
     * @param normalizedIntensity
     *            updated normalized diffraction intensity
     * 
     * @throws NullPointerException
     *             if the plane is null
     * @throws IllegalArgumentException
     *             if the plane spacing is less or equal to 0.
     * @throws IllegalArgumentException
     *             if the intensity is less or equal to 0.
     * @throws IllegalArgumentException
     *             if the normalized intensity is less or equal to 0.
     */
    protected Reflector(Reflector refl, double normalizedIntensity) {
        this(refl.plane, refl.planeSpacing, refl.intensity, normalizedIntensity);
    }



    /**
     * Checks if the plane of this <code>Reflector</code> is equal to the plane
     * of the specified one.
     * 
     * @param obj
     *            other <code>Reflector</code> to check equality
     * 
     * @return whether the planes of the two <code>Reflector</code> are equal
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;

        Reflector other = (Reflector) obj;
        if (!plane.equals(other.plane))
            return false;

        return true;
    }



    /**
     * Checks if the plane of this <code>Reflector</code> is equal to the
     * specified plane.
     * 
     * @param plane
     *            <code>Plane</code> to check equality
     * 
     * @return whether the planes are equal
     */
    public boolean equals(Plane plane) {
        if (plane == null)
            return false;

        if (!this.plane.equals(plane))
            return false;

        return true;
    }



    /**
     * Returns the hash code of the reflector. The hash code of the reflector is
     * equal to the hash code of the plane.
     * 
     * @return hash code
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + plane.hashCode();
        return result;
    }



    /**
     * Returns a representation of the <code>Reflector</code>.
     * 
     * @return information about the <code>Reflector</code>
     */
    @Override
    public String toString() {
        return plane + "\t" + planeSpacing + " A" + "\t" + normalizedIntensity
                * 100 + "%";
    }

}
