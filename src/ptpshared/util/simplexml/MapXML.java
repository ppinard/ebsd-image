package ptpshared.util.simplexml;

import java.util.Map;

import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.Root;

/**
 * Object used to serialize and deserialize a key-value map.
 * 
 * @author ppinard
 */
@Root(name = "map")
public class MapXML {

    /** Internal map of objects. */
    @ElementMap(entry = "item", key = "key", value = "value", inline = true)
    protected Map<?, ?> items;



    /**
     * Creates a new <code>MapXML</code> from a key-value map.
     * 
     * @param items
     *            a key-value map
     */
    public MapXML(
            @ElementMap(entry = "item", key = "key", value = "value", inline = true) Map<?, ?> items) {
        this.items = items;
    }

}
