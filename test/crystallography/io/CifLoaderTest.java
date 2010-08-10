package crystallography.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import rmlshared.io.FileUtil;
import crystallography.core.AtomSite;
import crystallography.core.Crystal;
import crystallography.core.LaueGroup;

public class CifLoaderTest {

    private File file;



    @Before
    public void setUp() throws Exception {
        file = FileUtil.getFile("crystallography/testdata/forsterite.cif");

    }



    @Test
    public void testLoad() throws IOException {
        Crystal crystal = new CifLoader().load(file);

        assertEquals("Forsterite", crystal.name);

        assertEquals(4.756, crystal.unitCell.a, 1e-3);
        assertEquals(10.207, crystal.unitCell.b, 1e-3);
        assertEquals(5.980, crystal.unitCell.c, 1e-3);
        assertEquals(90, Math.toDegrees(crystal.unitCell.alpha), 1e-3);
        assertEquals(91, Math.toDegrees(crystal.unitCell.beta), 1e-3);
        assertEquals(92, Math.toDegrees(crystal.unitCell.gamma), 1e-3);

        assertEquals(41, crystal.atoms.size());

        HashMap<Integer, Integer> atoms = new HashMap<Integer, Integer>();
        for (AtomSite atom : crystal.atoms)
            if (atoms.containsKey(atom.atomicNumber))
                atoms.put(atom.atomicNumber, atoms.get(atom.atomicNumber) + 1);
            else
                atoms.put(atom.atomicNumber, 1);

        assertTrue(atoms.get(12).equals(9)); // Mg
        assertTrue(atoms.get(14).equals(8)); // Si
        assertTrue(atoms.get(8).equals(24)); // O

        assertEquals(LaueGroup.LGmmm, crystal.laueGroup);
    }

}
