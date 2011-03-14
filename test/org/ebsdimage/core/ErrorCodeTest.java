package org.ebsdimage.core;

import java.io.File;
import java.io.IOException;

import org.ebsdimage.TestCase;
import org.junit.Before;
import org.junit.Test;

import ptpshared.util.simplexml.XmlLoader;
import ptpshared.util.simplexml.XmlSaver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ErrorCodeTest extends TestCase {

    private ErrorCode errorCode;



    @Before
    public void setUp() throws Exception {
        errorCode = new ErrorCode("Error1", "Desc1");
    }



    @Test
    public void testHashCode() {
        assertEquals(-167186582, errorCode.hashCode());
    }



    @Test
    public void testErrorCodeIntString() {
        ErrorCode errorCode = new ErrorCode("Error2");

        assertEquals("Error2", errorCode.name);
        assertEquals("", errorCode.description);
    }



    @Test
    public void testErrorCodeIntStringString() {
        assertEquals("Error1", errorCode.name);
        assertEquals("Desc1", errorCode.description);
    }



    @Test
    public void testEqualsObject() {
        assertTrue(errorCode.equals(errorCode));
        assertFalse(errorCode.equals(null));
        assertFalse(errorCode.equals(new Object()));

        ErrorCode other = new ErrorCode("Error1", "Desc1");
        assertTrue(errorCode.equals(other));

        other = new ErrorCode("Error2", "Desc1");
        assertFalse(errorCode.equals(other));

        other = new ErrorCode("Error1", "Desc2");
        assertFalse(errorCode.equals(other));
    }



    @Test
    public void testToString() {
        String expected = "ErrorCode [name=Error1, description=Desc1]";
        assertEquals(expected, errorCode.toString());
    }



    @Test
    public void testXML() throws IOException {
        File file = createTempFile();

        new XmlSaver().save(errorCode, file);

        ErrorCode other = new XmlLoader().load(ErrorCode.class, file);
        assertEquals("Error1", other.name);
        assertEquals("Desc1", other.description);
    }

}
