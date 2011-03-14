package org.ebsdimage.core.sim.ops.patternsim;

import java.awt.color.ColorSpace;
import java.awt.image.*;
import java.util.Hashtable;

public class BufferTest {

    public static void main(String[] args) {
        int destWidth = 4;
        int destHeight = 4;

        DataBufferDouble dbd = new DataBufferDouble(destWidth * destHeight);

        SampleModel sm =
                new PixelInterleavedSampleModel(DataBuffer.TYPE_DOUBLE,
                        destWidth, destHeight, 1, destWidth, new int[] { 0 });

        WritableRaster wr = WritableRaster.createWritableRaster(sm, dbd, null);

        ColorModel cm =
                new ComponentColorModel(
                        ColorSpace.getInstance(ColorSpace.CS_GRAY), true, true,
                        ColorModel.TRANSLUCENT, DataBuffer.TYPE_FLOAT);

        BufferedImage bi =
                new BufferedImage(cm, wr, true, new Hashtable<Object, Object>());

    }

}
