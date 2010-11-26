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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.ebsdimage.core.EbsdMetadata;

import ptpshared.util.xml.XmlLoader;
import rmlimage.module.multi.io.ZipLoader;

/**
 * Loader for an <code>EbsdMMap</code> from a zip file.
 * 
 * @author Philippe T. Pinard
 */
public abstract class EbsdMMapLoader extends ZipLoader {

    static {
        rmlimage.io.IO.addLoader(rmlimage.module.real.io.RmpLoader.class);
        rmlimage.io.IO.addLoader(org.ebsdimage.io.PhasesMapLoader.class);
        rmlimage.io.IO.addLoader(org.ebsdimage.io.ErrorMapLoader.class);
    }



    /**
     * Returns the metadata inside a EbsdMMap.
     * 
     * @param file
     *            file of the EbsdMMap
     * @param metadataClasz
     *            class of the metadata to read
     * @return metadata
     * @throws IOException
     *             if an error occurs while reading the zip and extracting the
     *             metadata
     */
    protected EbsdMetadata getMetadata(File file,
            Class<? extends EbsdMetadata> metadataClasz) throws IOException {
        ZipFile zipFile = new ZipFile(file);

        ZipEntry metadataEntry = zipFile.getEntry("metadata.xml");
        if (metadataEntry == null)
            throw new IOException(
                    "The EbsdMMap does not contain a metadata.xml file.");

        InputStream in = zipFile.getInputStream(metadataEntry);
        EbsdMetadata metdata = new XmlLoader().load(metadataClasz, in);

        zipFile.close();

        return metdata;
    }

}
