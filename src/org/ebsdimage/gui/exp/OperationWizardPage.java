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

import static ptpshared.io.FileUtil.joinPackageNames;

import java.io.IOException;

import org.ebsdimage.core.run.Operation;
import org.ebsdimage.gui.run.ops.MultipleChoicePanel;

import net.miginfocom.swing.MigLayout;

import ptpshared.gui.WizardPage;

import rmlshared.util.ArrayList;

/**
 * Template for the experiment's wizard page.
 * 
 * @author Philippe T. Pinard
 * 
 */
public class OperationWizardPage extends WizardPage {

    /** Panel for the pre operations. */
    protected MultipleChoicePanel prePanel;

    /** Panel for the operations. */
    protected SingleChoicePanel opPanel;

    /** Panel for the post operations. */
    protected MultipleChoicePanel postPanel;

    /** Panel for the results operations. */
    protected MultipleChoicePanel resultsPanel;



    /**
     * Creates a new <code>EngineWizardPage</code>.
     * 
     * @param opsDialogPackage
     *            root package for the operations
     * @param pre
     *            if <code>true</code> the pre-operations panel is displayed
     * @param op
     *            if <code>true</code> the operation panel is displayed
     * @param post
     *            if <code>true</code> the post-operations panel is displayed
     * @param results
     *            if <code>true</code> the results-operations panel is displayed
     * 
     * @throws IOException
     *             if an error occurs
     */
    public OperationWizardPage(String opsDialogPackage, boolean pre,
            boolean op, boolean post, boolean results) throws IOException {
        setLayout(new MigLayout("flowy, fill"));

        String preDialogPackage = joinPackageNames(opsDialogPackage, "pre");
        prePanel = new MultipleChoicePanel("Pre", preDialogPackage);
        if (pre)
            add(prePanel, "grow");

        String opDialogPackage = joinPackageNames(opsDialogPackage, "op");
        opPanel = new SingleChoicePanel("Operation", opDialogPackage);
        if (op)
            add(opPanel, "grow");

        String postDialogPackage = joinPackageNames(opsDialogPackage, "post");
        postPanel = new MultipleChoicePanel("Post", postDialogPackage);
        if (post)
            add(postPanel, "grow");

        String resultsDialogPackage =
                joinPackageNames(opsDialogPackage, "results");
        resultsPanel = new MultipleChoicePanel("Results", resultsDialogPackage);
        if (results)
            add(resultsPanel, "grow");
    }



    /**
     * Creates a new <code>EngineWizardPage</code>. All the panels are
     * displayed.
     * 
     * @param opsDialogPackage
     *            root package for the operations
     * 
     * @throws IOException
     *             if an error occurs
     */
    public OperationWizardPage(String opsDialogPackage) throws IOException {
        this(opsDialogPackage, true, true, true, true);
    }



    /**
     * Returns the list of <code>Operation</code>s chosen by the user.
     * 
     * @return array of <code>Operation</code>s
     */
    public Operation[] getOperations() {
        ArrayList<Operation> ops = new ArrayList<Operation>();

        ops.addAll(prePanel.getOperations());
        ops.addAll(opPanel.getOperations());
        ops.addAll(postPanel.getOperations());
        ops.addAll(resultsPanel.getOperations());

        return ops.toArray(new Operation[0]);
    }



    @Override
    public boolean isCorrect(boolean buffer) {
        if (!prePanel.isCorrect())
            return false;
        if (!opPanel.isCorrect())
            return false;
        if (!postPanel.isCorrect())
            return false;
        if (!resultsPanel.isCorrect())
            return false;

        return true;
    }

}
