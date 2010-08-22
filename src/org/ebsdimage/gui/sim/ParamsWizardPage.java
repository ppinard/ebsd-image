package org.ebsdimage.gui.sim;

import java.awt.Dimension;
import java.io.IOException;

import javax.swing.*;
import javax.swing.ImageIcon;
import javax.swing.border.TitledBorder;

import net.miginfocom.swing.MigLayout;

import org.ebsdimage.core.Camera;
import org.ebsdimage.core.run.Operation;
import org.ebsdimage.core.sim.Energy;
import org.ebsdimage.core.sim.Sim;
import org.ebsdimage.core.sim.SimConstants;
import org.ebsdimage.core.sim.ops.patternsim.PatternSimOp;
import org.ebsdimage.gui.run.ops.MultipleChoicePanel;
import org.ebsdimage.gui.run.ops.OperationCreator;
import org.ebsdimage.gui.run.ops.OperationCreatorUtil;

import ptpshared.core.math.Eulers;
import ptpshared.core.math.Quaternion;
import ptpshared.core.math.QuaternionMath;
import ptpshared.gui.GuiUtil;
import ptpshared.gui.WizardPage;
import rmlimage.gui.PlugIn;
import rmlshared.gui.*;

/**
 * Template for the parameters page of the wizard.
 * 
 * @author Philippe T. Pinard
 */
public class ParamsWizardPage extends WizardPage {

    /**
     * Dialog to create a new camera.
     * 
     * @author ppinard
     */
    private class CameraDialog extends OkCancelDialog {

        /** Field for the detector distance. */
        private DoubleField ddField;

        /** Field for the pattern centre horizontal. */
        private DoubleField pcHField;

        /** Field for the pattern centre vertical. */
        private DoubleField pcVField;



        /**
         * Creates a new <code>NewCameraDialog</code>.
         */
        public CameraDialog() {
            super(null, "New Camera");

            pcHField = new DoubleField("Pattern centre horizontal", 0.0);
            pcHField.setRange(-0.5, 0.5);

            pcVField = new DoubleField("Pattern centre vertical", 0.0);
            pcVField.setRange(-0.5, 0.5);

            ddField = new DoubleField("Detector distance", 0.3);
            ddField.setRange(1e-6, Double.MAX_VALUE);

            Panel panel = new ColumnPanel(2);

            panel.add(new JLabel("Pattern centre horizontal"));
            panel.add(pcHField);

            panel.add(new JLabel("Pattern centre vertical"));
            panel.add(pcVField);

            panel.add(new JLabel("Detector distance"));
            panel.add(ddField);

            setMainComponent(panel);
        }



        /**
         * Returns a new camera with the specified parameters.
         * 
         * @return a camera
         */
        public Camera getCamera() {
            return new Camera(pcHField.getValue(), pcVField.getValue(),
                    ddField.getValue());
        }

    }

    /**
     * Panel for the camera parameters.
     * 
     * @author ppinard
     */
    private class CameraPanel extends ParamsPanel {

        /**
         * Action to add a camera.
         * 
         * @author Philippe T. Pinard
         */
        private class Add extends PlugIn {

            @Override
            protected void xRun() throws Exception {
                CameraDialog dialog = new CameraDialog();

                if (dialog.show() == OkCancelDialog.CANCEL)
                    return;

                Camera camera = dialog.getCamera();

                DefaultListModel model = (DefaultListModel) list.getModel();
                model.addElement(camera);
            }

        }



        /**
         * Creates a new <code>CameraPanel</code>.
         */
        public CameraPanel() {
            super("Camera");
        }



        @Override
        protected Button getAddButton() {
            Button button = new Button(ADD_ICON);
            button.setToolTipText("Add a new camera");
            button.setPlugIn(new Add());
            return button;
        }



        /**
         * Returns an array of the cameras in the list.
         * 
         * @return array of cameras
         */
        public Camera[] getCameras() {
            DefaultListModel model = (DefaultListModel) list.getModel();

            Camera[] cameras = new Camera[model.size()];

            int i = 0;
            for (Object obj : model.toArray()) {
                cameras[i] = (Camera) obj;
                i++;
            }

            return cameras;
        }
    }

    /**
     * Dialog to create a new energy.
     * 
     * @author ppinard
     */
    private class EnergyDialog extends OkCancelDialog {

        /** Field for the value of the energy. */
        private DoubleField energyField;



        /**
         * Creates a new <code>NewEnergyDialog</code>.
         */
        public EnergyDialog() {
            super(null, "New Energy");

            energyField = new DoubleField("Energy value", 10);
            energyField.setRange(0.1, 1000);

            Panel panel = new ColumnPanel(3);

            panel.add(new JLabel("Energy"));
            panel.add(energyField);
            panel.add(new JLabel("keV"));

            setMainComponent(panel);
        }



        /**
         * Returns the selected energy.
         * 
         * @return energy
         */
        public Energy getEnergy() {
            return new Energy(energyField.getValue() * 1000);
        }
    }

    /**
     * Panel for the energy parameters.
     * 
     * @author ppinard
     */
    private class EnergyPanel extends ParamsPanel {

        /**
         * Action to add a energy.
         * 
         * @author Philippe T. Pinard
         */
        private class Add extends PlugIn {

            @Override
            protected void xRun() throws Exception {
                EnergyDialog dialog = new EnergyDialog();

                if (dialog.show() == OkCancelDialog.CANCEL)
                    return;

                Energy energy = dialog.getEnergy();

                DefaultListModel model = (DefaultListModel) list.getModel();
                model.addElement(energy);
            }

        }



        /**
         * Creates a new <code>EnergyPanel</code>.
         */
        public EnergyPanel() {
            super("Energy");
        }



        @Override
        protected Button getAddButton() {
            Button button = new Button(ADD_ICON);
            button.setToolTipText("Add a new energy");
            button.setPlugIn(new Add());
            return button;
        }



        /**
         * Returns an array of the energies in the list.
         * 
         * @return array of energies
         */
        public Energy[] getEnergies() {
            DefaultListModel model = (DefaultListModel) list.getModel();

            Energy[] energies = new Energy[model.size()];

            int i = 0;
            for (Object obj : model.toArray()) {
                energies[i] = (Energy) obj;
                i++;
            }

            return energies;
        }
    }

    /**
     * Panel for the parameters.
     * 
     * @author ppinard
     */
    private abstract class ParamsPanel extends JPanel {

        /**
         * Removes all parameters from the list.
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
         * Removes a parameter from list.
         * 
         * @author ppinard
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
        protected JList list;



        /**
         * Creates a new <code>ParamsPanel</code>.
         * 
         * @param title
         *            title/name of the parameter group
         */
        public ParamsPanel(String title) {
            setBorder(new TitledBorder(title));

            setLayout(new MigLayout());

            // List
            list = new JList(new DefaultListModel());
            JScrollPane listScroller = new JScrollPane(list);
            listScroller.setPreferredSize(new Dimension(150, 100));
            add(listScroller, "grow, push, wrap");

            // Buttons
            Button[] buttons = getButtons();
            if (buttons.length > 0) {
                add(buttons[0], "split " + buttons.length + ", align right");

                for (int i = 1; i < buttons.length; i++)
                    add(buttons[i]);
            }
        }



        /**
         * Returns the add button.
         * 
         * @return add button
         */
        protected abstract Button getAddButton();



        /**
         * Returns an array of all the buttons to add below the list.
         * 
         * @return buttons
         */
        protected Button[] getButtons() {
            return new Button[] { getAddButton(), getRemoveButton(),
                    getClearButton() };
        }



        /**
         * Returns the clear button.
         * 
         * @return clear button
         */
        protected Button getClearButton() {
            Button button = new Button(CLEAR_ICON);
            button.setToolTipText("Clear all items");
            button.setPlugIn(new Clear());
            return button;
        }



        /**
         * Returns the remove button.
         * 
         * @return remove button
         */
        protected Button getRemoveButton() {
            Button button = new Button(REMOVE_ICON);
            button.setToolTipText("Remove an item");
            button.setPlugIn(new Remove());
            return button;
        }
    }

    /**
     * Panel for the pattern simulation operation.
     * 
     * @author ppinard
     */
    private class PatternSimOpPanel extends JPanel {

        /**
         * Action to select an pattern simulation operation.
         * 
         * @author ppinard
         */
        private class Select extends PlugIn {

            @Override
            protected void xRun() throws Exception {
                OperationCreator op = opCBox.getSelectedItem();
                if (op == null)
                    return;

                if (op.show() == OperationCreator.CANCEL)
                    return;

                setPatternSimOp((PatternSimOp) op.getOperation());
            }

        }

        /** Combo box to select the pattern simulation operation. */
        private ComboBox<OperationCreator> opCBox;

        /** Pattern simulation operation selected. */
        private PatternSimOp selection = null;

        /** Label to indicate which pattern simulation operation is selected. */
        private JLabel selectionLabel;



        /**
         * Creates a new <code>PatternSimPanel</code>.
         * 
         * @throws IOException
         *             if an error occurs while searching for the pattern
         *             simulation operations
         */
        public PatternSimOpPanel() throws IOException {
            setBorder(new TitledBorder("Simulator"));

            setLayout(new MigLayout());

            opCBox = new ComboBox<OperationCreator>();
            OperationCreator[] ops =
                    OperationCreatorUtil.getOperationCreators(SimConstants.PATTERNSIM_GUI_PACKAGE);
            opCBox.addGenerics(ops);
            add(opCBox);

            Button selectButton = new Button("Select");
            selectButton.setPlugIn(new Select());
            add(selectButton, "wrap");

            add(new JLabel("Selection"), "wrap");
            selectionLabel = new JLabel("None");
            add(selectionLabel, "gapleft 30, span 2, wrap");
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
         * Sets the pattern simulation operation.
         * 
         * @param op
         *            pattern simulation operation
         */
        protected void setPatternSimOp(PatternSimOp op) {
            selection = op;
            selectionLabel.setText(selection.toString());
        }

    }

    /**
     * Dialog to create random rotations.
     * 
     * @author ppinard
     */
    private class RandomRotationsDialog extends OkCancelDialog {
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
        public Quaternion[] getRotations() {
            Quaternion[] rotations = new Quaternion[countField.getValue()];

            long seed;
            for (int i = 0; i < rotations.length; i++) {
                seed = System.currentTimeMillis() + i;
                rotations[i] = QuaternionMath.randomRotation(seed);
            }

            return rotations;
        }
    }

    /**
     * Dialog to create a new rotation.
     * 
     * @author ppinard
     */
    private class RotationDialog extends OkCancelDialog {

        /** Field for the first Euler angle. */
        private DoubleField theta1Field;

        /** Field for the second Euler angle. */
        private DoubleField theta2Field;

        /** Field for the third Euler angle. */
        private DoubleField theta3Field;



        /**
         * Creates a new <code>NewRotationDialog</code>.
         */
        public RotationDialog() {
            super(null, "New Rotation");

            theta1Field = new DoubleField("Theta 1", 0.0);
            theta1Field.setRange(-180, 180);

            theta2Field = new DoubleField("Theta 2", 0.0);
            theta2Field.setRange(0, 180);

            theta3Field = new DoubleField("Theta 3", 0.0);
            theta3Field.setRange(-180, 180);

            Panel panel = new Panel(new MigLayout());

            panel.add(new JLabel("Bunge convention"), "span 3, wrap");

            panel.add(new JLabel("\u03f4 1"));
            panel.add(theta1Field);
            panel.add(new JLabel("\u00b0"), "wrap"); // deg

            panel.add(new JLabel("\u03f4 2"));
            panel.add(theta2Field);
            panel.add(new JLabel("\u00b0"), "wrap"); // deg

            panel.add(new JLabel("\u03f4 3"));
            panel.add(theta3Field);
            panel.add(new JLabel("\u00b0"), "wrap"); // deg

            setMainComponent(panel);
        }



        /**
         * Returns the selected rotation.
         * 
         * @return rotation expressed as a <code>Quaternion</code>.
         */
        public Quaternion getRotation() {
            Eulers eulers =
                    new Eulers(Math.toRadians(theta1Field.getValue()),
                            Math.toRadians(theta2Field.getValue()),
                            Math.toRadians(theta3Field.getValue()));

            return new Quaternion(eulers);
        }
    }

    /**
     * Panel for the rotation parameters.
     * 
     * @author ppinard
     */
    private class RotationPanel extends ParamsPanel {

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

                Quaternion rotation = dialog.getRotation();

                DefaultListModel model = (DefaultListModel) list.getModel();
                model.addElement(rotation);
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

                Quaternion[] rotations = dialog.getRotations();

                DefaultListModel model = (DefaultListModel) list.getModel();

                for (Quaternion rotation : rotations)
                    model.addElement(rotation);
            }

        }



        /**
         * Creates a new <code>RotationPanel</code>.
         */
        public RotationPanel() {
            super("Rotation");
        }



        @Override
        protected Button getAddButton() {
            Button button = new Button(ADD_ICON);
            button.setToolTipText("Add a new rotation");
            button.setPlugIn(new Add());
            return button;
        }



        /**
         * Button to add a series of random rotations.
         * 
         * @return add random button
         */
        protected Button getAddRandomButton() {
            Button button = new Button(ADD_ICON);
            button.setToolTipText("Add a new random rotation");
            button.setPlugIn(new AddRandom());
            return button;
        }



        @Override
        protected Button[] getButtons() {
            return new Button[] { getAddButton(), getAddRandomButton(),
                    getRemoveButton(), getClearButton() };
        }



        /**
         * Returns an array of the rotations in the list.
         * 
         * @return array of rotations
         */
        public Quaternion[] getRotations() {
            DefaultListModel model = (DefaultListModel) list.getModel();

            Quaternion[] rotations = new Quaternion[model.size()];

            int i = 0;
            for (Object obj : model.toArray()) {
                rotations[i] = (Quaternion) obj;
                i++;
            }

            return rotations;
        }
    }

    /** Icon for the add button. */
    protected static final ImageIcon ADD_ICON =
            GuiUtil.loadIcon("ptpshared/data/icon/list-add_(22x22).png");

    /** Icon for the clear button. */
    protected static final ImageIcon CLEAR_ICON =
            GuiUtil.loadIcon("ptpshared/data/icon/list-clear_(22x22).png");

    /** Map key for the cameras. */
    public static final String KEY_CAMERAS = "params.cameras";

    /** Map key for the energies. */
    public static final String KEY_ENERGIES = "params.energies";

    /**
     * Map key to store if the data has been previously loaded. It prevents
     * loading the temporary metadata twice.
     */
    public static final String KEY_LOADED = "params.loaded";

    /** Map key for the pattern simulation operation. */
    public static final String KEY_PATTERNSIMOP = "params.patternsimop";

    /** Map key for the rotations. */
    public static final String KEY_ROTATIONS = "params.rotations";

    /** Map key for the output operations. */
    public static final String KEY_OUTPUT_OPS = "params.outputs";

    /** Icon for the remove button. */
    protected static final ImageIcon REMOVE_ICON =
            GuiUtil.loadIcon("ptpshared/data/icon/list-remove_(22x22).png");



    /**
     * Returns a description of this step.
     * 
     * @return description
     */
    public static String getDescription() {
        return "Parameters";
    }

    /** Panel for the cameras. */
    private CameraPanel cameraPanel;

    /** Panel for the energies. */
    private EnergyPanel energyPanel;

    /** Panel for the pattern simulation operation. */
    private PatternSimOpPanel patternSimOpPanel;

    /** Panel for the rotations. */
    private RotationPanel rotationPanel;

    /** Panel for the outputs. */
    private MultipleChoicePanel outputsPanel;



    /**
     * Creates a new <code>ParamsWizardPage</code>.
     * 
     * @throws IOException
     *             if an error occurs while listing the pattern simulation
     *             operations
     */
    public ParamsWizardPage() throws IOException {

        setLayout(new MigLayout("", "[]10[]"));

        cameraPanel = new CameraPanel();
        add(cameraPanel, "grow, push");

        rotationPanel = new RotationPanel();
        add(rotationPanel, "grow, push, wrap");

        energyPanel = new EnergyPanel();
        add(energyPanel, "grow, push");

        patternSimOpPanel = new PatternSimOpPanel();
        add(patternSimOpPanel, "grow, push, wrap");

        outputsPanel =
                new MultipleChoicePanel("Outputs",
                        SimConstants.OUTPUT_GUI_PACKAGE);
        add(outputsPanel, "span 2, grow, push, wrap");
    }



    @Override
    public boolean isCorrect(boolean buffer) {
        Camera[] cameras = cameraPanel.getCameras();
        Energy[] energies = energyPanel.getEnergies();
        Quaternion[] rotations = rotationPanel.getRotations();
        PatternSimOp patternSimOp = patternSimOpPanel.getPatternSimOp();
        Operation[] outputs = outputsPanel.getOperations();

        if (cameras.length < 1) {
            showErrorDialog("Please specify a camera");
            return false;
        }

        if (energies.length < 1) {
            showErrorDialog("Please specify an energy");
            return false;
        }

        if (rotations.length < 1) {
            showErrorDialog("Please specify a rotation");
            return false;
        }

        if (patternSimOp == null) {
            showErrorDialog("Please select a pattern simulator");
            return false;
        }

        if (outputs.length < 1) {
            showErrorDialog("Please specify an output");
            return false;
        }

        if (buffer) {
            put(KEY_CAMERAS, cameras);
            put(KEY_ENERGIES, energies);
            put(KEY_ROTATIONS, rotations);
            put(KEY_PATTERNSIMOP, patternSimOp);
            put(KEY_OUTPUT_OPS, outputs);
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

        // Camera
        DefaultListModel model = (DefaultListModel) cameraPanel.list.getModel();
        model.clear();
        for (Object op : sim.getCameras())
            model.addElement(op);

        // Energy
        model = (DefaultListModel) energyPanel.list.getModel();
        model.clear();
        for (Object op : sim.getEnergies())
            model.addElement(op);

        // Rotation
        model = (DefaultListModel) rotationPanel.list.getModel();
        model.clear();
        for (Object op : sim.getRotations())
            model.addElement(op);

        // Pattern simulation op
        PatternSimOp op = sim.getPatternSimOp();
        patternSimOpPanel.setPatternSimOp(op);

        put(KEY_LOADED, 1);
    }
}
