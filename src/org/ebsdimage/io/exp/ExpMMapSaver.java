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
package org.ebsdimage.io.exp;

import org.ebsdimage.core.exp.ExpMMap;
import org.ebsdimage.io.EbsdMMapSaver;

/**
 * Saver for a <code>ExpMMap</code>. A <code>ExpMMap</code> is saved in a human
 * readable zip file.
 * 
 * @author Philippe T. Pinard
 */
public class ExpMMapSaver extends EbsdMMapSaver {

    @Override
    public boolean canSave(Object obj, String fileFormat) {
        return (obj instanceof ExpMMap) && fileFormat.equalsIgnoreCase("zip");
    }



    @Override
    protected String getHeader() {
        return ExpMMap.FILE_HEADER;
    }



    @Override
    protected int getVersion() {
        return ExpMMap.VERSION;
    }

}
