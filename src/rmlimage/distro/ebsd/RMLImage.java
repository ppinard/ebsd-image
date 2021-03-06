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
package rmlimage.distro.ebsd;

import java.awt.Image;
import java.awt.Toolkit;
import java.net.URL;

import rmlimage.core.Constants;
import rmlshared.io.FileUtil;
import rmlshared.thread.PlugIn;
import rmlshared.thread.PlugInBuilder;
import rmlshared.util.Preferences;

/**
 * Main entry class to RML-Image GUI.
 * 
 * @author Marin Lagac&eacute;
 */
public class RMLImage extends rmlimage.gui.RMLImage {

    @Override
    protected void autoExec() {
    }



    @Override
    public PlugIn getAbout() {
        return PlugInBuilder.newInstance("org.ebsdimage.plugin.About");
    }



    @Override
    public String getAppName() {
        return "EBSD-Image";
    }



    @Override
    protected Image getTitleBarIcon() {
        URL iconFile = FileUtil.getURL("org/ebsdimage/gui/logo_(16x16).png");
        return Toolkit.getDefaultToolkit().getImage(iconFile);
    }



    @Override
    protected Preferences loadPreferences() {
        Preferences pref = super.loadPreferences();

        // Activate the plugin toolbar
        pref.setPreference(Constants.PLUGIN_TOOLBAR_ENABLED, true);

        // Deactivate the multi desktop
        pref.setPreference(Constants.MULTI_DESKTOP_ENABLED, false);

        // Deactivate the 16b map support
        pref.setPreference(Constants.SIXTEEN_BITS_ENABLED, false);

        // Deactivate the jpeg support
        pref.setPreference(Constants.JPEG_ENABLED, false);

        return pref;
    }



    /*
     * public static void main(String args[]) { if (args.length == 0) args = new
     * String[] { "ebsd" }; //System.out.println("Before main");
     * rmlimage.RMLImage.main(args); //System.out.println("After main"); }
     */

    @Override
    protected void savePreferences(Preferences pref) {
        // Remove the deactivation of the multi desktop
        pref.remove(Constants.MULTI_DESKTOP_ENABLED);

        // Remove the deactivation of the 16b map support
        pref.remove(Constants.SIXTEEN_BITS_ENABLED);

        // Remove the deactivation of the jpeg support
        pref.remove(Constants.JPEG_ENABLED);

        super.savePreferences(pref);
    }

}
