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

import java.util.logging.Logger;

import org.junit.Before;
import org.junit.Test;

import ptpshared.util.LoggerUtil;
import ptpshared.util.sql.Column;
import ptpshared.util.sql.ColumnFactory;
import ptpshared.util.sql.ColumnType;

public class ColumnTest {

    private Column column1;

    private Column column2;



    @Before
    public void setUp() throws Exception {
        Logger logger = Logger.getLogger("ptpshared.utility.sql");
        LoggerUtil.turnOffLogger(logger);

        column1 =
                new Column(ColumnType.TEXT, "col1.test", false, false, false,
                        "NULL", false, false);
        column2 =
                new Column(ColumnType.FLOAT, "col2/test", true, true, true,
                        true, true);
    }



    @Test
    public void testColumnColumnTypeStringBooleanBooleanBooleanBooleanBoolean() {
        assertEquals(ColumnType.FLOAT, column2.type);
        assertEquals("col2_test", column2.name);
        assertEquals(true, column2.unsigned);
        assertEquals(true, column2.zerofill);
        assertEquals(true, column2.notNull);
        assertEquals(null, column2.defaultValue);
        assertEquals(true, column2.primaryKey);
        assertEquals(true, column2.autoIncrement);
        assertEquals(
                "col2_test FLOAT UNSIGNED ZEROFILL NOT NULL AUTO_INCREMENT",
                column2.command);
    }



    @Test
    public void testColumnColumnTypeStringBooleanBooleanBooleanStringBooleanBoolean() {
        assertEquals(ColumnType.TEXT, column1.type);
        assertEquals("col1_test", column1.name);
        assertEquals(false, column1.unsigned);
        assertEquals(false, column1.zerofill);
        assertEquals(false, column1.notNull);
        assertEquals(null, column1.defaultValue);
        assertEquals(false, column1.primaryKey);
        assertEquals(false, column1.autoIncrement);
        assertEquals("col1_test TEXT", column1.command);
    }



    @Test
    public void testEqualsObject() {
        Column tmpColumn = ColumnFactory.columnBoolean("col1_test", false);
        assertEquals(column1, tmpColumn);
    }



    @Test
    public void testFormatValue() {
        String expected;

        expected = "'random text'";
        assertEquals(expected, column1.formatValue("random text"));

        expected = "2.0";
        assertEquals(expected, column2.formatValue("2.0"));
    }



    @Test
    public void testParseType() {
        assertEquals(ColumnType.INT, Column.parseType("int(4)"));
        assertEquals(ColumnType.TEXT, Column.parseType("TEXT DEFAULT 'abc'"));
        assertEquals(ColumnType.FLOAT, Column.parseType("float(4, 2)"));
        assertEquals(ColumnType.DATETIME, Column.parseType("DATETIME"));
        assertEquals(ColumnType.BOOLEAN, Column.parseType("tinyint(1)"));
        assertEquals(ColumnType.TINYINT, Column.parseType("tinyint(4)"));
    }



    @Test
    public void testToString() {
        assertEquals(column1.command, column1.toString());
    }

}
