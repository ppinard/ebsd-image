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
package ptpshared.gui;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.UIManager;

import org.netbeans.api.wizard.WizardDisplayer;

import rmlshared.thread.EventThreadDispatcher;

/**
 * A wizard.
 * 
 * @author Philippe T. Pinard
 * 
 */
public class Wizard {

    /**
     * Event thread to show the wizard.
     * 
     * @author Philippe T. Pinard
     * 
     */
    private class Show extends EventThreadDispatcher {

        @Override
        public Object doRun() {
            return WizardDisplayer.showWizard(wizard, dimensions);
        }
    }



    /** Pages of the wizard. */
    private WizardPage[] pages;

    /** Results from the wizard. */
    protected HashMap<String, Object> results;

    /** Title of the wizard window. */
    private String title = "Wizard";

    /** Dimensions of the wizard window. */
    private Rectangle dimensions;

    /** Wizard. */
    private org.netbeans.spi.wizard.Wizard wizard;

    /** Image for side panel background. */
    private BufferedImage image = null;



    /**
     * Creates a new wizard with the specified wizard pages.
     * <p/>
     * The final dimensions of the wizard window is automatically calculated
     * from the preferred dimensions for each page of the wizard.
     * 
     * @param pages
     *            pages of the wizard
     */
    public Wizard(WizardPage[] pages) {
        this.pages = pages.clone();

        // Sets the HashMap to hold shared data between the wizard pages
        results = new HashMap<String, Object>();

        int maxHeight = Integer.MIN_VALUE;
        int maxWidth = Integer.MIN_VALUE;
        for (WizardPage page : this.pages) {
            page.setHashMap(results);

            Dimension dim = page.getPreferredSize();
            if (dim.height > maxHeight)
                maxHeight = dim.height;
            if (dim.width > maxWidth)
                maxWidth = dim.width;
        }

        dimensions = new Rectangle(maxWidth, maxHeight);
    }



    /**
     * Returns the title of the wizard window.
     * 
     * @return title
     */
    public String getTitle() {
        return title;
    }



    /**
     * Sets a user defined preferred height for the wizard window dimensions.
     * 
     * @param height
     *            preferred height
     */
    public void setPreferredHeight(int height) {
        if (height <= 0)
            throw new IllegalArgumentException(
                    "Width has to be greater than 0.");

        dimensions = new Rectangle(dimensions.width, height);
    }



    /**
     * Sets a user defined preferred width for the wizard window dimensions.
     * 
     * @param width
     *            preferred width
     */
    public void setPreferredWidth(int width) {
        if (width <= 0)
            throw new IllegalArgumentException(
                    "Width has to be greater than 0.");

        dimensions = new Rectangle(width, dimensions.height);
    }



    /**
     * Sets the image to use for the side panel background. Typical dimensions
     * are 198x361 pixels.
     * 
     * @param image
     *            an image
     */
    public void setSidePanelBackground(BufferedImage image) {
        this.image = image;
    }



    /**
     * Sets the image to use for the side panel background. Typical dimensions
     * are 198x361 pixels.
     * 
     * @param imageFile
     *            file of the image
     * @throws IOException
     *             if an error occurs while reading the image file
     */
    public void setSidePanelBackground(File imageFile) throws IOException {
        image = ImageIO.read(imageFile);
    }



    /**
     * Sets the title of the wizard window.
     * 
     * @param title
     *            descriptive title
     * 
     * @throws NullPointerException
     *             if the title is null
     */
    public void setTitle(String title) {
        if (title == null)
            throw new NullPointerException("Title cannot be null.");

        this.title = title;
    }



    /**
     * Displays the wizard.
     * 
     * @return <code>true</code> if the wizard finish properly,
     *         <code>false</code> otherwise
     */
    public boolean show() {
        // Set background image
        UIManager.put("wizard.sidebar.image", image);

        // Create wizard
        wizard = WizardPage.createWizard(title, pages);

        // Show and wait until wizard closes
        Object result = new Show().invokeAndWait();

        // Reset background image
        UIManager.put("wizard.sidebar.image", null);

        if (result == null)
            return false;
        else
            return true;
    }
}
