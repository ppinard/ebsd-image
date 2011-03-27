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

import magnitude.core.Magnitude;

import org.ebsdimage.core.Edit;
import org.ebsdimage.core.HoughMap;

import ptpshared.gui.CalibratedDoubleField;
import rmlimage.core.Map;
import rmlimage.gui.PlugIn;
import rmlshared.gui.BasicDialog;
import rmlshared.gui.ColumnPanel;
import rmlshared.gui.OkCancelDialog;
import rmlshared.io.FileUtil;
import edu.umd.cs.findbugs.annotations.CheckForNull;

/**
 * Plug-in to crop a <code>HoughMap</code> using
 * {@link Edit#crop(HoughMap, double)}.
 * 
 * @author Philippe T. Pinard
 */
public class HoughCrop extends PlugIn {

    /**
     * Dialog to select the distance in rho from which to crop the
     * <code>HoughMap</code>.
     * 
     * @author Philippe T. Pinard
     */
    private static class Dialog extends BasicDialog {

        /** Field for the crop radius. */
        private CalibratedDoubleField rhoField;



        /**
         * Creates a new Dialog to select the distance in rho from which to crop
         * the <code>HoughMap</code>.
         * 
         * @param map
         *            <code>HoughMap</code> to crop
         */
        public Dialog(HoughMap map) {
            super("Crop");

            ColumnPanel cPanel = new ColumnPanel(3);

            cPanel.add("Positive \u03c1:");

            String rhoUnits = map.getDeltaRho().getPreferredUnitsLabel();
            String[] units = new String[] { rhoUnits };
            Magnitude rhoMax = new Magnitude(map.getRhoMax(), rhoUnits);

            rhoField = new CalibratedDoubleField("Rho", 5, rhoMax, units, true);
            rhoField.setRange(new Magnitude(0.1, rhoMax), rhoMax);
            cPanel.add(rhoField);

            setMainComponent(cPanel);
        }



        /**
         * Returns the distance in rho from which to drop the
         * <code>HoughMap</code>.
         * 
         * @return distance in rho
         */
        public Magnitude getRho() {
            return rhoField.getValueBFR();
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

        double rho = dialog.getRho().getPreferredUnitsValue();
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
