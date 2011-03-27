package org.ebsdimage.vendors.tsl.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import org.ebsdimage.vendors.tsl.core.TslMetadata;
import org.ebsdimage.vendors.tsl.io.AngLoader;

/**
 * Wizard page to fine-tune the microscope configuration.
 * 
 * @author Philippe T. Pinard
 */
public class MicroscopeWizardPage extends
        org.ebsdimage.gui.MicroscopeWizardPage {

    /**
     * Action listener of the microscopes combo box.
     * 
     * @author Philippe T. Pinard
     */
    private class MicroscopesCBoxListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            refresh();
        }

    }



    /**
     * Returns a description of this wizard page.
     * 
     * @return description
     */
    public static String getDescription() {
        return "Microscope configuration";
    }

    /** ANG file being loaded. */
    private File angFile;



    /**
     * Creates a new <code>MicroscopeWizardPage</code>.
     */
    public MicroscopeWizardPage() {
        super();

        microscopesCBox.addActionListener(new MicroscopesCBoxListener());
    }



    /**
     * Refresh the microscope configuration based on the selected microscope.
     */
    private void refresh() {
        if (angFile == null) {
            return;
        }

        TslMetadata metadata;
        try {
            metadata = new AngLoader().loadMetadata(angFile, getMicroscope());
        } catch (IOException e) {
            showErrorDialog("Cannot load ANG file.");
            return;
        }

        renderingPage(metadata.getMicroscope());
    }



    @Override
    protected void renderingPage() {
        super.renderingPage();

        angFile = (File) get(StartWizardPage.KEY_ANG_FILE);

        refresh();
    }
}
