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
package org.ebsdimage.vendors.hkl.core;

import java.io.File;
import java.util.HashMap;
import java.util.Map.Entry;

import org.ebsdimage.core.EbsdMMap;
import org.ebsdimage.core.EbsdMetadata;

import rmlimage.core.ByteMap;
import rmlimage.core.Map;
import rmlimage.module.real.core.RealMap;
import rmlshared.io.FileUtil;

/**
 * <code>EbsdMMap</code> holding all the data from an HLK acquisition.
 * <p/>
 * On top of the map defined in {@link EbsdMMap}, an <code>HklMMap</code> holds:
 * <ul>
 * <li>Band contrast</li>
 * <li>Band count</li>
 * <li>Band slope</li>
 * <li>Error number</li>
 * <li>Euler1</li>
 * <li>Euler2</li>
 * <li>Euler3</li>
 * <li>Mean angular deviation (MAD)</li>
 * </ul>
 * and the following metadata on top of the {@link EbsdMetadata}:
 * <ul>
 * <li>Project's name</li>
 * <li>Project's path</li>
 * </ul>
 * 
 * @author Philippe T. Pinard
 * @see EbsdMMap
 * @see EbsdMetadata
 * @see HklMetadata
 */
public class HklMMap extends EbsdMMap {

    /** Header for the ZIP file containing a hklMMap. */
    public static final String FILE_HEADER = EbsdMMap.FILE_HEADER + "-HKL";

    /** Version of the ZIP file. */
    public static final int VERSION = 2;

    /** Alias of the band contrast map. */
    public static final String BAND_CONTRAST = "BandContrast";

    /** Alias of the band count map. */
    public static final String BAND_COUNT = "BandCount";

    /** Alias of the band slope map. */
    public static final String BAND_SLOPE = "BandSlope";

    /** Alias for the mean angular deviation. */
    public static final String MEAN_ANGULAR_DEVIATION = "MeanAngularDeviation";



    /**
     * Creates a new <code>HklMMap</code> with the specified dimensions and
     * metadata. All the required maps are created but are empty.
     * 
     * @param width
     *            width of the map
     * @param height
     *            height of the map
     */
    public HklMMap(int width, int height) {
        this(width, height, new HashMap<String, Map>());
    }



    /**
     * Creates a new <code>HklMMap</code>.
     * 
     * @param width
     *            width of the map
     * @param height
     *            height of the map
     * @param mapList
     *            list of <code>Map</code> containing all the required maps
     */
    public HklMMap(int width, int height, HashMap<String, Map> mapList) {
        super(width, height, mapList); // Setup EbsdMMap and add base maps

        // Add missing HKL maps
        if (!contains(BAND_CONTRAST)) {
            ByteMap bandContrast = new ByteMap(width, height);
            bandContrast.clear();
            bandContrast.cloneMetadataFrom(this);
            add(BAND_CONTRAST, bandContrast);
        }

        if (!contains(BAND_COUNT)) {
            ByteMap bandCount = new ByteMap(width, height);
            bandCount.clear();
            bandCount.cloneMetadataFrom(this);
            add(BAND_COUNT, bandCount);
        }

        if (!contains(BAND_SLOPE)) {
            ByteMap bandSlope = new ByteMap(width, height);
            bandSlope.clear();
            bandSlope.cloneMetadataFrom(this);
            add(BAND_SLOPE, bandSlope);
        }

        if (!contains(MEAN_ANGULAR_DEVIATION)) {
            RealMap mad = new RealMap(width, height);
            mad.clear();
            mad.cloneMetadataFrom(this);
            add(MEAN_ANGULAR_DEVIATION, mad);
        }

        // Verify that all the needed Maps are present and have the right type
        if (!getMap(BAND_CONTRAST).getClass().equals(ByteMap.class))
            throw new IllegalArgumentException(
                    "Band contrast map must be a ByteMap.");

        if (!getMap(BAND_COUNT).getClass().equals(ByteMap.class))
            throw new IllegalArgumentException(
                    "Band count map must be a ByteMap.");

        if (!getMap(BAND_SLOPE).getClass().equals(ByteMap.class))
            throw new IllegalArgumentException(
                    "Band slope map must be a ByteMap.");

        if (!getMap(MEAN_ANGULAR_DEVIATION).getClass().equals(RealMap.class))
            throw new IllegalArgumentException(
                    "Mean angular deviation map must be a RealMap.");
    }



    @Override
    public HklMMap createMap(int width, int height) {
        return new HklMMap(width, height);
    }



    @Override
    public HklMMap duplicate() {
        HashMap<String, Map> mapList = new HashMap<String, Map>();

        for (Entry<String, Map> entry : getEntrySet())
            mapList.put(entry.getKey(), entry.getValue().duplicate());

        HklMMap dup = new HklMMap(width, height, mapList);

        dup.setMetadata(getMetadata());
        cloneMetadataFrom(this);

        return dup;
    }



    /**
     * Gets the band contrast. The data is taken from the <code>BC</code> column
     * of the ctf file. The data is returned as a greyscale <dfn>ByteMap</dfn>.
     * 
     * @return the band contrast
     */
    public ByteMap getBandContrastMap() {
        return (ByteMap) getMap(BAND_CONTRAST);
    }



    /**
     * Gets the numbers of bands detected. The data is taken from the
     * <code>Bands</code> column of the ctf file. The data is returned as a
     * greyscale <dfn>ByteMap</dfn>.
     * 
     * @return the number of bands detected
     */
    public ByteMap getBandCountMap() {
        return (ByteMap) getMap(BAND_COUNT);
    }



    /**
     * Gets the band slope. The data is taken from the <code>BS</code> column of
     * the ctf file. The data is returned as a greyscale <dfn>ByteMap</dfn>.
     * 
     * @return the band slope
     */
    public ByteMap getBandSlopeMap() {
        return (ByteMap) getMap(BAND_SLOPE);
    }



    /**
     * Gets the mean angular deviation. The data is taken from the
     * <code>MAD</code> column of the ctf file.
     * 
     * @return the mean angular deviation map
     */
    public RealMap getMeanAngularDeviationMap() {
        return (RealMap) getMap(MEAN_ANGULAR_DEVIATION);
    }



    @Override
    public HklMetadata getMetadata() {
        return (HklMetadata) super.getMetadata();
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
        File projectPath = getMetadata().getProjectDir();
        String projectName = getMetadata().getProjectName();

        File imageDir = new File(projectPath, projectName + "Images");

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

        String projectName = getMetadata().getProjectName();
        int nbDigits = Integer.toString(size).length();
        String suffix = rmlshared.math.Long.format(index + 1, nbDigits);
        File patternFile =
                FileUtil.append(new File(imageDir, projectName), suffix);
        patternFile = FileUtil.setExtension(patternFile, "jpg");

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
