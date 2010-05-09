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

import org.ebsdimage.core.HoughMap;
import org.ebsdimage.io.HoughMapLoader;
import org.junit.Assert;

import rmlshared.io.FileUtil;


import rmlimage.io.BmpLoader;
import rmlimage.io.JpgLoader;
import rmlimage.module.real.core.RealMap;
import rmlimage.module.real.io.RmpLoader;




public class TestCase
{
  
  public void assertArrayEquals(byte[] expected, byte[] actual)
  {
    if (actual == null)  Assert.fail("actual is null");
    if (expected == null)  Assert.fail("expected is null");
    
    if (actual.length != expected.length)
      Assert.fail("expected length (" + expected.length 
                  + ") is different from actual length (" + actual.length 
                  + ").");

    int size = actual.length;      
    for (int n=0; n<size; n++)
      if (actual[n] != expected[n])
        throw new AssertionError("Pixel different at index " + n
                                 + ": Actual = " + actual[n] 
                                 + ", expected = " + expected[n]);
  }


  public static File getFile(String fileName)
  {
    File file = FileUtil.getFile(fileName);
    if (file == null)  Assert.fail(fileName + " not found.");

    return file;
  }



  public static Object load(String fileName)
  {
    return load(getFile(fileName));
  }



  public static Object load(File file)
  {
    String extension = FileUtil.getExtension(file);

    try
      {
        if (extension.equalsIgnoreCase("bmp"))
          return new BmpLoader().load(file);
        else
          if (extension.equalsIgnoreCase("jpg"))
            return new JpgLoader().load(file);
          else
            if (extension.equalsIgnoreCase("hmp"))
              return (HoughMap)new HoughMapLoader().load(file);
            else
              if (extension.equalsIgnoreCase("rmp"))
                return (RealMap)new RmpLoader().load(file);
      }
    catch (IOException e)
      {
        Assert.fail(e.getMessage());
        return null;
      }

    Assert.fail("Cannot load " + extension + " files.");
    return null;
  }


}
