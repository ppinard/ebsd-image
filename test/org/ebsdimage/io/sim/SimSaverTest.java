package org.ebsdimage.io.sim;

import java.io.File;
import java.io.IOException;

import org.ebsdimage.TestCase;
import org.ebsdimage.core.Camera;
import org.ebsdimage.core.run.Operation;
import org.ebsdimage.core.sim.Energy;
import org.ebsdimage.core.sim.Sim;
import org.junit.Before;
import org.junit.Test;

import ptpshared.core.math.Quaternion;
import ptpshared.util.xml.XmlLoader;
import ptpshared.util.xml.XmlSaver;
import crystallography.core.Crystal;
import crystallography.core.CrystalFactory;

public class SimSaverTest extends TestCase {

    private Sim sim;



    @Before
    public void setUp() throws Exception {
        Camera[] cameras = new Camera[] { new Camera(0.0, 0.0, 0.5) };
        Crystal[] crystals = new Crystal[] { CrystalFactory.silicon() };
        Energy[] energies = new Energy[] { new Energy(20e3) };
        Quaternion[] rotations = new Quaternion[] { Quaternion.IDENTITY };

        sim =
                new Sim(new Operation[] {}, cameras, crystals, energies,
                        rotations);
    }



    @Test
    public void testSaveSimFile() throws IOException {
        new XmlSaver().save(new Energy(20e3), new File("/tmp/energy.xml"));

        new XmlLoader().load(Energy.class, new File("/tmp/energy.xml"));

        new SimSaver().save(sim, new File("/tmp/sim.xml"));

        Sim other = new SimLoader().load(new File("/tmp/sim.xml"));

        System.out.println(other.getCameras().length);
    }

}
