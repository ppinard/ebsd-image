package ptpshared.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.*;

import rmlshared.gui.ErrorDialog;
import rmlshared.gui.FlowLayout;
import rmlshared.gui.Spacer;
import rmlshared.ui.InputBuffering;
import rmlshared.ui.InputValidation;
import rmlshared.ui.PreferenceKeeping;
import rmlshared.util.Preferences;

import com.l2fprod.common.swing.JDirectoryChooser;

import edu.umd.cs.findbugs.annotations.CheckForNull;

/**
 * Field consisting of a text box and a browse button to select a directory or a
 * file.
 * 
 * @author Philippe T. Pinard
 */
public class DirBrowserField extends JComponent implements InputValidation,
        InputBuffering, PreferenceKeeping {

    /** Action listener for the browse button. */
    private class BrowseButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Object source = e.getSource();

            assert source == browseButton : "source (" + source
                    + ") should be " + browseButton;

            // Set selected directory
            String[] fileNames = nameField.getText().split(";");
            if (fileNames.length > 0)
                dirChooser.setCurrentDirectory(new File(fileNames[0]));

            // Show dialog
            if (dirChooser.showDialog(getParent(), "Select") == JFileChooser.CANCEL_OPTION)
                return;

            // Check file
            if (!isCorrect())
                actionPerformed(e);

            // Set name field
            StringBuilder text = new StringBuilder();
            if (dirChooser.isMultiSelectionEnabled())
                for (File file : dirChooser.getSelectedFiles())
                    text.append(file.getPath() + ";");
            else
                text.append(dirChooser.getSelectedFile().getPath());

            nameField.setText(text.toString());
        }

    }

    /** Field for the file path. */
    private final JTextField nameField;

    /** Browse button. */
    private JButton browseButton;

    /** Preferences. */
    private Preferences preferences;

    /** Key for the preferences. */
    private static final String PREF_VALUE = "file";

    /** Directory chooser linked with the browse button. */
    private final JDirectoryChooser dirChooser;



    /**
     * Create an empty <code>FileNameField</code> with the <code>Browse</code>
     * button.
     * 
     * @param name
     *            name of the field. (Will be used in reporting errors for the
     *            field)
     */
    public DirBrowserField(String name) {
        this(name, true);
    }



    /**
     * Create an empty <code>FileNameField</code> with or without the
     * <code>Browse</code> button.
     * 
     * @param name
     *            name of the field. (Will be used in reporting errors for the
     *            field)
     * @param showBrowseButton
     *            <code>true</code> to show the <code>Browse</code> button or
     *            <code>false</code> to get only the field
     */
    public DirBrowserField(String name, boolean showBrowseButton) {
        setLayout(new BorderLayout());
        // setLayout(new java.awt.FlowLayout(0, 0, 5));

        if (name == null)
            throw new NullPointerException("name is null.");
        super.setName(name);

        nameField = new JTextField("", 20);
        nameField.setEditable(false);
        add(nameField, BorderLayout.CENTER);
        add(nameField);

        if (showBrowseButton) {
            JPanel panel = new JPanel(new FlowLayout());
            panel.add(new Spacer(5, 5));
            browseButton = getBrowseButton();
            panel.add(browseButton);
            add(panel, BorderLayout.EAST);
        }

        // Directory chooser
        dirChooser = new JDirectoryChooser();
    }



    /**
     * Puts the text from the field in a buffer to be read later. <b>This method
     * must be called from the event thread.</b>
     */
    @Override
    public void bufferInput() {
        // No need to do anything because the input is naturally buffered
    }



    /**
     * Erases the file name.
     */
    public void clear() {
        nameField.setText(null); // Clear the field
        dirChooser.setSelectedFile(null);
    }



    /**
     * Returns a reference to the <code>Browse</code> button beside the field.
     * If the button does not exists, a new one is created and returned. This
     * new one is not shown on the GUI. It can be used to manually put it beside
     * the field in a specific layout
     * 
     * @return a reference to the <code>Browse</code> button
     */
    public JButton getBrowseButton() {
        // If the Browse button already exists, return it
        if (browseButton != null)
            return browseButton;

        // If the Browse button does not exists, create it and return it
        browseButton = new JButton("Browse");
        browseButton.addActionListener(new BrowseButtonListener());
        return browseButton;
    }



    /**
     * Returns the selected directory as a <code>File</code> object.
     * 
     * @return the selected directory
     */
    @CheckForNull
    public File getDir() {
        return dirChooser.getSelectedFile();
    }



    /**
     * Returns the selected directory as a <code>File</code> object <b>The
     * method <code>bufferInput()</code> must be called before to read the text
     * in the field and put it in the buffer.</b>
     * 
     * @return the selected file name
     */
    public File getDirBFR() {
        return getDir();
    }



    /**
     * Gets the name of the field.
     * 
     * @return name of the field
     */
    @Override
    public String getName() {
        String name = super.getName();
        return (name != null) ? name
                : rmlshared.util.SystemUtil.getClassBaseName(this);
    }



    /**
     * Returns the preference table assigned to the field or <code>null</code>
     * if none was assigned. The table will be set at the node representing this
     * field.
     * 
     * @return the preference table assigned to the field or <code>null</code>
     *         if none was assigned.
     */
    @Override
    public Preferences getPreferences() {
        return preferences;
    }



    /**
     * Checks if a directory has been chosen. If a directory has not been
     * chosen, an error dialog will appear informing the user that a file name
     * needs to be chosen
     * 
     * @return if the file is valid
     */
    @Override
    public boolean isCorrect() {
        return isCorrect(true);
    }



    /**
     * Checks if a directory has been chosen.
     * 
     * @param showErrorDialog
     *            if set to <code>true</code>, an error dialog will appear
     *            informing the user that a directory needs to be chosen
     * @return <code>true</code> if the file is valid <code>false</code>
     *         otherwise
     */
    @Override
    public boolean isCorrect(boolean showErrorDialog) {
        File dir = getDir();

        if (dir == null) {
            if (showErrorDialog)
                ErrorDialog.show("Please select a directory.");
            return false;
        }

        // Directory exists
        if (!dir.exists()) {
            if (showErrorDialog)
                ErrorDialog.show("Selected directory (" + dir
                        + ") does not exists. Please make another selection.");
            return false;
        }

        if (!dir.isDirectory()) {
            if (showErrorDialog)
                ErrorDialog.show("Selected file (" + dir
                        + ") is not a directory.");
            return false;
        }

        return true;
    }



    @Override
    public boolean isEnabled() {
        return nameField.isEnabled();
    }



    /**
     * Sets the directory of the field and the selected directory in the
     * directory chooser. If the specified directory is <code>null</code> the
     * value in the field is cleared. If the directory does not exists, the
     * value in the field is also cleared.
     * 
     * @param dir
     *            a directory
     * @throws IllegalArgumentException
     *             if the directory is invalid
     */
    public void setDir(File dir) {
        if (dir == null) {
            clear();
            return;
        }

        nameField.setText(dir.getPath());
        dirChooser.setSelectedFile(dir);
    }



    /**
     * Enables or disables the components of the <code>FileNameField</code>.
     * 
     * @param state
     *            <code>true</code> to enable <code>false</code> to disable
     */
    @Override
    public void setEnabled(boolean state) {
        nameField.setEnabled(state);
        if (browseButton != null)
            browseButton.setEnabled(state);
    }



    /**
     * Sets the name of the field.
     * 
     * @param name
     *            name of the field
     * @throws NullPointerException
     *             if <code>name</code> is <code>null</code>.
     */
    @Override
    public void setName(String name) {
        super.setName(name);

        // Resets the preferences using the new name
        Preferences pref = getPreferences();
        if (pref != null)
            setPreferences(pref.parent());
    }



    /**
     * Sets the preference table for the field. If set to <code>null</code>, no
     * <code>Preferences</code> will be used.
     * 
     * @param pref
     *            preference table to use
     */
    @Override
    public void setPreferences(Preferences pref) {
        if (pref == null) {
            preferences = null;
            return;
        }

        preferences = pref.node(getName());

        if (preferences.contains(PREF_VALUE)) {
            String dirname = preferences.getPreference(PREF_VALUE, null);
            if (dirname == null)
                clear();
            else
                setDir(new File(dirname));
        }
    }



    @Override
    public void updatePreferences() {
        if (preferences == null)
            return;

        if (getDir() != null)
            preferences.setPreference(PREF_VALUE, getDir().getPath());
    }

}
