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
package ptpshared.utility.sql;

/**
 * Factory to create SQL command of various types.
 * 
 * @author Philippe T. Pinard
 */
public class ColumnFactory {

    /**
     * Creates a new column to store boolean values. A value of zero is
     * considered false. Nonzero values are considered true.
     * 
     * @param name
     *            name of the column
     * @param defaultValue
     *            default value
     * @return BOOLEAN column
     */
    public static Column columnBoolean(String name, boolean defaultValue) {
        return new Column(ColumnType.BOOLEAN, name, false, false, true,
                Boolean.toString(defaultValue).toUpperCase(), false, false);
    }



    /**
     * Creates a new column to store date and time values.
     * <p/>
     * A date and time combination. The supported range is '1000-01-01 00:00:00'
     * to '9999-12-31 23:59:59'. MySQL displays DATETIME values in 'YYYY-MM-DD
     * HH:MM:SS' format, but allows assignment of values to DATETIME columns
     * using either strings or numbers.
     * 
     * @param name
     *            name of the column
     * @param notNull
     *            if <code>true</code>, force that the column must not be null
     * @return DATETIME column
     */
    public static Column columnDateTime(String name, boolean notNull) {
        return new Column(ColumnType.DATETIME, name, false, false, notNull,
                false, false);
    }



    /**
     * Creates a new column to store date and time values.
     * <p/>
     * A date and time combination. The supported range is '1000-01-01 00:00:00'
     * to '9999-12-31 23:59:59'. MySQL displays DATETIME values in 'YYYY-MM-DD
     * HH:MM:SS' format, but allows assignment of values to DATETIME columns
     * using either strings or numbers.
     * 
     * @param name
     *            name of the column
     * @param notNull
     *            if <code>true</code>, force that the column must not be null
     * @param defaultValue
     *            default value
     * @return DATETIME column
     */
    public static Column columnDateTime(String name, boolean notNull,
            String defaultValue) {
        return new Column(ColumnType.DATETIME, name, false, false, notNull,
                defaultValue, false, false);
    }



    /**
     * Creates a new column to store a float.
     * <p/>
     * A small (single-precision) floating-point number. Allowable values are
     * -3.402823466E+38 to -1.175494351E-38, 0, and 1.175494351E-38 to
     * 3.402823466E+38. These are the theoretical limits, based on the IEEE
     * standard. The actual range might be slightly smaller depending on your
     * hardware or operating system.
     * 
     * @param name
     *            name of the column
     * @param unsigned
     *            if <code>true</code>, only positive numbers allowed
     * @param zerofill
     *            if <code>true</code>, show the zeros preceding a number
     * @param notNull
     *            if <code>true</code>, force that the column must not be null
     * @return FLOAT column
     */
    public static Column columnFloat(String name, boolean unsigned,
            boolean zerofill, boolean notNull) {
        return new Column(ColumnType.FLOAT, name, unsigned, zerofill, notNull,
                false, false);
    }



    /**
     * Creates a new column to store a float.
     * <p/>
     * A small (single-precision) floating-point number. Allowable values are
     * -3.402823466E+38 to -1.175494351E-38, 0, and 1.175494351E-38 to
     * 3.402823466E+38. These are the theoretical limits, based on the IEEE
     * standard. The actual range might be slightly smaller depending on your
     * hardware or operating system.
     * 
     * @param name
     *            name of the column
     * @param unsigned
     *            if <code>true</code>, only positive numbers allowed
     * @param zerofill
     *            if <code>true</code>, show the zeros preceding a number
     * @param notNull
     *            if <code>true</code>, force that the column must not be null
     * @param defaultValue
     *            default value
     * @return FLOAT column
     */
    public static Column columnFloat(String name, boolean unsigned,
            boolean zerofill, boolean notNull, float defaultValue) {
        return new Column(ColumnType.FLOAT, name, unsigned, zerofill, notNull,
                Float.toString(defaultValue), false, false);
    }



    /**
     * Creates a new column to store an integer.
     * <p/>
     * A normal-size integer. The signed range is -2147483648 to 2147483647. The
     * unsigned range is 0 to 4294967295.
     * 
     * @param name
     *            name of the column
     * @param unsigned
     *            if <code>true</code>, only positive numbers allowed
     * @param zerofill
     *            if <code>true</code>, show the zeros preceding a number
     * @param notNull
     *            if <code>true</code>, force that the column must not be null
     * @param primaryKey
     *            if <code>true</code>, make the column a primary key
     * @param autoIncrement
     *            if <code>true</code>, auto increment the value of the column
     * @return INTEGER column
     */
    public static Column columnInteger(String name, boolean unsigned,
            boolean zerofill, boolean notNull, boolean primaryKey,
            boolean autoIncrement) {
        return new Column(ColumnType.INT, name, unsigned, zerofill, notNull,
                primaryKey, autoIncrement);
    }



    /**
     * Creates a new column to store an integer.
     * <p/>
     * A normal-size integer. The signed range is -2147483648 to 2147483647. The
     * unsigned range is 0 to 4294967295.
     * 
     * @param name
     *            name of the column
     * @param unsigned
     *            if <code>true</code>, only positive numbers allowed
     * @param zerofill
     *            if <code>true</code>, show the zeros preceding a number
     * @param notNull
     *            if <code>true</code>, force that the column must not be null
     * @param defaultValue
     *            default value
     * @param primaryKey
     *            if <code>true</code>, make the column a primary key
     * @param autoIncrement
     *            if <code>true</code>, auto increment the value of the column
     * @return INTEGER column
     */
    public static Column columnInteger(String name, boolean unsigned,
            boolean zerofill, boolean notNull, int defaultValue,
            boolean primaryKey, boolean autoIncrement) {
        return new Column(ColumnType.INT, name, unsigned, zerofill, notNull,
                Integer.toString(defaultValue), primaryKey, autoIncrement);
    }



    /**
     * Creates a new column to store text/string.
     * <p/>
     * A TEXT column with a maximum length of 65,535 characters. The effective
     * maximum length is less if the value contains multi-byte characters. Each
     * TEXT value is stored using a two-byte length prefix that indicates the
     * number of bytes in the value.
     * 
     * @param name
     *            name of the column
     * @param notNull
     *            if <code>true</code>, force that the column must not be null
     * @return TEXT column
     */
    public static Column columnText(String name, boolean notNull) {
        return new Column(ColumnType.TEXT, name, false, false, notNull, false,
                false);
    }



    /**
     * Creates a new column to store text/string.
     * <p/>
     * A TEXT column with a maximum length of 65,535 characters. The effective
     * maximum length is less if the value contains multi-byte characters. Each
     * TEXT value is stored using a two-byte length prefix that indicates the
     * number of bytes in the value.
     * 
     * @param name
     *            name of the column
     * @param notNull
     *            if <code>true</code>, force that the column must not be null
     * @param defaultValue
     *            default value
     * @return TEXT column
     */
    public static Column columnText(String name, boolean notNull,
            String defaultValue) {
        return new Column(ColumnType.TEXT, name, false, false, notNull,
                defaultValue, false, false);
    }



    /**
     * Creates a new column to store a tiny integer.
     * <p/>
     * A very small integer. The signed range is -128 to 127. The unsigned range
     * is 0 to 255.
     * 
     * @param name
     *            name of the column
     * @param notNull
     *            if <code>true</code>, force that the column must not be null
     * @param primaryKey
     *            if <code>true</code>, make the column a primary key
     * @param autoIncrement
     *            if <code>true</code>, auto increment the value of the column
     * @return TINYINT column
     */
    public static Column columnTinyInt(String name, boolean notNull,
            boolean primaryKey, boolean autoIncrement) {
        return new Column(ColumnType.TINYINT, name, false, false, notNull,
                primaryKey, autoIncrement);
    }



    /**
     * Creates a new column to store a tiny integer.
     * <p/>
     * A very small integer. The signed range is -128 to 127. The unsigned range
     * is 0 to 255.
     * 
     * @param name
     *            name of the column
     * @param notNull
     *            if <code>true</code>, force that the column must not be null
     * @param defaultValue
     *            default value
     * @param primaryKey
     *            if <code>true</code>, make the column a primary key
     * @param autoIncrement
     *            if <code>true</code>, auto increment the value of the column
     * @return TINYINT column
     */
    public static Column columnTinyInt(String name, boolean notNull,
            int defaultValue, boolean primaryKey, boolean autoIncrement) {
        return new Column(ColumnType.TINYINT, name, false, false, notNull,
                Integer.toString(defaultValue), primaryKey, autoIncrement);
    }
}
