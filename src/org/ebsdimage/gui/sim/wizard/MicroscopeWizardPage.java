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
