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

import java.awt.Dimension;

import javax.swing.*;
import javax.swing.ImageIcon;
import javax.swing.border.TitledBorder;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.math.geometry.Rotation;
import org.ebsdimage.core.run.Operation;
import org.ebsdimage.core.sim.Sim;
import org.ebsdimage.core.sim.SimConstants;
import org.ebsdimage.core.sim.ops.patternsim.PatternSimOp;
import org.ebsdimage.gui.run.ops.MultipleChoicePanel;
import org.ebsdimage.gui.run.ops.OperationCreator;
import org.ebsdimage.gui.run.ops.OperationCreatorUtil;

import ptpshared.geom.RotationUtils;
import ptpshared.gui.GuiUtil;
import ptpshared.gui.RotationField;
import ptpshared.gui.WizardPage;
import rmlimage.gui.PlugIn;
import rmlshared.gui.*;
import rmlshared.ui.InputValidation;
import crystallography.core.ScatteringFactorsEnum;

/**
 * Template for the parameters page of the wizard.
 * 
 * @author Philippe T. Pinard
 */
public class ParamsWizardPage extends WizardPage {

    /**
     * Panel for the pattern simulation operation.
     * 
     * @author Philippe T. Pinard
     */
    private static class PatternSimOpPanel extends JPanel implements
            InputValidation {

        /**
         * Action to select an pattern simulation operation.
         * 
         * @author Philippe T. Pinard
         */
        private class Select extends PlugIn {

            @Override
            protected void xRun() throws Exception {
                OperationCreator op = opCBox.getSelectedItem();
                if (op == null)
                    return;

                if (op.show() != OperationCreator.OK)
                    return;
                // TODO: Validate width and height with camera

                setPatternSimOp((PatternSimOp) op.getOperation());
            }

        }

        /** Combo box to select the pattern simulation operation. */
        private ComboBox<OperationCreator> opCBox;

        /** Pattern simulation operation selected. */
        private PatternSimOp selection = null;

        /** Label to indicate which pattern simulation operation is selected. */
        private JLabel selectionLabel;

        /** Combo box to select the scattering factors. */
        private ComboBox<ScatteringFactorsEnum> scatteringFactorsCBox;

        /** Field to select the maximum index of the reflectors. */
        private IntField maxIndexField;



        /**
         * Creates a new <code>PatternSimPanel</code>.
         */
        public PatternSimOpPanel() {
            setBorder(new TitledBorder("Simulator"));

            setLayout(new MigLayout());

            opCBox = new ComboBox<OperationCreator>();
            OperationCreator[] ops =
                    OperationCreatorUtil.getOperationCreators(SimConstants.PATTERNSIM_GUI_PACKAGE);
            opCBox.addGenerics(ops);
            add(opCBox, "growx");

            Button selectButton = new Button("Select");
            selectButton.setPlugIn(new Select());
            add(selectButton, "wrap");

            add(new JLabel("Selection"), "wrap");
            selectionLabel = new JLabel("None");
            add(selectionLabel, "gapleft 30, span 2, wrap");

            add(new JLabel("Scattering factors"));
            scatteringFactorsCBox =
                    new ComboBox<ScatteringFactorsEnum>(
                            ScatteringFactorsEnum.values());
            add(scatteringFactorsCBox, "wrap");

            add(new JLabel("Maximum index of the planes to simulate"));
            maxIndexField = new IntField("maxIndex", 4);
            maxIndexField.setRange(1, Integer.MAX_VALUE);
            add(maxIndexField, "wrap");
        }



        /**
         * Returns the selected maximum index of the planes to simulate.
         * 
         * @return maximum index
         */
        public int getMaxIndex() {
            return maxIndexField.getValue();
        }



        /**
         * Returns the selected pattern simulation operation.
         * 
         * @return pattern simulation operation
         */
        public PatternSimOp getPatternSimOp() {
            return selection;
        }



        /**
         * Returns the selected scattering factors.
         * 
         * @return scattering factors
         */
        public ScatteringFactorsEnum getScatteringFactors() {
            return scatteringFactorsCBox.getSelectedItem();
        }



        @Override
        public boolean isCorrect() {
            return isCorrect(true);
        }



        @Override
        public boolean isCorrect(boolean showErrorDialog) {
            if (selection == null) {
                if (showErrorDialog)
                    ErrorDialog.show("Please select a pattern simulation operation.");
                return false;
            }

            if (!maxIndexField.isCorrect(showErrorDialog))
                return false;

            return true;
        }



        /**
         * Sets the pattern simulation operation.
         * 
         * @param op
         *            pattern simulation operation
         */
        public void setPatternSimOp(PatternSimOp op) {
            selection = op;
            selectionLabel.setText(selection.toString());
        }

    }

    /**
     * Dialog to create random rotations.
     * 
     * @author Philippe T. Pinard
     */
    private static class RandomRotationsDialog extends OkCancelDialog {
        /** Field for the number of random rotations to create. */
        private IntField countField;



        /**
         * Creates a new <code>NewRandomRotationsDialog</code>.
         */
        public RandomRotationsDialog() {
            super(null, "Random Rotations");

            countField = new IntField("Number of rotations", 1);
            countField.setRange(1, Integer.MAX_VALUE);

            Panel panel = new ColumnPanel(2);

            panel.add(new JLabel("Number of random rotations"));
            panel.add(countField);

            setMainComponent(panel);
        }



        /**
         * Returns an array of random rotations.
         * 
         * @return random rotations
         */
        public Rotation[] getRotations() {
            Rotation[] rotations = new Rotation[countField.getValue()];

            long seed;
            for (int i = 0; i < rotations.length; i++) {
                seed = System.currentTimeMillis() + i;
                rotations[i] = RotationUtils.randomRotation(seed);
            }

            return rotations;
        }
    }

    /**
     * Dialog to create a new rotation.
     * 
     * @author Philippe T. Pinard
     */
    private static class RotationDialog extends BasicDialog {

        @Override
        public boolean isCorrect() {
            if (!super.isCorrect())
                return false;

            if (!rotationField.isCorrect())
                return false;

            return true;
        }

        /** Euler angles field. */
        private RotationField rotationField;



        /**
         * Creates a new <code>NewRotationDialog</code>.
         */
        public RotationDialog() {
            super(null, "New Rotation");

            Panel panel = new Panel(new MigLayout());

            panel.add(new JLabel("Bunge convention"), "span 3, wrap");

            Rotation defaultValue = new Rotation(1, 0, 0, 0, false);
            rotationField = new RotationField("rotation", defaultValue);
            panel.add(rotationField, "wrap");

            setMainComponent(panel);
        }



        /**
         * Returns the selected rotation.
         * 
         * @return rotation expressed as a <code>Quaternion</code>.
         */
        public Rotation getRotation() {
            return rotationField.getValue();
        }
    }

    /**
     * Panel for the rotation parameters.
     * 
     * @author Philippe T. Pinard
     */
    private class RotationPanel extends JPanel implements InputValidation {

        /**
         * Temporary rotation object to wrap around the Apache common match
         * <code>Rotation</code> object and implements a different
         * {@link #toString()}. This is required for the rotation to be showed
         * properly in a <code>JList</code>.
         * 
         * @author ppinard
         */
        private class TmpRotation {

            /** A rotation. */
            private final Rotation rotation;



            /**
             * Creates a new <code>TmpRotation</code>.
             * 
             * @param rotation
             *            a rotation
             */
            public TmpRotation(Rotation rotation) {
                this.rotation = rotation;
            }



            /**
             * Returns the <code>Rotation</code> object.
             * 
             * @return <code>Rotation</code> object
             */
            public Rotation getValue() {
                return rotation;
            }



            @Override
            public String toString() {
                return RotationUtils.toString(rotation);
            }
        }

        /**
         * Action to add a rotation.
         * 
         * @author Philippe T. Pinard
         */
        private class Add extends PlugIn {

            @Override
            protected void xRun() throws Exception {
                RotationDialog dialog = new RotationDialog();

                if (dialog.show() == OkCancelDialog.CANCEL)
                    return;

                Rotation rotation = dialog.getRotation();

                DefaultListModel model = (DefaultListModel) list.getModel();
                model.addElement(new TmpRotation(rotation));
            }

        }

        /**
         * Action to add a random rotation.
         * 
         * @author Philippe T. Pinard
         */
        private class AddRandom extends PlugIn {

            @Override
            protected void xRun() throws Exception {
                RandomRotationsDialog dialog = new RandomRotationsDialog();

                if (dialog.show() == OkCancelDialog.CANCEL)
                    return;

                Rotation[] rotations = dialog.getRotations();

                DefaultListModel model = (DefaultListModel) list.getModel();

                for (Rotation rotation : rotations)
                    model.addElement(new TmpRotation(rotation));
            }

        }

        /**
         * Removes all rotations from the list.
         * 
         * @author Philippe T. Pinard
         */
        private class Clear extends PlugIn {
            @Override
            protected void xRun() {
                DefaultListModel model = (DefaultListModel) list.getModel();

                for (Object obj : model.toArray())
                    model.removeElement(obj);
            }
        }

        /**
         * Removes a rotation from list.
         * 
         * @author Philippe T. Pinard
         */
        private class Remove extends PlugIn {
            @Override
            public void xRun() {
                // Get the selected operation
                Object obj = list.getSelectedValue();
                if (obj == null)
                    return;

                // Remove it from the user list
                DefaultListModel model = (DefaultListModel) list.getModel();
                model.removeElement(obj);
            }
        }

        /** List of the parameters. */
        private JList list;



        /**
         * Creates a new <code>ParamsPanel</code>.
         */
        public RotationPanel() {
            setBorder(new TitledBorder("Rotation"));

            setLayout(new MigLayout());

            // List
            list = new JList(new DefaultListModel());
            JScrollPane listScroller = new JScrollPane(list);
            listScroller.setPreferredSize(new Dimension(150, 100));
            add(listScroller, "grow, push, wrap");

            // Add button
            Button addButton = new Button(ADD_ICON);
            addButton.setToolTipText("Add a new rotation");
            addButton.setPlugIn(new Add());
            add(addButton, "split 4, align right");

            // Add random button
            Button addRandomButton = new Button(ADD_ICON);
            addRandomButton.setToolTipText("Add a new random rotation");
            addRandomButton.setPlugIn(new AddRandom());
            add(addRandomButton);

            // Remove button
            Button removeButton = new Button(REMOVE_ICON);
            removeButton.setToolTipText("Remove an item");
            removeButton.setPlugIn(new Remove());
            add(removeButton);

            // Clear button
            Button clearButton = new Button(CLEAR_ICON);
            clearButton.setToolTipText("Clear all items");
            clearButton.setPlugIn(new Clear());
            add(clearButton);
        }



        /**
         * Sets the rotations inside the list.
         * 
         * @param rotations
         *            rotations
         */
        public void setRotations(Rotation[] rotations) {
            DefaultListModel model = (DefaultListModel) list.getModel();
            model.clear();
            for (Rotation rotation : rotations)
                model.addElement(new TmpRotation(rotation));
        }



        /**
         * Returns an array of the rotations in the list.
         * 
         * @return array of rotations
         */
        public Rotation[] getRotations() {
            DefaultListModel model = (DefaultListModel) list.getModel();

            Rotation[] rotations = new Rotation[model.size()];

            int i = 0;
            for (Object obj : model.toArray()) {
                rotations[i] = ((TmpRotation) obj).getValue();
                i++;
            }

            return rotations;
        }



        @Override
        public boolean isCorrect() {
            return isCorrect(true);
        }



        @Override
        public boolean isCorrect(boolean showErrorDialog) {
            if (((DefaultListModel) list.getModel()).size() < 1) {
                if (showErrorDialog)
                    ErrorDialog.show("Please specify a rotation");
                return false;
            }

            return true;
        }

    }

    /** Icon for the add button. */
    private static final ImageIcon ADD_ICON =
            GuiUtil.loadIcon("ptpshared/data/icon/list-add_(22x22).png");

    /** Icon for the clear button. */
    private static final ImageIcon CLEAR_ICON =
            GuiUtil.loadIcon("ptpshared/data/icon/list-clear_(22x22).png");

    /**
     * Map key to store if the data has been previously loaded. It prevents
     * loading the temporary metadata twice.
     */
    public static final String KEY_LOADED = "params.loaded";

    /** Map key for the pattern simulation operation. */
    public static final String KEY_PATTERNSIMOP = "params.patternsimop";

    /** Map key for the scattering factors. */
    public static final String KEY_SCATTERINGFACTORS =
            "params.scatteringfactors";

    /** Map key for the maximum index. */
    public static final String KEY_MAXINDEX = "params.maxindex";

    /** Map key for the rotations. */
    public static final String KEY_ROTATIONS = "params.rotations";

    /** Map key for the output operations. */
    public static final String KEY_OUTPUT_OPS = "params.outputs";

    /** Icon for the remove button. */
    private static final ImageIcon REMOVE_ICON =
            GuiUtil.loadIcon("ptpshared/data/icon/list-remove_(22x22).png");



    /**
     * Returns a description of this step.
     * 
     * @return description
     */
    public static String getDescription() {
        return "Parameters";
    }

    /** Panel for the pattern simulation operation. */
    private PatternSimOpPanel patternSimOpPanel;

    /** Panel for the rotations. */
    private RotationPanel rotationPanel;

    /** Panel for the outputs. */
    private MultipleChoicePanel outputsPanel;



    /**
     * Creates a new <code>ParamsWizardPage</code>.
     */
    public ParamsWizardPage() {
        setLayout(new MigLayout("", "[]10[]"));

        rotationPanel = new RotationPanel();
        add(rotationPanel, "grow, push, wrap");

        patternSimOpPanel = new PatternSimOpPanel();
        add(patternSimOpPanel, "grow, push, wrap");

        outputsPanel =
                new MultipleChoicePanel("Outputs",
                        SimConstants.OUTPUT_GUI_PACKAGE);
        add(outputsPanel, "span 2, grow, push, wrap");
    }



    @Override
    public boolean isCorrect(boolean buffer) {
        Operation[] outputs = outputsPanel.getOperations();

        if (!rotationPanel.isCorrect())
            return false;

        if (!patternSimOpPanel.isCorrect())
            return false;

        if (outputs.length < 1) {
            showErrorDialog("Please specify an output");
            return false;
        }

        if (buffer) {
            put(KEY_ROTATIONS, rotationPanel.getRotations());
            put(KEY_PATTERNSIMOP, patternSimOpPanel.getPatternSimOp());
            put(KEY_OUTPUT_OPS, outputs);
            put(KEY_SCATTERINGFACTORS, patternSimOpPanel.getScatteringFactors());
            put(KEY_MAXINDEX, patternSimOpPanel.getMaxIndex());
        }

        return true;
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

        // Rotations
        rotationPanel.setRotations(sim.getRotations());

        // Pattern simulation op
        PatternSimOp op = sim.getPatternSimOp();
        patternSimOpPanel.setPatternSimOp(op);

        put(KEY_LOADED, 1);
    }
}
