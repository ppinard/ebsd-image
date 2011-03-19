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

import javax.swing.BorderFactory;
import javax.swing.JLabel;

import magnitude.core.Magnitude;
import net.miginfocom.swing.MigLayout;

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
public class MicroscopeWizardPage extends WizardPage {

    /** Map key for the metadata. */
    public static final String KEY_MICROSCOPE = "microscope";

    /**
     * Map key to store if the data has been previously loaded. It prevents
     * loading the temporary metadata twice.
     */
    public static final String KEY_LOADED = "microscope.loaded";

    /** Combo box to select the microscope configuration. */
    private ComboBox<Microscope> microscopesCBox;

    /** Beam energy field. */
    private CalibratedDoubleField energyField;

    /** Magnification field. */
    private DoubleField magField;

    /** Working distance field. */
    private CalibratedDoubleField wdField;

    /** Sample tilt field. */
    private CalibratedDoubleField tiltField;

    /** Sample rotation first Euler field. */
    private RotationField sampleRotationField;

    /** Pattern center horizontal coordinate field. */
    private DoubleField pcXField;

    /** Pattern center vertical coordinate field. */
    private DoubleField pcYField;

    /** Detector distance field. */
    private CalibratedDoubleField ddField;



    /**
     * Creates a new <code>AcqMetadataWizardPage</code>. This page is to setup
     * all the EBSD metadata.
     */
    public MicroscopeWizardPage() {
        setLayout(new MigLayout());

        Microscope defaultMicroscope = new Microscope();

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
                new Magnitude(defaultMicroscope.getBeamEnergy(), "eV");
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
                        defaultMicroscope.getMagnification());
        magField.setRange(0, 1e7);
        columnPanel.add(magField);
        columnPanel.add(new JLabel("X"), "wrap");

        // Working distance
        columnPanel.add(new JLabel("Working distance"));

        units = new String[] { "mm", "cm" };
        defaultValue =
                new Magnitude(defaultMicroscope.getWorkingDistance(), "m");
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
        defaultValue = new Magnitude(defaultMicroscope.getTiltAngle(), "rad");
        defaultValue.setPreferredUnits("deg");
        tiltField =
                new CalibratedDoubleField("tilt angle", defaultValue, units);
        tiltField.setRange(new Magnitude(0, "deg"), new Magnitude(90, "deg"));
        samplePanel.add(tiltField, "wrap");

        // Rotation
        samplePanel.add(new JLabel("Rotation"));

        sampleRotationField =
                new RotationField("rotation",
                        defaultMicroscope.getSampleRotation());
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
                        defaultMicroscope.getPatternCenterX() * 100.0);
        pcXField.setRange(0, 100);
        patternCenterPanel.add(pcXField);
        patternCenterPanel.add(new JLabel("%"));

        patternCenterPanel.add("y", "gapleft 10");
        pcYField =
                new DoubleField("y",
                        defaultMicroscope.getPatternCenterY() * 100.0);
        pcYField.setRange(0, 100);
        patternCenterPanel.add(pcYField);
        patternCenterPanel.add(new JLabel("%"));

        cameraPanel.add(patternCenterPanel, "wrap");

        // Detector distance
        cameraPanel.add(new JLabel("Detector distance"));

        units = new String[] { "mm", "cm" };
        defaultValue =
                new Magnitude(defaultMicroscope.getCameraDistance(), "m");
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
    private Microscope getMicroscope() {
        Microscope microscope = microscopesCBox.getSelectedItem();

        microscope.setBeamEnergy(energyField.getValue());
        microscope.setMagnification(magField.getValue());
        microscope.setWorkingDistance(wdField.getValue());

        microscope.setTiltAngle(tiltField.getValue());
        microscope.setSampleRotation(sampleRotationField.getValue());

        microscope.setPatternCenterX(pcXField.getValue() / 100.0);
        microscope.setPatternCenterY(pcYField.getValue() / 100.0);
        microscope.setCameraDistance(ddField.getValue());

        return microscope;
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

        Microscope microscope;
        try {
            microscope = getMicroscope();
        } catch (Exception ex) {
            showErrorDialog(ex.getMessage());
            return false;
        }

        if (buffer)
            put(KEY_MICROSCOPE, microscope);

        return true;
    }



    /**
     * Render the page by filling the fields with the values from the specified
     * microscope.
     * 
     * @param microscope
     *            microscope configuration
     */
    protected void renderingPage(Microscope microscope) {
        energyField.setValue(new Magnitude(microscope.getBeamEnergy(), "eV"));
        magField.setValue(microscope.getMagnification());

        Magnitude wd = new Magnitude(microscope.getWorkingDistance(), "m");
        wd.setPreferredUnits("mm");
        wdField.setValue(wd);

        Magnitude tiltAngle = new Magnitude(microscope.getTiltAngle(), "rad");
        tiltAngle.setPreferredUnits("deg");
        tiltField.setValue(tiltAngle);
        sampleRotationField.setValue(microscope.getSampleRotation());

        pcXField.setValue(microscope.getPatternCenterX() * 100.0);
        pcYField.setValue(microscope.getPatternCenterY() * 100.0);

        Magnitude dd = new Magnitude(microscope.getCameraDistance(), "m");
        dd.setPreferredUnits("cm");
        ddField.setValue(dd);
    }

}
