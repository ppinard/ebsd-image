/*
 * EBSD-Image
 * Copyright (C) 2010-2011 Philippe T. Pinard
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

public class CrystalFactory {

    public static Crystal ferrite() {
        return new Crystal("Ferrite", UnitCellFactory.cubic(2.87),
                AtomSitesFactory.atomSitesBCC(26), SpaceGroups.fromIndex(229));
    }



    public static Crystal silicon() {
        return new Crystal("Silicon", UnitCellFactory.cubic(5.43),
                AtomSitesFactory.atomSitesFCC(14), SpaceGroups.fromIndex(216));
    }



    public static Crystal zirconium() {
        return new Crystal("Zirconium", UnitCellFactory.hexagonal(3.2, 5.15),
                AtomSitesFactory.atomSitesHCP(40), SpaceGroups.fromIndex(194));
    }
}
