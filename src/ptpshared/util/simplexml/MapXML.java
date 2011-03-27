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

import java.util.Map;

import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.Root;

/**
 * Object used to serialize and deserialize a key-value map.
 * 
 * @author Philippe T. Pinard
 */
@Root(name = "map")
public class MapXML {

    /** Internal map of objects. */
    @ElementMap(entry = "item", key = "key", value = "value", inline = true)
    protected Map<?, ?> items;



    /**
     * Creates a new <code>MapXML</code> from a key-value map.
     * 
     * @param items
     *            a key-value map
     */
    public MapXML(
            @ElementMap(entry = "item", key = "key", value = "value", inline = true) Map<?, ?> items) {
        this.items = items;
    }

}
