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
package crystallography.io;

import crystallography.core.UnitCell;

/**
 * Tags for <code>UnitCell</code>'s XML <code>Element</code>.
 * 
 * @author Philippe T. Pinard
 */
public class UnitCellXmlTags {

    /** XML tag name for UnitCell. */
    public static final String TAG_NAME = UnitCell.class.getSimpleName();

    /** XML attribute for a. */
    public static final String ATTR_A = "a";

    /** XML attribute for b. */
    public static final String ATTR_B = "b";

    /** XML attribute for c. */
    public static final String ATTR_C = "c";

    /** XML attribute for alpha. */
    public static final String ATTR_ALPHA = "alpha";

    /** XML attribute for beta. */
    public static final String ATTR_BETA = "beta";

    /** XML attribute for gamma. */
    public static final String ATTR_GAMMA = "gamma";

    /** XML attribute for dimensions' units. */
    public static final String ATTR_DIM_UNITS = "dimUnits";

    /** XML attribute for angles' units. */
    public static final String ATTR_ANGLE_UNITS = "angleUnits";

}
