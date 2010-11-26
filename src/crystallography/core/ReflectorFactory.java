package crystallography.core;

import rmlshared.thread.Reflection;

/**
 * Factory to create a reflector.
 * 
 * @author ppinard
 */
public class ReflectorFactory {

    /**
     * Creates a new <code>Reflector</code>.
     * 
     * @param plane
     *            crystallographic plane
     * @param crystal
     *            crystal containing the plane
     * @param scatterType
     *            type of scattering coefficient to calculate the diffraction
     *            intensity
     * @throws NullPointerException
     *             if the plane, crystal or scattering factors is null
     * @return a reflector
     */
    public static Reflector create(Plane plane, Crystal crystal,
            ScatteringFactorsEnum scatterType) {
        if (plane == null)
            throw new NullPointerException("Plane cannot be null.");
        if (crystal == null)
            throw new NullPointerException("Crystal cannot be null.");
        if (scatterType == null)
            throw new NullPointerException("Scattering factors cannot be null.");

        ScatteringFactors scatter =
                Reflection.newInstance(scatterType.getScatteringFactors());

        return new Reflector(plane, Calculations.planeSpacing(plane,
                crystal.unitCell), Calculations.diffractionIntensity(plane,
                crystal.unitCell, crystal.atoms, scatter));
    }
}
