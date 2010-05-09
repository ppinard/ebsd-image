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

import org.ebsdimage.core.run.Operation;
import org.jdom.Element;

import ptpshared.utility.xml.ObjectXmlSaver;

/**
 * Abstract XML saver for a <code>Run</code>.
 * 
 * @author Philippe T. Pinard
 * 
 */
public abstract class RunXmlSaver implements ObjectXmlSaver {

    /**
     * Returns an XML <code>Element</code> for the array of operations.
     * 
     * @param ops
     *            operation array
     * @param tagName
     *            name of the child node
     * @param packageName
     *            package where to look for the operation XML saver
     * @return an <code>Element</code> of the operations
     */
    protected Element createOpsElement(Operation[] ops, String tagName,
            String packageName) {
        Element element = new Element(tagName);

        for (int i = 0; i < ops.length; i++) {
            Element child = saveOperation(ops[i], packageName);
            child.setText(Integer.toString(i));
            element.addContent(child);
        }

        return element;
    }



    /**
     * Returns an XML <code>Element</code> for the specified operation.
     * 
     * @param op
     *            an operation
     * @param tagName
     *            name of the child node
     * @param packageName
     *            package where to look for the operation XML saver
     * @return an <code>Element</code> of the operation
     */
    protected Element createOpElement(Operation op, String tagName,
            String packageName) {
        Element element = new Element(tagName);

        element.addContent(saveOperation(op, packageName));

        return element;
    }



    /**
     * Saves an operation as an XML <code>Element</code>. The operation XML
     * saver to use is found by {@link #getOperationSaver(Operation)}.
     * 
     * @param op
     *            an operation
     * @param packageName
     *            package where to look for the operation XML saver
     * @return an XML <code>Element</code> of the operation
     */
    protected Element saveOperation(Operation op, String packageName) {
        return getOperationSaver(op, packageName).save(op);
    }



    /**
     * Returns the operation XML saver to be used for the specified operation.
     * 
     * @param op
     *            an operation
     * @param packageName
     *            package where to look for the operation XML saver
     * @return operation XML saver
     */
    protected abstract ObjectXmlSaver getOperationSaver(Operation op,
            String packageName);

}
