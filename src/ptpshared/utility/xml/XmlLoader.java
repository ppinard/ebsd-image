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
package ptpshared.utility.xml;

import java.io.File;
import java.io.IOException;

import rmlshared.io.Loader;

/**
 * Abstract class to load an <code>ObjectXml</code> from an XML file.
 * 
 * @author Philippe T. Pinard
 * 
 */
public abstract class XmlLoader implements Loader {

    /** Progress of the loading process. */
    protected double progress = 0.0;



    @Override
    public double getTaskProgress() {
        return progress;
    }



    /**
     * Loads an XML file to an <code>ObjectXml</code>.
     * 
     * @param file
     *            an XML file
     * @return an <code>ObjectXML</code>
     * @throws IOException
     *             if an error occurs in the process
     */
    @Override
    public ObjectXml load(File file) throws IOException {
        return getXmlLoader().load(JDomUtil.loadXML(file).getRootElement());
    }



    /**
     * Loads an XML file to an <code>ObjectXml</code>.
     * 
     * @param file
     *            an XML file
     * @param obj
     *            ignored, not used in the loading process
     * @return an <code>ObjectXML</code>
     * @throws IOException
     *             if an error occurs in the process
     */
    @Override
    public ObjectXml load(File file, Object obj) throws IOException {
        return load(file);
    }



    /**
     * Returns the <code>ObjectXmlLoader</code> to use to load the XML file to
     * an <code>ObjectXml</code>.
     * 
     * @return <code>ObjectXmlLoader</code> for this loader
     */
    protected abstract ObjectXmlLoader getXmlLoader();

}
