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

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;

import org.ebsdimage.core.run.Operation;
import org.ebsdimage.core.run.RunsGenerator;
import org.jdom.Element;

import ptpshared.util.xml.ObjectXml;
import ptpshared.util.xml.ObjectXmlSaver;

/**
 * Abstract XML saver for a <code>RunsGenerator</code>.
 * 
 * @author Philippe T. Pinard
 */
public abstract class RunsGeneratorXmlSaver implements ObjectXmlSaver {

    /**
     * {@inheritDoc}
     * 
     * @see #save(RunsGenerator)
     */
    @Override
    public Element save(ObjectXml obj) {
        return save((RunsGenerator) obj);
    }



    /**
     * Creates the root XML <code>Element</code> with the right tag name.
     * 
     * @return root XML <code>Element</code>
     */
    protected abstract Element createElement();



    /**
     * Saves a <code>RunsGenerator</code> to an XML <code>Element</code>.
     * 
     * @param generator
     *            a <code>RunsGenerator</code>
     * @return an XML <code>Element</code>
     */
    public Element save(RunsGenerator generator) {
        HashMap<String, Operation[]> items = generator.getItems();

        // Sort operations by their superclass
        HashMap<String, String[]> sortedItems = new HashMap<String, String[]>();

        // Sort keys so that the order of the operation is respected
        String[] keys =
                items.keySet().toArray(new String[items.keySet().size()]);
        Arrays.sort(keys);

        for (String key : keys) {
            // The operations should at least contain one item
            Operation op = items.get(key)[0];

            // Get superclass (class with suffix Ops)
            String opsName =
                    op.getClass().getSuperclass().getPackage().getName();

            // Increment sortedOps HashMap
            if (!(sortedItems.containsKey(opsName))) {
                String[] tmpArray = new String[] { key };
                sortedItems.put(opsName, tmpArray);
            } else {
                String[] oldArray = sortedItems.get(opsName);
                String[] newArray = new String[oldArray.length + 1];

                System.arraycopy(oldArray, 0, newArray, 0, oldArray.length);
                newArray[newArray.length - 1] = key;
                sortedItems.put(opsName, newArray);
            }
        }

        // Create element
        Element element = createElement();

        // Loop through all operations classes
        for (Entry<String, String[]> sortedItem : sortedItems.entrySet()) {
            String opsName = sortedItem.getKey();

            // Create element for each grouping of element
            Element parent = getSuperclassOperationElement(opsName);

            // Add operation to XML element
            for (String key : sortedItem.getValue())
                for (Object obj : items.get(key)) {
                    // There should not be any casting error, since all objects
                    // are operations
                    Operation op = (Operation) obj;

                    // Output operation to XML element
                    Element child = saveOperation(op);

                    // Add order
                    child.setText(Integer.toString(RunsGenerator.getOrderFromKey(key)));

                    // Append child element
                    parent.addContent(child);
                }

            element.addContent(parent);
        }

        return element;
    }



    /**
     * Saves an operation as an XML <code>Element</code>. The operation XML
     * saver to use is found by {@link #getOperationSaver(Operation)}.
     * 
     * @param op
     *            operation to be saved
     * @return an XML <code>Element</code> of the operation
     */
    protected Element saveOperation(Operation op) {
        return getOperationSaver(op).save(op);
    }



    /**
     * Returns the operation XML saver to be used for the specified operation.
     * 
     * @param op
     *            an operation
     * @return operation XML saver
     */
    protected abstract ObjectXmlSaver getOperationSaver(Operation op);



    /**
     * Returns an XML <code>Element</code> for the superclass operation.
     * 
     * @param opsName
     *            name of the superclass operation
     * @return an XML <code>Element</code>
     */
    protected abstract Element getSuperclassOperationElement(String opsName);

}
