package ptpshared.util;

/**
 * Interface to test for equality using a precision.
 * 
 * @author ppinard
 */
public interface AlmostEquable {

    /**
     * Checks if this object is almost equal to the another object with the
     * given precision.
     * 
     * @param obj
     *            other object
     * @param precision
     *            level of precision
     * @return whether the two <code>Object</code> are almost equal
     */
    public boolean equals(Object obj, double precision);
}
