package ptpshared.util.simplexml;

import magnitude.core.Magnitude;

import org.simpleframework.xml.transform.Matcher;
import org.simpleframework.xml.transform.Transform;

/**
 * Simple XML matcher for the Magnitude library.
 * 
 * @author Philippe T. Pinard
 */
public class MagnitudeMatcher implements Matcher {

    @SuppressWarnings("rawtypes")
    @Override
    public Transform match(Class type) throws Exception {
        if (type == Magnitude.class)
            return new MagnitudeTransform();
        return null;
    }

}
