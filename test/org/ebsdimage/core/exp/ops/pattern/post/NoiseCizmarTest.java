package org.ebsdimage.core.exp.ops.pattern.post;

import java.io.File;
import java.io.IOException;

import org.ebsdimage.TestCase;
import org.junit.Before;
import org.junit.Test;

import ptpshared.util.simplexml.XmlLoader;
import ptpshared.util.simplexml.XmlSaver;
import rmlimage.core.ByteMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import static junittools.test.Assert.assertEquals;

public class NoiseCizmarTest extends TestCase {

    private NoiseCizmar op;



    @Before
    public void setUp() throws Exception {
        op = new NoiseCizmar(10.0, 2.0, 2);
    }



    @Test
    public void testEqualsObjectDouble() {
        assertTrue(op.equals(op, 1e-2));
        assertFalse(op.equals(null, 1e-2));
        assertFalse(op.equals(new Object(), 1e-2));

        assertFalse(op.equals(new NoiseCizmar(10.1, 2.0, 2), 1e-2));
        assertFalse(op.equals(new NoiseCizmar(10.0, 2.1, 2), 1e-2));
        assertFalse(op.equals(new NoiseCizmar(10.0, 2.0, 3), 1e-2));
        assertTrue(op.equals(new NoiseCizmar(10.001, 2.001, 2), 1e-2));
    }



    @Test
    public void testNoiseCizmar() {
        assertEquals(10.0, op.gaussian, 1e-6);
        assertEquals(2.0, op.poisson, 1e-6);
    }



    @Test
    public void testProcess() throws IOException {
        ByteMap patternMap =
                (ByteMap) load("org/ebsdimage/testdata/pattern.bmp");
        ByteMap expected =
                (ByteMap) load("org/ebsdimage/testdata/noisecizmar.bmp");

        ByteMap dest = op.process(null, patternMap);

        expected.assertEquals(dest);
    }



    @Test
    public void testToString() {
        String expected = "Noise Cizmar [gaussian=10.0, poisson=2.0]";
        assertEquals(expected, op.toString());
    }



    @Test
    public void testXML() throws Exception {
        File file = createTempFile();
        new XmlSaver().save(op, file);

        NoiseCizmar other = new XmlLoader().load(NoiseCizmar.class, file);
        assertEquals(op, other, 1e-6);
    }

}
