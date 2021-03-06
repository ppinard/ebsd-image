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
package org.ebsdimage;

import java.io.File;
import java.io.IOException;

import rmlimage.core.Map;
import rmlimage.io.IO;

public class TestCase extends junittools.test.TestCase {

    protected Map load(File file) {
        try {
            return IO.load(file);
        } catch (IOException e) {
            org.junit.Assert.fail(e.getMessage());
            return null;
        }
    }



    protected Map load(String fileName) {
        return load(getFile(fileName));
    }

}
