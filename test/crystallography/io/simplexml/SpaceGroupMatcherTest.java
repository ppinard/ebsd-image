package crystallography.io.simplexml;

import org.junit.Before;
import org.junit.Test;

import crystallography.core.SpaceGroup;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class SpaceGroupMatcherTest {
    private SpaceGroupMatcher matcher;



    @Before
    public void setUp() throws Exception {
        matcher = new SpaceGroupMatcher();
    }



    @Test
    public void testMatch() throws Exception {
        assertNotNull(matcher.match(SpaceGroup.class));
        assertNull(matcher.match(new Object().getClass()));
    }
}
