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
package org.ebsdimage.core;

import static java.lang.Math.abs;
import static java.util.Arrays.sort;
import static ptpshared.utility.Arrays.reverse;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;

/**
 * Comparator to sort a list of pairs by their direction cosine.
 * 
 * @author Philippe T. Pinard
 * 
 */
class DirectionCosineComparator implements Comparator<Pair>, Serializable {

    @Override
    public int compare(Pair pair0, Pair pair1) {
        double costheta0 = pair0.directionCosine;
        double costheta1 = pair1.directionCosine;

        if (costheta0 < costheta1)
            return -1;
        else if (costheta0 > costheta1)
            return 1;
        else
            return 0;
    }
}

/**
 * Abstract class to store many <code>Pair</code>s. It implements sort and
 * search methods.
 * 
 * @author Philippe T. Pinard
 * 
 * @param <E>
 */
public abstract class Pairs<E extends Pair> implements Iterable<E> {

    /** Array of <code>Pair</code>. */
    protected E[] pairs;



    /**
     * Finds the closest pair(s) that matches the desired direction cosine with
     * the specified precision. If many pair are close to the specified
     * direction cosine, all of them are returned. If no pair is close, an empty
     * array is returned.
     * 
     * @param directionCosine
     *            desired direction cosine
     * @param precision
     *            how far the match(es) should be from the desired direction
     *            cosine
     * @return closest matches
     * 
     * @throws IllegalArgumentException
     *             if the precision is less than 0.0
     * @throws IllegalArgumentException
     *             if the precision is not a number (NaN)
     * @throws IllegalArgumentException
     *             if the direction cosine is not a number (NaN)
     * @throws IllegalArgumentException
     *             if the direction cosine is infinite
     */
    public abstract E[] findClosestMatches(double directionCosine,
            double precision);



    /**
     * Internal method to find the closes match(es). For more info, see
     * {@link #findClosestMatches(double, double)}.
     * 
     * @param directionCosine
     *            desired direction cosine
     * @param precision
     *            how far the match(es) should be from the desired direction
     *            cosine
     * @return closest matches
     * 
     * @throws IllegalArgumentException
     *             if the precision is less than 0.0
     * @throws IllegalArgumentException
     *             if the precision is not a number (NaN)
     * @throws IllegalArgumentException
     *             if the direction cosine is not a number (NaN)
     * @throws IllegalArgumentException
     *             if the direction cosine is infinite
     * 
     * @see {@link #findClosestMatches(double, double)}
     */
    protected ArrayList<E> findClosestMatchesArrayList(double directionCosine,
            double precision) {
        if (precision < 0)
            throw new IllegalArgumentException(
                    "The precision has to be greater or equal to 0.0.");
        if (Double.isNaN(precision))
            throw new IllegalArgumentException(
                    "The precision must be a number.");
        if (Double.isNaN(directionCosine))
            throw new IllegalArgumentException(
                    "The direction cosine cannot be not a number (NaN).");
        if (Double.isInfinite(directionCosine))
            throw new IllegalArgumentException(
                    "The direction cosine cannot be infinite.");

        ArrayList<E> matches = new ArrayList<E>();

        // Find closest value to the given direction cosine
        double closestValue = Double.POSITIVE_INFINITY;
        for (E pair : pairs)
            if (abs(pair.directionCosine - directionCosine) < abs(closestValue
                    - directionCosine)) {
                closestValue = pair.directionCosine;
            }

        // Find all matches around that closest value
        for (E pair : pairs)
            if (abs(pair.directionCosine - closestValue) < precision)
                matches.add(pair);

        return matches;
    }



    /**
     * Returns the pair at the specified index.
     * 
     * @param index
     *            index of the pair
     * @return pair
     * 
     * @throws IllegalArgumentException
     *             if the index is out of range
     */
    public E get(int index) {
        if (index < 0 || index >= pairs.length)
            throw new IllegalArgumentException("Index (" + index
                    + ") needs to be between [0," + pairs.length + "[.");

        return pairs[index];
    }



    /**
     * Returns an iterator over the defined pairs.
     * 
     * @return iterator
     */
    @Override
    public Iterator<E> iterator() {
        return Arrays.asList(pairs).iterator();
    }



    /**
     * Returns the number of defined pairs.
     * 
     * @return size
     */
    public int size() {
        return pairs.length;
    }



    /**
     * Sorts the pairs by their direction cosine.
     * 
     * @param reverse
     *            if <code>true</code> the order is reversed
     */
    public void sortByDirectionCosine(boolean reverse) {
        sort(pairs, new DirectionCosineComparator());

        if (reverse)
            reverse(pairs);
    }



    /**
     * Returns a representation of the defined pairs one after the other.
     * Suitable for debugging.
     * 
     * @return info of all the defined pairs
     */
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();

        for (E pair : pairs) {
            str.append(pair.toString() + "\n");
        }

        return str.toString();
    }
}
