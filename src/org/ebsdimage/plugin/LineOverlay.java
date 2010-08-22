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

import java.awt.geom.Line2D;
import java.util.ArrayList;

import org.ebsdimage.core.Drawing;
import org.ebsdimage.core.HoughMap;

import rmlimage.core.ByteMap;
import rmlimage.core.Map;
import rmlimage.gui.*;
import rmlshared.geom.LineUtil;
import rmlshared.gui.ComboBox;
import rmlshared.gui.OkCancelDialog;
import rmlshared.gui.Panel;
import rmlshared.gui.ToggleButton;
import rmlshared.gui.mouse.LinePointer;

/**
 * Plug-in to relate the pattern space to the Hough space. The user can move the
 * cursor over the Hough map and see the corresponding line in the pattern map.
 * 
 * @author Marin Lagac&eacute;
 */
public class LineOverlay extends PlugIn {

    /**
     * Dialog to select the pattern map.
     * 
     * @author Marin Lagac&eacute;
     */
    private class Dialog extends BasicDialog {

        /** Combo box to select the pattern map. */
        private ComboBox<Map> comboBox;



        /**
         * Creates a new <code>Dialog</code> to select the pattern map.
         */
        public Dialog() {
            super("Line Overlay");

            Panel panel = new Panel();

            // Create the ByteMaps to link to combo box
            comboBox = new ComboBox<Map>(getByteMapList());
            panel.add("Link to: ", comboBox);

            setMainComponent(panel);
        }



        /**
         * Returns the selected map.
         * 
         * @return selected pattern map
         */
        public ByteMap getMap() {
            return (ByteMap) comboBox.getSelectedItemBFR();
        }

    }

    /**
     * Listener to check for the source window closing.
     * 
     * @author Marin Lagac&eacute;
     */
    private class InternalFrameListener extends
            javax.swing.event.InternalFrameAdapter {
        @Override
        public void internalFrameClosing(javax.swing.event.InternalFrameEvent e) {
            toggleButton.setSelected(false);
        }
    }

    /**
     * Listener to track the mouse motion on the map.
     * 
     * @author Marin Lagac&eacute;
     */
    private class MapMouseMotionListener extends MapMouseMotionAdapter {
        @Override
        public void mouseDragged(Map source, int button, int x, int y) {
            mouseMoved(source, button, x, y);
        }



        @Override
        public void mouseMoved(Map source, int button, int x, int y) {
            assert (source == sourceWindow.getMap()) : "source ("
                    + source.getName() + ") != sourceWindow's map ("
                    + sourceWindow.getMap().getName() + ')';

            if (source instanceof HoughMap) {
                HoughMap houghMap = (HoughMap) source;
                double r = houghMap.getR(x, y);
                double theta = houghMap.getTheta(x, y);

                ByteMap destination = (ByteMap) destinationWindow.getMap();
                Line2D line = Drawing.getLine(destination, r, theta);
                if (LineUtil.isEmpty(line))
                    return; // If does not exist
                pointer.setLocation(line.getX1(), line.getY1(), line.getX2(),
                        line.getY2());
            }

        }

    }

    /**
     * Listener to listen for scrolling and scale changes.
     * 
     * @author Marin Lagac&eacute;
     */
    private class ViewListener extends MapWindowAdapter {
        @Override
        public void scaleChanged(MapWindow source, double scale) {
            destinationWindow.setScale(scale);
        }



        @Override
        public void viewportChanged(MapWindow source, int x, int y) {
            destinationWindow.setViewPosition(x, y, 0, 0);
        }
    }

    /** Button to active the line overlay. */
    private ToggleButton toggleButton;

    /** Map source. */
    private MapWindow sourceWindow;

    /** Map destination. */
    private MapWindow destinationWindow;

    /** Line. */
    private LinePointer pointer;

    /** Listener for the mouse motion in the map. */
    private MapMouseMotionListener mapMouseMotionListener =
            new MapMouseMotionListener();

    /** Listener to listen for scrolling and scale changes. */
    private ViewListener viewListener = new ViewListener();

    /** Listener for the frame closing. */
    private InternalFrameListener internalFrameListener =
            new InternalFrameListener();



    /**
     * Creates a new <code>LineOverlay</code>.
     * 
     * @param toggleButton
     *            button to activate and desactive the line overlay
     */
    public LineOverlay(ToggleButton toggleButton) {
        if (toggleButton == null)
            throw new NullPointerException("toggleButton is null.");
        this.toggleButton = toggleButton;
    }



    /**
     * Returns the number of <code>ByteMap</code> in the GUI. Only the implicit
     * <code>ByteMap</code> are counted, not the maps that extends
     * <code>ByteMap</code>.
     * 
     * @return number of <code>ByteMap</code>
     */
    private int getByteMapCount() {
        return getByteMapList().length;
    }



    /**
     * Returns all the <code>ByteMap</code> in the GUI. Only the implicit
     * <code>ByteMap</code> are returned, not the maps that extends
     * <code>ByteMap</code>.
     * 
     * @return array of <code>ByteMap</code> present in the GUI
     */
    private ByteMap[] getByteMapList() {
        // Get the list of all the ByteMaps on the desktop
        Map[] maps = getMapList();
        ArrayList<ByteMap> byteMaps = new ArrayList<ByteMap>();
        for (Map map : maps)
            if (map.getClass() == ByteMap.class)
                byteMaps.add((ByteMap) map);

        return byteMaps.toArray(new ByteMap[0]);
    }



    /**
     * Links the Hough map (selected map) with the pattern map which is selected
     * in a dialog.
     */
    private void link() {
        if (!areMapsLoaded(true))
            return;

        Map map = getSelectedMap();
        if (!isCorrectType(map, HoughMap.class, true))
            return;

        if (getByteMapCount() < 1) {
            showErrorDialog("At least one ByteMap must be loaded "
                    + "to use the Line Overlay tool.");
            toggleButton.setSelected(false);
            return;
        }

        Dialog dialog = new Dialog();
        if (dialog.show() == OkCancelDialog.CANCEL) {
            toggleButton.setSelected(false);
            return;
        }

        // Get the source and destination maps
        HoughMap source = (HoughMap) map;
        ByteMap destination = dialog.getMap();

        sourceWindow = getWindow(source);
        destinationWindow = getWindow(destination);

        // Set up the pointer
        pointer = new LinePointer();
        pointer.setPanel(destinationWindow.getMapPanel());
        pointer.setVisible(true);

        sourceWindow.addMapMouseMotionListener(mapMouseMotionListener);

        // Set up the view listener to listen for scrolling and scale changes
        sourceWindow.addMapWindowListener(viewListener);

        // Setup the internal frame listener to listen to frame closing
        sourceWindow.addInternalFrameListener(internalFrameListener);
        destinationWindow.addInternalFrameListener(internalFrameListener);
    }



    /**
     * Unlink the Hough map and the pattern map.
     */
    private void unlink() {
        sourceWindow.removeMapMouseMotionListener(mapMouseMotionListener);
        pointer.setVisible(false);
        pointer.removePanel();

        sourceWindow.removeMapWindowListener(viewListener);

        sourceWindow.removeInternalFrameListener(internalFrameListener);
        destinationWindow.removeInternalFrameListener(internalFrameListener);

        sourceWindow = null;
        destinationWindow = null;

    }



    @Override
    public void xRun() {
        if ((toggleButton).isSelected()) // If button selected
            link();
        else
            // If button is deselected
            unlink();
    }

}
