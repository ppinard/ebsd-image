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

import static org.ebsdimage.io.sim.SimsGeneratorXmlTags.TAG_NAME;

import org.ebsdimage.core.sim.SimsGeneratorTester;
import org.ebsdimage.core.sim.ops.output.OutputOps2Mock;
import org.ebsdimage.core.sim.ops.output.OutputOpsMock;
import org.ebsdimage.io.sim.ops.output.OutputOps2MockXmlSaver;
import org.ebsdimage.io.sim.ops.output.OutputOpsMockXmlSaver;
import org.jdom.Element;


public abstract class SimsGeneratorXmlTester extends SimsGeneratorTester {

    public static Element createElement() {
        Element element = new Element(TAG_NAME);
        Element child;
        Element parent;

        parent = new Element("output");

        child = new OutputOpsMockXmlSaver().save(new OutputOpsMock());
        child.setText(Integer.toString(1));
        parent.addContent(child);

        child = new OutputOps2MockXmlSaver().save(new OutputOps2Mock());
        child.setText(Integer.toString(2));
        parent.addContent(child);

        child = new OutputOpsMockXmlSaver().save(new OutputOpsMock());
        child.setText(Integer.toString(3));
        parent.addContent(child);

        child = new OutputOpsMockXmlSaver().save(new OutputOpsMock());
        child.setText(Integer.toString(1));
        parent.addContent(child);

        element.addContent(parent);

        return element;
    }

}
