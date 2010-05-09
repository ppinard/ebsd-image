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
package org.ebsdimage.plugin;

import javax.swing.ButtonGroup;

import org.ebsdimage.core.HoughMap;
import org.ebsdimage.core.Threshold;

import rmlimage.RMLImage;
import rmlimage.core.BinMap;
import rmlimage.core.Map;
import rmlimage.gui.BasicDialog;
import rmlimage.plugin.PlugIn;
import rmlshared.gui.ColumnPanel;
import rmlshared.gui.RadioButton;

/**
 * Plug-in to perform a thresholding on a Hough map. The user can select between
 * a top hat thresholding and a two sigma standard deviation thresholding.
 * 
 * @author Marin Lagac&eacute;
 * @see Threshold
 */
public class Thresholding extends PlugIn {

    /**
     * Dialog to select the thresholding.
     * 
     * @author Marin Lagac&eacute;
     * 
     */
    private class Dialog extends BasicDialog {

        /** Radio button for the top hat thresholding. */
        private RadioButton tophatButton;

        /** Radio button for the two sigma thresholding. */
        private RadioButton twoSigmaButton;



        /**
         * Creates a new dialog to select the thresholding.
         */
        public Dialog() {
            super("Thresholding");

            // Create the buttons
            tophatButton = new RadioButton("Top Hat (classic)", true);
            tophatButton.setName("TopHat");
            twoSigmaButton = new RadioButton("2Sigma (aggressive)", false);
            twoSigmaButton.setName("2Sigma");

            // Create the button group
            ButtonGroup buttonGroup = new ButtonGroup();
            buttonGroup.add(tophatButton);
            buttonGroup.add(twoSigmaButton);

            // Create the control panel
            ColumnPanel controlPanel = new ColumnPanel(1);
            controlPanel.setAlignment(0, ColumnPanel.LEFT);

            // Place the buttons
            controlPanel.add(tophatButton);
            controlPanel.add(twoSigmaButton);

            controlPanel.setPreferences(getPlugIn().getPreferences().node(
                    "EBSD.Thresholding"));

            setMainComponent(controlPanel); // Add the control panel to the
            // dialog

        }



        /**
         * Returns the thresholding method selected.
         * 
         * @return thresholding
         */
        public Method getMethod() {
            if (tophatButton.isSelectedBFR())
                return Method.TOPHAT;
            if (twoSigmaButton.isSelectedBFR())
                return Method.TWOSIGMA;
            throw new AssertionError(
                    "Neither tophat or 2sigma buttons are selected.");
        }

    }



    /**
     * Two types of thresholding.
     * 
     * @author Marin Lagac&eacute;
     * 
     */
    private enum Method {
        /** Top hat thresholding. */
        TOPHAT,

        /** Two sigma standard deviation thresholding. */
        TWOSIGMA
    }



    /**
     * Performs the thresholding selected in the dialog on the currently
     * selected Hough map.
     * 
     * @return the resultant threshold map
     */
    private BinMap doThresholding() {
        if (!areMapsLoaded(true))
            return null;

        Map map = RMLImage.getSelectedMap();

        if (!isCorrectType(map, HoughMap.class, true))
            return null;

        HoughMap houghMap = (HoughMap) map;

        Dialog dialog = new Dialog();
        if (dialog.show() == Dialog.CANCEL)
            return null;

        // Do the thresholding according to the method chosen
        BinMap binMap = null;
        switch (dialog.getMethod()) {
        case TOPHAT:
            binMap = Threshold.automaticTopHat(houghMap);
            break;

        case TWOSIGMA:
            binMap = Threshold.automaticStdDev(houghMap);
            break;

        default:
            throw new AssertionError("Invalid method: " + dialog.getMethod());
        }

        RMLImage.add(binMap);

        return binMap;
    }



    @Override
    public void xRun() {
        doThresholding();
    }

}
