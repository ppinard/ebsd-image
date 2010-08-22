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
package org.ebsdimage.gui.run.ops;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

import org.ebsdimage.io.FileUtil;

import rmlshared.thread.Reflection;

/**
 * Utilities related to the <code>OperationCreator</code> interface for the GUI.
 * 
 * @author Philippe T. Pinard
 */
public class OperationCreatorUtil {
    /**
     * Search for all the <code>OperationCreator</code> in the specified
     * package.
     * 
     * @param packageName
     *            name of the package for the operations
     * @return <code>OperationCreator</code>s
     * @throws IOException
     *             if an error occurs while listing the
     *             <code>OperationCreator</code>
     */
    public static OperationCreator[] getOperationCreators(String packageName)
            throws IOException {
        ArrayList<OperationCreator> operationCreators =
                new ArrayList<OperationCreator>();

        for (Class<?> clasz : FileUtil.getClasses(packageName))
            if (OperationCreator.class.isAssignableFrom(clasz)
                    && !Modifier.isAbstract(clasz.getModifiers()))
                operationCreators.add((OperationCreator) Reflection.newInstance(clasz));

        return operationCreators.toArray(new OperationCreator[0]);
    }
}
