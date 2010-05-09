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
import org.ebsdimage.core.run.RunsGenerator;
import org.jdom.Element;

import ptpshared.utility.xml.ObjectXmlLoader;

/**
 * Abstract XML loader for an <code>RunsGenerator</code>.
 * 
 * @author Philippe T. Pinard
 * 
 */
public abstract class RunsGeneratorXmlLoader implements ObjectXmlLoader {

    /**
     * Loads all the children in the specified element and adds the item to the
     * generator.
     * 
     * @param element
     *            an XML <code>Element</code> containing generator's items
     * @param generator
     *            generator where the items will be added
     */
    protected void loadItems(Element element, RunsGenerator generator) {
        // Loop through all the parent elements
        for (Object obj1 : element.getChildren()) {
            Element parent = (Element) obj1;
            String packageName = parent.getName();

            for (Object obj2 : parent.getChildren()) {
                Element child = (Element) obj2;

                Operation op = (Operation) getOperationLoader(child,
                        packageName).load(child);
                int order = OperationXmlLoader.getOrder(child);

                generator.addItem(order, op);
            }
        }
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
