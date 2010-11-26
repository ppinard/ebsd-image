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

import ptpshared.core.math.Eulers;
import ptpshared.core.math.Quaternion;
import ptpshared.gui.WizardPage;
import rmlimage.core.Calibration;
import rmlshared.gui.CheckBox;
import rmlshared.gui.DoubleField;
import rmlshared.gui.FlowLayout;
import rmlshared.gui.Panel;
import rmlshared.util.Preferences;

/**
 * Template for the acquisition metadata wizard page.
 * 
 * @author Philippe T. Pinard
 */
public class AcqMetadataWizardPage extends WizardPage {

    /**
     * Listener to enable/disable the fields.
     */
    private class RefreshListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            refresh();
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
    private CheckBox sampleRotationCBox;

    /** Sample rotation first Euler field. */
    private DoubleField sampleTheta1Field;

    /** Sample rotation second Euler field. */
    private DoubleField sampleTheta2Field;

    /** Sample rotation third Euler field. */
    private DoubleField sampleTheta3Field;

    /** Not defined camera's rotation check box. */
    private CheckBox cameraRotationCBox;

    /** Sample rotation first Euler field. */
    private DoubleField cameraTheta1Field;

    /** Sample rotation second Euler field. */
    private DoubleField cameraTheta2Field;

    /** Sample rotation third Euler field. */
    private DoubleField cameraTheta3Field;

    /** Not defined camera check box. */
    private CheckBox cameraCBox;

    /** Pattern centre horizontal coordinate field. */
    private DoubleField pchField;

    /** Pattern centre vertical coordinate field. */
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
        energyField =
                new DoubleField("Beam energy", EbsdMetadata.DEFAULT.beamEnergy);
        energyField.setRange(1, 300);
        microscopeDataPanel.add(energyField);
        microscopeDataPanel.add("keV");
        energyCBox = new CheckBox();
        energyCBox.setName("Energy CBox");
        energyCBox.addActionListener(new RefreshListener());
        microscopeDataPanel.add(energyCBox);
        microscopeDataPanel.add("Not defined", "wrap");

        // Magnification line
        microscopeDataPanel.add("Magnification");
        magField =
                new DoubleField("Magnification",
                        EbsdMetadata.DEFAULT.magnification);
        magField.setRange(1, 1000000);
        microscopeDataPanel.add(magField);
        microscopeDataPanel.add("X");
        magCBox = new CheckBox();
        magCBox.setName("Magnification CBox");
        magCBox.addActionListener(new RefreshListener());
        microscopeDataPanel.add(magCBox);
        microscopeDataPanel.add("Not defined", "wrap");

        // Sample tilt line
        microscopeDataPanel.add("Sample tilt");
        tiltField =
                new DoubleField("Sample tilt",
                        Math.toDegrees(EbsdMetadata.DEFAULT.tiltAngle));
        tiltField.setRange(0, 90);
        microscopeDataPanel.add(tiltField);
        microscopeDataPanel.add("\u00b0"); // deg
        tiltCBox = new CheckBox();
        tiltCBox.setName("Tilt CBox");
        tiltCBox.addActionListener(new RefreshListener());
        microscopeDataPanel.add(tiltCBox);
        microscopeDataPanel.add("Not defined", "wrap");

        // Working distance line
        microscopeDataPanel.add("Working distance");
        wdField =
                new DoubleField("Working distance",
                        EbsdMetadata.DEFAULT.workingDistance);
        wdField.setRange(0, 45);
        microscopeDataPanel.add(wdField);
        microscopeDataPanel.add("mm");
        wdCBox = new CheckBox();
        wdCBox.setName("Working distance CBox");
        wdCBox.addActionListener(new RefreshListener());
        microscopeDataPanel.add(wdCBox);
        microscopeDataPanel.add("Not defined", "wrap");

        // Horizontal step size line
        microscopeDataPanel.add("Horizontal step size");
        xStepField = new DoubleField("Horizontal step size", 1);
        xStepField.setRange(0, 1000);
        microscopeDataPanel.add(xStepField);
        microscopeDataPanel.add("\u03bcm"); // micrometer
        xStepCBox = new CheckBox();
        xStepCBox.setName("Horizontal step size CBox");
        xStepCBox.addActionListener(new RefreshListener());
        microscopeDataPanel.add(xStepCBox);
        microscopeDataPanel.add("Not defined", "wrap");

        // Vertical step size line
        microscopeDataPanel.add("Vertical step size");
        yStepField = new DoubleField("Vertical step size", 1);
        yStepField.setRange(0, 1000);
        microscopeDataPanel.add(yStepField);
        microscopeDataPanel.add("\u03bcm"); // micrometer
        yStepCBox = new CheckBox();
        yStepCBox.setName("Vertical step size CBox");
        yStepCBox.addActionListener(new RefreshListener());
        microscopeDataPanel.add(yStepCBox);
        microscopeDataPanel.add("Not defined", "wrap");

        add(microscopeDataPanel);

        Panel dataPanel =
                new Panel(new MigLayout("",
                        "[][grow,fill][]25[][grow,fill][]25[][grow,fill][]"));

        // Sample rotation
        dataPanel.add("Sample's rotation", "span 2");
        sampleRotationCBox = new CheckBox();
        sampleRotationCBox.setName("Sample rotation CBox");
        sampleRotationCBox.addActionListener(new RefreshListener());
        dataPanel.add(sampleRotationCBox, "span, split 2");
        dataPanel.add("Not defined", "wrap");

        Eulers defaultThetas = EbsdMetadata.DEFAULT.sampleRotation.toEuler();

        dataPanel.add("Theta 1");
        sampleTheta1Field =
                new DoubleField("Sample Theta 1",
                        Math.toDegrees(defaultThetas.theta1));
        dataPanel.add(sampleTheta1Field);
        dataPanel.add("\u00b0"); // deg
        dataPanel.add("Theta 2");
        sampleTheta2Field =
                new DoubleField("Sample Theta 2",
                        Math.toDegrees(defaultThetas.theta2));
        dataPanel.add(sampleTheta2Field);
        dataPanel.add("\u00b0"); // deg
        dataPanel.add("Theta 3");
        sampleTheta3Field =
                new DoubleField("Sample Theta 3",
                        Math.toDegrees(defaultThetas.theta3));
        dataPanel.add(sampleTheta3Field);
        dataPanel.add("\u00b0", "wrap"); // deg

        // Camera rotation
        dataPanel.add("Camera's rotation", "span 2");
        cameraRotationCBox = new CheckBox();
        cameraRotationCBox.setName("Camera rotation CBox");
        cameraRotationCBox.addActionListener(new RefreshListener());
        dataPanel.add(cameraRotationCBox, "span, split 2");
        dataPanel.add("Not defined", "wrap");

        defaultThetas = EbsdMetadata.DEFAULT.cameraRotation.toEuler();

        dataPanel.add("Theta 1");
        cameraTheta1Field =
                new DoubleField("Camera Theta 1",
                        Math.toDegrees(defaultThetas.theta1));
        dataPanel.add(cameraTheta1Field);
        dataPanel.add("\u00b0"); // deg
        dataPanel.add("Theta 2");
        cameraTheta2Field =
                new DoubleField("Camera Theta 2",
                        Math.toDegrees(defaultThetas.theta2));
        dataPanel.add(cameraTheta2Field);
        dataPanel.add("\u00b0"); // deg
        dataPanel.add("Theta 3");
        cameraTheta3Field =
                new DoubleField("Camera Theta 3",
                        Math.toDegrees(defaultThetas.theta3));
        dataPanel.add(cameraTheta3Field);
        dataPanel.add("\u00b0", "wrap"); // deg

        dataPanel.add("Camera's calibration", "span 2");
        cameraCBox = new CheckBox();
        cameraCBox.setName("Camera calibration CBox");
        cameraCBox.addActionListener(new RefreshListener());
        dataPanel.add(cameraCBox, "span, split 2");
        dataPanel.add("Not defined", "wrap");

        dataPanel.add("PCh");
        pchField =
                new DoubleField("PCh",
                        EbsdMetadata.DEFAULT.camera.patternCenterH);
        dataPanel.add(pchField);
        dataPanel.add("PCv", "skip");
        pcvField =
                new DoubleField("PCv",
                        EbsdMetadata.DEFAULT.camera.patternCenterV);
        dataPanel.add(pcvField);
        dataPanel.add("DD", "skip");
        ddField =
                new DoubleField("DD",
                        EbsdMetadata.DEFAULT.camera.detectorDistance);
        ddField.setRange(1e-5, Double.MAX_VALUE);
        dataPanel.add(ddField);

        add(dataPanel);

        // Refresh
        energyCBox.doClick();
        magCBox.doClick();
        tiltCBox.doClick();
        wdCBox.doClick();
        xStepCBox.doClick();
        yStepCBox.doClick();
        sampleRotationCBox.doClick();
        cameraRotationCBox.doClick();
        cameraCBox.doClick();
        refresh();
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
            beamEnergy = EbsdMetadata.DEFAULT.beamEnergy;

        double magnification;
        if (!magCBox.isSelected())
            magnification = magField.getValue();
        else
            magnification = EbsdMetadata.DEFAULT.magnification;

        double tiltAngle;
        if (!tiltCBox.isSelected())
            tiltAngle = toRadians(tiltField.getValue());
        else
            tiltAngle = EbsdMetadata.DEFAULT.tiltAngle;

        double workingDistance;
        if (!wdCBox.isSelected())
            workingDistance = wdField.getValue() * 1e-3;
        else
            workingDistance = EbsdMetadata.DEFAULT.workingDistance;

        Quaternion sampleRotation;
        if (!sampleRotationCBox.isSelected())
            sampleRotation =
                    new Quaternion(new Eulers(
                            toRadians(sampleTheta1Field.getValue()),
                            toRadians(sampleTheta2Field.getValue()),
                            toRadians(sampleTheta3Field.getValue())));
        else
            sampleRotation = EbsdMetadata.DEFAULT.sampleRotation;

        Quaternion cameraRotation;
        if (!cameraRotationCBox.isSelected())
            cameraRotation =
                    new Quaternion(new Eulers(
                            toRadians(cameraTheta1Field.getValue()),
                            toRadians(cameraTheta2Field.getValue()),
                            toRadians(cameraTheta3Field.getValue())));
        else
            cameraRotation = EbsdMetadata.DEFAULT.cameraRotation;

        Camera camera;
        if (!cameraCBox.isSelected())
            camera =
                    new Camera(pchField.getValue(), pcvField.getValue(),
                            ddField.getValue());
        else
            camera = EbsdMetadata.DEFAULT.camera;

        return new EbsdMetadata(beamEnergy, magnification, tiltAngle,
                workingDistance, camera, sampleRotation, cameraRotation);
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

        if (!sampleRotationCBox.isSelected()) {
            if (!sampleTheta1Field.isCorrect())
                return false;
            if (!sampleTheta2Field.isCorrect())
                return false;
            if (!sampleTheta3Field.isCorrect())
                return false;
        }

        if (!sampleRotationCBox.isSelected()) {
            if (!sampleTheta1Field.isCorrect())
                return false;
            if (!sampleTheta2Field.isCorrect())
                return false;
            if (!sampleTheta3Field.isCorrect())
                return false;
        }

        if (!cameraCBox.isSelected()) {
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
     * Refresh combo boxes and fields.
     */
    private void refresh() {
        // Camera
        boolean enabled = !cameraCBox.isSelected();
        pchField.setEnabled(enabled);
        pcvField.setEnabled(enabled);
        ddField.setEnabled(enabled);

        // Energy
        energyField.setEnabled(!energyCBox.isSelected());

        // Mag
        magField.setEnabled(!magCBox.isSelected());

        // Sample rotation
        enabled = !sampleRotationCBox.isSelected();
        sampleTheta1Field.setEnabled(enabled);
        sampleTheta2Field.setEnabled(enabled);
        sampleTheta3Field.setEnabled(enabled);

        // Camera rotation
        enabled = !cameraRotationCBox.isSelected();
        cameraTheta1Field.setEnabled(enabled);
        cameraTheta2Field.setEnabled(enabled);
        cameraTheta3Field.setEnabled(enabled);

        // Tilt
        tiltField.setEnabled(!tiltCBox.isSelected());

        // Working distance
        wdField.setEnabled(!wdCBox.isSelected());

        // Horizontal step size
        xStepField.setEnabled(!xStepCBox.isSelected());

        // Vertical step size
        yStepField.setEnabled(!yStepCBox.isSelected());
    }



    @Override
    protected void renderingPage() {
        super.renderingPage();

        EbsdMMap mmap = (EbsdMMap) get(StartWizardPage.KEY_TEMP_EBSDMMAP);

        if (mmap == null)
            return;

        EbsdMetadata metadata = mmap.getMetadata();
        Calibration calibration = mmap.getCalibration();

        if (get(KEY_LOADED) != null)
            if ((Integer) get(KEY_LOADED) > 0)
                return;

        try {
            energyField.setValue(metadata.beamEnergy / 1e3);
            energyCBox.setSelected(Double.isNaN(metadata.beamEnergy));
            energyField.setEnabled(!Double.isNaN(metadata.beamEnergy));
        } catch (IllegalArgumentException ex) {
            showErrorDialog("Beam energy value could not be loaded from EbsdMMap.");
        }

        try {
            magField.setValue(metadata.magnification);
            magCBox.setSelected(Double.isNaN(metadata.magnification));
            magField.setEnabled(!Double.isNaN(metadata.magnification));
        } catch (IllegalArgumentException ex) {
            showErrorDialog("Magnification value could not be loaded from EbsdMMap.");
        }

        try {
            tiltField.setValue(toDegrees(metadata.tiltAngle));
            tiltCBox.setSelected(Double.isNaN(metadata.tiltAngle));
            tiltField.setEnabled(!Double.isNaN(metadata.tiltAngle));
        } catch (IllegalArgumentException ex) {
            showErrorDialog("Tilt angle value could not be loaded from EbsdMMap.");
        }

        try {
            wdField.setValue(metadata.workingDistance * 1e3);
            wdCBox.setSelected(Double.isNaN(metadata.workingDistance));
            wdField.setEnabled(!Double.isNaN(metadata.workingDistance));
        } catch (IllegalArgumentException ex) {
            showErrorDialog("Working distance value could not be loaded from EbsdMMap.");
        }

        try {
            double pixelWidth = calibration.getX0().getValue("um");
            xStepField.setValue(pixelWidth);
            xStepCBox.setSelected(Double.isNaN(pixelWidth));
            xStepField.setEnabled(!Double.isNaN(pixelWidth));
        } catch (IllegalArgumentException ex) {
            showErrorDialog("Horizontal step size value could not be loaded from EbsdMMap.");
        }

        try {
            double pixelHeight = calibration.getY0().getValue("um");
            yStepField.setValue(pixelHeight);
            yStepCBox.setSelected(Double.isNaN(pixelHeight));
            yStepField.setEnabled(!Double.isNaN(pixelHeight));
        } catch (IllegalArgumentException ex) {
            showErrorDialog("Vertical step size value could not be loaded from EbsdMMap.");
        }

        Eulers sampleRotation = metadata.sampleRotation.toEuler();
        try {
            sampleTheta1Field.setValue(toDegrees(sampleRotation.theta1));
            sampleTheta2Field.setValue(toDegrees(sampleRotation.theta2));
            sampleTheta3Field.setValue(toDegrees(sampleRotation.theta3));
            sampleRotationCBox.setSelected(false);
        } catch (IllegalArgumentException ex) {
            showErrorDialog("Sample rotation could not be loaded from EbsdMMap.");
        }

        Eulers cameraRotation = metadata.cameraRotation.toEuler();
        try {
            cameraTheta1Field.setValue(toDegrees(cameraRotation.theta1));
            cameraTheta2Field.setValue(toDegrees(cameraRotation.theta2));
            cameraTheta3Field.setValue(toDegrees(cameraRotation.theta3));
            cameraRotationCBox.setSelected(false);
        } catch (IllegalArgumentException ex) {
            showErrorDialog("Camera rotation could not be loaded from EbsdMMap.");
        }

        Camera camera = metadata.camera;
        try {
            pchField.setValue(camera.patternCenterH);
            pcvField.setValue(camera.patternCenterV);
            ddField.setValue(camera.detectorDistance);
            cameraCBox.setSelected(false);
        } catch (IllegalArgumentException ex) {
            showErrorDialog("Camera calibration could not be loaded from EbsdMMap.");
        }

        put(KEY_LOADED, 1);
    }



    @Override
    public void setPreferences(Preferences pref) {
        super.setPreferences(pref);

        energyCBox.setSelected(energyField.getValue().isNaN());
        magCBox.setSelected(magField.getValue().isNaN());
        tiltCBox.setSelected(tiltField.getValue().isNaN());
        wdCBox.setSelected(wdField.getValue().isNaN());
        xStepCBox.setSelected(xStepField.getValue().isNaN());
        yStepCBox.setSelected(yStepField.getValue().isNaN());

        refresh();
    }

}
