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
package org.ebsdimage.gui.exp.wizard;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import magnitude.core.Magnitude;
import net.miginfocom.swing.MigLayout;

import org.ebsdimage.core.EbsdMMap;

import ptpshared.gui.CalibratedDoubleField;
import ptpshared.gui.DirBrowserField;
import ptpshared.gui.SingleFileBrowserField;
import ptpshared.gui.WizardPage;
import rmlimage.core.Calibration;
import rmlshared.gui.IntField;
import rmlshared.gui.RadioButton;
import rmlshared.ui.InputValidation;
import rmlshared.util.Preferences;

/**
 * Template for the patterns wizard page.
 * 
 * @author Philippe T. Pinard
 */
public class PatternsWizardPage extends WizardPage {

    /**
     * Panel to define the width, height and calibration of a mapping.
     * 
     * @author Philippe T. Pinard
     */
    private class MappingPanel extends JPanel implements InputValidation {

        /** Field for the width of the map. */
        private IntField widthField;

        /** Field for the height of the map. */
        private IntField heightField;

        /** Horizontal step size field. */
        private CalibratedDoubleField dxField;

        /** Vertical step size field. */
        private CalibratedDoubleField dyField;



        /**
         * Creates a new <code>MappingPanel</code>.
         * 
         * @param name
         *            name of the component
         * @param title
         *            title to appear on top of the panel
         */
        public MappingPanel(String name, String title) {
            setLayout(new MigLayout());
            setName(name);
            setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createTitledBorder(title),
                    BorderFactory.createEmptyBorder(5, 5, 5, 5)));

            /* Dimensions */
            // Width
            add(new JLabel("width"));

            widthField = new IntField("width", 512);
            widthField.setRange(1, Integer.MAX_VALUE);
            add(widthField);

            add(new JLabel("px"));

            // Height
            add(new JLabel("height"));
            heightField = new IntField("height", 512);
            heightField.setRange(1, Integer.MAX_VALUE);
            add(heightField);

            add(new JLabel("px"), "wrap");

            /* Step size */
            String[] units = new String[] { "nm", "um", "mm" };
            Magnitude defaultValue = new Magnitude(1, "um");
            Magnitude min = new Magnitude(1, "nm");
            Magnitude max = new Magnitude(1, "cm");

            // Horizontal
            add(new JLabel("\u0394x"));

            dxField =
                    new CalibratedDoubleField("dx", defaultValue, units, false);
            dxField.setRange(min, max);
            add(dxField);

            add(dxField.getUnitsField());

            // Vertical
            add(new JLabel("\u0394y"), "gapleft 30");

            dyField =
                    new CalibratedDoubleField("dy", defaultValue, units, false);
            dyField.setRange(min, max);
            add(dyField);

            add(dyField.getUnitsField());
        }



        /**
         * Returns the calibration of the EBSD multimap constructed from this
         * page. The method {@link #isCorrect()} should be called prior to this
         * method to make sure all the data is correct.
         * 
         * @return a <code>Calibration</code>
         */
        public Calibration getCalibration() {
            return new Calibration(dxField.getValue(), dyField.getValue());
        }



        /**
         * Returns the height of the mapping.
         * 
         * @return height
         */
        public int getMappingHeight() {
            return heightField.getValue();
        }



        /**
         * Returns the width of the mapping.
         * 
         * @return width
         */
        public int getMappingWidth() {
            return widthField.getValue();
        }



        @Override
        public boolean isCorrect() {
            return isCorrect(true);
        }



        @Override
        public boolean isCorrect(boolean showErrorDialog) {
            if (!widthField.isCorrect(showErrorDialog))
                return false;

            if (!heightField.isCorrect(showErrorDialog))
                return false;

            if (!dxField.isCorrect(showErrorDialog))
                return false;

            if (!dyField.isCorrect(showErrorDialog))
                return false;

            return true;
        }



        @Override
        public void setEnabled(boolean enabled) {
            super.setEnabled(enabled);

            widthField.setEnabled(enabled);
            heightField.setEnabled(enabled);
            dxField.setEnabled(enabled);
            dyField.setEnabled(enabled);
        }



        /**
         * Sets the values of the panel.
         * 
         * @param width
         *            width of the mapping
         * @param height
         *            height of the mapping
         * @param cal
         *            calibration of the mapping
         */
        public void setValues(int width, int height, Calibration cal) {
            widthField.setValue(width);
            heightField.setValue(height);
            dxField.setValue(cal.getDX());
            dyField.setValue(cal.getDY());
        }
    }

    /**
     * Listener to enable/disable the fields.
     */
    private class RefreshListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            refresh();
        }
    }

    /**
     * Map key to store if the data has been previously loaded. It prevents
     * loading the temporary metadata twice.
     */
    public static final String KEY_LOADED = "patterns.loaded";

    /** Map key for the calibration. */
    public static final String KEY_CALIBRATION = "calibration";

    /** Map key for the smp file. */
    public static final String KEY_SMP_FILE = "patterns.smpFile";

    /** Map key for the patterns' directory. */
    public static final String KEY_DIR = "patterns.dir";

    /** Map key for the pattern file. */
    public static final String KEY_PATTERN_FILE = "patterns.file";

    /** Map key for the map's width. */
    public static final String KEY_WIDTH = "patterns.width";

    /** Map key for the map's height. */
    public static final String KEY_HEIGHT = "patterns.height";



    /**
     * Returns a description of this step.
     * 
     * @return description
     */
    public static String getDescription() {
        return "Patterns";
    }

    /** Radio button for the SMP file selection. */
    private RadioButton smpFileRButton;

    /** Field for the SMP file. */
    private SingleFileBrowserField smpFileField;

    /** Mapping panel for the SMP file selection. */
    private MappingPanel smpMappingPanel;

    /** Radio button for the folder selection. */
    private RadioButton patternFolderRButton;

    /** Field for the folder location. */
    private DirBrowserField patternFolderField;

    /** Mapping panel for the folder selection. */
    private MappingPanel patternFolderMappingPanel;

    /** Radio button for the pattern file selection. */
    private RadioButton patternFileRButton;

    /** Field for the pattern file location. */
    private SingleFileBrowserField patternFileField;



    /**
     * Creates a new <code>PatternsWizardPage</code>.
     */
    public PatternsWizardPage() {

        setLayout(new MigLayout());

        smpFileRButton = new RadioButton("From an smp file");
        smpFileRButton.addActionListener(new RefreshListener());
        add(smpFileRButton, "span");

        add(new JLabel("File"), "gapleft 35");

        FileFilter[] filters =
                new FileFilter[] { new FileNameExtensionFilter(
                        "SMP file (*.smp)", "smp") };
        smpFileField = new SingleFileBrowserField("SMP file", true, filters);
        add(smpFileField, "growx, pushx, wrap");

        smpMappingPanel = new MappingPanel("smp-mapping", "Mapping");
        add(smpMappingPanel, "gapleft 35, growx, pushx, span");

        patternFolderRButton = new RadioButton("From a folder of patterns");
        patternFolderRButton.addActionListener(new RefreshListener());
        add(patternFolderRButton, "span");

        add(new JLabel("Folder"), "gapleft 35");
        patternFolderField = new DirBrowserField("Folder of patterns", true);
        add(patternFolderField, "growx, pushx, wrap");

        patternFolderMappingPanel = new MappingPanel("smp-mapping", "Mapping");
        add(patternFolderMappingPanel, "gapleft 35, growx, pushx, span");

        patternFileRButton = new RadioButton("From a single pattern");
        patternFileRButton.addActionListener(new RefreshListener());
        add(patternFileRButton, "span");

        add(new JLabel("File"), "gapleft 35");
        filters =
                new FileFilter[] { new FileNameExtensionFilter(
                        "Image files (*.bmp, *.jpg)", "bmp", "jpg") };
        patternFileField =
                new SingleFileBrowserField("Single pattern", true, filters);
        add(patternFileField, "growx, pushx");

        // Used to manage only one radio button being selected at a time
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(smpFileRButton);
        buttonGroup.add(patternFolderRButton);
        buttonGroup.add(patternFileRButton);

        // Initial state
        smpFileRButton.doClick();
        refresh();
    }



    @Override
    public boolean isCorrect(boolean buffer) {
        File smpFile = null;
        File dir = null;
        File patternFile = null;
        int width = 0;
        int height = 0;
        Calibration cal = Calibration.NONE;

        if (smpFileRButton.isSelected()) {
            if (smpFileField.getFile() == null) {
                showErrorDialog("Please specify a smp file.");
                return false;
            }

            if (!smpMappingPanel.isCorrect())
                return false;

            smpFile = smpFileField.getFile();
            width = smpMappingPanel.getMappingWidth();
            height = smpMappingPanel.getMappingHeight();
            cal = smpMappingPanel.getCalibration();

        } else if (patternFolderRButton.isSelected()) {
            if (patternFolderField.getDir() == null) {
                showErrorDialog("Please specify a folder.");
                return false;
            }

            if (!patternFolderMappingPanel.isCorrect())
                return false;

            dir = patternFolderField.getDir();
            width = patternFolderMappingPanel.getMappingWidth();
            height = patternFolderMappingPanel.getMappingHeight();
            cal = patternFolderMappingPanel.getCalibration();

        } else if (patternFileRButton.isSelected()) {
            if (patternFileField.getFile() == null) {
                showErrorDialog("Please specifiy a file.");
                return false;
            }

            patternFile = patternFileField.getFile();
            width = 1;
            height = 1;
        }

        if (buffer) {
            put(KEY_SMP_FILE, smpFile);
            put(KEY_DIR, dir);
            put(KEY_PATTERN_FILE, patternFile);
            put(KEY_WIDTH, width);
            put(KEY_HEIGHT, height);
            put(KEY_CALIBRATION, cal);
        }

        return true;
    }



    /**
     * Refresh radio buttons and fields.
     */
    private void refresh() {
        smpFileField.setEnabled(smpFileRButton.isSelected());
        smpMappingPanel.setEnabled(smpFileRButton.isSelected());

        patternFolderField.setEnabled(patternFolderRButton.isSelected());
        patternFolderMappingPanel.setEnabled(patternFolderRButton.isSelected());

        patternFileField.setEnabled(patternFileRButton.isSelected());
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

        smpMappingPanel.setValues(mmap.width, mmap.height,
                mmap.getCalibration());
        patternFolderMappingPanel.setValues(mmap.width, mmap.height,
                mmap.getCalibration());

        put(KEY_LOADED, 1);
    }



    @Override
    public void setPreferences(Preferences pref) {
        super.setPreferences(pref);
        refresh();
    }
}
