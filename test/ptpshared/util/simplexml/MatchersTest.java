package ptpshared.util.simplexml;

import org.junit.Before;
import org.junit.Test;
import org.simpleframework.xml.transform.Matcher;
import org.simpleframework.xml.transform.Transform;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class MatchersTest {

    private class MockObject {

    }

    private class MockTransform implements Transform<MockObject> {

        @Override
        public MockObject read(String value) throws Exception {
            return new MockObject();
        }



        @Override
        public String write(MockObject value) throws Exception {
            return "";
        }

    }

    private class MockMatcher implements Matcher {

        @SuppressWarnings("rawtypes")
        @Override
        public Transform match(Class type) throws Exception {
            if (type == MockObject.class)
                return new MockTransform();
            return null;
        }

    }

    private Matchers matchers;



    @Before
    public void setUp() throws Exception {
        matchers = new Matchers();
    }



    @Test
    public void testRegisterMatcher() {
        Matcher matcher = new MockMatcher();
        matchers.registerMatcher(matcher);
        assertTrue(matchers.isRegister(matcher));
    }



    @Test(expected = IllegalArgumentException.class)
    public void testRegisterMatcherException() {
        Matcher matcher = new MockMatcher();
        matchers.registerMatcher(matcher);
        matchers.registerMatcher(matcher);
    }



    @Test
    public void testDeregisterMatcher() {
        Matcher matcher = new MockMatcher();
        matchers.registerMatcher(matcher);
        assertTrue(matchers.isRegister(matcher));

        matchers.deregisterMatcher(matcher);
        assertFalse(matchers.isRegister(matcher));
    }



    @Test
    public void testClearMatchers() {
        Matcher matcher = new MockMatcher();
        matchers.registerMatcher(matcher);
        assertTrue(matchers.isRegister(matcher));

        matchers.clearMatchers();
        assertFalse(matchers.isRegister(matcher));
    }



    @Test
    public void testMatch() throws Exception {
        assertNull(matchers.match(MockObject.class));

        Matcher matcher = new MockMatcher();
        matchers.registerMatcher(matcher);

        assertNotNull(matchers.match(MockObject.class));
        assertNull(matchers.match(new Object().getClass()));
    }

}
