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

/**
 * Utilities to deal with primitive data types.
 * 
 * @author Philippe T. Pinard
 */
public class DataTypeUtil {

    /**
     * Checks if a string can be parsed as a boolean. A string can be a boolean
     * if it is equal to
     * <ul>
     * <li>yes</li>
     * <li>no</li>
     * <li>true</li>
     * <li>false</li>
     * </ul>
     * 
     * @param str
     *            string
     * @return <code>true</code> if it can be parsed
     */
    public static boolean isBoolean(String str) {
        str = str.toLowerCase();

        return ("true".equals(str) || "yes".equals(str) || "false".equals(str) || "no".equals(str));
    }



    /**
     * Checks if a string can be parsed as a double.
     * 
     * @param str
     *            string
     * @return <code>true</code> if it can be parsed
     */
    public static boolean isDouble(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }



    /**
     * Check if a string can be parsed as an integer.
     * 
     * @param str
     *            string
     * @return <code>true</code> if it can be parsed
     */
    public static boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }
}
