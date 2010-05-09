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
package org.ebsdimage.io.exp;

import static org.ebsdimage.io.exp.ExpsGeneratorXmlTags.TAG_NAME;

import org.ebsdimage.core.exp.ExpsGeneratorTester;
import org.ebsdimage.core.exp.ops.pattern.post.PatternPostOps2Mock;
import org.ebsdimage.core.exp.ops.pattern.post.PatternPostOpsMock;
import org.ebsdimage.io.exp.ops.pattern.post.PatternPostOps2MockXmlSaver;
import org.ebsdimage.io.exp.ops.pattern.post.PatternPostOpsMockXmlSaver;
import org.jdom.Element;


public abstract class ExpsGeneratorXmlTester extends ExpsGeneratorTester {

    public static Element createElement() {
        Element element = new Element(TAG_NAME);
        Element parent;
        Element child;

        parent = new Element("pattern.post");
        child = new PatternPostOps2MockXmlSaver()
                .save(new PatternPostOps2Mock());
        child.setText(Integer.toString(1));
        parent.addContent(child);

        child = new PatternPostOpsMockXmlSaver().save(new PatternPostOpsMock());
        child.setText(Integer.toString(2));
        parent.addContent(child);

        child = new PatternPostOps2MockXmlSaver()
                .save(new PatternPostOps2Mock());
        child.setText(Integer.toString(3));
        parent.addContent(child);

        child = new PatternPostOps2MockXmlSaver().save(new PatternPostOps2Mock(
                4));
        child.setText(Integer.toString(1));
        parent.addContent(child);

        child = new PatternPostOps2MockXmlSaver().save(new PatternPostOps2Mock(
                8));
        child.setText(Integer.toString(1));
        parent.addContent(child);

        child = new PatternPostOpsMockXmlSaver().save(new PatternPostOpsMock());
        child.setText(Integer.toString(1));
        parent.addContent(child);

        element.addContent(parent);

        return element;
    }

}
