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
package org.ebsdimage.gui;

import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.JScrollPane;

import net.miginfocom.swing.MigLayout;
import ptpshared.gui.GuiUtil;
import ptpshared.gui.WizardPage;
import rmlshared.enums.YesNo;
import rmlshared.gui.*;
import rmlshared.io.FileUtil;
import rmlshared.thread.PlugIn;
import crystallography.core.Crystal;
import crystallography.io.CrystalSaver;
import crystallography.io.CrystalUtil;

/**
 * Wizard page to setup the phases.
 * 
 * @author Philippe T. Pinard
 * 
 */
public class PhasesWizardPage extends WizardPage {

    /**
     * Action to add a phase.
     * 
     * @author Philippe T. Pinard
     * 
     */
    private class Add extends PlugIn {
        @Override
        protected void xRun() {
            // Get the selected crystal
            Crystal crystal = (Crystal) existingPhasesList.getSelectedValue();
            if (crystal == null)
                return;

            // Add it to the current list
            DefaultListModel model =
                    (DefaultListModel) currentPhasesList.getModel();
            model.addElement(crystal);
        }

    }



    /**
     * Action to add all phases.
     * 
     * @author Philippe T. Pinard
     * 
     */
    private class AddAll extends PlugIn {
        @Override
        protected void xRun() {
            Object[] items =
                    ((DefaultListModel) existingPhasesList.getModel())
                            .toArray();

            DefaultListModel model =
                    (DefaultListModel) currentPhasesList.getModel();

            for (Object item : items)
                model.addElement(item);
        }

    }



    /**
     * Action to move a phase down one level.
     * 
     * @author Philippe T. Pinard
     * 
     */
    private class Down extends PlugIn {
        @Override
        protected void xRun() {
            // Get the selected crystal
            int index = currentPhasesList.getSelectedIndex();
            if (index < 0)
                return; // If no crystal selected

            DefaultListModel model =
                    (DefaultListModel) currentPhasesList.getModel();
            if (index >= model.size() - 1)
                return; // If last crystal selected

            // Move it down
            Object crystal = model.getElementAt(index);
            model.removeElementAt(index);
            model.insertElementAt(crystal, index + 1);
        }

    }



    /**
     * Action to edit a new phase.
     * 
     * @author Philippe T. Pinard
     * 
     */
    private class Edit extends New {

        @Override
        protected void xRun() {
            // Get the selected crystal
            Crystal crystal = (Crystal) existingPhasesList.getSelectedValue();
            if (crystal == null)
                return;

            NewPhaseDialog dialog = new NewPhaseDialog(crystal);
            if (dialog.show() == NewPhaseDialog.CANCEL)
                return;

            Crystal newCrystal = dialog.getCrystal();

            // Save crystal
            YesNo answer =
                    YesNoDialog.show("Do you want to save the modifications?",
                            YesNo.YES);
            if (answer == YesNo.YES) {
                File file = new File(phasesDirField.getFile(), crystal.name);
                file = FileUtil.setExtension(file, "xml");

                save(newCrystal, file);
                refresh();
            } else {
                // Remove selected crystal
                DefaultListModel model =
                        (DefaultListModel) existingPhasesList.getModel();
                model.removeElementAt(existingPhasesList.getSelectedIndex());

                // Add new crystal to the existing list
                model.addElement(newCrystal);
            }
        }

    }



    /**
     * Action to create a new phase.
     * 
     * @author Philippe T. Pinard
     * 
     */
    private class New extends PlugIn {
        /**
         * Saves the specified crystal with the specify file.
         * 
         * @param crystal
         *            crystal to save
         * @param file
         *            location where to save the crystal
         */
        protected void save(Crystal crystal, File file) {
            if (phasesDirField.getFileBFR() != null) {
                saveAskReplace(crystal, file);
            } else {
                saveNewLocation(crystal);
            }
        }



        /**
         * Saves the crystal even if a file already exists.
         * 
         * @param crystal
         *            crystal to save
         * @param file
         *            location where to save the crystal
         */
        protected void saveAndReplace(Crystal crystal, File file) {
            try {
                new CrystalSaver().save(crystal, file);
            } catch (IOException e) {
                ErrorDialog.show("Cannot save crystal, because: "
                        + e.getMessage());
            }
        }



        /**
         * Saves crystal. Checks if the specified file already exists and asks
         * the user whether to replace it or specify a new location.
         * 
         * @param crystal
         *            crystal to save
         * @param file
         *            location where to save the crystal
         */
        protected void saveAskReplace(Crystal crystal, File file) {
            if (file.exists()) {
                if (YesNoDialog.show("File (" + file.toString()
                        + ") already exists. "
                        + "Do you want to replace the file?", YesNo.YES) == YesNo.YES)
                    saveAndReplace(crystal, file);
                else
                    saveNewLocation(crystal);
            } else {
                saveAndReplace(crystal, file);
            }
        }



        /**
         * Asks the user for a new location where to save the crystal and calls @
         * #saveAndReplace(Crystal, File)} to save the crystal.
         * 
         * @param crystal
         *            crystal to save
         */
        protected void saveNewLocation(Crystal crystal) {
            FileDialog.setFileSelectionMode(FileDialog.DIRECTORIES_ONLY);

            if (FileDialog.showSaveDialog("Phases directory") == FileDialog.CANCEL)
                return;

            File dir = FileDialog.getSelectedFile();
            saveAndReplace(crystal, new File(dir, crystal.name));
        }



        @Override
        protected void xRun() {
            NewPhaseDialog dialog = new NewPhaseDialog();
            if (dialog.show() == NewPhaseDialog.CANCEL)
                return;

            Crystal crystal = dialog.getCrystal();

            // Save crystal
            YesNo answer =
                    YesNoDialog.show("Do you want to save the new crystal?",
                            YesNo.YES);
            if (answer == YesNo.YES) {
                File file = new File(phasesDirField.getFile(), crystal.name);
                file = FileUtil.setExtension(file, "xml");

                save(crystal, file);
                refresh();
            } else {
                // Add it to the user list
                DefaultListModel model =
                        (DefaultListModel) existingPhasesList.getModel();
                model.addElement(crystal);
            }
        }
    }



    /**
     * Listener to change the existing phases based on the phases directory.
     * 
     * @author Philippe T. Pinard
     * 
     */
    private class PhasesDirListener implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
        }



        @Override
        public void mouseEntered(MouseEvent e) {
        }



        @Override
        public void mouseExited(MouseEvent e) {
        }



        @Override
        public void mousePressed(MouseEvent e) {
        }



        @Override
        public void mouseReleased(MouseEvent e) {
            refresh();
        }

    }



    /**
     * Action to remove a phase.
     * 
     * @author Philippe T. Pinard
     * 
     */
    private class Remove extends PlugIn {
        @Override
        protected void xRun() {
            // Get the selected crystal
            Crystal crystal = (Crystal) currentPhasesList.getSelectedValue();
            if (crystal == null)
                return;

            // Remove it from the user list
            DefaultListModel model =
                    (DefaultListModel) currentPhasesList.getModel();
            model.removeElement(crystal);
        }
    }



    /**
     * Removes all phases from the current list.
     * 
     * @author Philippe T. Pinard
     * 
     */
    private class Clear extends PlugIn {
        @Override
        protected void xRun() {
            DefaultListModel model =
                    (DefaultListModel) currentPhasesList.getModel();

            for (Object obj : model.toArray())
                model.removeElement(obj);
        }
    }



    /**
     * Action to move a phase up one level.
     * 
     * @author Philippe T. Pinard
     * 
     */
    private class Up extends PlugIn {
        @Override
        public void xRun() {
            // Get the selected crystal
            int index = currentPhasesList.getSelectedIndex();
            if (index < 0)
                return; // If no crystal selected
            if (index == 0)
                return; // If first crystal selected

            // Move it up
            DefaultListModel model =
                    (DefaultListModel) currentPhasesList.getModel();
            Object crystal = model.getElementAt(index);
            model.removeElementAt(index);
            model.insertElementAt(crystal, index - 1);
        }
    }



    /** Icon for the add button. */
    private static final ImageIcon ADD_ICON =
            GuiUtil.loadIcon("ptpshared/data/icon/list-add_(22x22).png");

    /** Icon for the add all button. */
    private static final ImageIcon ADD_ALL_ICON =
            GuiUtil.loadIcon("ptpshared/data/icon/list-add-all_(22x22).png");

    /** Icon for the remove button. */
    private static final ImageIcon REMOVE_ICON =
            GuiUtil.loadIcon("ptpshared/data/icon/list-remove_(22x22).png");

    /** Icon for the clear button. */
    private static final ImageIcon CLEAR_ICON =
            GuiUtil.loadIcon("ptpshared/data/icon/list-clear_(22x22).png");

    /** Icon for the up button. */
    private static final ImageIcon UP_ICON =
            GuiUtil.loadIcon("ptpshared/data/icon/go-up_(22x22).png");

    /** Icon for the down button. */
    private static final ImageIcon DOWN_ICON =
            GuiUtil.loadIcon("ptpshared/data/icon/go-down_(22x22).png");

    /** Icon for the new button. */
    private static final ImageIcon NEW_ICON =
            GuiUtil.loadIcon("ptpshared/data/icon/document-new_(22x22).png");

    /** Icon for the edit button. */
    private static final ImageIcon EDIT_ICON =
            GuiUtil.loadIcon("ptpshared/data/icon/document-edit_(22x22).png");

    /** Map key for the phases. */
    public static final String KEY_PHASES = "phases";



    /**
     * Returns a description of this step.
     * 
     * @return description
     */
    public static String getDescription() {
        return "Phases";
    }



    /** Field for the directory containing the phases definition. */
    private FileNameField phasesDirField;

    /** Current list of phases. */
    private JList currentPhasesList;

    /** Button to move up one level a phase in the current list. */
    private Button upButton;

    /** Button to move down one level a phase in the current list. */
    private Button downButton;

    /** Button to remove a phase from the current list. */
    private Button removeButton;

    /** Button to remove all phases from the current list. */
    private Button clearButton;

    /** Existing list of phases. */
    private JList existingPhasesList;

    /** Button to add a phase from the existing list to the current list. */
    private Button addButton;

    /** Button to add all phases from the existing list to the current list. */
    private Button addAllButton;

    /** Button to create a new phase. */
    private Button newButton;

    /** Button to edit a phase. */
    private Button editButton;

    /** Minimum number of phases that needs to be defined. */
    private int minimumPhasesCount = 0;



    /**
     * Creates a new <code>PhasesWizardPage</code>.
     */
    public PhasesWizardPage() {
        // Layout
        setLayout(new MigLayout("flowy", "[grow,fill]", "[][grow,fill]"));

        Panel phasesDirectoryPanel =
                new Panel(new MigLayout("", "[][grow,fill]"));

        phasesDirectoryPanel.add("Phases directory");

        phasesDirField = new FileNameField("Phases directory", 25, true);
        phasesDirField.setFileSelectionMode(FileNameField.DIRECTORIES_ONLY);
        phasesDirField.getBrowseButton().addMouseListener(
                new PhasesDirListener());
        phasesDirectoryPanel.add(phasesDirField, "wrap");

        Panel phasesSelectionPanel = new Panel(new MigLayout("", "[][]50[]"));

        phasesSelectionPanel.add("Current phases");

        phasesSelectionPanel.add("Existing phases", "skip, wrap");

        currentPhasesList = new JList(new DefaultListModel());
        JScrollPane listScroller = new JScrollPane(currentPhasesList);
        listScroller.setPreferredSize(new Dimension(150, 100));
        phasesSelectionPanel.add(listScroller, "spany 2, grow, push");

        upButton = new Button(UP_ICON);
        upButton.setToolTipText("Bring selected phase up");
        upButton.setPlugIn(new Up());
        phasesSelectionPanel.add(upButton, "top left");

        existingPhasesList = new JList(new DefaultListModel());
        listScroller = new JScrollPane(existingPhasesList);
        listScroller.setPreferredSize(new Dimension(150, 100));
        phasesSelectionPanel.add(listScroller, "spany 2, grow, push, wrap");

        downButton = new Button(DOWN_ICON);
        downButton.setToolTipText("Bring selected phase down");
        downButton.setPlugIn(new Down());
        phasesSelectionPanel.add(downButton, "bottom left, wrap");

        removeButton = new Button(REMOVE_ICON);
        removeButton.setToolTipText("Remove selected phase");
        removeButton.setPlugIn(new Remove());
        phasesSelectionPanel.add(removeButton, "split 2, align right");

        clearButton = new Button(CLEAR_ICON);
        clearButton.setToolTipText("Remove all phases");
        clearButton.setPlugIn(new Clear());
        phasesSelectionPanel.add(clearButton);

        addButton = new Button(ADD_ICON);
        addButton.setToolTipText("Add selected phase to current list");
        addButton.setPlugIn(new Add());
        phasesSelectionPanel.add(addButton, "skip, split 4, align right");

        addAllButton = new Button(ADD_ALL_ICON);
        addAllButton.setToolTipText("Add all existing phases to current list");
        addAllButton.setPlugIn(new AddAll());
        phasesSelectionPanel.add(addAllButton);

        newButton = new Button(NEW_ICON);
        newButton.setToolTipText("New phase");
        newButton.setPlugIn(new New());
        phasesSelectionPanel.add(newButton);

        editButton = new Button(EDIT_ICON);
        editButton.setToolTipText("Edit selected phase");
        editButton.setPlugIn(new Edit());
        phasesSelectionPanel.add(editButton, "wrap");

        add(phasesDirectoryPanel);
        add(phasesSelectionPanel);
    }



    /**
     * Returns the phases in the current list.
     * 
     * @return selected phases
     */
    private Crystal[] getPhases() {
        DefaultListModel model =
                (DefaultListModel) currentPhasesList.getModel();
        int crystalCount = model.getSize();

        Crystal[] crystals = new Crystal[crystalCount];
        for (int n = 0; n < crystalCount; n++)
            crystals[n] = (Crystal) model.get(n);

        return crystals;
    }



    @Override
    public boolean isCorrect(boolean buffer) {
        int actualPhasesCount =
                ((DefaultListModel) currentPhasesList.getModel()).getSize();
        if (actualPhasesCount < minimumPhasesCount) {
            showErrorDialog("A minimum of " + minimumPhasesCount
                    + " phase(s) is required.");
            return false;
        }

        if (buffer)
            put(KEY_PHASES, getPhases());

        return true;
    }



    /**
     * Refresh the existing phases list.
     */
    private void refresh() {
        DefaultListModel model =
                (DefaultListModel) existingPhasesList.getModel();

        model.clear();

        Crystal[] phases =
                CrystalUtil.listCrystals(phasesDirField.getFileBFR());

        for (Crystal phase : phases)
            model.addElement(phase);
    }



    /**
     * Sets the minimum number of phases to be defined in this wizard page.
     * 
     * @param minimumPhasesCount
     *            minimum number of phases
     */
    protected void setMinimumPhasesCount(int minimumPhasesCount) {
        if (minimumPhasesCount < 0)
            throw new IllegalArgumentException(
                    "The phases count must be greater than 0.");
        this.minimumPhasesCount = minimumPhasesCount;
    }

}
