package org.ebsdimage.vendors.hkl.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import org.ebsdimage.vendors.hkl.core.HklMetadata;
import org.ebsdimage.vendors.hkl.io.CprLoader;

/**
 * Wizard page to fine-tune the microscope configuration.
 * 
 * @author ppinard
 */
public class MicroscopeWizardPage extends
        org.ebsdimage.gui.MicroscopeWizardPage {

    /**
     * Action listener of the microscopes combo box.
     * 
     * @author ppinard
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

    /** CPR file being loaded. */
    private File cprFile;



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
        if (cprFile == null) {
            return;
        }

        HklMetadata metadata;
        try {
            metadata = new CprLoader().load(cprFile, getMicroscope());
        } catch (IOException e) {
            showErrorDialog("Cannot load CPR file.");
            return;
        }

        renderingPage(metadata.getMicroscope());
    }



    @Override
    protected void renderingPage() {
        super.renderingPage();

        cprFile = (File) get(StartWizardPage.KEY_CPR_FILE);

        refresh();
    }
}
