package ptpshared.util.simplexml;

import org.apache.commons.math.geometry.Rotation;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RotationTransformTest {

    private RotationTransform transform;



    @Before
    public void setUp() throws Exception {
        transform = new RotationTransform();
    }



    @Test
    public void testRead() throws Exception {
        String value = "1.0;2.0;3.0;4.0";

        Rotation expected = new Rotation(1.0, 2.0, 3.0, 4.0, false);
        Rotation actual = transform.read(value);

        assertEquals(expected.getQ0(), actual.getQ0(), 1e-6);
        assertEquals(expected.getQ1(), actual.getQ1(), 1e-6);
        assertEquals(expected.getQ2(), actual.getQ2(), 1e-6);
        assertEquals(expected.getQ3(), actual.getQ3(), 1e-6);
    }



    @Test
    public void testWrite() throws Exception {
        Rotation r = new Rotation(1.0, 2.0, 3.0, 4.0, false);

        String expected = "1.0;2.0;3.0;4.0";
        String actual = transform.write(r);

        assertEquals(expected, actual);
    }

}
