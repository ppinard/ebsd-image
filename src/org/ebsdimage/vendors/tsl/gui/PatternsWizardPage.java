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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

import org.ebsdimage.vendors.tsl.io.Utils;

import ptpshared.gui.DirBrowserField;
import ptpshared.gui.WizardPage;
import rmlshared.gui.RadioButton;

/**
 * Wizard page to import or not diffraction patterns.
 * 
 * @author Philippe T. Pinard
 */
public class PatternsWizardPage extends WizardPage {

    /**
     * Listener to enable/disable the file browser field.
     */
    private class RButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            userPatternsDirField.setEnabled(userPatternsRButton.isSelected());
        }
    }

    /** Map key for the patterns' folder. */
    public static final String KEY_PATTERNS_FOLDER = "patternsFolder";



    /**
     * Returns a description of this page.
     * 
     * @return description
     */
    public static String getDescription() {
        return "Diffraction Patterns";
    }

    /** Do not import patterns radio button. */
    private RadioButton noPatternsRButton;

    /** Import patterns from the folder specified in the ANG radio button. */
    private RadioButton angPatternsRButton;

    /** Field to display what the folder specified in the ANG file is. */
    private JTextField angPatternsDirField;

    /** Import patterns from the folder specified by the user radio button. */
    private RadioButton userPatternsRButton;

    /** Field for the user defined folder for the patterns. */
    private DirBrowserField userPatternsDirField;



    /**
     * Creates a new <code>PatternsWizardPage</code>.
     */
    public PatternsWizardPage() {
        setLayout(new MigLayout());

        add(new JLabel("Three options whether or not to import "
                + "diffraction patterns."), "wrap");

        // No pattern
        noPatternsRButton =
                new RadioButton("Do not import diffraction patterns.");
        noPatternsRButton.addActionListener(new RButtonListener());
        add(noPatternsRButton, "wrap");

        // From CPR file
        angPatternsRButton =
                new RadioButton(
                        "Import from the folder relative to the ANG file.");
        angPatternsRButton.addActionListener(new RButtonListener());
        add(angPatternsRButton, "wrap");

        angPatternsDirField = new JTextField();
        angPatternsDirField.setEnabled(false);
        add(angPatternsDirField, "gapleft 35, growx, pushx, wrap");

        // User defined
        userPatternsRButton = new RadioButton("Import from a specific folder.");
        userPatternsRButton.addActionListener(new RButtonListener());
        add(userPatternsRButton, "wrap");

        userPatternsDirField = new DirBrowserField("User patterns folder");
        add(userPatternsDirField, "gapleft 35, growx, pushx, wrap");

        // Used to manage only one radio button being selected at a time
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(noPatternsRButton);
        buttonGroup.add(angPatternsRButton);
        buttonGroup.add(userPatternsRButton);

        // Initiate state
        noPatternsRButton.doClick();
    }



    @Override
    public boolean isCorrect(boolean buffer) {
        File patternsDir = null;

        if (angPatternsRButton.isSelected()) {
            patternsDir = new File(angPatternsDirField.getText());
        } else if (userPatternsRButton.isSelected()) {
            patternsDir = userPatternsDirField.getDir();

            if (patternsDir == null) {
                showErrorDialog("Please specify a folder.");
                return false;
            }
        }

        if (buffer)
            put(KEY_PATTERNS_FOLDER, patternsDir);

        return true;
    }



    @Override
    protected void renderingPage() {
        super.renderingPage();

        File cprFile = (File) get(StartWizardPage.KEY_ANG_FILE);

        if (cprFile == null) {
            showErrorDialog("No ANG file was defined in the Start wizard page.");
            return;
        }

        File patternsDir = Utils.getPatternImagesDir(cprFile);

        if (!patternsDir.exists()) {
            angPatternsRButton.setEnabled(false);
        } else {
            angPatternsRButton.setEnabled(true);
            angPatternsDirField.setText(patternsDir.toString());
        }
    }
}
