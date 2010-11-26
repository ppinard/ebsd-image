package org.ebsdimage.core;

import net.jcip.annotations.Immutable;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

/**
 * Error code used in <code>ErrorMap</code> to keep track of errors in an
 * experiment. An error code is defined by an index, a type of error and a
 * descripton of the error.
 * 
 * @author ppinard
 */
@Root
@Immutable
public class ErrorCode {

    /** Id of the error code. */
    @Attribute(name = "id")
    public final int id;

    /** Type of the error code. */
    @Attribute(name = "type")
    public final String type;

    /** Description of the error code. */
    @Attribute(name = "description")
    public final String description;



    /**
     * Creates a new <code>ErrorCode</code>.
     * 
     * @param id
     *            id of the error code. It must be between 0 and 255 (byte).
     * @param type
     *            type of the error code
     * @param description
     *            description of the error code
     * @throws IllegalArgumentException
     *             if the id is outside the [0, 255] range.
     * @throws NullPointerException
     *             if the type or the description is null
     * @throws IllegalArgumentException
     *             if the type is empty
     */
    public ErrorCode(@Attribute(name = "id") int id,
            @Attribute(name = "type") String type,
            @Attribute(name = "description") String description) {
        if (id < 0 || id > 255)
            throw new IllegalArgumentException("Id (" + id
                    + ") must be between 0 and 255.");
        if (type == null)
            throw new NullPointerException("Error type cannot be null.");
        if (type.length() == 0)
            throw new IllegalArgumentException("Error type cannot be empty.");
        if (description == null)
            throw new NullPointerException("Error description cannot be null.");

        this.id = id;
        this.type = type;
        this.description = description;
    }



    /**
     * Creates a new <code>ErrorCode</code>. The description is empty.
     * 
     * @param id
     *            id of the error code. It must be between 0 and 255 (byte).
     * @param type
     *            type of the error code
     * @throws IllegalArgumentException
     *             if the id is outside the [0, 255] range.
     * @throws NullPointerException
     *             if the type or the description is null
     * @throws IllegalArgumentException
     *             if the type is empty
     */
    public ErrorCode(int id, String type) {
        this(id, type, "");
    }
}
