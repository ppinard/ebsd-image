package ptpshared.util.simplexml;

import org.apache.commons.math.geometry.Rotation;
import org.apache.commons.math.geometry.Vector3D;
import org.simpleframework.xml.transform.Matcher;
import org.simpleframework.xml.transform.Transform;

/**
 * Simple XML matcher for some classes of the Apache Common Math library.
 * 
 * @author Philippe T. Pinard
 */
public class ApacheCommonMathMatcher implements Matcher {

    @SuppressWarnings("rawtypes")
    @Override
    public Transform match(Class type) throws Exception {
        if (type == Rotation.class)
            return new RotationTransform();
        else if (type == Vector3D.class)
            return new Vector3DTransform();
        return null;
    }

}
