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
package org.ebsdimage.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

import rmlimage.core.*;
import crystallography.core.Crystal;

/**
 * <code>Map</code> to hold the location of the different phases. To make sure
 * the map is always valid the method {@link #setPixValue(int, int)} and
 * {@link #setPixValue(int, int, int)} should be used instead of the
 * <code>pixArray</code>.
 * 
 * @author Philippe T. Pinard
 * 
 */
public class PhasesMap extends EightBitMap {

    /**
     * Returns a color look-up table for the different phases. Each phase is
     * assigned a specific color. The colors are random so that two consecutive
     * phases have different colors.
     * 
     * @param nbColors
     *            number of colors to generate
     * @return a look-up table
     */
    private static LUT randomColorLUT(int nbColors) {
        LUT lut = new LUT(nbColors);

        // Color 0 = black = background
        lut.red[0] = (byte) 0;
        lut.green[0] = (byte) 0;
        lut.blue[0] = (byte) 0;

        // Set the color components of the lut
        Random rnd = new Random(1);
        int red;
        int green;
        int blue;
        for (int n = 1; n < nbColors; n++) {
            red = rnd.nextInt(256);
            green = rnd.nextInt(256);
            blue = rnd.nextInt(256);

            // If color too dark, redo
            if (red + green + blue < 128) {
                n--;
                continue;
            }

            lut.red[n] = (byte) red;
            lut.green[n] = (byte) green;
            lut.blue[n] = (byte) blue;
        }

        return lut;
    }



    /** Defined phases. */
    private Crystal[] phases;



    /**
     * Creates an empty <code>PhasesMap</code>.
     * 
     * @param width
     *            width of the map
     * @param height
     *            height of the map
     */
    public PhasesMap(int width, int height) {
        super(width, height, randomColorLUT(255));

        phases = new Crystal[0];
    }



    /**
     * Creates a new <code>PhasesMap</code> with the specified pixArray and
     * phases. The map is validated so that all the pixels in the pixArray have
     * a phase assigned to them.
     * 
     * @param width
     *            width of the map
     * @param height
     *            height of the map
     * @param pixArray
     *            pixels in the map
     * @param phases
     *            phases of the map
     * 
     * @throws NullPointerException
     *             if the phases array is null
     * @throws IllegalArgumentException
     *             if the map is invalid
     * 
     * @see #validate()
     */
    public PhasesMap(int width, int height, byte[] pixArray, Crystal[] phases) {
        // Update pixArray, size, etc.
        super(width, height, randomColorLUT(255), pixArray);

        if (phases == null)
            throw new NullPointerException("Phases cannot be null.");

        // Update phases
        this.phases = phases;

        // Validate
        validate();
    }



    /**
     * Creates an empty <code>PhasesMap</code> with the specified phases.
     * 
     * @param width
     *            width of the map
     * @param height
     *            height of the map
     * @param phases
     *            phases of the map
     * 
     * @throws NullPointerException
     *             if the phases array is null
     * @throws IllegalArgumentException
     *             if the map is invalid
     * 
     * @see #validate()
     */
    public PhasesMap(int width, int height, Crystal[] phases) {
        this(width, height, new byte[width * height], phases);
    }



    /**
     * Creates a duplicate of the specified <code>PhasesMap</code>.
     * 
     * @param map
     *            a <code>PhasesMap</code>
     */
    public PhasesMap(PhasesMap map) {
        this(map.width, map.height, map.pixArray, map.phases);

        duplicate(map);
    }



    @Override
    public void assertEquals(Map map) {
        PhasesMap other = (PhasesMap) map;

        if (phases.length != other.phases.length)
            throw new AssertionError("Different number of phases between "
                    + getName() + " (" + phases.length + ") and "
                    + other.getName() + " (" + other.phases.length + ").");

        for (Crystal phase : phases) {
            boolean containsPhase = false;

            for (Crystal otherPhase : other.phases) {
                if (phase.equals(otherPhase)) {
                    containsPhase = true;
                    break;
                }
            }

            if (!containsPhase)
                throw new AssertionError("Phase " + phase.name + " exists in "
                        + getName() + ", but not in " + other.getName() + ".");
        }

        super.assertEquals(map);
    }



    /**
     * Asserts that the specified <code>PhasesMap</code> is equal to this
     * <code>PhasesMap</code> within the specified precision. The dimensions,
     * values and phases are compared between the two maps.
     * 
     * @param other
     *            other <code>PhasesMap</code> to check equality
     * @param precision
     *            level of precision
     * 
     * @throws IllegalArgumentException
     *             if the precision is less than 0.0
     * @throws IllegalArgumentException
     *             if the precision is not a number (NaN)
     * 
     * @see Crystal#equals(Crystal, double)
     */
    public void assertEquals(PhasesMap other, double precision) {
        if (precision < 0)
            throw new IllegalArgumentException(
                    "The precision has to be greater or equal to 0.0.");
        if (Double.isNaN(precision))
            throw new IllegalArgumentException(
                    "The precision must be a number.");

        if (phases.length != other.phases.length)
            throw new AssertionError("Different number of phases between "
                    + getName() + " (" + phases.length + ") and "
                    + other.getName() + " (" + other.phases.length + ").");

        for (Crystal phase : phases) {
            boolean containsPhase = false;

            for (Crystal otherPhase : other.phases) {
                if (phase.equals(otherPhase, precision)) {
                    containsPhase = true;
                    break;
                }
            }

            if (!containsPhase)
                throw new AssertionError("Phase " + phase.name + " exists in "
                        + getName() + ", but not in " + other.getName() + ".");
        }

        super.assertEquals(other);
    }



    /**
     * Fills the map with the value 0.
     */
    @Override
    public void clear() {
        Arrays.fill(pixArray, (byte) 0);

        setChanged(Map.MAP_CHANGED);
    }



    /**
     * Fill the map with the specified color.
     * 
     * @param color
     *            the color index
     * 
     * @throws IllegalArgumentException
     *             if <code>color</code> is < 0 or > 255.
     */
    public void clear(int color) {
        if (color < 0 || color > 255)
            throw new IllegalArgumentException("color (" + color
                    + ") must be between 0 and 255.");

        Arrays.fill(pixArray, (byte) color);

        setChanged(Map.MAP_CHANGED);
    }



    /**
     * Creates an empty <code>PhasesMap</code> with the specified dimensions.
     * The phases of this map are passed to the new map.
     * 
     * @param width
     *            width of the map
     * @param height
     *            height of the map
     * @return a <code>PhasesMap</code>
     */
    @Override
    public PhasesMap createMap(int width, int height) {
        return new PhasesMap(width, height, getPhases().clone());
    }



    /**
     * Creates a copy of the <code>PhasesMap</code>.
     * 
     * @return a copy of the <code>PhasesMap</code>
     */
    @Override
    public PhasesMap duplicate() {
        return new PhasesMap(this);
    }



    /**
     * Duplicates this map properties and values in the specified map.
     * 
     * @param map
     *            output map
     * 
     * @throws NullPointerException
     *             if the map is null
     * @throws IllegalArgumentException
     *             if the specified map and this map are not of the same size
     */
    public void duplicate(PhasesMap map) {
        if (map == null)
            throw new NullPointerException("Map cannot be null.");
        if (!isSameSize(map))
            throw new IllegalArgumentException("map ("
                    + map.getDimensionLabel()
                    + ") is not the same size as this (" + getDimensionLabel()
                    + ')');

        // Copy the file name
        setFile(map.getFile());

        // Copy the lut
        lut.setLUT(map.lut);

        // Copy the properties of the source map to the new map
        clearProperties();
        setProperties(map);

        System.arraycopy(map.pixArray, 0, pixArray, 0, size);
        System.arraycopy(map.phases, 0, phases, 0, map.phases.length);

        setChanged(ByteMap.MAP_CHANGED | ByteMap.LUT_CHANGED);
    }



    /**
     * Returns the defined phases. A copy of the array is returned.
     * 
     * @return phases
     */
    public Crystal[] getPhases() {
        return phases.clone();
    }



    /**
     * Returns a <code>PhasePixel</code> representing the specified pixel.
     * 
     * @param index
     *            index of the pixel
     * @return a <code>PhasePixel</code>
     * 
     * @throws IllegalArgumentException
     *             if the index is out of range
     */
    @Override
    public Pixel getPixel(int index) {
        if (index < 0 || index > size)
            throw new IllegalArgumentException("Index must be between [0,"
                    + size + "[.");

        int id = pixArray[index] & 0xff;

        if (id == 0) // Not indexed
            return new PhasePixel();
        else
            return new PhasePixel(id, phases[id - 1]);
    }



    @Override
    public String[] getValidFileFormats() {
        return new String[] { "bmp" };
    }



    /**
     * Checks if this <code>PhasesMap</code> is valid.
     * 
     * @return <code>true</code> if the map is valid, <code>false</code>
     *         otherwise
     */
    public boolean isCorrect() {
        try {
            validate();
            return true;
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }



    /**
     * Sets a new array of defined phases.
     * 
     * @param phases
     *            new phases
     * 
     * @throws NullPointerException
     *             if the phases array is null
     * @throws IllegalArgumentException
     *             if the new phases array is invalid
     * 
     * @see #validate()
     */
    public void setPhases(Crystal[] phases) {
        if (phases == null)
            throw new NullPointerException("Phases cannot be null.");

        // Store old phases in case of an error
        Crystal[] oldPhases = getPhases();

        // Set new phases
        this.phases = phases;

        try {
            // Validate
            validate();
        } catch (IllegalArgumentException e) {
            // Set old phases back
            this.phases = oldPhases;

            // Throw exception
            throw new IllegalArgumentException(e);
        }
    }



    /**
     * Sets a new value to a pixel. The <code>PhasesMap</code> is checked to
     * make sure that the new value is valid.
     * 
     * @param index
     *            index of the pixel
     * @param value
     *            new value
     * 
     * @throws IllegalArgumentException
     *             if the index is out of range
     * @throws IllegalArgumentException
     *             if the new value is invalid
     * 
     * @see #validate()
     */
    @Override
    public void setPixValue(int index, int value) {
        if (index < 0 || index > size)
            throw new IllegalArgumentException("Index must be between [0,"
                    + size + "[.");
        if (value < 0 || value > phases.length)
            throw new IllegalArgumentException("Value (" + value
                    + ") must be between ]0 and " + phases.length + "].");

        // Store old value in case of an error
        byte oldValue = pixArray[index];

        // Set new value
        pixArray[index] = (byte) value;

        try {
            // Validate
            validate();
        } catch (IllegalArgumentException e) {
            // Set old value back
            pixArray[index] = oldValue;

            // Throw exception
            throw new IllegalArgumentException(e);
        }
    }



    /**
     * Sets a new value to a pixel. The <code>PhasesMap</code> is checked to
     * make sure that the new value is valid.
     * 
     * @param x
     *            position in x
     * @param y
     *            position in y
     * @param value
     *            new value
     * 
     * @throws IllegalArgumentException
     *             if the index is out of range
     * @throws IllegalArgumentException
     *             if the new value is invalid
     * 
     * @see #validate()
     */
    @Override
    public void setPixValue(int x, int y, int value) {
        setPixValue(getPixArrayIndex(x, y), value);
    }



    /**
     * Validates this <code>PhasesMap</code>. To be valid, the number of phases
     * in the pixArray must be equal to the number of defined phases.
     * 
     * @throws IllegalArgumentException
     *             if the pixArray contained undefined phase
     * @throws IllegalArgumentException
     *             if the number of phases in the pixArray is greater than the
     *             number of defined phases
     */
    public void validate() {
        ArrayList<Integer> tmpArray = new ArrayList<Integer>();
        tmpArray.add(0); // Add non-indexed value

        for (byte color : pixArray) {
            if (!tmpArray.contains((int) color))
                tmpArray.add((int) color);
        }

        Collections.sort(tmpArray);
        int phasesCount = tmpArray.size() - 1;
        int lastPhase = tmpArray.get(tmpArray.size() - 1);

        if (lastPhase > phases.length)
            throw new IllegalArgumentException(
                    "The pixArray contains undefined phase (id=" + lastPhase
                            + ").");

        if (phasesCount > phases.length)
            throw new IllegalArgumentException(
                    "The number of phases in the pixArray (" + phasesCount
                            + ") is greater than the number "
                            + "of defined phases (" + phases.length + ").");
    }

}
