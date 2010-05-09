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

import java.util.HashMap;
import java.util.Map;

import org.netbeans.spi.wizard.Wizard;
import org.netbeans.spi.wizard.WizardPanelNavResult;

import rmlshared.gui.ErrorDialog;

/**
 * Abstract template for the experiment's wizard page.
 * 
 * @author Philippe T. Pinard
 * 
 */
public abstract class WizardPage extends org.netbeans.spi.wizard.WizardPage {

    /** Holds data shared between wizard pages. */
    private HashMap<String, Object> results;



    @SuppressWarnings("unchecked")
    @Override
    public WizardPanelNavResult allowFinish(String stepName, Map settings,
            Wizard wizard) {
        return (isCorrect(true)) ? WizardPanelNavResult.PROCEED
                : WizardPanelNavResult.REMAIN_ON_PAGE;
    }



    @SuppressWarnings("unchecked")
    @Override
    public WizardPanelNavResult allowNext(String stepName, Map settings,
            Wizard wizard) {
        return (isCorrect(true)) ? WizardPanelNavResult.PROCEED
                : WizardPanelNavResult.REMAIN_ON_PAGE;
    }



    /**
     * Returns the value associated with the specified key in the shared
     * <code>HashMap</code>.
     * 
     * @param key
     *            key associated with the value to be returned
     * 
     * @throws NullPointerException
     *             if the key is null
     * 
     * @return the value associated with the specified key
     */
    public Object get(String key) {
        assert (results != null) : "Uninitialized hashMap";
        if (key == null)
            throw new NullPointerException("Key cannot be null.");

        return results.get(key);
    }



    /**
     * Check that all the fields in the page are valid. No results are buffered.
     * 
     * @return <code>true</code> if all the fields are valid, <code>false</code>
     *         otherwise
     */
    public boolean isCorrect() {
        return isCorrect(false);
    }



    /**
     * Checks that all the fields in the page are valid.
     * 
     * @param buffer
     *            if <code>true</code> the results from the page are put in the
     *            wizard <code>Map</code>.
     * @return <code>true</code> if all the fields are valid, <code>false</code>
     *         otherwise
     */
    public abstract boolean isCorrect(boolean buffer);



    /**
     * Puts a value in the shared <code>HashMap</code>. The key and value cannot
     * be <code>null</code>. If the same key already exists in the
     * <code>HashMap</code>, its value will be overwritten with the new one.
     * 
     * @param key
     *            key with which the specified value is to be associated.
     * @param value
     *            value to be associated with the specified key.
     * 
     * @throws NullPointerException
     *             if the key is null
     */
    public void put(String key, Object value) {
        assert (results != null) : "Uninitialized hashMap";
        if (key == null)
            throw new NullPointerException("key == null.");
        // if (value == null)
        // throw new NullPointerException("value == null.");

        results.put(key, value);
    }



    /**
     * Sets a <code>HashMap</code> map used to pass values between panels of the
     * wizard. <b>This method should not be used by the user.</b> It is only
     * used by <code>EngineWizard</code> so to ensure that all the panels in the
     * wizard will share the same <code>HashMap</code>.
     * 
     * @param hashMap
     *            <code>HashMap</code> for all the wizard pages
     * 
     * @throws NullPointerException
     *             if the hashMap is null
     * 
     * @see #put
     * @see #get
     */
    protected void setHashMap(HashMap<String, Object> hashMap) {
        if (hashMap == null)
            throw new NullPointerException("HashMap cannot be null.");
        this.results = hashMap;
    }



    /**
     * Displays a error dialog with the specified message.
     * 
     * @param message
     *            error message
     */
    protected void showErrorDialog(String message) {
        ErrorDialog.show(message);
    }

}
