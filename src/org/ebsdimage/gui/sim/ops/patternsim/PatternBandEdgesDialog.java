package org.ebsdimage.gui.sim.ops.patternsim;

import org.ebsdimage.core.run.Operation;
import org.ebsdimage.core.sim.ops.patternsim.PatternBandEdges;

/**
 * Dialog for the <code>PatternBandEdges</code> operation.
 * 
 * @author ppinard
 */
public class PatternBandEdgesDialog extends PatternSimOpDialog {

    /**
     * Creates a new <code>PatternBandEdgesDialog</code>.
     */
    public PatternBandEdgesDialog() {
        super("Pattern Band Edges");
    }



    @Override
    public String toString() {
        return "Pattern Band Edges";
    }



    @Override
    public String getDescription() {
        return "Simulate a pattern where the edges of the bands are drawn.";
    }



    @Override
    public Operation getOperation() {
        return new PatternBandEdges(widthField.getValueBFR(),
                heightField.getValueBFR(), maxIndexfield.getValueBFR(),
                scatterTypeField.getSelectedItemBFR());
    }

}
