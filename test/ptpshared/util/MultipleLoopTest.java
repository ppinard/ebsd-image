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
package ptpshared.util;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class MultipleLoopTest {

    private MultipleLoop multiple;

    private HashMap<String, Object[]> prmValues;



    @Before
    public void setUp() throws Exception {
        prmValues = new HashMap<String, Object[]>();
        prmValues.put("dt",
                new Object[] { new Double(0.75), new Double(0.375) });
        prmValues.put("dx", new Object[] { new Double(0.25), new Double(0.125),
                new Double(0.0625) });

        multiple = new MultipleLoop(prmValues);
    }



    @Test
    public void testGetAll() {
        ArrayList<Object[]> all = multiple.getAll();

        assertEquals(6, all.size());

        for (int i = 0; i < all.size(); i++) {
            assertEquals(all.get(i).length, 2);

            if (!(all.get(i)[0].equals(new Double(0.75)))
                    && !(all.get(i)[0].equals(new Double(0.375))))
                fail();
            if (!(all.get(i)[1].equals(new Double(0.25)))
                    && !(all.get(i)[1].equals(new Double(0.125)))
                    && !(all.get(i)[1].equals(new Double(0.0625))))
                fail();
        }
    }



    @Test
    public void testGetCombinations() {
        ArrayList<HashMap<String, Object>> combinations =
                multiple.getCombinations();

        assertEquals(6, combinations.size());

        for (HashMap<String, Object> combination : combinations) {
            assertTrue(combination.keySet().contains("dt"));
            assertTrue(combination.keySet().contains("dx"));

            if (!(combination.get("dt").equals(new Double(0.75)))
                    && !(combination.get("dt").equals(new Double(0.375))))
                fail();
            if (!(combination.get("dx").equals(new Double(0.25)))
                    && !(combination.get("dx").equals(new Double(0.125)))
                    && !(combination.get("dx").equals(new Double(0.0625))))
                fail();
        }
    }



    @Test
    public void testGetNames() {
        ArrayList<String> names = multiple.getNames();

        ArrayList<String> expected = new ArrayList<String>();
        expected.add("dt");
        expected.add("dx");

        assertEquals(expected, names);
    }



    @Test
    public void testGetVaried() {
        ArrayList<String> varied = multiple.getVaried();

        ArrayList<String> expected = new ArrayList<String>();
        expected.add("dt");
        expected.add("dx");

        assertEquals(expected, varied);
    }



    @Test
    public void testMultipleLoop() {
        assertTrue(true);
    }
}
