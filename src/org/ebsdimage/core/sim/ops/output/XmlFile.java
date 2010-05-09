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
package org.ebsdimage.core.sim.ops.output;

import java.io.File;
import java.io.IOException;

import org.ebsdimage.core.sim.Sim;
import org.ebsdimage.io.sim.SimXmlSaver;
import org.jdom.Element;

import ptpshared.utility.xml.JDomUtil;

/**
 * Operation to save the parameters and results in an XML file.
 * 
 * @author Philippe T. Pinard
 */
public class XmlFile extends OutputOps {

    /**
     * Saves results, operations or parameters of the simulation in an XML file.
     * 
     * @param sim
     *            simulation executing this method
     * @throws IOException
     *             if an error occurs while saving
     */
    @Override
    public void save(Sim sim) throws IOException {
        Element element = new SimXmlSaver().save(sim);

        // Output file
        File file = new File(sim.getDir(), sim.getName() + "_"
                + sim.getCurrentIndex() + ".xml");
        JDomUtil.saveXML(element, file);
    }

}
