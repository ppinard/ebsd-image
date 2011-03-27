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
package org.ebsdimage.gui.exp.wizard;

import javax.swing.DefaultListModel;

import org.ebsdimage.core.exp.Exp;
import org.ebsdimage.core.exp.ExpConstants;
import org.ebsdimage.core.exp.ExpOperation;
import org.ebsdimage.core.run.Operation;

/**
 * Wizard page for the positioning operations.
 * 
 * @author Philippe T. Pinard
 */
public class PositioningWizardPage extends OperationWizardPage {

    /** Map key for the pre-operations of the positioning. */
    public static final String KEY_POSITIONING_PRE = "positioning.pre";

    /** Map key for the post-operations of the positioning. */
    public static final String KEY_POSITIONING_POST = "positioning.post";

    /** Map key for the operation of the positioning. */
    public static final String KEY_POSITIONING_OP = "positioning.op";

    /** Map key for the results operations of the positioning. */
    public static final String KEY_POSITIONING_RESULTS = "positioning.results";

    /**
     * Map key to store if the data has been previously loaded. It prevents
     * loading the temporary metadata twice.
     */
    public static final String KEY_LOADED = "positioning.loaded";



    /**
     * Returns a description of this step.
     * 
     * @return description
     */
    public static final String getDescription() {
        return "Peak Positioning Operations";
    }



    /**
     * Creates a new <code>PositioningWizardPage</code> to display in the
     * wizard.
     */
    public PositioningWizardPage() {
        super(ExpConstants.POSITIONING_GUI_PACKAGE);
    }



    @Override
    public boolean isCorrect(boolean buffer) {
        if (!super.isCorrect(buffer))
            return false;

        int previousOpCount =
                ((Operation[]) get(DetectionWizardPage.KEY_DETECTION_OP)).length;
        int preCount = (prePanel.getUserListModel()).size();
        int opCount = (opPanel.getUserListModel()).size();
        int postCount = (postPanel.getUserListModel()).size();
        int resultsCount = (resultsPanel.getUserListModel()).size();

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
            put(KEY_POSITIONING_PRE, prePanel.getOperations());
            put(KEY_POSITIONING_OP, opPanel.getOperations());
            put(KEY_POSITIONING_POST, postPanel.getOperations());
            put(KEY_POSITIONING_RESULTS, resultsPanel.getOperations());
        }

        return true;
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
        DefaultListModel model = prePanel.getUserListModel();
        model.clear();
        for (ExpOperation op : exp.getPositioningPreOps())
            model.addElement(op);

        // Op
        model = opPanel.getUserListModel();
        model.clear();
        model.addElement(exp.getPositioningOp());

        // Post
        model = postPanel.getUserListModel();
        model.clear();
        for (ExpOperation op : exp.getPositioningPostOps())
            model.addElement(op);

        // Results
        model = resultsPanel.getUserListModel();
        model.clear();
        for (ExpOperation op : exp.getPositioningResultsOps())
            model.addElement(op);

        put(KEY_LOADED, 1);
    }
}
