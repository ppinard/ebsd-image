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
package crystallography.core;

import java.util.ArrayList;
import java.util.Collection;

import ptpshared.util.AlmostEquable;

/**
 * List of all the atom sites of an unit cell. The object is an
 * <code>ArrayList</code> containing a list of unique <code>AtomSite</code>s.
 * Equivalent atom sites are automatically removed and an unchecked
 * <code>AtomSitePositionException</code> is thrown.
 * 
 * @author Philippe T. Pinard
 */
public class AtomSites extends ArrayList<AtomSite> implements AlmostEquable {

    /**
     * Creates a new <code>AtomSites</code> to store the position of all atoms
     * of a unit cell. Use {@link #add(AtomSite)}, {@link #add(int, AtomSite)},
     * {@link #addAll(Collection)} or {@link #addAll(int, Collection)} to add
     * {@link AtomSite} to the <code>AtomSites</code>.
     */
    public AtomSites() {
    }



    /**
     * Adds an atom site.
     * 
     * @param atom
     *            an <code>AtomSite</code>
     * @throws AtomSitePositionException
     *             if the position is already occupied by another
     *             <code>AtomSite</code>
     * @return <code>true</code> if the atom was successfully added;
     *         <code>false</code> otherwise
     */
    @Override
    public boolean add(AtomSite atom) {
        assertNewItem(atom);
        return super.add(atom);
    }



    /**
     * Inserts an atom site at the specified <code>index</code>.
     * 
     * @param index
     *            position in the {@link AtomSites} list
     * @param atom
     *            an <code>AtomSite</code>
     * @throws AtomSitePositionException
     *             if the position is already occupied by another
     *             <code>AtomSite</code>
     */
    @Override
    public void add(int index, AtomSite atom) {
        assertNewItem(atom);
        super.add(index, atom);
    }



    /**
     * Adds all <code>AtomSite</code> to the list.
     * 
     * @param items
     *            list of <code>AtomSite</code>
     * @throws AtomSitePositionException
     *             if one position is already occupied by another
     *             <code>AtomSite</code>
     * @return <code>true</code> if all atoms were successfully added;
     *         <code>false</code> otherwise
     */
    @Override
    public boolean addAll(Collection<? extends AtomSite> items) {
        ArrayList<AtomSite> atoms = new ArrayList<AtomSite>(items);
        assertNewItems(atoms);
        return super.addAll(atoms);
    }



    /**
     * Inserts all <code>AtomSite</code> to list after the specified
     * <code>index</code>.
     * 
     * @param index
     *            position in the <code>AtomSite</code> list
     * @param items
     *            list of <code>AtomSite</code>
     * @throws AtomSitePositionException
     *             if one position is already occupied by another
     *             <code>AtomSite</code>
     * @return <code>true</code> if all atoms were successfully added;
     *         <code>false</code> otherwise
     */
    @Override
    public boolean addAll(int index, Collection<? extends AtomSite> items) {
        ArrayList<AtomSite> atoms = new ArrayList<AtomSite>(items);
        assertNewItems(atoms);
        return super.addAll(index, atoms);
    }



    /**
     * Assert that two atom sites don't have the same position.
     * 
     * @param item
     *            item to check
     * @throws AtomSitePositionException
     *             if two atom sites have the same position
     */
    private void assertNewItem(AtomSite item) {
        for (AtomSite atom : this)
            if (atom.position.equals(item.position, 1e-5))
                throw new AtomSitePositionException("Position already exists");
    }



    /**
     * Assert that two atom sites don't have the same position.
     * 
     * @param items
     *            items to check
     * @throws AtomSitePositionException
     *             if two atom sites have the same position
     */
    private void assertNewItems(ArrayList<AtomSite> items) {
        for (AtomSite item : items)
            assertNewItem(item);
    }



    /**
     * Returns <code>true</code> if this list contains the specified atom site.
     * More formally, returns <code>true</code> if and only if this list
     * contains at least one atom site such that
     * <code>(o==null&nbsp;?&nbsp;atom==null&nbsp;:&nbsp;
     * o.equals(atom, precision))</code>.
     * 
     * @param atom
     *            atom site whose presence in this list is to be tested
     * @param precision
     *            level of precision
     * @return <code>true</code> if this list contains the specified atom site,
     *         <code>false</code> otherwise
     * @throws IllegalArgumentException
     *             if the precision is less than 0.0
     * @throws IllegalArgumentException
     *             if the precision is not a number (NaN)
     */
    public boolean contains(AtomSite atom, double precision) {
        return indexOf(atom, precision) >= 0;
    }



    /**
     * Checks if this <code>AtomSites</code> is almost equal to the specified
     * one with the given precision. The method uses {@link AtomSite#equals} to
     * check the <code>AtomSite</code> inside the array list;
     * 
     * @param obj
     *            other <code>AtomSite</code> to check equality
     * @param precision
     *            level of precision
     * @return whether the two <code>AtomSite</code> are almost equal
     * @throws IllegalArgumentException
     *             if the precision is less than 0.0
     * @throws IllegalArgumentException
     *             if the precision is not a number (NaN)
     */
    @Override
    public boolean equals(Object obj, double precision) {
        if (precision < 0)
            throw new IllegalArgumentException(
                    "The precision has to be greater or equal to 0.0.");
        if (Double.isNaN(precision))
            throw new IllegalArgumentException(
                    "The precision must be a number.");

        if (obj == this)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;

        AtomSites other = (AtomSites) obj;

        if (size() != other.size())
            return false;

        for (AtomSite o : other)
            if (!contains(o, precision))
                return false;

        return true;
    }



    /**
     * Returns the index of the first occurrence of the specified atom site in
     * this list, or -1 if this list does not contain the atom site.
     * <p/>
     * More formally, returns the lowest index <code>i</code> such that
     * <code>(o==null&nbsp;?&nbsp;get(i)==null&nbsp;:&nbsp;
     * o.equals(get(i, precision)))</code> , or -1 if there is no such index.
     * 
     * @param atom
     *            specified element
     * @param precision
     *            level of precision
     * @return the index of the specified atom site, or -1 if no atom site is
     *         found
     * @throws IllegalArgumentException
     *             if the precision is less than 0.0
     * @throws IllegalArgumentException
     *             if the precision is not a number (NaN)
     */
    public int indexOf(AtomSite atom, double precision) {
        if (precision < 0)
            throw new IllegalArgumentException(
                    "The precision has to be greater or equal to 0.0.");
        if (Double.isNaN(precision))
            throw new IllegalArgumentException(
                    "The precision must be a number.");

        if (atom == null) {
            for (int i = 0; i < size(); i++)
                if (get(i) == null)
                    return i;
        } else {
            for (int i = 0; i < size(); i++)
                if (atom.equals(get(i), precision))
                    return i;
        }
        return -1;
    }



    /**
     * Replaces an <code>AtomSite</code> at the specified <code>index</code>
     * with the specified <code>AtomSite</code>.
     * 
     * @param index
     *            position in the list
     * @param atom
     *            the new atom site <code>AtomSite</code>
     * @return the <code>AtomSite</code> previously at the specified position.
     */
    @Override
    public AtomSite set(int index, AtomSite atom) {
        assertNewItem(atom);
        return super.set(index, atom);
    }

}
