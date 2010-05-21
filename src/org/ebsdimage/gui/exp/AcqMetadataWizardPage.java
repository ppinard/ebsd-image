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
package org.ebsdimage.gui.exp;

import static java.lang.Math.toDegrees;
import static java.lang.Math.toRadians;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import net.miginfocom.swing.MigLayout;

import org.ebsdimage.core.Camera;
import org.ebsdimage.core.EbsdMMap;
import org.ebsdimage.core.EbsdMetadata;
import org.ebsdimage.core.exp.ExpMetadata;

import ptpshared.core.math.Eulers;
import ptpshared.core.math.Quaternion;
import ptpshared.gui.WizardPage;
import rmlshared.gui.CheckBox;
import rmlshared.gui.DoubleField;
import rmlshared.gui.FlowLayout;
import rmlshared.gui.Panel;

/**
 * Template for the acquisition metadata wizard page.
 * 
 * @author Philippe T. Pinard
 * 
 */
public class AcqMetadataWizardPage extends WizardPage {

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



    /**
     * Listener to enable/disable the working distance field.
     */
    private class WdCBoxListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            wdField.setEnabled(!wdCBox.isSelected());
        }
    }



    /**
     * Listener to enable/disable the horizontal step size field.
     */
    private class XStepCBoxListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            xStepField.setEnabled(!xStepCBox.isSelected());
        }
    }



    /**
     * Listener to enable/disable the vertical step size field.
     */
    private class YStepCBoxListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            yStepField.setEnabled(!yStepCBox.isSelected());
        }
    }



    /** Map key for the metadata. */
    public static final String KEY_METADATA = "metadata";

    /**
     * Map key to store if the data has been previously loaded. It prevents
     * loading the temporary metadata twice.
     */
    public static final String KEY_LOADED = "metadata.loaded";



    /**
     * Returns a description of this step.
     * 
     * @return description
     */
    public static String getDescription() {
        return "Acquisition Metadata";
    }



    /** Beam energy field. */
    private DoubleField energyField;

    /** Magnification field. */
    private DoubleField magField;

    /** Sample tilt field. */
    private DoubleField tiltField;

    /** Working distance field. */
    private DoubleField wdField;

    /** Horizontal step size field. */
    private DoubleField xStepField;

    /** Vertical step size field. */
    private DoubleField yStepField;

    /** Not defined energy check box. */
    private CheckBox energyCBox;

    /** Not defined magnification check box. */
    private CheckBox magCBox;

    /** Not defined tilt angle check box. */
    private CheckBox tiltCBox;

    /** Not defined working distance check box. */
    private CheckBox wdCBox;

    /** Not defined horizontal step size check box. */
    private CheckBox xStepCBox;

    /** Not defined vertical step size check box. */
    private CheckBox yStepCBox;

    /** Not defined sample's rotation check box. */
    private CheckBox rotationCBox;

    /** Sample rotation first Euler field. */
    private DoubleField theta1Field;

    /** Sample rotation second Euler field. */
    private DoubleField theta2Field;

    /** Sample rotation third Euler field. */
    private DoubleField theta3Field;

    /** Not defined calibration check box. */
    private CheckBox calibrationCBox;

    /** Pattern center horizontal coordinate field. */
    private DoubleField pchField;

    /** Pattern center vertical coordinate field. */
    private DoubleField pcvField;

    /** Detector distance field. */
    private DoubleField ddField;



    /**
     * Creates a new <code>AcqMetadataWizardPage</code>. This page is to setup
     * all the EBSD metadata.
     */
    public AcqMetadataWizardPage() {

        setLayout(new FlowLayout(FlowLayout.VERTICAL, FlowLayout.NORTHWEST));

        Panel microscopeDataPanel =
                new Panel(new MigLayout("", "[][grow,fill][]25[][]"));

        // Beam energy line
        microscopeDataPanel.add("Beam energy");
        energyField = new DoubleField("Beam energy", 15);
        energyField.setRange(5, 30);
        microscopeDataPanel.add(energyField);
        microscopeDataPanel.add("keV");
        energyCBox = new CheckBox();
        energyCBox.addActionListener(new EnergyCBoxListener());
        microscopeDataPanel.add(energyCBox);
        microscopeDataPanel.add("Not defined", "wrap");

        // Magnification line
        microscopeDataPanel.add("Magnification");
        magField = new DoubleField("Magnification", 1000);
        magField.setRange(1, 1000000);
        microscopeDataPanel.add(magField);
        microscopeDataPanel.add("X");
        magCBox = new CheckBox();
        magCBox.addActionListener(new MagCBoxListener());
        microscopeDataPanel.add(magCBox);
        microscopeDataPanel.add("Not defined", "wrap");

        // Sample tilt line
        microscopeDataPanel.add("Sample tilt");
        tiltField = new DoubleField("Sample tilt", 70);
        tiltField.setRange(0, 90);
        microscopeDataPanel.add(tiltField);
        microscopeDataPanel.add("\u00b0"); // deg
        tiltCBox = new CheckBox();
        tiltCBox.addActionListener(new TiltCBoxListener());
        microscopeDataPanel.add(tiltCBox);
        microscopeDataPanel.add("Not defined", "wrap");

        // Working distance line
        microscopeDataPanel.add("Working distance");
        wdField = new DoubleField("Working distance", 20);
        wdField.setRange(0, 45);
        microscopeDataPanel.add(wdField);
        microscopeDataPanel.add("mm");
        wdCBox = new CheckBox();
        wdCBox.addActionListener(new WdCBoxListener());
        microscopeDataPanel.add(wdCBox);
        microscopeDataPanel.add("Not defined", "wrap");

        // Horizontal step size line
        microscopeDataPanel.add("Horizontal step size");
        xStepField = new DoubleField("Horizontal step size", 1);
        xStepField.setRange(0, 1000);
        microscopeDataPanel.add(xStepField);
        microscopeDataPanel.add("\u03bcm"); // micrometer
        xStepCBox = new CheckBox();
        xStepCBox.addActionListener(new XStepCBoxListener());
        microscopeDataPanel.add(xStepCBox);
        microscopeDataPanel.add("Not defined", "wrap");

        // Vertical step size line
        microscopeDataPanel.add("Vertical step size");
        yStepField = new DoubleField("Vertical step size", 1);
        yStepField.setRange(0, 1000);
        microscopeDataPanel.add(yStepField);
        microscopeDataPanel.add("\u03bcm"); // micrometer
        yStepCBox = new CheckBox();
        yStepCBox.addActionListener(new YStepCBoxListener());
        microscopeDataPanel.add(yStepCBox);
        microscopeDataPanel.add("Not defined", "wrap");

        add(microscopeDataPanel);

        Panel dataPanel =
                new Panel(new MigLayout("",
                        "[][grow,fill][]25[][grow,fill][]25[][grow,fill][]"));

        dataPanel.add("Sample's rotation", "span 2");
        rotationCBox = new CheckBox();
        rotationCBox.addActionListener(new RotationCBoxListener());
        dataPanel.add(rotationCBox, "span, split 2");
        dataPanel.add("Not defined", "wrap");

        dataPanel.add("Theta 1");
        theta1Field = new DoubleField("Theta 1", 0);
        dataPanel.add(theta1Field);
        dataPanel.add("\u00b0"); // deg
        dataPanel.add("Theta 2");
        theta2Field = new DoubleField("Theta 2", 0);
        dataPanel.add(theta2Field);
        dataPanel.add("\u00b0"); // deg
        dataPanel.add("Theta 3");
        theta3Field = new DoubleField("Theta 3", 0);
        dataPanel.add(theta3Field);
        dataPanel.add("\u00b0", "wrap"); // deg

        dataPanel.add("Calibration", "span 2");
        calibrationCBox = new CheckBox();
        calibrationCBox.addActionListener(new CalibrationCBoxListener());
        dataPanel.add(calibrationCBox, "span, split 2");
        dataPanel.add("Not defined", "wrap");

        dataPanel.add("PCh");
        pchField = new DoubleField("PCh", 0);
        dataPanel.add(pchField);
        dataPanel.add("PCv", "skip");
        pcvField = new DoubleField("PCv", 0);
        dataPanel.add(pcvField);
        dataPanel.add("DD", "skip");
        ddField = new DoubleField("DD", 1.0);
        ddField.setRange(1e-5, Double.MAX_VALUE);
        dataPanel.add(ddField);

        add(dataPanel);
    }



    @Override
    public boolean isCorrect(boolean buffer) {
        if (!energyCBox.isSelected())
            if (!energyField.isCorrect())
                return false;

        if (!magCBox.isSelected())
            if (!magField.isCorrect())
                return false;

        if (!tiltCBox.isSelected())
            if (!tiltField.isCorrect())
                return false;

        if (!wdCBox.isSelected())
            if (!wdField.isCorrect())
                return false;

        if (!xStepCBox.isSelected())
            if (!xStepField.isCorrect())
                return false;

        if (!yStepCBox.isSelected())
            if (!yStepField.isCorrect())
                return false;

        if (!rotationCBox.isSelected()) {
            if (!theta1Field.isCorrect())
                return false;
            if (!theta2Field.isCorrect())
                return false;
            if (!theta3Field.isCorrect())
                return false;
        }

        if (!calibrationCBox.isSelected()) {
            if (!pchField.isCorrect())
                return false;
            if (!pcvField.isCorrect())
                return false;
            if (!ddField.isCorrect())
                return false;
        }

        EbsdMetadata metadata;
        try {
            metadata = getMetadata();
        } catch (Exception ex) {
            showErrorDialog(ex.getMessage());
            return false;
        }

        if (buffer)
            put(KEY_METADATA, metadata);

        return true;
    }



    /**
     * Returns the metadata constructed from this page. The method
     * {@link #isCorrect()} should be called prior to this method to make sure
     * all the data is correct.
     * 
     * @return a <code>EbsdMetadata</code>
     */
    protected EbsdMetadata getMetadata() {
        double beamEnergy;
        if (!energyCBox.isSelected())
            beamEnergy = energyField.getValue() * 1e3;
        else
            beamEnergy = EbsdMetadata.DEFAULT_BEAM_ENERGY;

        double magnification;
        if (!magCBox.isSelected())
            magnification = magField.getValue();
        else
            magnification = EbsdMetadata.DEFAULT_MAGNIFICATION;

        double tiltAngle;
        if (!tiltCBox.isSelected())
            tiltAngle = toRadians(tiltField.getValue());
        else
            tiltAngle = EbsdMetadata.DEFAULT_TILT_ANGLE;

        double workingDistance;
        if (!wdCBox.isSelected())
            workingDistance = wdField.getValue() * 1e-3;
        else
            workingDistance = EbsdMetadata.DEFAULT_WORKING_DISTANCE;

        double pixelWidth;
        if (!xStepCBox.isSelected())
            pixelWidth = xStepField.getValue() * 1e-6;
        else
            pixelWidth = EbsdMetadata.DEFAULT_PIXEL_WIDTH;

        double pixelHeight;
        if (!yStepCBox.isSelected())
            pixelHeight = yStepField.getValue() * 1e-6;
        else
            pixelHeight = EbsdMetadata.DEFAULT_PIXEL_HEIGHT;

        Quaternion sampleRotation;
        if (!rotationCBox.isSelected())
            sampleRotation =
                    new Quaternion(new Eulers(
                            toRadians(theta1Field.getValue()),
                            toRadians(theta2Field.getValue()),
                            toRadians(theta3Field.getValue())));
        else
            sampleRotation = EbsdMetadata.DEFAULT_SAMPLE_ROTATION;

        Camera calibration;
        if (!calibrationCBox.isSelected())
            calibration =
                    new Camera(pchField.getValue(), pcvField.getValue(),
                            ddField.getValue());
        else
            calibration = EbsdMetadata.DEFAULT_CALIBRATION;

        return new ExpMetadata(beamEnergy, magnification, tiltAngle,
                workingDistance, pixelWidth, pixelHeight, sampleRotation,
                calibration);
    }



    @Override
    protected void renderingPage() {
        super.renderingPage();

        EbsdMMap mmap = (EbsdMMap) get(StartWizardPage.KEY_TEMP_EBSDMMAP);

        if (mmap == null)
            return;

        if (get(KEY_LOADED) != null)
            if ((Integer) get(KEY_LOADED) > 0)
                return;

        try {
            energyField.setValue(mmap.beamEnergy / 1e3);
            energyCBox.setSelected(Double.isNaN(mmap.beamEnergy));
            energyField.setEnabled(!Double.isNaN(mmap.beamEnergy));
        } catch (IllegalArgumentException ex) {
            showErrorDialog("Beam energy value could not be loaded from EbsdMMap.");
        }

        try {
            magField.setValue(mmap.magnification);
            magCBox.setSelected(Double.isNaN(mmap.magnification));
            magField.setEnabled(!Double.isNaN(mmap.magnification));
        } catch (IllegalArgumentException ex) {
            showErrorDialog("Magnification value could not be loaded from EbsdMMap.");
        }

        try {
            tiltField.setValue(toDegrees(mmap.tiltAngle));
            tiltCBox.setSelected(Double.isNaN(mmap.tiltAngle));
            tiltField.setEnabled(!Double.isNaN(mmap.tiltAngle));
        } catch (IllegalArgumentException ex) {
            showErrorDialog("Tilt angle value could not be loaded from EbsdMMap.");
        }

        try {
            wdField.setValue(mmap.workingDistance * 1e3);
            wdCBox.setSelected(Double.isNaN(mmap.workingDistance));
            wdField.setEnabled(!Double.isNaN(mmap.workingDistance));
        } catch (IllegalArgumentException ex) {
            showErrorDialog("Working distance value could not be loaded from EbsdMMap.");
        }

        try {
            xStepField.setValue(mmap.pixelWidth * 1e6);
            xStepCBox.setSelected(Double.isNaN(mmap.pixelWidth));
            xStepField.setEnabled(!Double.isNaN(mmap.pixelWidth));
        } catch (IllegalArgumentException ex) {
            showErrorDialog("Horizontal step size value could not be loaded from EbsdMMap.");
        }

        try {
            yStepField.setValue(mmap.pixelHeight * 1e6);
            yStepCBox.setSelected(Double.isNaN(mmap.pixelHeight));
            yStepField.setEnabled(!Double.isNaN(mmap.pixelHeight));
        } catch (IllegalArgumentException ex) {
            showErrorDialog("Vertical step size value could not be loaded from EbsdMMap.");
        }

        Eulers sampleRotation = mmap.sampleRotation.toEuler();
        try {
            theta1Field.setValue(toDegrees(sampleRotation.theta1));
            theta2Field.setValue(toDegrees(sampleRotation.theta2));
            theta3Field.setValue(toDegrees(sampleRotation.theta3));
            rotationCBox.setSelected(false);
        } catch (IllegalArgumentException ex) {
            showErrorDialog("Sample rotation could not be loaded from EbsdMMap.");
        }

        Camera calibration = mmap.calibration;
        try {
            pchField.setValue(calibration.patternCenterH);
            pcvField.setValue(calibration.patternCenterV);
            ddField.setValue(calibration.detectorDistance);
            calibrationCBox.setSelected(false);
        } catch (IllegalArgumentException ex) {
            showErrorDialog("Calibration could not be loaded from EbsdMMap.");
        }

        put(KEY_LOADED, 1);
    }

}
