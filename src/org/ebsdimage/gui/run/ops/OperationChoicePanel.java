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

import java.awt.Dimension;
import java.io.IOException;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import net.miginfocom.swing.MigLayout;

import org.ebsdimage.core.exp.ExpOperation;

import ptpshared.gui.GuiUtil;
import rmlshared.gui.Button;

/**
 * Abstract class to list and select operations for the experiment.
 * 
 * @author Marin Lagac&eacute;
 */
public abstract class OperationChoicePanel extends JPanel {

    /** Icon for the add all button. */
    protected static final ImageIcon ADD_ALL_ICON =
            GuiUtil.loadIcon("ptpshared/data/icon/list-add-all_(22x22).png");

    /** Icon for the add button. */
    protected static final ImageIcon ADD_ICON =
            GuiUtil.loadIcon("ptpshared/data/icon/list-add_(22x22).png");

    /** Icon for the clear button. */
    protected static final ImageIcon CLEAR_ICON =
            GuiUtil.loadIcon("ptpshared/data/icon/list-clear_(22x22).png");

    /** Icon for the down button. */
    protected static final ImageIcon DOWN_ICON =
            GuiUtil.loadIcon("ptpshared/data/icon/go-down_(22x22).png");

    /** Icon for the remove button. */
    protected static final ImageIcon REMOVE_ICON =
            GuiUtil.loadIcon("ptpshared/data/icon/list-remove_(22x22).png");

    /** Icon for the up button. */
    protected static final ImageIcon UP_ICON =
            GuiUtil.loadIcon("ptpshared/data/icon/go-up_(22x22).png");

    /** List of operations available. */
    protected JList opList;

    /** List of operations selected by the user. */
    protected JList userList;



    /**
     * Creates a new <code>OperationChoicePanel</code> and list all the
     * available operations in the specified package.
     * 
     * @param title
     *            title of the panel
     * @param packageName
     *            name of the package for the operations
     * @throws IOException
     *             if an error occurs while listing the operations
     */
    public OperationChoicePanel(String title, String packageName)
            throws IOException {

        setBorder(new TitledBorder(title));

        setLayout(new MigLayout("wrap 3", "[][]50[]"));

        // User list
        userList = new JList(new DefaultListModel());
        JScrollPane listScroller = new JScrollPane(userList);
        listScroller.setPreferredSize(new Dimension(150, 100));
        add(listScroller, "spany 2, grow, push");

        JComponent upButton = getUpButton();
        add(upButton, "top left");

        // Op list
        DefaultListModel model = new DefaultListModel();
        OperationCreator[] ops =
                OperationCreatorUtil.getOperationCreators(packageName);
        for (OperationCreator op : ops)
            model.addElement(op);

        opList = new JList(model);
        listScroller = new JScrollPane(opList);
        listScroller.setPreferredSize(new Dimension(150, 100));
        add(listScroller, "spany 2, grow, push");

        JComponent downButton = getDownButton();
        add(downButton, "bottom left");

        JComponent removeButton = getRemoveButton();
        add(removeButton, "split 2, align right");

        JComponent clearButton = getClearButton();
        add(clearButton);

        JComponent addButton = getAddButton();
        add(addButton, "skip, split 2, align right");

        JComponent addAllButton = getAddAllButton();
        add(addAllButton);

    }



    /**
     * Returns the add all button to add all operations from the available
     * operations list to the user's operations list.
     * 
     * @return button
     */
    protected abstract Button getAddAllButton();



    /**
     * Returns the add button to add an operation from the available operations
     * list to the user's operations list.
     * 
     * @return button
     */
    protected abstract Button getAddButton();



    /**
     * Returns the button to remove all operations from the user's operations
     * list.
     * 
     * @return button
     */
    protected abstract Button getClearButton();



    /**
     * Returns the down button to move down a level an operation in the user's
     * operations list.
     * 
     * @return button
     */
    protected abstract Button getDownButton();



    /**
     * Returns the list of <code>Operation</code>s chosen by the user.
     * 
     * @return operations
     */
    public ExpOperation[] getOperations() {
        DefaultListModel model = (DefaultListModel) userList.getModel();
        int nbOps = model.getSize();

        ExpOperation[] ops = new ExpOperation[nbOps];
        for (int n = 0; n < nbOps; n++)
            ops[n] = (ExpOperation) model.get(n);

        return ops;
    }



    /**
     * Returns the button to remove an operation from the user's operations
     * list.
     * 
     * @return button
     */
    protected abstract Button getRemoveButton();



    /**
     * Returns the down button to move up a level an operation in the user's
     * operations list.
     * 
     * @return button
     */
    protected abstract Button getUpButton();



    /**
     * Returns the model for the list of operations selected by the user.
     * 
     * @return list model
     */
    public DefaultListModel getUserListModel() {
        return (DefaultListModel) userList.getModel();
    }



    /**
     * Checks is the user has chosen a valid number of <dfn>Operation</dfn>s.
     * 
     * @return <code>true</code> if the minimum number of <dfn>Operation</dfn>s
     *         have been chosen or <code>false</code> otherwise.
     */
    public abstract boolean isCorrect();

}
