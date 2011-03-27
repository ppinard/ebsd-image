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
package ptpshared.gui;

import java.awt.Component;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import org.netbeans.spi.wizard.Wizard;
import org.netbeans.spi.wizard.WizardPanelNavResult;

import rmlshared.gui.ErrorDialog;
import rmlshared.ui.PreferenceKeeping;
import rmlshared.util.Preferences;
import edu.umd.cs.findbugs.annotations.CheckForNull;

/**
 * Abstract template for the experiment's wizard page.
 * 
 * @author Philippe T. Pinard
 */
public abstract class WizardPage extends org.netbeans.spi.wizard.WizardPage
        implements PreferenceKeeping {

    /** Holds data shared between wizard pages. */
    private HashMap<String, Object> results;

    /** Holds the preferences. */
    private Preferences preferences;



    @Override
    public WizardPanelNavResult allowFinish(String stepName,
            @SuppressWarnings("rawtypes") Map settings, Wizard wizard) {
        return (isCorrect(true)) ? WizardPanelNavResult.PROCEED
                : WizardPanelNavResult.REMAIN_ON_PAGE;
    }



    @Override
    public WizardPanelNavResult allowNext(String stepName,
            @SuppressWarnings("rawtypes") Map settings, Wizard wizard) {
        return (isCorrect(true)) ? WizardPanelNavResult.PROCEED
                : WizardPanelNavResult.REMAIN_ON_PAGE;
    }



    /**
     * Returns the value associated with the specified key in the shared
     * <code>HashMap</code>.
     * 
     * @param key
     *            key associated with the value to be returned
     * @throws NullPointerException
     *             if the key is null
     * @return the value associated with the specified key
     */
    public Object get(String key) {
        assert (results != null) : "Uninitialized hashMap";
        if (key == null)
            throw new NullPointerException("Key cannot be null.");

        return results.get(key);
    }



    /**
     * Invokes the child's static getDescription() method to return the page's
     * description.
     * 
     * @return page's description
     */
    private String getDescription() {
        try {
            Method method =
                    this.getClass().getDeclaredMethod("getDescription",
                            new Class[0]);
            if (!Modifier.isStatic(method.getModifiers()))
                throw new IllegalArgumentException(
                        "The getDescription() method is not static");

            return (String) method.invoke(this, new Object[0]);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException(
                    "No getDescription() method found in "
                            + this.getClass().getName(), e);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException("Cannot call "
                    + this.getClass().getName() + ".getDescription()", e);
        } catch (InvocationTargetException e) {
            throw new IllegalArgumentException(
                    "An exception occured during the execution of "
                            + this.getClass().getName() + ".getDescription()",
                    e);
        }
    }



    /**
     * Returns the preferences associated with this <code>WizardPage</code>.
     * 
     * @return the preferences associated with this <code>WizardPage</code> or
     *         <code>null</code> if no preferences have been associated.
     */
    @Override
    @CheckForNull
    public Preferences getPreferences() {
        return preferences;
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
     * @throws NullPointerException
     *             if the hashMap is null
     * @see #put
     * @see #get
     */
    protected void setHashMap(HashMap<String, Object> hashMap) {
        if (hashMap == null)
            throw new NullPointerException("HashMap cannot be null.");
        this.results = hashMap;
    }



    /**
     * Sets the preferences associated with this <code>WizardPage</code>. The
     * preferences will trickle down to all the WizardPage's components that
     * implement the <code>PreferenceKeeping</code> interface.
     * 
     * @param pref
     *            preferences to associate. Set to <code>null</code> to
     *            deactivate the preferences
     * @see PreferenceKeeping
     * @see #updatePreferences
     */
    @Override
    public void setPreferences(Preferences pref) {

        // Set the preference node to the page's name
        if (pref != null) {
            pref = pref.node(getDescription());
        }

        // Set the preference on all the page's components
        for (Component component : getComponents()) {
            if (component instanceof PreferenceKeeping) {
                ((PreferenceKeeping) component).setPreferences(pref);
            }
        }

        this.preferences = pref;
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



    /**
     * Updates the preferences. The update call will trickle down to all the
     * WizardPage's components that implement the <code>PreferenceKeeping</code>
     * interface. This trickling will happen even if no preferences have been
     * associated with the <code>WizardPage</code> as preferences might still
     * have been associated with some of the page's components.
     * <p>
     * <b>This method is not thread safe. It should be called on the event
     * thread.</b>
     * </p>
     * 
     * @see #setPreferences(Preferences)
     */
    @Override
    public void updatePreferences() {
        for (Component component : getComponents())
            if (component instanceof PreferenceKeeping)
                ((PreferenceKeeping) component).updatePreferences();
    }

}
