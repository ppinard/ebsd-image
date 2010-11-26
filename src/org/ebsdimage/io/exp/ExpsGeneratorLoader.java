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

import java.io.File;
import java.io.IOException;

import org.ebsdimage.core.exp.ExpsGenerator;

import ptpshared.util.xml.ObjectXmlLoader;
import ptpshared.util.xml.XmlLoader;

/**
 * Loads <code>ExpsGenerator</code> from an XML file.
 * 
 * @author Philippe T. Pinard
 */
public class ExpsGeneratorLoader extends XmlLoader {

    @Override
    protected ObjectXmlLoader getXmlLoader() {
        return new ExpsGeneratorXmlLoader();
    }



    @Override
    public ExpsGenerator load(File file) throws IOException {
        return (ExpsGenerator) super.load(file);
    }

}
