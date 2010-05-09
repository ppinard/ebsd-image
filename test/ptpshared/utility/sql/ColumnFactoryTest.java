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

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ColumnFactoryTest {

  @Test
  public void testColumnBoolean() {
    Column column = ColumnFactory.columnBoolean("col1", false);
    assertEquals("col1 BOOLEAN NOT NULL DEFAULT FALSE", column.command);
  }



  @Test
  public void testColumnDateTime() {
    Column column = ColumnFactory.columnDateTime("col1", true);
    assertEquals("col1 DATETIME NOT NULL", column.command);
  }



  @Test
  public void testColumnFloat() {
    Column column = ColumnFactory.columnFloat("col1", false, true, false, 0.0f);
    assertEquals("col1 FLOAT ZEROFILL DEFAULT 0.0", column.command);
  }



  @Test
  public void testColumnInteger() {
    Column column =
        ColumnFactory.columnInteger("col1", true, false, false, true, true);
    assertEquals("col1 INT UNSIGNED AUTO_INCREMENT", column.command);
  }



  @Test
  public void testColumnText() {
    Column column = ColumnFactory.columnText("col1", true, "abc");
    assertEquals("col1 TEXT NOT NULL DEFAULT abc", column.command);
  }



  @Test
  public void testColumnTinyInt() {
    Column column = ColumnFactory.columnTinyInt("col1", true, 0, false, false);
    assertEquals("col1 TINYINT NOT NULL DEFAULT 0", column.command);
  }

}
