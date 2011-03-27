/*
 * EBSD-Image
 * Copyright (C) 2010-2011 Philippe T. Pinard
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
package ptpshared.util.simplexml;

import java.util.ArrayList;
import java.util.Arrays;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

/**
 * Object used to serialize and deserialize an array of Object.
 * 
 * @author Philippe T. Pinard
 */
@Root(name = "array")
public class ArrayXML {

    /** Internal list of objects. */
    @ElementList(inline = true)
    protected ArrayList<Object> list;



    /**
     * Creates a new <code>ArrayXML</code> from an array list of objects.
     * 
     * @param array
     *            objects
     */
    public ArrayXML(@ElementList(inline = true) ArrayList<Object> array) {
        list = new ArrayList<Object>();
        list.addAll(array);
    }



    /**
     * Creates a new <code>ArrayXML</code> from an array of objects.
     * 
     * @param array
     *            objects
     */
    public ArrayXML(Object[] array) {
        list = new ArrayList<Object>();
        list.addAll(Arrays.asList(array));
    }

}
