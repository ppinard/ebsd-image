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
import org.apache.commons.math.geometry.Vector3D;
import org.simpleframework.xml.transform.Matcher;
import org.simpleframework.xml.transform.Transform;

/**
 * Simple XML matcher for some classes of the Apache Common Math library.
 * 
 * @author Philippe T. Pinard
 */
public class ApacheCommonMathMatcher implements Matcher {

    @SuppressWarnings("rawtypes")
    @Override
    public Transform match(Class type) throws Exception {
        if (type == Rotation.class)
            return new RotationTransform();
        else if (type == Vector3D.class)
            return new Vector3DTransform();
        return null;
    }

}
