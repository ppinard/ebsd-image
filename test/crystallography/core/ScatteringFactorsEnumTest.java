package crystallography.core;

import java.io.File;

import org.ebsdimage.TestCase;
import org.junit.Before;
import org.junit.Test;

import ptpshared.util.simplexml.XmlLoader;
import ptpshared.util.simplexml.XmlSaver;

import static org.junit.Assert.assertEquals;

public class ScatteringFactorsEnumTest extends TestCase {

    ScatteringFactorsEnum scatterType;



    @Before
    public void setUp() throws Exception {
        scatterType = ScatteringFactorsEnum.XRAY;
    }



    @Test
    public void testGetScatteringFactors() {
        assertEquals(XrayScatteringFactors.class,
                scatterType.getScatteringFactors());
    }



    @Test
    public void testXML() throws Exception {
        File tmpFile = createTempFile();

        // Write
        new XmlSaver().save(scatterType, tmpFile);

        // Read
        ScatteringFactorsEnum other =
                new XmlLoader().load(ScatteringFactorsEnum.class, tmpFile);
        assertEquals(scatterType, other);
    }

}
