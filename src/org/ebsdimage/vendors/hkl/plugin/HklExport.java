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
package org.ebsdimage.vendors.hkl.plugin;

import java.io.File;
import java.io.IOException;

import org.ebsdimage.vendors.hkl.core.HklMMap;
import org.ebsdimage.vendors.hkl.io.CtfSaver;

import rmlimage.core.Map;
import rmlimage.gui.BasicDialog;
import rmlimage.plugin.PlugIn;
import rmlshared.gui.*;
import rmlshared.io.FileUtil;
import rmlshared.ui.Monitorable;

/**
 * Export a HKL multimap to a ctf file.
 * 
 * @author Philippe T. Pinard
 */
public class HklExport extends PlugIn implements Monitorable {

    /**
     * Dialog to select an HKL multimap from those loaded in the GUI.
     * 
     * @author Philippe T. Pinard
     */
    private class Dialog extends BasicDialog {

        /** Combo box to select the HKL multimap. */
        private ComboBox<Map> mmapCBox;



        /**
         * Creates a new dialog to select an HKL multimap.
         */
        public Dialog() {
            super("EBSD Multimap Selector");

            Panel panel = new ColumnPanel(2);

            panel.add("Select HKL multimap:");

            Map[] maps = getMapList(HklMMap.class);
            mmapCBox = new ComboBox<Map>(maps);
            panel.add(mmapCBox);

            setMainComponent(panel);
        }



        /**
         * Returns the selected multimap.
         * 
         * @return multimap
         */
        public HklMMap getSelectedMap() {
            return (HklMMap) mmapCBox.getSelectedItem();
        }

    }

    /** Saver for the ctf file. */
    private CtfSaver ctfSaver;

    /** Progress status. */
    private String status;



    /**
     * Creates a new <code>HklExport</code> plug-in.
     */
    public HklExport() {
        setInterruptable(true);
    }



    /**
     * Displays a dialog and performs the export of an HKL multimap to a ctf
     * file.
     */
    private void doExport() {
        if (!areMapsLoaded(true))
            return;

        // Ask which HKL multimap to save
        Dialog dialog = new Dialog();
        if (dialog.show() == OkCancelDialog.CANCEL)
            return;

        // Select multimap
        HklMMap mmap = dialog.getSelectedMap();

        // Ask where to save ctf
        FileDialog.setFileSelectionMode(FileDialog.FILES_ONLY);
        FileDialog.setMultipleSelection(false);
        FileDialog.addFilter("*.ctf");
        FileDialog.setFilter("*.ctf");
        FileDialog.setSelectedFile(FileUtil.setExtension(mmap.getFile(), "ctf"));
        FileDialog.setTitle("Saving ctf file");

        if (FileDialog.showSaveDialog() == FileDialog.CANCEL)
            return;

        File ctfFile = FileDialog.getSelectedFile();

        ctfSaver = new CtfSaver();
        status = "Saving ctf file.";

        try {
            ctfSaver.save(mmap, ctfFile);
        } catch (IOException e) {
            showErrorDialog("While saving the ctf:" + e.getMessage());
        }

        status = "Completed.";

        ctfSaver = null;

        // Saving patterns
        // TODO: Implement saving patterns of smp to single files
    }



    @Override
    public double getTaskProgress() {
        if (ctfSaver != null)
            return ctfSaver.getTaskProgress();
        else
            return 0;
    }



    @Override
    public String getTaskStatus() {
        return status;
    }



    @Override
    public void interrupt() {
        super.interrupt();
    }



    @Override
    protected void xRun() throws Exception {
        doExport();
    }

}
