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
package org.ebsdimage.gui.exp;

import org.ebsdimage.core.HoughMap;
import org.ebsdimage.core.exp.CurrentMapsSaver;
import org.ebsdimage.core.exp.Exp;
import org.ebsdimage.core.run.Operation;

import rmlimage.RMLImage;
import rmlimage.core.BinMap;
import rmlimage.core.ByteMap;
import rmlimage.core.Map;

/**
 * Display current maps from an experiment in the GUI.
 * 
 * @author Philippe T. Pinard
 * 
 */
public class CurrentMapsGUISaver implements CurrentMapsSaver {

    /**
     * Adds a map to the GUI Desktop.
     * 
     * @param map
     *            map to add
     */
    private void add(Map map) {
        RMLImage.getDesktop().add(map);
    }



    /**
     * Creates a name for a map from the experiment's name, a given name and
     * experiment's index.
     * 
     * @param exp
     *            an <code>Exp</code>
     * @param name
     *            a given name
     * @return name
     */
    private String createName(Exp exp, String name) {
        return name + "_" + exp.getCurrentIndex();
    }



    @Override
    public void saveHoughMap(Exp exp, HoughMap map) {
        map.setName(createName(exp, "Hough"));
        add(map);
    }



    @Override
    public void saveMap(Exp exp, Operation op, Map map) {
        map.setName(createName(exp, op.getName()));
        add(map);
    }



    @Override
    public void savePatternMap(Exp exp, ByteMap map) {
        map.setName(createName(exp, "Pattern"));
        add(map);
    }



    @Override
    public void savePeaksMap(Exp exp, BinMap map) {
        map.setName(createName(exp, "Peaks"));
        add(map);
    }

}
