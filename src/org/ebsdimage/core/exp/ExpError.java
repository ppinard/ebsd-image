package org.ebsdimage.core.exp;

import net.jcip.annotations.Immutable;

import org.ebsdimage.core.ErrorCode;

/**
 * Minor error occurring during the execution of an experiment. The error will
 * be saved in the <code>ErrorMap</code> of the EBSD multimap.
 * 
 * @author Philippe T. Pinard
 */
@Immutable
public class ExpError extends Exception {

    /** Error code associated with this exception. */
    private final ErrorCode errorCode;



    /**
     * Creates a new <code>ExpError</code>.
     * 
     * @param errorCode
     *            error code describing the error
     */
    public ExpError(ErrorCode errorCode) {
        super(errorCode.getLabel());
        this.errorCode = errorCode;
    }



    /**
     * Returns the error code.
     * 
     * @return error code
     */
    public ErrorCode getErrorCode() {
        return errorCode;
    }

}
