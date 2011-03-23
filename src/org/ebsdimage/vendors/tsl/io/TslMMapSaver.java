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
package org.ebsdimage.vendors.tsl.io;

import org.ebsdimage.io.EbsdMMapSaver;
import org.ebsdimage.vendors.tsl.core.TslMMap;

/**
 * Saver for a <code>TslMMap</code>. A <code>TslMMap</code> is saved in a human
 * readable ZIP file.
 * 
 * @author Philippe T. Pinard
 */
public class TslMMapSaver extends EbsdMMapSaver {

    @Override
    public boolean canSave(Object obj, String fileFormat) {
        return (obj instanceof TslMMap) && fileFormat.equalsIgnoreCase("zip");
    }



    @Override
    protected String getHeader() {
        return TslMMap.FILE_HEADER;
    }



    @Override
    protected int getVersion() {
        return TslMMap.VERSION;
    }

}
