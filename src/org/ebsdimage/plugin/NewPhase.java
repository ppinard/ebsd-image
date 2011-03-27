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

import javax.swing.JFileChooser;

import org.ebsdimage.gui.NewPhaseDialog;

import ptpshared.util.simplexml.ApacheCommonMathMatcher;
import ptpshared.util.simplexml.XmlSaver;
import rmlimage.gui.PlugIn;
import rmlimage.gui.RMLImage;
import rmlshared.gui.OkCancelDialog;
import rmlshared.io.FileUtil;
import rmlshared.io.WildcardFileFilter;
import crystallography.core.Crystal;
import crystallography.io.simplexml.SpaceGroupMatcher;

/**
 * Plug-in to create a new phase.
 * 
 * @author Philippe T. Pinard
 */
public class NewPhase extends PlugIn {

    @Override
    protected void xRun() throws Exception {
        // Create new phase
        NewPhaseDialog dialog = new NewPhaseDialog();

        if (dialog.show() != OkCancelDialog.OK)
            return;

        Crystal phase = dialog.getCrystal();

        // Location where to save the phase
        String currentDirectory =
                getPreferences().getPreference("FileDialog.path", null);
        JFileChooser fileChooser = new JFileChooser(currentDirectory);
        fileChooser.setFileFilter(new WildcardFileFilter("*.xml"));
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        if (fileChooser.showSaveDialog(RMLImage.getSelectedFrame()) != JFileChooser.APPROVE_OPTION)
            return;

        File file = fileChooser.getSelectedFile();
        file = FileUtil.setExtension(file, "xml");

        // Save
        XmlSaver saver = new XmlSaver();
        saver.matchers.registerMatcher(new ApacheCommonMathMatcher());
        saver.matchers.registerMatcher(new SpaceGroupMatcher());

        saver.save(phase, file);
    }
}
