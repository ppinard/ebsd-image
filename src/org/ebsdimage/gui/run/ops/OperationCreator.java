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
package org.ebsdimage.gui.run.ops;

import org.ebsdimage.core.run.Operation;

import rmlshared.gui.OkCancelDialog;

/**
 * Interface for the GUI to create operation.
 * 
 * @author Marin Lagac@eacute;
 */
public interface OperationCreator {

    /**
     * Value returned if the operation should be added to the list of
     * operations.
     */
    public static final int OK = OkCancelDialog.OK;

    /** Value returned if the user cancels the creation of a new operation. */
    public static final int CANCEL = OkCancelDialog.CANCEL;



    /**
     * Long description of the <code>Operation</code>.
     * 
     * @return long description
     */
    public String getDescription();



    /**
     * Returns the <code>Operation</code> properly initialised using the
     * parameters specified in the dialog.
     * <p/>
     * This method must be called after <code>show()</code> and only if the
     * method returns <code>OK</code>.
     * 
     * @return the <code>Operation</code>
     */
    public Operation getOperation();



    /**
     * Shows the dialog.
     * 
     * @return <code>OK</code> if the OK button has been pressed or
     *         <code>CANCEL</code> if the Cancel button has been pressed
     */
    public int show();



    /**
     * Returns the name of the operation. Used by the list or combo box.
     * 
     * @return name of the operation
     */
    @Override
    public String toString();
}
