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
import org.ebsdimage.io.CameraXmlSaver;
import org.jdom.Element;
import org.junit.Before;
import org.junit.Test;


import ptpshared.utility.xml.JDomUtil;

public class CameraXmlSaverTest {

    private Camera camera;



    @Before
    public void setUp() throws Exception {
        camera = new Camera(0.3, 0.4, 0.5);
    }



    @Test
    public void testSaveCamera() {
        Element element = new CameraXmlSaver().save(camera);

        assertEquals(TAG_NAME, element.getName());
        assertEquals(0.3, JDomUtil.getDoubleFromAttribute(element, ATTR_PCH),
                1e-7);
        assertEquals(0.4, JDomUtil.getDoubleFromAttribute(element, ATTR_PCV),
                1e-7);
        assertEquals(0.5, JDomUtil.getDoubleFromAttribute(element, ATTR_DD),
                1e-7);
    }

}
