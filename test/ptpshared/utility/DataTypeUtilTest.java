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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class DataTypeUtilTest {

  @Test
  public void testIsBoolean() {
    assertTrue(DataTypeUtil.isBoolean("true"));
    assertTrue(DataTypeUtil.isBoolean("TRUE"));
    assertTrue(DataTypeUtil.isBoolean("yes"));
    assertTrue(DataTypeUtil.isBoolean("yEs"));
    assertTrue(DataTypeUtil.isBoolean("false"));
    assertTrue(DataTypeUtil.isBoolean("FALsE"));
    assertTrue(DataTypeUtil.isBoolean("no"));
    assertTrue(DataTypeUtil.isBoolean("NO"));

    assertFalse(DataTypeUtil.isBoolean("abc"));
    assertFalse(DataTypeUtil.isBoolean("1"));
    assertFalse(DataTypeUtil.isBoolean("1.04"));
  }



  @Test
  public void testIsDouble() {
    assertTrue(DataTypeUtil.isDouble("1"));
    assertTrue(DataTypeUtil.isDouble("1232523523"));
    assertTrue(DataTypeUtil.isDouble("-1"));
    assertTrue(DataTypeUtil.isDouble("-1232523523"));
    assertTrue(DataTypeUtil.isDouble("1.023421"));
    assertTrue(DataTypeUtil.isDouble("-1.023421"));

    assertFalse(DataTypeUtil.isDouble("abc"));
    assertFalse(DataTypeUtil.isDouble("1.01.01.0"));
  }



  @Test
  public void testIsInteger() {
    assertTrue(DataTypeUtil.isInteger("1"));
    assertTrue(DataTypeUtil.isInteger("1232523523"));
    assertTrue(DataTypeUtil.isInteger("-1"));
    assertTrue(DataTypeUtil.isInteger("-1232523523"));

    assertFalse(DataTypeUtil.isInteger("1.0"));
    assertFalse(DataTypeUtil.isInteger("1.045436"));
    assertFalse(DataTypeUtil.isInteger("abc"));
  }

}
