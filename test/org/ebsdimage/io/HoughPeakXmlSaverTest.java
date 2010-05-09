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
package org.ebsdimage.io;

import static org.ebsdimage.io.HoughPeakXmlTags.ATTR_MAXINTENSITY;
import static org.ebsdimage.io.HoughPeakXmlTags.ATTR_RHO;
import static org.ebsdimage.io.HoughPeakXmlTags.ATTR_THETA;
import static org.ebsdimage.io.HoughPeakXmlTags.TAG_NAME;
import static org.junit.Assert.assertEquals;

import org.ebsdimage.core.HoughPeak;
import org.ebsdimage.io.HoughPeakXmlSaver;
import org.jdom.Element;
import org.junit.Before;
import org.junit.Test;

import ptpshared.utility.xml.JDomUtil;

public class HoughPeakXmlSaverTest {

    private HoughPeak peak;



    @Before
    public void setUp() throws Exception {
        peak = new HoughPeak(5.0, 0.1, 4);
    }



    @Test
    public void testSaveHoughPeak() {
        Element element = new HoughPeakXmlSaver().save(peak);

        assertEquals(TAG_NAME, element.getName());
        assertEquals(5.0, JDomUtil.getDoubleFromAttribute(element, ATTR_RHO),
                1e-7);
        assertEquals(0.1, JDomUtil.getDoubleFromAttribute(element, ATTR_THETA),
                1e-7);
        assertEquals(4.0, JDomUtil.getDoubleFromAttribute(element,
                ATTR_MAXINTENSITY), 1e-7);
    }

}
