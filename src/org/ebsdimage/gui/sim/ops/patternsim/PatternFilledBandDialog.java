package org.ebsdimage.gui.sim.ops.patternsim;

import org.ebsdimage.core.run.Operation;
import org.ebsdimage.core.sim.ops.patternsim.PatternFilledBand;

/**
 * Dialog for the <code>PatternFilledBand</code> operation.
 * 
 * @author ppinard
 */
public class PatternFilledBandDialog extends PatternSimOpDialog {

    /**
     * Creates a new <code>PatternFilledBandDialog</code>.
     */
    public PatternFilledBandDialog() {
        super("Pattern Filled Band");
    }



    @Override
    public String toString() {
        return "Pattern Filled Band";
    }



    @Override
    public String getDescription() {
        return "Simulate a pattern where the bands are filled with a "
                + "constant tone of gray.";
    }



    @Override
    public Operation getOperation() {
        return new PatternFilledBand(widthField.getValueBFR(),
                heightField.getValueBFR(), maxIndexfield.getValueBFR(),
                scatterTypeField.getSelectedItemBFR());
    }

}
