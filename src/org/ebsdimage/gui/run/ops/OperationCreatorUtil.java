package org.ebsdimage.gui.run.ops;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

import org.ebsdimage.io.FileUtil;

import rmlshared.thread.Reflection;

/**
 * Utilities related to the <code>OperationCreator</code> interface for the GUI.
 * 
 * @author ppinard
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
