package org.ebsdimage.gui.exp;

import org.ebsdimage.io.exp.ExpMMapLoader;

import rmlimage.module.multi.gui.MultiMapStitcher;
import rmlimage.module.stitch.Tessera;

/**
 * Multimap stitcher for experiment's EBSD multimaps.
 * 
 * @author ppinard
 */
public class ExpMMapStitcher extends MultiMapStitcher {

    @Override
    public boolean isCorrect(Tessera[][] tesserae) {
        int nbRows = tesserae.length;
        int nbColumns = tesserae[0].length;

        for (int row = 0; row < nbRows; row++)
            for (int column = 0; column < nbColumns; column++)
                if (!ExpMMapLoader.isExpMMap(tesserae[row][column].file))
                    return false;

        return true;
    }

}
