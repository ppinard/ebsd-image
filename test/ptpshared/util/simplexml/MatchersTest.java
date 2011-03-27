/*
 * EBSD-Image
 * Copyright (C) 2010-2011 Philippe T. Pinard
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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

    private class MockMatcher implements Matcher {

        @SuppressWarnings("rawtypes")
        @Override
        public Transform match(Class type) throws Exception {
            if (type == MockObject.class)
                return new MockTransform();
            return null;
        }

    }

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

    private Matchers matchers;



    @Before
    public void setUp() throws Exception {
        matchers = new Matchers();
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
    public void testDeregisterMatcher() {
        Matcher matcher = new MockMatcher();
        matchers.registerMatcher(matcher);
        assertTrue(matchers.isRegister(matcher));

        matchers.deregisterMatcher(matcher);
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

}
