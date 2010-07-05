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

import java.util.HashMap;

/**
 * A singleton containing a <code>HashMap</code> of buffers. Each buffer is
 * identified by a unique key. Only one cache can exist in a session.
 * <p/>
 * <b>References:</b>
 * <ul>
 * <li>Bill Pugh (http://en.wikipedia.org/wiki/Singleton_pattern)</li>
 * </ul>
 * 
 * @author Philippe T. Pinard
 */
public final class Cache extends HashMap<String, Buffer> {
    /**
     * SingletonHolder is loaded on the first execution of
     * Singleton.getInstance() or the first access to SingletonHolder.INSTANCE,
     * not before.
     */
    private static class CacheHolder {
        /** Cache instance. */
        private static final Cache INSTANCE = new Cache();
    }



    /**
     * Returns a cache instance.
     * 
     * @return cache instance
     */
    public static Cache getInstance() {
        return CacheHolder.INSTANCE;
    }



    /**
     * Private constructor prevents instantiation from other classes.
     */
    private Cache() {
    }



    /**
     * Flush all buffers in the cache.
     */
    public void flush() {
        for (Buffer buffer : values())
            buffer.flush();
    }
}
