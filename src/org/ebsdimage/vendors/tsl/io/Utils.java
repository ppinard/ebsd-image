package org.ebsdimage.vendors.tsl.io;

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
     * specified location of the ANG file. The directory is assumed to be
     * located in the same parent directory as the ANG file and to be named as
     * the filename of the ANG file.
     * 
     * @param file
     *            location of the ANG file
     * @return directory containing the pattern images
     */
    public static File getPatternImagesDir(File file) {
        return new File(file.getParentFile(), FileUtil.getBaseName(file));
    }
}
