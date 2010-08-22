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

import static org.ebsdimage.core.sim.SimConstants.OUTPUT_IO_PACKAGE;
import static org.ebsdimage.core.sim.SimConstants.PATTERNSIM_IO_PACKAGE;
import static org.ebsdimage.io.run.RunXmlTags.ATTR_NAME;
import static org.ebsdimage.io.run.RunXmlTags.ATTR_PATH;
import static org.ebsdimage.io.sim.SimXmlTags.TAG_NAME;

import org.ebsdimage.core.Camera;
import org.ebsdimage.core.run.Operation;
import org.ebsdimage.core.sim.Energy;
import org.ebsdimage.core.sim.Sim;
import org.ebsdimage.io.CameraXmlSaver;
import org.ebsdimage.io.run.RunXmlSaver;
import org.ebsdimage.io.sim.ops.output.OutputOpsXmlTags;
import org.ebsdimage.io.sim.ops.patternsim.PatternSimOpXmlTags;
import org.jdom.Element;

import ptpshared.core.math.Quaternion;
import ptpshared.io.FileUtil;
import ptpshared.io.math.QuaternionXmlSaver;
import ptpshared.utility.xml.ObjectXml;
import ptpshared.utility.xml.ObjectXmlSaver;
import rmlshared.thread.Reflection;
import crystallography.core.Crystal;
import crystallography.io.CrystalXmlSaver;

/**
 * XML saver for a <code>Sim</code>.
 * 
 * @author Philippe T. Pinard
 */
public class SimXmlSaver extends RunXmlSaver {

    @Override
    protected ObjectXmlSaver getOperationSaver(Operation op, String packageName) {
        String opName = op.getName();
        String classLoaderName =
                FileUtil.joinPackageNames(packageName, opName + "XmlSaver");
        return (ObjectXmlSaver) Reflection.newInstance(classLoaderName);
    }



    /**
     * {@inheritDoc}
     * 
     * @see #save(Sim)
     */
    @Override
    public Element save(ObjectXml obj) {
        return save((Sim) obj);
    }



    /**
     * Saves a <code>Sim</code> to an XML <code>Element</code>.
     * 
     * @param sim
     *            a <code>Sim</code>
     * @return an XML <code>Element</code>
     */
    public Element save(Sim sim) {
        Element element = new Element(TAG_NAME);

        // Attributes
        saveAttributes(element, sim);

        // Parameters
        // Camera
        for (Camera camera : sim.getCameras())
            element.addContent(new CameraXmlSaver().save(camera));

        // Crystal
        for (Crystal crystal : sim.getCrystals())
            element.addContent(new CrystalXmlSaver().save(crystal));

        // Energy
        for (Energy energy : sim.getEnergies())
            element.addContent(new EnergyXmlSaver().save(energy));

        // Rotation
        for (Quaternion rotation : sim.getRotations())
            element.addContent(new QuaternionXmlSaver().save(rotation));

        // Operations
        saveOperations(element, sim);

        return element;
    }



    /**
     * Saves a <code>Sim</code> to an XML <code>Element</code>. Only the current
     * parameters (camera, crystal, energy and rotation) are saved instead of
     * the list of parameters.
     * 
     * @param sim
     *            a <code>Sim</code>
     * @return an XML <code>Element</code>
     */
    public Element saveCurrent(Sim sim) {
        Element element = new Element(TAG_NAME);

        // Attributes
        saveAttributes(element, sim);

        // Parameters
        // Camera
        Camera camera = sim.getCurrentCamera();
        if (camera != null)
            element.addContent(new CameraXmlSaver().save(camera));

        // Crystal
        Crystal crystal = sim.getCurrentCrystal();
        if (crystal != null)
            element.addContent(new CrystalXmlSaver().save(crystal));

        // Energy
        Energy energy = sim.getCurrentEnergy();
        if (energy != null)
            element.addContent(new EnergyXmlSaver().save(energy));

        // Rotation
        Quaternion rotation = sim.getCurrentRotation();
        if (rotation != null)
            element.addContent(new QuaternionXmlSaver().save(rotation));

        // Operations
        saveOperations(element, sim);

        return element;
    }



    /**
     * Saves simulation's attributes.
     * 
     * @param element
     *            XML <code>Element</code> where to save the data
     * @param sim
     *            a <code>Sim</code>
     */
    private void saveAttributes(Element element, Sim sim) {
        element.setAttribute(ATTR_NAME, sim.getName());
        element.setAttribute(ATTR_PATH, sim.getDir().toString());
    }



    /**
     * Saves all simulation's operations.
     * 
     * @param element
     *            XML <code>Element</code> where to save the data
     * @param sim
     *            a <code>Sim</code>
     */
    private void saveOperations(Element element, Sim sim) {
        // Pattern Simulation Op
        String tagName = PatternSimOpXmlTags.TAG_NAME;
        String packageName = PATTERNSIM_IO_PACKAGE;
        Operation op = sim.getPatternSimOp();
        if (op != null)
            element.addContent(createOpElement(op, tagName, packageName));

        // Output Ops
        tagName = OutputOpsXmlTags.TAG_NAME;
        packageName = OUTPUT_IO_PACKAGE;
        Operation[] ops = sim.getOutputOps();
        element.addContent(createOpsElement(ops, tagName, packageName));
    }

}
