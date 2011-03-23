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
package org.ebsdimage.core.run;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import ptpshared.util.MultipleLoop;

/**
 * Object use to serialize and deserialize items in the
 * <code>OperationGenerator</code>. An item combines an operation and its order.
 * 
 * @author ppinard
 */
@Root
class Item {

    /** Order of the operation. */
    @Attribute(name = "order")
    public final int order;

    /** Operation. */
    @Element(name = "op")
    public final Operation op;



    /**
     * Creates a new <code>Item</code>.
     * 
     * @param order
     *            order of the operation
     * @param op
     *            operation
     */
    public Item(@Attribute(name = "order") int order,
            @Element(name = "op") Operation op) {
        this.order = order;
        this.op = op;
    }
}

/**
 * Abstract class to create a generator from a list of items. An item is defined
 * as an operation and the order where this operation should be performed. The
 * generator takes all the defined items and creates combinations of these items
 * using {@link MultipleLoop}. These combinations can then be used to create a
 * series of <code>Run</code>. For example, if two <code>Binning</code>
 * operations are given (with the same order), two <code>Run</code> object will
 * be created, each implementing one of the <code>Binning</code> operations.
 * 
 * @author Philippe T. Pinard
 */
@Root
public class OperationGenerator {

    /**
     * Return the order from a key.
     * 
     * @param key
     *            item key
     * @return order
     */
    public static int getOrderFromKey(String key) {
        String[] split = key.split(":");
        return Integer.parseInt(split[0]);
    }

    // /**
    // * Creates a new generator that stores a list of items. This allows to
    // * generate many <code>Run</code>s based on the given items. Use
    // * {@link OperationGenerator#addItem(int, Operation)} to add items.
    // *
    // * @see MultipleLoop#MultipleLoop(HashMap)
    // */
    // public OperationGenerator() {
    //
    // }

    /** HashMap of the items containing a key and an array of operations. */
    protected HashMap<String, Operation[]> items =
            new HashMap<String, Operation[]>();



    /**
     * Add an operation to the items.
     * 
     * @param order
     *            order of the operation with respect with the other operations
     * @param op
     *            operation to be added
     * @return the key of the newly added operation
     */
    public String addItem(int order, Operation op) {
        String key = createItemKey(order, op);

        // Increment or create Object array
        if (!(items.containsKey(key))) {
            Operation[] tmpArray = new Operation[] { op };
            items.put(key, tmpArray);
        } else {
            Operation[] oldArray = items.get(key);
            Operation[] newArray = new Operation[oldArray.length + 1];

            System.arraycopy(oldArray, 0, newArray, 0, oldArray.length);
            newArray[newArray.length - 1] = op;
            items.put(key, newArray);
        }

        return key;
    }



    /**
     * Clear an item.
     * 
     * @param order
     *            order of the operation with respect with the other operations
     * @param op
     *            operation to be added
     * @throws IllegalArgumentException
     *             if the key for the given order and operation doesn't exist
     */
    public void clearItem(int order, Operation op) {
        String key = createItemKey(order, op);
        if (!(items.containsKey(key)))
            throw new IllegalArgumentException("Key (" + key
                    + ") does not exist");
        items.remove(key);
    }



    /**
     * Create a key for the given operation. For example, the key for a
     * <code>Binning</code> operation with an order of 5 will be '0005:Binning'.
     * 
     * @param order
     *            order of the operation with respect with the other operations
     * @param op
     *            an Operation
     * @return key
     */
    protected String createItemKey(int order, Operation op) {
        return String.format("%04d", order) + ":" + op.getClass().getName();
    }



    /**
     * Generate a list of combinations from the defined items.
     * 
     * @see MultipleLoop#getCombinations()
     * @return combinations
     */
    public ArrayList<HashMap<String, Operation>> getCombinations() {
        // Convert items to HashMap<String, Object[]>
        HashMap<String, Object[]> tmpItems = new HashMap<String, Object[]>();
        for (String key : items.keySet())
            tmpItems.put(key, items.get(key));

        // Get combinations
        MultipleLoop loop = new MultipleLoop(tmpItems);
        ArrayList<HashMap<String, Object>> tmpCombs = loop.getCombinations();

        // Convert combinations to ArrayList<HashMap<String, Operation>>
        ArrayList<HashMap<String, Operation>> combs =
                new ArrayList<HashMap<String, Operation>>();

        for (HashMap<String, Object> tmpComb : tmpCombs) {
            HashMap<String, Operation> comb = new HashMap<String, Operation>();

            // Sort keys so that the order of the operation is respected
            String[] keys =
                    tmpComb.keySet().toArray(
                            new String[tmpComb.keySet().size()]);
            Arrays.sort(keys);

            // Change Object cast to Operation
            for (String key : keys)
                comb.put(key, (Operation) tmpComb.get(key));

            combs.add(comb);
        }

        return combs;
    }



    /**
     * Returns the operations for each combination.
     * 
     * @return an <code>ArrayList</code> of operations
     */
    public ArrayList<Operation[]> getCombinationsOperations() {
        ArrayList<Operation[]> combsOps = new ArrayList<Operation[]>();

        for (HashMap<String, Operation> comb : getCombinations()) {
            ArrayList<Operation> ops = new ArrayList<Operation>();

            // Sort keys
            String[] keys = comb.keySet().toArray(new String[0]);
            Arrays.sort(keys);

            // Add operations
            for (String key : keys)
                ops.add(comb.get(key));

            combsOps.add(ops.toArray(new Operation[0]));
        }

        return combsOps;
    }



    /**
     * Returns the items added to this generator.
     * 
     * @return items (key, array of operations)
     */
    public HashMap<String, Operation[]> getItems() {
        return new HashMap<String, Operation[]>(items);
    }



    /**
     * Returns the keys of the items defined.
     * 
     * @return array of keys
     */
    public String[] getKeys() {
        return items.keySet().toArray(new String[0]);
    }



    /**
     * Returns an array of <code>Item</code> to serialize the operations in the
     * generator.
     * 
     * @return array of <code>Item</code>
     */
    @SuppressWarnings("unused")
    @ElementList(name = "items")
    private ArrayList<Item> getOperations() {
        ArrayList<Item> items = new ArrayList<Item>();

        int order;
        for (Entry<String, Operation[]> item : this.items.entrySet()) {
            order = getOrderFromKey(item.getKey());

            for (Operation op : item.getValue()) {
                items.add(new Item(order, op));
            }
        }

        return items;
    }



    /**
     * Sets the operations inside the generator in the deserialization process.
     * The order is taken as the order saved inside each operation.
     * 
     * @param items
     *            array of <code>Item</code>
     */
    @SuppressWarnings("unused")
    @ElementList(name = "items")
    private void setOperations(ArrayList<Item> items) {
        for (Item item : items) {
            addItem(item.order, item.op);
        }
    }

}