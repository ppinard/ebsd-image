package ptpshared.gui;

import javax.swing.JComponent;
import javax.swing.JLabel;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.math.geometry.Vector3D;

import rmlshared.gui.DoubleField;
import rmlshared.ui.InputBuffering;
import rmlshared.ui.InputValidation;
import rmlshared.ui.PreferenceKeeping;
import rmlshared.util.Preferences;
import rmlshared.util.Range;

/**
 * Field consisting of 3 <code>DoubleField</code> to enter the value of the x,y
 * and z components of a <code>Vector3D</code>.
 * 
 * @author ppinard
 */
public class Vector3DField extends JComponent implements PreferenceKeeping,
        InputValidation, InputBuffering {

    /** Preferences. */
    private Preferences preferences;

    /** Field for the x component of the vector. */
    private DoubleField xField;

    /** Field for the y component of the vector. */
    private DoubleField yField;

    /** Field for the z component of the vector. */
    private DoubleField zField;



    /**
     * Creates a new <code>Vector3DField</code>.
     * 
     * @param name
     *            name of the component
     * @param defaultValue
     *            default vector values
     */
    public Vector3DField(String name, Vector3D defaultValue) {
        setName(name);
        setLayout(new MigLayout());

        add(new JLabel("x"));
        xField = new DoubleField(name + "-x", defaultValue.getX());
        add(xField, "gapleft 2");

        add(new JLabel("y"));
        yField = new DoubleField(name + "-y", defaultValue.getY());
        add(yField, "gapleft 2");

        add(new JLabel("z"));
        zField = new DoubleField(name + "-z", defaultValue.getZ());
        add(zField, "gapleft 2");
    }



    /**
     * Wrapper over {@link DoubleField#setDecimalCount(double)}.
     * 
     * @param nbDecimals
     *            number of decimal for the 3 <code>DoubleField</code>
     */
    public void setDecimalCount(int nbDecimals) {
        xField.setDecimalCount(nbDecimals);
        yField.setDecimalCount(nbDecimals);
        zField.setDecimalCount(nbDecimals);
    }



    /**
     * Wrapper over {@link DoubleField#setIncrementalStep(double)}.
     * 
     * @param step
     *            step of the 3 spinners
     */
    public void setIncrementalStep(double step) {
        xField.setIncrementalStep(step);
        yField.setIncrementalStep(step);
        zField.setIncrementalStep(step);
    }



    /**
     * Returns the selected <code>Vector3D</code>.
     * 
     * @return selected <code>Vector3D</code>
     */
    public Vector3D getValue() {
        return new Vector3D(xField.getValue(), yField.getValue(),
                zField.getValue());
    }



    /**
     * Returns the buffered value and units.
     * 
     * @return selected value and units
     */
    public Vector3D getValueBFR() {
        return new Vector3D(xField.getValueBFR(), yField.getValueBFR(),
                zField.getValueBFR());
    }



    /**
     * Returns the thread-safe value and units.
     * 
     * @return selected value and units
     */
    public Vector3D getValueTS() {
        return new Vector3D(xField.getValueTS(), yField.getValueTS(),
                zField.getValueTS());
    }



    /**
     * Sets the values from a <code>Vector3D</code>.
     * 
     * @param v
     *            a vector
     */
    public void setValue(Vector3D v) {
        xField.setValue(v.getX());
        yField.setValue(v.getY());
        zField.setValue(v.getZ());
    }



    @Override
    public void bufferInput() {
        xField.bufferInput();
        yField.bufferInput();
        zField.bufferInput();
    }



    /**
     * Wrapper over {@link DoubleField#getRange()}.
     * 
     * @return range of the <code>DoubleField</code>
     */
    public Range<Double> getRange() {
        return xField.getRange();
    }



    /**
     * Wrapper over {@link DoubleField#setRange(double, double)}.
     * 
     * @param min
     *            minimum value of the range
     * @param max
     *            maximum value of the range
     */
    public void setRange(double min, double max) {
        xField.setRange(min, max);
        yField.setRange(min, max);
        zField.setRange(min, max);
    }



    @Override
    public boolean isCorrect() {
        return isCorrect(true);
    }



    @Override
    public boolean isCorrect(boolean showErrorDialog) {
        if (!xField.isCorrect(showErrorDialog))
            return false;

        if (!yField.isCorrect(showErrorDialog))
            return false;

        if (!zField.isCorrect(showErrorDialog))
            return false;

        return true;
    }



    @Override
    public Preferences getPreferences() {
        return preferences;
    }



    @Override
    public void setPreferences(Preferences pref) {
        xField.setPreferences(pref);
        yField.setPreferences(pref);
        zField.setPreferences(pref);
    }



    @Override
    public void updatePreferences() {
        xField.updatePreferences();
        yField.updatePreferences();
        zField.updatePreferences();
    }
}
