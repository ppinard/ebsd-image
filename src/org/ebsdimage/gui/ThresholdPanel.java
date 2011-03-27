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
package org.ebsdimage.gui;

import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

import org.ebsdimage.core.IndexedByteMap;
import org.ebsdimage.core.Threshold;

import rmlimage.RMLImage;
import rmlimage.core.BinMap;
import rmlimage.gui.MapWindow;
import rmlimage.gui.MultiDesktop;
import rmlshared.gui.ComboBox;

/**
 * Panel to appear below an <code>IndexedByteMap</code> to select which item to
 * be thresholded.
 * 
 * @param <Item>
 *            type of item in the <code>IndexedByteMap</code>
 * @author Philippe T. Pinard
 */
public class ThresholdPanel<Item> extends JPanel {

    /**
     * Listener of okay and cancel buttons.
     * 
     * @author Philippe T. Pinard
     */
    private class ActionListener implements java.awt.event.ActionListener {
        @Override
        public void actionPerformed(java.awt.event.ActionEvent event) {
            if (event.getSource() == okButton) {
                Item item = itemsCBox.getSelectedItem();

                BinMap binMap = Threshold.item(map, item);

                RMLImage.add(binMap);
            }

            // Remove the threshold panel from the image panel
            MapWindow window =
                    ((MultiDesktop) rmlimage.plugin.PlugIn.getDesktop()).getWindow(map);
            window.removeComponent(getPanel());

            // Needed to resume the threshold macro
            // that is waiting for either the OK or CANCEL button to be pressed
            synchronized (getPanel()) {
                getPanel().notifyAll();
            }
        }
    }

    /** Combo box to select a item. */
    private ComboBox<Item> itemsCBox;

    /** Selected map. */
    private IndexedByteMap<Item> map;

    /** Okay button. */
    private JButton okButton;

    /** Cancel button. */
    private JButton cancelButton;



    /**
     * Creates a new <code>ThresholdPanel</code> to appear below an
     * <code>IndexedByteMap</code> to select which item to threshold.
     * 
     * @param map
     *            selected map on which the thresholding will apply
     * @param label
     *            label for the combo box to identify the type of item
     */
    public ThresholdPanel(IndexedByteMap<Item> map, String label) {
        this.map = map;

        // Layout
        setLayout(new MigLayout());

        add(new JLabel(label));
        itemsCBox = new ComboBox<Item>();

        java.util.Map<Integer, Item> items = map.getItems();
        ArrayList<Integer> keys = new ArrayList<Integer>(items.keySet());
        Collections.sort(keys);
        for (int key : keys)
            itemsCBox.addGeneric(items.get(key));

        itemsCBox.setSelectedItem(items.get(keys.get(0)));
        add(itemsCBox);

        okButton = new JButton("OK");
        okButton.addActionListener(new ActionListener());
        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener());
        add(okButton, "gapleft 50");
        add(cancelButton);
    }



    /**
     * Returns this panel.
     * 
     * @return this panel
     */
    private ThresholdPanel<Item> getPanel() {
        return this;
    }

}
