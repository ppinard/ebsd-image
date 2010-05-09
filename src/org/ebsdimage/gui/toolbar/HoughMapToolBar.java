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

import static rmlimage.core.Constants.MULTI_DESKTOP_ENABLED;

import javax.swing.ImageIcon;
import javax.swing.JToggleButton;

import org.ebsdimage.plugin.LineOverlay;

import rmlimage.gui.toolbar.LinkButton;
import rmlimage.gui.toolbar.MapToolBar;
import rmlimage.gui.toolbar.ZoomButton;
import rmlimage.plugin.PlugIn;
import rmlshared.gui.Button;
import rmlshared.gui.ToggleButton;
import rmlshared.io.FileUtil;
import rmlshared.thread.WorkerThread;

/**
 * Tool bar for Hough Map with the button to toggle the line overlay.
 * 
 * @author Marin Lagac&eacute;
 * @see LineOverlay
 */
public class HoughMapToolBar extends MapToolBar {

    /** Icon for the line overlay. */
    private static final ImageIcon LINE_OVERLAY_ICON =
            new ImageIcon(FileUtil
                    .getURL("org/ebsdimage/plugin/LineOverlay_(16x16).gif"));

    /** Icon for the crop. */
    private static final ImageIcon CROP_ICON =
            new ImageIcon(FileUtil.getURL("rmlimage/gui/Crop_(16x16).gif"));



    /**
     * Creates a new <code>HoughMapToolBar</code>.
     * 
     * @param wThread
     *            worker thread
     */
    public HoughMapToolBar(WorkerThread wThread) {
        JToggleButton tButton = new ZoomButton(wThread);
        addToButtonGroup(tButton);
        add(tButton);

        // Link button available for all map types
        if (rmlimage.RMLImage.getPreferences().getPreference(
                MULTI_DESKTOP_ENABLED, false)) {
            tButton = new LinkButton();
            add(tButton);
        }

        ToggleButton btnLineOverlay = new ToggleButton(LINE_OVERLAY_ICON);
        btnLineOverlay.setName("Line Overlay");
        btnLineOverlay.setToolTipText("Line Overlay");
        btnLineOverlay.setFocusable(false);
        PlugIn plugInLineOverlay =
                new org.ebsdimage.plugin.LineOverlay(btnLineOverlay);
        btnLineOverlay.setPlugIn(plugInLineOverlay, plugInLineOverlay);
        // addToButtonGroup(button);
        add(btnLineOverlay);

        Button btnCrop = new Button(CROP_ICON);
        btnCrop.setName("Crop");
        btnCrop.setToolTipText("Crop");
        btnCrop.setFocusable(false);
        PlugIn plugInCrop = new org.ebsdimage.plugin.HoughCrop();
        btnCrop.setPlugIn(plugInCrop);
        add(btnCrop);

    }

}
