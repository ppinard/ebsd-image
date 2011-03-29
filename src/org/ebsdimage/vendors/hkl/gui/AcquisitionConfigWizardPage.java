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
 * @author Philippe T. Pinard
 */
public class AcquisitionConfigWizardPage extends
        org.ebsdimage.gui.AcquisitionConfigWizardPage {

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
        return "Acquisition configuration";
    }

    /** CPR file being loaded. */
    private File cprFile;



    /**
     * Creates a new <code>MicroscopeWizardPage</code>.
     */
    public AcquisitionConfigWizardPage() {
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
            metadata =
                    new CprLoader().load(cprFile,
                            getAcquisitionConfig().microscope);
        } catch (IOException e) {
            showErrorDialog("Cannot load CPR file.");
            return;
        }

        renderingPage(metadata.acquisitionConfig);
    }



    @Override
    protected void renderingPage() {
        super.renderingPage();

        cprFile = (File) get(StartWizardPage.KEY_CPR_FILE);

        refresh();
    }
}
