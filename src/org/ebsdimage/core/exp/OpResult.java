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
package org.ebsdimage.core.exp;

import rmlimage.core.Map;

/**
 * Result obtained from an operation.
 * 
 * @author Philippe T. Pinard
 */
public class OpResult {

    /** Alias identifying the result. */
    public final String alias;

    /** Value of the result. */
    public final Number value;

    /** Units of the value. */
    public final String units;

    /** Type of map where to save the result. */
    public final Class<? extends Map> type;



    /**
     * Creates a new <code>OpResult</code>.
     * 
     * @param alias
     *            alias identifying the result
     * @param value
     *            value of the result
     * @param units
     *            units of the value
     * @param type
     *            type of map where to save the result
     */
    public OpResult(String alias, Number value, String units,
            Class<? extends Map> type) {
        if (alias == null)
            throw new NullPointerException("Alias cannot be null.");
        if (value == null)
            throw new NullPointerException("Value cannot be null.");
        if (units == null)
            throw new NullPointerException("Units cannot be null.");
        if (type == null)
            throw new NullPointerException("Map type cannot be null.");

        this.alias = alias;
        this.value = value;
        this.units = units;
        this.type = type;
    }



    /**
     * Creates a new <code>OpResult</code>.
     * 
     * @param alias
     *            alias identifying the result
     * @param value
     *            value of the result
     * @param type
     *            type of map where to save the result
     */
    public OpResult(String alias, Number value, Class<? extends Map> type) {
        this(alias, value, "", type);
    }
}
