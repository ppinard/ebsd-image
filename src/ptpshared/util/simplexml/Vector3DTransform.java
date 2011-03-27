package ptpshared.util.simplexml;

import org.apache.commons.math.geometry.Vector3D;
import org.simpleframework.xml.transform.Transform;

/**
 * Simple XML transform for Apache Common Math's <code>Vector3D</code>.
 * 
 * @author Philippe T. Pinard
 */
public class Vector3DTransform implements Transform<Vector3D> {

    @Override
    public Vector3D read(String value) throws Exception {
        String[] qs = value.split(";");
        if (qs.length != 3)
            throw new IllegalArgumentException(
                    "String contains more than 3 values.");

        return new Vector3D(Double.parseDouble(qs[0]),
                Double.parseDouble(qs[1]), Double.parseDouble(qs[2]));
    }



    @Override
    public String write(Vector3D value) throws Exception {
        return value.getX() + ";" + value.getY() + ";" + value.getZ();
    }

}
