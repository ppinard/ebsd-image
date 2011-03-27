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
package crystallography.core;

import java.io.File;

import org.ebsdimage.TestCase;
import org.junit.Before;
import org.junit.Test;

import ptpshared.util.simplexml.XmlLoader;
import ptpshared.util.simplexml.XmlSaver;

import static org.junit.Assert.assertEquals;

public class ScatteringFactorsEnumTest extends TestCase {

    ScatteringFactorsEnum scatterType;



    @Before
    public void setUp() throws Exception {
        scatterType = ScatteringFactorsEnum.XRAY;
    }



    @Test
    public void testGetScatteringFactors() {
        assertEquals(XrayScatteringFactors.class,
                scatterType.getScatteringFactors());
    }



    @Test
    public void testXML() throws Exception {
        File tmpFile = createTempFile();

        // Write
        new XmlSaver().save(scatterType, tmpFile);

        // Read
        ScatteringFactorsEnum other =
                new XmlLoader().load(ScatteringFactorsEnum.class, tmpFile);
        assertEquals(scatterType, other);
    }

}
