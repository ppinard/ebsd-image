package ptpshared.util.simplexml;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root
public class Item {
    @Attribute(name = "index")
    public final int index;



    public Item(@Attribute(name = "index") int index) {
        this.index = index;
    }
}