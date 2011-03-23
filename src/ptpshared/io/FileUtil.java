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
package ptpshared.io;

import java.io.File;
import java.io.IOException;

import rmlshared.io.JarFile;
import rmlshared.thread.Reflection;
import rmlshared.util.ArrayList;
import static rmlshared.io.FileUtil.getBaseName;
import static rmlshared.io.FileUtil.listFilesOnly;

/**
 * Miscellaneous utilities to deal with files, jars and class path.
 * 
 * @author Philippe T. Pinard
 */
public class FileUtil {

    /**
     * Searches all the jars and returns all the classes in the specified
     * package name.
     * 
     * @param jarFiles
     *            array of jar files to search
     * @param packageName
     *            package name
     * @return array of classes
     */
    public static Class<?>[] getClassesInJars(JarFile[] jarFiles,
            String packageName) {
        // Convert the package name to a path
        String packagePath = packageName.replace(".", "/");

        ArrayList<Class<?>> classList = new ArrayList<Class<?>>();
        for (JarFile jarFile : jarFiles) {
            String[] paths = jarFile.listFilesOnly(packagePath, "*.class");
            for (String path : paths) {
                if (path.indexOf('$') >= 0)
                    continue; // Remove inner classes
                path = path.substring(0, path.length() - 6); // Remove .class
                String className = path.replace('/', '.'); // Build class name
                Class<?> clasz = Reflection.forName(className); // Get Class
                classList.add(clasz);
            }
        }

        return classList.toArray(new Class<?>[0]);
    }



    /**
     * Searches the specified directory and returns all the classes in the
     * specified package name.
     * 
     * @param rootDir
     *            directory to search
     * @param packageName
     *            package name
     * @return array of classes or an empty array if the directory is empty or
     *         does not exist
     */
    public static Class<?>[] getClassesInPath(File rootDir, String packageName) {
        // Convert the package name to a path
        String packagePath = packageName.replace(".", "/");

        ArrayList<Class<?>> classList = new ArrayList<Class<?>>();

        // Build the package full path
        File packageDir = new File(rootDir, packagePath);

        // If the package directory does not exist
        if (!packageDir.exists())
            return new Class<?>[0];

        // List the files in the package directory
        File[] files = listFilesOnly(packageDir, "*.class");

        for (File file : files) {
            // Remove inner classes
            if (file.getPath().indexOf('$') >= 0)
                continue;

            // Build class name
            String className = packageName + '.' + getBaseName(file);

            // Get the Class object
            Class<?> clasz = Reflection.forName(className);
            classList.add(clasz);
        }

        return classList.toArray(new Class<?>[0]);
    }



    /**
     * Returns all the jar files in the specified directory.
     * 
     * @param dir
     *            a directory
     * @return array of jar files
     * @throws IOException
     *             if an error occurs while listing the jars
     */
    public static JarFile[] getJarFiles(File dir) throws IOException {
        File[] files = listFilesOnly(dir, "*.jar");

        JarFile[] jarFiles = new JarFile[files.length];
        for (int i = 0; i < jarFiles.length; i++)
            jarFiles[i] = new JarFile(files[i]);

        return jarFiles;
    }



    /**
     * Checks if the jar file contains the specified package path.
     * 
     * @param jarFile
     *            a jar file
     * @param packagePath
     *            package path
     * @return <code>true</code> if the package path is in the jar file,
     *         <code>false</code> otherwise
     */
    public static boolean jarContainsPackage(JarFile jarFile, String packagePath) {
        return (jarFile.getEntry(packagePath) != null);
    }



    /**
     * Joins portion of a package name together.
     * 
     * @param packageNames
     *            part of a package name
     * @return package name
     */
    public static String joinPackageNames(String... packageNames) {
        StringBuilder finalPackageName = new StringBuilder();

        for (String packageName : packageNames) {
            if (packageName.startsWith("."))
                packageName = packageName.substring(1);
            if (packageName.endsWith("."))
                packageName =
                        packageName.substring(0, packageName.length() - 1);
            finalPackageName.append(packageName + ".");
        }

        finalPackageName.setLength(finalPackageName.length() - 1);

        return finalPackageName.toString();
    }

}
