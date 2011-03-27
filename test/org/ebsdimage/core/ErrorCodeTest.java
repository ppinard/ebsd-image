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
    public void testHashCode() {
        assertEquals(-167186582, errorCode.hashCode());
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
