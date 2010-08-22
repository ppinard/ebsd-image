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

import javax.swing.JLabel;

import net.miginfocom.swing.MigLayout;
import rmlimage.core.Constants;
import rmlimage.gui.PlugIn;
import rmlshared.gui.Panel;

/**
 * About dialog.
 * 
 * @author Philippe T. Pinard
 */
public class About extends PlugIn {

    /**
     * Simple dialog.
     * 
     * @author Philippe T. Pinard
     */
    private class Dialog extends rmlshared.gui.Dialog {

        /**
         * Creates a new dialog.
         */
        public Dialog() {
            super(null, "Dialog", "Ok");

            Panel panel = new Panel(new MigLayout());

            panel.add(new JLabel("<html><h1>EBSD-Image</h1></html>"), "wrap");

            panel.add(new JLabel("<html><h2>" + getVersion() + "</h2></html>"),
                    "wrap");

            panel.add(new JLabel("<html>" + "Philippe T. Pinard<br/>"
                    + "Marin Lagac&eacute;<br/>" + "Pierre Hovington<br/>"
                    + "Raynald Gauvin</html>"), "wrap");

            setMainComponent(panel);
        }

    }



    @Override
    protected void xRun() throws Exception {
        new Dialog().show();
    }



    /**
     * Returns the version of the application.
     * 
     * @return version
     */
    protected String getVersion() {
        return getPreferences().getPreference(Constants.APP_VERSION, "x.xx");
    }

}
