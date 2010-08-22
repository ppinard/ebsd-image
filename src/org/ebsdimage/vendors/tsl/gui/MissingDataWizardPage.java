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
package org.ebsdimage.vendors.tsl.gui;

import static java.lang.Math.toRadians;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import net.miginfocom.swing.MigLayout;

import org.ebsdimage.core.EbsdMetadata;

import ptpshared.core.math.Eulers;
import ptpshared.core.math.Quaternion;
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
     * Listener to enable/disable the energy field.
     */
    private class EnergyCBoxListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            energyField.setEnabled(!energyCBox.isSelected());
        }
    }

    /**
     * Listener to enable/disable the magnification field.
     */
    private class MagCBoxListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            magField.setEnabled(!magCBox.isSelected());
        }
    }

    /**
     * Listener to enable/disable the sample rotation fields.
     */
    private class RotationCBoxListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            boolean enabled = !rotationCBox.isSelected();
            theta1Field.setEnabled(enabled);
            theta2Field.setEnabled(enabled);
            theta3Field.setEnabled(enabled);
        }
    }

    /**
     * Listener to enable/disable the tilt angle field.
     */
    private class TiltCBoxListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            tiltField.setEnabled(!tiltCBox.isSelected());
        }
    }

    /** Map key for the beam energy. */
    public static final String KEY_BEAM_ENERGY = "beamEnergy";

    /** Map key for the magnification. */
    public static final String KEY_MAGNIFICATION = "magnification";

    /** Map key for the tilt. */
    public static final String KEY_TILT_ANGLE = "tiltAngle";

    /** Map key for the sample's rotation. */
    public static final String KEY_SAMPLE_ROTATION = "sampleRotation";



    /**
     * Returns a description of this page.
     * 
     * @return description
     */
    public static String getDescription() {
        return "Missing Data";
    }

    /** Beam energy field. */
    private DoubleField energyField;

    /** Not defined energy check box. */
    private CheckBox energyCBox;

    /** Magnification field. */
    private DoubleField magField;

    /** Not defined magnification check box. */
    private CheckBox magCBox;

    /** Sample tilt field. */
    private DoubleField tiltField;

    /** Not defined tilt angle check box. */
    private CheckBox tiltCBox;

    /** Not defined sample's rotation check box. */
    private CheckBox rotationCBox;

    /** Sample rotation first Euler field. */
    private DoubleField theta1Field;

    /** Sample rotation second Euler field. */
    private DoubleField theta2Field;

    /** Sample rotation third Euler field. */
    private DoubleField theta3Field;



    /**
     * Creates a new <code>MissingDataWizardPage</code>.
     */
    public MissingDataWizardPage() {

        setLayout(new FlowLayout(FlowLayout.VERTICAL, FlowLayout.NORTHWEST));

        /* Microscope data panel */
        Panel microscopeDataPanel =
                new Panel(new MigLayout("", "[][grow,fill][]25[][]"));

        // Working distance line
        // Beam energy line
        microscopeDataPanel.add("Beam energy:");
        energyField = new DoubleField("Beam energy", 15);
        energyField.setRange(5, 30);
        microscopeDataPanel.add(energyField);
        microscopeDataPanel.add("keV");
        energyCBox = new CheckBox();
        energyCBox.addActionListener(new EnergyCBoxListener());
        microscopeDataPanel.add(energyCBox);
        microscopeDataPanel.add("Not defined", "wrap");

        // Magnification line
        microscopeDataPanel.add("Magnification:");
        magField = new DoubleField("Magnification", 1000);
        magField.setRange(1, 1000000);
        microscopeDataPanel.add(magField);
        microscopeDataPanel.add("X");
        magCBox = new CheckBox();
        magCBox.addActionListener(new MagCBoxListener());
        microscopeDataPanel.add(magCBox);
        microscopeDataPanel.add("Not defined", "wrap");

        // Sample tilt line
        microscopeDataPanel.add("Sample tilt:");
        tiltField = new DoubleField("Sample tilt", 70);
        tiltField.setRange(0, 90);
        microscopeDataPanel.add(tiltField);
        microscopeDataPanel.add("deg");
        tiltCBox = new CheckBox();
        tiltCBox.addActionListener(new TiltCBoxListener());
        microscopeDataPanel.add(tiltCBox);
        microscopeDataPanel.add("Not defined", "wrap");

        add(microscopeDataPanel, "wrap");

        /* Data panel */
        Panel dataPanel =
                new Panel(new MigLayout("",
                        "[][grow,fill][]25[][grow,fill][]25[][grow,fill][]"));

        dataPanel.add("Sample's rotation", "span 2");
        rotationCBox = new CheckBox();
        rotationCBox.addActionListener(new RotationCBoxListener());
        dataPanel.add(rotationCBox, "span, split 2");
        dataPanel.add("Not defined", "wrap");

        dataPanel.add("Theta 1:");
        theta1Field = new DoubleField("Theta 1", 0);
        dataPanel.add(theta1Field);
        dataPanel.add("deg");
        dataPanel.add("Theta 2:");
        theta2Field = new DoubleField("Theta 2", 0);
        dataPanel.add(theta2Field);
        dataPanel.add("deg");
        dataPanel.add("Theta 3:");
        theta3Field = new DoubleField("Theta 3", 0);
        dataPanel.add(theta3Field);
        dataPanel.add("deg", "wrap");

        add(dataPanel, "wrap");

        // Initialize state
        energyCBox.doClick();
        magCBox.doClick();
        tiltCBox.doClick();
        rotationCBox.doClick();
    }



    @Override
    public boolean isCorrect(boolean buffer) {
        double energy = EbsdMetadata.DEFAULT_BEAM_ENERGY;
        double magnification = EbsdMetadata.DEFAULT_MAGNIFICATION;
        double tiltAngle = EbsdMetadata.DEFAULT_TILT_ANGLE;
        Quaternion rotation = EbsdMetadata.DEFAULT_SAMPLE_ROTATION;

        if (!energyCBox.isSelected()) {
            if (!energyField.isCorrect())
                return false;

            energy = energyField.getValue() * 1e3; // to eV
        }

        if (!magCBox.isSelected()) {
            if (!magField.isCorrect())
                return false;

            magnification = magField.getValue();
        }

        if (!tiltCBox.isSelected()) {
            if (!tiltField.isCorrect())
                return false;

            tiltAngle = toRadians(tiltField.getValue()); // to radians
        }

        if (!rotationCBox.isSelected()) {
            if (!theta1Field.isCorrect())
                return false;
            if (!theta2Field.isCorrect())
                return false;
            if (!theta3Field.isCorrect())
                return false;

            try {
                rotation =
                        new Quaternion(new Eulers(
                                toRadians(theta1Field.getValue()),
                                toRadians(theta2Field.getValue()),
                                toRadians(theta3Field.getValue())));
            } catch (Exception e) {
                showErrorDialog(e.getMessage());
                return false;
            }
        }

        if (buffer) {
            put(KEY_BEAM_ENERGY, energy);
            put(KEY_MAGNIFICATION, magnification);
            put(KEY_TILT_ANGLE, tiltAngle);
            put(KEY_SAMPLE_ROTATION, rotation);
        }

        return true;
    }

}
