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
package ptpshared.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * Generator of combinations from multiple-valued parameters.
 * <p/>
 * <b>References:</b>
 * <ul>
 * <li>Code from Python module <code>scitools.multipleloop</code></li>
 * </ul>
 * 
 * @author Philippe T. Pinard
 */
public class MultipleLoop {

    /** <code>HashMap</code> of parameters and values. */
    private final HashMap<String, Object[]> prmValues;

    /**
     * <code>ArrayList</code> containing all combinations of the parameters and
     * values.
     */
    private ArrayList<Object[]> all;

    /** <code>ArrayList</code> containing the names of the parameters. */
    private ArrayList<String> names;

    /** <code>ArrayList</code> of parameter names that were varied. */
    private ArrayList<String> varied;



    /**
     * Creates a new <code>MultipleLoop</code> to generate combination of
     * multiple-valued parameters. Create a list of cases from the given
     * parameters-values hash map.
     * 
     * @param prmValues
     *            <code>HashMap</code> containing parameters and their value
     */
    public MultipleLoop(HashMap<String, Object[]> prmValues) {
        this.prmValues = prmValues;
        combine();
    }



    /**
     * Finds all the combinations.
     */
    private void combine() {
        all = new ArrayList<Object[]>();
        varied = new ArrayList<String>();

        Set<String> keys = prmValues.keySet();
        names = new ArrayList<String>(keys);

        Iterator<String> it = keys.iterator();

        while (it.hasNext()) {
            String key = it.next();

            Object[] values = prmValues.get(key);

            all = outer(all, values);

            if (values.length > 1)
                varied.add(key);
        }
    }



    /**
     * Returns all combinations of the parameters and values.
     * 
     * @return <code>List</code> containing the combinations of parameters
     */
    public ArrayList<Object[]> getAll() {
        return all;
    }



    /**
     * Returns all the possible combinations as an <code>ArrayList</code> of
     * <code>HashMap</code>. Each <code>HashMap</code> specified one value
     * (object) for each parameter.
     * 
     * @return <code>List</code> of <code>HashMap</code> containing the
     *         combinations
     */
    public ArrayList<HashMap<String, Object>> getCombinations() {
        ArrayList<HashMap<String, Object>> combinations =
                new ArrayList<HashMap<String, Object>>();

        for (int i = 0; i < all.size(); i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            Object[] values = all.get(i);

            for (int j = 0; j < names.size(); j++) {
                String name = names.get(j);
                Object value = values[j];
                map.put(name, value);
            }

            combinations.add(map);
        }

        return combinations;
    }



    /**
     * Returns the names of the parameters. The order of this list matched the
     * order of the <code>ArrayList</code> all.
     * 
     * @return <code>List</code> containing the ordered name of the parameters
     */
    public ArrayList<String> getNames() {
        return names;
    }



    /**
     * Returns a list of parameter names that are varied, i.e. where there is
     * more than one value of the parameter
     * 
     * @return <code>List</code> containing parameters that were varied
     */
    public ArrayList<String> getVaried() {
        return varied;
    }



    /**
     * Builds the <code>ArrayList</code> of values (objects).
     * 
     * @param a
     *            old values
     * @param b
     *            new values
     * @return old values combined with new values
     */
    private ArrayList<Object[]> outer(ArrayList<Object[]> a, Object[] b) {
        ArrayList<Object[]> all = new ArrayList<Object[]>();

        if (a.size() == 0)
            // First call
            for (Object j : b)
                all.add(new Object[] { j });
        else
            for (Object j : b)
                for (Object[] i : a) {
                    int size = i.length + 1; // Size of i + new value j
                    Object[] k = new Object[size];

                    System.arraycopy(i, 0, k, 0, i.length);
                    k[k.length - 1] = j;

                    all.add(k);
                }

        return all;
    }
}
