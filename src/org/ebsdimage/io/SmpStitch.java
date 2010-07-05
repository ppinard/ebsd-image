package org.ebsdimage.io;

import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;

import net.jcip.annotations.Immutable;
import rmlimage.core.ByteMap;
import rmlimage.core.ROI;
import rmlshared.ui.Monitorable;

/**
 * Stitch smp files together.
 * 
 * @author ppinard
 */
@Immutable
public class SmpStitch implements Monitorable {

    /** Progress tracking variable. */
    private double progress;

    /** Regions of interest. */
    private final ROI[] rois;

    /** Smps. */
    private final SmpInputStream[] smps;

    /** Width of the final map. */
    public final int width;

    /** Height of the final map. */
    public final int height;



    /**
     * Creates a new <code>SmpMerge</code> to stitch the specified smp files
     * into one smp file according to the defined regions of interest.
     * 
     * @param maps
     *            <code>HashMap</code> where the keys are the located of the smp
     *            files and the values the regions of interest for each smp file
     * @param width
     *            width of the final stitch map
     * @param height
     *            height of the final stitch map
     * @throws IOException
     *             if an error occurs while loading the smp file
     */
    public SmpStitch(HashMap<File, ROI> maps, int width, int height)
            throws IOException {
        if (maps.size() == 0)
            throw new IllegalArgumentException(
                    "Specifigy at least one project.");
        if (width == 0)
            throw new IllegalArgumentException("Width cannot be zero.");
        if (height == 0)
            throw new IllegalArgumentException("Height cannot be zero.");

        this.width = width;
        this.height = height;

        rois = new ROI[maps.size()];
        smps = new SmpInputStream[maps.size()];

        int i = 0;
        for (Entry<File, ROI> entry : maps.entrySet()) {
            SmpInputStream smp = new SmpInputStream(entry.getKey());
            smps[i] = smp;

            rois[i] = entry.getValue();

            i++;
        }
    }



    /**
     * Closes all smp input streams.
     * 
     * @throws IOException
     *             if a stream cannot be close
     */
    public void close() throws IOException {
        for (SmpInputStream smp : smps)
            smp.close();
    }



    /**
     * Returns an empty pattern for pixels that don't correspond to any region
     * of interest.
     * 
     * @return <code>ByteMap</code> of an empty pattern
     */
    private ByteMap getEmptyPattern() {
        int width = smps[0].getMapWidth();
        int height = smps[0].getMapHeight();

        ByteMap map = new ByteMap(width, height);
        map.clear(0);

        return map;
    }



    /**
     * Returns the pattern at position x, y of the specified map.
     * 
     * @param x
     *            absolute x position of the desired pattern in the stitched map
     * @param y
     *            absolute y position of the desired pattern in the stitched map
     * @param mapIndex
     *            index of the map where to extract the pattern
     * @return <code>ByteMap</code> of the pattern
     * @throws IOException
     *             if an error occurs while loading the specified pattern
     */
    private ByteMap getPattern(int x, int y, int mapIndex) throws IOException {
        Point relPos = getRelativePosition(x, y, rois[mapIndex]);

        SmpInputStream smp = smps[mapIndex];

        ByteMap map = (ByteMap) smp.readMap(relPos.x, relPos.y);

        return map;
    }



    /**
     * Returns the index of map containing the coordinate x and y. A value of
     * <code>-1</code> is returned if the coordinate doesn't belong to any map.
     * Note that the first map containing x and y is returned.
     * 
     * @param x
     *            absolute x position of the desired pattern in the stitched map
     * @param y
     *            absolute y position of the desired pattern in the stitched map
     * @return index of map containing x and y, or -1 if no map is found
     */
    private int getMapIndex(int x, int y) {
        for (int i = 0; i < rois.length; i++)
            if (rois[i].isInside(x, y))
                return i;

        return -1;
    }



    /**
     * Returns the relation position of the absolute coordinate x and y with
     * respect to their region of interest.
     * 
     * @param x
     *            absolute x position of the desired pattern in the stitched map
     * @param y
     *            absolute y position of the desired pattern in the stitched map
     * @param roi
     *            region of interest which contains coordinates x and y
     * @return relative position of x and y with respect to the ROI
     * @throws IllegalArgumentException
     *             if the x and y are outside the region of interest
     */
    private Point getRelativePosition(int x, int y, ROI roi) {
        int newX = roi.x - x;
        int newY = roi.y - y;

        if (newX < 0)
            throw new IllegalArgumentException("Relative x position (" + newX
                    + ") is less than zero.");
        if (newY < 0)
            throw new IllegalArgumentException("Relative y position (" + newY
                    + ") is less than zero.");

        return new Point(newX, newY);
    }



    @Override
    public double getTaskProgress() {
        return progress;
    }



    @Override
    public String getTaskStatus() {
        return "";
    }



    /**
     * Stitches the smp files into one smp file.
     * 
     * @param outputFile
     *            output smp file
     * @throws IOException
     *             if an error occurs while loading or saving smp files
     */
    public void stitch(File outputFile) throws IOException {
        SmpOutputStream output = new SmpOutputStream(outputFile);

        int size = width * height;
        int count = 0;

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                // Update progress
                progress = (double) count / size;

                // Coordinates corresponds to which map
                int mapIndex = getMapIndex(x, y);

                // Get corresponding map
                ByteMap map;
                if (mapIndex < 0)
                    map = getEmptyPattern();
                else
                    map = getPattern(x, y, mapIndex);

                // Put map in output smp
                output.writeMap(map);

                count++;
            }
        }

        output.close();
    }
}