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

import rmlshared.io.Saver;

/**
 * Abstract class to save an <code>ObjectXml</code> to an XML file.
 * 
 * @author Philippe T. Pinard
 * 
 */
public abstract class XmlSaver implements Saver {

    /** Progress of the saving process. */
    protected double progress = 0.0;



    @Override
    public double getTaskProgress() {
        return progress;
    }



    /**
     * Saves the <code>ObjectXml</code> to an XML <code>Element</code>.
     * 
     * @param obj
     *            an <code>ObjectXml</code>
     * @param file
     *            XML file
     * @throws IOException
     *             if an error occurs during the saving process
     * @throws ClassCastException
     *             if the <code>obj</code> is not an <code>ObjectXml</code>
     */
    @Override
    public void save(Object obj, File file) throws IOException {
        save((ObjectXml) obj, file);
    }



    /**
     * Saves the <code>ObjectXml</code> to an XML <code>Element</code>.
     * 
     * @param obj
     *            an <code>ObjectXml</code>
     * @param file
     *            XML file
     * @throws IOException
     *             if an error occurs during the saving process
     */
    public void save(ObjectXml obj, File file) throws IOException {
        JDomUtil.saveXML(getXmlSaver().save(obj), file);
    }



    /**
     * Returns the <code>ObjectXmlSaver</code> to use to save the
     * <code>ObjectXml</code> to an XML file.
     * 
     * @return <code>ObjectXmlSaver</code> for this saver
     */
    protected abstract ObjectXmlSaver getXmlSaver();

}
