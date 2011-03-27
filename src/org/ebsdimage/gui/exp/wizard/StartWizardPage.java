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
import java.io.IOException;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import net.miginfocom.swing.MigLayout;

import org.ebsdimage.core.EbsdMMap;
import org.ebsdimage.core.exp.Exp;
import org.ebsdimage.io.EbsdMMapLoader;
import org.ebsdimage.io.exp.ExpLoader;
import org.netbeans.spi.wizard.Wizard;
import org.netbeans.spi.wizard.WizardPanelNavResult;

import ptpshared.gui.SingleFileBrowserField;
import ptpshared.gui.WizardPage;
import rmlimage.io.IO;
import rmlshared.gui.CheckBox;
import rmlshared.io.Loader;
import rmlshared.util.Preferences;

/**
 * Template for the start page of the wizard.
 * 
 * @author Philippe T. Pinard
 */
public class StartWizardPage extends WizardPage {

    /**
     * Listener to enable/disable the fields.
     */
    private class RefreshListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            refresh();
        }
    }

    /** Map key for the temporary EbsdMMap. */
    public static final String KEY_TEMP_EBSDMMAP = "start.ebsdmmap";

    /** Map key for the temporary Exp. */
    public static final String KEY_TEMP_EXP = "start.exp";



    /**
     * Returns a description of this step.
     * 
     * @return description
     */
    public static String getDescription() {
        return "Start";
    }

    /** Check box for importing the metadata. */
    private CheckBox metadataCBox;

    /** Field for the metadata file. */
    private SingleFileBrowserField metadataFileField;

    /** Check box for importing the operations. */
    private CheckBox opsCBox;

    /** Field for the operations file. */
    private SingleFileBrowserField opsFileField;



    /**
     * Creates a new <code>StartWizardPage</code>.
     */
    public StartWizardPage() {

        setLayout(new MigLayout("", "[grow,fill]", ""));

        // Welcome
        String text =
                "<html>This is the wizard for the analysis engine. "
                        + "In EBSD-Image, the procedure of going from diffraction "
                        + "patterns to indexed solutions is called an experiment. "
                        + "All the parameters and operations of an experiment are "
                        + "setup using this wizard. "
                        + "<br/><br/>"
                        + "The wizard has 11 steps. The first "
                        + "steps are to setup the parameters such as the working "
                        + "directory, phases and calibration. Other information about "
                        + "the EBSD acquisition such as the beam energy, "
                        + "magnification, working distance, etc. can also be "
                        + "specified. All these information are saved with the final "
                        + "results. The latter steps are to select the operations. "
                        + "<br/><br/>"
                        + "For further information on this wizard, please refer to "
                        + "the \"How to run an experiment\" quick-start guide on "
                        + "http://ebsd-image.org." + "</html>";
        add(new JLabel(text), "grow, wrap 40");

        metadataCBox = new CheckBox("Import metadata");
        metadataCBox.addActionListener(new RefreshListener());
        add(metadataCBox, "wrap");

        FileFilter[] filters =
                new FileFilter[] { new FileNameExtensionFilter(
                        "EBSD multimap (*.zip)", "zip") };
        metadataFileField =
                new SingleFileBrowserField("Metadata file", true, filters);
        add(metadataFileField, "gapleft 35, wrap");

        opsCBox = new CheckBox("Import operations");
        opsCBox.addActionListener(new RefreshListener());
        add(opsCBox, "wrap");

        filters =
                new FileFilter[] { new FileNameExtensionFilter(
                        "Experiment operations (*.xml)", "xml") };
        opsFileField =
                new SingleFileBrowserField("Operations file", true, filters);
        add(opsFileField, "gapleft 35, wrap");

        // Refresh
        refresh();
    }



    @Override
    public WizardPanelNavResult allowNext(String stepName,
            @SuppressWarnings("rawtypes") Map settings, Wizard wizard) {

        put(org.ebsdimage.gui.MicroscopeWizardPage.KEY_LOADED, 0);
        put(PatternWizardPage.KEY_LOADED, 0);
        put(HoughWizardPage.KEY_LOADED, 0);
        put(DetectionWizardPage.KEY_LOADED, 0);
        put(PositioningWizardPage.KEY_LOADED, 0);
        put(IndexingWizardPage.KEY_LOADED, 0);

        return super.allowNext(stepName, settings, wizard);
    };



    @Override
    public boolean isCorrect(boolean buffer) {
        EbsdMMap mmap = null;
        Exp exp = null;

        if (metadataCBox.isSelected()) {
            if (metadataFileField.getFile() == null) {
                showErrorDialog("Specify the file for the metadata.");
                return false;
            } else {
                File metadataFile = metadataFileField.getFile();

                Loader loader = IO.getLoader(metadataFile);
                if (loader instanceof EbsdMMapLoader) {
                    rmlimage.core.Map map;
                    try {
                        map = IO.load(metadataFile);
                    } catch (IOException e) {
                        showErrorDialog("Not a valid EBSD multimap");
                        return false;
                    }

                    mmap = (EbsdMMap) map;
                }

                loader = new ExpLoader();
                if (loader.canLoad(metadataFile)) {
                    try {
                        mmap = ((ExpLoader) loader).load(metadataFile).mmap;
                    } catch (IOException e) {
                        showErrorDialog("Could not import the metadata from "
                                + "the specified file.");
                        return false;
                    }
                }

                if (mmap == null) {
                    showErrorDialog("Cannot load (" + metadataFile + ").");
                    return false;
                }
            }
        }

        if (opsCBox.isSelected()) {
            if (opsFileField.getFile() == null) {
                showErrorDialog("Specify the file for the operations.");
                return false;
            } else {
                File opsFile = opsFileField.getFile();

                try {
                    exp = new ExpLoader().load(opsFile);
                } catch (IOException ex) {
                    showErrorDialog("Could not import operations from "
                            + "the specified file.");
                    return false;
                }

                if (exp == null) {
                    showErrorDialog("Cannot load (" + opsFile + ").");
                    return false;
                }
            }

        }

        if (buffer) {
            put(KEY_TEMP_EBSDMMAP, mmap);
            put(KEY_TEMP_EXP, exp);
        }

        return true;
    }



    /**
     * Refresh combo boxes and file fields.
     */
    private void refresh() {
        metadataFileField.setEnabled(metadataCBox.isSelected());
        opsFileField.setEnabled(opsCBox.isSelected());
    }



    @Override
    public void setPreferences(Preferences pref) {
        super.setPreferences(pref);
        refresh();
    }

}
