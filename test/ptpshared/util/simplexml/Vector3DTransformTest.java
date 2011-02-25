package ptpshared.util.simplexml;

import org.apache.commons.math.geometry.Vector3D;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class Vector3DTransformTest {

    private Vector3DTransform transform;



    @Before
    public void setUp() throws Exception {
        transform = new Vector3DTransform();
    }



    @Test
    public void testRead() throws Exception {
        String value = "1.0;2.0;3.0";

        Vector3D expected = new Vector3D(1.0, 2.0, 3.0);
        Vector3D actual = transform.read(value);

        assertEquals(expected.getX(), actual.getX(), 1e-6);
        assertEquals(expected.getY(), actual.getY(), 1e-6);
        assertEquals(expected.getZ(), actual.getZ(), 1e-6);
    }



    @Test
    public void testWrite() throws Exception {
        Vector3D v = new Vector3D(1.0, 2.0, 3.0);

        String expected = "1.0;2.0;3.0";
        String actual = transform.write(v);

        assertEquals(expected, actual);
    }
}
