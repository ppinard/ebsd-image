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
package org.ebsdimage.gui.mouse;

import java.awt.Color;

import org.ebsdimage.plugin.LineOverlay;

import rmlimage.core.Map;
import rmlimage.gui.MapPanel;
import rmlshared.gui.Panel;

/**
 * Line pointer used in the line overlay between the Hough map and the pattern
 * map.
 * 
 * @author Marin Lagac&eacute;
 * @see LineOverlay
 */
public class LinePointer extends rmlshared.gui.mouse.LinePointer {

    /**
     * Creates a new <code>LinePointer</code>.
     */
    public LinePointer() {
    }



    /**
     * Creates a new <code>LinePointer</code> with a specific thickness and
     * color.
     * 
     * @param thickness
     *            thickness of the line
     * @param color
     *            color of the line
     */
    public LinePointer(int thickness, Color color) {
        super(thickness, color);
    }



    @Override
    public void setLocation(int x1, int y1, int x2, int y2) {
        MapPanel panel = (MapPanel) getPanel();

        Map map = panel.getMap();
        if (x1 < 0 || x1 >= map.width)
            throw new IllegalArgumentException("x1 (" + x1
                    + ") must be between 0 and " + (map.width - 1));
        if (y1 < 0 || y1 >= map.height)
            throw new IllegalArgumentException("y1 (" + y1
                    + ") must be between 0 and " + (map.height - 1));
        if (x2 < 0 || x2 >= map.width)
            throw new IllegalArgumentException("x2 (" + x2
                    + ") must be between 0 and " + (map.width - 1));
        if (y2 < 0 || y2 >= map.height)
            throw new IllegalArgumentException("y2 (" + y2
                    + ") must be between 0 and " + (map.height - 1));

        // Calculate the panel coordinates of the specified map coordinates
        x1 = panel.getPanelX(x1);
        y1 = panel.getPanelY(y1);
        x2 = panel.getPanelX(x2);
        y2 = panel.getPanelY(y2);

        super.setLocation(x1, y1, x2, y2);
    }



    @Override
    public void setPanel(Panel panel) {
        if (panel instanceof MapPanel)
            setPanel((MapPanel) panel);
        else
            throw new IllegalArgumentException("panel ("
                    + panel.getClass().getName() + ") must be a MapPanel.");
    }



    /**
     * Sets the map panel.
     * 
     * @param panel
     *            map panel
     */
    public void setPanel(MapPanel panel) {
        super.setPanel(panel);
    }

}
