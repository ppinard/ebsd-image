/*
 * EBSD-Image
 * Copyright (C) 2010-2011 Philippe T. Pinard
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.ebsdimage.vendors.tsl.core;

import java.io.File;
import java.util.HashMap;
import java.util.Map.Entry;

import org.ebsdimage.core.EbsdMMap;
import org.ebsdimage.core.EbsdMetadata;

import rmlimage.core.Map;
import rmlimage.module.real.core.RealMap;
import rmlshared.io.FileUtil;

/**
 * <code>EbsdMMap</code> holding all the data from a TSL acquisition.
 * <p/>
 * On top of the map defined in {@link EbsdMMap}, a <code>TslMMap</code> holds:
 * <ul>
 * <li>Euler1</li>
 * <li>Euler2</li>
 * <li>Euler3</li>
 * <li>Image quality</li>
 * <li>Confidence index</li>
 * </ul>
 * and the same metadata as {@link EbsdMetadata}.
 * 
 * @author Philippe T. Pinard
 * @see EbsdMMap
 * @see EbsdMetadata
 * @see TslMetadata
 */
public class TslMMap extends EbsdMMap {

    /** Header for the ZIP file containing a TslMMap. */
    public static final String FILE_HEADER = EbsdMMap.FILE_HEADER + "-TSL";

    /** Version of the ZIP file. */
    public static final int VERSION = 2;

    /** Alias of the map of the image quality. */
    public static final String IMAGE_QUALITY = "ImageQuality";

    /** Alias of the map of the confidence index. */
    public static final String CONFIDENCE_INDEX = "confidenceIndex";



    /**
     * Creates a new <code>TslMMap</code> with the specified dimensions and
     * metadata. All the required maps are created but are empty.
     * 
     * @param width
     *            width of the map
     * @param height
     *            height of the map
     */
    public TslMMap(int width, int height) {
        this(width, height, new HashMap<String, Map>());
    }



    /**
     * Creates a new <code>TslMMap</code>.
     * 
     * @param width
     *            width of the map
     * @param height
     *            height of the map
     * @param mapList
     *            list of <code>Map</code> containing all the required maps
     */
    public TslMMap(int width, int height, HashMap<String, Map> mapList) {
        super(width, height, mapList); // Setup EbsdMMap and add base maps

        // Add missing HKL maps
        if (!contains(IMAGE_QUALITY)) {
            RealMap imageQuality = new RealMap(width, height);
            imageQuality.clear();
            imageQuality.cloneMetadataFrom(this);
            add(IMAGE_QUALITY, imageQuality);
        }

        if (!contains(CONFIDENCE_INDEX)) {
            RealMap confidenceIndex = new RealMap(width, height);
            confidenceIndex.clear();
            confidenceIndex.cloneMetadataFrom(this);
            add(CONFIDENCE_INDEX, confidenceIndex);
        }

        // Verify that all the needed Maps are present and have the right type
        if (!getMap(IMAGE_QUALITY).getClass().equals(RealMap.class))
            throw new IllegalArgumentException(
                    "Image quality map must be a RealMap.");

        if (!getMap(CONFIDENCE_INDEX).getClass().equals(RealMap.class))
            throw new IllegalArgumentException(
                    "Confidence index map must be a RealMap.");
    }



    @Override
    public TslMMap createMap(int width, int height) {
        return new TslMMap(width, height);
    }



    @Override
    public TslMMap duplicate() {
        HashMap<String, Map> mapList = new HashMap<String, Map>();

        for (Entry<String, Map> entry : getEntrySet())
            mapList.put(entry.getKey(), entry.getValue().duplicate());

        TslMMap dup = new TslMMap(width, height, mapList);

        dup.setMetadata(getMetadata());
        cloneMetadataFrom(this);

        return dup;
    }



    /**
     * Returns the map for the confidence index.
     * 
     * @return confidence index map
     */
    public RealMap getConfidenceIndexMap() {
        return (RealMap) getMap(CONFIDENCE_INDEX);
    }



    /**
     * Returns the map for the image quality.
     * 
     * @return image quality map
     */
    public RealMap getImageQualityMap() {
        return (RealMap) getMap(IMAGE_QUALITY);
    }



    @Override
    public TslMetadata getMetadata() {
        return (TslMetadata) super.getMetadata();
    }



    /**
     * Returns the possible location of the pattern image for the specified
     * image. No check is performed to verify if the image exists. The location
     * is found from the project's path, project's name and index value.
     * 
     * @param index
     *            index of the pattern
     * @return possible location of the pattern image
     * @throws IllegalArgumentException
     *             if the index is out of range
     */
    public File getPatternFile(int index) {
        File imageDir = new File(FileUtil.getParentFile(getFile()), getName());

        return getPatternFile(index, imageDir);
    }



    /**
     * Returns the possible location of the pattern image for the specified
     * image. No check is performed to verify if the image exists. The location
     * is found from the image directory, project's name and index value.
     * 
     * @param index
     *            index of the pattern
     * @param imageDir
     *            alternate location of the image directory
     * @return possible location of the pattern image
     * @throws IllegalArgumentException
     *             if the index is out of range
     * @throws NullPointerException
     *             if the image directory is null
     */
    public File getPatternFile(int index, File imageDir) {
        if (index < 0 || index >= size)
            throw new IllegalArgumentException("Index (" + index
                    + ") must be between [0," + size + "[.");
        if (imageDir == null)
            throw new NullPointerException("Image dir cannot be null.");

        StringBuilder filename = new StringBuilder();
        filename.append(getName() + "_");
        filename.append("r" + getY(index));
        filename.append("c" + getX(index));

        File patternFile = new File(imageDir, filename.toString());
        patternFile = FileUtil.setExtension(patternFile, "bmp");

        return patternFile;
    }



    /**
     * Returns an array of all the pattern image files of the map. No check is
     * performed to verify if the pattern images exists.
     * 
     * @return an array of pattern image files
     * @see #getPatternFile(int)
     */
    public File[] getPatternFiles() {
        File[] files = new File[size];

        for (int i = 0; i < size; i++)
            files[i] = getPatternFile(i);

        return files;
    }



    /**
     * Returns an array of all the pattern image files of the map. No check is
     * performed to verify if the pattern images exists.
     * 
     * @return an array of pattern image files
     * @param imageDir
     *            alternate location of the image directory
     * @throws NullPointerException
     *             if the image directory is null
     * @see #getPatternFile(int, File)
     */
    public File[] getPatternFiles(File imageDir) {
        if (imageDir == null)
            throw new NullPointerException("Image directory cannot be null.");

        File[] files = new File[size];

        for (int i = 0; i < size; i++)
            files[i] = getPatternFile(i, imageDir);

        return files;
    }
}
