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

import static java.util.Arrays.sort;
import static ptpshared.utility.Arrays.reverse;

import java.util.Arrays;
import java.util.Iterator;

import net.jcip.annotations.Immutable;

/**
 * List of <code>Reflector</code>s of a crystal. The class finds all the
 * diffracting planes of a given crystal and creates the corresponding
 * reflectors.
 * <p/>
 * The list of reflectors is limited by the maximum index of the plane. For
 * example, if the maximum index is equal to 3, the last planes to be evaluated
 * will be {3,3,3) and {-3,-3,-3). The diffraction intensity of the plane is
 * calculated using the specified scattering factors (either
 * {@link ElectronScatteringFactors} or {@link XrayScatteringFactors}). The
 * X-rays scattering factors are more robust. <b>References:</b>
 * <ul>
 * <li>Lecture notes from Raynald Gauvin, McGill University, 2007</li>
 * </ul>
 * 
 * @author Philippe T. Pinard
 */
@Immutable
public class Reflectors implements Iterable<Reflector> {

    /** Array of reflectors. */
    private final Reflector[] reflectors;

    /** Crystal associated with the reflectors. */
    public final Crystal crystal;



    /**
     * Creates a new <code>Reflectors</code> with the given crystal and array of
     * <code>Reflector</code>. Use
     * {@link ReflectorsFactory#generate(Crystal, ScatteringFactors, int)} to
     * create a <code>Reflectors</code>.
     * 
     * @param crystal
     *            crystal
     * @param reflectors
     *            array of reflectors
     */
    protected Reflectors(Crystal crystal, Reflector[] reflectors) {
        if (crystal == null)
            throw new NullPointerException("Crystal cannot be null.");
        if (reflectors == null)
            throw new NullPointerException("Reflectors array cannot be null.");
        for (int i = 0; i < reflectors.length; i++)
            if (reflectors[i] == null)
                throw new NullPointerException("Reflector at position (" + i
                        + ") is null.");

        this.crystal = crystal;
        this.reflectors = reflectors;
    }



    /**
     * Returns the reflector with the specified index.
     * 
     * @param index
     *            index of a reflector in the array.
     * @return a <code>Reflector</code>
     */
    public Reflector get(int index) {
        if (index < 0 || index >= size())
            throw new IllegalArgumentException("Index must be between [0,"
                    + size() + "[");

        return reflectors[index];
    }



    /**
     * Returns a reflector for the given plane.
     * 
     * @param plane
     *            a plane
     * @return reflector associated to the plane
     */
    public Reflector get(Plane plane) {
        for (Reflector refl : reflectors)
            if (refl.equals(plane))
                return refl;

        throw new IllegalArgumentException("Plane " + plane.toString()
                + " was not found.");
    }



    /**
     * Iterates over all the reflectors.
     * 
     * @return an iterator
     */
    @Override
    public Iterator<Reflector> iterator() {
        return Arrays.asList(reflectors).iterator();
    }



    /**
     * Returns the number of defined <code>Reflector</code>.
     * 
     * @return number of reflectors
     */
    public int size() {
        return reflectors.length;
    }



    /**
     * Sort the reflectors list by their intensity.
     * 
     * @param reverse
     *            if <code>true</code> the reflectors are sorted by descending
     *            order of intensity
     */
    public void sortByIntensity(boolean reverse) {
        sort(reflectors, new IntensityComparator());

        if (reverse)
            reverse(reflectors);
    }



    /**
     * Sort the reflectors list by their plane spacing.
     * 
     * @param reverse
     *            if <code>true</code> the reflectors are sorted by descending
     *            order of plane spacing
     */
    public void sortByPlaneSpacing(boolean reverse) {
        sort(reflectors, new PlaneSpacingComparator());

        if (reverse)
            reverse(reflectors);
    }
};
