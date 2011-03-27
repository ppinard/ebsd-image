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
package org.ebsdimage.io;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import org.ebsdimage.core.Microscope;

import ptpshared.util.simplexml.ApacheCommonMathMatcher;
import ptpshared.util.simplexml.XmlLoader;
import rmlshared.io.FileUtil;

/**
 * Utilities to access microscope configurations.
 * 
 * @author Philippe T. Pinard
 */
public class MicroscopeUtil {
    /**
     * Returns the directory where all the microscope configurations are saved.
     * 
     * @return directory where all the microscope configurations are saved
     */
    public static File getMicroscopeBasedir() {
        File dir = new File(rmlimage.RMLImage.getConfigDir(), "microscope");

        if (!dir.exists())
            dir.mkdirs();

        return dir;
    }



    /**
     * Returns an array of all the microscope configurations saved in the
     * microscope configuration directory.
     * 
     * @return array of microscope configurations defined by the user
     */
    public static Microscope[] getMicroscopes() {
        File dir = getMicroscopeBasedir();
        if (!dir.exists())
            return new Microscope[0];

        ArrayList<Microscope> microscopes = new ArrayList<Microscope>();

        File[] files = FileUtil.listFilesOnly(dir, "*.xml");
        Arrays.sort(files);

        XmlLoader loader = new XmlLoader();
        loader.matchers.registerMatcher(new ApacheCommonMathMatcher());

        Microscope microscope;
        for (File file : files) {
            try {
                microscope = loader.load(Microscope.class, file);
            } catch (Exception e) {
                continue;
            }

            microscopes.add(microscope);
        }

        return microscopes.toArray(new Microscope[0]);
    }
}
