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
package ptpshared.util.xml;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.logging.Logger;

import org.jdom.*;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import rmlshared.io.FileUtil;
import rmlshared.util.Properties;

/**
 * Utilities to use in conjunction with org.jdom.
 * 
 * @author Philippe T. Pinard
 */
public class JDomUtil {

    /**
     * Used in {@link #toProperties(Element)} to add XML attributes to the
     * properties.
     * 
     * @param props
     *            <code>Properties</code> object
     * @param element
     *            <code>Element</code> containing the attributes
     * @param prefix
     *            prefix to add to each property key
     * @param separator
     *            character to use as a separator in the property key between
     *            the prefix and the attribute
     * @return <code>Properties</code> object with the added attributes
     */
    private static Properties addAttributesToProperties(Properties props,
            Element element, String prefix, char separator) {
        Iterator<?> itrAttr = element.getAttributes().iterator();

        while (itrAttr.hasNext()) {
            Attribute attr = (Attribute) itrAttr.next();

            String key = prefix + separator + attr.getName();
            String value = attr.getValue();
            props.setProperty(key, value);
        }

        return props;
    }



    /**
     * Finds the prefix based on the parent(s) of an <code>Element</code>.
     * 
     * @param element
     *            child <code>Element</code> to start the search from
     * @param root
     *            root <code>Element</code>
     * @param separator
     *            character to use as a separator between the parent(s)
     * @return all the parents separated by the <code>separator</code>
     */
    private static String findPrefixFromParents(Element element, Element root,
            char separator) {
        StringBuilder parentKey = new StringBuilder();

        while (element != root) {
            parentKey.insert(0, element.getName() + separator);
            element = element.getParentElement();
        }
        parentKey.setLength(parentKey.length() - 1);

        return parentKey.toString();
    }



    /**
     * Returns an <code>Attribute</code> from an XML <code>Element</code> which
     * has the name <code>attrName</code>.
     * 
     * @param element
     *            an XML <code>Element</code> containing the attribute
     * @param attrName
     *            name of the attribute
     * @return found <code>Attribute</code>
     * @throws IllegalArgumentException
     *             if <code>Attribute</code> with the name <code>attrName</code>
     *             is not found in the <code>element</code>.
     */
    public static Attribute getAttribute(Element element, String attrName) {
        Attribute attr = element.getAttribute(attrName);

        if (attr == null) // Check that attribute exists
            throw new IllegalArgumentException("Element must contains "
                    + "the attribute `" + attrName + "`");

        return attr;
    }



    /**
     * Returns a boolean from an attribute.
     * 
     * @param element
     *            an XML <code>Element</code> containing the attribute
     * @param attrName
     *            name of the attribute
     * @return boolean value of the attribute
     * @throws IllegalArgumentException
     *             if the attribute doesn't exist or its value cannot be parsed
     *             as a boolean
     */
    public static boolean getBooleanFromAttribute(Element element,
            String attrName) {
        Attribute attr = getAttribute(element, attrName);

        // Check that the attribute value is a boolean
        try {
            return attr.getBooleanValue();
        } catch (DataConversionException e) {
            throw new IllegalArgumentException("Value (" + attr.toString()
                    + ") of attribute (" + attrName
                    + ") could not be converted to boolean.");
        }
    }



    /**
     * Returns a boolean from an attribute.
     * 
     * @param element
     *            an XML <code>Element</code> containing the attribute
     * @param attrName
     *            name of the attribute
     * @param defaultValue
     *            default value in case of an exception
     * @return boolean value of the attribute. If the attribute doesn't exist or
     *         its value cannot be parsed as a boolean, the
     *         <code>defaultValue</code> is returned.
     */
    public static boolean getBooleanFromAttribute(Element element,
            String attrName, boolean defaultValue) {
        try {
            return getBooleanFromAttribute(element, attrName);
        } catch (IllegalArgumentException e) {
            Logger logger = Logger.getLogger("ptpshared.utility");
            logger.warning("Attribute " + attrName + " of element "
                    + element.getName()
                    + " could not be converted to a boolean. "
                    + "Using default value (" + defaultValue + ")");
            return defaultValue;
        }
    }



    /**
     * Returns a boolean from an attribute of a child XML <code>Element</code>
     * of <code>element</code>.
     * 
     * @param element
     *            an XML <code>Element</code>
     * @param childName
     *            name of the child of <code>element</code> containing the
     *            attribute
     * @param attrName
     *            name of the attribute
     * @return boolean value of the attribute
     * @throws IllegalArgumentException
     *             if the child or the attribute don't exist or the attribute
     *             value cannot be parsed as a boolean
     */
    public static boolean getBooleanFromAttribute(Element element,
            String childName, String attrName) {
        Element child = getChild(element, childName);

        return getBooleanFromAttribute(child, attrName);
    }



    /**
     * Returns a boolean from an attribute of a child XML <code>Element</code>
     * of <code>element</code>.
     * 
     * @param element
     *            an XML <code>Element</code>
     * @param childName
     *            name of the child of <code>element</code> containing the
     *            attribute
     * @param attrName
     *            name of the attribute
     * @param defaultValue
     *            default value in case of an exception
     * @return boolean value of the attribute. If the attribute or child don't
     *         exist or the attribute value cannot be parsed as a boolean, the
     *         <code>defaultValue</code> is returned.
     */
    public static boolean getBooleanFromAttribute(Element element,
            String childName, String attrName, boolean defaultValue) {
        try {
            return getBooleanFromAttribute(element, childName, attrName);
        } catch (IllegalArgumentException e) {
            Logger logger = Logger.getLogger("ptpshared.utility");
            logger.warning("Attribute " + attrName + " of element "
                    + element.getName()
                    + " could not be converted to a boolean. "
                    + "Using default value (" + defaultValue + ")");
            return defaultValue;
        }
    }



    /**
     * Returns a child XML <code>Element</code> from <code>element</code> which
     * has the name <code>childName</code>.
     * 
     * @param element
     *            an XML <code>Element</code> containing the child
     * @param childName
     *            name of the child
     * @return found XML <code>Element</code>
     * @throws IllegalArgumentException
     *             if a child with the name <code>childName</code> is not found
     *             in the <code>element</code>.
     */
    public static Element getChild(Element element, String childName) {
        Element child = element.getChild(childName);

        if (child == null)
            throw new IllegalArgumentException("Element must have "
                    + "a child `" + childName + "`");

        return child;
    }



    /**
     * Returns a double from an attribute.
     * 
     * @param element
     *            an XML <code>Element</code> containing the attribute
     * @param attrName
     *            name of the attribute
     * @return double value of the attribute
     * @throws IllegalArgumentException
     *             if the attribute doesn't exist or its value cannot be parsed
     *             as a double
     */
    public static double getDoubleFromAttribute(Element element, String attrName) {
        Attribute attr = getAttribute(element, attrName);

        // Check that the attribute value is a double
        try {
            return attr.getDoubleValue();
        } catch (DataConversionException e) {
            throw new IllegalArgumentException("Value (" + attr.toString()
                    + ") of attribute (" + attrName
                    + ") could not be converted to double.");
        }
    }



    /**
     * Returns a double from an attribute.
     * 
     * @param element
     *            an XML <code>Element</code> containing the attribute
     * @param attrName
     *            name of the attribute
     * @param defaultValue
     *            default value in case of an exception
     * @return double value of the attribute. If the attribute doesn't exist or
     *         its value cannot be parsed as a double, the
     *         <code>defaultValue</code> is returned.
     */
    public static double getDoubleFromAttribute(Element element,
            String attrName, double defaultValue) {
        try {
            return getDoubleFromAttribute(element, attrName);
        } catch (IllegalArgumentException e) {
            Logger logger = Logger.getLogger("ptpshared.utility");
            logger.warning("Attribute " + attrName + " of element "
                    + element.getName()
                    + " could not be converted to a double. "
                    + "Using default value (" + defaultValue + ")");
            return defaultValue;
        }
    }



    /**
     * Returns a double from an attribute of a child XML <code>Element</code> of
     * <code>element</code>.
     * 
     * @param element
     *            an XML <code>Element</code>
     * @param childName
     *            name of the child of <code>element</code> containing the
     *            attribute
     * @param attrName
     *            name of the attribute
     * @return double value of the attribute
     * @throws IllegalArgumentException
     *             if the child or the attribute don't exist or the attribute
     *             value cannot be parsed as a double
     */
    public static double getDoubleFromAttribute(Element element,
            String childName, String attrName) {
        Element child = getChild(element, childName);

        return getDoubleFromAttribute(child, attrName);
    }



    /**
     * Returns a double from an attribute of a child XML <code>Element</code> of
     * <code>element</code>.
     * 
     * @param element
     *            an XML <code>Element</code>
     * @param childName
     *            name of the child of <code>element</code> containing the
     *            attribute
     * @param attrName
     *            name of the attribute
     * @param defaultValue
     *            default value in case of an exception
     * @return double value of the attribute. If the attribute or child don't
     *         exist or the attribute value cannot be parsed as a double, the
     *         <code>defaultValue</code> is returned.
     */
    public static double getDoubleFromAttribute(Element element,
            String childName, String attrName, double defaultValue) {
        try {
            return getDoubleFromAttribute(element, childName, attrName);
        } catch (IllegalArgumentException e) {
            Logger logger = Logger.getLogger("ptpshared.utility");
            logger.warning("Attribute " + attrName + " of element "
                    + element.getName()
                    + " could not be converted to a double. "
                    + "Using default value (" + defaultValue + ")");
            return defaultValue;
        }
    }



    /**
     * Returns a double from the XML <code>Element</code>'s text.
     * 
     * @param element
     *            an XML <code>Element</code>
     * @return double value of the text
     * @throws IllegalArgumentException
     *             if the text cannot be parsed as a double
     */
    public static double getDoubleFromText(Element element) {
        String text = element.getText();

        try {
            return Double.parseDouble(text);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Value (" + text
                    + ") of element (" + element.getName()
                    + ") could not be converted to double.");
        }
    }



    /**
     * Returns a double from the XML <code>Element</code>'s text.
     * 
     * @param element
     *            an XML <code>Element</code>
     * @param defaultValue
     *            default value
     * @return double value of the text. If the text value cannot be parsed as a
     *         double, the <code>defaultValue</code> is returned.
     */
    public static double getDoubleFromText(Element element, double defaultValue) {
        try {
            return getDoubleFromText(element);
        } catch (NumberFormatException e) {
            Logger logger = Logger.getLogger("ptpshared.utility");
            logger.warning("Text of element " + element.getName()
                    + " could not be converted to a double. "
                    + "Using default value (" + defaultValue + ")");
            return defaultValue;
        }
    }



    /**
     * Returns an integer from an attribute.
     * 
     * @param element
     *            an XML <code>Element</code> containing the attribute
     * @param attrName
     *            name of the attribute
     * @return integer value of the attribute
     * @throws IllegalArgumentException
     *             if the attribute doesn't exist or its value cannot be parsed
     *             as an integer
     */
    public static int getIntegerFromAttribute(Element element, String attrName) {
        Attribute attr = getAttribute(element, attrName);

        // Check that the attribute value is a double
        try {
            return attr.getIntValue();
        } catch (DataConversionException e) {
            throw new IllegalArgumentException("Value (" + attr.toString()
                    + ") of attribute (" + attrName
                    + ") could not be converted to integer.");
        }
    }



    /**
     * Returns an integer from an attribute.
     * 
     * @param element
     *            an XML <code>Element</code> containing the attribute
     * @param attrName
     *            name of the attribute
     * @param defaultValue
     *            default value in case of an exception
     * @return integer value of the attribute. If the attribute doesn't exist or
     *         its value cannot be parsed as a integer, the
     *         <code>defaultValue</code> is returned.
     */
    public static int getIntegerFromAttribute(Element element, String attrName,
            int defaultValue) {
        try {
            return getIntegerFromAttribute(element, attrName);
        } catch (IllegalArgumentException e) {
            Logger logger = Logger.getLogger("ptpshared.utility");
            logger.warning("Attribute " + attrName + " of element "
                    + element.getName()
                    + " could not be converted to an integer. "
                    + "Using default value (" + defaultValue + ")");
            return defaultValue;
        }
    }



    /**
     * Returns an integer from an attribute of a child XML <code>Element</code>
     * of <code>element</code>.
     * 
     * @param element
     *            an XML <code>Element</code>
     * @param childName
     *            name of the child of <code>element</code> containing the
     *            attribute
     * @param attrName
     *            name of the attribute
     * @return integer value of the attribute
     * @throws IllegalArgumentException
     *             if the child or the attribute don't exist or the attribute
     *             value cannot be parsed as an integer
     */
    public static int getIntegerFromAttribute(Element element,
            String childName, String attrName) {
        Element child = getChild(element, childName);

        return getIntegerFromAttribute(child, attrName);
    }



    /**
     * Returns an integer from an attribute of a child XML <code>Element</code>
     * of <code>element</code>.
     * 
     * @param element
     *            an XML <code>Element</code>
     * @param childName
     *            name of the child of <code>element</code> containing the
     *            attribute
     * @param attrName
     *            name of the attribute
     * @param defaultValue
     *            integer value in case of an exception
     * @return double value of the attribute. If the attribute or child don't
     *         exist or the attribute value cannot be parsed as an integer, the
     *         <code>defaultValue</code> is returned.
     */
    public static int getIntegerFromAttribute(Element element,
            String childName, String attrName, int defaultValue) {
        try {
            return getIntegerFromAttribute(element, childName, attrName);
        } catch (IllegalArgumentException e) {
            Logger logger = Logger.getLogger("ptpshared.utility");
            logger.warning("Attribute " + attrName + " of element "
                    + element.getName()
                    + " could not be converted to an integer. "
                    + "Using default value (" + defaultValue + ")");
            return defaultValue;
        }
    }



    /**
     * Returns an integer from the XML <code>Element</code>'s text.
     * 
     * @param element
     *            an XML <code>Element</code>
     * @return integer value of the text
     * @throws IllegalArgumentException
     *             if the text cannot be parsed as a integer
     */
    public static int getIntegerFromText(Element element) {
        String text = element.getText();

        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Value (" + text + ") of element ("
                    + element.getName()
                    + ") could not be converted to integer.");
        }
    }



    /**
     * Returns an integer from the XML <code>Element</code>'s text.
     * 
     * @param element
     *            an XML <code>Element</code>
     * @param defaultValue
     *            default value
     * @return integer value of the text. If the text value cannot be parsed as
     *         an integer, the <code>defaultValue</code> is returned.
     */
    public static int getIntegerFromText(Element element, int defaultValue) {
        try {
            return getIntegerFromText(element);
        } catch (NumberFormatException e) {
            Logger logger = Logger.getLogger("ptpshared.utility");
            logger.warning("Text of element " + element.getName()
                    + " could not be converted to a integer. "
                    + "Using default value (" + defaultValue + ")");
            return defaultValue;
        }

    }



    /**
     * Returns a string from an attribute.
     * 
     * @param element
     *            an XML <code>Element</code> containing the attribute
     * @param attrName
     *            name of the attribute
     * @return string value of the attribute
     * @throws IllegalArgumentException
     *             if the attribute doesn't exist
     */
    public static String getStringFromAttribute(Element element, String attrName) {
        Attribute attr = getAttribute(element, attrName);

        return attr.getValue();
    }



    /**
     * Returns a string from an attribute of a child XML <code>Element</code> of
     * <code>element</code>.
     * 
     * @param element
     *            an XML <code>Element</code>
     * @param childName
     *            name of the child of <code>element</code> containing the
     *            attribute
     * @param attrName
     *            name of the attribute
     * @return string value of the attribute
     * @throws IllegalArgumentException
     *             if the child or the attribute don't exist
     */
    public static String getStringFromAttribute(Element element,
            String childName, String attrName) {
        Element child = getChild(element, childName);

        return getStringFromAttribute(child, attrName);
    }



    /**
     * Returns a string from an attribute.
     * 
     * @param element
     *            an XML <code>Element</code> containing the attribute
     * @param attrName
     *            name of the attribute
     * @param defaultValue
     *            default value in case of an exception
     * @return string value of the attribute. If the attribute doesn't exist,
     *         the <code>defaultValue</code> is returned.
     */
    public static String getStringFromAttributeDefault(Element element,
            String attrName, String defaultValue) {
        try {
            return getStringFromAttribute(element, attrName);
        } catch (IllegalArgumentException e) {
            Logger logger = Logger.getLogger("ptpshared.utility");
            logger.warning("Attribute " + attrName + " of element "
                    + element.getName()
                    + " could not be converted to a string. "
                    + "Using default value (" + defaultValue + ")");
            return defaultValue;
        }
    }



    /**
     * Returns a string from an attribute of a child XML <code>Element</code> of
     * <code>element</code>.
     * 
     * @param element
     *            an XML <code>Element</code>
     * @param childName
     *            name of the child of <code>element</code> containing the
     *            attribute
     * @param attrName
     *            name of the attribute
     * @param defaultValue
     *            integer value in case of an exception
     * @return string value of the attribute. If the attribute or child don't
     *         exist, the <code>defaultValue</code> is returned.
     */
    public static String getStringFromAttributeDefault(Element element,
            String childName, String attrName, String defaultValue) {
        try {
            return getStringFromAttribute(element, childName, attrName);
        } catch (IllegalArgumentException e) {
            Logger logger = Logger.getLogger("ptpshared.utility");
            logger.warning("Attribute " + attrName + " of element "
                    + element.getName()
                    + " could not be converted to a string. "
                    + "Using default value (" + defaultValue + ")");
            return defaultValue;
        }
    }



    /**
     * Check if the <code>element</code> has a attribute named
     * <code>attrName</code>.
     * 
     * @param element
     *            <code>Element</code>
     * @param attrName
     *            name of the attribute
     * @return <code>true</code> if an attribute with the specified name exists
     */
    public static boolean hasAttribute(Element element, String attrName) {
        Attribute attr = element.getAttribute(attrName);

        return (attr != null);
    }



    /**
     * Check if the <code>element</code> has a child named
     * <code>childName</code>.
     * 
     * @param element
     *            XML <code>Element</code>
     * @param childName
     *            name of the child <code>Element</code>
     * @return <code>true</code> if a child with the specified name exists
     */
    public static boolean hasChild(Element element, String childName) {
        Element child = element.getChild(childName);

        return (child != null);
    }



    /**
     * Loads an XML file into an XML <code>Document</code>. The
     * <code>SAXBuilder</code> is used to parse the XML file.
     * 
     * @param file
     *            an XML file
     * @return an XML <code>Document</code>
     * @throws IOException
     *             if an error occurs during the parsing of the XML file
     */
    public static Document loadXML(File file) throws IOException {
        SAXBuilder builder = new SAXBuilder();
        Document doc;
        try {
            doc = builder.build(file);
        } catch (JDOMException e) {
            throw new IOException(e);
        }
        return doc;
    }



    /**
     * Saves an XML <code>Document</code> to an XML file. The XML is formatted
     * to be easily readable in the file. The file is forced to have the xml
     * extension.
     * 
     * @param doc
     *            an XML <code>Document</code>
     * @param file
     *            an XML file
     * @throws IOException
     *             if an error occurs during the saving process
     */
    public static void saveXML(Document doc, File file) throws IOException {
        file = FileUtil.setExtension(file, "xml");

        FileWriter writer = new FileWriter(file);
        XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
        outputter.output(doc, writer);
        writer.close();
    }



    /**
     * Saves an XML <code>Element</code> to an XML file. The
     * <code>element</code> is taken as the root of the XML file. The XML is
     * formatted to be easily readable in the file. The file is forced to have
     * the xml extension.
     * 
     * @param element
     *            an XML <code>Element</code>
     * @param file
     *            an XML file
     * @throws IOException
     *             if an error occurs during the saving process
     */
    public static void saveXML(Element element, File file) throws IOException {
        Document doc = new Document();
        doc.addContent(element);
        saveXML(doc, file);
    }



    /**
     * Returns a <code>Properties</code> from an XML <code>Element</code>. The
     * hierarchy of the <code>Element</code> is converted to a property key
     * using the separator '.'
     * 
     * @param element
     *            XML <code>Element</code>
     * @return a <code>Properties</code>
     */
    public static Properties toProperties(Element element) {
        return toProperties(element, '.');
    }



    /**
     * Returns a <code>Properties</code> from a XML <code>Element</code>. The
     * hierarchy of the <code>Element</code> is converted to a property key
     * using the <code>separator</code>.
     * 
     * @param element
     *            XML <code>Element</code>
     * @param separator
     *            character separating the XML elements
     * @return a <code>Properties</code>
     */
    public static Properties toProperties(Element element, char separator) {
        Properties props = new Properties();

        Iterator<?> itr = element.getDescendants();
        while (itr.hasNext()) {
            Object itrNext = itr.next();

            if (itrNext instanceof Element) { // Only look at Element, not Text
                Element subLevel = (Element) itrNext;

                // Find prefix from the parent nodes
                String prefix =
                        findPrefixFromParents(subLevel, element, separator);

                // Cycle through all attributes
                props =
                        addAttributesToProperties(props, subLevel, prefix,
                                separator);

                // Add property for the node's text value
                if (subLevel.getText().length() > 0) {
                    String value = subLevel.getText();
                    props.setProperty(prefix, value);
                }
            }
        }

        // Add attributes from the root element
        props =
                addAttributesToProperties(props, element, element.getName(),
                        separator);

        return props;
    }
}
