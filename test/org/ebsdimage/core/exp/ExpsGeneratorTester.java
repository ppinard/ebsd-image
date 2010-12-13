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
package org.ebsdimage.core.exp;

import java.io.File;

import org.ebsdimage.TestCase;
import org.ebsdimage.core.exp.ops.pattern.post.PatternPostOps2Mock;
import org.ebsdimage.core.exp.ops.pattern.post.PatternPostOpsMock;
import org.ebsdimage.core.exp.ops.pattern.results.PatternResultsOpsMock;
import org.junit.Test;

import ptpshared.util.xml.XmlSaver;

import static org.junit.Assert.assertEquals;

public abstract class ExpsGeneratorTester extends TestCase {

    protected ExpsGenerator generator;



    public static ExpsGenerator createExpsGenerator() {
        ExpsGenerator generator = new ExpsGenerator();

        // Add operations
        generator.addItem(1, new PatternPostOps2Mock());
        generator.addItem(2, new PatternPostOpsMock());
        generator.addItem(3, new PatternPostOps2Mock());
        generator.addItem(1, new PatternPostOps2Mock(4));
        generator.addItem(1, new PatternPostOps2Mock(8));
        generator.addItem(1, new PatternResultsOpsMock());

        return generator;
    }



    @Test
    public void testExpsGenerator() {
        assertEquals(3, generator.getCombinations().size());

        PatternPostOps2Mock op;
        op =
                (PatternPostOps2Mock) generator.getCombinations().get(0).get(
                        "0001:org.ebsdimage.core.exp.ops.pattern.post.PatternPostOps2Mock");
        assertEquals(1, op.var);
        op =
                (PatternPostOps2Mock) generator.getCombinations().get(1).get(
                        "0001:org.ebsdimage.core.exp.ops.pattern.post.PatternPostOps2Mock");
        assertEquals(4, op.var);
        op =
                (PatternPostOps2Mock) generator.getCombinations().get(2).get(
                        "0001:org.ebsdimage.core.exp.ops.pattern.post.PatternPostOps2Mock");
        assertEquals(8, op.var);
    }



    @Test
    public void testAddItem() {

    }



    @Test
    public void testClearItems() {

    }



    @Test
    public void testGetCombinations() {

    }



    @Test
    public void testGetKeys() {

    }



    @Test
    public void testXML() throws Exception {
        File tmpFile = new File("/tmp/generator.xml");

        // Write
        new XmlSaver().save(generator, tmpFile);
    }

}
