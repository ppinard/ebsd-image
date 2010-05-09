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
 * Factory to create <code>AtomSites</code> for common unit cell.
 * 
 * @author Philippe T. Pinard
 * 
 */
public class AtomSitesFactory {

    /**
     * Returns the <code>AtomSites</code> for a body centered cubic unit cell.
     * 
     * @param atomicNumber
     *            atomic number of the atoms
     * 
     * @return BCC <code>AtomSites</code>
     */
    public static AtomSites atomSitesBCC(int atomicNumber) {
        AtomSites atoms = new AtomSites();

        atoms.add(new AtomSite(atomicNumber, 0.0, 0.0, 0.0));
        atoms.add(new AtomSite(atomicNumber, 0.5, 0.5, 0.5));

        return atoms;
    }



    /**
     * Returns the <code>AtomSites</code> for a face centered cubic unit cell.
     * 
     * @param atomicNumber
     *            atomic number of the atoms
     * 
     * @return FCC <code>AtomSites</code>
     */
    public static AtomSites atomSitesFCC(int atomicNumber) {
        AtomSites atoms = new AtomSites();

        atoms.add(new AtomSite(atomicNumber, 0.0, 0.0, 0.0));
        atoms.add(new AtomSite(atomicNumber, 0.0, 0.5, 0.5));
        atoms.add(new AtomSite(atomicNumber, 0.5, 0.0, 0.5));
        atoms.add(new AtomSite(atomicNumber, 0.5, 0.5, 0.0));

        return atoms;
    }



    /**
     * Returns the <code>AtomSites</code> for a hexagonal close packed unit
     * cell.
     * 
     * @param atomicNumber
     *            atomic number of the atoms
     * 
     * @return HCP <code>AtomSites</code>
     */
    public static AtomSites atomSitesHCP(int atomicNumber) {
        AtomSites atoms = new AtomSites();

        atoms.add(new AtomSite(atomicNumber, 0.0, 0.0, 0.0));
        atoms.add(new AtomSite(atomicNumber, 1 / 3.0, 2 / 3.0, 0.5));

        return atoms;
    }



    /**
     * Returns the <code>AtomSites</code> for a unit cell with a single atom at
     * (0, 0, 0).
     * 
     * @param atomicNumber
     *            atomic number of the atoms
     * 
     * @return single atom <code>AtomSites</code>
     */
    public static AtomSites atomSitesSingleAtom(int atomicNumber) {
        AtomSites atoms = new AtomSites();

        atoms.add(new AtomSite(atomicNumber, 0.0, 0.0, 0.0));

        return atoms;
    }
}
