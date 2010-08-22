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
import org.jdom.Element;
import org.junit.Before;
import org.junit.Test;

public class HoughPeakXmlLoaderTest {

    private Element element;



    @Before
    public void setUp() throws Exception {
        element = new Element(TAG_NAME);
        element.setAttribute(ATTR_RHO, Double.toString(5));
        element.setAttribute(ATTR_THETA, Double.toString(0.1));
        element.setAttribute(ATTR_MAXINTENSITY, Integer.toString(4));
    }



    @Test
    public void testLoad() {
        HoughPeak peak = new HoughPeakXmlLoader().load(element);

        assertEquals(5.0, peak.rho, 1e-7);
        assertEquals(0.1, peak.theta, 1e-7);
        assertEquals(4.0, peak.intensity, 1e-7);
    }
}
