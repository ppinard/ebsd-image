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
package org.ebsdimage.gui.sim.wizard;

/**
 * Template for the phases' wizard page.
 * 
 * @author Philippe T. Pinard
 */
public class PhasesWizardPage extends org.ebsdimage.gui.PhasesWizardPage {

    /**
     * Returns a description of this step.
     * 
     * @return description
     */
    public static String getDescription() {
        return "Phases";
    }



    /**
     * Creates a new <code>PhasesWizardPage</code> with a minimum required
     * number of phases as 1.
     */
    public PhasesWizardPage() {
        super();
        setMinimumPhasesCount(1);
    }

}
