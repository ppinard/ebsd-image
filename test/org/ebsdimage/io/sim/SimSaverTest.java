package org.ebsdimage.io.sim;

import java.io.File;

import org.ebsdimage.core.sim.Sim;
import org.ebsdimage.core.sim.SimTester;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import rmlshared.io.FileUtil;

public class SimSaverTest extends SimTester {

    private static File file = new File(FileUtil.getTempDir(), "sim.xml");



    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        Sim other = createSim();
        new SimSaver().save(other, file);
    }



    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        if (file.exists())
            if (!file.delete())
                throw new RuntimeException("Could not delete "
                        + file.getAbsolutePath());
    }



    @Before
    public void setUp() throws Exception {
        sim = new SimLoader().load(file);
    }

}
