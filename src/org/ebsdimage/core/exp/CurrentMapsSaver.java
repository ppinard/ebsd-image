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
package org.ebsdimage.core.exp;

import org.ebsdimage.core.HoughMap;
import org.ebsdimage.core.run.Operation;

import ptpshared.utility.xml.ObjectXml;
import rmlimage.core.BinMap;
import rmlimage.core.ByteMap;
import rmlimage.core.Map;

/**
 * Interface for saving current maps of an experiment.
 * 
 * @author Philippe T. Pinard
 * 
 */
public interface CurrentMapsSaver extends ObjectXml {

    /**
     * Saves the pattern map.
     * 
     * @param exp
     *            experiment executing this method
     * @param map
     *            pattern map
     */
    public void savePatternMap(Exp exp, ByteMap map);



    /**
     * Saves the Hough map.
     * 
     * @param exp
     *            experiment executing this method
     * @param map
     *            Hough map
     */
    public void saveHoughMap(Exp exp, HoughMap map);



    /**
     * Saves the peaks map.
     * 
     * @param exp
     *            experiment executing this method
     * @param map
     *            peaks map
     */
    public void savePeaksMap(Exp exp, BinMap map);



    /**
     * Saves any map.
     * 
     * @param exp
     *            experiment executing this method
     * @param op
     *            operation that created the map
     * @param map
     *            a map
     */
    public void saveMap(Exp exp, Operation op, Map map);
}
