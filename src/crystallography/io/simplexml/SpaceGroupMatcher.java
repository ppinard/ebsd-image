package crystallography.io.simplexml;

import org.simpleframework.xml.transform.Matcher;
import org.simpleframework.xml.transform.Transform;

import crystallography.core.SpaceGroup;

/**
 * Simple XML matcher for the <code>SpaceGroup</code> class.
 * 
 * @author ppinard
 */
public class SpaceGroupMatcher implements Matcher {

    @SuppressWarnings("rawtypes")
    @Override
    public Transform match(Class type) throws Exception {
        if (type == SpaceGroup.class)
            return new SpaceGroupTransform();
        return null;
    }

}
