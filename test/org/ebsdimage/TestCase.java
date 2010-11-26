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
package org.ebsdimage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.ebsdimage.io.HoughMapLoader;
import org.junit.After;
import org.junit.Assert;

import ptpshared.util.AlmostEquable;
import rmlimage.io.BmpLoader;
import rmlimage.io.JpgLoader;
import rmlimage.module.real.io.RmpLoader;
import rmlshared.io.FileUtil;

public class TestCase {

    private ArrayList<File> files = new ArrayList<File>();



    @After
    public void tearDown() throws Exception {
        for (File file : files) {
            if (!file.exists())
                continue;

            if (file.isFile()) {
                FileUtil.remove(file);
            } else if (file.isDirectory()) {
                FileUtil.rmdir(file);
            }
        }
    }



    public void assertArrayEquals(byte[] expected, byte[] actual) {
        if (actual == null)
            Assert.fail("actual is null");
        if (expected == null)
            Assert.fail("expected is null");

        if (actual.length != expected.length)
            Assert.fail("expected length (" + expected.length
                    + ") is different from actual length (" + actual.length
                    + ").");

        int size = actual.length;
        for (int n = 0; n < size; n++)
            if (actual[n] != expected[n])
                throw new AssertionError("Pixel different at index " + n
                        + ": Actual = " + actual[n] + ", expected = "
                        + expected[n]);
    }



    /**
     * Asserts if two objects are almost equal to the specified precision.
     * 
     * @param expected
     *            expected object
     * @param actual
     *            actual object
     * @param precision
     *            level of precision
     */
    public void assertAlmostEquals(AlmostEquable expected,
            AlmostEquable actual, double precision) {
        if (actual == null)
            Assert.fail("actual is null");
        if (expected == null)
            Assert.fail("expected is null");

        if (!expected.equals(actual, precision))
            Assert.fail(format(null, expected, actual));
    }



    /**
     * Formats fail message. Copied from org.junit.Assert.
     * 
     * @param message
     *            message
     * @param expected
     *            expected object
     * @param actual
     *            actual object
     * @return fail message
     */
    private String format(String message, Object expected, Object actual) {
        String formatted = "";
        if (message != null && !message.equals(""))
            formatted = message + " ";
        String expectedString = String.valueOf(expected);
        String actualString = String.valueOf(actual);
        if (expectedString.equals(actualString))
            return formatted + "expected: "
                    + formatClassAndValue(expected, expectedString)
                    + " but was: " + formatClassAndValue(actual, actualString);
        else
            return formatted + "expected:<" + expectedString + "> but was:<"
                    + actualString + ">";
    }



    private String formatClassAndValue(Object value, String valueString) {
        String className = value == null ? "null" : value.getClass().getName();
        return className + "<" + valueString + ">";
    }



    public static File getFile(String fileName) {
        File file = FileUtil.getFile(fileName);
        if (file == null)
            Assert.fail(fileName + " not found.");

        return file;
    }



    public static Object load(String fileName) {
        return load(getFile(fileName));
    }



    public static Object load(File file) {
        String extension = FileUtil.getExtension(file);

        try {
            if (extension.equalsIgnoreCase("bmp"))
                return new BmpLoader().load(file);
            else if (extension.equalsIgnoreCase("jpg"))
                return new JpgLoader().load(file);
            else if (extension.equalsIgnoreCase("hmp"))
                return new HoughMapLoader().load(file);
            else if (extension.equalsIgnoreCase("rmp"))
                return new RmpLoader().load(file);
        } catch (IOException e) {
            Assert.fail(e.getMessage());
            return null;
        }

        Assert.fail("Cannot load " + extension + " files.");
        return null;
    }



    public File createTempFile() throws IOException {
        File file = File.createTempFile("tmp", null);
        files.add(file);
        return file;
    }



    public File createTempDir() throws IOException {
        File dir = File.createTempFile("tmp", Long.toString(System.nanoTime()));

        if (!dir.delete())
            throw new IOException("Could not delete temp file: "
                    + dir.getAbsolutePath());

        if (!dir.mkdir())
            throw new IOException("Could not create temp dir: "
                    + dir.getAbsolutePath());

        files.add(dir);

        return dir;
    }

}
