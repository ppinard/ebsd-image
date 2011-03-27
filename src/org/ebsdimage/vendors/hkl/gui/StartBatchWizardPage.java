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
package org.ebsdimage.vendors.hkl.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.io.File;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import net.miginfocom.swing.MigLayout;

import org.ebsdimage.vendors.hkl.io.CprLoader;
import org.ebsdimage.vendors.hkl.io.CtfLoader;

import ptpshared.gui.GuiUtil;
import ptpshared.gui.WizardPage;
import rmlshared.gui.Button;
import rmlshared.gui.CheckBox;
import rmlshared.io.FileUtil;
import rmlshared.thread.PlugIn;

/**
 * First page for the import wizard.
 * 
 * @author Philippe T. Pinard
 */
public class StartBatchWizardPage extends WizardPage {

    /**
     * Action to add CPR(s) files.
     * 
     * @author Philippe T. Pinard
     */
    private class Add extends PlugIn {
        @Override
        public void xRun() {
            // Show the file dialog
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fileChooser.setMultiSelectionEnabled(true);
            fileChooser.setAcceptAllFileFilterUsed(false);
            FileFilter filter =
                    new FileNameExtensionFilter(
                            "Channel 5 project file (*.cpr)", "cpr");
            fileChooser.addChoosableFileFilter(filter);

            Component parent = rmlshared.util.SystemUtil.getSelectedFrame();
            if (fileChooser.showOpenDialog(parent) != JFileChooser.APPROVE_OPTION)
                return;
            File[] files = fileChooser.getSelectedFiles();

            // Add file(s) to the list
            DefaultListModel model = (DefaultListModel) fileList.getModel();
            File ctfFile;
            for (File cprFile : files) {
                // Verify CPR
                if (!new CprLoader().canLoad(cprFile)) {
                    showErrorDialog("The CPR file (" + cprFile
                            + ") is not valid.");
                    continue;
                }

                // Verify CTF
                ctfFile = FileUtil.setExtension(cprFile, "ctf");
                if (!ctfFile.exists()) {
                    showErrorDialog("No CTF file (" + ctfFile
                            + ") for the specified CPR file.");
                    continue;
                }

                if (!new CtfLoader().canLoad(ctfFile)) {
                    showErrorDialog("The CTF file (" + ctfFile
                            + ") is not valid.");
                    continue;
                }

                model.addElement(cprFile);
            }
        }
    }

    /**
     * Action to clear all CPR files.
     * 
     * @author Philippe T. Pinard
     */
    private class Clear extends PlugIn {
        @Override
        public void xRun() {
            DefaultListModel model = (DefaultListModel) fileList.getModel();

            for (Object obj : model.toArray())
                model.removeElement(obj);
        }
    }

    /**
     * Action to remove a CPR file.
     * 
     * @author Philippe T. Pinard
     */
    private class Remove extends PlugIn {
        @Override
        public void xRun() {
            // Get the selected operation
            Object obj = fileList.getSelectedValue();
            if (obj == null)
                return;

            // Remove it from the user list
            DefaultListModel model = (DefaultListModel) fileList.getModel();
            model.removeElement(obj);
        }
    }

    /** Icon for the add button. */
    private static final ImageIcon ADD_ICON =
            GuiUtil.loadIcon("ptpshared/data/icon/list-add_(22x22).png");

    /** Icon for the clear button. */
    private static final ImageIcon CLEAR_ICON =
            GuiUtil.loadIcon("ptpshared/data/icon/list-clear_(22x22).png");

    /** Map key for the CPR files. */
    public static final String KEY_CPR_FILES = "cprFiles";

    /** Map key whether to import diffraction patterns to SMP file. */
    public static final String KEY_IMPORT_PATTERNS = "importPatterns";

    /** Icon for the remove button. */
    private static final ImageIcon REMOVE_ICON =
            GuiUtil.loadIcon("ptpshared/data/icon/list-remove_(22x22).png");



    /**
     * Returns a description of this page.
     * 
     * @return description
     */
    public static String getDescription() {
        return "Start";
    }

    /** Add CPR button. */
    private Button addButton;

    /** Clear CPR button. */
    private Button clearButton;

    /** List of selected CPR files. */
    private JList fileList;

    /** Delete CPR button. */
    private Button removeButton;

    /** Check box whether to import the diffraction patterns. */
    private CheckBox patternCBox;



    /**
     * Creates a new <code>StartBatchWizardPage</code>.
     */
    public StartBatchWizardPage() {

        setLayout(new MigLayout());

        String text =
                "<html>This importer converts the acquisition parameters "
                        + "and data from a list of CPR/CTF files into EBSD-Image's EBSD multimap "
                        + "format. The user can select whether if wants to import "
                        + "the diffraction patterns of the acquisition to a smp file."
                        + "The imported ctf files must share the same acquisition parameters "
                        + "and the same phases."
                        + "<br/><br/>"
                        + "For more information, please visit: http://ebsd-image.org"
                        + "</html>";
        add(new JLabel(text), "grow, wrap 40");

        add(new JLabel("Select the CPR files"), "wrap");
        fileList = new JList(new DefaultListModel());
        JScrollPane listScroller = new JScrollPane(fileList);
        listScroller.setPreferredSize(new Dimension(150, 100));
        add(listScroller, "grow, wrap");

        addButton = new Button(ADD_ICON);
        addButton.setToolTipText("Add CPR(s) to the list");
        addButton.setPlugIn(new Add());
        add(addButton, "split 3, align right");

        removeButton = new Button(REMOVE_ICON);
        removeButton.setToolTipText("Remove selected CPR from the list");
        removeButton.setPlugIn(new Remove());
        add(removeButton);

        clearButton = new Button(CLEAR_ICON);
        clearButton.setToolTipText("Clear all CPR(s) from the list");
        clearButton.setPlugIn(new Clear());
        add(clearButton, "wrap 20");

        patternCBox = new CheckBox("Import diffraction patterns");
        add(patternCBox);
    }



    /**
     * Returns the CTF files defined in the list.
     * 
     * @return an array of files
     */
    private File[] getFiles() {
        DefaultListModel model = (DefaultListModel) fileList.getModel();
        int nbOps = model.getSize();

        File[] files = new File[nbOps];
        for (int n = 0; n < nbOps; n++)
            files[n] = (File) model.get(n);

        return files;
    }



    @Override
    public boolean isCorrect(boolean buffer) {
        if (fileList.getModel().getSize() == 0) {
            showErrorDialog("Please specify at least one CPR file.");
            return false;
        }

        if (buffer) {
            put(KEY_CPR_FILES, getFiles());
            put(KEY_IMPORT_PATTERNS, patternCBox.isSelected());
        }

        return true;
    }
}
