package ptpshared.geom;

import edu.umd.cs.findbugs.annotations.CheckReturnValue;

/**
 * When an object can be transformed using an affine transformation.
 * 
 * @author ppinard
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
