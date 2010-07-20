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
package org.ebsdimage.io.exp.ops.hough.results;

import static org.ebsdimage.io.exp.ops.hough.results.AverageXmlTags.TAG_NAME;
import static org.junit.Assert.assertEquals;

import org.ebsdimage.core.exp.ops.hough.results.Average;
import org.jdom.Element;
import org.junit.Before;
import org.junit.Test;

public class AverageXmlSaverTest {

    private Average op;



    @Before
    public void setUp() throws Exception {
        op = new Average();
    }



    @Test
    public void testSaveHoughAverage() {
        Element element = new AverageXmlSaver().save(op);

        assertEquals(TAG_NAME, element.getName());
    }

}
