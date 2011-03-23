package crystallography.io;

import java.io.File;
import java.io.IOException;

import org.ebsdimage.TestCase;
import org.junit.Before;
import org.junit.Test;

import rmlshared.io.FileUtil;
import crystallography.core.Crystal;
import crystallography.core.CrystalFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CrystalSaverTest extends TestCase {

    private CrystalSaver saver;

    private Crystal crystal;



    @Before
    public void setUp() throws Exception {
        saver = new CrystalSaver();
        crystal = CrystalFactory.silicon();
    }



    @Test
    public void testCanSave() {
        assertTrue(saver.canSave(crystal, "xml"));
        assertTrue(saver.canSave(crystal, "XML"));
        assertFalse(saver.canSave(crystal, "zip"));
        assertFalse(saver.canSave(null, "xml"));
    }



    private void testCrystal(File file) throws IOException {
        Crystal other = new CrystalLoader().load(file);
        assertEquals("Silicon", other.name);
    }



    @Test
    public void testSaveCrystalFile() throws IOException {
        File file = createTempFile();
        file = FileUtil.setExtension(file, "xml");

        saver.save(crystal, file);

        testCrystal(file);
    }



    @Test
    public void testSaveObjectFile() throws IOException {
        File file = createTempFile();
        file = FileUtil.setExtension(file, "xml");

        saver.save((Object) crystal, file);

        testCrystal(file);
    }

}
