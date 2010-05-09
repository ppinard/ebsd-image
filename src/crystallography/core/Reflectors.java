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

import java.io.Serializable;
import java.util.*;

import net.jcip.annotations.Immutable;

/**
 * Comparator for <code>Reflector</code> according to their intensity.
 * 
 * @author Philippe T. Pinard
 * 
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
 * 
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
 * X-rays scattering factors are more robust.
 * 
 * <b>References:</b>
 * <ul>
 * <li>Lecture notes from Raynald Gauvin, McGill University, 2007</li>
 * </ul>
 * 
 * @author Philippe T. Pinard
 * 
 */
@Immutable
public class Reflectors implements Iterable<Reflector> {

    /** Array of reflectors. */
    private final Reflector[] reflectors;

    /** Crystal associated with the reflectors. */
    public final Crystal crystal;



    /**
     * Creates a new list of <code>Reflector</code> for the given crystal. The
     * reflectors are automatically computed for all planes with indices less or
     * equal to <code>maxIndice</code>. Only diffracting plane are added.
     * 
     * @param crystal
     *            a <code>Crystal</code> representing a unit cell and atom sites
     * @param scatter
     *            scattering factors
     * @param maxIndex
     *            maximum index of the planes to compute
     * 
     * @throws NullPointerException
     *             if the crystal is null
     * @throws NullPointerException
     *             if the scattering factors is null
     * @throws IllegalArgumentException
     *             if the maxIndex is less than 0
     */
    public Reflectors(Crystal crystal, ScatteringFactors scatter, int maxIndex) {
        if (crystal == null)
            throw new NullPointerException("Crystal cannot be null.");
        if (scatter == null)
            throw new NullPointerException("Scattering factors cannot be null.");
        if (maxIndex < 1)
            throw new IllegalArgumentException(
                    "The maximum index has to greater or equal to 1.");

        this.crystal = crystal;

        ArrayList<Reflector> tmpRefls = new ArrayList<Reflector>();
        UnitCell unitCell = crystal.unitCell;
        AtomSites atoms = crystal.atoms;

        double maxDiffractionIntensity =
                Calculations.maximumDiffractionIntensity(unitCell, atoms,
                        scatter);

        // Find reflectors
        for (int h = -maxIndex; h <= maxIndex; h++)
            for (int k = -maxIndex; k <= maxIndex; k++)
                for (int l = -maxIndex; l <= maxIndex; l++) {
                    // Create plane
                    // Only look at positive planes since negative plane are
                    // equivalent
                    Plane p;
                    try {
                        p = new Plane(h, k, l).positive();
                    } catch (InvalidPlaneException e) {
                        continue;
                    }

                    // Calculate the intensities
                    double intensity =
                            Calculations.diffractionIntensity(p, unitCell,
                                    atoms, scatter);

                    // Check if the plane diffracts
                    if (Calculations.isDiffracting(intensity,
                            maxDiffractionIntensity, 1e-14)) {
                        double planeSpacing =
                                Calculations.planeSpacing(p, unitCell);
                        Reflector reflector =
                                new Reflector(p, planeSpacing, intensity);

                        // Add only if it doesn't already exists
                        if (!(tmpRefls.contains(reflector)))
                            tmpRefls.add(reflector);
                    }
                }

        // Sort reflectors by intensity
        Collections.sort(tmpRefls, new IntensityComparator());
        Collections.reverse(tmpRefls);

        // Find max intensity
        double maxIntensity = tmpRefls.get(0).intensity;

        // Initalize reflectors array
        reflectors = new Reflector[tmpRefls.size()];

        // Calculate normalized intensity and add reflector to class ArrayList
        for (int i = 0; i < tmpRefls.size(); i++) {
            Reflector refl = tmpRefls.get(i);
            double normalizedIntensity = refl.intensity / maxIntensity;
            reflectors[i] = new Reflector(refl, normalizedIntensity);
        }
    }



    /**
     * Returns the reflector with the specified index.
     * 
     * @param index
     *            index of a reflector in the array.
     * 
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
