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
package org.ebsdimage.vendors.hkl.io;

import java.io.File;

import org.ebsdimage.vendors.hkl.core.HklMMap;

import rmlimage.core.Map;
import rmlimage.core.handler.IOHandler;
import rmlshared.io.Loader;
import rmlshared.io.Saver;

/**
 * Class holding method to easily load and save <code>HklMMap</code>s.
 * <p/>
 * The methods in this class will fail if one tries to save Maps of other types
 * than <code>HklMMap</code>s or to load files of other formats than
 * <code>HKL</code>'s <code>ctf</code> format or properly formatted
 * <code>zip</code> files. To load or save any types of Maps, use the methods in
 * {@link rmlimage.io.IO} instead.
 * 
 * @author Philippe T. Pinard
 */
public class IO implements IOHandler {

    @Override
    public Saver getSaver(Map map) {
        if (map.getClass().equals(HklMMap.class))
            return new HklMMapSaver();
        else
            return null;
    }



    @Override
    public Loader getLoader(File file) {
        if (HklMMapLoader.isHklMMap(file))
            return new HklMMapLoader();
        else
            return null;
    }

}
