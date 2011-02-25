package ptpshared.util.simplexml;

import magnitude.core.Magnitude;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

import static junittools.test.Assert.assertEquals;

public class MagnitudeTransformTest {

    private MagnitudeTransform transform;



    @Before
    public void setUp() throws Exception {
        transform = new MagnitudeTransform();
    }



    @Test
    public void testRead() throws Exception {
        String value = "1.23 m.s-1";

        Magnitude expected = new Magnitude(1.23, "m.s-1");
        Magnitude actual = transform.read(value);

        assertEquals(expected, actual, 1e-6);
    }



    @Test
    public void testWrite() throws Exception {
        Magnitude m = new Magnitude(1.23, "m.s-1");

        String expected = "1.23 m.s-1";
        String actual = transform.write(m);

        assertEquals(expected, actual);
    }

}
