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

import static org.ebsdimage.io.exp.CurrentMapsFileSaverXmlTags.ATTR_SAVEALLMAPS;
import static org.ebsdimage.io.exp.CurrentMapsFileSaverXmlTags.ATTR_SAVEHOUGHMAP;
import static org.ebsdimage.io.exp.CurrentMapsFileSaverXmlTags.ATTR_SAVEPATTERNMAP;
import static org.ebsdimage.io.exp.CurrentMapsFileSaverXmlTags.TAG_NAME;
import static org.junit.Assert.assertEquals;

import org.ebsdimage.core.exp.CurrentMapsFileSaver;
import org.ebsdimage.io.exp.CurrentMapsFileSaverXmlSaver;
import org.jdom.Element;
import org.junit.Before;
import org.junit.Test;

import ptpshared.utility.xml.JDomUtil;

public class CurrentMapsSaverXmlSaverTest {

    private CurrentMapsFileSaver saveMaps;



    @Before
    public void setUp() throws Exception {
        saveMaps = new CurrentMapsFileSaver(false, false, true, false);
    }



    @Test
    public void testSaveSaveMaps() {
        Element element = new CurrentMapsFileSaverXmlSaver().save(saveMaps);

        assertEquals(TAG_NAME, element.getName());
        assertEquals(false, JDomUtil.getBooleanFromAttribute(element,
                ATTR_SAVEALLMAPS));
        assertEquals(false, JDomUtil.getBooleanFromAttribute(element,
                ATTR_SAVEPATTERNMAP));
        assertEquals(true, JDomUtil.getBooleanFromAttribute(element,
                ATTR_SAVEHOUGHMAP));
    }

}
