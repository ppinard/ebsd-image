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

import javax.swing.BorderFactory;
import javax.swing.JLabel;

import net.miginfocom.swing.MigLayout;
import net.sf.magnitude.core.Magnitude;

import org.apache.commons.math.geometry.Rotation;
import org.ebsdimage.core.AcquisitionConfig;
import org.ebsdimage.core.Microscope;
import org.ebsdimage.io.MicroscopeUtil;

import ptpshared.gui.CalibratedDoubleField;
import ptpshared.gui.RotationField;
import ptpshared.gui.WizardPage;
import rmlshared.gui.ComboBox;
import rmlshared.gui.DoubleField;
import rmlshared.gui.Panel;

/**
 * Template for the microscope wizard page.
 * 
 * @author Philippe T. Pinard
 */
public class AcquisitionConfigWizardPage extends WizardPage {

    /** Map key for the acquisition configuration. */
    public static final String KEY_ACQUISITION_CONFIG = "acqConfig";

    /**
     * Map key to store if the data has been previously loaded. It prevents
     * loading the temporary metadata twice.
     */
    public static final String KEY_LOADED = "acqConfig.loaded";

    /** Combo box to select the microscope configuration. */
    protected ComboBox<Microscope> microscopesCBox;

    /** Beam energy field. */
    protected CalibratedDoubleField energyField;

    /** Magnification field. */
    protected DoubleField magField;

    /** Working distance field. */
    protected CalibratedDoubleField wdField;

    /** Sample tilt field. */
    protected CalibratedDoubleField tiltField;

    /** Sample rotation first Euler field. */
    protected RotationField sampleRotationField;

    /** Pattern center horizontal coordinate field. */
    protected DoubleField pcXField;

    /** Pattern center vertical coordinate field. */
    protected DoubleField pcYField;

    /** Detector distance field. */
    protected CalibratedDoubleField ddField;



    /**
     * Creates a new <code>AcqMetadataWizardPage</code>. This page is to setup
     * all the EBSD metadata.
     */
    public AcquisitionConfigWizardPage() {
        setLayout(new MigLayout());

        // Microscope
        Panel microscopePanel = new Panel(new MigLayout());

        microscopePanel.add(new JLabel("Microscope"));

        Microscope[] microscopes = MicroscopeUtil.getMicroscopes();
        if (microscopes.length == 0)
            throw new IllegalArgumentException(
                    "At least one microscope configuration must exist.");

        microscopesCBox = new ComboBox<Microscope>(microscopes);
        microscopePanel.add(microscopesCBox, "growx, pushx, wrap");

        add(microscopePanel, "grow, push, wrap");

        /* Column */
        Panel columnPanel = new Panel(new MigLayout());
        columnPanel.setName("Column");
        columnPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Column"),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        // Beam energy
        columnPanel.add(new JLabel("Beam energy"));

        String[] units = new String[] { "eV", "keV" };
        Magnitude defaultValue =
                new Magnitude(AcquisitionConfig.DEFAULT.beamEnergy, "eV");
        energyField =
                new CalibratedDoubleField("beam energy", defaultValue, units,
                        false);
        energyField.setRange(new Magnitude(0, "eV"), new Magnitude(300, "keV"));
        columnPanel.add(energyField);
        columnPanel.add(energyField.getUnitsField(), "wrap");

        // Magnification
        columnPanel.add(new JLabel("Magnification"));

        magField =
                new DoubleField("Magnification",
                        AcquisitionConfig.DEFAULT.magnification);
        magField.setRange(0, 1e7);
        columnPanel.add(magField);
        columnPanel.add(new JLabel("X"), "wrap");

        // Working distance
        columnPanel.add(new JLabel("Working distance"));

        units = new String[] { "mm", "cm" };
        defaultValue =
                new Magnitude(AcquisitionConfig.DEFAULT.workingDistance, "m");
        defaultValue.setPreferredUnits("mm");
        wdField =
                new CalibratedDoubleField("Working distance", defaultValue,
                        units, false);
        wdField.setRange(new Magnitude(0, "mm"), new Magnitude(45, "mm"));
        columnPanel.add(wdField);
        columnPanel.add(wdField.getUnitsField(), "wrap");

        add(columnPanel, "grow, push, wrap");

        /* Sample */
        Panel samplePanel = new Panel(new MigLayout());
        samplePanel.setName("Sample");
        samplePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Sample"),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        // Tilt angle
        samplePanel.add(new JLabel("Tilt angle"));

        units = new String[] { "deg", "rad" };
        defaultValue =
                new Magnitude(AcquisitionConfig.DEFAULT.tiltAngle, "rad");
        defaultValue.setPreferredUnits("deg");
        tiltField =
                new CalibratedDoubleField("tilt angle", defaultValue, units);
        tiltField.setRange(new Magnitude(0, "deg"), new Magnitude(90, "deg"));
        samplePanel.add(tiltField, "wrap");

        // Rotation
        samplePanel.add(new JLabel("Rotation"));

        sampleRotationField =
                new RotationField("rotation",
                        AcquisitionConfig.DEFAULT.sampleRotation);
        samplePanel.add(sampleRotationField, "wrap");

        add(samplePanel, "grow, push, wrap");

        /* Camera */
        Panel cameraPanel = new Panel(new MigLayout());
        cameraPanel.setName("Camera");
        cameraPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Camera"),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        // Pattern center
        cameraPanel.add(new JLabel("Pattern center"));

        Panel patternCenterPanel = new Panel(new MigLayout());

        patternCenterPanel.add("x");
        pcXField =
                new DoubleField("x",
                        AcquisitionConfig.DEFAULT.patternCenterX * 100.0);
        pcXField.setRange(0, 100);
        patternCenterPanel.add(pcXField);
        patternCenterPanel.add(new JLabel("%"));

        patternCenterPanel.add("y", "gapleft 10");
        pcYField =
                new DoubleField("y",
                        AcquisitionConfig.DEFAULT.patternCenterY * 100.0);
        pcYField.setRange(0, 100);
        patternCenterPanel.add(pcYField);
        patternCenterPanel.add(new JLabel("%"));

        cameraPanel.add(patternCenterPanel, "wrap");

        // Detector distance
        cameraPanel.add(new JLabel("Detector distance"));

        units = new String[] { "mm", "cm" };
        defaultValue =
                new Magnitude(AcquisitionConfig.DEFAULT.cameraDistance, "m");
        defaultValue.setPreferredUnits("mm");
        ddField =
                new CalibratedDoubleField("detector distance", defaultValue,
                        units);
        ddField.setRange(new Magnitude(0, "cm"), new Magnitude(45, "cm"));
        cameraPanel.add(ddField, "wrap");

        add(cameraPanel, "grow, push, wrap");
    }



    /**
     * Returns the metadata constructed from this page. The method
     * {@link #isCorrect()} should be called prior to this method to make sure
     * all the data is correct.
     * 
     * @return a <code>EbsdMetadata</code>
     */
    protected AcquisitionConfig getAcquisitionConfig() {
        Microscope microscope = microscopesCBox.getSelectedItem();

        double beamEnergy = energyField.getValue().getValue("eV");
        double magnification = magField.getValue();
        double workingDistance = wdField.getValue().getValue("m");

        double tiltAngle = tiltField.getValue().getValue("rad");
        Rotation sampleRotation = sampleRotationField.getValue();

        double patternCenterX = pcXField.getValue() / 100.0;
        double patternCenterY = pcYField.getValue() / 100.0;
        double cameraDistance = ddField.getValue().getValue("m");

        return new AcquisitionConfig(microscope, tiltAngle, workingDistance,
                beamEnergy, magnification, sampleRotation, patternCenterX,
                patternCenterY, cameraDistance);
    }



    @Override
    public boolean isCorrect(boolean buffer) {
        if (!energyField.isCorrect())
            return false;
        if (!magField.isCorrect())
            return false;
        if (!wdField.isCorrect())
            return false;

        if (!tiltField.isCorrect())
            return false;
        if (!sampleRotationField.isCorrect())
            return false;

        if (!pcXField.isCorrect())
            return false;
        if (!pcYField.isCorrect())
            return false;
        if (!ddField.isCorrect())
            return false;

        AcquisitionConfig acqConfig;
        try {
            acqConfig = getAcquisitionConfig();
        } catch (Exception ex) {
            showErrorDialog(ex.getMessage());
            return false;
        }

        if (buffer)
            put(KEY_ACQUISITION_CONFIG, acqConfig);

        return true;
    }



    /**
     * Render the page by filling the fields with the values from the specified
     * microscope.
     * 
     * @param acqConfig
     *            acquisition configuration
     */
    protected void renderingPage(AcquisitionConfig acqConfig) {
        energyField.setValue(new Magnitude(acqConfig.beamEnergy, "eV"));
        magField.setValue(acqConfig.magnification);

        Magnitude wd = new Magnitude(acqConfig.workingDistance, "m");
        wd.setPreferredUnits("mm");
        wdField.setValue(wd);

        Magnitude tiltAngle = new Magnitude(acqConfig.tiltAngle, "rad");
        tiltAngle.setPreferredUnits("deg");
        tiltField.setValue(tiltAngle);
        sampleRotationField.setValue(acqConfig.sampleRotation);

        pcXField.setValue(acqConfig.patternCenterX * 100.0);
        pcYField.setValue(acqConfig.patternCenterY * 100.0);

        Magnitude dd = new Magnitude(acqConfig.cameraDistance, "m");
        dd.setPreferredUnits("cm");
        ddField.setValue(dd);
    }

}
