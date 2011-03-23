package org.ebsdimage.vendors.hkl.io;

import java.io.File;

import rmlshared.io.FileUtil;

/**
 * Utilities for HKL files and formats.
 * 
 * @author ppinard
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
