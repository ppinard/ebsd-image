package org.ebsdimage.gui.sim;

/**
 * Template for the phases' wizard page.
 * 
 * @author ppinard
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
