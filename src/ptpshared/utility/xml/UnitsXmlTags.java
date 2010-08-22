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
package ptpshared.utility.xml;

/**
 * Tags for loading and saving common units in an XML <code>Element</code>.
 * 
 * @author Philippe T. Pinard
 */
public class UnitsXmlTags {
    /** Default XML attribute for the units. */
    public static final String ATTR = "units";

    /** Units for electron-volts. */
    public static final String EV = "eV";

    /** Units for meters. */
    public static final String M = "m";

    /** Units for radians. */
    public static final String RAD = "rad";

    /** Units for angstroms. */
    public static final String ANGSTROM = "angstrom";

    /** Units for pixels. */
    public static final String PX = "px";

}
