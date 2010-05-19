package org.ebsdimage.plugin;

import javax.swing.JLabel;

import net.miginfocom.swing.MigLayout;
import rmlimage.core.Constants;
import rmlimage.gui.PlugIn;
import rmlshared.gui.Panel;

/**
 * About dialog.
 * 
 * @author ppinard
 * 
 */
public class About extends PlugIn {

    /**
     * Simple dialog.
     * 
     * @author ppinard
     * 
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
