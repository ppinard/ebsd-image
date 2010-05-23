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

import static org.ebsdimage.io.exp.CurrentMapsFileSaverXmlTags.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.ebsdimage.core.exp.CurrentMapsFileSaver;
import org.jdom.Element;
import org.junit.Before;
import org.junit.Test;

import ptpshared.utility.xml.JDomUtil;

public class CurrentMapsSaverXmlSaverTest {

    private CurrentMapsFileSaver saveMaps;



    @Before
    public void setUp() throws Exception {
        saveMaps = new CurrentMapsFileSaver(false, false, true, false, false);
    }



    @Test
    public void testSaveSaveMaps() {
        Element element = new CurrentMapsFileSaverXmlSaver().save(saveMaps);

        assertEquals(TAG_NAME, element.getName());
        assertFalse(JDomUtil.getBooleanFromAttribute(element, ATTR_SAVEALLMAPS));
        assertFalse(JDomUtil.getBooleanFromAttribute(element,
                ATTR_SAVEPATTERNMAP));
        assertTrue(JDomUtil.getBooleanFromAttribute(element, ATTR_SAVEHOUGHMAP));
        assertFalse(JDomUtil
                .getBooleanFromAttribute(element, ATTR_SAVEPEAKSMAP));
        assertFalse(JDomUtil.getBooleanFromAttribute(element,
                ATTR_SAVESOLUTIONOVERLAY));
    }

}
