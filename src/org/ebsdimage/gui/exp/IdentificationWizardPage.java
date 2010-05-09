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
package org.ebsdimage.gui.exp;

import java.io.IOException;

import javax.swing.DefaultListModel;

import org.ebsdimage.core.exp.Exp;
import org.ebsdimage.core.exp.ExpConstants;
import org.ebsdimage.core.run.Operation;


/**
 * Wizard page for the detection operations.
 * 
 * @author Philippe T. Pinard
 * 
 */
public class IdentificationWizardPage extends OperationWizardPage {

    /** Map key for the pre-operations of the identification. */
    public static final String KEY_IDENTIFICATION_PRE = "identification.pre";

    /** Map key for the post-operations of the detection. */
    public static final String KEY_IDENTIFICATION_POST = "identification.post";

    /** Map key for the operation of the identification. */
    public static final String KEY_IDENTIFICATION_OP = "identification.op";

    /** Map key for the results operations of the identification. */
    public static final String KEY_IDENTIFICATION_RESULTS =
            "identification.results";

    /**
     * Map key to store if the data has been previously loaded. It prevents
     * loading the temporary metadata twice.
     */
    public static final String KEY_LOADED = "identification.loaded";



    /**
     * Returns a description of this step.
     * 
     * @return description
     */
    public static final String getDescription() {
        return "Peak Identification Operations";
    }



    /**
     * Creates a new <code>IdentificationWizardPage</code> to display in the
     * wizard.
     * 
     * @throws IOException
     *             if an error occurs while loading the operations
     */
    public IdentificationWizardPage() throws IOException {
        super(ExpConstants.IDENTIFICATION_GUI_PACKAGE);
    }



    @Override
    protected void renderingPage() {
        super.renderingPage();

        Exp exp = (Exp) get(StartWizardPage.KEY_TEMP_EXP);

        if (exp == null)
            return;

        if (get(KEY_LOADED) != null)
            if ((Integer) get(KEY_LOADED) > 0)
                return;

        // Pre
        DefaultListModel model =
                (DefaultListModel) prePanel.userList.getModel();
        model.clear();
        for (Operation op : exp.getIdentificationPreOps())
            model.addElement(op);

        // Op
        model = (DefaultListModel) opPanel.userList.getModel();
        model.clear();
        model.addElement(exp.getIdentificationOp());

        // Post
        model = (DefaultListModel) postPanel.userList.getModel();
        model.clear();
        for (Operation op : exp.getIdentificationPostOps())
            model.addElement(op);

        // Results
        model = (DefaultListModel) resultsPanel.userList.getModel();
        model.clear();
        for (Operation op : exp.getIdentificationResultsOps())
            model.addElement(op);

        put(KEY_LOADED, 1);
    }



    @Override
    public boolean isCorrect(boolean buffer) {
        if (!super.isCorrect(buffer))
            return false;

        int previousOpCount =
                ((Operation[]) get(DetectionWizardPage.KEY_DETECTION_OP)).length;
        int preCount =
                ((DefaultListModel) prePanel.userList.getModel()).getSize();
        int opCount =
                ((DefaultListModel) opPanel.userList.getModel()).getSize();
        int postCount =
                ((DefaultListModel) postPanel.userList.getModel()).getSize();
        int resultsCount =
                ((DefaultListModel) resultsPanel.userList.getModel()).getSize();

        if (previousOpCount == 0)
            if (preCount > 0 || opCount > 0 || postCount > 0
                    || resultsCount > 0) {
                showErrorDialog("A detection operation is required.");
                return false;
            }

        if (opCount == 0 && postCount > 0) {
            showErrorDialog("An operation is required to have post operations.");
            return false;
        }

        if (opCount == 0 && resultsCount > 0) {
            showErrorDialog("An operation is required to have results operations.");
            return false;
        }

        if (buffer) {
            put(KEY_IDENTIFICATION_PRE, prePanel.getOperations());
            put(KEY_IDENTIFICATION_OP, opPanel.getOperations());
            put(KEY_IDENTIFICATION_POST, postPanel.getOperations());
            put(KEY_IDENTIFICATION_RESULTS, resultsPanel.getOperations());
        }

        return true;
    }
}
