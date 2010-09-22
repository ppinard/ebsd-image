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
     * specified location of the CTF file. The directory is assumed to be
     * located in the same parent directory as the CTF file and to be named as
     * the filename of the CTF file with the suffix "Image".
     * 
     * @param ctfFile
     *            location of the CTF file
     * @return directory containing the pattern images
     */
    public static File getPatternImagesDir(File ctfFile) {
        String folder = FileUtil.getBaseName(ctfFile) + "Images";
        return new File(ctfFile.getParentFile(), folder);
    }
}
