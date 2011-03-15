package ptpshared.geom;

import org.apache.commons.math.geometry.Rotation;
import org.apache.commons.math.geometry.Vector3D;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RotationUtilsTest {

    private Rotation r;



    @Before
    public void setUp() throws Exception {
        r = new Rotation(new Vector3D(1, 0, 0), Math.PI / 3);
    }



    @Test
    public void testEqualsRotationRotationDouble() {
        assertFalse(RotationUtils.equals(r, null, 1e-6));
        assertFalse(RotationUtils.equals(null, r, 1e-6));

        Rotation other = new Rotation(0.8660254037844387, -0.5, 0, 0, false);
        assertTrue(RotationUtils.equals(r, other, 1e-6));

        other = new Rotation(new Vector3D(-1, 0, 0), Math.PI / 3);
        assertFalse(RotationUtils.equals(r, other, 1e-6));

        other = new Rotation(new Vector3D(1, 0, 0), -Math.PI / 3);
        assertFalse(RotationUtils.equals(r, other, 1e-6));

        other = new Rotation(new Vector3D(-1, 0, 0), -Math.PI / 3);
        assertTrue(RotationUtils.equals(r, other, 1e-6));
    }



    @Test
    public void testToString() {
        String expected =
                "[[0.8660254037844387; -0.49999999999999994; -0.0; -0.0]]";
        assertEquals(expected, RotationUtils.toString(r));
    }

}
