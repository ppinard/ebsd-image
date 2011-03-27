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
