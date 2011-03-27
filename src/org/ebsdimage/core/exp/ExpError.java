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
package org.ebsdimage.core.exp;

import net.jcip.annotations.Immutable;

import org.ebsdimage.core.ErrorCode;

/**
 * Minor error occurring during the execution of an experiment. The error will
 * be saved in the <code>ErrorMap</code> of the EBSD multimap.
 * 
 * @author Philippe T. Pinard
 */
@Immutable
public class ExpError extends Exception {

    /** Error code associated with this exception. */
    private final ErrorCode errorCode;



    /**
     * Creates a new <code>ExpError</code>.
     * 
     * @param errorCode
     *            error code describing the error
     */
    public ExpError(ErrorCode errorCode) {
        super(errorCode.getLabel());
        this.errorCode = errorCode;
    }



    /**
     * Returns the error code.
     * 
     * @return error code
     */
    public ErrorCode getErrorCode() {
        return errorCode;
    }

}
