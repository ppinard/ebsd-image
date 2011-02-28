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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import ptpshared.util.sql.Column;
import ptpshared.util.sql.ColumnFactory;
import ptpshared.util.sql.Table;

public class TableTest {

    private Table table1;

    private Column column1;

    private Column column2;

    private Column column3;



    @Before
    public void setUp() throws Exception {
        table1 = new Table("table1");

        column1 = ColumnFactory.columnText("col1", false);
        column2 = ColumnFactory.columnText("col2", true, "default");
        column3 =
                ColumnFactory.columnInteger("col3", true, false, false, false,
                        false);
    }



    @Test
    public void testAddColumn() {
        table1.addColumn(column1);
        table1.addColumn(column2);
        table1.addColumn(column3);

        assertEquals(3, table1.getColumns().length);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testAddColumnException() {
        table1.addColumn(column1);
        table1.addColumn(column1);
    }



    @Test
    public void testExistsColumnColumn() {
        table1.addColumn(column1);

        assertTrue(table1.existsColumn(column1));
        assertFalse(table1.existsColumn(column2));
    }



    @Test
    public void testExistsColumnString() {
        table1.addColumn(column1);

        assertTrue(table1.existsColumn("col1"));
        assertFalse(table1.existsColumn("col2"));
    }



    @Test
    public void testGetColumn() {
        table1.addColumn(column1);

        assertEquals(column1, table1.getColumn("col1"));
    }



    @Test(expected = IllegalArgumentException.class)
    public void testGetColumnException() {
        table1.getColumn("col1");
    }



    @Test
    public void testGetColumnNames() {
        table1.addColumn(column3);
        table1.addColumn(column2);
        table1.addColumn(column1);

        String[] columnNames = table1.getColumnNames();

        assertEquals("col3", columnNames[0]);
        assertEquals("col2", columnNames[1]);
        assertEquals("col1", columnNames[2]);
    }



    @Test
    public void testGetColumns() {
        table1.addColumn(column3);
        table1.addColumn(column2);
        table1.addColumn(column1);

        Column[] columns = table1.getColumns();

        assertEquals(column3, columns[0]);
        assertEquals(column2, columns[1]);
        assertEquals(column1, columns[2]);
    }



    @Test
    public void testToString() {
        table1.addColumn(column1);
        table1.addColumn(column2);
        table1.addColumn(column3);

        String expected =
                "CREATE TABLE table1(col1 TEXT, col2 TEXT NOT NULL "
                        + "DEFAULT default, col3 INT UNSIGNED)";

        assertEquals(expected, table1.toString());
    }



    @Test
    public void testRemoveColumnColumn() {
        table1.addColumn(column1);
        table1.addColumn(column2);
        table1.addColumn(column3);

        table1.removeColumn(column2);

        assertEquals(2, table1.getColumns().length);
        assertFalse(table1.existsColumn(column2));
    }



    @Test
    public void testRemoveColumnInt() {
        table1.addColumn(column1);
        table1.addColumn(column2);
        table1.addColumn(column3);

        table1.removeColumn(1);

        assertEquals(2, table1.getColumns().length);
        assertFalse(table1.existsColumn(column2));
    }



    @Test
    public void testRemoveColumnString() {
        table1.addColumn(column1);
        table1.addColumn(column2);
        table1.addColumn(column3);

        table1.removeColumn("col2");

        assertEquals(2, table1.getColumns().length);
        assertFalse(table1.existsColumn(column2));
    }



    @Test
    public void testTable() {
        assertEquals("table1", table1.name);
    }

}
