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
package org.ebsdimage.plugin;

import java.io.File;
import java.io.IOException;

import org.ebsdimage.io.SmpCreator;

import rmlimage.plugin.PlugIn;
import rmlshared.enums.YesNo;
import rmlshared.gui.FileSelectionMode;
import rmlshared.ui.Monitorable;

/**
 * Plug-in to create a SMP file from images in a directory.
 * 
 * @author Philippe T. Pinard
 */
public class SmpCreation extends PlugIn implements Monitorable {

    /** Smp creator. */
    private SmpCreator smpCreator;



    @Override
    public double getTaskProgress() {
        if (smpCreator != null)
            return smpCreator.getTaskProgress();
        else
            return 0;
    }



    @Override
    public String getTaskStatus() {
        if (smpCreator != null)
            return smpCreator.getTaskStatus();
        else
            return "";
    }



    @Override
    public void xRun() throws IOException {
        rmlshared.gui.FileDialog
                .setFileSelectionMode(FileSelectionMode.DIRECTORIES_ONLY);
        if (rmlshared.gui.FileDialog
                .showOpenDialog("Select directory holding files to process") == rmlshared.gui.FileDialog.CANCEL)
            return;
        File directory = rmlshared.gui.FileDialog.getSelectedFile();

        rmlshared.gui.FileDialog.setFilter("*.smp");
        if (rmlshared.gui.FileDialog.showSaveDialog("Specify SMP file name") == rmlshared.gui.FileDialog.CANCEL)
            return;
        File smpFile = rmlshared.gui.FileDialog.getSelectedFile();

        if (smpFile.exists()) {
            YesNo answer =
                    showQuestionDialog("Smp Creation", smpFile.getPath()
                            + " already exists.\nDo you want to overwrite it?",
                            YesNo.NO);
            if (answer == YesNo.NO)
                return;
        }

        smpCreator = new SmpCreator();

        smpCreator.create(smpFile, directory);

        showMessageDialog("Smp Creation", "Creation of " + smpFile.getName()
                + " done.");
    }

}
