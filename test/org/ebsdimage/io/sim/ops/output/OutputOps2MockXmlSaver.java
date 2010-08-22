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
package org.ebsdimage.io.sim.ops.output;

import static org.ebsdimage.io.sim.ops.output.OutputOps2MockXmlTags.TAG_NAME;

import org.ebsdimage.core.sim.ops.output.OutputOps2Mock;
import org.jdom.Element;

import ptpshared.utility.xml.ObjectXml;
import ptpshared.utility.xml.ObjectXmlSaver;

public class OutputOps2MockXmlSaver implements ObjectXmlSaver {

    @Override
    public Element save(ObjectXml obj) {
        return save((OutputOps2Mock) obj);
    }



    public Element save(OutputOps2Mock obj) {
        return new Element(TAG_NAME);
    }

}
