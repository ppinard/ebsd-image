package org.ebsdimage.core.run;

import java.io.File;
import java.io.IOException;

import org.ebsdimage.TestCase;
import org.ebsdimage.core.exp.ops.pattern.post.PatternPostOps2Mock;
import org.ebsdimage.core.exp.ops.pattern.post.PatternPostOpsMock;
import org.ebsdimage.core.exp.ops.pattern.results.PatternResultsOpsMock;
import org.junit.Before;
import org.junit.Test;

import ptpshared.util.simplexml.XmlLoader;
import ptpshared.util.simplexml.XmlSaver;

import static org.junit.Assert.assertEquals;

public class OperationGeneratorTest extends TestCase {

    private OperationGenerator generator;



    @Before
    public void setUp() throws Exception {
        generator = new OperationGenerator();

        // Add operations
        generator.addItem(1, new PatternPostOps2Mock(1));
        generator.addItem(2, new PatternPostOpsMock());
        generator.addItem(3, new PatternPostOps2Mock(1));
        generator.addItem(1, new PatternPostOps2Mock(4));
        generator.addItem(1, new PatternPostOps2Mock(8));
        generator.addItem(1, new PatternResultsOpsMock());
    }



    @Test
    public void testAddItem() {
        generator.addItem(1, new PatternPostOps2Mock(12));
        assertEquals(4, generator.getCombinations().size());
    }



    @Test
    public void testClearItem() {
        generator.clearItem(1, new PatternPostOps2Mock(1));
        assertEquals(1, generator.getCombinations().size());
    }



    @Test
    public void testCreateItemKey() {
        String expected =
                "0005:org.ebsdimage.core.exp.ops.pattern.post.PatternPostOps2Mock";
        String actual = generator.createItemKey(5, new PatternPostOps2Mock(1));
        assertEquals(expected, actual);
    }



    @Test
    public void testGetCombinations() {
        assertEquals(3, generator.getCombinations().size());
    }



    @Test
    public void testGetCombinationsOperations() {
        assertEquals(3, generator.getCombinationsOperations().size());
    }



    @Test
    public void testGetItems() {
        assertEquals(4, generator.getItems().keySet().size());
    }



    @Test
    public void testGetKeys() {
        String[] keys = generator.getKeys();
        assertEquals(4, keys.length);
    }



    @Test
    public void testGetOrderFromKey() {
        String key =
                "0005:org.ebsdimage.core.exp.ops.pattern.post.PatternPostOps2Mock";
        assertEquals(5, OperationGenerator.getOrderFromKey(key));

    }



    @Test
    public void testOperationGenerator() {
        assertEquals(3, generator.getCombinations().size());

        PatternPostOps2Mock op;
        op =
                (PatternPostOps2Mock) generator.getCombinations().get(0).get(
                        "0001:org.ebsdimage.core.exp.ops.pattern.post.PatternPostOps2Mock");
        assertEquals(1, op.var);
        op =
                (PatternPostOps2Mock) generator.getCombinations().get(1).get(
                        "0001:org.ebsdimage.core.exp.ops.pattern.post.PatternPostOps2Mock");
        assertEquals(4, op.var);
        op =
                (PatternPostOps2Mock) generator.getCombinations().get(2).get(
                        "0001:org.ebsdimage.core.exp.ops.pattern.post.PatternPostOps2Mock");
        assertEquals(8, op.var);
    }



    @Test
    public void testXML() throws IOException {
        File tmpFile = createTempFile();

        // Write
        new XmlSaver().save(generator, tmpFile);

        // Load
        OperationGenerator other =
                new XmlLoader().load(OperationGenerator.class, tmpFile);

        assertEquals(3, other.getCombinations().size());

        PatternPostOps2Mock op;
        op =
                (PatternPostOps2Mock) other.getCombinations().get(0).get(
                        "0001:org.ebsdimage.core.exp.ops.pattern.post.PatternPostOps2Mock");
        assertEquals(1, op.var);
        op =
                (PatternPostOps2Mock) other.getCombinations().get(1).get(
                        "0001:org.ebsdimage.core.exp.ops.pattern.post.PatternPostOps2Mock");
        assertEquals(4, op.var);
        op =
                (PatternPostOps2Mock) other.getCombinations().get(2).get(
                        "0001:org.ebsdimage.core.exp.ops.pattern.post.PatternPostOps2Mock");
        assertEquals(8, op.var);
    }

}
