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
import ptpshared.gui.DirBrowserField;
import ptpshared.gui.GuiUtil;
import ptpshared.gui.WizardPage;
import rmlshared.gui.Button;
import rmlshared.gui.ErrorDialog;
import rmlshared.gui.FileDialog;
import rmlshared.gui.FileSelectionMode;
import rmlshared.gui.OkCancelDialog;
import rmlshared.gui.Panel;
import rmlshared.io.FileUtil;
import rmlshared.thread.PlugIn;
import crystallography.core.Crystal;
import crystallography.io.CifLoader;
import crystallography.io.CrystalSaver;
import crystallography.io.CrystalUtil;

/**
 * Wizard page to setup the phases.
 * 
 * @author Philippe T. Pinard
 */
public class PhasesWizardPage extends WizardPage {

    /**
     * Action to add a phase.
     * 
     * @author Philippe T. Pinard
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
     * Removes all phases from the current list.
     * 
     * @author Philippe T. Pinard
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
     * Action to move a phase down one level.
     * 
     * @author Philippe T. Pinard
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
     */
    private class Edit extends New {
        @Override
        protected void save(Crystal crystal) {
            File crystalFile = new File(phasesDirField.getDir(), crystal.name);
            crystalFile = FileUtil.setExtension(crystalFile, "xml");

            try {
                new CrystalSaver().save(crystal, crystalFile);
            } catch (IOException e) {
                ErrorDialog
                        .show("This error occured while saving the XML file for the phase: "
                                + e.getMessage());
                return;
            }

            refresh();
        }



        @Override
        protected void xRun() {
            if (!checkPhaseDir())
                return;

            // Get the selected crystal
            Crystal crystal = (Crystal) existingPhasesList.getSelectedValue();
            if (crystal == null)
                return;

            NewPhaseDialog dialog = new NewPhaseDialog(crystal);
            if (dialog.show() == OkCancelDialog.CANCEL)
                return;

            Crystal newCrystal = dialog.getCrystal();

            // Save crystal
            save(newCrystal);
        }

    }

    /**
     * Action to import a phase from a cif file.
     * 
     * @author Philippe T. Pinard
     */
    private class Import extends New {
        @Override
        protected void xRun() {
            if (!checkPhaseDir())
                return;

            // Get cif file
            FileDialog.setFilter(FileDialog.addFilter("*.cif"));
            FileDialog.setMultipleSelection(false);
            FileDialog.setTitle("Select cif file");
            FileDialog.setFileSelectionMode(FileSelectionMode.FILES_ONLY);

            if (FileDialog.showOpenDialog() == FileDialog.CANCEL)
                return;

            File file = FileDialog.getSelectedFile();

            // Load cif
            Crystal crystal;
            try {
                crystal = new CifLoader().load(file);
            } catch (IOException e) {
                ErrorDialog.show(e.getMessage());
                return;
            }

            // Open new phase dialog to check crystal
            NewPhaseDialog dialog = new NewPhaseDialog(crystal);

            if (dialog.show() == OkCancelDialog.CANCEL)
                return;

            crystal = dialog.getCrystal();

            // Save crystal
            save(crystal);
        }

    }

    /**
     * Action to create a new phase.
     * 
     * @author Philippe T. Pinard
     */
    private class New extends PlugIn {

        /**
         * Checks if the crystal file already exists in the phases' directory.
         * 
         * @param crystalFile
         *            crystal file to be saved
         * @return <code>true</code> if the crystal file can be saved,
         *         <code>false</code> if it cannot be saved (i.e. the phases'
         *         directory already contains a file with the same name
         */
        protected boolean checkPhaseAlreadyExists(File crystalFile) {
            if (crystalFile.exists()) {
                ErrorDialog
                        .show("The file ("
                                + crystalFile.getAbsolutePath()
                                + ") already exists. Please choose a different name for the crystal.");
                return false;
            } else
                return true;
        }



        /**
         * Checks if the phases' directory is specified.
         * 
         * @return <code>true</code> if it is specified, <code>false</code>
         *         otherwise
         */
        protected boolean checkPhaseDir() {
            if (phasesDirField.getDir() == null) {
                ErrorDialog.show("Please select a phases' directory.");
                return false;
            } else
                return true;
        }



        /**
         * Run the plugin:
         * <ul>
         * <li>Check if the phases' directory is defined</li>
         * <li>Display the new phase dialog</li>
         * <li>Saves the crystal.</li>
         * </ul>
         * If a crystal is specified the phase dialog fields are filled with the
         * values of the crystal
         * 
         * @param crystal
         *            crystal to be displayed in the phase dialog. Can be null
         */
        protected void run(Crystal crystal) {
            if (!checkPhaseDir())
                return;

            // Create crystal
            NewPhaseDialog dialog;
            if (crystal == null)
                dialog = new NewPhaseDialog();
            else
                dialog = new NewPhaseDialog(crystal);

            if (dialog.show() == OkCancelDialog.CANCEL)
                return;

            crystal = dialog.getCrystal();

            // Save crystal
            save(crystal);
        }



        /**
         * Saves the crystal in the phases' directory. The method makes sure
         * that the crystal file doesn't already exists. If so, the phase dialog
         * is displayed again to allow the user to change the name of the
         * crystal.
         * 
         * @param crystal
         *            crystal to save
         */
        protected void save(Crystal crystal) {
            File crystalFile = new File(phasesDirField.getDir(), crystal.name);
            crystalFile = FileUtil.setExtension(crystalFile, "xml");

            if (!checkPhaseAlreadyExists(crystalFile)) {
                run(crystal);
                return;
            }

            try {
                new CrystalSaver().save(crystal, crystalFile);
            } catch (IOException e) {
                ErrorDialog
                        .show("This error occured while saving the XML file for the phase: "
                                + e.getMessage());
                return;
            }

            refresh();
        }



        @Override
        protected void xRun() {
            run(null);
        }
    }

    /**
     * Listener to change the existing phases based on the phases directory.
     * 
     * @author Philippe T. Pinard
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
     * Action to move a phase up one level.
     * 
     * @author Philippe T. Pinard
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

    /** Icon for the add all button. */
    private static final ImageIcon ADD_ALL_ICON = GuiUtil
            .loadIcon("ptpshared/data/icon/list-add-all_(22x22).png");

    /** Icon for the add button. */
    private static final ImageIcon ADD_ICON = GuiUtil
            .loadIcon("ptpshared/data/icon/list-add_(22x22).png");

    /** Icon for the clear button. */
    private static final ImageIcon CLEAR_ICON = GuiUtil
            .loadIcon("ptpshared/data/icon/list-clear_(22x22).png");

    /** Icon for the down button. */
    private static final ImageIcon DOWN_ICON = GuiUtil
            .loadIcon("ptpshared/data/icon/go-down_(22x22).png");

    /** Icon for the edit button. */
    private static final ImageIcon EDIT_ICON = GuiUtil
            .loadIcon("ptpshared/data/icon/document-edit_(22x22).png");

    /** Icon for the import button. */
    private static final ImageIcon IMPORT_ICON = GuiUtil
            .loadIcon("ptpshared/data/icon/import-generic_(22x22).png");

    /** Map key for the phases. */
    public static final String KEY_PHASES = "phases";

    /** Icon for the new button. */
    private static final ImageIcon NEW_ICON = GuiUtil
            .loadIcon("ptpshared/data/icon/document-new_(22x22).png");

    /** Icon for the remove button. */
    private static final ImageIcon REMOVE_ICON = GuiUtil
            .loadIcon("ptpshared/data/icon/list-remove_(22x22).png");

    /** Icon for the up button. */
    private static final ImageIcon UP_ICON = GuiUtil
            .loadIcon("ptpshared/data/icon/go-up_(22x22).png");



    /**
     * Returns a description of this step.
     * 
     * @return description
     */
    public static String getDescription() {
        return "Phases";
    }

    /** Button to add all phases from the existing list to the current list. */
    private Button addAllButton;

    /** Button to add a phase from the existing list to the current list. */
    private Button addButton;

    /** Button to remove all phases from the current list. */
    private Button clearButton;

    /** Current list of phases. */
    private JList currentPhasesList;

    /** Button to move down one level a phase in the current list. */
    private Button downButton;

    /** Button to edit a phase. */
    private Button editButton;

    /** Existing list of phases. */
    private JList existingPhasesList;

    /** Button to import a phase from a cif file. */
    private Button importButton;

    /** Minimum number of phases that needs to be defined. */
    private int minimumPhasesCount = 0;

    /** Button to create a new phase. */
    private Button newButton;

    /** Field for the directory containing the phases definition. */
    private DirBrowserField phasesDirField;

    /** Button to remove a phase from the current list. */
    private Button removeButton;

    /** Button to move up one level a phase in the current list. */
    private Button upButton;



    /**
     * Creates a new <code>PhasesWizardPage</code>.
     */
    public PhasesWizardPage() {
        // Layout
        setLayout(new MigLayout("flowy", "[grow,fill]", "[][grow,fill]"));

        Panel phasesDirectoryPanel =
                new Panel(new MigLayout("", "[][grow,fill]"));

        phasesDirectoryPanel.add("Phases directory");

        phasesDirField = new DirBrowserField("Phases directory", true);
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
        phasesSelectionPanel.add(addButton, "skip, split 5, align right");

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
        phasesSelectionPanel.add(editButton);

        importButton = new Button(IMPORT_ICON);
        importButton.setToolTipText("Import a phase from a cif file");
        importButton.setPlugIn(new Import());
        phasesSelectionPanel.add(importButton, "wrap");

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

        Crystal[] phases;
        try {
            phases = CrystalUtil.listCrystals(phasesDirField.getDirBFR());
        } catch (IOException e) {
            ErrorDialog.show(e.getMessage());
            return;
        }

        for (Crystal phase : phases)
            model.addElement(phase);
    }



    @Override
    protected void renderingPage() {
        refresh();
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
