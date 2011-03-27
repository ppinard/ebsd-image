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

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JScrollPane;

import magnitude.core.Magnitude;
import net.miginfocom.swing.MigLayout;

import org.apache.commons.math.geometry.Vector3D;
import org.ebsdimage.core.Camera;
import org.ebsdimage.core.Microscope;
import org.ebsdimage.io.MicroscopeUtil;

import ptpshared.geom.Vector3DUtils;
import ptpshared.gui.CalibratedDoubleField;
import ptpshared.gui.GuiUtil;
import ptpshared.gui.Vector3DField;
import ptpshared.util.simplexml.ApacheCommonMathMatcher;
import ptpshared.util.simplexml.XmlSaver;
import rmlimage.gui.BasicDialog;
import rmlimage.gui.PlugIn;
import rmlimage.gui.RMLImage;
import rmlshared.enums.YesNo;
import rmlshared.gui.*;
import rmlshared.io.FileUtil;

/**
 * Setup of microscope configurations which allow users to create, edit and
 * delete microscope configurations.
 * 
 * @author Philippe T. Pinard
 */
public class MicroscopeSetup extends PlugIn {

    /**
     * Dialog to create and edit <code>Microscope</code>.
     * 
     * @author Philippe T. Pinard
     */
    private static class MicroscopeDialog extends BasicDialog {

        /** Field for the name of the microscope. */
        public TextField nameField;

        /** Field for the width of the camera. */
        private CalibratedDoubleField widthField;

        /** Field for the height of the camera. */
        private CalibratedDoubleField heightField;

        /** Field for the normal of the camera. */
        private Vector3DField normalField;

        /** Field for the direction of the x vector of the camera. */
        private Vector3DField directionField;

        /** Field for the tilt axis. */
        private Vector3DField tiltAxisField;



        /**
         * Creates a new <code>MicroscopeDialog</code> to create a new
         * <code>Microscope</code>.
         */
        public MicroscopeDialog() {
            this(new Microscope());
        }



        /**
         * Creates a new <code>MicroscopeDialog</code> to edit an existing
         * <code>Microscope</code>.
         * 
         * @param microscope
         *            microscope to edit
         */
        public MicroscopeDialog(Microscope microscope) {
            super("New microscope");

            // Name
            Panel namePanel = new Panel(new MigLayout());

            namePanel.add("Name");
            nameField = new TextField("name", microscope.getName());
            namePanel.add(nameField, "wrap");

            // Camera
            Camera camera = microscope.getCamera();

            Panel cameraPanel = new Panel(new MigLayout());
            cameraPanel.setName("Camera");
            cameraPanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createTitledBorder("Camera"),
                    BorderFactory.createEmptyBorder(5, 5, 5, 5)));

            cameraPanel.add("Width");
            String[] units = new String[] { "m", "cm", "mm" };
            widthField =
                    new CalibratedDoubleField("width", camera.getWidth(), units);
            widthField.setRange(new Magnitude(0, "m"), new Magnitude(
                    Double.POSITIVE_INFINITY, "m"));
            cameraPanel.add(widthField, "wrap");

            cameraPanel.add("Height");
            heightField =
                    new CalibratedDoubleField("height", camera.getHeight(),
                            units);
            heightField.setRange(new Magnitude(0, "m"), new Magnitude(
                    Double.POSITIVE_INFINITY, "m"));
            cameraPanel.add(heightField, "wrap");

            cameraPanel.add("Normal");
            normalField = new Vector3DField("normal", camera.n);
            cameraPanel.add(normalField, "wrap");

            cameraPanel.add("Direction");
            directionField = new Vector3DField("direction", camera.x);
            cameraPanel.add(directionField, "wrap");

            // Tilt axis
            Panel tiltAxisPanel = new Panel(new MigLayout());
            tiltAxisPanel.setName("Tilt axis");

            tiltAxisPanel.add("Tilt axis");
            tiltAxisField =
                    new Vector3DField("tilt axis", microscope.getTiltAxis());
            tiltAxisPanel.add(tiltAxisField, "wrap");

            // Main panel
            Panel cPanel = new Panel(new MigLayout());

            cPanel.add(namePanel, "wrap");
            cPanel.add(cameraPanel, "wrap");
            cPanel.add(tiltAxisPanel, "wrap");

            setMainComponent(cPanel);
        }



        /**
         * Returns the <code>Microscope</code> defined in the dialog.
         * 
         * @return <code>Microscope</code> object
         */
        public Microscope getMicroscope() {
            Camera camera =
                    new Camera(normalField.getValue(),
                            directionField.getValue(), widthField.getValue(),
                            heightField.getValue());

            Microscope microscope =
                    new Microscope(camera, tiltAxisField.getValue());
            microscope.setName(nameField.getValue());

            return microscope;
        }



        @Override
        public boolean isCorrect() {
            if (!super.isCorrect())
                return false;

            if (nameField.getValue().isEmpty()) {
                ErrorDialog.show("Name of the crystal cannot be empty.");
                return false;
            }

            if (widthField.getValue().getBaseUnitsValue() <= 0) {
                ErrorDialog.show("Width of the camera must be greater than zero.");
                return false;
            }

            if (heightField.getValue().getBaseUnitsValue() <= 0) {
                ErrorDialog.show("Height of the camera must be greater than zero.");
                return false;
            }

            Vector3D n = normalField.getValue();
            if (n.getNorm() == 0.0) {
                ErrorDialog.show("Norm of the camera cannot be a null vector.");
                return false;
            }

            Vector3D x = directionField.getValue();
            if (x.getNorm() == 0.0) {
                ErrorDialog.show("Direction of the camera cannot be a null vector.");
                return false;
            }

            if (Vector3DUtils.areParallel(n, x, 1e-6)) {
                ErrorDialog.show("Normal and direction of the camera cannot be "
                        + "parallel vectors.");
                return false;
            }

            if (tiltAxisField.getValue().getNorm() == 0.0) {
                ErrorDialog.show("Tilt axis cannot be a null vector.");
                return false;
            }

            return true;
        }
    }

    /**
     * Dialog to list, edit, add remove available microscope configurations
     * saved in the microscope base directory.
     * 
     * @author Philippe T. Pinard
     */
    private static class SelectionDialog extends Dialog {

        /**
         * Common methods for the action plugins of this dialog.
         * 
         * @author Philippe T. Pinard
         */
        private abstract class ActionPlugIn extends PlugIn {

            /**
             * Deletes a microscope saved on the disk.
             * 
             * @param microscope
             *            a microscope
             */
            public void delete(Microscope microscope) {
                File file = getMicroscopeFile(microscope);

                if (!file.delete())
                    ErrorDialog.show("Could not delete (" + file + ").");
            }



            /**
             * Returns the file where a microscope is saved or will be saved.
             * 
             * @param microscope
             *            a microscope
             * @return file for a microscope
             */
            private File getMicroscopeFile(Microscope microscope) {
                File file =
                        new File(MicroscopeUtil.getMicroscopeBasedir(),
                                microscope.getName());
                return FileUtil.setExtension(file, "xml");
            }



            /**
             * Saves the specified microscope in the microscrope base directory.
             * 
             * @param microscope
             *            a microscope
             */
            public void save(Microscope microscope) {
                File file = getMicroscopeFile(microscope);

                XmlSaver saver = new XmlSaver();
                saver.matchers.registerMatcher(new ApacheCommonMathMatcher());

                try {
                    saver.save(microscope, file);
                } catch (IOException e) {
                    ErrorDialog.show("Could not save (" + file + ").");
                }
            }

        }

        /**
         * Action to edit a microscope.
         * 
         * @author Philippe T. Pinard
         */
        private class Edit extends ActionPlugIn {
            @Override
            protected void xRun() throws Exception {
                // Get the selected microscope
                Microscope microscope =
                        (Microscope) microscopesList.getSelectedValue();
                if (microscope == null)
                    return;

                // Open dialog
                MicroscopeDialog dialog = new MicroscopeDialog(microscope);

                if (dialog.show() == OkCancelDialog.CANCEL)
                    return;

                // Delete old microscope and save new one
                delete(microscope);
                save(dialog.getMicroscope());

                refresh();
            }
        }

        /**
         * Action to create a new microscope.
         * 
         * @author Philippe T. Pinard
         */
        private class New extends ActionPlugIn {

            @Override
            protected void xRun() throws Exception {
                MicroscopeDialog dialog = new MicroscopeDialog();

                if (dialog.show() == OkCancelDialog.CANCEL)
                    return;

                save(dialog.getMicroscope());

                refresh();
            }
        }

        /**
         * Action to remove a microscope configuration.
         * 
         * @author Philippe T. Pinard
         */
        private class Remove extends ActionPlugIn {

            @Override
            protected void xRun() throws Exception {
                // Get the selected microscope
                Microscope microscope =
                        (Microscope) microscopesList.getSelectedValue();
                if (microscope == null)
                    return;

                if (YesNoDialog.show(
                        "Are you sure you want to delete this configuration?",
                        YesNo.NO) == YesNo.YES)
                    delete(microscope);

                refresh();
            }

        }

        /** Icon for the new button. */
        private static final ImageIcon NEW_ICON =
                GuiUtil.loadIcon("ptpshared/data/icon/document-new_(22x22).png");

        /** Icon for the remove button. */
        private static final ImageIcon REMOVE_ICON =
                GuiUtil.loadIcon("ptpshared/data/icon/list-remove_(22x22).png");

        /** Icon for the edit button. */
        private static final ImageIcon EDIT_ICON =
                GuiUtil.loadIcon("ptpshared/data/icon/document-edit_(22x22).png");

        /** List of available microscope configurations. */
        private JList microscopesList;

        /** New button. */
        private Button newButton;

        /** Edit button. */
        private Button editButton;

        /** Remove button. */
        private Button removeButton;



        /**
         * Creates a new <code>SelectionDialog</code>.
         */
        public SelectionDialog() {
            super(RMLImage.getSelectedFrame(), "Microscope Setup", "OK");

            Panel panel = new Panel(new MigLayout());

            panel.add("Available microscopes", "wrap");

            microscopesList = new JList(new DefaultListModel());
            JScrollPane listScroller = new JScrollPane(microscopesList);
            listScroller.setPreferredSize(new Dimension(300, 100));
            panel.add(listScroller, "grow, wrap");

            newButton = new Button(NEW_ICON);
            newButton.setToolTipText("New microscope");
            newButton.setPlugIn(new New());
            panel.add(newButton, "split 3, align right");

            editButton = new Button(EDIT_ICON);
            editButton.setToolTipText("Edit selected microscope");
            editButton.setPlugIn(new Edit());
            panel.add(editButton);

            removeButton = new Button(REMOVE_ICON);
            removeButton.setToolTipText("Remove selected microscope");
            removeButton.setPlugIn(new Remove());
            panel.add(removeButton);

            setMainComponent(panel);

            refresh();
        }



        /**
         * Refresh the list of available microscope configurations.
         */
        private void refresh() {
            DefaultListModel model =
                    (DefaultListModel) microscopesList.getModel();

            model.clear();

            for (Microscope microscope : MicroscopeUtil.getMicroscopes())
                model.addElement(microscope);
        }
    }



    @Override
    protected void xRun() throws Exception {
        SelectionDialog dialog = new SelectionDialog();

        if (dialog.show() == OkCancelDialog.CANCEL)
            return;

    }

}
