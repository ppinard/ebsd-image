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
 * Wizard page for the pattern operations.
 * 
 * @author Philippe T. Pinard
 */
public class PatternWizardPage extends OperationWizardPage {

    /** Map key for the post-operations of the pattern. */
    public static final String KEY_PATTERN_POST = "pattern.post";

    /** Map key for the results operations of the pattern. */
    public static final String KEY_PATTERN_RESULTS = "pattern.results";

    /**
     * Map key to store if the data has been previously loaded. It prevents
     * loading the temporary metadata twice.
     */
    public static final String KEY_LOADED = "pattern.loaded";



    /**
     * Returns a description of this step.
     * 
     * @return description
     */
    public static final String getDescription() {
        return "Pattern Operations";
    }



    /**
     * Creates a new <code>PatternWizardPage</code> to display in the wizard.
     * 
     * @throws IOException
     *             if an error occurs while loading the operations
     */
    public PatternWizardPage() throws IOException {
        super(ExpConstants.PATTERN_GUI_PACKAGE, false, false, true, true);
    }



    @Override
    public boolean isCorrect(boolean buffer) {
        if (!super.isCorrect(buffer))
            return false;

        if (buffer) {
            put(KEY_PATTERN_POST, postPanel.getOperations());
            put(KEY_PATTERN_RESULTS, resultsPanel.getOperations());
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

        // Post
        DefaultListModel model = postPanel.getUserListModel();
        model.clear();
        for (ExpOperation op : exp.getPatternPostOps())
            model.addElement(op);

        // Results
        model = resultsPanel.getUserListModel();
        model.clear();
        for (ExpOperation op : exp.getPatternResultsOps())
            model.addElement(op);

        put(KEY_LOADED, 1);
    }

}
