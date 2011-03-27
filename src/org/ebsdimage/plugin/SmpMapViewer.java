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

import javax.swing.JLabel;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import net.miginfocom.swing.MigLayout;

import org.ebsdimage.io.SmpInputStream;

import ptpshared.gui.SingleFileBrowserField;
import rmlimage.core.ByteMap;
import rmlimage.core.Map;
import rmlimage.core.RenderableMap;
import rmlimage.gui.BasicDialog;
import rmlimage.gui.MapWindow;
import rmlimage.gui.PlugIn;
import rmlshared.gui.ComboBox;
import rmlshared.gui.OkCancelDialog;
import rmlshared.gui.Panel;

/**
 * Interactive viewer of diffraction patterns stored in a SMP file. The plug-in
 * links an EBSD map with a SMP file. When the user moves the mouse over the
 * EBSD map, the diffraction pattern of the hovering pixel is shown.
 * 
 * @author Philippe T. Pinard
 */
public class SmpMapViewer extends PlugIn {

    /**
     * Dialog to select the pattern map.
     * 
     * @author Marin Lagac&eacute;
     */
    private class Dialog extends BasicDialog {

        /** Combo box to select the pattern map. */
        private ComboBox<Map> comboBox;

        /** File name browser for the SMP file. */
        private SingleFileBrowserField smpFileField;



        /**
         * Creates a new <code>Dialog</code> to select the pattern map.
         */
        public Dialog() {
            super("Link SMP to map");

            Panel panel = new Panel(new MigLayout());

            panel.add(new JLabel("Source map"));
            comboBox = new ComboBox<Map>(getMapList(RenderableMap.class));
            panel.add(comboBox, "wrap");

            panel.add("SMP file");
            FileFilter[] filters =
                    new FileFilter[] { new FileNameExtensionFilter(
                            "SMP file  (*.smp)", "smp") };
            smpFileField =
                    new SingleFileBrowserField("SMP file", true, filters);
            panel.add(smpFileField, "pushx, growx, wrap");

            panel.add(new JLabel(
                    "Note: It may take a few seconds for the first "
                            + "diffraction pattern to appear"), "span");

            setMainComponent(panel);
        }



        /**
         * Returns the selected map.
         * 
         * @return selected pattern map
         */
        public Map getMap() {
            return comboBox.getSelectedItemBFR();
        }



        /**
         * Returns the selected SMP file.
         * 
         * @return SMP file
         */
        public File getSmpFile() {
            return smpFileField.getFileBFR();
        }



        @Override
        public boolean isCorrect() {
            if (!super.isCorrect())
                return false;

            if (!smpFileField.isCorrect())
                return false;

            Map selectedMap = comboBox.getSelectedItem();
            int mapCount;
            try {
                SmpInputStream smpReader =
                        new SmpInputStream(smpFileField.getFile());
                mapCount = smpReader.getMapCount();
                smpReader.close();
            } catch (IOException e) {
                showErrorDialog(e.getMessage());
                return false;
            }

            if (selectedMap.size != mapCount) {
                showErrorDialog("The number of pixels in the selected map ("
                        + selectedMap.size
                        + ") is different from the number of diffraction "
                        + "patterns in the SMP file (" + mapCount + ")");
                return false;
            }

            return true;
        }

    }

    /**
     * Listener to check for the map or SMP window are closing.
     * 
     * @author Philippe T. Pinard
     */
    private class InternalFrameListener extends
            javax.swing.event.InternalFrameAdapter {
        @Override
        public void internalFrameClosing(javax.swing.event.InternalFrameEvent e) {
            mapWindow.removeMapMouseMotionListener(mapMouseMotionListener);

            try {
                smpReader.close();
            } catch (IOException e1) {
                showErrorDialog(e1.getMessage());
            }
        }
    }

    /**
     * Listener of the mouse motion over the EBSD map window.
     * 
     * @author Philippe T. Pinard
     */
    private class MapMouseMotionListener implements
            rmlimage.gui.MapMouseMotionListener {

        @Override
        public void mouseDragged(Map source, int button, int x, int y) {
        }



        @Override
        public void mouseMoved(Map source, int button, int x, int y) {
            int index = source.getPixArrayIndex(x, y);

            try {
                smpReader.readMap(index, smpMap);
            } catch (IOException e) {
                smpMap.clear();
            }

            smpMap.setChanged(Map.MAP_CHANGED);
            smpMap.notifyListeners();
            smpMap.shouldSave(false);
        }

    }

    /** Reader of the SMP file. */
    SmpInputStream smpReader;

    /** Listener of the mouse motion in the map window. */
    private MapMouseMotionListener mapMouseMotionListener =
            new MapMouseMotionListener();

    /** Window of the EBSD map. */
    private MapWindow mapWindow;

    /** Window where the diffraction patterns are displayed. */
    private MapWindow smpWindow;

    /** Map to store the diffraction patterns. */
    private ByteMap smpMap;

    /** Listener for the frame closing. */
    private InternalFrameListener internalFrameListener =
            new InternalFrameListener();



    @Override
    protected void xRun() throws Exception {
        if (getMapCount(RenderableMap.class) == 0) {
            showErrorDialog("No maps loaded");
            return;
        }

        Dialog dialog = new Dialog();
        if (dialog.show() != OkCancelDialog.OK)
            return;

        // SMP window
        File smpFile = dialog.getSmpFile();
        smpReader = new SmpInputStream(smpFile);

        smpMap = new ByteMap(smpReader.getMapWidth(), smpReader.getMapHeight());
        smpMap.setName("SMP viewer");
        smpMap.shouldSave(false);
        add(smpMap);

        smpWindow = getWindow(smpMap);
        smpWindow.addInternalFrameListener(internalFrameListener);
        smpWindow.pack();

        // Map window
        Map selectedMap = dialog.getMap();
        mapWindow = getWindow(selectedMap);
        mapWindow.addMapMouseMotionListener(mapMouseMotionListener);
        mapWindow.addInternalFrameListener(internalFrameListener);
    }
}
