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

import java.util.HashMap;
import java.util.Iterator;

import net.jcip.annotations.Immutable;
import ptpshared.util.Arrays;

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
    private final HashMap<Integer, Reflector> reflectors;

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

        this.reflectors = new HashMap<Integer, Reflector>();
        for (Reflector refl : reflectors)
            this.reflectors.put(refl.hashCode(), refl);
    }



    /**
     * Returns a reflector for the given plane.
     * 
     * @param h
     *            h index of the crystallographic plane
     * @param k
     *            k index of the crystallographic plane
     * @param l
     *            l index of the crystallographic plane
     * @return reflector associated to the plane
     */
    public Reflector get(int h, int k, int l) {
        int hashCode = Reflector.calculateHashCode(h, k, l);

        Reflector refl = reflectors.get(hashCode);
        if (refl == null)
            throw new IllegalArgumentException("Plane (" + h + ";" + k + ";"
                    + l + ") was not found.");
        else
            return refl;
    }



    /**
     * Check whether this <code>Reflectors</code> object contains a
     * <code>Reflector</code> with the specified indices.
     * 
     * @param h
     *            h index of the crystallographic plane
     * @param k
     *            k index of the crystallographic plane
     * @param l
     *            l index of the crystallographic plane
     * @return <code>true</code> if a <code>Reflector</code> exists,
     *         <code>false</code> otherwise
     */
    public boolean contains(int h, int k, int l) {
        int hashCode = Reflector.calculateHashCode(h, k, l);
        return reflectors.containsKey(hashCode);
    }



    /**
     * Iterates over all the reflectors.
     * 
     * @return an iterator
     */
    @Override
    public Iterator<Reflector> iterator() {
        return reflectors.values().iterator();
    }



    /**
     * Returns the number of defined <code>Reflector</code>.
     * 
     * @return number of reflectors
     */
    public int size() {
        return reflectors.size();
    }



    /**
     * Returns a array of the reflectors sorted by their intensity. If
     * <code>reverse</code> is <code>false</code>, the order is ascending. If
     * <code>reverse</code> is <code>true</code>, the order is descending.
     * 
     * @param reverse
     *            order of the sorting
     * @return sorted array of the reflectors
     */
    public Reflector[] getReflectorsSortedByIntensity(boolean reverse) {
        Reflector[] refls = reflectors.values().toArray(new Reflector[0]);

        Arrays.sort(refls, new IntensityComparator(), reverse);

        return refls;
    }



    /**
     * Returns a array of the reflectors sorted by their plane spacing. If
     * <code>reverse</code> is <code>false</code>, the order is ascending. If
     * <code>reverse</code> is <code>true</code>, the order is descending.
     * 
     * @param reverse
     *            order of the sorting
     * @return sorted array of the reflectors
     */
    public Reflector[] getReflectorsSortedByPlaneSpacing(boolean reverse) {
        Reflector[] refls = reflectors.values().toArray(new Reflector[0]);

        Arrays.sort(refls, new PlaneSpacingComparator(), reverse);

        return refls;
    }

};
