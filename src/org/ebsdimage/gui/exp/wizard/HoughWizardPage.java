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
package org.ebsdimage.gui.exp.wizard;

import java.io.IOException;

import javax.swing.DefaultListModel;

import org.ebsdimage.core.exp.Exp;
import org.ebsdimage.core.exp.ExpConstants;
import org.ebsdimage.core.exp.ExpOperation;

/**
 * Wizard page for the Hough operations.
 * 
 * @author Philippe T. Pinard
 */
public class HoughWizardPage extends OperationWizardPage {

    /** Map key for the pre-operations of the hough. */
    public static final String KEY_HOUGH_PRE = "hough.pre";

    /** Map key for the post-operations of the hough. */
    public static final String KEY_HOUGH_POST = "hough.post";

    /** Map key for the operation of the hough. */
    public static final String KEY_HOUGH_OP = "hough.op";

    /** Map key for the results operations of the hough. */
    public static final String KEY_HOUGH_RESULTS = "hough.results";

    /**
     * Map key to store if the data has been previously loaded. It prevents
     * loading the temporary metadata twice.
     */
    public static final String KEY_LOADED = "hough.loaded";



    /**
     * Returns a description of this step.
     * 
     * @return description
     */
    public static final String getDescription() {
        return "Hough Operations";
    }



    /**
     * Creates a new <code>HoughWizardPage</code> to display in the wizard.
     * 
     * @throws IOException
     *             if an error occurs while loading the operations
     */
    public HoughWizardPage() throws IOException {
        super(ExpConstants.HOUGH_GUI_PACKAGE);
    }



    @Override
    public boolean isCorrect(boolean buffer) {
        if (!super.isCorrect(buffer))
            return false;

        int opCount = (opPanel.getUserListModel()).size();
        int postCount = (postPanel.getUserListModel()).size();
        int resultsCount = (resultsPanel.getUserListModel()).size();

        if (opCount == 0 && postCount > 0) {
            showErrorDialog("An operation is required to have post operations.");
            return false;
        }

        if (opCount == 0 && resultsCount > 0) {
            showErrorDialog("An operation is required to have results operations.");
            return false;
        }

        if (buffer) {
            put(KEY_HOUGH_PRE, prePanel.getOperations());
            put(KEY_HOUGH_OP, opPanel.getOperations());
            put(KEY_HOUGH_POST, postPanel.getOperations());
            put(KEY_HOUGH_RESULTS, resultsPanel.getOperations());
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
        for (ExpOperation op : exp.getHoughPreOps())
            model.addElement(op);

        // Op
        model = opPanel.getUserListModel();
        model.clear();
        model.addElement(exp.getHoughOp());

        // Post
        model = postPanel.getUserListModel();
        model.clear();
        for (ExpOperation op : exp.getHoughPostOps())
            model.addElement(op);

        // Results
        model = resultsPanel.getUserListModel();
        model.clear();
        for (ExpOperation op : exp.getHoughResultsOps())
            model.addElement(op);

        put(KEY_LOADED, 1);
    }

}
