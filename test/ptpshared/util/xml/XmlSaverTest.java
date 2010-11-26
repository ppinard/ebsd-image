package ptpshared.util.xml;

import java.io.File;
import java.io.IOException;

import org.ebsdimage.TestCase;
import org.junit.Before;
import org.junit.Test;

public class XmlSaverTest extends TestCase {

    private Item[] items;



    @Before
    public void setUp() throws Exception {
        items = new Item[] { new Item(2) };
    }



    @Test
    public void testSave() throws IOException {
        File file = new File("/tmp/items.xml");

        Class<?> E = items.getClass().getComponentType();
        System.out.println(E);

        new XmlSaver().saveArray(items, file);

        Item[] b = new XmlLoader().loadArray(Item.class, file);

        System.out.println(b.length);
        System.out.println(b[0].index);
    }

}
