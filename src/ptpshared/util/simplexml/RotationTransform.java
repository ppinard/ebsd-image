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
package ptpshared.util.simplexml;

import org.apache.commons.math.geometry.Rotation;
import org.simpleframework.xml.transform.Transform;

/**
 * Simple XML transform for Apache Common Math's <code>Rotation</code>.
 * 
 * @author Philippe T. Pinard
 */
public class RotationTransform implements Transform<Rotation> {

    @Override
    public Rotation read(String value) throws Exception {
        String[] qs = value.split(";");
        if (qs.length != 4)
            throw new IllegalArgumentException(
                    "String contains more than 4 values.");

        return new Rotation(Double.parseDouble(qs[0]),
                Double.parseDouble(qs[1]), Double.parseDouble(qs[2]),
                Double.parseDouble(qs[3]), false);
    }



    @Override
    public String write(Rotation value) throws Exception {
        return value.getQ0() + ";" + value.getQ1() + ";" + value.getQ2() + ";"
                + value.getQ3();
    }

}
