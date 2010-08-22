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
package org.ebsdimage.io.exp;

import static org.ebsdimage.core.exp.ExpConstants.OPS_CORE_PACKAGE;
import static org.ebsdimage.core.exp.ExpConstants.OPS_IO_PACKAGE;
import static org.ebsdimage.io.exp.ExpsGeneratorXmlTags.TAG_NAME;

import org.ebsdimage.core.run.Operation;
import org.ebsdimage.io.run.RunsGeneratorXmlSaver;
import org.jdom.Element;

import ptpshared.io.FileUtil;
import ptpshared.utility.xml.ObjectXmlSaver;
import rmlshared.thread.Reflection;

/**
 * XML saver for <code>Exp</code>.
 * 
 * @author Philippe T. Pinard
 */
public class ExpsGeneratorXmlSaver extends RunsGeneratorXmlSaver {

    @Override
    protected ObjectXmlSaver getOperationSaver(Operation op) {
        String opName =
                op.getClass().getName().replaceFirst(OPS_CORE_PACKAGE, "");
        String classLoaderName =
                FileUtil.joinPackageNames(OPS_IO_PACKAGE, opName + "XmlSaver");
        return (ObjectXmlSaver) Reflection.newInstance(classLoaderName);
    }



    @Override
    protected Element createElement() {
        return new Element(TAG_NAME);
    }



    @Override
    protected Element getSuperclassOperationElement(String opName) {
        return new Element(opName.replaceFirst(OPS_CORE_PACKAGE + ".", ""));
    }

}
