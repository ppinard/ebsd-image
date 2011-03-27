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
package org.ebsdimage.vendors.hkl.io;

import java.io.File;

import rmlshared.io.FileUtil;

/**
 * Utilities for HKL files and formats.
 * 
 * @author Philippe T. Pinard
 */
public class Utils {

    /**
     * Returns the directory containing the pattern images based on the
     * specified location of the CPR or CTF file. The directory is assumed to be
     * located in the same parent directory as the CPR or CTF file and to be
     * named as the filename of the CPR or CTF file with the suffix "Image".
     * 
     * @param file
     *            location of the CPR or CTF file
     * @return directory containing the pattern images
     */
    public static File getPatternImagesDir(File file) {
        String folder = FileUtil.getBaseName(file) + "Images";
        return new File(file.getParentFile(), folder);
    }
}
