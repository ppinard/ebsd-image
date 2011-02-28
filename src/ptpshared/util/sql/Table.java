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
package ptpshared.util.sql;

import java.util.ArrayList;

/**
 * Defines a SQL table. Only the name of the table and the columns' description
 * are stored. No values are saved. Use {@link #toString()} to output the SQL
 * command to create this table.
 * 
 * @author Philippe T. Pinard
 */
public class Table {

    /** Name of the table. */
    public final String name;

    /** Columns of the table. */
    private ArrayList<Column> columns;



    /**
     * Creates a new SQL table.
     * 
     * @param name
     *            name of the table
     */
    public Table(String name) {
        this.name = name;
        columns = new ArrayList<Column>();
    }



    /**
     * Adds a column to the table.
     * 
     * @param column
     *            a <code>Column</code>
     */
    public void addColumn(Column column) {
        if (columns.contains(column))
            throw new IllegalArgumentException("Column (" + column.name + ") "
                    + "already exists.");
        columns.add(column);
    }



    /**
     * Checks if a column exists.
     * 
     * @param column
     *            a <code>Column</code>
     * @return <code>true</code> if the column exists
     */
    public boolean existsColumn(Column column) {
        return columns.contains(column);
    }



    /**
     * Checks if a column exists.
     * 
     * @param columnName
     *            name of the column
     * @return <code>true</code> if the column exists
     */
    public boolean existsColumn(String columnName) {
        for (int i = 0; i < columns.size(); i++)
            if (columns.get(i).name.equals(columnName))
                return true;

        return false;
    }



    /**
     * Returns a column named <code>columnName</code>.
     * 
     * @param columnName
     *            name of the column
     * @return a <code>Column</code>
     * @throws IllegalArgumentException
     *             if the column doesn't exist
     */
    public Column getColumn(String columnName) {
        for (int i = 0; i < columns.size(); i++)
            if (columns.get(i).name.equals(columnName))
                return columns.get(i);

        throw new IllegalArgumentException("Column (" + columnName
                + ") doesn't exist.");
    }



    /**
     * Returns the name of all the columns in the table.
     * 
     * @return array of column names
     */
    public String[] getColumnNames() {
        ArrayList<String> names = new ArrayList<String>();

        for (Column column : columns)
            names.add(column.name);

        return names.toArray(new String[names.size()]);
    }



    /**
     * Returns all the columns in the table.
     * 
     * @return array of <code>Column</code>
     */
    public Column[] getColumns() {
        return columns.toArray(new Column[columns.size()]);
    }



    /**
     * Returns the SQL command to create the table.
     * 
     * @return SQL command
     */
    @Override
    public String toString() {
        StringBuilder command = new StringBuilder();

        command.append("CREATE TABLE " + name + "(");
        for (Column column : columns)
            command.append(column.command + ", ");

        for (Column column : columns)
            if (column.primaryKey)
                command.append("PRIMARY KEY (`" + column.name + "`), ");

        command.setLength(command.length() - 2);
        command.append(")");

        return command.toString();
    }



    /**
     * Removes a <code>column</code> from the table.
     * 
     * @param column
     *            a <code>Column</code>
     */
    public void removeColumn(Column column) {
        columns.remove(column);
    }



    /**
     * Removes the column at <code>index</code> from the table.
     * 
     * @param index
     *            index of the column
     */
    public void removeColumn(int index) {
        columns.remove(index);
    }



    /**
     * Removes the column with the name <code>columnName</code> from the table.
     * 
     * @param columnName
     *            name of the column
     */
    public void removeColumn(String columnName) {
        for (int i = 0; i < columns.size(); i++)
            if (columns.get(i).name.equals(columnName))
                columns.remove(i);
    }

}
