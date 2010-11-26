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
package org.ebsdimage.vendors.hkl.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import net.miginfocom.swing.MigLayout;

import org.ebsdimage.core.Camera;
import org.ebsdimage.core.EbsdMetadata;

import ptpshared.gui.WizardPage;
import rmlshared.gui.CheckBox;
import rmlshared.gui.DoubleField;
import rmlshared.gui.FlowLayout;
import rmlshared.gui.Panel;

/**
 * Wizard page for the missing data.
 * 
 * @author Philippe T. Pinard
 */
public class MissingDataWizardPage extends WizardPage {
    /**
     * Listener to enable/disable the calibration fields.
     */
    private class CalibrationCBoxListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            boolean enabled = !calibrationCBox.isSelected();
            pchField.setEnabled(enabled);
            pcvField.setEnabled(enabled);
            ddField.setEnabled(enabled);
        }
    }

    /**
     * Listener to enable/disable the working distance field.
     */
    private class WdCBoxListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            wdField.setEnabled(!wdCBox.isSelected());
        }
    }

    /** Map key for the calibration. */
    public static final String KEY_CALIBRATION = "calibration";

    /** Map key for the working distance. */
    public static final String KEY_WORKING_DISTANCE = "workingDistance";



    /**
     * Returns a description of this page.
     * 
     * @return description
     */
    public static String getDescription() {
        return "Missing Data";
    }

    /** Field for the missing working distance. */
    private DoubleField wdField;

    /** Not defined working distance check box. */
    private CheckBox wdCBox;

    /** Not defined calibration check box. */
    private CheckBox calibrationCBox;

    /** Pattern center horizontal coordinate field. */
    private DoubleField pchField;

    /** Pattern center vertical coordinate field. */
    private DoubleField pcvField;

    /** Detector distance field. */
    private DoubleField ddField;



    /**
     * Creates a new <code>MissingDataWizardPage</code>.
     */
    public MissingDataWizardPage() {

        setLayout(new FlowLayout(FlowLayout.VERTICAL, FlowLayout.NORTHWEST));

        /* Microscope data panel */
        Panel microscopeDataPanel =
                new Panel(new MigLayout("", "[][grow,fill][]25[][]"));

        // Working distance line
        microscopeDataPanel.add("Working distance:");
        wdField = new DoubleField("WORKINGDISTANCE", 15);
        wdField.setRange(0, 45);
        microscopeDataPanel.add(wdField);
        microscopeDataPanel.add("mm");
        wdCBox = new CheckBox();
        wdCBox.addActionListener(new WdCBoxListener());
        microscopeDataPanel.add(wdCBox);
        microscopeDataPanel.add("Not defined", "wrap");

        wdField.setEnabled(false);
        wdCBox.setSelected(true);

        add(microscopeDataPanel, "wrap");

        /* Data panel */
        Panel dataPanel =
                new Panel(new MigLayout("",
                        "[][grow,fill][]25[][grow,fill][]25[][grow,fill][]"));

        dataPanel.add("Calibration", "span 2");
        calibrationCBox = new CheckBox();
        calibrationCBox.addActionListener(new CalibrationCBoxListener());
        dataPanel.add(calibrationCBox, "span, split 2");
        dataPanel.add("Not defined", "wrap");

        Camera defaultCal = EbsdMetadata.DEFAULT_CAMERA;
        dataPanel.add("PCh:");
        pchField = new DoubleField("PCH", defaultCal.patternCenterH);
        dataPanel.add(pchField);
        dataPanel.add("PCv:", "skip");
        pcvField = new DoubleField("PCV", defaultCal.patternCenterV);
        dataPanel.add(pcvField);
        dataPanel.add("DD:", "skip");
        ddField = new DoubleField("DD", defaultCal.detectorDistance);
        dataPanel.add(ddField);

        calibrationCBox.setSelected(true);
        pchField.setEnabled(false);
        pcvField.setEnabled(false);
        ddField.setEnabled(false);

        add(dataPanel, "wrap");
    }



    @Override
    public boolean isCorrect(boolean buffer) {
        double workingDistance = EbsdMetadata.DEFAULT_WORKING_DISTANCE;
        Camera calibration = EbsdMetadata.DEFAULT_CAMERA;

        if (!wdCBox.isSelected()) {
            if (!wdField.isCorrect())
                return false;

            workingDistance = wdField.getValue() * 1e-3; // to mm
        }

        if (!calibrationCBox.isSelected()) {
            if (!pchField.isCorrect())
                return false;
            if (!pcvField.isCorrect())
                return false;
            if (!ddField.isCorrect())
                return false;

            try {
                calibration =
                        new Camera(pchField.getValue(), pcvField.getValue(),
                                ddField.getValue());
            } catch (Exception e) {
                showErrorDialog(e.getMessage());
                return false;
            }
        }

        if (buffer) {
            put(KEY_CALIBRATION, calibration);
            put(KEY_WORKING_DISTANCE, workingDistance);
        }

        return true;
    }

}
