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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.ebsdimage.core.*;
import org.ebsdimage.core.exp.ops.detection.op.AutomaticTopHat;
import org.ebsdimage.core.exp.ops.detection.op.DetectionOp;
import org.ebsdimage.core.exp.ops.detection.post.DetectionPostOps;
import org.ebsdimage.core.exp.ops.detection.pre.DetectionPreOps;
import org.ebsdimage.core.exp.ops.detection.results.DetectionResultsOps;
import org.ebsdimage.core.exp.ops.hough.op.AutoHoughTransform;
import org.ebsdimage.core.exp.ops.hough.op.HoughOp;
import org.ebsdimage.core.exp.ops.hough.post.HoughPostOps;
import org.ebsdimage.core.exp.ops.hough.pre.HoughPreOps;
import org.ebsdimage.core.exp.ops.hough.results.HoughResultsOps;
import org.ebsdimage.core.exp.ops.identification.op.CenterOfMass;
import org.ebsdimage.core.exp.ops.identification.op.IdentificationOp;
import org.ebsdimage.core.exp.ops.identification.post.IdentificationPostOps;
import org.ebsdimage.core.exp.ops.identification.pre.IdentificationPreOps;
import org.ebsdimage.core.exp.ops.identification.results.IdentificationResultsOps;
import org.ebsdimage.core.exp.ops.indexing.op.IndexingOp;
import org.ebsdimage.core.exp.ops.indexing.post.IndexingPostOps;
import org.ebsdimage.core.exp.ops.indexing.pre.IndexingPreOps;
import org.ebsdimage.core.exp.ops.indexing.results.IndexingResultsOps;
import org.ebsdimage.core.exp.ops.pattern.op.PatternOp;
import org.ebsdimage.core.exp.ops.pattern.post.PatternPostOps;
import org.ebsdimage.core.exp.ops.pattern.results.PatternResultsOps;
import org.ebsdimage.core.run.Operation;
import org.ebsdimage.core.run.Run;
import org.simpleframework.xml.*;

import ptpshared.io.FileUtil;
import rmlimage.core.BinMap;
import rmlimage.core.ByteMap;
import rmlimage.core.Map;
import rmlimage.module.real.core.Calibration3D;
import rmlimage.module.real.core.RealMap;
import crystallography.core.Crystal;
import static org.ebsdimage.core.exp.ExpConstants.*;

/**
 * Experiment engine.
 * 
 * @author Philippe T. Pinard
 */
@Order(elements = { "listeners", "metadata", "phases", "patternOp",
        "patternPostOps", "patternResultsOps", "houghPreOps", "houghOp",
        "houghPostOps", "houghResultsOps", "detectionPreOps", "detectionOp",
        "detectionPostOps", "detectionResultsOps", "identificationPreOps",
        "identificationOp", "identificationPostOps",
        "identificationResultsOps", "indexingPreOps", "indexingOp",
        "indexingPostOps", "indexingResultsOps" }, attributes = { "name",
        "dir", "width", "height" })
public class Exp extends Run {

    /** Experiment listeners. */
    @ElementList(name = "listeners")
    private ArrayList<ExpListener> listeners = new ArrayList<ExpListener>();

    /** <code>MultiMap</code> holding the result and metadata for the experiment. */
    public final EbsdMMap mmap;

    /** Runtime variable for the source pattern map. */
    protected ByteMap sourcePatternMap;

    /** Runtime variable for the source Hough map (original Hough map). */
    protected HoughMap sourceHoughMap;

    /** Runtime variable for the source peaks map (original peaks map). */
    protected BinMap sourcePeaksMap;

    /** Runtime variable of the pattern map. */
    protected ByteMap currentPatternMap;

    /** Runtime variable of the Hough map. */
    protected HoughMap currentHoughMap;

    /** Runtime variable of the peaks map. */
    protected BinMap currentPeaksMap;

    /** Runtime variable of the Hough peaks. */
    protected HoughPeak[] currentPeaks;

    /** Runtime variable of the solutions. */
    protected Solution[] currentSolutions;

    /** Pattern operation of the experiment. */
    private PatternOp patternOp = null;

    /** List of pattern post operations of the experiment. */
    private ArrayList<PatternPostOps> patternPostOps =
            new ArrayList<PatternPostOps>();

    /** List of pattern results operations of the experiment. */
    private ArrayList<PatternResultsOps> patternResultsOps =
            new ArrayList<PatternResultsOps>();

    /** List of Hough pre operations of the experiment. */
    private ArrayList<HoughPreOps> houghPreOps = new ArrayList<HoughPreOps>();

    /** Default Hough operation. */
    public static final HoughOp DEFAULT_HOUGH_OP = AutoHoughTransform.DEFAULT;

    /** Hough operation. */
    private HoughOp houghOp = DEFAULT_HOUGH_OP;

    /** List of Hough post operations of the experiment. */
    private ArrayList<HoughPostOps> houghPostOps =
            new ArrayList<HoughPostOps>();

    /** List of Hough results operations of the experiment. */
    private ArrayList<HoughResultsOps> houghResultsOps =
            new ArrayList<HoughResultsOps>();

    /** List of detection pre operations of the experiment. */
    private ArrayList<DetectionPreOps> detectionPreOps =
            new ArrayList<DetectionPreOps>();

    /** Default detection operation. */
    public static final DetectionOp DEFAULT_DETECTION_OP =
            AutomaticTopHat.DEFAULT;

    /** Detection operation. */
    private DetectionOp detectionOp = DEFAULT_DETECTION_OP;

    /** List of detection post operations of the experiment. */
    private ArrayList<DetectionPostOps> detectionPostOps =
            new ArrayList<DetectionPostOps>();

    /** List of detection results operations of the experiment. */
    private ArrayList<DetectionResultsOps> detectionResultsOps =
            new ArrayList<DetectionResultsOps>();

    /** List of identification pre operations of the experiment. */
    private ArrayList<IdentificationPreOps> identificationPreOps =
            new ArrayList<IdentificationPreOps>();

    /** Default identification operation. */
    public static final IdentificationOp DEFAULT_IDENTIFICATION_OP =
            CenterOfMass.DEFAULT;

    /** Identification operation. */
    private IdentificationOp identificationOp = DEFAULT_IDENTIFICATION_OP;

    /** List of identification post operations of the experiment. */
    private ArrayList<IdentificationPostOps> identificationPostOps =
            new ArrayList<IdentificationPostOps>();

    /** List of identification results operations of the experiment. */
    private ArrayList<IdentificationResultsOps> identificationResultsOps =
            new ArrayList<IdentificationResultsOps>();

    /** List of indexing pre operations of the experiment. */
    private ArrayList<IndexingPreOps> indexingPreOps =
            new ArrayList<IndexingPreOps>();

    /** Default indexing operation. */
    public static final IndexingOp DEFAULT_INDEXING_OP = null;

    /** Indexing operation. */
    private IndexingOp indexingOp = DEFAULT_INDEXING_OP;

    /** List of indexing post operations of the experiment. */
    private ArrayList<IndexingPostOps> indexingPostOps =
            new ArrayList<IndexingPostOps>();

    /** List of indexing results operations of the experiment. */
    private ArrayList<IndexingResultsOps> indexingResultsOps =
            new ArrayList<IndexingResultsOps>();



    /**
     * Creates a new experiment. The experiment is constructed from an
     * <code>EbsdMMap</code>, an array of <code>Operation</code> and a
     * <code>SaveMaps</code> object.
     * <p/>
     * The <code>EbsdMMap</code> contains the metadata for the experiment as
     * well as is used to store the results calculated during the execution of
     * the experiment. If the <code>EbsdMMap</code> already contains results
     * maps, they are not erased before running the experiment. Therefore, the
     * new results may only overwrite some results maps depending on the
     * selected operations.
     * <p/>
     * The array of <code>Operation</code> contains all the operations that the
     * experiment will execute. The <code>Operation</code>s are organised into
     * each step of the experiment (pattern, Hough, detection, identification,
     * indexing and output). The order of the <code>Operation</code> is
     * maintained.
     * <p/>
     * The <code>SaveMaps</code> object is used to save the output map after
     * each operation. To turn off this feature, use the empty constructor of
     * <code>SaveMaps</code> (
     * {@link CurrentMapsFileSaver#CurrentMapsFileSaver()}).
     * 
     * @param mmap
     *            <code>EbsdMMap</code> contains the metadata and maps for the
     *            experiment
     * @param ops
     *            operations for the experiment
     * @throws NullPointerException
     *             if the <code>EbsdMMap</code> is null
     * @throws NullPointerException
     *             if the array of <code>Operation</code> is null
     * @throws NullPointerException
     *             if an <code>Operation</code> is null
     * @see EbsdMMap
     * @see CurrentMapsFileSaver
     */
    public Exp(EbsdMMap mmap, Operation[] ops) {
        if (mmap == null)
            throw new NullPointerException("EBSD multimap cannot be null.");
        if (ops == null)
            throw new NullPointerException(
                    "The operations array cannot be null.");

        // EbsdMMap
        this.mmap = mmap;

        // Operations
        for (Operation op : ops)
            addOperation(op);

        if (patternOp == null)
            throw new IllegalArgumentException(
                    "No pattern operation was initialized.");

        // Initialize runtime variables
        initRuntimeVariables();
    }



    /**
     * Special constructor to load an experiment from a XML. This constructor is
     * used by the deserialization.
     * 
     * @param width
     *            width of the mapping
     * @param height
     *            height of the mapping
     * @param metadata
     *            metadata
     * @param phases
     *            phases
     * @param listeners
     *            array of experiment listeners
     * @param patternOp
     *            pattern operation
     * @param patternPostOps
     *            pattern post operation(s)
     * @param patternResultsOps
     *            pattern results operation(s)
     * @param houghPreOps
     *            Hough pre operation(s)
     * @param houghOp
     *            Hough operation
     * @param houghPostOps
     *            Hough post operation(s)
     * @param houghResultsOps
     *            Hough results operation(s)
     * @param detectionPreOps
     *            detection pre operation(s)
     * @param detectionOp
     *            detection operation
     * @param detectionPostOps
     *            detection post operation(s)
     * @param detectionResultsOps
     *            detection results operation(s)
     * @param identificationPreOps
     *            identification pre operation(s)
     * @param identificationOp
     *            identification operation
     * @param identificationPostOps
     *            identification post operation(s)
     * @param identificationResultsOps
     *            identification results operation(s)
     * @param indexingPreOps
     *            indexing pre operation(s)
     * @param indexingOp
     *            indexing operation
     * @param indexingPostOps
     *            indexing post operation(s)
     * @param indexingResultsOps
     *            indexing results operation(s)
     */
    @SuppressWarnings("unused")
    private Exp(
            @Attribute(name = "width") int width,
            @Attribute(name = "height") int height,
            @Element(name = "metadata") EbsdMetadata metadata,
            @ElementMap(name = "phases") java.util.Map<Integer, Crystal> phases,
            @ElementList(name = "listeners") ArrayList<ExpListener> listeners,
            @Element(name = "patternOp") PatternOp patternOp,
            @ElementArray(name = "patternPostOps") PatternPostOps[] patternPostOps,
            @ElementArray(name = "patternResultsOps") PatternResultsOps[] patternResultsOps,
            @ElementArray(name = "houghPreOps") HoughPreOps[] houghPreOps,
            @Element(name = "houghOp") HoughOp houghOp,
            @ElementArray(name = "houghPostOps") HoughPostOps[] houghPostOps,
            @ElementArray(name = "houghResultsOps") HoughResultsOps[] houghResultsOps,
            @ElementArray(name = "detectionPreOps") DetectionPreOps[] detectionPreOps,
            @Element(name = "detectionOp") DetectionOp detectionOp,
            @ElementArray(name = "detectionPostOps") DetectionPostOps[] detectionPostOps,
            @ElementArray(name = "detectionResultsOps") DetectionResultsOps[] detectionResultsOps,
            @ElementArray(name = "identificationPreOps") IdentificationPreOps[] identificationPreOps,
            @Element(name = "identificationOp") IdentificationOp identificationOp,
            @ElementArray(name = "identificationPostOps") IdentificationPostOps[] identificationPostOps,
            @ElementArray(name = "identificationResultsOps") IdentificationResultsOps[] identificationResultsOps,
            @ElementArray(name = "indexingPreOps") IndexingPreOps[] indexingPreOps,
            @Element(name = "indexingOp") IndexingOp indexingOp,
            @ElementArray(name = "indexingPostOps") IndexingPostOps[] indexingPostOps,
            @ElementArray(name = "indexingResultsOps") IndexingResultsOps[] indexingResultsOps) {
        mmap = new ExpMMap(width, height);
        mmap.setMetadata(metadata);

        PhaseMap phaseMap = new PhaseMap(width, height, phases);
        mmap.put(EbsdMMap.PHASES, phaseMap);

        // Listeners
        for (ExpListener listener : listeners)
            addExpListener(listener);

        // Operations
        addOperation(patternOp);
        addOperations(patternPostOps);
        addOperations(patternResultsOps);

        addOperations(houghPreOps);
        addOperation(houghOp);
        addOperations(houghPostOps);
        addOperations(houghResultsOps);

        addOperations(detectionPreOps);
        addOperation(detectionOp);
        addOperations(detectionPostOps);
        addOperations(detectionResultsOps);

        addOperations(identificationPreOps);
        addOperation(identificationOp);
        addOperations(identificationPostOps);
        addOperations(identificationResultsOps);

        addOperations(indexingPreOps);
        addOperation(indexingOp);
        addOperations(indexingPostOps);
        addOperations(indexingResultsOps);
    }



    /**
     * Registers an experiment listener.
     * 
     * @param listener
     *            experiment listener
     * @throws NullPointerException
     *             if the listener is null
     */
    public void addExpListener(ExpListener listener) {
        if (listener == null)
            throw new NullPointerException("Listener cannot be null.");

        if (!listeners.contains(listener))
            listeners.add(listener);
    }



    /**
     * Registers an array of experiment listeners.
     * 
     * @param listeners
     *            experiments listeners
     * @throws NullPointerException
     *             if the listeners is null
     */
    public void addExpListeners(ExpListener[] listeners) {
        if (listeners == null)
            throw new NullPointerException("Listeners cannot be null.");

        for (ExpListener listener : listeners)
            addExpListener(listener);
    }



    /**
     * Adds an <code>Operation</code> to the experiment. The type of
     * <code>Operation</code> is automatically deduced from its super class.
     * 
     * @param op
     *            operation to be added.
     * @throws IllegalArgumentException
     *             if the operation doesn't have a known super class
     * @throws NullPointerException
     *             if the operation is null
     */
    @Override
    protected void addOperation(Operation op) {
        if (op == null)
            throw new NullPointerException("Operation cannot be null.");

        String packageName =
                op.getClass().getSuperclass().getPackage().getName();

        // Pattern
        if (packageName.startsWith(PATTERN_CORE_PACKAGE)) {
            if (packageName.equals(FileUtil.joinPackageNames(
                    PATTERN_CORE_PACKAGE, "op")))
                patternOp = (PatternOp) op;
            else if (packageName.equals(FileUtil.joinPackageNames(
                    PATTERN_CORE_PACKAGE, ".post")))
                patternPostOps.add((PatternPostOps) op);
            else if (packageName.equals(FileUtil.joinPackageNames(
                    PATTERN_CORE_PACKAGE, ".results")))
                patternResultsOps.add((PatternResultsOps) op);
        }

        // Hough
        else if (packageName.startsWith(HOUGH_CORE_PACKAGE)) {
            if (packageName.equals(FileUtil.joinPackageNames(
                    HOUGH_CORE_PACKAGE, "pre")))
                houghPreOps.add((HoughPreOps) op);
            else if (packageName.equals(FileUtil.joinPackageNames(
                    HOUGH_CORE_PACKAGE, "op")))
                houghOp = (HoughOp) op;
            else if (packageName.equals(FileUtil.joinPackageNames(
                    HOUGH_CORE_PACKAGE, "post")))
                houghPostOps.add((HoughPostOps) op);
            else if (packageName.equals(FileUtil.joinPackageNames(
                    HOUGH_CORE_PACKAGE, "results")))
                houghResultsOps.add((HoughResultsOps) op);
        }

        // Detection
        else if (packageName.startsWith(DETECTION_CORE_PACKAGE)) {
            if (packageName.equals(FileUtil.joinPackageNames(
                    DETECTION_CORE_PACKAGE, "pre")))
                detectionPreOps.add((DetectionPreOps) op);
            else if (packageName.equals(FileUtil.joinPackageNames(
                    DETECTION_CORE_PACKAGE, "op")))
                detectionOp = (DetectionOp) op;
            else if (packageName.equals(FileUtil.joinPackageNames(
                    DETECTION_CORE_PACKAGE, "post")))
                detectionPostOps.add((DetectionPostOps) op);
            else if (packageName.equals(FileUtil.joinPackageNames(
                    DETECTION_CORE_PACKAGE, "results")))
                detectionResultsOps.add((DetectionResultsOps) op);
        }

        // Identification
        else if (packageName.startsWith(IDENTIFICATION_CORE_PACKAGE)) {
            if (packageName.equals(FileUtil.joinPackageNames(
                    IDENTIFICATION_CORE_PACKAGE, "pre")))
                identificationPreOps.add((IdentificationPreOps) op);
            else if (packageName.equals(FileUtil.joinPackageNames(
                    IDENTIFICATION_CORE_PACKAGE, "op")))
                identificationOp = (IdentificationOp) op;
            else if (packageName.equals(FileUtil.joinPackageNames(
                    IDENTIFICATION_CORE_PACKAGE, "post")))
                identificationPostOps.add((IdentificationPostOps) op);
            else if (packageName.equals(FileUtil.joinPackageNames(
                    IDENTIFICATION_CORE_PACKAGE, "results")))
                identificationResultsOps.add((IdentificationResultsOps) op);
        }

        // Indexing
        else if (packageName.startsWith(INDEXING_CORE_PACKAGE)) {
            if (packageName.equals(FileUtil.joinPackageNames(
                    INDEXING_CORE_PACKAGE, "pre")))
                indexingPreOps.add((IndexingPreOps) op);
            else if (packageName.equals(FileUtil.joinPackageNames(
                    INDEXING_CORE_PACKAGE, "op")))
                indexingOp = (IndexingOp) op;
            else if (packageName.equals(FileUtil.joinPackageNames(
                    INDEXING_CORE_PACKAGE, "post")))
                indexingPostOps.add((IndexingPostOps) op);
            else if (packageName.equals(FileUtil.joinPackageNames(
                    INDEXING_CORE_PACKAGE, "results")))
                indexingResultsOps.add((IndexingResultsOps) op);
        }

        // Else
        else
            throw new IllegalArgumentException("The operation (" + op.getName()
                    + ") is unknown and cannot be loaded.");
    }



    /**
     * Adds a list of operations. The method {@link #addOperation(ExpOperation)}
     * is called for each operation.
     * 
     * @param ops
     *            operations
     */
    protected void addOperations(Operation[] ops) {
        for (Operation op : ops)
            addOperation(op);
    }



    /**
     * Clears all registered experiment listeners.
     */
    public void clearExpListners() {
        listeners.clear();
    }



    /**
     * Creates a new map of the specified type.
     * 
     * @param result
     *            result obtained from an operation
     * @return a new map
     * @throws IllegalArgumentException
     *             if the result's map type is unknown
     */
    private Map createMap(OpResult result) {
        // RealMap
        if (result.type.equals(RealMap.class)) {
            RealMap map = new RealMap(mmap.width, mmap.height);
            map.clear(Float.NaN);
            map.setCalibration(new Calibration3D(mmap.getCalibration(), 0, 1,
                    result.units, false));
            return map;

            // ByteMap
        } else if (result.type.equals(ByteMap.class)) {
            ByteMap map = new ByteMap(mmap.width, mmap.height);
            map.clear(0);
            map.setCalibration(mmap);
            return map;

            // PhaseMap
        } else if (result.type.equals(PhaseMap.class)) {
            PhaseMap map = new PhaseMap(mmap.width, mmap.height);
            map.clear(0);
            map.setCalibration(mmap);
            return map;

            // BinMap
        } else if (result.type.equals(BinMap.class)) {
            BinMap map = new BinMap(mmap.width, mmap.height);
            map.clear(false);
            map.setCalibration(mmap);
            return map;
        } else
            throw new IllegalArgumentException("Unknown type of map ("
                    + result.type.toString() + ").");
    }



    /**
     * Returns a list of all the defined operations in this experiment.
     * 
     * @return operations
     */
    public ArrayList<ExpOperation> getAllOperations() {
        ArrayList<ExpOperation> ops = new ArrayList<ExpOperation>();

        ops.add(patternOp);
        ops.addAll(patternPostOps);
        ops.addAll(patternResultsOps);

        ops.addAll(houghPreOps);
        ops.add(houghOp);
        ops.addAll(houghPostOps);
        ops.addAll(houghResultsOps);

        ops.addAll(detectionPreOps);
        ops.add(detectionOp);
        ops.addAll(detectionPostOps);
        ops.addAll(detectionResultsOps);

        ops.addAll(identificationPreOps);
        ops.add(identificationOp);
        ops.addAll(identificationPostOps);
        ops.addAll(identificationResultsOps);

        ops.addAll(indexingPreOps);
        ops.add(indexingOp);
        ops.addAll(indexingPostOps);
        ops.addAll(indexingResultsOps);

        return ops;
    }



    /**
     * Returns the Hough map that is currently being used by the experiment.
     * Only valid when the experiment is running.
     * 
     * @return Hough map
     */
    public HoughMap getCurrentHoughMap() {
        if (currentHoughMap == null)
            throw new RuntimeException(
                    "The experiment is not running, there is no Hough map.");
        return currentHoughMap.duplicate();
    }



    /**
     * Returns the pattern map that is currently being used by the experiment.
     * Only valid when the experiment is running.
     * 
     * @return pattern map
     */
    public ByteMap getCurrentPatternMap() {
        if (currentPatternMap == null)
            throw new RuntimeException(
                    "The experiment is not running, there is no pattern map.");
        return currentPatternMap.duplicate();
    }



    /**
     * Returns the Hough peaks that are currently being used by the experiment.
     * Only valid when the experiment is running.
     * 
     * @return Hough map
     */
    public HoughPeak[] getCurrentPeaks() {
        if (currentPeaks == null)
            throw new RuntimeException(
                    "The experiment is not running, there is no peaks.");
        return currentPeaks.clone();
    }



    /**
     * Returns the peaks map that is currently being used by the experiment.
     * Only valid when the experiment is running.
     * 
     * @return Hough map
     */
    public BinMap getCurrentPeaksMap() {
        if (currentPeaksMap == null)
            throw new RuntimeException(
                    "The experiment is not running, there is no peaks map.");
        return currentPeaksMap.duplicate();
    }



    /**
     * Returns the solutions that are currently being used by the experiment.
     * Only valid when the experiment is running.
     * 
     * @return Hough map
     */
    public Solution[] getCurrentSolutions() {
        if (currentSolutions == null)
            throw new RuntimeException(
                    "The experiment is not running, there is no solution.");
        return currentSolutions.clone();
    }



    /**
     * Returns the detection operation of this experiment.
     * 
     * @return detection operation
     */
    @Element(name = "detectionOp")
    public DetectionOp getDetectionOp() {
        return detectionOp;
    }



    /**
     * Returns the detection post operations of this experiment.
     * 
     * @return detection post operations
     */
    @ElementArray(name = "detectionPostOps")
    public DetectionPostOps[] getDetectionPostOps() {
        return detectionPostOps.toArray(new DetectionPostOps[detectionPostOps.size()]);
    }



    /**
     * Returns the detection pre operations of this experiment.
     * 
     * @return detection pre operations
     */
    @ElementArray(name = "detectionPreOps")
    public DetectionPreOps[] getDetectionPreOps() {
        return detectionPreOps.toArray(new DetectionPreOps[detectionPreOps.size()]);
    }



    /**
     * Returns the detection results operations of this experiment.
     * 
     * @return detection results operations
     */
    @ElementArray(name = "detectionResultsOps")
    public DetectionResultsOps[] getDetectionResultsOps() {
        return detectionResultsOps.toArray(new DetectionResultsOps[detectionResultsOps.size()]);
    }



    /**
     * Returns the registered experiment listeners.
     * 
     * @return experiment listeners
     */
    public ExpListener[] getExpListeners() {
        return listeners.toArray(new ExpListener[0]);
    }



    /**
     * Returns the height of the mapping. This corresponds to the height of the
     * multimap.
     * 
     * @return height
     */
    @Attribute(name = "height")
    public int getHeight() {
        return mmap.height;
    }



    /**
     * Returns the Hough operation of this experiment.
     * 
     * @return Hough operation
     */
    @Element(name = "houghOp")
    public HoughOp getHoughOp() {
        return houghOp;
    }



    /**
     * Returns the Hough post operations of this experiment.
     * 
     * @return Hough post operations
     */
    @ElementArray(name = "houghPostOps")
    public HoughPostOps[] getHoughPostOps() {
        return houghPostOps.toArray(new HoughPostOps[houghPostOps.size()]);
    }



    /**
     * Returns the Hough pre operations of this experiment.
     * 
     * @return Hough pre operations
     */
    @ElementArray(name = "houghPreOps")
    public HoughPreOps[] getHoughPreOps() {
        return houghPreOps.toArray(new HoughPreOps[houghPreOps.size()]);
    }



    /**
     * Returns the Hough results operations of this experiment.
     * 
     * @return Hough results operations
     */
    @ElementArray(name = "houghResultsOps")
    public HoughResultsOps[] getHoughResultsOps() {
        return houghResultsOps.toArray(new HoughResultsOps[houghResultsOps.size()]);
    }



    /**
     * Returns the identification operation of this experiment.
     * 
     * @return identification operation
     */
    @Element(name = "identificationOp")
    public IdentificationOp getIdentificationOp() {
        return identificationOp;
    }



    /**
     * Returns the identification post operations of this experiment.
     * 
     * @return identification post operations
     */
    @ElementArray(name = "identificationPostOps")
    public IdentificationPostOps[] getIdentificationPostOps() {
        return identificationPostOps.toArray(new IdentificationPostOps[identificationPostOps.size()]);
    }



    /**
     * Returns the identification pre operations of this experiment.
     * 
     * @return identification pre operations
     */
    @ElementArray(name = "identificationPreOps")
    public IdentificationPreOps[] getIdentificationPreOps() {
        return identificationPreOps.toArray(new IdentificationPreOps[identificationPreOps.size()]);
    }



    /**
     * Returns the identification results operations of this experiment.
     * 
     * @return identification results operations
     */
    @ElementArray(name = "identificationResultsOps")
    public IdentificationResultsOps[] getIdentificationResultsOps() {
        return identificationResultsOps.toArray(new IdentificationResultsOps[identificationResultsOps.size()]);
    }



    /**
     * Returns the indexing operation of this experiment.
     * 
     * @return indexing operation
     */
    @Element(name = "indexingOp")
    public IndexingOp getIndexingOp() {
        return indexingOp;
    }



    /**
     * Returns the indexing post operations of this experiment.
     * 
     * @return indexing post operations
     */
    @ElementArray(name = "indexingPostOps")
    public IndexingPostOps[] getIndexingPostOps() {
        return indexingPostOps.toArray(new IndexingPostOps[indexingPostOps.size()]);
    }



    /**
     * Returns the indexing pre operations of this experiment.
     * 
     * @return indexing pre operations
     */
    @ElementArray(name = "indexingPreOps")
    public IndexingPreOps[] getIndexingPreOps() {
        return indexingPreOps.toArray(new IndexingPreOps[indexingPreOps.size()]);
    }



    /**
     * Returns the indexing results operations of this experiment.
     * 
     * @return indexing results operations
     */
    @ElementArray(name = "indexingResultsOps")
    public IndexingResultsOps[] getIndexingResultsOps() {
        return indexingResultsOps.toArray(new IndexingResultsOps[indexingResultsOps.size()]);
    }



    /**
     * Returns the metadata of the multimap. The metadata contains the
     * acquisition parameters.
     * 
     * @return metadata
     */
    @Element(name = "metadata")
    public EbsdMetadata getMetadata() {
        return mmap.getMetadata();
    }



    /**
     * Returns the pattern operation of this experiment.
     * 
     * @return pattern operation
     */
    @Element(name = "patternOp")
    public PatternOp getPatternOp() {
        return patternOp;
    }



    /**
     * Returns the pattern post operations of this experiment.
     * 
     * @return pattern post operations
     */
    @ElementArray(name = "patternPostOps")
    public PatternPostOps[] getPatternPostOps() {
        return patternPostOps.toArray(new PatternPostOps[patternPostOps.size()]);
    }



    /**
     * Returns the pattern results operations of this experiment.
     * 
     * @return pattern results operations
     */
    @ElementArray(name = "patternResultsOps")
    public PatternResultsOps[] getPatternResultsOps() {
        return patternResultsOps.toArray(new PatternResultsOps[patternResultsOps.size()]);
    }



    /**
     * Returns the phases defined in the multimap. A copy of the phases is
     * returned.
     * 
     * @return phases
     */
    @ElementMap(name = "phases")
    public java.util.Map<Integer, Crystal> getPhases() {
        return mmap.getPhaseMap().getItems();
    }



    /**
     * Returns the source Hough map that is currently being used by the
     * experiment. The source Hough map corresponds to the Hough map write after
     * the Hough operation. Only valid when the experiment is running.
     * 
     * @return Hough map
     */
    public HoughMap getSourceHoughMap() {
        if (sourceHoughMap == null)
            throw new RuntimeException(
                    "The experiment is not running, there is no Hough map.");
        return sourceHoughMap.duplicate();
    }



    /**
     * Returns the source pattern map that is currently being used by the
     * experiment. Only valid when the experiment is running.
     * 
     * @return pattern map
     */
    public ByteMap getSourcePatternMap() {
        if (sourcePatternMap == null)
            throw new RuntimeException(
                    "The experiment is not running, there is no pattern map.");
        return sourcePatternMap.duplicate();
    }



    /**
     * Returns the source peaks map that is currently being used by the
     * experiment. Only valid when the experiment is running.
     * 
     * @return pattern map
     */
    public BinMap getSourcePeaksMap() {
        if (sourcePeaksMap == null)
            throw new RuntimeException(
                    "The experiment is not running, there is no peaks map.");
        return sourcePeaksMap.duplicate();
    }



    @Override
    public double getTaskProgress() {
        // Quick fix to notify the listeners of the EbsdMMap. This allows the
        // map to be refresh when displayed in the GUI.
        mmap.notifyListeners();

        return super.getTaskProgress();
    }



    /**
     * Returns the width of the mapping. This corresponds to the width of the
     * multimap.
     * 
     * @return width
     */
    @Attribute(name = "width")
    public int getWidth() {
        return mmap.width;
    }



    /**
     * Checks whether the size of the current map has changed. If so, the source
     * map is updated to be a duplicate of the current map.
     * 
     * @param source
     *            original map obtained after a "op" operation
     * @param current
     *            current map
     * @return new source map or original one
     */
    private Map updateSourceMap(Map source, Map current) {
        if (source == null)
            source = current.duplicate();

        if (!source.isSameSize(current))
            source = current.duplicate();

        return source;
    }



    @Override
    protected void initRuntimeVariables() {
        super.initRuntimeVariables();

        sourcePatternMap = null;
        sourceHoughMap = null;
        sourcePeaksMap = null;
        currentPatternMap = null;
        currentHoughMap = null;
        currentPeaksMap = null;
        currentPeaks = null;
        currentSolutions = null;
    }



    /**
     * Removes a registered listener from the experiment.
     * 
     * @param listener
     *            experiment listener
     * @throws NullPointerException
     *             if the listener is null
     */
    public void removeExpListerner(ExpListener listener) {
        if (listener == null)
            throw new NullPointerException("Listener cannot be null.");

        listeners.remove(listener);
    }



    /**
     * Runs the experiment based on the given parameters and operations. If some
     * parameters and operations are missing to the correct execution of the
     * experiment, they will be automatically initialize before the run. The
     * results are saved based on the given parameters. Info through out the
     * execution is given by the logger ebsd.
     * 
     * @throws IOException
     *             if an error occurs during the run
     */
    @Override
    public void run() throws IOException {
        setStatus("--- START ---");

        // Create directory for the experiment results
        createDir();

        // Get all operations
        ArrayList<ExpOperation> ops = getAllOperations();

        // Initialize ops
        setStatus("--- Initializing ops ---");
        for (ExpOperation op : ops)
            op.setUp(this);

        int size = patternOp.size;
        int startIndex = patternOp.startIndex;
        for (int index = startIndex; index < startIndex + size; index++) {
            // Increment progress
            progress = (double) (index - startIndex) / size;

            // Interrupt
            if (isInterrupted())
                break;

            initRuntimeVariables();

            // Set current index
            currentIndex = index;

            // Run
            runOnce(patternOp, index);
        }

        // Flush ops
        setStatus("--- Flushing ops ---");
        for (ExpOperation op : ops)
            op.tearDown(this);

        // Reset runtime variables
        initRuntimeVariables();

        // Refresh multimap
        mmap.notifyListeners();

        setStatus("--- END ---");
    }



    /**
     * Runs the experiment with one pattern operation.
     * 
     * @param patternOp
     *            current pattern operation
     * @param index
     *            index of the pattern to load from the pattern operation
     * @throws IOException
     *             if an error occurs during the run
     */
    private void runOnce(PatternOp patternOp, int index) throws IOException {
        // Pattern Op
        setStatus("--- Pattern Operation ---");

        currentPatternMap = (ByteMap) runOperation(patternOp, index);
        sourcePatternMap =
                (ByteMap) updateSourceMap(sourcePatternMap, currentPatternMap);

        // Pattern Post Ops
        setStatus("--- Pattern Post Operations ---");
        for (PatternPostOps op : patternPostOps) {
            currentPatternMap = (ByteMap) runOperation(op, currentPatternMap);
            sourcePatternMap =
                    (ByteMap) updateSourceMap(sourcePatternMap,
                            currentPatternMap);
        }

        // Pattern Results Ops
        setStatus("--- Pattern Results Operations ---");
        for (PatternResultsOps op : patternResultsOps)
            runResultsOperation(op, currentPatternMap);

        // Test to continue
        if (houghResultsOps.size() > 0 || detectionResultsOps.size() > 0
                || identificationResultsOps.size() > 0
                || indexingResultsOps.size() > 0) {

            // Hough Pre Ops
            setStatus("--- Hough Pre Operations ---");
            for (HoughPreOps op : houghPreOps)
                currentPatternMap =
                        (ByteMap) runOperation(op, currentPatternMap);

            // Hough Op
            setStatus("--- Hough Operation ---");
            currentHoughMap =
                    (HoughMap) runOperation(houghOp, currentPatternMap);
            sourceHoughMap =
                    (HoughMap) updateSourceMap(sourceHoughMap, currentHoughMap);

            // Hough Post Ops
            setStatus("--- Hough Post Operations ---");
            for (HoughPostOps op : houghPostOps) {
                currentHoughMap = (HoughMap) runOperation(op, currentHoughMap);
                sourceHoughMap =
                        (HoughMap) updateSourceMap(sourceHoughMap,
                                currentHoughMap);
            }

            // Hough Results Ops
            setStatus("--- Hough Results Operations ---");
            for (HoughResultsOps op : houghResultsOps)
                runResultsOperation(op, currentHoughMap);

            // Test to continue
            if (detectionResultsOps.size() > 0
                    || identificationResultsOps.size() > 0
                    || indexingResultsOps.size() > 0) {

                // Detection Pre Ops
                setStatus("--- Detection Pre Operations ---");
                for (DetectionPreOps op : detectionPreOps)
                    currentHoughMap =
                            (HoughMap) runOperation(op, currentHoughMap);

                // Detection Op
                setStatus("--- Detection Operation ---");

                currentPeaksMap =
                        (BinMap) runOperation(detectionOp, currentHoughMap);
                sourcePeaksMap =
                        (BinMap) updateSourceMap(sourcePeaksMap,
                                currentPeaksMap);

                // Detection Post Ops
                setStatus("--- Detection Post Operations ---");
                for (DetectionPostOps op : detectionPostOps) {
                    currentPeaksMap =
                            (BinMap) runOperation(op, currentPeaksMap);
                    sourcePeaksMap =
                            (BinMap) updateSourceMap(sourcePeaksMap,
                                    currentPeaksMap);
                }

                // Detection Results Ops
                setStatus("--- Detection Results Operations ---");
                for (DetectionResultsOps op : detectionResultsOps)
                    runResultsOperation(op, currentPeaksMap);

                // Test to continue
                if (identificationResultsOps.size() > 0
                        || indexingResultsOps.size() > 0) {

                    // Identification Pre Ops
                    setStatus("--- Identification Pre Operations ---");
                    for (IdentificationPreOps op : identificationPreOps)
                        currentPeaksMap =
                                (BinMap) runOperation(op, currentPeaksMap);

                    // Identification Op
                    setStatus("--- Identification Operation ---");

                    currentPeaks =
                            (HoughPeak[]) runOperation(identificationOp,
                                    currentPeaksMap, sourceHoughMap);

                    // Identification Post Ops
                    setStatus("--- Identification Post Operations ---");
                    for (IdentificationPostOps op : identificationPostOps)
                        currentPeaks =
                                (HoughPeak[]) runOperation(op,
                                        (Object[]) currentPeaks);

                    // Identification Results Ops
                    setStatus("--- Identification Results Operations ---");
                    for (IdentificationResultsOps op : identificationResultsOps)
                        runResultsOperation(op, (Object[]) currentPeaks);

                    // Test to continue
                    if (indexingResultsOps.size() > 0) {

                        // Indexing Pre Ops
                        setStatus("--- Indexing Pre Operations ---");
                        for (IndexingPreOps op : indexingPreOps)
                            currentPeaks =
                                    (HoughPeak[]) runOperation(op,
                                            (Object[]) currentPeaks);

                        // Indexing Op
                        setStatus("--- Indexing Operation ---");
                        currentSolutions =
                                (Solution[]) runOperation(indexingOp,
                                        (Object[]) currentPeaks);

                        // Indexing Post Ops
                        setStatus("--- Indexing Post Operations ---");
                        for (IndexingPostOps op : indexingPostOps)
                            currentSolutions =
                                    (Solution[]) runOperation(op,
                                            (Object[]) currentSolutions);

                        // Indexing Results Ops
                        setStatus("--- Indexing Results Operations ---");
                        for (IndexingResultsOps op : indexingResultsOps)
                            runResultsOperation(op, (Object[]) currentSolutions);
                    }
                }
            }
        }
    }



    /**
     * Runs the specified operation with the given arguments.
     * 
     * @param op
     *            an operation
     * @param args
     *            arguments
     * @return return of the operation
     */
    private Object runOperation(ExpOperation op, Object... args) {
        setStatus("Executing " + op.getName() + "...");

        Object out = op.execute(this, args);

        for (ExpListener listener : listeners)
            op.fireExecuted(listener, this, out);

        setStatus("Executing " + op.getName() + "... DONE");

        return out;
    }



    /**
     * Runs a results operation with the given arguments. The results are saved
     * in the multimap using {@link #saveResult(OpResult)} method.
     * 
     * @param op
     *            a results operation
     * @param args
     *            arguments
     */
    private void runResultsOperation(ExpOperation op, Object... args) {
        OpResult[] results = (OpResult[]) runOperation(op, args);

        for (OpResult result : results)
            saveResult(result);
    }



    /**
     * Saves a result in a map with the given alias in the <code>EbsdMMap</code>
     * . If the map does not exist, it is creates by the method
     * {@link #createEmptyMap(String, Number)}. If the result is a
     * <code>Byte</code> the result is saved in a <code>ByteMap</code>. For any
     * other type of <code>Number</code>, the result is saved in a
     * <code>RealMap</code>.
     * 
     * @param result
     *            operation's result
     */
    private void saveResult(OpResult result) {
        Map map = mmap.getMap(result.alias);

        // Create map if it doesn't exist
        if (map == null) {
            map = createMap(result);
            mmap.add(result.alias, map);
        }

        // Save value
        if (result.type.equals(RealMap.class))
            ((RealMap) map).pixArray[currentIndex] = result.value.floatValue();
        else if (result.type.equals(ByteMap.class))
            ((ByteMap) map).pixArray[currentIndex] = result.value.byteValue();
        else if (result.type.equals(PhaseMap.class))
            ((PhaseMap) map).pixArray[currentIndex] = result.value.byteValue();
        else if (result.type.equals(BinMap.class))
            ((BinMap) map).pixArray[currentIndex] =
                    (result.value.intValue() == 0) ? (byte) 0 : (byte) 1;
        else
            throw new RuntimeException("Unknown type of map ("
                    + result.type.toString() + ").");

        // Set that the pixArray of the map was modified
        map.setChanged(Map.MAP_CHANGED);
    }



    @Override
    public void setDir(File dir) {
        super.setDir(dir);

        mmap.setDir(dir);
    }



    @Override
    public void setName(String name) {
        super.setName(name);

        mmap.setName(name);
    }

}
