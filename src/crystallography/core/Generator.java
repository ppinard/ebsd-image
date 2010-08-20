package crystallography.core;

import ptpshared.core.math.Matrix3D;
import ptpshared.core.math.Vector3D;

/**
 * Space group's generator to calculate equivalent positions.
 * 
 * @author ppinard
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
