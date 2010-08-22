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
package org.ebsdimage.gui;

import java.util.Arrays;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

import org.ebsdimage.core.PhasesMap;
import org.ebsdimage.core.Threshold;

import rmlimage.RMLImage;
import rmlimage.core.BinMap;
import rmlimage.gui.MapWindow;
import rmlimage.gui.MultiDesktop;
import rmlshared.gui.ComboBox;
import crystallography.core.Crystal;

/**
 * Panel to appear below a <code>PhasesMap</code> to select which phase to be
 * thresholded.
 * 
 * @author Philippe T. Pinard
 */
public class ThresholdPanel extends JPanel {

    /** Combo box to select a phase. */
    private ComboBox<String> phaseCBox;

    /** HashMap to access the phases information by their name. */
    private HashMap<String, Integer> phases;

    /** Selected map. */
    private PhasesMap phasesMap;

    /** Okay button. */
    private JButton okButton;

    /** Cancel button. */
    private JButton cancelButton;



    /**
     * Creates a new <code>ThresholdPanel</code> to appear below a
     * <code>PhasesMap</code> to select which phase to threshold.
     * 
     * @param phasesMap
     *            selected phases map on which the thresholding will apply
     */
    public ThresholdPanel(PhasesMap phasesMap) {
        this.phasesMap = phasesMap;

        // Fill HashMap with phases
        Crystal[] phases = phasesMap.getPhases();

        if (phases.length == 0)
            throw new IllegalArgumentException(
                    "At least one phase must be defined.");

        this.phases = new HashMap<String, Integer>();
        this.phases.put("0 - Non-Indexed", 0);
        for (int i = 0; i < phases.length; i++) {
            this.phases.put((i + 1) + " - " + phases[i].name, (i + 1));
        }

        // Layout
        setLayout(new MigLayout());

        String[] phasesName = this.phases.keySet().toArray(new String[0]);
        Arrays.sort(phasesName);
        add(new JLabel("Phase:"));
        phaseCBox = new ComboBox<String>(phasesName);
        phaseCBox.setSelectedItem(phasesName[0]);
        add(phaseCBox);

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
    private ThresholdPanel getPanel() {
        return this;
    }

    /**
     * Listener of okay and cancel buttons.
     * 
     * @author Philippe T. Pinard
     */
    private class ActionListener implements java.awt.event.ActionListener {
        @Override
        public void actionPerformed(java.awt.event.ActionEvent event) {
            if (event.getSource() == okButton) {
                int phaseId = phases.get(phaseCBox.getSelectedItem());

                BinMap binMap = Threshold.phase(phasesMap, phaseId);

                RMLImage.add(binMap);
            }

            // Remove the threshold panel from the image panel
            MapWindow window =
                    ((MultiDesktop) rmlimage.plugin.PlugIn.getDesktop()).getWindow(phasesMap);
            window.removeComponent(getPanel());

            // Needed to resume the threshold macro
            // that is waiting for either the OK or CANCEL button to be pressed
            synchronized (getPanel()) {
                getPanel().notifyAll();
            }
        }
    }

}
