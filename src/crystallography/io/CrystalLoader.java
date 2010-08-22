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
package crystallography.io;

import java.io.File;
import java.io.IOException;

import ptpshared.utility.xml.ObjectXmlLoader;
import ptpshared.utility.xml.XmlLoader;
import crystallography.core.Crystal;

/**
 * Loads a <code>Crystal</code> from an XML file.
 * 
 * @author Philippe T. Pinard
 */
public class CrystalLoader extends XmlLoader {

    @Override
    public Crystal load(File file) throws IOException {
        return (Crystal) super.load(file);
    }



    @Override
    protected ObjectXmlLoader getXmlLoader() {
        return new CrystalXmlLoader();
    }

}
