package crystallography.io.simplexml;

import org.junit.Before;
import org.junit.Test;

import crystallography.core.SpaceGroup;
import crystallography.core.SpaceGroups;

import static org.junit.Assert.assertEquals;

public class SpaceGroupTransformTest {

    private SpaceGroupTransform transform;



    @Before
    public void setUp() throws Exception {
        transform = new SpaceGroupTransform();
    }



    @Test
    public void testRead() throws Exception {
        String value = "216";

        SpaceGroup expected = SpaceGroups.fromIndex(216);
        SpaceGroup actual = transform.read(value);

        assertEquals(expected, actual);
    }



    @Test
    public void testWrite() throws Exception {
        SpaceGroup m = SpaceGroups.fromIndex(216);

        String expected = "216";
        String actual = transform.write(m);

        assertEquals(expected, actual);
    }

}
