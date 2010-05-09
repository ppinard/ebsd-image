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
package org.ebsdimage.io;

import org.ebsdimage.core.EbsdMMap;
import org.jdom.Document;
import org.jdom.Element;

import rmlimage.module.multi.core.MultiMap;
import rmlimage.module.multi.io.ZipSaver;

/**
 * Saver for an <code>EbsdMMap</code> to a zip file.
 * 
 * @author Philippe T. Pinard
 */
public abstract class EbsdMMapSaver extends ZipSaver {

    static {
        rmlimage.io.IO.addHandler(rmlimage.module.real.io.IO.class);
        rmlimage.io.IO.addHandler(org.ebsdimage.io.IO.class);
    }



    @Override
    protected Document getMetadata(MultiMap multiMap) {
        if (!(multiMap instanceof EbsdMMap))
            throw new IllegalArgumentException(multiMap.getName() + " is a "
                    + multiMap.getClass().getName() + ". It should be a "
                    + EbsdMMap.class.getName());
        EbsdMMap mmap = (EbsdMMap) multiMap;

        Element root = getMetadataSaver().save(mmap.getMetadata());

        return new Document(root);
    }



    /**
     * Returns the correct XML saver for the metadata.
     * 
     * @return XML saver
     */
    protected abstract EbsdMetadataXmlSaver getMetadataSaver();

}
