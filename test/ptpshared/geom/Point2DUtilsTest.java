package ptpshared.geom;

import java.awt.geom.Point2D;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class Point2DUtilsTest {

    private Point2D p;



    @Before
    public void setUp() throws Exception {
        p = new Point2D.Double(1, 2);
    }



    @Test
    public void testEqualsPoint2DPoint2DDouble() {
        assertFalse(Point2DUtils.equals(p, null, 1e-6));
        assertFalse(Point2DUtils.equals(null, p, 1e-6));

        Point2D other = new Point2D.Double(1.001, 2.001);
        assertTrue(Point2DUtils.equals(p, other, 1e-3));

        other = new Point2D.Double(1.1, 2.001);
        assertFalse(Point2DUtils.equals(p, other, 1e-3));

        other = new Point2D.Double(1.001, 2.1);
        assertFalse(Point2DUtils.equals(p, other, 1e-3));
    }



    @Test
    public void testToStringPoint2D() {
        String expected = "(1.0;2.0)";
        assertEquals(expected, Point2DUtils.toString(p));

    }

}
