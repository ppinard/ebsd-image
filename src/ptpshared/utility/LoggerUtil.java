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

import java.util.logging.Filter;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * Utilities to use in conjunction with <code>java.util.logging</code>.
 * 
 * @author Philippe T. Pinard
 * 
 */
public class LoggerUtil {

    /**
     * Turn off the specified <code>logger</code>.
     * 
     * @param logger
     *            a <code>Logger</code>
     */
    public static void turnOffLogger(Logger logger) {
        logger.setFilter(new Filter() {
            public boolean isLoggable(LogRecord record) {
                return false;
            }
        });
    }

}
