package ptpshared.util.simplexml;

import org.apache.commons.math.geometry.Rotation;
import org.simpleframework.xml.transform.Transform;

/**
 * Simple XML transform for Apache Common Math's <code>Rotation</code>.
 * 
 * @author ppinard
 */
public class RotationTransform implements Transform<Rotation> {

    @Override
    public Rotation read(String value) throws Exception {
        String[] qs = value.split(";");
        if (qs.length != 4)
            throw new IllegalArgumentException(
                    "String contains more than 4 values.");

        return new Rotation(Double.parseDouble(qs[0]),
                Double.parseDouble(qs[1]), Double.parseDouble(qs[2]),
                Double.parseDouble(qs[3]), false);
    }



    @Override
    public String write(Rotation value) throws Exception {
        return value.getQ0() + ";" + value.getQ1() + ";" + value.getQ2() + ";"
                + value.getQ3();
    }

}
