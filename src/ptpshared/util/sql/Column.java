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
package ptpshared.util.sql;

import java.util.logging.Logger;

/**
 * Defines a SQL column. The definition of the column parameters are stored. Use
 * {@link #toString()} to output the SQL command to create this column.
 * 
 * @author Philippe T. Pinard
 */
public class Column {

    /**
     * Returns the correct column type from a string.
     * 
     * @param type
     *            string of the time.
     * @return column type
     * @throws RuntimeException
     *             if the type is invalid
     */
    public static ColumnType parseType(String type) {
        type = type.toLowerCase().trim();

        if (type.startsWith("int"))
            return ColumnType.INT;
        else if (type.startsWith("text"))
            return ColumnType.TEXT;
        else if (type.startsWith("float"))
            return ColumnType.FLOAT;
        else if (type.startsWith("datetime"))
            return ColumnType.DATETIME;
        else if (type.startsWith("tinyint(1)"))
            return ColumnType.BOOLEAN;
        else if (type.startsWith("tinyint"))
            return ColumnType.TINYINT;
        else
            throw new RuntimeException("The type (" + type + ") is invalid.");
    }

    /** If <code>true</code>, auto increment the value of the column. */
    public final boolean autoIncrement;

    /** SQL command to create the column. */
    public final String command;

    /** Default value of the column. */
    public final String defaultValue;

    /** Name of the column. */
    public final String name;

    /** If <code>true</code>, force that the column must not be null. */
    public final boolean notNull;

    /** If <code>true</code>, make the column a primary key. */
    public final boolean primaryKey;

    /** type of the column {@link ColumnType}. */
    public final ColumnType type;

    /** If <code>true</code>, only positive numbers allowed. */
    public final boolean unsigned;

    /** If <code>true</code>, show the zeros preceding a number. */
    public final boolean zerofill;



    /**
     * Creates a new SQL column.
     * 
     * @param type
     *            type of the column {@link ColumnType}
     * @param name
     *            name of the column. The name is automatically formatted to
     *            prevent non-permitted characters.
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
     */
    public Column(ColumnType type, String name, boolean unsigned,
            boolean zerofill, boolean notNull, boolean primaryKey,
            boolean autoIncrement) {
        this(type, name, unsigned, zerofill, notNull, null, primaryKey,
                autoIncrement);
    }



    /**
     * Create a new SQL column with a default value.
     * 
     * @param type
     *            type of the column {@link ColumnType}
     * @param name
     *            name of the column. The name is automatically formatted to
     *            prevent non-permitted characters.
     * @param unsigned
     *            if <code>true</code>, only positive numbers allowed
     * @param zerofill
     *            if <code>true</code>, show the zeros preceding a number
     * @param notNull
     *            if <code>true</code>, force that the column must not be null
     * @param defaultValue
     *            specified a default value for the column
     * @param primaryKey
     *            if <code>true</code>, make the column a primary key
     * @param autoIncrement
     *            if <code>true</code>, auto increment the value of the column
     */
    public Column(ColumnType type, String name, boolean unsigned,
            boolean zerofill, boolean notNull, String defaultValue,
            boolean primaryKey, boolean autoIncrement) {

        if (defaultValue != null)
            if ("NULL".equals(defaultValue.toUpperCase()))
                defaultValue = null;

        this.type = type;
        this.name = formatColumnName(name);
        this.unsigned = unsigned;
        this.zerofill = zerofill;
        this.notNull = notNull;
        this.defaultValue = defaultValue;
        this.primaryKey = primaryKey;
        this.autoIncrement = autoIncrement;
        command = getCommand();
    }



    /**
     * Checks if this <code>Column</code> is equal to the specified one. Two
     * columns are said to be equal if they have the same name.
     * 
     * @param obj
     *            other column
     * @return whether the <code>Column</code> is equal to the supplied
     *         <code>Object</code>
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;

        Column other = (Column) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;

        return true;
    }



    /**
     * Formats the column name to remove non-permitted character(s).
     * 
     * @param name
     *            name to be formatted
     * @return formatted name
     */
    private String formatColumnName(String name) {
        Logger logger = Logger.getLogger("ptpshared.utility.sql");

        if (name.contains(" ")) {
            logger.warning("Column name (" + name + ") contains space(s). "
                    + "Space(s) are be replaced by '_'");
            name = name.replace(" ", "_");
        }

        if (name.contains(".")) {
            logger.warning("Column name (" + name + ") contains dot(s). "
                    + "Dot(s) are be replaced by '_'");
            name = name.replace(".", "_");
        }

        if (name.contains(",")) {
            logger.warning("Column name (" + name + ") contains comma(s). "
                    + "Comma(s) are be replaced by '_'");
            name = name.replace(",", "_");
        }

        if (name.contains("/")) {
            logger.warning("Column name (" + name + ") contains comma(s). "
                    + "Comma(s) are be replaced by '_'");
            name = name.replace("/", "_");
        }

        return name;
    }



    /**
     * Formats the value of a column. Adds <code>'</code> if the column type is
     * TEXT.
     * 
     * @param value
     *            value to be formatted
     * @return formatted value
     */
    public String formatValue(String value) {
        value = value.replace("'", "''");
        if (type == ColumnType.TEXT)
            return "'" + value + "'";
        else
            return value;
    }



    /**
     * Creates the SQL command to create the column.
     * 
     * @return SQL command
     */
    private String getCommand() {
        StringBuilder command = new StringBuilder();

        command.append(name + " ");
        command.append(type.toString() + " ");

        if (unsigned)
            command.append("UNSIGNED ");
        if (zerofill)
            command.append("ZEROFILL ");
        if (notNull)
            command.append("NOT NULL ");
        if (defaultValue != null)
            command.append("DEFAULT " + defaultValue + " ");
        if (autoIncrement)
            command.append("AUTO_INCREMENT ");

        return command.toString().trim();
    }



    /**
     * Returns the hash code for this <code>Column</code>.
     * 
     * @return hash code
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }



    /**
     * Return the SQL command to create the column.
     * 
     * @return SQL command
     */
    @Override
    public String toString() {
        return command;
    }

}
