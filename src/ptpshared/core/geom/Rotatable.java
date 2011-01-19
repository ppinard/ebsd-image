package ptpshared.core.geom;

import ptpshared.core.math.Quaternion;

/**
 * When an object can be translated.
 * 
 * @author ppinard
 */
public interface Rotatable {

    /**
     * Rotates an object by the specified rotation.
     * 
     * @param r
     *            rotation
     * @return rotated object
     */
    public Rotatable rotate(Quaternion r);
}
