package ptpshared.geom;

import java.awt.geom.Line2D;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class Line2DUtilsTest {

    private Line2D line;



    @Before
    public void setUp() throws Exception {
        line = new Line2D.Double(0, 1, 2, 3);
    }



    @Test
    public void testEqualsLine2DLine2DDouble() {
        assertFalse(Line2DUtils.equals(line, null, 1e-3));
        assertFalse(Line2DUtils.equals(null, line, 1e-3));

        Line2D other = new Line2D.Double(0.001, 1.001, 2.001, 3.001);
        assertTrue(Line2DUtils.equals(line, other, 1e-3));

        other = new Line2D.Double(2.001, 3.001, 0.001, 1.001);
        assertTrue(Line2DUtils.equals(line, other, 1e-3));

        other = new Line2D.Double(0.1, 1.001, 2.001, 3.001);
        assertFalse(Line2DUtils.equals(line, other, 1e-3));

        other = new Line2D.Double(0.001, 1.1, 2.001, 3.001);
        assertFalse(Line2DUtils.equals(line, other, 1e-3));

        other = new Line2D.Double(0.001, 1.001, 2.1, 3.001);
        assertFalse(Line2DUtils.equals(line, other, 1e-3));

        other = new Line2D.Double(0.001, 1.001, 2.001, 3.1);
        assertFalse(Line2DUtils.equals(line, other, 1e-3));
    }

}
