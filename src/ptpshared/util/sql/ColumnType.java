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

/**
 * Enumeration of SQL column types.
 * 
 * @author Philippe T. Pinard
 */
public enum ColumnType {
    /**
     * A very small integer. The signed range is -128 to 127. The unsigned range
     * is 0 to 255.
     */
    TINYINT,

    /** True or false value. */
    BOOLEAN,

    /** A string of less than 65,535 characters. */
    TEXT,

    /**
     * A normal-size integer. The signed range is -2147483648 to 2147483647. The
     * unsigned range is 0 to 4294967295.
     */
    INT,

    /**
     * A small (single-precision) floating-point number. Allowable values are
     * -3.402823466E+38 to -1.175494351E-38, 0, and 1.175494351E-38 to
     * 3.402823466E+38.
     */
    FLOAT,

    /**
     * A date and time combination. The supported range is '1000-01-01 00:00:00'
     * to '9999-12-31 23:59:59'.
     */
    DATETIME
}
