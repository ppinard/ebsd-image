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
package org.ebsdimage.io.sim;

import static org.ebsdimage.io.sim.EnergyXmlTags.ATTR_VALUE;
import static org.ebsdimage.io.sim.EnergyXmlTags.TAG_NAME;
import static org.junit.Assert.assertEquals;

import org.ebsdimage.core.sim.Energy;
import org.ebsdimage.io.sim.EnergyXmlSaver;
import org.jdom.Element;
import org.junit.Before;
import org.junit.Test;


import ptpshared.utility.xml.JDomUtil;

public class EnergyXmlSaverTest {

    private Energy energy;



    @Before
    public void setUp() throws Exception {
        energy = new Energy(20e3);
    }



    @Test
    public void testSaveEnergy() {
        Element element = new EnergyXmlSaver().save(energy);

        assertEquals(TAG_NAME, element.getName());
        assertEquals(20e3,
                JDomUtil.getDoubleFromAttribute(element, ATTR_VALUE), 1e-7);
    }

}
