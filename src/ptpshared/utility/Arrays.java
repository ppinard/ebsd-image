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
package ptpshared.utility;

import java.util.Comparator;

/**
 * Utilities for manipulating arrays.
 * 
 * @author Philippe T. Pinard
 */
public class Arrays {

    /**
     * Reverses the order of the array elements.
     * 
     * @param array
     *            an array
     */
    public static void reverse(Object[] array) {
        for (int left = 0, right = array.length - 1; left < right; left++, right--) {
            Object temp = array[left];
            array[left] = array[right];
            array[right] = temp;
        }
    }



    /**
     * Concatenates the values of a string array into a single string.
     * 
     * @param array
     *            array of string
     * @return a string
     */
    public static String concatenate(String[] array) {
        StringBuilder str = new StringBuilder();

        for (String item : array)
            str.append(item);

        return str.toString();
    }



    /**
     * Sorts an array of object with the specified comparator. The array is
     * reversed if the reverse argument is <code>true</code>.
     * 
     * @param <T>
     *            type of the object
     * @param array
     *            an array
     * @param comparator
     *            comparator to sort the array
     * @param reverse
     *            if <code>true</code> the array is reversed after being sorted
     */
    public static <T> void sort(T[] array, Comparator<? super T> comparator,
            boolean reverse) {
        java.util.Arrays.sort(array, comparator);
        if (reverse)
            reverse(array);
    }
}
