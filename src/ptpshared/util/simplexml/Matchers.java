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
