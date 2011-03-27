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
package org.ebsdimage.gui.sim.wizard;

import org.ebsdimage.core.sim.Sim;

/**
 * Microscope configuration wizard page.
 * 
 * @author Philippe T. Pinard
 */
public class MicroscopeWizardPage extends
        org.ebsdimage.gui.MicroscopeWizardPage {

    /**
     * Returns a description of this step.
     * 
     * @return description
     */
    public static String getDescription() {
        return "Microscope configuration";
    }



    @Override
    protected void renderingPage() {
        super.renderingPage();

        Sim sim = (Sim) get(StartWizardPage.KEY_TEMP_SIM);

        if (sim == null)
            return;

        if (get(KEY_LOADED) != null)
            if ((Integer) get(KEY_LOADED) > 0)
                return;

        renderingPage(sim.getMetadata().getMicroscope());

        put(KEY_LOADED, 1);
    }
}
