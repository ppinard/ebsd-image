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
package ptpshared.geom;

import edu.umd.cs.findbugs.annotations.CheckReturnValue;

/**
 * When an object can be transformed using an affine transformation.
 * 
 * @author Philippe T. Pinard
 */
public interface Transformable {

    /**
     * Rotates and translates an object by the specified affine transformation.
     * 
     * @param t
     *            affine transformation
     * @return transformed object
     */
    @CheckReturnValue
    public Transformable transform(AffineTransform3D t);

}
