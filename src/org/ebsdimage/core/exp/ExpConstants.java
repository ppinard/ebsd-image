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
package org.ebsdimage.core.exp;

import ptpshared.io.FileUtil;

/**
 * Constants used to access the experiment's operations.
 * 
 * @author Philippe T. Pinard
 */
public class ExpConstants {

    /** Package name for the operations' core. */
    public static final String OPS_CORE_PACKAGE = "org.ebsdimage.core.exp.ops";

    /** Package name for the operations' gui. */
    public static final String OPS_GUI_PACKAGE = "org.ebsdimage.gui.exp.ops";

    /** Package name for the detection's core operations. */
    public static final String DETECTION_CORE_PACKAGE =
            FileUtil.joinPackageNames(OPS_CORE_PACKAGE, "detection");

    /** Package name for the detection's gui operations. */
    public static final String DETECTION_GUI_PACKAGE =
            FileUtil.joinPackageNames(OPS_GUI_PACKAGE, "detection");

    /** Package name for the hough's core operations. */
    public static final String HOUGH_CORE_PACKAGE = FileUtil.joinPackageNames(
            OPS_CORE_PACKAGE, "hough");

    /** Package name for the hough's gui operations. */
    public static final String HOUGH_GUI_PACKAGE = FileUtil.joinPackageNames(
            OPS_GUI_PACKAGE, "hough");

    /** Package name for the identification's core operations. */
    public static final String IDENTIFICATION_CORE_PACKAGE =
            FileUtil.joinPackageNames(OPS_CORE_PACKAGE, "identification");

    /** Package name for the identification's gui operations. */
    public static final String IDENTIFICATION_GUI_PACKAGE =
            FileUtil.joinPackageNames(OPS_GUI_PACKAGE, "identification");

    /** Package name for the indexing's core operations. */
    public static final String INDEXING_CORE_PACKAGE =
            FileUtil.joinPackageNames(OPS_CORE_PACKAGE, "indexing");

    /** Package name for the indexing's gui operations. */
    public static final String INDEXING_GUI_PACKAGE =
            FileUtil.joinPackageNames(OPS_GUI_PACKAGE, "indexing");

    /** Package name for the patterns' core operations. */
    public static final String PATTERN_CORE_PACKAGE =
            FileUtil.joinPackageNames(OPS_CORE_PACKAGE, "pattern");

    /** Package name for the patterns' gui operations. */
    public static final String PATTERN_GUI_PACKAGE = FileUtil.joinPackageNames(
            OPS_GUI_PACKAGE, "pattern");

}
