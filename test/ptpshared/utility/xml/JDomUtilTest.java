/*
 * EBSD-Image
 * Copyright (C) 2010 Philippe T. Pinard
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
package ptpshared.utility.xml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import rmlshared.io.FileUtil;
import rmlshared.util.Properties;

public class JDomUtilTest {

    private Element element1;

    private Element element2;

    private Element element3;

    private Element element4;

    private Element element5;

    private Element element6;

    private File tmpFile;



    @Before
    public void setUp() throws Exception {
        // Element
        element1 = new Element("test");
        element1.setAttribute("attr1", Double.toString(1.5234));

        element2 = new Element("test");
        element2.setAttribute("attr1", Integer.toString(99));

        element3 = new Element("test");
        element3.setAttribute("attr1", "abc");

        element4 = new Element("test");
        element4.setAttribute("attr1", "true");
        element4.setAttribute("attr2", "false");

        // Element 5
        element5 = new Element("root");
        Element child1 = new Element("child1");
        child1.setAttribute("attr1", Double.toString(1.5234));
        element5.addContent(child1);

        Element child2 = new Element("child2");
        child2.setAttribute("attr1", Integer.toString(99));
        element5.addContent(child2);

        Element child3 = new Element("child3");
        child3.setAttribute("attr1", "abc");
        element5.addContent(child3);

        Element child4 = new Element("child4");
        child4.setAttribute("attr1", "true");
        child4.setAttribute("attr2", "false");
        element5.addContent(child4);

        // Element 6 for toProperties
        element6 = new Element("root");
        element6.setAttribute("attr1", "r");

        Element subLevel1 = new Element("level1A");
        subLevel1.setAttribute("attr1", "1A");
        subLevel1.setText("text1A");
        Element subLevel2 = new Element("level2A");
        subLevel2.setAttribute("attr1", "2A");
        Element subLevel3 = new Element("level3A");
        subLevel3.setAttribute("attr1", "3A");
        subLevel3.setText("text3A");

        subLevel2.addContent(subLevel3);
        subLevel1.addContent(subLevel2);
        element6.addContent(subLevel1);

        Element subLevel4 = new Element("level1B");
        subLevel4.setText("text1B");
        Element subLevel5 = new Element("level2B");
        subLevel5.setAttribute("attr1", "1B-1");
        subLevel5.setAttribute("attr2", "1B-2");

        subLevel4.addContent(subLevel5);
        element6.addContent(subLevel4);

        // Temp file
        tmpFile = new File(FileUtil.getTempDirFile(), "test.xml");
    }



    @After
    public void tearDown() throws Exception {
        if (tmpFile.exists())
            if (!(tmpFile.delete()))
                throw new RuntimeException("File (" + tmpFile.getAbsolutePath()
                        + ") cannot be deleted.");
    }



    @Test
    public void testGetAttribute() {
        String expected = "attr1";
        String attrName = JDomUtil.getAttribute(element1, "attr1").getName();
        assertEquals(expected, attrName);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testGetAttributeException() {
        JDomUtil.getAttribute(element1, "attr2");
    }



    @Test
    public void testGetBooleanFromAttributeElementString() {
        boolean value;

        value = JDomUtil.getBooleanFromAttribute(element4, "attr1");
        assertEquals(true, value);

        value = JDomUtil.getBooleanFromAttribute(element4, "attr2");
        assertEquals(false, value);
    }



    @Test
    public void testGetBooleanFromAttributeElementStringBoolean() {
        boolean value;

        value = JDomUtil.getBooleanFromAttribute(element4, "attr1", false);
        assertEquals(true, value);

        value = JDomUtil.getBooleanFromAttribute(element4, "attr99", true);
        assertEquals(true, value);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testGetBooleanFromAttributeElementStringException1() {
        JDomUtil.getBooleanFromAttribute(element4, "attr99");
    }



    @Test(expected = IllegalArgumentException.class)
    public void testGetBooleanFromAttributeElementStringException2() {
        element4.setAttribute("attr1", "a");
        JDomUtil.getBooleanFromAttribute(element4, "attr1");
    }



    @Test
    public void testGetBooleanFromAttributeElementStringString() {
        boolean value;

        value = JDomUtil.getBooleanFromAttribute(element5, "child4", "attr1");
        assertEquals(true, value);

        value = JDomUtil.getBooleanFromAttribute(element5, "child4", "attr2");
        assertEquals(false, value);
    }



    @Test
    public void testGetBooleanFromAttributeElementStringStringBoolean() {
        boolean value;

        value =
                JDomUtil.getBooleanFromAttribute(element5, "child4", "attr1",
                        false);
        assertEquals(true, value);

        value =
                JDomUtil.getBooleanFromAttribute(element5, "child4", "attr99",
                        true);
        assertEquals(true, value);

        value =
                JDomUtil.getBooleanFromAttribute(element5, "child99", "attr1",
                        true);
        assertEquals(true, value);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testGetBooleanFromAttributeElementStringStringException1() {
        JDomUtil.getBooleanFromAttribute(element5, "child4", "attr99");
    }



    @Test(expected = IllegalArgumentException.class)
    public void testGetBooleanFromAttributeElementStringStringException2() {
        JDomUtil.getBooleanFromAttribute(element5, "child3", "attr1");
    }



    @Test(expected = IllegalArgumentException.class)
    public void testGetBooleanFromAttributeElementStringStringException3() {
        JDomUtil.getBooleanFromAttribute(element5, "child99", "attr1");
    }



    @Test
    public void testGetChild() {
        String expected = "child1";
        String childName = JDomUtil.getChild(element5, "child1").getName();
        assertEquals(expected, childName);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testGetChildException() {
        JDomUtil.getChild(element5, "child99");
    }



    @Test
    public void testGetDoubleFromAttributeElementString() {
        double value = JDomUtil.getDoubleFromAttribute(element1, "attr1");
        assertEquals(1.5234, value, 1e-7);
    }



    @Test
    public void testGetDoubleFromAttributeElementStringDouble() {
        double value;

        value = JDomUtil.getDoubleFromAttribute(element1, "attr1", 1235.6);
        assertEquals(1.5234, value, 1e-7);

        value = JDomUtil.getDoubleFromAttribute(element1, "attr99", 1235.6);
        assertEquals(1235.6, value, 1e-7);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testGetDoubleFromAttributeElementStringException1() {
        JDomUtil.getDoubleFromAttribute(element1, "attr99");
    }



    @Test(expected = IllegalArgumentException.class)
    public void testGetDoubleFromAttributeElementStringException2() {
        element1.setAttribute("attr1", "a");
        JDomUtil.getDoubleFromAttribute(element1, "attr1");
    }



    @Test
    public void testGetDoubleFromAttributeElementStringString() {
        double value =
                JDomUtil.getDoubleFromAttribute(element5, "child1", "attr1");
        assertEquals(1.5234, value, 1e-7);
    }



    @Test
    public void testGetDoubleFromAttributeElementStringStringDouble() {
        double value;

        value =
                JDomUtil.getDoubleFromAttribute(element5, "child1", "attr1",
                        1235.6);
        assertEquals(1.5234, value, 1e-7);

        value =
                JDomUtil.getDoubleFromAttribute(element5, "child1", "attr99",
                        1235.6);
        assertEquals(1235.6, value, 1e-7);

        value =
                JDomUtil.getDoubleFromAttribute(element5, "child99", "attr1",
                        1235.6);
        assertEquals(1235.6, value, 1e-7);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testGetDoubleFromAttributeElementStringStringException1() {
        JDomUtil.getDoubleFromAttribute(element5, "child1", "attr2");
    }



    @Test(expected = IllegalArgumentException.class)
    public void testGetDoubleFromAttributeElementStringStringException2() {
        JDomUtil.getDoubleFromAttribute(element5, "child3", "attr99");
    }



    @Test(expected = IllegalArgumentException.class)
    public void testGetDoubleFromAttributeElementStringStringException3() {
        JDomUtil.getDoubleFromAttribute(element5, "child99", "attr1");
    }



    @Test
    public void testGetIntegerFromAttributeElementString() {
        int value = JDomUtil.getIntegerFromAttribute(element2, "attr1");
        assertEquals(99, value);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testGetIntegerFromAttributeElementStringException1() {
        JDomUtil.getIntegerFromAttribute(element2, "attr99");
    }



    @Test(expected = IllegalArgumentException.class)
    public void testGetIntegerFromAttributeElementStringException2() {
        element2.setAttribute("attr1", "a");
        JDomUtil.getIntegerFromAttribute(element2, "attr1");
    }



    @Test
    public void testGetIntegerFromAttributeElementStringInt() {
        int value;

        value = JDomUtil.getIntegerFromAttribute(element2, "attr1", 1235);
        assertEquals(99, value);

        value = JDomUtil.getIntegerFromAttribute(element2, "attr99", 1235);
        assertEquals(1235, value);
    }



    @Test
    public void testGetIntegerFromAttributeElementStringString() {
        int value =
                JDomUtil.getIntegerFromAttribute(element5, "child2", "attr1");
        assertEquals(99, value, 1e-7);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testGetIntegerFromAttributeElementStringStringException1() {
        JDomUtil.getIntegerFromAttribute(element5, "child2", "attr99");
    }



    @Test(expected = IllegalArgumentException.class)
    public void testGetIntegerFromAttributeElementStringStringException2() {
        JDomUtil.getIntegerFromAttribute(element5, "child3", "attr1");
    }



    @Test(expected = IllegalArgumentException.class)
    public void testGetIntegerFromAttributeElementStringStringException3() {
        JDomUtil.getIntegerFromAttribute(element5, "child99", "attr1");
    }



    @Test
    public void testGetIntegerFromAttributeElementStringStringInt() {
        int value;

        value =
                JDomUtil.getIntegerFromAttribute(element5, "child2", "attr1",
                        1235);
        assertEquals(99, value, 1e-7);

        value =
                JDomUtil.getIntegerFromAttribute(element5, "child2", "attr99",
                        1235);
        assertEquals(1235, value, 1e-7);

        value =
                JDomUtil.getIntegerFromAttribute(element5, "child99", "attr1",
                        1235);
        assertEquals(1235, value, 1e-7);
    }



    @Test
    public void testGetStringFromAttributeElementDefaultStringStringString() {
        String value;

        value =
                JDomUtil.getStringFromAttributeDefault(element3, "attr1", "def");
        assertEquals("abc", value);

        value =
                JDomUtil.getStringFromAttributeDefault(element3, "attr99",
                        "def");
        assertEquals("def", value);
    }



    @Test
    public void testGetStringFromAttributeElementDefaultStringStringStringString() {
        String value;

        value =
                JDomUtil.getStringFromAttributeDefault(element5, "child3",
                        "attr1", "def");
        assertEquals("abc", value);

        value =
                JDomUtil.getStringFromAttributeDefault(element5, "child3",
                        "attr99", "def");
        assertEquals("def", value);

        value =
                JDomUtil.getStringFromAttributeDefault(element5, "child99",
                        "attr1", "def");
        assertEquals("def", value);
    }



    @Test
    public void testGetStringFromAttributeElementString() {
        String value = JDomUtil.getStringFromAttribute(element3, "attr1");
        assertEquals("abc", value);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testGetStringFromAttributeElementStringException1() {
        JDomUtil.getStringFromAttribute(element3, "attr2");
    }



    @Test
    public void testGetStringFromAttributeElementStringString() {
        String value =
                JDomUtil.getStringFromAttribute(element5, "child3", "attr1");
        assertEquals("abc", value);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testGetStringFromAttributeElementStringStringException1() {
        JDomUtil.getStringFromAttribute(element5, "child3", "attr99");
    }



    @Test(expected = IllegalArgumentException.class)
    public void testGetStringFromAttributeElementStringStringException2() {
        JDomUtil.getStringFromAttribute(element5, "child99", "attr1");
    }



    @Test
    public void testHasAttribute() {
        assertTrue(JDomUtil.hasAttribute(element1, "attr1"));
        assertFalse(JDomUtil.hasAttribute(element1, "attr99"));
    }



    @Test
    public void testHasChild() {
        assertTrue(JDomUtil.hasChild(element5, "child1"));
        assertFalse(JDomUtil.hasChild(element5, "child99"));
    }



    @Test
    public void testLoadXML() throws IOException, JDOMException {
        File file = FileUtil.getFile("ptpshared/testdata/test.xml");
        Element element = JDomUtil.loadXML(file).getRootElement();

        assertTrue(JDomUtil.hasChild(element, "child1"));
        assertTrue(JDomUtil.hasChild(element, "child2"));
        assertEquals("text1", element.getChild("child1").getText());
        assertEquals("text2", element.getChild("child2").getText());
        assertEquals("a",
                JDomUtil.getStringFromAttribute(element, "child1", "attr"));
        assertEquals("b",
                JDomUtil.getStringFromAttribute(element, "child2", "attr"));
    }



    @Test
    public void testSaveXMLDocumentFile() throws IOException, JDOMException {
        // Save
        Document doc = new Document(element1);
        JDomUtil.saveXML(doc, tmpFile);

        // Load
        Element element = JDomUtil.loadXML(tmpFile).getRootElement();

        // Tests
        assertEquals(1.5234, JDomUtil.getDoubleFromAttribute(element, "attr1"),
                1e-4);
    }



    @Test
    public void testSaveXMLElementFile() throws IOException, JDOMException {
        // Save
        JDomUtil.saveXML(element1, tmpFile);

        // Load
        Element element = JDomUtil.loadXML(tmpFile).getRootElement();

        // Tests
        assertEquals(1.5234, JDomUtil.getDoubleFromAttribute(element, "attr1"),
                1e-4);
    }



    @Test
    public void testToPropertiesElement() {
        Properties props = JDomUtil.toProperties(element6);

        assertEquals(9, props.size());
        assertEquals("r", props.getProperty("root.attr1"));

        assertEquals("1A", props.getProperty("level1A.attr1"));
        assertEquals("text1A", props.getProperty("level1A"));
        assertEquals("2A", props.getProperty("level1A.level2A.attr1"));
        assertEquals("3A", props.getProperty("level1A.level2A.level3A.attr1"));
        assertEquals("text3A", props.getProperty("level1A.level2A.level3A"));

        assertEquals("text1B", props.getProperty("level1B"));
        assertEquals("1B-1", props.getProperty("level1B.level2B.attr1"));
        assertEquals("1B-2", props.getProperty("level1B.level2B.attr2"));
    }



    @Test
    public void testToPropertiesElementString() {
        Properties props = JDomUtil.toProperties(element6, '_');

        assertEquals(9, props.size());
        assertEquals("r", props.getProperty("root_attr1"));

        assertEquals("1A", props.getProperty("level1A_attr1"));
        assertEquals("text1A", props.getProperty("level1A"));
        assertEquals("2A", props.getProperty("level1A_level2A_attr1"));
        assertEquals("3A", props.getProperty("level1A_level2A_level3A_attr1"));
        assertEquals("text3A", props.getProperty("level1A_level2A_level3A"));

        assertEquals("text1B", props.getProperty("level1B"));
        assertEquals("1B-1", props.getProperty("level1B_level2B_attr1"));
        assertEquals("1B-2", props.getProperty("level1B_level2B_attr2"));
    }

}
