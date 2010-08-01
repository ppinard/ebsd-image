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

import org.ebsdimage.io.SmpInputStream;

import rmlimage.core.ByteMap;
import rmlimage.gui.BasicDialog;
import rmlimage.gui.PlugIn;
import rmlshared.cui.ErrorDialog;
import rmlshared.gui.ColumnPanel;
import rmlshared.gui.FileNameField;
import rmlshared.gui.IntField;
import rmlshared.gui.Panel;
import edu.umd.cs.findbugs.annotations.CheckForNull;

/**
 * Plug-in to convert the Euler maps into a RGB Eulers map.
 * 
 * @author Philippe T. Pinard
 */
public class SmpViewer extends PlugIn {

    /**
     * Dialog to select an EBSD multimap from those loaded in the GUI.
     * 
     * @author Philippe T. Pinard
     */
    private class Dialog extends BasicDialog {

        /** File name browswer for the smp file. */
        private FileNameField smpFilenameField;

        /** Index number. */
        private IntField indexField;



        /**
         * Creates a new dialog to select an EBSD multimap.
         */
        public Dialog() {
            super("Smp viewer");

            Panel panel = new ColumnPanel(2);

            panel.add("Smp file");
            smpFilenameField = new FileNameField("Smp file", true);
            smpFilenameField.setFileFilter("*.smp");
            panel.add(smpFilenameField);

            panel.add("Pattern index");
            indexField = new IntField("Pattern index", 0);
            indexField.setRange(0, Integer.MAX_VALUE);
            panel.add(indexField);

            setMainComponent(panel);
        }



        /**
         * Returns the selected smp file.
         * 
         * @return smp file
         */
        public File getSmpFile() {
            return smpFilenameField.getFileBFR();
        }



        /**
         * Returns the index of the pattern.
         * 
         * @return
         */
        public int getIndex() {
            return indexField.getValueBFR();
        }

    }



    /**
     * Performs the conversion from an <code>EbsdMMap</code> to a
     * <code>RGBMap</code> and adds the later in the multimap.
     * 
     * @return the resultant <code>RGBMap</code>
     * @throws IOException
     */
    @CheckForNull
    private ByteMap getPattern() throws IOException {
        Dialog dialog = new Dialog();

        if (dialog.show() == Dialog.CANCEL)
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
