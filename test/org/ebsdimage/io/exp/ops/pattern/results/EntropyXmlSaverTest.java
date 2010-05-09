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
package org.ebsdimage.io.exp.ops.pattern.results;

import static org.ebsdimage.io.exp.ops.pattern.results.EntropyXmlTags.TAG_NAME;
import static org.junit.Assert.assertEquals;

import org.ebsdimage.core.exp.ops.pattern.results.Entropy;
import org.ebsdimage.io.exp.ops.pattern.results.EntropyXmlSaver;
import org.jdom.Element;
import org.junit.Before;
import org.junit.Test;


public class EntropyXmlSaverTest {

    private Entropy op;



    @Before
    public void setUp() throws Exception {
        op = new Entropy();
    }



    @Test
    public void testSaveEntropy() {
        Element element = new EntropyXmlSaver().save(op);

        assertEquals(TAG_NAME, element.getName());
    }

}
