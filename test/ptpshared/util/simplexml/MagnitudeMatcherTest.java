package ptpshared.util.simplexml;

import magnitude.core.Magnitude;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class MagnitudeMatcherTest {

    private MagnitudeMatcher matcher;



    @Before
    public void setUp() throws Exception {
        matcher = new MagnitudeMatcher();
    }



    @Test
    public void testMatch() throws Exception {
        assertNotNull(matcher.match(Magnitude.class));
        assertNull(matcher.match(new Object().getClass()));
    }
}
