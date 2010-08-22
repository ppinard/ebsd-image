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
package org.ebsdimage.io.sim;

import static org.ebsdimage.core.sim.SimConstants.OPS_IO_PACKAGE;
import static org.ebsdimage.io.sim.SimsGeneratorXmlTags.TAG_NAME;

import org.ebsdimage.core.sim.SimsGenerator;
import org.ebsdimage.io.run.RunsGeneratorXmlLoader;
import org.jdom.Element;
import org.jdom.IllegalNameException;

import ptpshared.io.FileUtil;
import ptpshared.utility.xml.ObjectXmlLoader;
import rmlshared.thread.Reflection;

/**
 * XML loader for a <code>SimsGenerator</code>.
 * 
 * @author Philippe T. Pinard
 */
public class SimsGeneratorXmlLoader extends RunsGeneratorXmlLoader {

    /**
     * Loads a <code>SimsGenerator</code> from an XML <code>Element</code>.
     * 
     * @param element
     *            an XML <code>Element</code>
     * @return a <code>SimsGenerator</code>
     * @throws IllegalNameException
     *             if the <code>Element</code> tag name is incorrect.
     */
    @Override
    public SimsGenerator load(Element element) {
        if (!element.getName().equals(TAG_NAME))
            throw new IllegalNameException("Name of the element should be "
                    + TAG_NAME + " not " + element.getName() + ".");

        SimsGenerator generator = new SimsGenerator();

        loadItems(element, generator);

        return generator;
    }



    @Override
    protected ObjectXmlLoader getOperationLoader(Element child,
            String packageName) {
        String childName = child.getName();
        String classLoaderName =
                FileUtil.joinPackageNames(OPS_IO_PACKAGE, packageName,
                        childName + "XmlLoader");
        return (ObjectXmlLoader) Reflection.newInstance(classLoaderName);
    }

}
