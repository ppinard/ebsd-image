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
import org.ebsdimage.io.exp.CurrentMapsFileSaverXmlLoader;
import org.jdom.Element;
import org.junit.Before;
import org.junit.Test;


public class CurrentMapsSaverXmlLoaderTest {

    private Element element;



    @Before
    public void setUp() throws Exception {
        element = new Element(TAG_NAME);

        element.setAttribute(ATTR_SAVEALLMAPS, Boolean.toString(false));
        element.setAttribute(ATTR_SAVEPATTERNMAP, Boolean.toString(false));
        element.setAttribute(ATTR_SAVEHOUGHMAP, Boolean.toString(true));
    }



    @Test
    public void testLoad() {
        CurrentMapsFileSaver saveMaps = new CurrentMapsFileSaverXmlLoader().load(element);

        assertEquals(false, saveMaps.saveAllMaps);
        assertEquals(false, saveMaps.savePatternMap);
        assertEquals(true, saveMaps.saveHoughMap);
    }

}
