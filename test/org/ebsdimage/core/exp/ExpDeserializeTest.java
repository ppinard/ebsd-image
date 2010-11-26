package org.ebsdimage.core.exp;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.simpleframework.xml.core.Persister;

public class ExpDeserializeTest extends ExpTester {

    File tmpFile;



    @Before
    public void setUp() throws Exception {
        Exp tmpExp = createExp();

        // Write
        tmpFile = createTempFile();
        Persister serializer = new Persister();
        serializer.write(tmpExp, tmpFile);

        // Read
        exp = serializer.read(Exp.class, tmpFile);
    }



    @Override
    @After
    public void tearDown() throws Exception {
        super.tearDown();
        tmpFile.delete();
    }

}
