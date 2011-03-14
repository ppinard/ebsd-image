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

import ptpshared.util.simplexml.XmlSaver;
import rmlimage.module.multi.core.MultiMap;
import rmlimage.module.multi.io.ZipSaver;

/**
 * Saver for an <code>EbsdMMap</code> to a zip file.
 * 
 * @author Philippe T. Pinard
 */
public abstract class EbsdMMapSaver extends ZipSaver {

    static {
        rmlimage.io.IO.addSaver(rmlimage.module.real.io.RmpSaver.class);
        rmlimage.io.IO.addSaver(org.ebsdimage.io.PhaseMapSaver.class);
        rmlimage.io.IO.addSaver(org.ebsdimage.io.ErrorMapSaver.class);
    }



    @Override
    protected void saveOtherFiles(MultiMap mmap, File zipDir)
            throws IOException {
        super.saveOtherFiles(mmap, zipDir);

        File file = new File(zipDir, "metadata.xml");
        new XmlSaver().save(((EbsdMMap) mmap).getMetadata(), file);
    }
}
