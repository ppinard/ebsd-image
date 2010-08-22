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

/**
 * Enumeration of the scattering factors.
 * 
 * @author Philippe T. Pinard
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
