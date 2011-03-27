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
package ptpshared.util.simplexml;

import java.util.ArrayList;

import org.simpleframework.xml.transform.Matcher;
import org.simpleframework.xml.transform.Transform;

/**
 * Utility to combine different Simple XML's matchers together before feeding
 * them to a <code>Serializer</code>.
 * 
 * @author Philippe T. Pinard
 */
public class Matchers implements Matcher {

    /** List of matchers. */
    private ArrayList<Matcher> matchers = new ArrayList<Matcher>();



    /**
     * Clears all registered matchers.
     */
    public void clearMatchers() {
        matchers.clear();
    }



    /**
     * De-register a matcher.
     * 
     * @param matcher
     *            matcher to de-register
     */
    public void deregisterMatcher(Matcher matcher) {
        if (matcher == null)
            throw new NullPointerException("Cannot deregister a null matcher.");
        if (!matchers.remove(matcher))
            throw new IllegalArgumentException("Matcher (" + matcher
                    + ") is not registered.");
    }



    /**
     * Returns whether the specified matcher is registered.
     * 
     * @param matcher
     *            a matcher
     * @return <code>true</code> if the specified matcher is registered,
     *         <code>false</code> otherwise
     */
    public boolean isRegister(Matcher matcher) {
        return matchers.contains(matcher);
    }



    @SuppressWarnings("rawtypes")
    @Override
    public Transform match(Class type) throws Exception {
        for (Matcher matcher : matchers) {
            Transform transform = matcher.match(type);
            if (transform != null)
                return transform;
        }
        return null;
    }



    /**
     * Register a matcher.
     * 
     * @param matcher
     *            new matcher
     */
    public void registerMatcher(Matcher matcher) {
        if (matcher == null)
            throw new NullPointerException("Cannot register a null matcher.");
        if (matchers.contains(matcher))
            throw new IllegalArgumentException("Matcher (" + matcher
                    + ") is already registered.");
        matchers.add(matcher);
    }
}
