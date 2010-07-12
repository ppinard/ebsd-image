package org.ebsdimage.vendors.tsl.gui;

import org.ebsdimage.vendors.tsl.io.TslMMapLoader;

import rmlimage.module.multi.gui.MultiMapStitcher;
import rmlimage.module.stitch.Tessera;

/**
 * Multimap stitcher for TSL EBSD multimaps.
 * 
 * @author ppinard
 */
public class TslMMapStitcher extends MultiMapStitcher {

    @Override
    public boolean isCorrect(Tessera[][] tesserae) {
        int nbRows = tesserae.length;
        int nbColumns = tesserae[0].length;

        for (int row = 0; row < nbRows; row++)
            for (int column = 0; column < nbColumns; column++)
                if (!TslMMapLoader.isTslMMap(tesserae[row][column].file))
                    return false;

        return true;
    }

}
