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

import javax.swing.ButtonGroup;
import javax.swing.JLabel;

import net.miginfocom.swing.MigLayout;
import ptpshared.gui.WizardPage;
import rmlshared.gui.FileNameField;
import rmlshared.gui.RadioButton;

/**
 * Wizard page to import or not diffraction patterns.
 * 
 * @author Philippe T. Pinard
 * 
 */
public class PatternsWizardPage extends WizardPage {

    /**
     * Listener to enable/disable the file browser field.
     */
    private class RButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            userPatternsFileFiled.setEnabled(userPatternsRButton.isSelected());
        }
    }



    /** Map key for the patterns folder. */
    public static final String KEY_PATTERNS_FOLDER = "patternsFolder";

    /** Map key for whether to save the patterns. */
    public static final String KEY_IMPORT_PATTERNS = "importPatterns";



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

    /** Import patterns from the folder specified in the ctf radio button. */
    private RadioButton ctfPatternsRButton;

    /** Import patterns from the folder specified by the user radio button. */
    private RadioButton userPatternsRButton;

    /** Field for the user defined folder for the patterns. */
    private FileNameField userPatternsFileFiled;



    /**
     * Creates a new <code>PatternsWizardPage</code>.
     */
    public PatternsWizardPage() {
        setLayout(new MigLayout());

        add(new JLabel("Three options whether or not to import "
                + "diffraction patterns."), "wrap");

        noPatternsRButton =
                new RadioButton("Do not import diffraction patterns.");
        noPatternsRButton.addActionListener(new RButtonListener());
        add(noPatternsRButton, "wrap");

        ctfPatternsRButton =
                new RadioButton(
                        "Import from the folder specified in the ctf file.");
        ctfPatternsRButton.addActionListener(new RButtonListener());
        add(ctfPatternsRButton, "wrap");

        userPatternsRButton = new RadioButton("Import from a specific folder.");
        userPatternsRButton.addActionListener(new RButtonListener());
        add(userPatternsRButton, "wrap");

        userPatternsFileFiled = new FileNameField("Pattern file", true);
        userPatternsFileFiled
                .setFileSelectionMode(FileNameField.DIRECTORIES_ONLY);
        add(userPatternsFileFiled, "gapleft 35, growx, pushx, wrap");

        // Used to manage only one radio button being selected at a time
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(noPatternsRButton);
        buttonGroup.add(ctfPatternsRButton);
        buttonGroup.add(userPatternsRButton);

        // Initiate state
        noPatternsRButton.doClick();
    }



    @Override
    public boolean isCorrect(boolean buffer) {
        if (userPatternsRButton.isSelected()) {
            if (userPatternsFileFiled.getFile() == null) {
                showErrorDialog("Please specify a folder.");
                return false;
            }
        }

        if (buffer) {
            if (ctfPatternsRButton.isSelected()
                    || userPatternsRButton.isSelected())
                put(KEY_IMPORT_PATTERNS, true);
            else
                put(KEY_IMPORT_PATTERNS, false);

            if (userPatternsRButton.isSelected())
                put(KEY_PATTERNS_FOLDER, userPatternsFileFiled.getFile());
            else
                put(KEY_PATTERNS_FOLDER, null);
        }

        return true;
    }

}
