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
 */
public class IndexingWizardPage extends OperationWizardPage {

    /** Map key for the pre-operations of the indexing. */
    public static final String KEY_INDEXING_PRE = "indexing.pre";

    /** Map key for the post-operations of the indexing. */
    public static final String KEY_INDEXING_POST = "indexing.post";

    /** Map key for the operation of the indexing. */
    public static final String KEY_INDEXING_OP = "indexing.op";

    /** Map key for the results operations of the indexing. */
    public static final String KEY_INDEXING_RESULTS = "indexing.results";

    /**
     * Map key to store if the data has been previously loaded. It prevents
     * loading the temporary metadata twice.
     */
    public static final String KEY_LOADED = "indexing.loaded";



    /**
     * Returns a description of this step.
     * 
     * @return description
     */
    public static final String getDescription() {
        return "Indexing Operations";
    }



    /**
     * Creates a new <code>IndexingWizardPage</code> to display in the wizard.
     * 
     * @throws IOException
     *             if an error occurs while loading the operations
     */
    public IndexingWizardPage() throws IOException {
        super(ExpConstants.INDEXING_GUI_PACKAGE);
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
        for (Operation op : exp.getIndexingPreOps())
            model.addElement(op);

        // Op
        model = opPanel.getUserListModel();
        model.clear();
        model.addElement(exp.getIndexingOp());

        // Post
        model = postPanel.getUserListModel();
        model.clear();
        for (Operation op : exp.getIndexingPostOps())
            model.addElement(op);

        // Results
        model = resultsPanel.getUserListModel();
        model.clear();
        for (Operation op : exp.getIndexingResultsOps())
            model.addElement(op);

        put(KEY_LOADED, 1);
    }



    @Override
    public boolean isCorrect(boolean buffer) {
        if (!super.isCorrect(buffer))
            return false;

        int previousOpCount =
                ((Operation[]) get(IdentificationWizardPage.KEY_IDENTIFICATION_OP)).length;
        int preCount = (prePanel.getUserListModel()).size();
        int opCount = (opPanel.getUserListModel()).size();
        int postCount = (postPanel.getUserListModel()).size();
        int resultsCount = (resultsPanel.getUserListModel()).size();

        if (previousOpCount == 0)
            if (preCount > 0 || opCount > 0 || postCount > 0
                    || resultsCount > 0) {
                showErrorDialog("An identification operation is required.");
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
            put(KEY_INDEXING_PRE, prePanel.getOperations());
            put(KEY_INDEXING_OP, opPanel.getOperations());
            put(KEY_INDEXING_POST, postPanel.getOperations());
            put(KEY_INDEXING_RESULTS, resultsPanel.getOperations());
        }

        return true;
    }

}
