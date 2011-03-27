package ptpshared.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;

import rmlshared.gui.ErrorDialog;
import rmlshared.gui.FlowLayout;
import rmlshared.gui.Spacer;
import rmlshared.ui.InputBuffering;
import rmlshared.ui.InputValidation;
import rmlshared.ui.PreferenceKeeping;
import rmlshared.util.Preferences;
import edu.umd.cs.findbugs.annotations.CheckForNull;

/**
 * Field consisting of a text box and a browse button to select a directory or a
 * file.
 * 
 * @author Philippe T. Pinard
 */
public class MultiFilesBrowserField extends JComponent implements
        InputValidation, InputBuffering, PreferenceKeeping {

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
                fileChooser.setCurrentDirectory(new File(fileNames[0]));

            // Show dialog
            if (fileChooser.showDialog(getParent(), "Select") == JFileChooser.CANCEL_OPTION)
                return;

            // Check file
            if (!isCorrect())
                actionPerformed(e);

            // Set name field
            StringBuilder text = new StringBuilder();
            if (fileChooser.isMultiSelectionEnabled())
                for (File file : fileChooser.getSelectedFiles())
                    text.append(file.getPath() + ";");
            else
                text.append(fileChooser.getSelectedFile().getPath());

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

    /** File chooser linked with the browse button. */
    private final JFileChooser fileChooser;



    /**
     * Create an empty <code>FileNameField</code> with the <code>Browse</code>
     * button.
     * 
     * @param name
     *            name of the field. (Will be used in reporting errors for the
     *            field)
     */
    public MultiFilesBrowserField(String name) {
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
    public MultiFilesBrowserField(String name, boolean showBrowseButton) {
        this(name, showBrowseButton, new FileFilter[0]);
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
     * @param fileFilters
     *            user choosable file filters
     */
    public MultiFilesBrowserField(String name, boolean showBrowseButton,
            FileFilter[] fileFilters) {
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

        // File chooser
        fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setMultiSelectionEnabled(true);

        if (fileFilters.length == 0)
            fileChooser.setAcceptAllFileFilterUsed(true);
        else {
            fileChooser.setAcceptAllFileFilterUsed(false);
            for (FileFilter fileFilter : fileFilters)
                fileChooser.addChoosableFileFilter(fileFilter);
        }
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
    }



    /**
     * Converts an array of files to a single string.
     * 
     * @param files
     *            an array of files
     * @return path of the files in the array separated by a comma
     */
    private String filesToText(File[] files) {
        StringBuilder str = new StringBuilder();
        for (File file : files)
            str.append(file.getPath() + ",");
        return str.toString();
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
     * Returns the selected file name as a <code>File</code> object.
     * 
     * @return the selected file name
     */
    @CheckForNull
    public File[] getFiles() {
        return fileChooser.getSelectedFiles();
    }



    /**
     * Returns the selected file name as a <code>File</code> object <b>The
     * method <code>bufferInput()</code> must be called before to read the text
     * in the field and put it in the buffer.</b>
     * 
     * @return the selected file name
     */
    public File[] getFilesBFR() {
        return getFiles();
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
     * Returns a validation message whether the specified files are valid:
     * <ul>
     * <li>the file exists</li>
     * <li>the file meets the file filters.</li>
     * </ul>
     * If the file is valid, an empty string is returned.
     * 
     * @param files
     *            an array of files
     * @return a validation message if the files are invalid or an empty string
     *         if the files are valid
     */
    private String getValidationMessage(File[] files) {
        for (File file : files) {
            // File exists
            if (!file.exists()) {
                return "Selected file (" + file
                        + ") does not exists. Please make another selection.";
            }

            // File respects file filters
            if (!fileChooser.accept(file)) {
                return "Selected file (" + file
                        + ") does not have a valid extension.";
            }
        }

        return "";
    }



    /**
     * Checks if a file name has been choosen. If a file name has not been
     * chosen, an error dialog will appear informing the user that a file name
     * needs to be chosen
     * 
     * @return <code>true</code> if a file name has been chosen
     *         <code>false</code> otherwise
     */
    @Override
    public boolean isCorrect() {
        return isCorrect(true);
    }



    /**
     * Checks if a file name has been chosen.
     * 
     * @param showErrorDialog
     *            if set to <code>true</code>, an error dialog will appear
     *            informing the user that a file name needs to be chosen
     * @return <code>true</code> if a file name has been chosen
     *         <code>false</code> otherwise
     */
    @Override
    public boolean isCorrect(boolean showErrorDialog) {
        String message = getValidationMessage(getFiles());
        if (message.length() > 0 && showErrorDialog) {
            ErrorDialog.show(message);
            return false;
        } else
            return true;
    }



    /**
     * Returns the selected file name as a <code>String</code> object
     * 
     * @return the selected file name
     */
    /*
     * public String getText() { return nameField.getText(); }
     */

    @Override
    public boolean isEnabled() {
        return nameField.isEnabled();
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
     * Sets the files of the field and the selected files in the file chooser.
     * If the array is null, the value of the field is cleared.
     * 
     * @param files
     *            an array of files
     * @throws NullPointerException
     *             if one file in the array of files is null
     * @throws IllegalArgumentException
     *             if a file in the array is invalid
     */
    public void setFiles(File[] files) {
        if (files == null)
            clear();

        for (File file : files)
            if (file == null)
                throw new NullPointerException("A file in the array is null.");

        String message = getValidationMessage(files);
        if (message.length() > 0)
            throw new IllegalArgumentException(message);

        nameField.setText(filesToText(files));
        fileChooser.setSelectedFiles(files);
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

        String filenames = preferences.getPreference(PREF_VALUE, null);
        if (filenames == null)
            clear();
        else
            setFiles(textToFiles(filenames));

    }



    /**
     * Converts a string of file paths separated by commas to an array of files.
     * 
     * @param text
     *            a string of file paths
     * @return an array of files
     */
    private File[] textToFiles(String text) {
        String[] filePaths = text.split(",");
        File[] files = new File[filePaths.length];

        for (int i = 0; i < filePaths.length; i++)
            files[i] = new File(filePaths[i]);

        return files;
    }



    @Override
    public void updatePreferences() {
        if (preferences == null)
            return;

        preferences.setPreference(PREF_VALUE, filesToText(getFiles()));
    }

}
