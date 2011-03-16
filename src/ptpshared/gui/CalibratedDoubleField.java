package ptpshared.gui;

import javax.swing.JComponent;

import magnitude.core.Magnitude;
import rmlshared.gui.ComboBox;
import rmlshared.gui.DoubleField;
import rmlshared.gui.FlowLayout;
import rmlshared.gui.Spacer;
import rmlshared.ui.InputBuffering;
import rmlshared.ui.InputValidation;
import rmlshared.ui.PreferenceKeeping;
import rmlshared.util.Arrays;
import rmlshared.util.Preferences;
import rmlshared.util.Range;

/**
 * A special <code>DoubleField</code> that allow the users to select calibrated
 * value (value + units). The field gives a selection of valid units that can be
 * used. It offers the same functionalities as the original
 * <code>DoubleField</code>.
 * 
 * @author ppinard
 */
public class CalibratedDoubleField extends JComponent implements
        PreferenceKeeping, InputValidation, InputBuffering {

    /** Preferences. */
    private Preferences preferences;

    /** Double field for the value. */
    private DoubleField valueField;

    /** Combo box for the units. */
    private ComboBox<String> unitsCBox;

    /** Units that the field should accept. */
    private String[] validUnits;



    /**
     * Creates a new <code>CalibratedDoubleField</code>.
     * 
     * @param name
     *            name of the field
     * @param length
     *            length of the <code>DoubleField</code>
     * @param defaultValue
     *            default value
     * @param validUnits
     *            units that the field should accept
     * @throws IllegalArgumentException
     *             if the default value cannot be expressed by all the valid
     *             units
     * @throws IllegalArgumentException
     *             the preferred units of the default value are not in the valid
     *             units
     */
    public CalibratedDoubleField(String name, int length,
            Magnitude defaultValue, String[] validUnits) {
        setLayout(new FlowLayout());

        if (name == null)
            throw new NullPointerException("name is null.");
        setName(name);

        // Value
        valueField =
                new DoubleField(name + "-value", length,
                        defaultValue.getPreferredUnitsValue());
        add(valueField);

        add(new Spacer(5, 5));

        // Units
        for (String unit : validUnits)
            if (!defaultValue.areUnits(unit))
                throw new IllegalArgumentException("Invalid unit (" + unit
                        + "). The default value (" + defaultValue
                        + ") cannot be expressed using these units.");
        if (!Arrays.contains(validUnits, defaultValue.getPreferredUnitsLabel()))
            throw new IllegalArgumentException(
                    "The preferred units of the default value ("
                            + defaultValue.getPreferredUnitsLabel()
                            + ") must be in the " + "valid units array ("
                            + java.util.Arrays.toString(validUnits) + ").");
        this.validUnits = validUnits;

        unitsCBox = new ComboBox<String>(validUnits);
        unitsCBox.setName(name + "-units");
        unitsCBox.setSelectedItem(defaultValue.getPreferredUnitsLabel());
        add(unitsCBox);
    }



    /**
     * Creates a new <code>CalibratedDoubleField</code>.
     * 
     * @param name
     *            name of the field
     * @param defaultValue
     *            default value
     * @param validUnits
     *            units that the field should accept
     * @throws IllegalArgumentException
     *             if the default value cannot be expressed by all the valid
     *             units
     * @throws IllegalArgumentException
     *             the preferred units of the default value are not in the valid
     *             units
     */
    public CalibratedDoubleField(String name, Magnitude defaultValue,
            String[] validUnits) {
        this(name, 5, defaultValue, validUnits);
    }



    /**
     * Creates a new <code>CalibratedDoubleField</code>. The valid units is set
     * to the preferred units of the default value.
     * 
     * @param name
     *            name of the field
     * @param defaultValue
     *            default value
     */
    public CalibratedDoubleField(String name, Magnitude defaultValue) {
        this(name, defaultValue,
                new String[] { defaultValue.getPreferredUnitsLabel() });
    }



    /**
     * Wrapper over {@link DoubleField#setDecimalCount(double)}.
     * 
     * @param nbDecimals
     *            number of decimal for the <code>DoubleField</code>
     */
    public void setDecimalCount(int nbDecimals) {
        valueField.setDecimalCount(nbDecimals);
    }



    /**
     * Wrapper over {@link DoubleField#setIncrementalStep(double)}.
     * 
     * @param step
     *            step of the spinner
     */
    public void setIncrementalStep(double step) {
        valueField.setIncrementalStep(step);
    }



    /**
     * Returns the selected value and units.
     * 
     * @return selected value and units
     */
    public Magnitude getValue() {
        return new Magnitude(valueField.getValue(), unitsCBox.getSelectedItem());
    }



    /**
     * Returns the buffered value and units.
     * 
     * @return selected value and units
     */
    public Magnitude getValueBFR() {
        return new Magnitude(valueField.getValueBFR(),
                unitsCBox.getSelectedItemBFR());
    }



    /**
     * Returns the thread-safe value and units.
     * 
     * @return selected value and units
     */
    public Magnitude getValueTS() {
        return new Magnitude(valueField.getValueTS(),
                unitsCBox.getSelectedItemTS());
    }



    /**
     * Sets the value and the units.
     * 
     * @param mag
     *            a magnitude
     * @throws IllegalArgumentException
     *             if the units of the new magnitude are not in the valid units
     *             array
     */
    public void setValue(Magnitude mag) {
        if (!Arrays.contains(validUnits, mag.getPreferredUnitsLabel()))
            throw new IllegalArgumentException(
                    "The preferred units of the new value ("
                            + mag.getPreferredUnitsLabel()
                            + ") must be in the " + "valid units array ("
                            + java.util.Arrays.toString(validUnits) + ").");
        valueField.setValue(mag.getBaseUnitsValue());
        unitsCBox.setSelectedItem(mag.getPreferredUnitsLabel());
    }



    @Override
    public void bufferInput() {
        valueField.bufferInput();
        unitsCBox.bufferInput();
    }



    /**
     * Wrapper over {@link DoubleField#getRange()}.
     * 
     * @return range of the <code>DoubleField</code>
     */
    public Range<Magnitude> getRange() {
        Range<Double> range = valueField.getRange();
        String units = unitsCBox.getSelectedItem();

        Magnitude minimum = new Magnitude(range.min, units);
        Magnitude maximum = new Magnitude(range.max, units);
        return new Range<Magnitude>(minimum, maximum);
    }



    /**
     * Wrapper over {@link DoubleField#setRange(double, double)}.
     * 
     * @param min
     *            minimum value of the range
     * @param max
     *            maximum value of the range
     */
    public void setRange(Magnitude min, Magnitude max) {
        String units = unitsCBox.getSelectedItem();
        valueField.setRange(min.getValue(units), max.getValue(units));
    }



    @Override
    public boolean isCorrect() {
        return isCorrect(true);
    }



    @Override
    public boolean isCorrect(boolean showErrorDialog) {
        return valueField.isCorrect(showErrorDialog);
    }



    @Override
    public Preferences getPreferences() {
        return preferences;
    }



    @Override
    public void setPreferences(Preferences pref) {
        valueField.setPreferences(pref);
        unitsCBox.setPreferences(pref);
    }



    @Override
    public void updatePreferences() {
        valueField.updatePreferences();
        unitsCBox.updatePreferences();
    }
}
