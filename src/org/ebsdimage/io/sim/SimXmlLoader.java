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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.ebsdimage.core.Camera;
import org.ebsdimage.core.run.Operation;
import org.ebsdimage.core.run.Run;
import org.ebsdimage.core.sim.Energy;
import org.ebsdimage.core.sim.Sim;
import org.ebsdimage.io.CameraXmlLoader;
import org.ebsdimage.io.CameraXmlTags;
import org.ebsdimage.io.run.RunXmlLoader;
import org.ebsdimage.io.sim.ops.output.OutputOpsXmlTags;
import org.ebsdimage.io.sim.ops.patternsim.op.PatternSimOpXmlTags;
import org.jdom.Element;
import org.jdom.IllegalNameException;

import ptpshared.core.math.Quaternion;
import ptpshared.io.FileUtil;
import ptpshared.io.math.QuaternionXmlLoader;
import ptpshared.io.math.QuaternionXmlTags;
import ptpshared.utility.xml.JDomUtil;
import ptpshared.utility.xml.ObjectXmlLoader;
import rmlshared.thread.Reflection;
import crystallography.core.Crystal;
import crystallography.io.CrystalXmlLoader;
import crystallography.io.CrystalXmlTags;

/**
 * XML loader for a <code>Sim</code>.
 * 
 * @author Philippe T. Pinard
 * 
 */
public class SimXmlLoader extends RunXmlLoader {

    @Override
    protected ObjectXmlLoader getOperationLoader(Element child,
            String packageName) {
        String childName = child.getName();
        String classLoaderName = packageName + "." + childName + "XmlLoader";
        return (ObjectXmlLoader) Reflection.newInstance(classLoaderName);
    }



    /**
     * Loads a <code>Sim</code> from an XML <code>Element</code>.
     * 
     * @param element
     *            an XML <code>Element</code>
     * @return a <code>Sim</code>
     * @throws IllegalNameException
     *             if the <code>Element</code> tag name is incorrect.
     */
    @Override
    public Sim load(Element element) {
        if (!element.getName().equals(TAG_NAME))
            throw new IllegalNameException("Name of the element should be "
                    + TAG_NAME + " not " + element.getName() + ".");

        // Name
        String name =
                JDomUtil.getStringFromAttribute(element, ATTR_NAME,
                        Run.DEFAULT_NAME);

        // Path
        File path =
                new File(JDomUtil.getStringFromAttributeDefault(element,
                        ATTR_PATH, Run.DEFAULT_DIR.toString()));

        // Crystal
        List<?> elements = element.getChildren(CrystalXmlTags.TAG_NAME);
        Crystal[] crystals = new Crystal[elements.size()];

        for (int i = 0; i < crystals.length; i++)
            crystals[i] =
                    new CrystalXmlLoader().load((Element) elements.get(i));

        // Camera
        elements = element.getChildren(CameraXmlTags.TAG_NAME);
        Camera[] cameras = new Camera[elements.size()];

        for (int i = 0; i < cameras.length; i++)
            cameras[i] = new CameraXmlLoader().load((Element) elements.get(i));

        // Energy
        elements = element.getChildren(EnergyXmlTags.TAG_NAME);
        Energy[] energies = new Energy[elements.size()];

        for (int i = 0; i < energies.length; i++)
            energies[i] = new EnergyXmlLoader().load((Element) elements.get(i));

        // Rotation
        elements = element.getChildren(QuaternionXmlTags.TAG_NAME);
        Quaternion[] rotations = new Quaternion[elements.size()];

        for (int i = 0; i < rotations.length; i++)
            rotations[i] =
                    new QuaternionXmlLoader().load((Element) elements.get(i));

        /* Operations */
        ArrayList<Operation> ops = new ArrayList<Operation>();

        // Pattern Sim Op
        String tagName = PatternSimOpXmlTags.TAG_NAME;
        String packageName =
                FileUtil.joinPackageNames(PATTERNSIM_IO_PACKAGE, "op");
        ops.add(loadOperation(element, tagName, packageName));

        // Output Ops
        tagName = OutputOpsXmlTags.TAG_NAME;
        packageName = OUTPUT_IO_PACKAGE;
        ops.addAll(loadOperations(element, tagName, packageName));

        // Constructor simulation
        Sim sim =
                new Sim(ops.toArray(new Operation[ops.size()]), cameras,
                        crystals, energies, rotations);
        sim.setName(name);
        sim.setDir(path);

        return sim;
    }
}
