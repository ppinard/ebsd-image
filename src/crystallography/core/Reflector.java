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

import java.io.Serializable;
import java.util.Comparator;

import net.jcip.annotations.Immutable;
import ptpshared.core.geom.Plane;
import ptpshared.core.math.Vector3D;

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
 */
@Immutable
public class Reflector {

    /** Crystallographic plane h index. */
    public final int h;

    /** Crystallographic plane k index. */
    public final int k;

    /** Crystallographic plane l index. */
    public final int l;

    /** Spacing between planes. */
    public final double planeSpacing;

    /** Diffraction intensity. */
    public final double intensity;

    /**
     * Normalised diffraction intensity of the reflector (i.e.
     * <code>max=1</code>)
     */
    public final double normalizedIntensity;



    /**
     * Calculates the hash code (unique integer representation) for the indices.
     * 
     * @param h
     *            h index of the crystallographic plane
     * @param k
     *            k index of the crystallographic plane
     * @param l
     *            l index of the crystallographic plane
     * @return hash code
     */
    protected static int calculateHashCode(int h, int k, int l) {
        final int prime = 31;
        int result = 1;
        result = prime * result + h;
        result = prime * result + k;
        result = prime * result + l;
        return result;
    }



    /**
     * Creates a new <code>Reflector</code>. Protected constructor. Reflectors
     * should be created using {@link Reflectors}. The normalised intensity is
     * set to 1.0.
     * 
     * @param h
     *            h index of the crystallographic plane
     * @param k
     *            k index of the crystallographic plane
     * @param l
     *            l index of the crystallographic plane
     * @param planeSpacing
     *            spacing between planes
     * @param intensity
     *            diffraction intensity
     */
    protected Reflector(int h, int k, int l, double planeSpacing,
            double intensity) {
        this(h, k, l, planeSpacing, intensity, 1.0);
    }



    /**
     * Creates a new <code>Reflector</code>. Protected constructor. Reflectors
     * should be created using {@link ReflectorsFactory}. *
     * 
     * @param h
     *            h index of the crystallographic plane
     * @param k
     *            k index of the crystallographic plane
     * @param l
     *            l index of the crystallographic plane
     * @param planeSpacing
     *            plane spacing
     * @param intensity
     *            diffraction intensity
     * @param normalizedIntensity
     *            normalised diffraction intensity
     * @throws NullPointerException
     *             if the plane is null
     * @throws IllegalArgumentException
     *             if the plane spacing is less or equal to 0.
     * @throws IllegalArgumentException
     *             if the intensity is less or equal to 0.
     * @throws IllegalArgumentException
     *             if the normalised intensity is less or equal to 0.
     */
    protected Reflector(int h, int k, int l, double planeSpacing,
            double intensity, double normalizedIntensity) {
        if (h == 0 && k == 0 && l == 0)
            throw new IllegalArgumentException(
                    "Plane hkl cannot be a null vector.");
        if (planeSpacing <= 0)
            throw new IllegalArgumentException(
                    "The plane spacing cannot be less or equal to 0.0.");
        if (intensity <= 0)
            throw new IllegalArgumentException(
                    "The plane spacing cannot be less or equal to 0.0.");
        if (normalizedIntensity <= 0)
            throw new IllegalArgumentException(
                    "The plane spacing cannot be less or equal to 0.0.");

        this.h = h;
        this.k = k;
        this.l = l;
        this.planeSpacing = planeSpacing;
        this.intensity = intensity;
        this.normalizedIntensity = normalizedIntensity;
    }



    /**
     * Creates a new <code>Reflector</code> from an old <code>Reflector</code>
     * and updates the normalised intensity.
     * 
     * @param refl
     *            old <code>Reflector</code>
     * @param normalizedIntensity
     *            updated normalised diffraction intensity
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
        this(refl.h, refl.k, refl.l, refl.planeSpacing, refl.intensity,
                normalizedIntensity);
    }



    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;

        Reflector other = (Reflector) obj;
        if (h != other.h)
            return false;
        if (k != other.k)
            return false;
        if (l != other.l)
            return false;

        return true;
    }



    /**
     * Check whether this reflector is made out of the specified indices.
     * 
     * @param h
     *            h index of the crystallographic plane
     * @param k
     *            k index of the crystallographic plane
     * @param l
     *            l index of the crystallographic plane
     * @return <code>true</code> if the reflector is made of the specified
     *         indices, <code>false</code> otherwise
     */
    public boolean equals(int h, int k, int l) {
        if (h != this.h)
            return false;
        if (k != this.k)
            return false;
        if (l != this.l)
            return false;

        return true;
    }



    @Override
    public int hashCode() {
        return calculateHashCode(h, k, l);
    }



    /**
     * Returns a representation of the <code>Reflector</code>.
     * 
     * @return information about the <code>Reflector</code>
     */
    @Override
    public String toString() {
        return "(" + h + ";" + k + ";" + l + ")" + "\t" + planeSpacing + " A"
                + "\t" + normalizedIntensity * 100 + "%";
    }



    /**
     * Returns the normal of the crystallographic plane as a
     * <code>Vector3D</code>.
     * 
     * @return normal of the crystallographic plane
     */
    public Vector3D getNormal() {
        return new Vector3D(h, k, l);
    }



    /**
     * Returns a <code>Plane</code> of the reflector. The plane's normal is
     * given by the indices. The plane is centred at the origin.
     * 
     * @return crystallographic plane
     */
    public Plane getPlane() {
        return new Plane(new Vector3D(), getNormal());
    }



    /**
     * Returns the indices of the crystallographic plane using the Bravais
     * notation.
     * 
     * @return indices in the Bravais notation (array of length 4)
     */
    public int[] getBravaisIndices() {
        int i = -(h + k);
        return new int[] { h, k, i, l };
    }



    /**
     * Returns the indices of the crystallographic plane using the Miller
     * notation.
     * 
     * @return indices in the Miller notation (array of length 3)
     */
    public int[] getMillerIndices() {
        return new int[] { h, k, l };
    }
}

/**
 * Comparator for <code>Reflector</code> according to their intensity.
 * 
 * @author Philippe T. Pinard
 */
class IntensityComparator implements Comparator<Reflector>, Serializable {

    @Override
    public int compare(Reflector refl0, Reflector refl1) {
        double intensity0 = refl0.intensity;
        double intensity1 = refl1.intensity;

        if (intensity0 < intensity1)
            return -1;
        else if (intensity0 > intensity1)
            return 1;
        else
            return 0;
    }
}

/**
 * Comparator for <code>Reflector</code> according to their plane spacing.
 * 
 * @author Philippe T. Pinard
 */
class PlaneSpacingComparator implements Comparator<Reflector>, Serializable {

    @Override
    public int compare(Reflector refl0, Reflector refl1) {
        double planeSpacing0 = refl0.planeSpacing;
        double planeSpacing1 = refl1.planeSpacing;

        if (planeSpacing0 < planeSpacing1)
            return -1;
        else if (planeSpacing0 > planeSpacing1)
            return 1;
        else
            return 0;
    }
}