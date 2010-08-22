package org.ebsdimage.gui.sim.ops.patternsim;

import net.miginfocom.swing.MigLayout;

import org.ebsdimage.core.sim.ops.patternsim.PatternSimOp;
import org.ebsdimage.gui.run.ops.OperationDialog;

import rmlshared.gui.ComboBox;
import rmlshared.gui.IntField;
import rmlshared.gui.Panel;
import crystallography.core.ScatteringFactorsEnum;

/**
 * Abstract class common to the pattern simulation operations.
 * 
 * @author ppinard
 */
public abstract class PatternSimOpDialog extends OperationDialog {

    /** Field for the width of the simulated pattern. */
    protected IntField widthField;

    /** Field for the height of the simulated pattern. */
    protected IntField heightField;

    /** Field for the maximum index of the reflectors. */
    protected IntField maxIndexfield;

    /** Field for the type of scattering factors. */
    protected ComboBox<ScatteringFactorsEnum> scatterTypeField;



    /**
     * Creates a new <code>PatternSimOpDialog</code>.
     * 
     * @param title
     *            title of the dialog
     */
    public PatternSimOpDialog(String title) {
        super(title);

        widthField = new IntField("Width", PatternSimOp.DEFAULT_WIDTH);
        widthField.setRange(1, Integer.MAX_VALUE);

        heightField = new IntField("Height", PatternSimOp.DEFAULT_HEIGHT);
        heightField.setRange(1, Integer.MAX_VALUE);

        maxIndexfield =
                new IntField("Maximum index", PatternSimOp.DEFAULT_MAX_INDEX);
        maxIndexfield.setRange(1, Integer.MAX_VALUE);

        scatterTypeField =
                new ComboBox<ScatteringFactorsEnum>(
                        ScatteringFactorsEnum.values());
        scatterTypeField.setSelectedItem(PatternSimOp.DEFAULT_SCATTER_TYPE);

        Panel panel = new Panel(new MigLayout());

        panel.add("Width of the pattern");
        panel.add(widthField);
        panel.add("px", "align left, wrap");

        panel.add("Height of the pattern");
        panel.add(heightField);
        panel.add("px", "align left, wrap");

        panel.add("Maximum index of the reflectors");
        panel.add(maxIndexfield, "wrap");

        panel.add("Type of scattering factors");
        panel.add(scatterTypeField, "wrap");

        setMainComponent(panel);
    }

}
