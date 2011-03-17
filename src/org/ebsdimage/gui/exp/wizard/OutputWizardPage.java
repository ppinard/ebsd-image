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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;

import net.miginfocom.swing.MigLayout;
import ptpshared.gui.WizardPage;
import rmlshared.gui.IntField;
import rmlshared.gui.RadioButton;

/**
 * Template for the output wizard page.
 * 
 * @author Philippe T. Pinard
 */
public class OutputWizardPage extends WizardPage {

    /**
     * Listener to enable/disable fields related to radio buttons.
     */
    private class RButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            previewIndexField.setEnabled(previewRButton.isSelected());
        }
    }

    /** Map key whether to run the experiment. */
    public static final String KEY_RUN = "output.run";

    /** Map key whether to save the experiment. */
    public static final String KEY_SAVE = "output.save";

    /** Map key to launch the experiment run in preview mode. */
    public static final String KEY_PREVIEW = "output.preview";

    /** Map key for the pattern index of the preview mode. */
    public static final String KEY_PREVIEW_INDEX = "output.preview.index";



    /**
     * Returns a description of this step.
     * 
     * @return description
     */
    public static String getDescription() {
        return "Output";
    }

    /** Radio button to run the experiment in preview mode. */
    private RadioButton previewRButton;

    /** Field for the pattern index on which to preview the experiment. */
    private IntField previewIndexField;

    /** Radio button to only save the experiment after closing the wizard. */
    private RadioButton saveOnlyRButton;

    /** Radio button to run the experiment after closing the wizard. */
    private RadioButton runOnlyRButton;

    /** Radio button to save and run the experiment after closing the wizard. */
    private RadioButton saveRunRButton;



    /**
     * Creates a new <code>OutputWizardPage</code>.
     */
    public OutputWizardPage() {

        setLayout(new MigLayout());

        previewRButton = new RadioButton("Preview mode");
        previewRButton.addActionListener(new RButtonListener());
        add(previewRButton, "span");

        add(new JLabel("Pattern index"), "gapleft 35");
        previewIndexField = new IntField("Preview index", 0);
        previewIndexField.setRange(0, Integer.MAX_VALUE);
        add(previewIndexField, "wrap");

        saveOnlyRButton = new RadioButton("Save XML only");
        saveOnlyRButton.addActionListener(new RButtonListener());
        add(saveOnlyRButton, "span");

        runOnlyRButton = new RadioButton("Run only");
        runOnlyRButton.addActionListener(new RButtonListener());
        add(runOnlyRButton, "span");

        saveRunRButton = new RadioButton("Save XML and run");
        saveRunRButton.addActionListener(new RButtonListener());
        add(saveRunRButton, "span");

        // Used to manage only one radio button being selected at a time
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(previewRButton);
        buttonGroup.add(saveOnlyRButton);
        buttonGroup.add(runOnlyRButton);
        buttonGroup.add(saveRunRButton);

        previewRButton.doClick();
    }



    @Override
    public boolean isCorrect(boolean buffer) {
        boolean run = false;
        boolean save = false;
        boolean preview = false;
        int previewIndex = -1;

        if (previewRButton.isSelected()) {
            if (!previewIndexField.isCorrect())
                return false;

            preview = true;
            run = true;
            save = true;
            previewIndex = previewIndexField.getValue();
        } else if (saveOnlyRButton.isSelected()) {
            save = true;
        } else if (runOnlyRButton.isSelected()) {
            run = true;
        } else if (saveRunRButton.isSelected()) {
            save = true;
            run = true;
        }

        if (buffer) {
            put(KEY_RUN, run);
            put(KEY_SAVE, save);
            put(KEY_PREVIEW, preview);
            put(KEY_PREVIEW_INDEX, previewIndex);
        }

        return true;
    }



    @Override
    protected void renderingPage() {
        super.renderingPage();

        previewIndexField.setRange(Integer.MIN_VALUE, Integer.MAX_VALUE);
        previewIndexField.setValue(0);

        int width = (Integer) get(PatternsWizardPage.KEY_WIDTH);
        int height = (Integer) get(PatternsWizardPage.KEY_HEIGHT);

        previewIndexField.setRange(0, width * height);
    }

}
