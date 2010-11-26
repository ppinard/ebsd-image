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
package ptpshared.util.xml;

import java.io.File;
import java.io.IOException;

import org.simpleframework.xml.convert.AnnotationStrategy;
import org.simpleframework.xml.core.Persister;

import rmlshared.ui.Monitorable;

/**
 * Save/Serialize an <code>Object</code> to an XML file.
 * 
 * @author Philippe T. Pinard
 */
public class XmlSaver implements Monitorable {

    /** Persister to serialize XML. */
    private static Persister persister;

    /** Progress of the saving process. */
    protected double progress = 0.0;

    /** Progress status. */
    protected String status = "";



    /**
     * Creates a new <code>XmlSaver</code>.
     */
    public XmlSaver() {
        AnnotationStrategy strategy = new AnnotationStrategy();
        persister = new Persister(strategy);
    }



    @Override
    public double getTaskProgress() {
        return progress;
    }



    /**
     * Saves the <code>Object</code> to an XMLfile.
     * 
     * @param obj
     *            an <code>Object</code>
     * @param file
     *            XML file
     * @throws IOException
     *             if an error occurs during the saving process
     */
    public void save(Object obj, File file) throws IOException {
        try {
            persister.write(obj, file);
        } catch (Exception e) {
            throw new IOException(e);
        }
    }



    /**
     * Saves an array of <code>Object</code> to an XML file.
     * 
     * @param obj
     *            an array of <code>Object</code>
     * @param file
     *            XML file
     * @throws IOException
     *             if an error occurs during the saving process
     */
    public void saveArray(Object[] obj, File file) throws IOException {
        save(new ArrayXML(obj), file);
    }



    @Override
    public String getTaskStatus() {
        return status;
    }

}
