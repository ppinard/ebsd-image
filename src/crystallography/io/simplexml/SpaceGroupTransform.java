package crystallography.io.simplexml;

import org.simpleframework.xml.transform.Transform;

import crystallography.core.SpaceGroup;
import crystallography.core.SpaceGroups;

/**
 * Simple XML transform for the <code>SpaceGroup</code> class.
 * 
 * @author Philippe T. Pinard
 */
public class SpaceGroupTransform implements Transform<SpaceGroup> {

    @Override
    public SpaceGroup read(String value) throws Exception {
        return SpaceGroups.fromIndex(Integer.parseInt(value));
    }



    @Override
    public String write(SpaceGroup value) throws Exception {
        return Integer.toString(value.index);
    }

}
