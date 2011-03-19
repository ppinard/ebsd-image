package crystallography.io;

import java.io.File;
import java.io.IOException;

import org.ebsdimage.TestCase;
import org.junit.Before;
import org.junit.Test;

import crystallography.core.Crystal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CrystalLoaderTest extends TestCase {

    private CrystalLoader loader;

    private File file;



    @Before
    public void setUp() throws Exception {
        loader = new CrystalLoader();
        file = getFile("crystallography/testdata/silicon.xml");
    }



    @Test
    public void testLoadFile() throws IOException {
        Crystal crystal = loader.load(file);
        assertEquals("Silicon", crystal.name);
    }



    @Test
    public void testLoadFileObject() throws IOException {
        Crystal crystal = loader.load(file, null);
        assertEquals("Silicon", crystal.name);
    }



    @Test
    public void testCanLoad() {
        assertTrue(loader.canLoad(file));
        assertFalse(loader.canLoad(getFile("crystallography/testdata/forsterite.cif")));
    }
}
