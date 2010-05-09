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
package ptpshared.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static rmlshared.io.FileUtil.getFile;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import rmlshared.io.JarFile;

public class FileUtilTest {

    @Test
    public void testJoinPackageNames() {
        String expected = "abc.def.ghi";

        String packageName = FileUtil.joinPackageNames("abc", "def", "ghi");
        assertEquals(expected, packageName);

        packageName = FileUtil.joinPackageNames("abc.", "def.", "ghi.");
        assertEquals(expected, packageName);

        packageName = FileUtil.joinPackageNames(".abc.", ".def.", ".ghi.");
        assertEquals(expected, packageName);

        packageName = FileUtil.joinPackageNames(".abc", ".def", ".ghi");
        assertEquals(expected, packageName);
    }



    @Test
    public void testJarContainsPackage() throws IOException {
        JarFile jarFile = new JarFile(getFile("ptpshared/testdata/test.jar"));

        String packagePath = "rmlimage/module/multi/core";
        assertTrue(FileUtil.jarContainsPackage(jarFile, packagePath));

        packagePath = "rmlimage/module/multi";
        assertTrue(FileUtil.jarContainsPackage(jarFile, packagePath));

        packagePath = "rmlimage/module/impossible";
        assertFalse(FileUtil.jarContainsPackage(jarFile, packagePath));
    }



    @Test
    public void testGetJarFiles() throws IOException {
        JarFile[] jarFiles = FileUtil
                .getJarFiles(getFile("ptpshared/testdata/"));
        assertEquals(1, jarFiles.length);
        assertEquals("test.jar", new File(jarFiles[0].getName()).getName());
    }

}
