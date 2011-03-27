package ptpshared.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.text.ParseException;

import javax.swing.JComponent;

import magnitude.core.Magnitude;
import rmlshared.gui.ComboBox;
import rmlshared.gui.ErrorDialog;
import rmlshared.gui.Spinner;
import rmlshared.util.Arrays;
import rmlshared.util.Preferences;
import rmlshared.util.Range;

/**
 * A special <code>DoubleField</code> that allow the users to select calibrated
 * value (value + units). The field gives a selection of valid units that can be
 * used. It offers the same functionalities as the original
 * <code>DoubleField</code>.
 * 
 * @author Philippe T. Pinard
 */
public class CalibratedDoubleField extends Spinner {

    /**
     * Listener of the units combo box.
     * 
     * @author Philippe T. Pinard
     */
    private class UnitsCBoxListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String oldUnits = step.getPreferredUnitsLabel();
            String newUnits = unitsCBox.getSelectedItem();

            if (!oldUnits.equals(newUnits)) {
                step = new Magnitude(step.getPreferredUnitsValue(), newUnits);

                Magnitude newValue = new Magnitude(getDoubleValue(), oldUnits);
                newValue.setPreferredUnits(newUnits);

                if (Magnitude.isNaN(newValue))
                    setValue("NaN");
                else
                    setValue(numberFormatter.format(newValue.getPreferredUnitsValue()));
            }
        }

    }

    /** Minimum value of the range. */
    private Magnitude minimum;

    /** Maximum value of the range. */
    private Magnitude maximum;

    /** Increment of the spinner. */
    private Magnitude step;

    /** Combo box for the units. */
    private ComboBox<String> unitsCBox;

    /** Units that the field should accept. */
    private String[] validUnits;

    /** Formatter for the values. */
    private NumberFormat numberFormatter = NumberFormat.getNumberInstance();

    /** Preference to save the units. */
    protected static final String PREF_UNITS = "units";



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
     * @param showUnitsField
     *            show the units field, if <code>false</code> the field can be
     *            placed in a layout using {@link #getUnitsField}.
     * @throws IllegalArgumentException
     *             if the default value cannot be expressed by all the valid
     *             units
     * @throws IllegalArgumentException
     *             the preferred units of the default value are not in the valid
     *             units
     */
    public CalibratedDoubleField(String name, int length,
            Magnitude defaultValue, String[] validUnits, boolean showUnitsField) {
        super(name, length, "");

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
        String preferredUnits = defaultValue.getPreferredUnitsLabel();

        unitsCBox = new ComboBox<String>(validUnits);
        unitsCBox.setName(name + "-units");
        unitsCBox.setSelectedItem(preferredUnits);
        unitsCBox.addActionListener(new UnitsCBoxListener());

        if (showUnitsField)
            add(unitsCBox, BorderLayout.EAST);

        // Range and step
        minimum = new Magnitude(Double.NEGATIVE_INFINITY, preferredUnits);
        maximum = new Magnitude(Double.POSITIVE_INFINITY, preferredUnits);
        step = new Magnitude(1, preferredUnits);

        // Value
        setValue(defaultValue);
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
        this(name, 5, defaultValue, validUnits, true);
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
     * @param showUnitsField
     *            show the units field, if <code>false</code> the field can be
     *            placed in a layout using {@link #getUnitsField}.
     * @throws IllegalArgumentException
     *             if the default value cannot be expressed by all the valid
     *             units
     * @throws IllegalArgumentException
     *             the preferred units of the default value are not in the valid
     *             units
     */
    public CalibratedDoubleField(String name, Magnitude defaultValue,
            String[] validUnits, boolean showUnitsField) {
        this(name, 5, defaultValue, validUnits, showUnitsField);
    }



    @Override
    protected void decrementPerformed() {
        // If the value in the field is invalid,
        // fire the event anyway and let the user deal with it
        if (!isCorrect(false)) {
            fireValueChanged();
            return;
        }

        // Performed the increment
        Magnitude oldValue = getValue();
        Magnitude newValue = oldValue.minus(step);

        // Check if it is not out of range
        if (newValue.compareTo(minimum) < 0)
            newValue = minimum;

        newValue.setPreferredUnits(oldValue.getPreferredUnitsLabel());

        if (!newValue.equals(oldValue)) {
            setValue(newValue);
            fireValueChanged();
        }
    }



    /**
     * Returns the value of the spinner.
     * 
     * @return double value
     */
    private double getDoubleValue() {
        // Parse the value
        String text = super.getText();

        if (text.trim().equals("NaN"))
            return Double.NaN;

        double value;
        try {
            value = numberFormatter.parse(text).doubleValue();
        } catch (ParseException e) {
            throw new NumberFormatException(getName() + " (" + text
                    + ") is not an double.");
        }

        return value;
    }



    /**
     * Returns the range of the field.
     * 
     * @return the range of the field
     */
    public Range<Magnitude> getRange() {
        return new Range<Magnitude>(minimum, maximum);
    }



    /**
     * Returns the units combo box field.
     * 
     * @return units combo box
     */
    public JComponent getUnitsField() {
        return unitsCBox;
    }



    /**
     * Returns the value.
     * 
     * @return value
     * @throws NumberFormatException
     *             if the text in the field is not an Double
     * @throws IllegalArgumentException
     *             if the value in the field is not within the predefined range.
     */
    @Override
    public Magnitude getValue() {
        return new Magnitude(getDoubleValue(), unitsCBox.getSelectedItem());
    }



    @Override
    public Magnitude getValueBFR() {
        return (Magnitude) super.getValueBFR();
    }



    @Override
    public Magnitude getValueTS() {
        return (Magnitude) super.getValueTS();
    }



    @Override
    protected void incrementPerformed() {
        // If the value in the field is invalid,
        // fire the event anyway and let the user deal with it
        if (!isCorrect(false)) {
            fireValueChanged();
            return;
        }

        // Performed the increment
        Magnitude oldValue = getValue();
        Magnitude newValue = oldValue.add(step);

        // Check if it is not out of range
        if (newValue.compareTo(maximum) > 0)
            newValue = maximum;

        newValue.setPreferredUnits(oldValue.getPreferredUnitsLabel());

        if (!newValue.equals(oldValue)) {
            setValue(newValue);
            fireValueChanged();
        }
    }



    /*
     * public boolean isNaN() { String text = getText(); return
     * (text.indexOf("NaN") >= 0) ? true : false; }
     */

    /**
     * {@inheritDoc} For the value to be valid, it must be a double and within
     * the predefined range.
     */
    @Override
    public boolean isCorrect(boolean showErrorDialog) {
        Magnitude value = getValue();
        if (value.compareTo(minimum.minus(step.div(2.0))) < 0
                || value.compareTo(maximum.add(step.div(2.0))) > 0) {
            if (showErrorDialog)
                ErrorDialog.show(getName() + " (" + value
                        + ") is out of range (" + minimum + '-' + maximum
                        + ").");
            return false;
        }

        return true;
    }



    /**
     * Sets the number of decimal used to display the number in the field.
     * 
     * @param nbDecimals
     *            the number of decimal used to display the number in the field.
     *            If < 0, no format will be applied (default)
     */
    public void setDecimalCount(int nbDecimals) {
        if (nbDecimals < 0)
            throw new IllegalArgumentException("nbDecimals (" + nbDecimals
                    + ") must be >= " + 0 + '.');

        if (nbDecimals == 0) {
            numberFormatter.setParseIntegerOnly(true);
        } else {
            numberFormatter.setParseIntegerOnly(false);
            numberFormatter.setMinimumFractionDigits(nbDecimals);
            numberFormatter.setMaximumFractionDigits(nbDecimals);
        }
    }



    /**
     * Sets the incremental step of the spinner.
     * 
     * @param step
     *            incremental step
     * @throws IllegalArgumentException
     *             if <code>step</code> is lower than 0
     */
    public void setIncrementalStep(Magnitude step) {
        if (step.getBaseUnitsValue() <= 0)
            throw new IllegalArgumentException("step (" + step + ") must be > "
                    + 0 + '.');
        minimum.validateEqualUnits(step);

        this.step = step;
    }



    @Override
    public void setPreferredDefault() {
        Preferences pref = getPreferences();

        if (pref.contains(PREF_VALUE) && pref.contains(PREF_UNITS)) {
            double val = pref.getPreference(PREF_VALUE, 0.0);
            String units = pref.getPreference(PREF_UNITS, null);

            Magnitude value = new Magnitude(val, units);
            if (value.compareTo(minimum) >= 0 && value.compareTo(maximum) <= 0)
                setValue(value);
        }
    }



    /**
     * Sets the valid data range. If the incremental step previously defined is
     * too big for the range, it is reset to 1. If the default value previously
     * defined is out of range, it is set to its closest limit
     * 
     * @param min
     *            minimum data value (inclusively)
     * @param max
     *            maximum data value (inclusively)
     * @throws IllegalArgumentException
     *             if min > max
     */
    public void setRange(Magnitude min, Magnitude max) {
        if (min.compareTo(max) > 0)
            throw new IllegalArgumentException("min (" + min
                    + ") is higher than max (" + max + ')');

        minimum = min;
        maximum = max;

        Magnitude value = getValue();
        if (value.compareTo(min) < 0)
            value = min;
        if (value.compareTo(max) > 0)
            value = max;
        setValue(value);
    }



    /**
     * Sets the value shown in the field.
     * 
     * @param value
     *            value to be put in the field
     * @throws IllegalArgumentException
     *             if <code>value</code> is not within the predefined range
     */
    public void setValue(Magnitude value) {
        minimum.validateEqualUnits(value);
        if (value.compareTo(minimum) < 0 || value.compareTo(maximum) > 0)
            throw new IllegalArgumentException("value (" + value
                    + ") is not within predefined range (" + minimum + "-"
                    + maximum + ")");

        // Units
        if (!Arrays.contains(validUnits, value.getPreferredUnitsLabel()))
            value.setPreferredUnits(unitsCBox.getSelectedItem());
        unitsCBox.setSelectedItem(value.getPreferredUnitsLabel());

        // Value
        if (Magnitude.isNaN(value))
            super.setValue("NaN");
        else
            super.setValue(numberFormatter.format(value.getPreferredUnitsValue()));
    }



    @Override
    public void updatePreferences() {
        Preferences pref = getPreferences();

        Magnitude value = getValue();
        pref.setPreference(PREF_VALUE, value.getPreferredUnitsValue());
        pref.setPreference(PREF_UNITS, value.getPreferredUnitsLabel());
    }

}
