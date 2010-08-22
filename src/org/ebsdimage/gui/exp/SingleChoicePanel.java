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
package org.ebsdimage.gui.exp;

import java.io.IOException;

import javax.swing.DefaultListModel;

import org.ebsdimage.core.run.Operation;
import org.ebsdimage.gui.run.ops.OperationChoicePanel;
import org.ebsdimage.gui.run.ops.OperationCreator;

import rmlshared.gui.Button;
import rmlshared.thread.PlugIn;

/**
 * Panel for the experiment's wizard where the user can only select one
 * operation.
 * 
 * @author Marin Lagac&eacute;
 */
public class SingleChoicePanel extends OperationChoicePanel {

    /**
     * Action to add an operation. Only one operation can be added.
     */
    private class Add extends PlugIn {
        @Override
        public void xRun() {
            // Get the selected operation
            OperationCreator oc = (OperationCreator) opList.getSelectedValue();
            if (oc == null)
                return;

            // Show the dialog
            int answer = oc.show();
            if (answer == OperationCreator.CANCEL)
                return;

            // Get the operation
            Operation op = oc.getOperation();

            // Add it to the user list
            DefaultListModel model = (DefaultListModel) userList.getModel();
            if (model.getSize() == 0)
                model.addElement(op);
            else
                model.set(0, op);
        }

    }

    /**
     * Removes all operations from the selected list of operations.
     * 
     * @author Philippe T. Pinard
     */
    private class Clear extends PlugIn {
        @Override
        public void xRun() {
            DefaultListModel model = (DefaultListModel) userList.getModel();

            for (Object obj : model.toArray())
                model.removeElement(obj);
        }
    }

    /**
     * Removes an operation from the selected list of operations.
     * 
     * @author Marin Lagac&eacute;
     */
    private class Remove extends PlugIn {
        @Override
        public void xRun() {
            // Remove the only operation
            DefaultListModel model = (DefaultListModel) userList.getModel();
            if (model.getSize() > 0)
                model.removeElement(model.get(0));
        }
    }



    /**
     * Creates a <code>SingleChoicePanel</code> to select only one operation.
     * 
     * @param title
     *            title of the panel
     * @param packageName
     *            name of the package for the operations
     * @throws IOException
     *             if an error occurs while listing the operations
     */
    public SingleChoicePanel(String title, String packageName)
            throws IOException {

        super(title, packageName);
    }



    @Override
    protected Button getAddAllButton() {
        Button addAllButton = new Button(ADD_ALL_ICON);
        addAllButton.setEnabled(false);
        return addAllButton;
    }



    @Override
    protected Button getAddButton() {
        Button addButton = new Button(ADD_ICON);
        addButton.setToolTipText("Add selected operation to current list");
        addButton.setPlugIn(new Add());
        return addButton;
    }



    @Override
    protected Button getClearButton() {
        Button clearButton = new Button(CLEAR_ICON);
        clearButton.setToolTipText("Remove operation from current list");
        clearButton.setPlugIn(new Clear());
        return clearButton;
    }



    @Override
    protected Button getDownButton() {
        Button downButton = new Button(DOWN_ICON);
        downButton.setEnabled(false);
        return downButton;
    }



    @Override
    protected Button getRemoveButton() {
        Button removeButton = new Button(REMOVE_ICON);
        removeButton.setToolTipText("Remove selected operation from current list");
        removeButton.setPlugIn(new Remove());
        return removeButton;
    }



    @Override
    protected Button getUpButton() {
        Button upButton = new Button(UP_ICON);
        upButton.setEnabled(false);
        return upButton;
    }



    @Override
    public boolean isCorrect() {
        return true;
    }
}
