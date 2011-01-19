package ptpshared.core.geom;

import ptpshared.core.math.Vector3D;

/**
 * When an object can be translated.
 * 
 * @author ppinard
 */
public interface Translatable {

    /**
     * Translates an object by the vector <code>t</code>.
     * 
     * @param t
     *            translation vector
     * @return translated object
     */
    public Translatable translate(Vector3D t);
}
