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
import crystallography.core.LaueGroup;
import crystallography.core.UnitCellFactory;

/**
 * Silicon crystal.
 * 
 * @author Philippe T. Pinard
 * 
 */
public class Silicon extends Crystal {

    /**
     * Creates a new <code>Crystal</code> for pure silicon (FCC,
     * <code>a=5.43 angstroms</code>).
     */
    public Silicon() {
        super("Silicon", UnitCellFactory.cubic(5.43), AtomSitesFactory
                .atomSitesFCC(14), LaueGroup.LGm3m);
    }
}
