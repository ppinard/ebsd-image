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

import static org.ebsdimage.io.CameraXmlTags.ATTR_DD;
import static org.ebsdimage.io.CameraXmlTags.ATTR_PCH;
import static org.ebsdimage.io.CameraXmlTags.ATTR_PCV;
import static org.ebsdimage.io.CameraXmlTags.TAG_NAME;
import static org.junit.Assert.assertEquals;

import org.ebsdimage.core.Camera;
import org.jdom.Element;
import org.junit.Before;
import org.junit.Test;

public class CameraXmlLoaderTest {

    private Element element;



    @Before
    public void setUp() throws Exception {
        element = new Element(TAG_NAME);

        element.setAttribute(ATTR_PCH, Double.toString(0.3));
        element.setAttribute(ATTR_PCV, Double.toString(0.4));
        element.setAttribute(ATTR_DD, Double.toString(0.5));
    }



    @Test
    public void testLoad() {
        Camera camera = new CameraXmlLoader().load(element);

        assertEquals(0.3, camera.patternCenterH, 1e-7);
        assertEquals(0.4, camera.patternCenterV, 1e-7);
        assertEquals(0.5, camera.detectorDistance, 1e-7);
    }

}
