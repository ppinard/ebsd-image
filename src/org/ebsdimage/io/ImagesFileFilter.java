package org.ebsdimage.io;

import java.io.File;
import java.io.FileFilter;

import rmlshared.io.FileUtil;

/**
 * File filter to accept only jpg and bmp files.
 * 
 * @author ppinard
 */
public class ImagesFileFilter implements FileFilter {

    @Override
    public boolean accept(File f) {
        if (f.isDirectory())
            return false;

        String ext = FileUtil.getExtension(f);
        if (ext.equalsIgnoreCase("jpg") || ext.equalsIgnoreCase("bmp"))
            return true;
        else
            return false;
    }

}
