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
package org.ebsdimage.io;

import static ptpshared.io.FileUtil.getClassesInJars;
import static ptpshared.io.FileUtil.getClassesInPath;
import static ptpshared.io.FileUtil.getJarFiles;
import static rmlshared.io.FileUtil.*;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import rmlshared.io.JarFile;
import rmlshared.util.ArrayList;

/**
 * Miscellaneous utilities to deal with files, jars and class path.
 * 
 * @author Philippe T. Pinard
 * 
 */
public class FileUtil {

    /**
     * Searches the package to find all the classes. The method looks in all the
     * jars and class files inside the module folder of the RML-Image
     * installation and in the module folder in the RML-Image home folder.
     * 
     * @param packageName
     *            name of the package
     * @return array of class
     * @throws IOException
     *             if an error occurs while searching for the classes
     */
    public static Class<?>[] getClasses(String packageName) throws IOException {

        // Get the location of org.ebsdimage.core.HoughMap
        URL houghMapURL = getURL(org.ebsdimage.core.HoughMap.class);

        if (isJar(houghMapURL)) { // If the class is in a jar (standard mode)
            // It is in $installDir/module/ebsd.jar
            File installDir =
                    getJarFile(houghMapURL).getParentFile().getParentFile();

            ArrayList<JarFile> jarFiles = new ArrayList<JarFile>();

            // List the jars in the $installDir/module directory
            File dir = new File(installDir, "module");
            if (dir.exists())
                jarFiles.addAll(getJarFiles(dir));

            // List the jars in the $home/.RML-Image/module directory
            dir = new File(getHomeDirFile(), ".RML-Image/module");
            if (dir.exists())
                jarFiles.addAll(getJarFiles(dir));

            return getClassesInJars(jarFiles.toArray(new JarFile[0]),
                    packageName);
        } else {
            if (isFile(houghMapURL)) {// If class in directory (development
                // mode)
                // It is $rootDir/org/ebsdimage/core/HoughMap.class
                File rootDir =
                        getFile(houghMapURL).getParentFile().getParentFile()
                                .getParentFile();
                return getClassesInPath(rootDir, packageName);
            } else
                throw new IllegalArgumentException(
                        "Invalid HoughMap.class URL: " + houghMapURL);
        }
    }

}
