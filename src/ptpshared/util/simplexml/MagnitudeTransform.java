package ptpshared.util.simplexml;

import magnitude.core.Magnitude;

import org.simpleframework.xml.transform.Transform;

/**
 * Simple XML transform for the Magnitude library.
 * 
 * @author ppinard
 */
public class MagnitudeTransform implements Transform<Magnitude> {

    @Override
    public Magnitude read(String value) throws Exception {
        String[] parts = value.split(" ", 2);
        return new Magnitude(Double.parseDouble(parts[0]), parts[1]);
    }



    @Override
    public String write(Magnitude value) throws Exception {
        return value.toString();
    }

}
