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
package org.ebsdimage.gui.exp;

import org.ebsdimage.io.exp.ExpMMapLoader;

import rmlimage.module.multi.gui.MultiMapStitcher;
import rmlimage.module.stitch.Tessera;

/**
 * Multimap stitcher for experiment's EBSD multimaps.
 * 
 * @author Philippe T. Pinard
 */
public class ExpMMapStitcher extends MultiMapStitcher {

    @Override
    public boolean isCorrect(Tessera[][] tesserae) {
        int nbRows = tesserae.length;
        int nbColumns = tesserae[0].length;

        ExpMMapLoader loader = new ExpMMapLoader();
        for (int row = 0; row < nbRows; row++)
            for (int column = 0; column < nbColumns; column++)
                if (!loader.canLoad(tesserae[row][column].file))
                    return false;

        return true;
    }

}
