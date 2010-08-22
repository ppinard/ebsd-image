package org.ebsdimage.gui.sim.ops.patternsim;

import org.ebsdimage.core.run.Operation;
import org.ebsdimage.core.sim.ops.patternsim.PatternBandCenter;

/**
 * Dialog for the <code>PatternBandCenter</code> operation.
 * 
 * @author ppinard
 */
public class PatternBandCenterDialog extends PatternSimOpDialog {

    /**
     * Creates a new <code>PatternBandCenterDialog</code>.
     */
    public PatternBandCenterDialog() {
        super("Pattern Band Center");
    }



    @Override
    public String toString() {
        return "Pattern Band Center";
    }



    @Override
    public String getDescription() {
        return "Simulate a pattern where the center of the bands is drawn.";
    }



    @Override
    public Operation getOperation() {
        return new PatternBandCenter(widthField.getValueBFR(),
                heightField.getValueBFR(), maxIndexfield.getValueBFR(),
                scatterTypeField.getSelectedItemBFR());
    }

}
