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
package org.ebsdimage.io.run;

import java.util.*;
import java.util.logging.Logger;

import org.ebsdimage.core.run.Operation;
import org.jdom.Element;

import ptpshared.utility.xml.ObjectXmlLoader;
import edu.umd.cs.findbugs.annotations.CheckForNull;

/**
 * Abstract XML loader for a <code>RunXmlLoader</code>.
 * 
 * @author Philippe T. Pinard
 */
public abstract class RunXmlLoader implements ObjectXmlLoader {

    /**
     * Loads a series of operations from an XML <code>Element</code>. The
     * operations are ordered based on the given order of their XML
     * <code>Element</code>. If two operations of the same type have the same
     * order, only the first one is loaded. A warning is issued.
     * 
     * @param element
     *            root XML <code>Element</code> of all the operations
     * @param tagName
     *            name of the operations' type to load
     * @param packageName
     *            name of the package where the operations belong
     * @return a list containing the loaded operations
     */
    protected ArrayList<Operation> loadOperations(Element element,
            String tagName, String packageName) {
        Logger logger = Logger.getLogger("ebsd");

        // Create temporary map to store operations
        HashMap<Integer, Operation> tmpMap = new HashMap<Integer, Operation>();

        // If no operations are present, returns.
        Element parent = element.getChild(tagName);
        if (parent == null)
            return new ArrayList<Operation>();

        // Add operation to the map based on their order
        Iterator<?> itr = (parent.getChildren()).iterator();
        while (itr.hasNext()) {
            Element child = (Element) itr.next();
            int order = OperationXmlLoader.getOrder(child);

            if (tmpMap.containsKey(order))
                logger.warning("Two operations (" + child.getName() + " and "
                        + tmpMap.get(order) + ") with the same order. "
                        + "The former is discarded.");
            else {
                Operation op = getOperation(child, packageName);
                tmpMap.put(order, op);
            }
        }

        // Sort operations by their order
        List<Integer> keys = new ArrayList<Integer>(tmpMap.keySet());
        Collections.sort(keys);

        ArrayList<Operation> ops = new ArrayList<Operation>();
        for (int key : keys)
            ops.add(tmpMap.get(key));

        return ops;
    }



    /**
     * Loads one operation from an XML <code>Element</code>.
     * 
     * @param element
     *            an XML <code>Element</code> of the operation
     * @param tagName
     *            name of the operation type to load
     * @param packageName
     *            name of the package where the operation belong
     * @return the operation or <code>null</code> if no operation is found
     */
    @CheckForNull
    protected Operation loadOperation(Element element, String tagName,
            String packageName) {
        Element parent = element.getChild(tagName);
        if (parent != null) {
            if (parent.getChildren().size() > 0) {
                Element child = (Element) parent.getChildren().get(0);
                if (child != null)
                    return getOperation(child, packageName);
            }
        }

        return null;
    }



    /**
     * Creates a new instance for an operation from an XML <code>Element</code>.
     * 
     * @param child
     *            an XML <code>Element</code>
     * @param packageName
     *            package where to look for the operation
     * @return an operation
     */
    protected Operation getOperation(Element child, String packageName) {
        return (Operation) getOperationLoader(child, packageName).load(child);
    }



    /**
     * Returns the operation XML loader to use to parse the specified
     * <code>Element</code>.
     * 
     * @param child
     *            XML <code>Element</code> to be parse
     * @param packageName
     *            package where to look for the operation XML loader
     * @return operation XML loader
     */
    protected abstract ObjectXmlLoader getOperationLoader(Element child,
            String packageName);
}
