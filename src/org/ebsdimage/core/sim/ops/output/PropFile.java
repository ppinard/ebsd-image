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

import ptpshared.utility.xml.JDomUtil;
import rmlshared.util.Properties;

/**
 * Operation to save the parameters of the simulation in a property file
 * (.prop).
 * 
 * @author Philippe T. Pinard
 */
public class PropFile extends OutputOps {

    /**
     * Saves the parameters of the simulation in a property file.
     * 
     * @param sim
     *            simulation executing this method
     * @throws IOException
     *             if an error occurs while saving the prop file
     */
    @Override
    public void save(Sim sim) throws IOException {
        // Output file
        File file = new File(sim.getDir(), sim.getName() + "_"
                + sim.getCurrentIndex() + ".prop");

        // Properties from XML element
        Properties props = JDomUtil.toProperties(new SimXmlSaver().save(sim));

        props.store(file);
    }
}
