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
package org.ebsdimage.core.sim;

import ptpshared.io.FileUtil;

/**
 * Constants used to access the simulation's operations.
 * 
 * @author Philippe T. Pinard
 * 
 */
public class SimConstants {

    /** Package name for the operations' core. */
    public static final String OPS_CORE_PACKAGE = "org.ebsdimage.core.sim.ops";

    /** Package name for the operations' io. */
    public static final String OPS_IO_PACKAGE = "org.ebsdimage.io.sim.ops";

    /** Package name for the pattern simulation's core operations. */
    public static final String PATTERNSIM_CORE_PACKAGE =
            FileUtil.joinPackageNames(OPS_CORE_PACKAGE, "patternsim");

    /** Package name for the pattern simulation's io operations. */
    public static final String PATTERNSIM_IO_PACKAGE =
            FileUtil.joinPackageNames(OPS_IO_PACKAGE, "patternsim");

    /** Package name for the output' core operations. */
    public static final String OUTPUT_CORE_PACKAGE =
            FileUtil.joinPackageNames(OPS_CORE_PACKAGE, "output");

    /** Package name for the output' io operations. */
    public static final String OUTPUT_IO_PACKAGE =
            FileUtil.joinPackageNames(OPS_IO_PACKAGE, "output");

}
