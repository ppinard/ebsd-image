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
package org.ebsdimage.plugin;

import java.io.File;
import java.io.IOException;

import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.ebsdimage.io.SmpInputStream;

import ptpshared.gui.SingleFileBrowserField;
import rmlimage.core.ByteMap;
import rmlimage.gui.BasicDialog;
import rmlimage.gui.PlugIn;
import rmlshared.cui.ErrorDialog;
import rmlshared.gui.ColumnPanel;
import rmlshared.gui.IntField;
import rmlshared.gui.OkCancelDialog;
import rmlshared.gui.Panel;
import edu.umd.cs.findbugs.annotations.CheckForNull;

/**
 * Plug-in to load a pattern from a SMP file.
 * 
 * @author Philippe T. Pinard
 */
public class SmpViewer extends PlugIn {

    /**
     * Dialog to select the SMP file and the index of the pattern to load.
     * 
     * @author Philippe T. Pinard
     */
    private class Dialog extends BasicDialog {

        /** File name browser for the SMP file. */
        private SingleFileBrowserField fileField;

        /** Index number. */
        private IntField indexField;



        /**
         * Creates a new dialog.
         */
        public Dialog() {
            super("Smp viewer");

            Panel panel = new ColumnPanel(2);

            panel.add("SMP file");
            FileFilter[] filters =
                    new FileFilter[] { new FileNameExtensionFilter(
                            "SMP file  (*.smp)", "smp") };
            fileField = new SingleFileBrowserField("SMP file", true, filters);
            panel.add(fileField);

            panel.add("Pattern index");
            indexField = new IntField("Pattern index", 0);
            indexField.setRange(0, Integer.MAX_VALUE);
            panel.add(indexField);

            setMainComponent(panel);

            setPreferences(getPlugIn().getPreferences());
        }



        /**
         * Returns the index of the pattern.
         * 
         * @return index of the pattern
         */
        public int getIndex() {
            return indexField.getValueBFR();
        }



        /**
         * Returns the selected smp file.
         * 
         * @return smp file
         */
        public File getSmpFile() {
            return fileField.getFileBFR();
        }

    }



    /**
     * Loads a pattern from the smp file. A dialog is displayed to select the
     * SMP file and the index of the pattern.
     * 
     * @return the resultant <code>RGBMap</code>
     * @throws IOException
     *             if an error occurs while loading the pattern
     */
    @CheckForNull
    private ByteMap getPattern() throws IOException {
        Dialog dialog = new Dialog();

        if (dialog.show() == OkCancelDialog.CANCEL)
            return null;

        File smpFile = dialog.getSmpFile();
        if (smpFile == null) {
            ErrorDialog.show("No smp file specified");
            return null;
        }

        SmpInputStream reader = new SmpInputStream(smpFile);

        int index = dialog.getIndex();
        ByteMap map = (ByteMap) reader.readMap(index);

        add(map);

        return map;
    }



    @Override
    protected void xRun() throws Exception {
        getPattern();
    }

}
