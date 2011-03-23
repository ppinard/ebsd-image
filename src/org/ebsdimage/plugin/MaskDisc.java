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

import rmlimage.RMLImage;
import rmlimage.core.BinMap;
import rmlimage.core.Map;
import rmlimage.gui.BasicDialog;
import rmlimage.plugin.PlugIn;
import rmlshared.gui.ColumnPanel;
import rmlshared.gui.ErrorDialog;
import rmlshared.gui.IntField;
import rmlshared.gui.OkCancelDialog;

/**
 * Plug-in to create a mask disc.
 * 
 * @author Philippe T. Pinard
 */
public class MaskDisc extends PlugIn {

    /**
     * Dialog to specify the position and size of the disc.
     * 
     * @author Philippe T. Pinard
     */
    private class Dialog extends BasicDialog {

        /** Field for the x position of the disc. */
        private IntField centerXField;

        /** Field for the y position of the disc. */
        private IntField centerYField;

        /** Field for the radius of the disc. */
        private IntField radiusField;



        /**
         * Creates a new dialog.
         * 
         * @param map
         *            map on which the disc will be applied
         */
        public Dialog(Map map) {
            super("Mask Disc");

            ColumnPanel cPanel = new ColumnPanel(3);

            // Create the center x field
            cPanel.add("Center X:");
            centerXField = new IntField("Center X", map.width / 2);
            centerXField.setRange(Integer.MIN_VALUE, Integer.MAX_VALUE);
            cPanel.add(centerXField);
            cPanel.add("px");

            // Create the center y field
            cPanel.add("Center Y:");
            centerYField = new IntField("Center Y", map.height / 2);
            centerYField.setRange(Integer.MIN_VALUE, Integer.MAX_VALUE);
            cPanel.add(centerYField);
            cPanel.add("px");

            // Create the radius field
            cPanel.add("Radius:");
            radiusField = new IntField("Radius", map.height / 2);
            radiusField.setRange(1, Integer.MAX_VALUE);
            cPanel.add(radiusField);
            cPanel.add("px");

            setMainComponent(cPanel);

            setPreferences(MaskDisc.this.getPreferences());
        }



        /**
         * Returns the specified x position of the disc.
         * 
         * @return x position
         */
        public int getCenterX() {
            return centerXField.getValueBFR();
        }



        /**
         * Returns the specified y position of the disc.
         * 
         * @return y position
         */
        public int getCenterY() {
            return centerYField.getValueBFR();
        }



        /**
         * Returns the specified radius of the disc.
         * 
         * @return radius
         */
        public int getRadius() {
            return radiusField.getValueBFR();
        }

    }



    /**
     * Creates the mask disc.
     * 
     * @return the resultant mask disc
     */
    private BinMap doMaskDisc() {
        if (!areMapsLoaded(true))
            return null;

        Map map = getSelectedMap();

        if (map == null) {
            ErrorDialog.show("Select a map.");
            return null;
        }

        Dialog dialog = new Dialog(map);
        if (dialog.show() != OkCancelDialog.OK)
            return null;

        BinMap maskDisc =
                new org.ebsdimage.core.MaskDisc(map.width, map.height,
                        dialog.getCenterX(), dialog.getCenterY(),
                        dialog.getRadius());

        RMLImage.add(maskDisc);

        return maskDisc;
    }



    @Override
    public void xRun() {
        doMaskDisc();
    }

}
