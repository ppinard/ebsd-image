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
package org.ebsdimage.gui.run.ops;

import javax.swing.DefaultListModel;

import org.ebsdimage.core.run.Operation;

import rmlshared.gui.Button;
import rmlshared.thread.PlugIn;

/**
 * Panel for the experiment's wizard where the user can only select one or many
 * operations.
 * 
 * @author Marin Lagac&eacute;
 */
public class MultipleChoicePanel extends OperationChoicePanel {

    /**
     * Action to add an operation.
     * 
     * @author Philippe T. Pinard
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
            model.addElement(op);
        }
    }

    /**
     * Action to add all operations.
     * 
     * @author Philippe T. Pinard
     */
    private class AddAll extends PlugIn {
        @Override
        protected void xRun() {
            // Get all operation creators
            Object[] ocs = ((DefaultListModel) opList.getModel()).toArray();

            DefaultListModel model = (DefaultListModel) userList.getModel();

            for (Object obj : ocs) {
                OperationCreator oc = (OperationCreator) obj;

                // Show the dialog
                int answer = oc.show();
                if (answer == OperationCreator.CANCEL)
                    return;

                // Get the operation
                Operation op = oc.getOperation();

                // Add it to the user list
                model.addElement(op);
            }
        }
    }

    /**
     * Removes all operations from the selected list of operations.
     * 
     * @author Philippe T. Pinard
     */
    private class Clear extends PlugIn {
        @Override
        protected void xRun() {
            DefaultListModel model = (DefaultListModel) userList.getModel();

            for (Object obj : model.toArray())
                model.removeElement(obj);
        }
    }

    /**
     * Moves an operation down one level in the selected operations' list.
     * 
     * @author Marin Lagac&eacute;
     */
    private class Down extends PlugIn {
        @Override
        public void xRun() {
            // Get the selected operation
            int index = userList.getSelectedIndex();
            if (index < 0)
                return; // If no operation selected

            DefaultListModel model = (DefaultListModel) userList.getModel();
            if (index >= model.size() - 1)
                return; // If last operation selected

            // Move it down
            Object op = model.getElementAt(index);
            model.removeElementAt(index);
            model.insertElementAt(op, index + 1);
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
            // Get the selected operation
            Operation op = (Operation) userList.getSelectedValue();
            if (op == null)
                return;

            // Remove it from the user list
            DefaultListModel model = (DefaultListModel) userList.getModel();
            model.removeElement(op);
        }
    }

    /**
     * Moves an operation up one level in the selected operations' list.
     * 
     * @author Marin Lagac&eacute;
     */
    private class Up extends PlugIn {
        @Override
        public void xRun() {
            // Get the selected operation
            int index = userList.getSelectedIndex();
            if (index < 0)
                return; // If no operation selected
            if (index == 0)
                return; // If first operation selected

            // Move it up
            DefaultListModel model = (DefaultListModel) userList.getModel();
            Object op = model.getElementAt(index);
            model.removeElementAt(index);
            model.insertElementAt(op, index - 1);
        }

    }



    /**
     * Creates a new <code>MultipleChoicePanel</code> to select many operations.
     * 
     * @param title
     *            title of the panel
     * @param packageName
     *            name of the package for the operations
     */
    public MultipleChoicePanel(String title, String packageName) {
        super(title, packageName);
    }



    @Override
    protected Button getAddAllButton() {
        Button addAllButton = new Button(ADD_ALL_ICON);
        addAllButton.setToolTipText("Add all operations to current list");
        addAllButton.setPlugIn(new AddAll());
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
        clearButton.setToolTipText("Remove all operations from current list");
        clearButton.setPlugIn(new Clear());
        return clearButton;
    }



    @Override
    protected Button getDownButton() {
        Button downButton = new Button(DOWN_ICON);
        downButton.setToolTipText("Move operation down");
        downButton.setPlugIn(new Down());
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
        upButton.setToolTipText("Move operation up");
        upButton.setPlugIn(new Up());
        return upButton;
    }



    @Override
    public boolean isCorrect() {
        return true;
    }

}
