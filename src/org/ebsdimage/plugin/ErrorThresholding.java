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

import org.ebsdimage.core.ErrorCode;
import org.ebsdimage.core.ErrorMap;
import org.ebsdimage.gui.ThresholdPanel;

import rmlimage.core.Map;
import rmlimage.gui.MapWindow;
import rmlimage.plugin.builtin.ManualThresholdingHandler;

/**
 * Thresholding handler to be activated when a user selects to threshold a
 * <code>ErrorMap</code>.
 * 
 * @author Philippe T. Pinard
 */
public class ErrorThresholding implements ManualThresholdingHandler {

    @Override
    public boolean isCorrectType(Map map) {
        return (map instanceof ErrorMap) ? true : false;
    }



    @Override
    public void xRun(MapWindow window) {
        Map map = window.getMap();
        assert (map instanceof ErrorMap) : "Invalid map type: " + map.getName()
                + " = " + map.getClass().getName();

        window.add(new ThresholdPanel<ErrorCode>((ErrorMap) map, "Error"),
                MapWindow.SOUTH);
    }

}
