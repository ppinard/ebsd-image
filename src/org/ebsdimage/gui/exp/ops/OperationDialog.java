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
package org.ebsdimage.gui.exp.ops;

import rmlshared.gui.BasicDialog;

/**
 * Abstract class for all the operation dialogs.
 * 
 * @author Philippe T. Pinard
 * 
 */
public abstract class OperationDialog extends BasicDialog implements
        OperationCreator {

    /**
     * Creates a new <code>OperationDialog</code> with the specified title.
     * 
     * @param title
     *            title of the dialog
     */
    public OperationDialog(String title) {
        super(null, title);
    }

}
