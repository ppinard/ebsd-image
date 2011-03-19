package org.ebsdimage.core.exp.ops.indexing.op;

import org.ebsdimage.core.HoughPeak;
import org.ebsdimage.core.Solution;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class IndexingOpNullTest {

    private IndexingOpNull op;



    @Before
    public void setUp() throws Exception {
        op = new IndexingOpNull();
    }



    @Test
    public void testIndex() {
        Solution[] slns = op.index(null, new HoughPeak[0]);
        assertEquals(0, slns.length);
    }

}
