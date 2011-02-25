package ptpshared.util.simplexml;

import java.util.ArrayList;
import java.util.Arrays;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

/**
 * Object used to serialize and deserialize an array of Object.
 * 
 * @author ppinard
 */
@Root(name = "array")
public class ArrayXML {

    /** Internal list of objects. */
    @ElementList(inline = true)
    protected ArrayList<Object> list;



    /**
     * Creates a new <code>ArrayXML</code> from an array list of objects.
     * 
     * @param array
     *            objects
     */
    public ArrayXML(@ElementList(inline = true) ArrayList<Object> array) {
        list = new ArrayList<Object>();
        list.addAll(array);
    }



    /**
     * Creates a new <code>ArrayXML</code> from an array of objects.
     * 
     * @param array
     *            objects
     */
    public ArrayXML(Object[] array) {
        list = new ArrayList<Object>();
        list.addAll(Arrays.asList(array));
    }

}
