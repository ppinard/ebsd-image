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

import org.ebsdimage.core.HoughMap;
import org.ebsdimage.core.PhasesMap;
import org.ebsdimage.core.exp.ExpMMap;
import org.ebsdimage.io.exp.ExpMMapLoader;
import org.ebsdimage.io.exp.ExpMMapSaver;

import rmlimage.core.Map;
import rmlimage.core.handler.IOHandler;
import rmlshared.io.Loader;
import rmlshared.io.Saver;

/**
 * Class holding method to easily load and save <dfn>HoughMap</dfn>s.
 * <p/>
 * The methods in this class will fail if one tries to save Maps of other types
 * than <dfn>HoughMap</dfn>s or to load files of other formats than
 * <code>.hmp</code>. To load or save any types of Maps, use the methods in
 * {@link rmlimage.io.IO} instead.
 * 
 * @author Philippe T. Pinard
 */
public class IO implements IOHandler {

    @Override
    public Saver getSaver(Map map) {
        if (map.getClass().equals(HoughMap.class))
            return new HoughMapSaver();
        else if (map.getClass().equals(PhasesMap.class))
            return new PhasesMapSaver();
        else if (map.getClass().equals(ExpMMap.class))
            return new ExpMMapSaver();
        else
            return null;
    }



    @Override
    public Loader getLoader(File file) {
        if (HoughMapLoader.isHoughMap(file))
            return new HoughMapLoader();
        else if (PhasesMapLoader.isPhasesMap(file))
            return new PhasesMapLoader();
        else if (ExpMMapLoader.isExpMMap(file))
            return new ExpMMapLoader();
        else
            return null;
    }
}
