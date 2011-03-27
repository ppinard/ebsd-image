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
package org.ebsdimage.core.run;

import java.io.IOException;
import java.util.ArrayList;

import org.ebsdimage.io.FileUtil;

/**
 * Utilities used to get operations for a <code>Run</code>.
 * 
 * @author Philippe T. Pinard
 */
public class RunUtil {
    /**
     * Returns all <code>Operation</code>s for the specified package name.
     * Classes ending with <em>Test</em> (unit tests) or <code>Mock</code> (mock
     * classes) are ignored.
     * 
     * @param packageName
     *            name of the package containing the operations
     * @return list of classes of the operations
     * @throws IOException
     *             if an operation cannot be loaded
     * @see FileUtil#getClasses(String)
     */
    public static Class<?>[] getOperations(String packageName)
            throws IOException {
        ArrayList<Class<?>> classOps = new ArrayList<Class<?>>();

        Class<?>[] classes = FileUtil.getClasses(packageName);
        for (Class<?> clasz : classes) {
            // Remove Mock and Test
            String simpleName = clasz.getSimpleName();
            if (simpleName.endsWith("Mock") || simpleName.endsWith("Test"))
                continue;

            classOps.add(clasz);
        }

        return classOps.toArray(new Class<?>[classOps.size()]);
    }
}
