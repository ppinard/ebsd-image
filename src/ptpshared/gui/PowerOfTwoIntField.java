/*
 * EBSD-Image
 * Copyright (C) 2010-2011 Philippe T. Pinard
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
package ptpshared.gui;

import rmlimage.core.OperationNotSupportedException;
import rmlshared.gui.ErrorDialog;
import rmlshared.gui.IntField;
import rmlshared.math.IntUtil;

/**
 * Integer spinner field when only power of two values can be inputed.
 * 
 * @author Philippe T. Pinard
 */
public class PowerOfTwoIntField extends IntField {

    /**
     * Creates a new <code>PowerOfTwoIntField</code>.
     * 
     * @param name
     *            name of the field
     * @param deFault
     *            default value
     * @throws IllegalArgumentException
     *             if the default value is not a power of two
     */
    public PowerOfTwoIntField(String name, int deFault) {
        this(name, 5, deFault);
    }



    /**
     * Creates a new <code>PowerOfTwoIntField</code>.
     * 
     * @param name
     *            name of the field
     * @param length
     *            size of the field
     * @param deFault
     *            default value
     * @throws IllegalArgumentException
     *             if the default value is not a power of two
     */
    public PowerOfTwoIntField(String name, int length, int deFault) {
        super(name, length, deFault);

        if (!IntUtil.isPowerOfTwo(deFault))
            throw new IllegalArgumentException("Default value (" + deFault
                    + ") is not a power of two.");

        setRange(1, (int) Math.pow(2, 30));
    }



    @Override
    protected void decrementPerformed() {
        // If the value in the field is invalid,
        // fire the event anyway and let the user deal with it
        if (!isCorrect(false)) {
            fireValueChanged();
            return;
        }

        // Perform the increment
        int oldValue = getValue();
        int newValue = oldValue / 2;

        // Check if it is not out of range
        if (newValue < getRange().min)
            newValue = getRange().min;

        if (newValue != oldValue) {
            setValue(newValue);
            fireValueChanged();
        }
    }



    @Override
    protected void incrementPerformed() {
        // If the value in the field is invalid,
        // fire the event anyway and let the user deal with it
        if (!isCorrect(false)) {
            fireValueChanged();
            return;
        }

        // Perform the increment
        int oldValue = getValue();
        int newValue = oldValue * 2;

        // Check if it is not out of range
        if (newValue > getRange().max)
            newValue = getRange().max;

        if (newValue != oldValue) {
            setValue(newValue);
            fireValueChanged();
        }
    }



    @Override
    public boolean isCorrect(boolean showErrorDialog) {
        if (!super.isCorrect(showErrorDialog))
            return false;

        int value = getValue();
        if (!IntUtil.isPowerOfTwo(value)) {
            if (showErrorDialog)
                ErrorDialog.show(getName() + " (" + value
                        + ") must a power of two.");

            return false;
        }

        return true;
    }



    /**
     * {@inheritDoc} Not supported operation.
     */
    @Override
    public void setIncrementalStep(int step) {
        throw new OperationNotSupportedException();
    }



    /**
     * {@inheritDoc}
     * 
     * @throws IllegalArgumentException
     *             if the minimum or maximum value are not a power of two
     */
    @Override
    public void setRange(int min, int max) {
        if (!IntUtil.isPowerOfTwo(min))
            throw new IllegalArgumentException("min (" + min
                    + ") must be a power of two.");

        if (!IntUtil.isPowerOfTwo(max))
            throw new IllegalArgumentException("max (" + max
                    + ") must be a power of two.");

        super.setRange(min, max);
    }



    /**
     * {@inheritDoc}
     * 
     * @throws IllegalArgumentException
     *             if the value is not a power of two
     */
    @Override
    public void setValue(int value) {
        if (!IntUtil.isPowerOfTwo(value))
            throw new IllegalArgumentException("value (" + value
                    + ") is not a power of two.");

        super.setValue(value);
    }

}
