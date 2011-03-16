package org.ebsdimage.core;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Random;

import rmlimage.core.ByteMap;
import rmlimage.core.LUT;
import rmlimage.core.Map;
import rmlshared.util.Labeled;

/**
 * A special <code>ByteMap</code> where an item can be associated to the value
 * of a pixel.
 * 
 * @author ppinard
 * @param <Item>
 *            type of item
 */
public class IndexedByteMap<Item> extends ByteMap {

    /**
     * Returns a color look-up table for the different error codes. Each error
     * code is assigned a specific color. The colors are random so that two
     * consecutive error codes have different colors.
     * 
     * @param nbColors
     *            number of colors to generate
     * @return a look-up table
     */
    private static LUT randomColorLUT(int nbColors) {
        LUT lut = new LUT(nbColors);

        // Color 0 = black = background
        lut.red[0] = (byte) 0;
        lut.green[0] = (byte) 0;
        lut.blue[0] = (byte) 0;

        // Set the color components of the LUT
        Random rnd = new Random(1);
        int red;
        int green;
        int blue;
        for (int n = 1; n < nbColors; n++) {
            red = rnd.nextInt(256);
            green = rnd.nextInt(256);
            blue = rnd.nextInt(256);

            // If color too dark, redo
            if (red + green + blue < 128) {
                n--;
                continue;
            }

            lut.red[n] = (byte) red;
            lut.green[n] = (byte) green;
            lut.blue[n] = (byte) blue;
        }

        return lut;
    }

    /** Registered items. */
    private Object[] items = new Object[256];



    /**
     * Creates a new <code>ErrorMap</code>.
     * 
     * @param width
     *            width of the map
     * @param height
     *            height of the map
     * @param item0
     *            item for the pixels with a value of 0
     */
    public IndexedByteMap(int width, int height, Item item0) {
        this(width, height, item0, new HashMap<Integer, Item>());
    }



    /**
     * Creates a new <code>ErrorMap</code>.
     * 
     * @param width
     *            width of the map
     * @param height
     *            height of the map
     * @param pixArray
     *            pixels in the map
     * @param item0
     *            item for the pixels with a value of 0
     * @param items
     *            items indexing the pixels in the map
     * @throws NullPointerException
     *             if the error codes array is null
     * @throws IllegalArgumentException
     *             if a pixel refers to an undefined error code
     */
    public IndexedByteMap(int width, int height, byte[] pixArray, Item item0,
            java.util.Map<Integer, Item> items) {
        super(width, height, randomColorLUT(255), pixArray);

        if (item0 == null)
            throw new NullPointerException("Item 0 cannot be null.");
        this.items[0] = item0;

        items.remove(0); // In case an entry with the key 0 was set
        for (Entry<Integer, Item> entry : items.entrySet())
            register(entry.getKey(), entry.getValue());

        validate();
    }



    /**
     * Creates a new <code>ErrorMap</code>.
     * 
     * @param width
     *            width of the map
     * @param height
     *            height of the map
     * @param item0
     *            item for the pixels with a value of 0
     * @param items
     *            items indexing the pixels in the map
     * @throws NullPointerException
     *             if the error codes array is null
     * @throws IllegalArgumentException
     *             if a pixel refers to an undefined error code
     */
    public IndexedByteMap(int width, int height, Item item0,
            java.util.Map<Integer, Item> items) {
        this(width, height, new byte[width * height], item0, items);
    }



    @Override
    public void assertEquals(Map map) {
        @SuppressWarnings("unchecked")
        IndexedByteMap<Item> other = (IndexedByteMap<Item>) map;

        if (!Arrays.equals(items, other.items))
            throw new AssertionError(
                    "The indexes from the two maps are different.");

        super.assertEquals(map);
    }



    @Override
    public IndexedByteMap<Item> createMap(int width, int height) {
        return new IndexedByteMap<Item>(width, height, getItem(0));
    }



    @Override
    public IndexedByteMap<Item> duplicate() {
        IndexedByteMap<Item> dup = createMap(width, height);

        // Copy items
        System.arraycopy(items, 0, dup.items, 0, 256);

        // Copy pixArray
        System.arraycopy(pixArray, 0, dup.pixArray, 0, size);

        // Copy metadata
        dup.cloneMetadataFrom(this);

        return dup;
    }



    /**
     * Returns the registered item at the specified id. Returns
     * <code>null</code> if no item is registered.
     * 
     * @param id
     *            id of an item between 0 and 255
     * @return registered item at the specified id, or <code>null</code> if no
     *         item is associated with the specified id
     */
    @SuppressWarnings("unchecked")
    // Okay since item can only be added by register(int, Item) method
    public Item getItem(int id) {
        if (id < 0 || id > 255)
            throw new IllegalArgumentException(
                    "The ID must be between 0 and 255.");
        return (Item) items[id];
    }



    /**
     * Returns the id of the specified item. If the item is not registered,
     * <code>-1</code> is returned.
     * 
     * @param item
     *            an item
     * @return id of the specified item. -1 if the item is not registered
     */
    public int getItemId(Item item) {
        if (item == null)
            throw new NullPointerException("Item cannot be null.");

        for (int id = 0; id < 256; id++)
            if (item.equals(items[id]))
                return id;

        return -1;
    }



    /**
     * Returns a <code>HashMap</code> of the registered items and their ids. The
     * item for the pixel with a value of zero is part of the Map.
     * 
     * @return map of items and their ids
     */
    public java.util.Map<Integer, Item> getItems() {
        HashMap<Integer, Item> out = new HashMap<Integer, Item>();

        for (int id = 0; id < 256; id++)
            if (isRegistered(id))
                out.put(id, getItem(id));

        return out;
    }



    @Override
    public String getPixelInfoLabel(int index) {
        StringBuilder str = new StringBuilder();
        str.append(super.getPixelInfoLabel(index));

        int id = pixArray[index] & 0xff;
        Item item = getItem(id);
        if (item == null)
            return str.toString();

        str.append("  ");
        str.append(id);
        str.append(" - ");

        if (item instanceof Labeled)
            str.append(((Labeled) item).getLabel());
        else
            str.append(item.toString());

        return str.toString();
    }



    /**
     * Returns the validation message. An empty string is the file is valid; a
     * error message otherwise.
     * 
     * @return error message or empty string
     */
    private String getValidationMessage() {
        int id;

        for (int i = 0; i < size; i++) {
            id = pixArray[i] & 0xff;
            if (!isRegistered(id))
                return "Item (" + id + ") at pixel (index=" + i
                        + ") is undefined";
        }

        return "";
    }



    @Override
    public String[] getValidFileFormats() {
        return new String[] { "bmp" };
    }



    /**
     * Checks that all the values inside the <code>pixArray</code> are linked
     * with an error code.
     * 
     * @return <code>true</code> if all the values are linked with an error
     *         code, <code>false</code> otherwise
     */
    public boolean isCorrect() {
        return getValidationMessage().isEmpty();
    }



    /**
     * Checks whether the specified id corresponds to a registered item.
     * 
     * @param id
     *            id of an item
     * @return <code>true</code> if the specified id corresponds to a registered
     *         item, <code>false</code> otherwise
     */
    public boolean isRegistered(int id) {
        if (id < 0 || id > 255)
            return false;
        return items[id] != null;
    }



    /**
     * Checks whether the specified item is registered.
     * 
     * @param item
     *            an item
     * @return <code>true</code> if the specified item is registered,
     *         <code>false</code> otherwise
     */
    public boolean isRegistered(Item item) {
        if (item == null)
            return false;

        return getItemId(item) >= 0;
    }



    /**
     * Registers a new item.
     * 
     * @param id
     *            id to be associated with the new item (between 1 and 255)
     * @param item
     *            new item
     * @throws IllegalArgumentException
     *             if the id of the new item is already used by another item
     */
    public void register(int id, Item item) {
        if (id < 1 || id > 255)
            throw new IllegalArgumentException("Id (" + id
                    + ") must be between [1, 255].");
        if (isRegistered(id))
            throw new IllegalArgumentException("An item with id (" + id
                    + ") is already registered.");
        if (item == null)
            throw new NullPointerException("Item cannot be null.");

        items[id] = item;
    }



    /**
     * Registers a new item. The id is automatically selected as the first
     * available id.
     * 
     * @param item
     *            new item
     * @return id of the new item
     */
    public int register(Item item) {
        int id = findFirstNullItem();
        items[id] = item;
        return id;
    }



    /**
     * Returns the id of the first null item in the items array.
     * 
     * @return id of the first null item
     */
    protected int findFirstNullItem() {
        for (int id = 0; id < items.length; id++)
            if (items[id] == null)
                return id;

        throw new IllegalArgumentException("No more items can be registered.");
    }



    /**
     * Desactivate methods since the LUT is fixed. {@inheritDoc}
     */
    @Override
    public void setLUT(LUT lut) {
    }



    /**
     * Sets a new value to a pixel. The map is checked to make sure that the new
     * value is valid.
     * 
     * @param index
     *            index of the pixel
     * @param id
     *            id of an item already registered
     * @throws IllegalArgumentException
     *             if the index is out of range
     * @throws IllegalArgumentException
     *             if the value corresponds to a unregistered item
     */
    @Override
    public void setPixValue(int index, int id) {
        if (!isRegistered(id))
            throw new IllegalArgumentException("Value (" + id
                    + ") does not match a registered item.");
        super.setPixValue(index, id);
    }



    /**
     * Sets a new value to a pixel corresponding to the id of the specified
     * item. If the specified item is not registered, the method
     * {@link #register(Object)} is called and the item is registered.
     * 
     * @param index
     *            index of the pixel
     * @param item
     *            an item
     * @return the id of the item
     * @throws IllegalArgumentException
     *             if the index is out of range
     */
    public int setPixValue(int index, Item item) {
        int id = getItemId(item);
        if (id < 0)
            id = register(item);

        setPixValue(index, id);

        return id;
    }



    /**
     * Sets a new value to a pixel. The map is checked to make sure that the new
     * value is valid.
     * 
     * @param x
     *            position in x
     * @param y
     *            position in y
     * @param id
     *            id of an item already registered
     * @throws IllegalArgumentException
     *             if the index is out of range
     * @throws IllegalArgumentException
     *             if the value corresponds to a unregistered item
     * @see #validate()
     */
    @Override
    public void setPixValue(int x, int y, int id) {
        setPixValue(getPixArrayIndex(x, y), id);
    }



    /**
     * Sets a new value to a pixel corresponding to the id of the specified
     * item. If the specified item is not registered, the method
     * {@link #register(Object)} is called and the item is registered.
     * 
     * @param x
     *            position in x
     * @param y
     *            position in y
     * @param item
     *            an item
     * @return the id of the item
     * @throws IllegalArgumentException
     *             if the index is out of range
     */
    public int setPixValue(int x, int y, Item item) {
        return setPixValue(getPixArrayIndex(x, y), item);
    }



    /**
     * Unregisters an item from this map. To be unregistered, an item must not
     * be used in any pixel of the map.
     * 
     * @param id
     *            id of the item to unregister (between 1 and 255)
     * @throws IllegalArgumentException
     *             if the specified id does not correspond to any registered
     *             item
     */
    public void unregister(int id) {
        if (id == 0)
            throw new IllegalArgumentException(
                    "Cannot unregister item with id 0.");
        if (!isRegistered(id))
            throw new IllegalArgumentException("No item with id (" + id
                    + ") is registered.");

        // Store old value in case of an error
        Item oldItem = getItem(id);

        // Attempt to remove error code
        items[id] = null;

        String validationMessage = getValidationMessage();
        if (!validationMessage.isEmpty()) { // Revert
            items[id] = oldItem;
            throw new IllegalArgumentException(validationMessage);
        }
    }



    /**
     * Unregisters an item from this map. To be unregistered, an item must not
     * be used in any pixel of the map.
     * 
     * @param item
     *            item to unregister
     */
    public void unregister(Item item) {
        unregister(getItemId(item));
    }



    /**
     * Validates that all the values inside the <code>pixArray</code> are linked
     * with an error code.
     * 
     * @throws IllegalArgumentException
     *             if a pixel links to an undefined error code
     */
    public void validate() {
        String message = getValidationMessage();
        if (!message.isEmpty())
            throw new IllegalArgumentException(message);
    }
}
