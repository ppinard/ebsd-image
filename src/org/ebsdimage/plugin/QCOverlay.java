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

import java.util.ArrayList;

import org.ebsdimage.core.QC;

import rmlimage.RMLImage;
import rmlimage.core.BinMap;
import rmlimage.core.ByteMap;
import rmlimage.core.Map;
import rmlimage.core.RGB;
import rmlimage.gui.BasicDialog;
import rmlimage.plugin.PlugIn;
import rmlshared.gui.ColumnPanel;
import rmlshared.gui.ComboBox;
import rmlshared.gui.IntField;
import rmlshared.gui.OkCancelDialog;

/**
 * Plug-in to overlay the peaks on the pattern map. From a <code>BinMap</code>
 * where the Hough peaks are identified, the plug-in overlays the corresponding
 * lines on the pattern map.
 * 
 * @author Philippe T. Pinard
 */
public class QCOverlay extends PlugIn {

    /**
     * Dialog to select the pattern map and the color of the overlay lines.
     * 
     * @author Marin Lagac&eacute;
     */
    private class Dialog extends BasicDialog {
        /** Combo box to select the pattern map. */
        private ComboBox<ByteMap> comboBox;

        /** Field to select the color of the overlay lines (red). */
        private IntField redField;

        /** Field to select the color of the overlay lines (green). */
        private IntField greenField;

        /** Field to select the color of the overlay lines (blue). */
        private IntField blueField;



        /**
         * Creates a new dialog to select the parameters of the
         * <code>QCOverlay</code>.
         */
        public Dialog() {
            super("QC Overlay");

            ColumnPanel cPanel = new ColumnPanel(2);

            // Create and place the map comboBox
            comboBox = new ComboBox<ByteMap>(getByteMapList());
            cPanel.add("Destination:", comboBox);

            cPanel.add("Overlay color:");
            cPanel.skip();

            // Create and place the red color component field
            redField = new IntField("Red", 255);
            redField.setRange(0, 255);
            cPanel.add("Red:", redField);

            // Create and place the green color component field
            greenField = new IntField("Green", 0);
            greenField.setRange(0, 255);
            cPanel.add("Green:", greenField);

            // Create and place the blue color component field
            blueField = new IntField("Blue", 0);
            blueField.setRange(0, 255);
            cPanel.add("Blue:", blueField);

            setPreferences(getPlugIn().getPreferences().node("EBSD.QCOverlay"));

            setMainComponent(cPanel);

        }



        /**
         * Returns the selected pattern map on which to overlay the lines.
         * 
         * @return pattern map
         */
        public ByteMap getByteMap() {
            return comboBox.getSelectedItemBFR();
        }



        /**
         * Returns the color of the lines in the overlay.
         * 
         * @return color
         */
        public RGB getRGB() {
            return new RGB(redField.getValueBFR(), greenField.getValueBFR(),
                    blueField.getValueBFR());
        }

    }



    /**
     * Performs the overlay from the current selected <code>BinMap</code> onto
     * the pattern map specified in the dialog.
     * 
     * @return <code>true</code> if the no error occurs during the overlay,
     *         <code>false</code> otherwise
     */
    private boolean doOverlay() {
        if (!areMapsLoaded(true))
            return false;

        Map map = RMLImage.getSelectedMap();

        // Check if the selected map is a BinMap
        if (!isCorrectType(map, BinMap.class, true))
            return false;
        BinMap binMap = (BinMap) map;

        if (!binMap.isCalibrated())
            throw new IllegalArgumentException("The map (" + binMap
                    + ") must be calibrated.");
        if (!binMap.getCalibration().getDX().areUnits("rad"))
            throw new IllegalArgumentException("Invalid map, delta x units ("
                    + binMap.getCalibration().getDX().getBaseUnitsLabel()
                    + ") cannot be expressed as \"rad\".");

        // Check if at least one ByteMap is loaded
        if (getByteMapCount() < 1) {
            RMLImage.showErrorDialog("At least one ByteMap "
                    + "must be loaded to use this function.");
            return false;
        }

        Dialog dialog = new Dialog();
        if (dialog.show() != OkCancelDialog.OK)
            return false;

        ByteMap byteMap = dialog.getByteMap();
        RGB rgb = dialog.getRGB();

        getUndoBuffer().set(byteMap);

        new QC().overlay(byteMap, binMap, rgb);

        byteMap.notifyListeners();

        // Move the bytemap to the front
        if (getDesktop() instanceof rmlimage.gui.Desktop)
            ((rmlimage.gui.Desktop) getDesktop()).moveToFront(byteMap);

        return true;
    }



    /**
     * Returns the number of <code>ByteMap</code> in the GUI. Only the implicit
     * <code>ByteMap</code> are counted, not the maps that extends
     * <code>ByteMap</code>.
     * 
     * @return number of <code>ByteMap</code>
     */
    private int getByteMapCount() {
        return getByteMapList().length;
    }



    /**
     * Returns all the <code>ByteMap</code> in the GUI. Only the implicit
     * <code>ByteMap</code> are returned, not the maps that extends
     * <code>ByteMap</code>.
     * 
     * @return array of <code>ByteMap</code> present in the GUI
     */
    private ByteMap[] getByteMapList() {
        Map[] byteMaps = getMapList(ByteMap.class);

        ArrayList<ByteMap> bMaps = new ArrayList<ByteMap>();
        for (Map map : byteMaps)
            if (map.getClass() == ByteMap.class)
                bMaps.add((ByteMap) map);

        return bMaps.toArray(new ByteMap[0]);
    }



    @Override
    protected void xRun() {
        doOverlay();
    }

}
