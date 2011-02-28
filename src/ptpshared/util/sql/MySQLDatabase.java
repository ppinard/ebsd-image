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

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Connection to a MySQL database. Wrapper over the JDBC MySQL connection.
 * 
 * @author Philippe T. Pinard
 */
public class MySQLDatabase {

    /** Connection to database. */
    public Connection conn = null;

    /** Name of the database. */
    public final String name;



    /**
     * Returns the connection string to use to connect to the specified
     * database.
     * 
     * @param host
     *            address of the host
     * @param databaseName
     *            name of the database
     * @param username
     *            username to connect to the database
     * @param password
     *            password to connect to the database
     * @return connection command
     */
    public static String createConnectionString(String host,
            String databaseName, String username, String password) {
        StringBuilder connection = new StringBuilder();
        connection.append(host);
        if (!(host.endsWith("/")))
            connection.append("/");
        connection.append(databaseName);

        return "jdbc:mysql:" + connection + "?" + "user=" + username + "&"
                + "password=" + password;
    }



    /**
     * Creates a new <code>MySQLDatabase</code> using the established
     * <code>conn</code>.
     * 
     * @param conn
     *            a SQL connection
     * @throws RuntimeException
     *             if the connection cannot be established
     */
    public MySQLDatabase(Connection conn) {
        initJDBC();
        this.conn = conn;

        try {
            Statement s = conn.createStatement();
            s.executeQuery("SELECT DATABASE()");
            ResultSet rs = s.getResultSet();

            name = rs.getString(1);

            rs.close();
            s.close();
        } catch (SQLException ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }



    /**
     * Creates a new <code>MySQLDatabase</code> by opening a connection to a
     * server.
     * 
     * @param host
     *            server address
     * @param databaseName
     *            name of the database
     * @param username
     *            username of the database
     * @param password
     *            password of the database
     * @throws RuntimeException
     *             if the connection cannot be established
     */
    public MySQLDatabase(String host, String databaseName, String username,
            String password) {
        name = databaseName;

        initJDBC();

        String connectionString =
                MySQLDatabase.createConnectionString(host, databaseName,
                        username, password);

        try {
            conn = DriverManager.getConnection(connectionString);

            // Do something with the Connection

        } catch (SQLException ex) {
            throw new RuntimeException(
                    "Connection to the following address could "
                            + "not be established (" + connectionString + ")");
        }
    }



    /**
     * Creates a new table in the database.
     * 
     * @param table
     *            <code>Table</code> to be added.
     * @throws IllegalArgumentException
     *             if a table with the same name already exists
     * @throws RuntimeException
     *             if the table cannot be created
     */
    public void createTable(Table table) {
        if (isTable(table.name))
            throw new IllegalArgumentException("Table named (" + table.name
                    + ") " + "already exist. A new table cannot be created.");

        try {
            Statement s = conn.createStatement();
            s.executeUpdate(table.toString());
            s.close();
        } catch (SQLException ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }



    /**
     * Drops a table from the database.
     * 
     * @param name
     *            name of the table to be dropped
     * @throws IllegalArgumentException
     *             if no table with the specified name exists in the database
     * @throws RuntimeException
     *             if the table cannot be dropped
     */
    public void dropTable(String name) {
        if (!(isTable(name)))
            throw new IllegalArgumentException("Table named (" + name + ") "
                    + "doesn't exist.");

        try {
            Statement s = conn.createStatement();
            s.executeUpdate("DROP TABLE " + name);
            s.close();
        } catch (SQLException ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }



    /**
     * Drops a table from the database.
     * 
     * @param table
     *            <code>Table</code> to be dropped
     * @throws IllegalArgumentException
     *             if no table with the specified name exists in the database
     * @throws RuntimeException
     *             if the table cannot be dropped
     */
    public void dropTable(Table table) {
        dropTable(table.name);
    }



    /**
     * Returns a table from the database.
     * 
     * @param name
     *            name of the table
     * @return a <code>Table</code> object
     * @throws IllegalArgumentException
     *             if no table with the specified name exists in the database
     * @throws RuntimeException
     *             if the table cannot be returned
     */
    public Table getTable(String name) {
        if (!(isTable(name)))
            throw new IllegalArgumentException("Table named (" + name + ") "
                    + "doesn't exist.");

        Table table = new Table(name);

        try {
            Statement s = conn.createStatement();
            s.executeQuery("SHOW COLUMNS FROM " + name);
            ResultSet rs = s.getResultSet();

            Column column;

            while (rs.next()) {

                String columnName = rs.getString("Field");
                ColumnType type = Column.parseType(rs.getString("Type"));

                boolean unsigned = false;
                if (rs.getString("Type").toLowerCase().contains("unsigned"))
                    unsigned = true;

                boolean zerofill = false;
                if (rs.getString("Type").toLowerCase().contains("zerofill"))
                    zerofill = true;

                boolean notNull = !(rs.getBoolean("Null"));

                String primaryKeyString = rs.getString("Key");
                boolean primaryKey = false;
                if (primaryKeyString.length() > 0)
                    primaryKey = true;

                String defaultValue = rs.getString("Default");

                boolean autoIncrement = false;
                if (rs.getString("Extra").toLowerCase().contains(
                        "auto_increment"))
                    autoIncrement = true;

                column =
                        new Column(type, columnName, unsigned, zerofill,
                                notNull, defaultValue, primaryKey,
                                autoIncrement);

                table.addColumn(column);
            }

            rs.close();
            s.close();
        } catch (SQLException ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }

        return table;
    }



    /**
     * Returns an array of the name of all the tables in the database.
     * 
     * @return name of tables
     */
    public String[] getTableNames() {
        ArrayList<String> names = new ArrayList<String>();

        try {
            Statement s = conn.createStatement();
            s.executeQuery("SHOW TABLES");
            ResultSet rs = s.getResultSet();

            while (rs.next())
                names.add(rs.getString(1));

            rs.close();
            s.close();
        } catch (SQLException ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }

        return names.toArray(new String[names.size()]);
    }



    /**
     * Initializes the MySQL JDBC driver.
     */
    private void initJDBC() {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (Exception ex) {
            throw new RuntimeException(
                    "MySQL driver for JDBC could not be found.");
        }
    }



    /**
     * Inserts a row a table.
     * 
     * @param table
     *            a <code>Table</code>
     * @param values
     *            list of the values of the new row
     * @return id of the newly inserted row
     * @throws IllegalArgumentException
     *             if the table doesn't exist in the database
     * @throws IllegalArgumentException
     *             if a column name specified in <code>row</code> doesn't exist
     *             in the table
     * @throws RuntimeException
     *             if the row cannot be inserted
     */
    public int insertRow(Table table, ArrayList<Object> values) {
        HashMap<String, Object> row = new HashMap<String, Object>();

        String[] columnNames = table.getColumnNames();

        for (int i = 0; i < values.size(); i++) {
            String columnName = columnNames[i];
            Object value = values.get(i);
            row.put(columnName, value);
        }

        return insertRow(table, row);
    }



    /**
     * Inserts a row a table.
     * 
     * @param table
     *            a <code>Table</code>
     * @param row
     *            <code>HashMap</code> where the keys are the name of the
     *            columns and the values are the values to be inserted
     * @return id of the newly inserted row
     * @throws IllegalArgumentException
     *             if the table doesn't exist in the database
     * @throws IllegalArgumentException
     *             if a column name specified in <code>row</code> doesn't exist
     *             in the table
     * @throws RuntimeException
     *             if the row cannot be inserted
     */
    public int insertRow(Table table, HashMap<String, Object> row) {
        if (!(isTable(name)))
            throw new IllegalArgumentException("Table named (" + name + ") "
                    + "doesn't exist.");

        // Verify inputs
        for (String columnName : row.keySet())
            if (!(table.existsColumn(columnName)))
                throw new IllegalArgumentException("Column name (" + columnName
                        + ") " + "doesn't exist in table (" + table.name + ")");

        // Command
        StringBuilder command = new StringBuilder();

        command.append("INSERT INTO " + table.name + "(");

        for (String columnName : row.keySet())
            command.append(columnName + ", ");

        command.setLength(command.length() - 2);
        command.append(") VALUES (");

        for (String columnName : row.keySet()) {
            Column column = table.getColumn(columnName);
            String value = column.formatValue(row.get(columnName).toString());
            command.append(value + ", ");
        }

        command.setLength(command.length() - 2);
        command.append(")");

        // Execute command
        int id = -1;

        try {
            Statement s = conn.createStatement();
            s.executeUpdate(command.toString());
            s.close();

            // Get id
            s = conn.createStatement();
            s.executeQuery("SELECT LAST_INSERT_ID()");
            ResultSet rs = s.getResultSet();

            rs.first();
            id = rs.getInt(1);

            rs.close();
            s.close();
        } catch (SQLException ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }

        return id;
    }



    /**
     * Checks if the table named <code>name</code> exists in the database.
     * 
     * @param name
     *            name of the table
     * @return <code>true</code> if the table exists
     */
    public boolean isTable(String name) {
        ArrayList<String> names =
                new ArrayList<String>(Arrays.asList(getTableNames()));
        return names.contains(name);
    }

}
