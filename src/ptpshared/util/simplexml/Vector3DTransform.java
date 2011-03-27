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

import org.apache.commons.math.geometry.Vector3D;
import org.simpleframework.xml.transform.Transform;

/**
 * Simple XML transform for Apache Common Math's <code>Vector3D</code>.
 * 
 * @author Philippe T. Pinard
 */
public class Vector3DTransform implements Transform<Vector3D> {

    @Override
    public Vector3D read(String value) throws Exception {
        String[] qs = value.split(";");
        if (qs.length != 3)
            throw new IllegalArgumentException(
                    "String contains more than 3 values.");

        return new Vector3D(Double.parseDouble(qs[0]),
                Double.parseDouble(qs[1]), Double.parseDouble(qs[2]));
    }



    @Override
    public String write(Vector3D value) throws Exception {
        return value.getX() + ";" + value.getY() + ";" + value.getZ();
    }

}
