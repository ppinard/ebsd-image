package ptpshared.gui;

import javax.swing.JComponent;
import javax.swing.JLabel;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.math.geometry.CardanEulerSingularityException;
import org.apache.commons.math.geometry.Rotation;
import org.apache.commons.math.geometry.RotationOrder;

import ptpshared.geom.RotationUtils;
import rmlshared.gui.DoubleField;
import rmlshared.ui.InputBuffering;
import rmlshared.ui.InputValidation;
import rmlshared.ui.PreferenceKeeping;
import rmlshared.util.Preferences;

/**
 * Field consisting of 3 <code>DoubleField</code> to enter a rotation using
 * Euler angles.
 * 
 * @author ppinard
 */
public class RotationField extends JComponent implements PreferenceKeeping,
        InputValidation, InputBuffering {

    /** Rotation order for the Euler angles. */
    private static final RotationOrder ROTATION_ORDER = RotationOrder.ZXZ;

    /** Preferences. */
    private Preferences preferences;

    /** Field for the x component of the vector. */
    private DoubleField theta1Field;

    /** Field for the y component of the vector. */
    private DoubleField theta2Field;

    /** Field for the z component of the vector. */
    private DoubleField theta3Field;



    /**
     * Creates a new <code>RotationField</code>.
     * 
     * @param name
     *            name of the component
     * @param defaultValue
     *            default vector values
     */
    public RotationField(String name, Rotation defaultValue) {
        setName(name);
        setLayout(new MigLayout());

        double[] eulers;
        try {
            eulers = RotationUtils.getBungeEulerAngles(defaultValue);
        } catch (CardanEulerSingularityException e) {
            eulers = new double[] { 0.0, 0.0, 0.0 };
        }

        add(new JLabel("\u03b1"));
        theta1Field =
                new DoubleField(name + "-theta1", Math.toDegrees(eulers[0]));
        theta1Field.setRange(0, 360);
        add(theta1Field, "gapleft 2");
        add(new JLabel("\u00b0"));

        add(new JLabel("\u03b2"));
        theta2Field =
                new DoubleField(name + "-theta2", Math.toDegrees(eulers[1]));
        theta2Field.setRange(0, 180);
        add(theta2Field);
        add(new JLabel("\u00b0"), "gapleft 2");

        add(new JLabel("\u03b3"));
        theta3Field =
                new DoubleField(name + "-theta3", Math.toDegrees(eulers[2]));
        theta3Field.setRange(0, 360);
        add(theta3Field);
        add(new JLabel("\u00b0"), "gapleft 2");
    }



    /**
     * Wrapper over {@link DoubleField#setDecimalCount(double)}.
     * 
     * @param nbDecimals
     *            number of decimal for the 3 <code>DoubleField</code>
     */
    public void setDecimalCount(int nbDecimals) {
        theta1Field.setDecimalCount(nbDecimals);
        theta2Field.setDecimalCount(nbDecimals);
        theta3Field.setDecimalCount(nbDecimals);
    }



    /**
     * Wrapper over {@link DoubleField#setIncrementalStep(double)}.
     * 
     * @param step
     *            step of the 3 spinners
     */
    public void setIncrementalStep(double step) {
        theta1Field.setIncrementalStep(step);
        theta2Field.setIncrementalStep(step);
        theta3Field.setIncrementalStep(step);
    }



    /**
     * Returns the selected <code>Rotation</code>.
     * 
     * @return selected <code>Rotation</code>
     */
    public Rotation getValue() {
        return new Rotation(ROTATION_ORDER,
                Math.toRadians(theta1Field.getValue()),
                Math.toRadians(theta2Field.getValue()),
                Math.toRadians(theta3Field.getValue()));
    }



    /**
     * Returns the buffered <code>Rotation</code>.
     * 
     * @return selected <code>Rotation</code>
     */
    public Rotation getValueBFR() {
        return new Rotation(ROTATION_ORDER,
                Math.toRadians(theta1Field.getValueBFR()),
                Math.toRadians(theta2Field.getValueBFR()),
                Math.toRadians(theta3Field.getValueBFR()));
    }



    /**
     * Returns the thread safe <code>Rotation</code>.
     * 
     * @return selected <code>Rotation</code>
     */
    public Rotation getValueTS() {
        return new Rotation(ROTATION_ORDER,
                Math.toRadians(theta1Field.getValueTS()),
                Math.toRadians(theta2Field.getValueTS()),
                Math.toRadians(theta3Field.getValueTS()));
    }



    /**
     * Sets the values from a <code>Rotation</code>. If a
     * {@link CardanEulerSingularityException} occurs, the values of the Euler
     * angles are default back to 0.0, 0.0, 0.0.
     * 
     * @param rotation
     *            a rotation
     */
    public void setValue(Rotation rotation) {
        double eulers[];
        try {
            eulers = RotationUtils.getBungeEulerAngles(rotation);
        } catch (CardanEulerSingularityException e) {
            eulers = new double[] { 0.0, 0.0, 0.0 };
        }

        theta1Field.setValue(Math.toDegrees(eulers[0]));
        theta2Field.setValue(Math.toDegrees(eulers[1]));
        theta3Field.setValue(Math.toDegrees(eulers[2]));
    }



    @Override
    public void bufferInput() {
        theta1Field.bufferInput();
        theta2Field.bufferInput();
        theta3Field.bufferInput();
    }



    @Override
    public boolean isCorrect() {
        return isCorrect(true);
    }



    @Override
    public boolean isCorrect(boolean showErrorDialog) {
        if (!theta1Field.isCorrect(showErrorDialog))
            return false;

        if (!theta2Field.isCorrect(showErrorDialog))
            return false;

        if (!theta3Field.isCorrect(showErrorDialog))
            return false;

        return true;
    }



    @Override
    public Preferences getPreferences() {
        return preferences;
    }



    @Override
    public void setPreferences(Preferences pref) {
        theta1Field.setPreferences(pref);
        theta2Field.setPreferences(pref);
        theta3Field.setPreferences(pref);
    }



    @Override
    public void updatePreferences() {
        theta1Field.updatePreferences();
        theta2Field.updatePreferences();
        theta3Field.updatePreferences();
    }

}
