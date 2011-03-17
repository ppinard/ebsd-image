package org.ebsdimage.plugin;

import java.io.File;

import javax.swing.JFileChooser;

import org.ebsdimage.gui.NewPhaseDialog;

import ptpshared.util.simplexml.ApacheCommonMathMatcher;
import ptpshared.util.simplexml.XmlSaver;
import rmlimage.gui.PlugIn;
import rmlimage.gui.RMLImage;
import rmlshared.io.FileUtil;
import rmlshared.io.WildcardFileFilter;
import crystallography.core.Crystal;
import crystallography.io.simplexml.SpaceGroupMatcher;

/**
 * Plug-in to create a new phase.
 * 
 * @author ppinard
 */
public class NewPhase extends PlugIn {

    @Override
    protected void xRun() throws Exception {
        // Create new phase
        NewPhaseDialog dialog = new NewPhaseDialog();

        if (dialog.show() != NewPhaseDialog.OK)
            return;

        Crystal phase = dialog.getCrystal();

        // Location where to save the phase
        String currentDirectory =
                getPreferences().getPreference("FileDialog.path", null);
        JFileChooser fileChooser = new JFileChooser(currentDirectory);
        fileChooser.setFileFilter(new WildcardFileFilter("*.xml"));
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        if (fileChooser.showSaveDialog(RMLImage.getSelectedFrame()) != JFileChooser.APPROVE_OPTION)
            return;

        File file = fileChooser.getSelectedFile();
        file = FileUtil.setExtension(file, "xml");

        // Save
        XmlSaver saver = new XmlSaver();
        saver.matchers.registerMatcher(new ApacheCommonMathMatcher());
        saver.matchers.registerMatcher(new SpaceGroupMatcher());

        saver.save(phase, file);
    }
}
