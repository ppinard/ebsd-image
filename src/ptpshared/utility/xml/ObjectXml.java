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

/**
 * Soft definition. If a object implements this interface it can be assumed that
 * the following classes also exists:
 * <ul>
 * <li>a <code>Tags</code> code
 * <li>a <code>XmlLoader</code> class extending <code>ObjectXMLLoader</code></li>
 * <li>a <code>XmlSaver</code> class extending <code>ObjectXMLSaver</code></li>
 * </ul>
 * <p/>
 * For example, if the class <code>Foo</code> implements <code>ObjectXML</code>,
 * the classes <code>FooTags</code>, <code>FooXmlLoader</code> and
 * <code>FooXmlSaver</code> must also exist.
 * 
 * @author Philippe T. Pinard
 */
public interface ObjectXml {
}
