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
package org.ebsdimage.plugin;

import javax.swing.ButtonGroup;

import org.ebsdimage.core.Conversion;
import org.ebsdimage.core.HoughMap;
import org.ebsdimage.core.Threshold;

import rmlimage.RMLImage;
import rmlimage.core.BinMap;
import rmlimage.core.ByteMap;
import rmlimage.core.Map;
import rmlimage.core.MathMorph;
import rmlimage.gui.BasicDialog;
import rmlimage.plugin.PlugIn;
import rmlshared.gui.ColumnPanel;
import rmlshared.gui.DoubleField;
import rmlshared.gui.OkCancelDialog;
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
     */
    private class Dialog extends BasicDialog {

        /** Radio button for the top hat thresholding. */
        private RadioButton tophatButton;

        /** Radio button for the standard deviation thresholding. */
        private RadioButton stdDevButton;



        /**
         * Creates a new dialog to select the thresholding.
         */
        public Dialog() {
            super("Thresholding");

            // Create the buttons
            tophatButton = new RadioButton("Top Hat (classic)", true);
            tophatButton.setName("Top hat");
            stdDevButton = new RadioButton("Standard deviation", false);
            stdDevButton.setName("Standard deviation");

            // Create the button group
            ButtonGroup buttonGroup = new ButtonGroup();
            buttonGroup.add(tophatButton);
            buttonGroup.add(stdDevButton);

            // Create the control panel
            ColumnPanel controlPanel = new ColumnPanel(1);
            controlPanel.setAlignment(0, ColumnPanel.LEFT);

            // Place the buttons
            controlPanel.add(tophatButton);
            controlPanel.add(stdDevButton);

            controlPanel.setPreferences(getPlugIn().getPreferences().node(
                    "EBSD.Thresholding"));

            // Add the control panel to the dialog
            setMainComponent(controlPanel);

        }



        /**
         * Returns the thresholding method selected.
         * 
         * @return thresholding
         */
        public Method getMethod() {
            if (tophatButton.isSelectedBFR())
                return Method.TOPHAT;
            if (stdDevButton.isSelectedBFR())
                return Method.STDDEV;
            throw new AssertionError(
                    "Neither tophat or standard deviation buttons are selected.");
        }

    }

    /**
     * Two types of thresholding.
     * 
     * @author Marin Lagac&eacute;
     */
    private enum Method {
        /** Top hat thresholding. */
        TOPHAT,

        /** Standard deviation thresholding. */
        STDDEV
    }

    /**
     * Dialog to select the sigma factor for the automatic std. dev.
     * thresholding.
     * 
     * @author Philippe T. Pinard
     */
    private static class StdDevDialog extends BasicDialog {

        /** Double field for the sigma factor. */
        private DoubleField sigmaFactorField;



        /**
         * Creates a new dialog.
         */
        public StdDevDialog() {
            super("Standard Deviation Thresholding");

            ColumnPanel panel = new ColumnPanel(2);

            panel.add("Sigma factor");
            sigmaFactorField = new DoubleField("Sigma factor", 2);
            sigmaFactorField.setRange(0, Double.MAX_VALUE);
            panel.add(sigmaFactorField);

            setMainComponent(panel);
        }



        /**
         * Returns the sigma factor value.
         * 
         * @return sigma factor
         */
        public double getSigmaFactor() {
            return sigmaFactorField.getValueBFR();
        }
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
        if (dialog.show() == OkCancelDialog.CANCEL)
            return null;

        // Do the thresholding according to the method chosen
        BinMap binMap = null;
        switch (dialog.getMethod()) {
        case TOPHAT:
            // Thresholding
            binMap = Threshold.automaticTopHat(houghMap);

            // Remove small peaks
            MathMorph.opening(binMap, 2, 8);

            break;

        case STDDEV:
            StdDevDialog stdDevDialog = new StdDevDialog();
            if (stdDevDialog.show() != OkCancelDialog.OK)
                return null;

            // Perform inversion division
            ByteMap dup = Conversion.toByteMap(houghMap);
            rmlimage.core.MapMath.not(dup);
            rmlimage.core.MapMath.division(houghMap, dup, 128.0, 0, dup);

            // Important to get a hough-related binMap
            dup.setProperties(houghMap);

            // Thresholding
            binMap =
                    Threshold.automaticStdDev(houghMap,
                            stdDevDialog.getSigmaFactor());

            // Remove small peaks
            MathMorph.opening(binMap, 2, 8);

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
