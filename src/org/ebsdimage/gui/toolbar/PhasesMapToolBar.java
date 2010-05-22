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
package org.ebsdimage.gui.toolbar;

import javax.swing.ImageIcon;
import javax.swing.JToggleButton;

import org.ebsdimage.plugin.PhasesInfo;

import ptpshared.gui.GuiUtil;
import rmlimage.gui.toolbar.CropButton;
import rmlimage.gui.toolbar.MapToolBar;
import rmlimage.gui.toolbar.ROIButton;
import rmlimage.gui.toolbar.ZoomButton;
import rmlimage.plugin.PlugIn;
import rmlshared.gui.Button;
import rmlshared.thread.WorkerThread;

/**
 * Tool bar for the <code>PhasesMap</code>.
 * 
 * @author Philippe T. Pinard
 * 
 */
public class PhasesMapToolBar extends MapToolBar {

    /** Icon for the phases info. */
    private static final ImageIcon INFO_ICON =
            GuiUtil.loadIcon("org/ebsdimage/plugin/PhasesInfo_(16x16).png");



    /**
     * Creates a new <code>PhasesMapToolBar</code>.
     * 
     * @param wThread
     *            worker thread
     */
    public PhasesMapToolBar(WorkerThread wThread) {
        Button infoButton = new Button(INFO_ICON);
        infoButton.setName("Info");
        infoButton.setToolTipText("Info");
        infoButton.setFocusable(false);
        PlugIn plugInCrop = new PhasesInfo();
        infoButton.setPlugIn(plugInCrop);
        add(infoButton);

        JToggleButton tButton = new ZoomButton(wThread);
        addToButtonGroup(tButton);
        add(tButton);

        tButton = new ROIButton(wThread);
        addToButtonGroup(tButton);
        add(tButton);

        Button button = new CropButton(wThread);
        add(button);
    }

}
