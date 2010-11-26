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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import net.miginfocom.swing.MigLayout;

import org.ebsdimage.core.EbsdMMap;

import ptpshared.gui.DirBrowserField;
import ptpshared.gui.SingleFileBrowserField;
import ptpshared.gui.WizardPage;
import rmlshared.gui.IntField;
import rmlshared.gui.RadioButton;
import rmlshared.util.Preferences;

/**
 * Template for the patterns wizard page.
 * 
 * @author Philippe T. Pinard
 */
public class PatternsWizardPage extends WizardPage {

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

    /** Radio button for the smp file selection. */
    private RadioButton smpFileRButton;

    /** Field for the smp file. */
    private SingleFileBrowserField smpFileField;

    /** Radio button for the folder selection. */
    private RadioButton patternFolderRButton;

    /** Field for the folder location. */
    private DirBrowserField patternFolderField;

    /** Field for the width of the map (smp file selection only). */
    private IntField widthField1;

    /** Field for the height of the map (smp file selection only). */
    private IntField heightField1;

    /** Field for the width of the map (folder selection only). */
    private IntField widthField2;

    /** Field for the height of the map (folder selection only). */
    private IntField heightField2;

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
        add(smpFileRButton, "wrap");

        add(new JLabel("File"), "gapleft 35");

        FileFilter[] filters =
                new FileFilter[] { new FileNameExtensionFilter(
                        "SMP file (*.smp)", "smp") };
        smpFileField = new SingleFileBrowserField("SMP file", true, filters);
        add(smpFileField, "growx, pushx, wrap");

        add(new JLabel("Mapping's width"), "gapleft 35");
        widthField1 = new IntField("width", 512);
        add(widthField1, "split 5");
        add(new JLabel("px"));

        add(new JLabel("Mapping's height"), "gapleft 35");
        heightField1 = new IntField("height", 512);
        add(heightField1);
        add(new JLabel("px"), "wrap");

        patternFolderRButton = new RadioButton("From a folder of patterns");
        patternFolderRButton.addActionListener(new RefreshListener());
        add(patternFolderRButton, "span");

        add(new JLabel("Folder"), "gapleft 35");
        patternFolderField = new DirBrowserField("Folder of patterns", true);
        add(patternFolderField, "growx, pushx, wrap");

        add(new JLabel("Mapping's width"), "gapleft 35");
        widthField2 = new IntField("width", 512);
        add(widthField2, "split 5");
        add(new JLabel("px"));

        add(new JLabel("Mapping's height"), "gapleft 35");
        heightField2 = new IntField("height", 512);
        add(heightField2);
        add(new JLabel("px"), "wrap");

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

        if (smpFileRButton.isSelected()) {
            if (smpFileField.getFile() == null) {
                showErrorDialog("Please specify a smp file.");
                return false;
            }

            if (!widthField1.isCorrect())
                return false;

            if (!heightField1.isCorrect())
                return false;

            smpFile = smpFileField.getFile();
            width = widthField1.getValue();
            height = heightField1.getValue();
        } else if (patternFolderRButton.isSelected()) {
            if (patternFolderField.getDir() == null) {
                showErrorDialog("Please specify a folder.");
                return false;
            }

            if (!widthField2.isCorrect())
                return false;

            if (!heightField2.isCorrect())
                return false;

            dir = patternFolderField.getDir();
            width = widthField2.getValue();
            height = heightField2.getValue();
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
        }

        return true;
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
            widthField1.setValue(mmap.width);
            widthField2.setValue(mmap.width);
        } catch (IllegalArgumentException ex) {
            showErrorDialog("Width value could not be loaded from EbsdMMap.");
        }

        try {
            heightField1.setValue(mmap.height);
            heightField2.setValue(mmap.height);
        } catch (IllegalArgumentException ex) {
            showErrorDialog("Height value could not be loaded from EbsdMMap.");
        }

        put(KEY_LOADED, 1);
    }



    @Override
    public void setPreferences(Preferences pref) {
        super.setPreferences(pref);
        refresh();
    }



    /**
     * Refresh radio buttons and fields.
     */
    private void refresh() {
        smpFileField.setEnabled(smpFileRButton.isSelected());
        widthField1.setEnabled(smpFileRButton.isSelected());
        heightField1.setEnabled(smpFileRButton.isSelected());

        widthField2.setEnabled(patternFolderRButton.isSelected());
        heightField2.setEnabled(patternFolderRButton.isSelected());
        patternFolderField.setEnabled(patternFolderRButton.isSelected());

        patternFileField.setEnabled(patternFileRButton.isSelected());
    }
}
