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

import org.ebsdimage.core.EbsdMMap;
import org.ebsdimage.core.EbsdMMapUtil;

import rmlimage.core.Map;
import rmlimage.gui.BasicDialog;
import rmlimage.gui.PlugIn;
import rmlimage.module.multi.core.BasicMultiMap;
import rmlshared.gui.*;

/**
 * Plug-in to paste a micron bar on all the maps inside an <code>EbsdMMap</code>
 * .
 * 
 * @author Philippe T. Pinard
 */
public class MicronBar extends PlugIn {

    /**
     * Dialog to select an EBSD multimap from those loaded in the GUI.
     * 
     * @author Philippe T. Pinard
     */
    private class Dialog extends BasicDialog {

        /** Combo box to select the EBSD multimap. */
        private ComboBox<Map> mmapCBox;

        /** Field for the scale factor. */
        private IntField scaleFactorField;



        /**
         * Creates a new dialog to select an EBSD multimap.
         */
        public Dialog() {
            super("EBSD Multimap Selector");

            Panel panel = new ColumnPanel(2);

            panel.add("EBSD multimap");

            Map[] maps = getMapList(EbsdMMap.class);
            mmapCBox = new ComboBox<Map>(maps);
            panel.add(mmapCBox);

            panel.add("Scaling factor");

            scaleFactorField = new IntField("Scaling factor", 1);
            scaleFactorField.setRange(1, Integer.MAX_VALUE);
            panel.add(scaleFactorField);

            setMainComponent(panel);
        }



        /**
         * Returns the scaling factor.
         * 
         * @return scaling factor
         */
        public int getScaleFactor() {
            return scaleFactorField.getValue();
        }



        /**
         * Returns the selected multimap.
         * 
         * @return multimap
         */
        public EbsdMMap getSelectedMap() {
            return (EbsdMMap) mmapCBox.getSelectedItem();
        }

    }



    /**
     * Selects an <code>EbsdMMap</code> and performs the addition of a micron
     * bar on all maps.
     * 
     * @return a <code>BasicMultiMap</code>
     */
    private BasicMultiMap doMicroBar() {
        if (!areMapsLoaded(true))
            return null;

        Dialog dialog = new Dialog();
        if (dialog.show() != OkCancelDialog.OK)
            return null;

        EbsdMMap mmap = dialog.getSelectedMap();
        int scaleFactor = dialog.getScaleFactor();

        BasicMultiMap dest = EbsdMMapUtil.pasteMicronBar(mmap, scaleFactor);

        add(dest);

        return dest;
    }



    @Override
    protected void xRun() throws Exception {
        doMicroBar();
    }
}
