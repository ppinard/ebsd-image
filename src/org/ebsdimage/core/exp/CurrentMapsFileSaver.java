/*
 * EBSD-Image
 * Copyright (C) 2010 Philippe T. Pinard
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
package org.ebsdimage.core.exp;

import java.io.IOException;
import java.util.logging.Logger;

import net.jcip.annotations.Immutable;

import org.ebsdimage.core.HoughMap;
import org.ebsdimage.core.Solution;
import org.ebsdimage.core.run.Operation;
import org.ebsdimage.io.HoughMapSaver;

import rmlimage.core.BinMap;
import rmlimage.core.ByteMap;
import rmlimage.core.Map;
import rmlimage.io.IO;

/**
 * Parameter to save maps through an <code>Exp</code>'s run.
 * 
 * @author Philippe T. Pinard
 */
@Immutable
public class CurrentMapsFileSaver extends CurrentMapsSaver {

    /** Logger. */
    private final Logger logger = Logger.getLogger("ebsd");

    /** If <code>true</code>, all possible maps are saved. */
    public final boolean saveAllMaps;

    /** Default value whether to save all maps. */
    public static final boolean DEFAULT_SAVEMAPS_ALL = false;

    /** If <code>true</code>, the pattern map is saved. */
    public final boolean savePatternMap;

    /** Default value whether to save the pattern map. */
    public static final boolean DEFAULT_SAVE_PATTERNMAP = false;

    /** If <code>true</code>, the Hough map is saved. */
    public final boolean saveHoughMap;

    /** Default value whether to save the Hough map. */
    public static final boolean DEFAULT_SAVE_HOUGHMAP = false;

    /** If <code>true</code>, the peaks map is saved. */
    public final boolean savePeaksMap;

    /** Default value whether to save the peak map. */
    public static final boolean DEFAULT_SAVE_PEAKSMAP = false;

    /** If <code>true</code>, the solution overlay map is saved. */
    public final boolean saveSolutionOverlay;

    /** Default value whether to save the peak map. */
    public static final boolean DEFAULT_SAVE_SOLUTIONOVERLAY = false;



    /**
     * Creates a new <code>CurrentMapsSaver</code> parameter using the default
     * values.
     */
    public CurrentMapsFileSaver() {
        saveAllMaps = DEFAULT_SAVEMAPS_ALL;
        savePatternMap = DEFAULT_SAVE_PATTERNMAP || DEFAULT_SAVEMAPS_ALL;
        saveHoughMap = DEFAULT_SAVE_HOUGHMAP || DEFAULT_SAVEMAPS_ALL;
        savePeaksMap = DEFAULT_SAVE_PEAKSMAP || DEFAULT_SAVEMAPS_ALL;
        saveSolutionOverlay =
                DEFAULT_SAVE_SOLUTIONOVERLAY || DEFAULT_SAVEMAPS_ALL;
    }



    /**
     * Creates a new <code>CurrentMapsSaver</code> from the specified values.
     * 
     * @param saveAllMaps
     *            if <code>true</code>, all possible maps are saved
     * @param savePatternMap
     *            if <code>true</code>, all pattern maps are saved
     * @param saveHoughMap
     *            if <code>true</code>, all Hough maps are saved
     * @param savePeaksMap
     *            if <code>true</code>, all peaks maps are saved
     * @param saveSolutionOverlay
     *            if <code>true</code> the solution overlay map is saved
     */
    public CurrentMapsFileSaver(boolean saveAllMaps, boolean savePatternMap,
            boolean saveHoughMap, boolean savePeaksMap,
            boolean saveSolutionOverlay) {
        this.saveAllMaps = saveAllMaps;
        this.savePatternMap = savePatternMap || saveAllMaps;
        this.saveHoughMap = saveHoughMap || saveAllMaps;
        this.savePeaksMap = savePeaksMap || saveAllMaps;
        this.saveSolutionOverlay = saveSolutionOverlay || saveAllMaps;
    }



    /**
     * Creates a name for a map from the experiment's name, a given name and
     * experiment's index.
     * 
     * @param exp
     *            an <code>Exp</code>
     * @param name
     *            a given name
     * @return name
     */
    private String createName(Exp exp, String name) {
        return exp.getName() + "_" + name + "_" + exp.getCurrentIndex();
    }



    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;

        CurrentMapsFileSaver other = (CurrentMapsFileSaver) obj;
        if (saveAllMaps != other.saveAllMaps)
            return false;
        if (saveHoughMap != other.saveHoughMap)
            return false;
        if (savePatternMap != other.savePatternMap)
            return false;
        if (savePeaksMap != other.savePeaksMap)
            return false;
        if (saveSolutionOverlay != other.saveSolutionOverlay)
            return false;

        return true;
    }



    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (saveAllMaps ? 1231 : 1237);
        result = prime * result + (saveHoughMap ? 1231 : 1237);
        result = prime * result + (savePatternMap ? 1231 : 1237);
        result = prime * result + (savePeaksMap ? 1231 : 1237);
        result = prime * result + (saveSolutionOverlay ? 1231 : 1237);
        return result;
    }



    /**
     * Save the Hough map if the <code>saveHoughMap</code> variable is
     * <code>true</code>. The map is saved in the experiment's path with the
     * name <em>hough.bmp</em>.
     * 
     * @param exp
     *            experiment executing this method
     * @param map
     *            Hough map
     */
    @Override
    public void saveHoughMap(Exp exp, HoughMap map) {
        if (saveHoughMap) {
            map.setDir(exp.getDir());
            map.setName(createName(exp, "hough"));

            try {
                new HoughMapSaver().save(map);
            } catch (IOException ex) {
                logger.warning("Hough map could not be saved because: "
                        + ex.getMessage());
            }

            logger.info("Hough map saved at " + map.getFile().getPath());
        }
    }



    /**
     * Save a map if the <code>saveAllMaps</code> variable is <code>true</code>.
     * The map is saved in the experiment's path. The name of the map is taken
     * from the name of the <code>Operation</code>.
     * 
     * @param exp
     *            experiment executing this method
     * @param op
     *            operation that creates the map
     * @param map
     *            a map
     */
    @Override
    public void saveMap(Exp exp, Operation op, Map map) {
        if (saveAllMaps) {
            map.setDir(exp.getDir());
            map.setName(createName(exp, op.getName()));

            try {
                IO.save(map);
            } catch (IOException ex) {
                logger.warning(op.getName() + " map could not be "
                        + "saved because: " + ex.getMessage());
            }

            logger.info(op.getName() + " saved at " + map.getFile().getPath());
        }
    }



    /**
     * Save the pattern map if the <code>savePatternMap</code> variable is
     * <code>true</code>. The map is saved in the experiment's path with the
     * name <em>pattern.bmp</em>.
     * 
     * @param exp
     *            experiment executing this method
     * @param map
     *            pattern map
     */
    @Override
    public void savePatternMap(Exp exp, ByteMap map) {
        if (savePatternMap) {
            map.setDir(exp.getDir());
            map.setName(createName(exp, "pattern"));

            try {
                rmlimage.io.IO.save(map);
            } catch (IOException ex) {
                logger.warning("Pattern map could not be saved because: "
                        + ex.getMessage());
            }

            logger.info("Pattern map saved at " + map.getFile().getPath());
        }
    }



    /**
     * Save the peaks map if the <code>savePeaksMap</code> variable is
     * <code>true</code>. The map is saved in the experiment's path with the
     * name <em>peaks.bmp</em>.
     * 
     * @param exp
     *            experiment executing this method
     * @param map
     *            peaks map
     */
    @Override
    public void savePeaksMap(Exp exp, BinMap map) {
        if (savePeaksMap) {
            map.setDir(exp.getDir());
            map.setName(createName(exp, "peaks"));

            try {
                rmlimage.io.IO.save(map);
            } catch (IOException ex) {
                logger.warning("Peaks map could not be saved because: "
                        + ex.getMessage());
            }

            logger.info("Peaks map saved at " + map.getFile().getPath());
        }
    }



    @Override
    public String toString() {
        return "CurrentMapsSaver [saveAllMaps=" + saveAllMaps
                + ", saveHoughMap=" + saveHoughMap + ", savePatternMap="
                + savePatternMap + ", savePeaksMap=" + savePeaksMap
                + ", saveSolutionOverlay=" + saveSolutionOverlay + "]";
    }



    @Override
    public void saveSolutionOverlay(Exp exp, Solution sln) {
        if (saveSolutionOverlay) {
            ByteMap map = createSolutionOverlay(exp, sln);

            map.setDir(exp.getDir());
            map.setName(createName(exp, "solution"));

            try {
                rmlimage.io.IO.save(map);
            } catch (IOException ex) {
                logger.warning("Solution map could not be saved because: "
                        + ex.getMessage());
            }

            logger.info("Solution map saved at " + map.getFile().getPath());
        }
    }

}
