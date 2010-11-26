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
import java.io.InputStream;
import java.lang.reflect.Array;

import org.simpleframework.xml.convert.AnnotationStrategy;
import org.simpleframework.xml.core.Persister;

import rmlshared.ui.Monitorable;

/**
 * Loader/Deserializer of an XML file.
 * 
 * @author Philippe T. Pinard
 */
public class XmlLoader implements Monitorable {

    /** Persister to deserialize XML. */
    private static Persister persister;

    /** Progress of the loading process. */
    protected double progress = 0.0;

    /** Progress status. */
    protected String status = "";



    /**
     * Creates a new <code>XmlLoader</code>.
     */
    public XmlLoader() {
        AnnotationStrategy strategy = new AnnotationStrategy();
        persister = new Persister(strategy);
    }



    @Override
    public double getTaskProgress() {
        return progress;
    }



    @Override
    public String getTaskStatus() {
        return status;
    }



    /**
     * Loads an XML file.
     * 
     * @param <T>
     *            class type
     * @param type
     *            this is the class type to be deserialized from XML
     * @param source
     *            this provides the source of the XML document
     * @throws IOException
     *             if an error occurs in the process
     * @return Deserialized XML object
     */
    public <T> T load(Class<? extends T> type, File source) throws IOException {
        try {
            return persister.read(type, source);
        } catch (Exception e) {
            throw new IOException(e);
        }
    }



    /**
     * Loads an array saved in a XML file.
     * 
     * @param <T>
     *            class type
     * @param type
     *            this is the class type of the array element
     * @param source
     *            source file of the XML
     * @return Deserialized XML object
     * @throws IOException
     *             if an error occurs in the process
     */
    @SuppressWarnings("unchecked")
    public <T> T[] loadArray(Class<? extends T> type, File source)
            throws IOException {
        ArrayXML array = load(ArrayXML.class, source);

        T[] a = (T[]) Array.newInstance(type, array.list.size());

        for (int i = 0; i < a.length; i++) {
            a[i] = (T) array.list.get(i);
        }

        return a;
    }



    /**
     * Loads an XML file.
     * 
     * @param type
     *            this is the class type to be deserialized from XML
     * @param source
     *            this provides the source of the XML document
     * @throws IOException
     *             if an error occurs in the process
     * @return Deserialized XML object
     * @param <T>
     *            class type
     */
    public <T> T load(Class<? extends T> type, InputStream source)
            throws IOException {
        try {
            return persister.read(type, source);
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

}
