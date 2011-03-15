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

import org.ebsdimage.core.EbsdMMap;
import org.ebsdimage.core.EbsdMetadata;

import ptpshared.util.simplexml.ApacheCommonMathMatcher;
import ptpshared.util.simplexml.XmlLoader;
import rmlimage.module.multi.core.MultiMap;
import rmlimage.module.multi.io.ZipLoader;

/**
 * Loader for an <code>EbsdMMap</code> from a zip file.
 * 
 * @author Philippe T. Pinard
 */
public abstract class EbsdMMapLoader extends ZipLoader {

    static {
        rmlimage.io.IO.addLoader(rmlimage.module.real.io.RmpLoader.class);
        rmlimage.io.IO.addLoader(org.ebsdimage.io.PhaseMapLoader.class);
        rmlimage.io.IO.addLoader(org.ebsdimage.io.ErrorMapLoader.class);
    }



    /**
     * Returns the class of the <code>EbsdMetadata</code> for this
     * <code>EbsdMMap</code>.
     * 
     * @return class of the <code>EbsdMetadata</code>
     */
    protected abstract Class<? extends EbsdMetadata> getMetadataClass();



    @Override
    protected void loadOtherFiles(MultiMap mmap, File zipDir)
            throws IOException {
        super.loadOtherFiles(mmap, zipDir);

        File metadataFile = new File(zipDir, "metadata.xml");
        if (!metadataFile.exists())
            throw new IOException(
                    "The EbsdMMap does not contain a metadata.xml file.");

        XmlLoader loader = new XmlLoader();
        loader.matchers.registerMatcher(new ApacheCommonMathMatcher());

        EbsdMetadata metadata = loader.load(getMetadataClass(), metadataFile);
        ((EbsdMMap) mmap).setMetadata(metadata);
    }

}
