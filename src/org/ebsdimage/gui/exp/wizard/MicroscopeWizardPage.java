package org.ebsdimage.gui.exp.wizard;

import org.ebsdimage.core.EbsdMMap;

/**
 * Microscope configuration wizard page.
 * 
 * @author ppinard
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

        EbsdMMap mmap = (EbsdMMap) get(StartWizardPage.KEY_TEMP_EBSDMMAP);

        if (mmap == null)
            return;

        if (get(KEY_LOADED) != null)
            if ((Integer) get(KEY_LOADED) > 0)
                return;

        renderingPage(mmap.getMetadata().microscope);

        put(KEY_LOADED, 1);
    }
}
