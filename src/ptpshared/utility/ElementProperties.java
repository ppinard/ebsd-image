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

/**
 * Periodic table's elements information.
 * 
 * @author Philippe T. Pinard
 * 
 */
public class ElementProperties {

    /** List of elements' symbol. */
    private static final String[] ELEMENT_SYMBOLS = { "H", "He", "Li", "Be",
            "B", "C", "N", "O", "F", "Ne", "Na", "Mg", "Al", "Si", "P", "S",
            "Cl", "Ar", "K", "Ca", "Sc", "Ti", "V", "Cr", "Mn", "Fe", "Co",
            "Ni", "Cu", "Zn", "Ga", "Ge", "As", "Se", "Br", "Kr", "Rb", "Sr",
            "Y", "Zr", "Nb", "Mo", "Tc", "Ru", "Rh", "Pd", "Ag", "Cd", "In",
            "Sn", "Sb", "Te", "I", "Xe", "Cs", "Ba", "La", "Ce", "Pr", "Nd",
            "Pm", "Sm", "Eu", "Gd", "Tb", "Dy", "Ho", "Er", "Tm", "Yb", "Lu",
            "Hf", "Ta", "W", "Re", "Os", "Ir", "Pt", "Au", "Hg", "Tl", "Pb",
            "Bi", "Po", "At", "Rn", "Fr", "Ra", "Ac", "Th", "Pa", "U", "Np",
            "Pu", "Am", "Cm", "Bk", "Cf", "Es", "Fm", "Md", "No", "Lr", "Unq",
            "Unp", "Unh" };



    /**
     * Returns the atomic number of the specified symbol.
     * 
     * @param symbol
     *            element's symbol (two letters, e.g. Al)
     * @return atomic number
     * 
     * @throws IllegalArgumentException
     *             if the atomic number for the specified symbol cannot be found
     */
    public static int getAtomicNumber(String symbol) {
        for (int i = 0; i < ELEMENT_SYMBOLS.length; i++)
            if (ELEMENT_SYMBOLS[i].equalsIgnoreCase(symbol))
                return i + 1;

        throw new IllegalArgumentException(symbol + " is not a valid symbol.");
    }



    /**
     * Returns the element's symbol from the specified atomic number.
     * 
     * @param atomicNumber
     *            atomic number of the element
     * @return element's symbol
     * 
     * @throws IllegalArgumentException
     *             if the atomic number is invalid
     */
    public static String getSymbol(int atomicNumber) {
        int index = atomicNumber - 1;

        if (index > ELEMENT_SYMBOLS.length)
            throw new IllegalArgumentException("The maximum atomic number is "
                    + ELEMENT_SYMBOLS.length);
        if (index < 0)
            throw new IllegalArgumentException(
                    "The atomic number must be greater or equal to 1.");

        return ELEMENT_SYMBOLS[index];
    }

    
    /**
     * Checks if the specified symbol is a valid atomic symbol.
     * Leading and trailing whitespaces will be removed before comparison.
     * The comparison is case insensitive
     *
     * @param   symbol  atomic symbol to check
     *
     * @return  <code>true</code> if the symbol is valid
     *          or <code>false</code> if it is not
     */
    public static boolean isCorrect(String symbol) {
        for (String s : ELEMENT_SYMBOLS) {
            if (symbol.equalsIgnoreCase(s))  return true;
        }
        
        return false;
    }

}
