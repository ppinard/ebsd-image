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

import org.ebsdimage.core.Conversion;
import org.ebsdimage.core.EbsdMMap;

import rmlimage.core.Map;
import rmlimage.core.RGBMap;
import rmlimage.gui.BasicDialog;
import rmlimage.gui.PlugIn;
import rmlshared.gui.ColumnPanel;
import rmlshared.gui.ComboBox;
import rmlshared.gui.OkCancelDialog;
import rmlshared.gui.Panel;
import edu.umd.cs.findbugs.annotations.CheckForNull;

/**
 * Plug-in to convert the Euler maps into a RGB Eulers map.
 * 
 * @author Philippe T. Pinard
 */
public class RGBEulers extends PlugIn {

    /**
     * Dialog to select an EBSD multimap from those loaded in the GUI.
     * 
     * @author Philippe T. Pinard
     */
    private class Dialog extends BasicDialog {

        /** Combo box to select the EBSD multimap. */
        private ComboBox<Map> mmapCBox;



        /**
         * Creates a new dialog to select an EBSD multimap.
         */
        public Dialog() {
            super("EBSD Multimap Selector");

            Panel panel = new ColumnPanel(2);

            panel.add("Select EBSD multimap:");

            Map[] maps = getMapList(EbsdMMap.class);
            mmapCBox = new ComboBox<Map>(maps);
            panel.add(mmapCBox);

            setMainComponent(panel);
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
     * Performs the conversion from an <code>EbsdMMap</code> to a
     * <code>RGBMap</code> and adds the later in the multimap.
     * 
     * @return the resultant <code>RGBMap</code>
     */
    @CheckForNull
    private RGBMap doConversion() {
        if (!areMapsLoaded(true))
            return null;

        Dialog dialog = new Dialog();
        if (dialog.show() != OkCancelDialog.OK)
            return null;

        EbsdMMap mmap = dialog.getSelectedMap();

        // Convert
        RGBMap rgbMap = Conversion.toRGBMap(mmap);

        // Add to EbsdMMap
        mmap.put("RGBEulers", rgbMap);
        mmap.notifyListeners();

        // Add to Desktop
        add(rgbMap);

        return rgbMap;
    }



    @Override
    protected void xRun() throws Exception {
        doConversion();
    }
}
