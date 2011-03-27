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
package ptpshared.util.simplexml;

import java.io.File;
import java.io.IOException;

import org.ebsdimage.TestCase;
import org.junit.Before;
import org.junit.Test;

public class XmlSaverTest extends TestCase {

    private Item[] items;



    @Before
    public void setUp() throws Exception {
        items = new Item[] { new Item(2) };
    }



    @Test
    public void testSave() throws IOException {
        File file = createTempFile();

        Class<?> E = items.getClass().getComponentType();
        System.out.println(E);

        new XmlSaver().saveArray(items, file);

        Item[] b = new XmlLoader().loadArray(Item.class, file);

        System.out.println(b.length);
        System.out.println(b[0].index);
    }

}
