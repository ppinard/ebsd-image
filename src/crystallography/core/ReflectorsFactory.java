package crystallography.core;

import java.util.ArrayList;
import java.util.Collections;

import ptpshared.core.math.Vector3D;
import rmlshared.thread.Reflection;

/**
 * Factory to create reflectors of a crystal.
 * 
 * @author ppinard
 */
public class ReflectorsFactory {

    /**
     * Generates a new list of <code>Reflector</code>s for the given crystal.
     * The reflectors are automatically computed for all planes with indices
     * less or equal to <code>maxIndice</code>. Only diffracting plane are
     * added.
     * 
     * @param crystal
     *            a <code>Crystal</code> representing a unit cell and atom sites
     * @param scatterType
     *            scattering factors
     * @param maxIndex
     *            maximum index of the planes to compute
     * @throws NullPointerException
     *             if the crystal is null
     * @throws NullPointerException
     *             if the scattering factors is null
     * @throws IllegalArgumentException
     *             if the maxIndex is less than 0
     * @return reflectors for the given crystal
     */
    public static Reflectors generate(Crystal crystal,
            ScatteringFactorsEnum scatterType, int maxIndex) {
        if (crystal == null)
            throw new NullPointerException("Crystal cannot be null.");
        if (scatterType == null)
            throw new NullPointerException("Scattering factors cannot be null.");
        if (maxIndex < 1)
            throw new IllegalArgumentException(
                    "The maximum index has to greater or equal to 1.");

        ArrayList<Reflector> tmpRefls = new ArrayList<Reflector>();
        UnitCell unitCell = crystal.unitCell;
        AtomSites atoms = crystal.atoms;
        ScatteringFactors scatter =
                (ScatteringFactors) Reflection.newInstance(scatterType.getScatteringFactors());

        double maxDiffractionIntensity =
                Calculations.maximumDiffractionIntensity(unitCell, atoms,
                        scatter);

        // Find reflectors
        for (int h = -maxIndex; h <= maxIndex; h++)
            for (int k = -maxIndex; k <= maxIndex; k++)
                for (int l = -maxIndex; l <= maxIndex; l++) {
                    if (h == 0 && k == 0 & l == 0)
                        continue;

                    // Create plane
                    // Only look at positive planes since negative plane are
                    // equivalent
                    Vector3D p = new Vector3D(h, k, l).positive();

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
                                new Reflector((int) p.getX(), (int) p.getY(),
                                        (int) p.getZ(), planeSpacing, intensity);

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

        // Initialise reflectors array
        Reflector[] reflectors = new Reflector[tmpRefls.size()];

        // Calculate normalised intensity and add reflector to class ArrayList
        for (int i = 0; i < tmpRefls.size(); i++) {
            Reflector refl = tmpRefls.get(i);
            double normalizedIntensity = refl.intensity / maxIntensity;
            reflectors[i] = new Reflector(refl, normalizedIntensity);
        }

        return new Reflectors(crystal, reflectors);
    }
}
