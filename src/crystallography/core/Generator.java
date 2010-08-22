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
package crystallography.core;

import ptpshared.core.math.Matrix3D;
import ptpshared.core.math.Vector3D;

/**
 * Space group's generator to calculate equivalent positions.
 * 
 * @author Philippe T. Pinard
 */
public class Generator {

    /** Rotation matrix. */
    public final Matrix3D matrix;

    /** Translation vector. */
    public final Vector3D translation;



    /**
     * Creates a new <code>Generator</code>.
     * 
     * @param matrix
     *            rotation matrix
     * @param translation
     *            translation vector
     */
    public Generator(Matrix3D matrix, Vector3D translation) {
        this.matrix = matrix;
        this.translation = translation;
    }



    /**
     * Apply the generator on an atom position.
     * 
     * @param atom
     *            an atom
     * @return resultant atom after applying the generator
     */
    public AtomSite apply(AtomSite atom) {
        Vector3D position = matrix.multiply(atom.position).plus(translation);

        return new AtomSite(atom.atomicNumber, position);
    }



    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;

        Generator other = (Generator) obj;
        if (matrix == null) {
            if (other.matrix != null)
                return false;
        } else if (!matrix.equals(other.matrix))
            return false;
        if (translation == null) {
            if (other.translation != null)
                return false;
        } else if (!translation.equals(other.translation))
            return false;

        return true;
    }



    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((matrix == null) ? 0 : matrix.hashCode());
        result =
                prime * result
                        + ((translation == null) ? 0 : translation.hashCode());
        return result;
    }
}
