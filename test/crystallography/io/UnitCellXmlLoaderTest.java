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
package crystallography.io;

import static crystallography.io.UnitCellXmlTags.*;
import static org.junit.Assert.assertEquals;

import org.jdom.Element;
import org.jdom.IllegalNameException;
import org.junit.Before;
import org.junit.Test;

import ptpshared.utility.BadUnitException;
import crystallography.core.UnitCell;

public class UnitCellXmlLoaderTest {

    private Element element;



    @Before
    public void setUp() throws Exception {
        element = new Element(TAG_NAME);

        element.setAttribute(ATTR_A, Double.toString(1.0));
        element.setAttribute(ATTR_B, Double.toString(2.0));
        element.setAttribute(ATTR_C, Double.toString(3.0));
        element.setAttribute(ATTR_ALPHA, Double.toString(1.1));
        element.setAttribute(ATTR_BETA, Double.toString(2.1));
        element.setAttribute(ATTR_GAMMA, Double.toString(3.1));
    }



    @Test
    public void testLoad() {
        UnitCell unitCell = new UnitCellXmlLoader().load(element);

        assertEquals(1.0, unitCell.a, 1e-7);
        assertEquals(2.0, unitCell.b, 1e-7);
        assertEquals(3.0, unitCell.c, 1e-7);
        assertEquals(1.1, unitCell.alpha, 1e-7);
        assertEquals(2.1, unitCell.beta, 1e-7);
        assertEquals(3.1, unitCell.gamma, 1e-7);
    }



    @Test(expected = IllegalNameException.class)
    public void testLoadExceptionElementName() {
        Element element = new Element("Incorrect");
        new UnitCellXmlLoader().load(element);
    }



    @Test(expected = BadUnitException.class)
    public void testLoadExceptionUnitDimension() {
        element.setAttribute(ATTR_DIM_UNITS, "incorrect");
        new UnitCellXmlLoader().load(element);
    }



    @Test(expected = BadUnitException.class)
    public void testLoadExceptionUnitAngle() {
        element.setAttribute(ATTR_ANGLE_UNITS, "incorrect");
        new UnitCellXmlLoader().load(element);
    }

}
