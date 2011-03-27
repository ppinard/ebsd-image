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

import java.io.IOException;

import magnitude.core.Magnitude;

import org.ebsdimage.core.HoughMap;
import org.ebsdimage.core.Transform;

import ptpshared.gui.CalibratedDoubleField;
import rmlimage.core.ByteMap;
import rmlimage.core.Map;
import rmlimage.gui.BasicDialog;
import rmlimage.plugin.PlugIn;
import rmlshared.gui.ColumnPanel;
import rmlshared.gui.OkCancelDialog;
import rmlshared.ui.Monitorable;
import edu.umd.cs.findbugs.annotations.CheckForNull;

/**
 * Plug-in for performing the Hough transform.
 * 
 * @author Philippe T. Pinard
 */
public class HoughTransform extends PlugIn implements Monitorable {

    /**
     * Dialog to specify the resolution of the Hough transform.
     * 
     * @author Philippe T. Pinard
     */
    private class Dialog extends BasicDialog {

        /** Field for the theta resolution. */
        private CalibratedDoubleField deltaTheta;



        /**
         * Creates a new <code>Dialog</code>.
         * 
         * @param map
         */
        public Dialog() {
            super("Hough Transform");

            ColumnPanel panel = new ColumnPanel(3);
            panel.setAlignment(2, ColumnPanel.LEFT);

            String[] units = new String[] { "rad", "deg" };
            Magnitude value = new Magnitude(0.5, "deg");
            Magnitude min = new Magnitude(0.1, "deg");
            Magnitude max = new Magnitude(180, "deg");

            deltaTheta = new CalibratedDoubleField("delta theta", value, units);
            deltaTheta.setRange(min, max);
            deltaTheta.setIncrementalStep(value);
            deltaTheta.setDecimalCount(1);
            panel.add("\u2206\u03b8", deltaTheta);

            /*
             * deltaR = new DoubleField("\u2206r", 1); deltaR.setRange(0.1,
             * 100); deltaR.setIncrementalStep(0.5); deltaR.setDecimalCount(1);
             * panel.add("\u2206r : ", deltaR); panel.add("pix"); forceSquared =
             * new CheckBox(); forceSquared.setName("Force squared");
             * //showMapsCBox.setSelected(true); panel.add(forceSquared);
             * panel.add("Force squared");
             */
            setMainComponent(panel);

            // Set the panel preferences
            // Will trickle down to the fields
            setName("HoughTransform");
            setPreferences(getPlugIn().getPreferences().node("EBSD"));
        }



        /**
         * Returns the specified theta resolution.
         * 
         * @return resolution
         */
        public double getDeltaTheta() {
            return deltaTheta.getValueBFR().getValue("rad");
        }

        /*
         * public double getDeltaR() { return deltaR.getValueBFR(); }
         */

        /*
         * public boolean forceSquared() { return forceSquared.isSelectedBFR();
         * }
         */
        /*
         * private class Listener implements FieldListener { public void
         * decrementPerformed(Field source); public void enterPressed(Field
         * source); public void focusGained(Field source) { } public void
         * focusLost(Field source); public void incrementPerformed(Field
         * source); private void refresh(Field source) { if (source ==
         * deltaTheta) { double dTheta = deltaTheta.getValue(); double width =
         * HoughMap.calculateWidth(thetaMin, thetaMax, dTheta); }
         */
    }

    /** Transform operation. */
    private Transform transform; // For progress monitoring



    /**
     * Creates a new <code>HoughTransform</code> plug-in.
     */
    public HoughTransform() {
        setInterruptable(true);
    }



    /**
     * Performs the Hough transform on the selected map.
     * 
     * @return <code>HoughMap</code>
     */
    @CheckForNull
    private HoughMap doTransform() {
        if (!areMapsLoaded(true))
            return null;

        Map map = getSelectedMap();

        if (!isCorrectType(map, ByteMap.class, true))
            return null;

        ByteMap byteMap = (ByteMap) map;

        Dialog dialog = new Dialog();
        if (dialog.show() == OkCancelDialog.CANCEL)
            return null;

        double deltaTheta = dialog.getDeltaTheta();
        // double deltaR = dialog.getDeltaR();

        transform = new Transform();

        HoughMap houghMap = transform.doHough(byteMap, deltaTheta);

        transform = null;

        // Add the HoughMap only if the plugin has not been interrupted
        if (!isInterrupted())
            add(houghMap);

        return houghMap;
    }



    @Override
    public double getTaskProgress() {
        if (transform == null)
            return 0;
        else
            return transform.getTaskProgress();
    }



    @Override
    public void interrupt() {
        if (transform != null) {
            super.interrupt();
            transform.interrupt();
        }
    }



    @Override
    protected void xRun() throws IOException {
        doTransform();
    }

}
