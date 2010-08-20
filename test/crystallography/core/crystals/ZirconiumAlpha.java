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
package crystallography.core.crystals;

import crystallography.core.AtomSitesFactory;
import crystallography.core.Crystal;
import crystallography.core.SpaceGroups;
import crystallography.core.UnitCellFactory;

/**
 * Zirconium Alpha crystal.
 * 
 * @author Philippe T. Pinard
 */
public class ZirconiumAlpha extends Crystal {

    /**
     * Creates a new <code>Crystal</code> for pure zirconium alpha (HCP,
     * <code>a=3.2 angstroms</code> and <code>c=5.15 angstroms</code>).
     */
    public ZirconiumAlpha() {
        super("Zirconium Alpha", UnitCellFactory.hexagonal(3.2, 5.15),
                AtomSitesFactory.atomSitesHCP(40), SpaceGroups.fromIndex(194));
    }
}