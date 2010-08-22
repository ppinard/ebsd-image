package crystallography.core;

/**
 * Enumeration of the scattering factors.
 * 
 * @author ppinard
 */
public enum ScatteringFactorsEnum {

    /** Scattering factors based on X-ray diffraction. */
    XRAY(XrayScatteringFactors.class),

    /** Scattering factors based on electron diffraction (TEM). */
    ELECTRON(ElectronScatteringFactors.class);

    /** Scattering factors class. */
    private final Class<? extends ScatteringFactors> clasz;



    /**
     * Links the enumeration variable with the scattering factors class.
     * 
     * @param clasz
     *            scattering factors class
     */
    private ScatteringFactorsEnum(Class<? extends ScatteringFactors> clasz) {
        this.clasz = clasz;
    }



    /**
     * Returns the scattering factors class associated with the enumeration
     * variable.
     * 
     * @return scattering factors class
     */
    public Class<? extends ScatteringFactors> getScatteringFactors() {
        return clasz;
    }
}
