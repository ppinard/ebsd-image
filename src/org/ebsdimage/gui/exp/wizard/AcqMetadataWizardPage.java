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
package org.ebsdimage.gui.exp.wizard;

import javax.swing.BorderFactory;
import javax.swing.JLabel;

import magnitude.core.Magnitude;
import net.miginfocom.swing.MigLayout;

import org.ebsdimage.core.EbsdMMap;
import org.ebsdimage.core.EbsdMetadata;
import org.ebsdimage.core.Microscope;
import org.ebsdimage.io.MicroscopeUtil;

import ptpshared.gui.CalibratedDoubleField;
import ptpshared.gui.RotationField;
import ptpshared.gui.WizardPage;
import rmlimage.core.Calibration;
import rmlshared.gui.ComboBox;
import rmlshared.gui.DoubleField;
import rmlshared.gui.Panel;

/**
 * Template for the acquisition metadata wizard page.
 * 
 * @author Philippe T. Pinard
 */
public class AcqMetadataWizardPage extends WizardPage {

    /** Map key for the calibration. */
    public static final String KEY_CALIBRATION = "calibration";

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

    /** Combo box to select the microscope configuration. */
    private ComboBox<Microscope> microscopesCBox;

    /** Beam energy field. */
    private CalibratedDoubleField energyField;

    /** Magnification field. */
    private DoubleField magField;

    /** Working distance field. */
    private CalibratedDoubleField wdField;

    /** Horizontal step size field. */
    private CalibratedDoubleField dxField;

    /** Vertical step size field. */
    private CalibratedDoubleField dyField;

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
    public AcqMetadataWizardPage() {
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
        microscopePanel.add(microscopesCBox, "grow, push, wrap");

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

        /* Step size */
        Panel stepSizePanel = new Panel(new MigLayout());
        stepSizePanel.setName("Step size");
        stepSizePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Step size"),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        units = new String[] { "nm", "um", "mm" };
        defaultValue = new Magnitude(1, "um");
        Magnitude min = new Magnitude(1, "nm");
        Magnitude max = new Magnitude(1, "cm");

        // Horizontal
        stepSizePanel.add(new JLabel("\u0394x"));

        dxField = new CalibratedDoubleField("dx", defaultValue, units);
        dxField.setRange(min, max);
        stepSizePanel.add(dxField);

        // Vertical
        stepSizePanel.add(new JLabel("\u0394y"), "gapleft 30");

        dyField = new CalibratedDoubleField("dy", defaultValue, units);
        dyField.setRange(min, max);
        stepSizePanel.add(dyField);

        add(stepSizePanel, "grow, push, wrap");

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
     * Returns the calibration of the EBSD multimap constructed from this page.
     * The method {@link #isCorrect()} should be called prior to this method to
     * make sure all the data is correct.
     * 
     * @return a <code>Calibration</code>
     */
    public Calibration getCalibration() {
        return new Calibration(dxField.getValue(), dyField.getValue());
    }



    /**
     * Returns the metadata constructed from this page. The method
     * {@link #isCorrect()} should be called prior to this method to make sure
     * all the data is correct.
     * 
     * @return a <code>EbsdMetadata</code>
     */
    public EbsdMetadata getMetadata() {
        Microscope microscope = microscopesCBox.getSelectedItem();

        microscope.setBeamEnergy(energyField.getValue());
        microscope.setMagnification(magField.getValue());
        microscope.setWorkingDistance(wdField.getValue());

        microscope.setTiltAngle(tiltField.getValue());
        microscope.setSampleRotation(sampleRotationField.getValue());

        microscope.setPatternCenterX(pcXField.getValue() / 100.0);
        microscope.setPatternCenterY(pcYField.getValue() / 100.0);
        microscope.setCameraDistance(ddField.getValue());

        return new EbsdMetadata(microscope);
    }



    @Override
    public boolean isCorrect(boolean buffer) {
        if (!energyField.isCorrect())
            return false;
        if (!magField.isCorrect())
            return false;
        if (!wdField.isCorrect())
            return false;

        if (!dxField.isCorrect())
            return false;
        if (!dyField.isCorrect())
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

        EbsdMetadata metadata;
        try {
            metadata = getMetadata();
        } catch (Exception ex) {
            showErrorDialog(ex.getMessage());
            return false;
        }

        Calibration calibration;
        try {
            calibration = getCalibration();
        } catch (Exception ex) {
            showErrorDialog(ex.getMessage());
            return false;
        }

        if (buffer) {
            put(KEY_METADATA, metadata);
            put(KEY_CALIBRATION, calibration);
        }

        return true;
    }



    @Override
    protected void renderingPage() {
        super.renderingPage();

        EbsdMMap mmap = (EbsdMMap) get(StartWizardPage.KEY_TEMP_EBSDMMAP);

        if (mmap == null)
            return;

        EbsdMetadata metadata = mmap.getMetadata();
        Microscope microscope = metadata.microscope;
        Calibration calibration = mmap.getCalibration();

        if (get(KEY_LOADED) != null)
            if ((Integer) get(KEY_LOADED) > 0)
                return;

        energyField.setValue(new Magnitude(microscope.getBeamEnergy(), "ev"));
        magField.setValue(microscope.getMagnification());
        wdField.setValue(new Magnitude(microscope.getWorkingDistance(), "m"));

        dxField.setValue(calibration.getDX());
        dyField.setValue(calibration.getDY());

        tiltField.setValue(new Magnitude(microscope.getTiltAngle(), "rad"));
        sampleRotationField.setValue(microscope.getSampleRotation());

        pcXField.setValue(microscope.getPatternCenterX() * 100.0);
        pcYField.setValue(microscope.getPatternCenterY() * 100.0);
        ddField.setValue(new Magnitude(microscope.getCameraDistance(), "m"));

        put(KEY_LOADED, 1);
    }

}
