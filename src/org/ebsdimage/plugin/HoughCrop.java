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

import org.ebsdimage.core.Edit;
import org.ebsdimage.core.HoughMap;

import rmlimage.core.Map;
import rmlimage.gui.PlugIn;
import rmlshared.gui.*;
import rmlshared.io.FileUtil;
import edu.umd.cs.findbugs.annotations.CheckForNull;

/**
 * Plug-in to crop a <code>HoughMap</code> using
 * {@link Edit#crop(HoughMap, double)}.
 * 
 * @author Philippe T. Pinard
 */
public class HoughCrop extends PlugIn {

    /** Item of the combo box for uncalibrated unit (i.e. px). */
    private static final String UNCALIBRATED_UNIT = "px (uncalibrated)";

    /**
     * Dialog to select the distance in rho from which to crop the
     * <code>HoughMap</code>.
     * 
     * @author Philippe T. Pinard
     */
    private static class Dialog extends BasicDialog {

        /** Field for the crop radius. */
        private DoubleField rhoField;

        /** Combo box to select the units. */
        private ComboBox<String> unitsCBox;

        /** Map to crop. */
        private final HoughMap map;



        /**
         * Creates a new Dialog to select the distance in rho from which to crop
         * the <code>HoughMap</code>.
         * 
         * @param map
         *            <code>HoughMap</code> to crop
         */
        public Dialog(HoughMap map) {
            super("Crop");

            this.map = map;

            ColumnPanel cPanel = new ColumnPanel(3);

            cPanel.add("Positive \u03c1:");

            rhoField = new DoubleField("Rho", map.getRhoMax());
            rhoField.setRange(0.0, Double.POSITIVE_INFINITY);
            cPanel.add(rhoField);

            unitsCBox =
                    new ComboBox<String>(UNCALIBRATED_UNIT,
                            map.getCalibration().unitsY);
            unitsCBox.setName("units");
            unitsCBox.setSelectedItem(map.getCalibration().unitsY);
            cPanel.add(unitsCBox);

            setMainComponent(cPanel);
        }



        @Override
        public boolean isCorrect() {
            if (!super.isCorrect())
                return false;

            double rho = rhoField.getValue();
            double max;
            if (unitsCBox.getSelectedItem() == UNCALIBRATED_UNIT)
                max = map.height / 2;
            else
                max = map.getRhoMax();

            if (rho > max) {
                ErrorDialog.show("rho (" + rho + ") must be less than " + max
                        + ".");
                return false;
            }

            return true;
        }



        /**
         * Returns the distance in rho from which to crop the
         * <code>HoughMap</code>.
         * 
         * @return distance in rho
         */
        public double getRho() {
            return rhoField.getValueBFR();
        }



        /**
         * Returns the units of rho.
         * 
         * @return units of rho
         */
        public String getUnits() {
            return unitsCBox.getSelectedItemBFR();
        }

    }



    /**
     * Crops the currently selected map if it is a <code>HoughMap</code>.
     * 
     * @return the cropped map or <code>null</code>
     */
    @CheckForNull
    private HoughMap doCrop() {
        if (!areMapsLoaded(true))
            return null;

        Map map = getSelectedMap();

        if (!isCorrectType(map, HoughMap.class, true))
            return null;

        HoughMap srcMap = (HoughMap) map;

        Dialog dialog = new Dialog(srcMap);

        if (dialog.show() != OkCancelDialog.OK)
            return null;

        String units = dialog.getUnits();

        double rho = dialog.getRho();
        if (units == UNCALIBRATED_UNIT)
            rho *= srcMap.getCalibration().dy;

        HoughMap cropMap = Edit.crop(srcMap, rho);

        cropMap.setFile(FileUtil.append(srcMap.getFile(), "(Crop)"));

        rmlimage.RMLImage.add(cropMap);

        return cropMap;
    }



    @Override
    protected void xRun() throws Exception {
        doCrop();
    }

}
