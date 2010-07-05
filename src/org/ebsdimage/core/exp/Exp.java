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

import static java.util.Arrays.sort;
import static org.ebsdimage.core.exp.ExpConstants.*;
import static ptpshared.utility.Arrays.reverse;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.ebsdimage.core.*;
import org.ebsdimage.core.exp.ops.detection.op.AutomaticTopHat;
import org.ebsdimage.core.exp.ops.detection.op.DetectionOp;
import org.ebsdimage.core.exp.ops.detection.post.DetectionPostOps;
import org.ebsdimage.core.exp.ops.detection.pre.DetectionPreOps;
import org.ebsdimage.core.exp.ops.detection.results.DetectionResultsOps;
import org.ebsdimage.core.exp.ops.hough.op.HoughOp;
import org.ebsdimage.core.exp.ops.hough.op.HoughTransform;
import org.ebsdimage.core.exp.ops.hough.post.HoughPostOps;
import org.ebsdimage.core.exp.ops.hough.pre.HoughPreOps;
import org.ebsdimage.core.exp.ops.hough.results.HoughResultsOps;
import org.ebsdimage.core.exp.ops.identification.op.IdentificationOp;
import org.ebsdimage.core.exp.ops.identification.op.LocalCentroid;
import org.ebsdimage.core.exp.ops.identification.post.IdentificationPostOps;
import org.ebsdimage.core.exp.ops.identification.pre.IdentificationPreOps;
import org.ebsdimage.core.exp.ops.identification.results.IdentificationResultsOps;
import org.ebsdimage.core.exp.ops.indexing.op.IndexingOp;
import org.ebsdimage.core.exp.ops.indexing.op.KriegerLassen1994;
import org.ebsdimage.core.exp.ops.indexing.post.IndexingPostOps;
import org.ebsdimage.core.exp.ops.indexing.pre.IndexingPreOps;
import org.ebsdimage.core.exp.ops.indexing.results.IndexingResultsOps;
import org.ebsdimage.core.exp.ops.pattern.op.PatternOp;
import org.ebsdimage.core.exp.ops.pattern.post.PatternPostOps;
import org.ebsdimage.core.exp.ops.pattern.results.PatternResultsOps;
import org.ebsdimage.core.run.Operation;
import org.ebsdimage.core.run.Run;

import ptpshared.io.FileUtil;
import rmlimage.core.BinMap;
import rmlimage.core.ByteMap;
import rmlimage.core.Map;
import rmlimage.module.real.core.RealMap;
import crystallography.core.Crystal;

/**
 * Experiment engine.
 * 
 * @author Philippe T. Pinard
 */
public class Exp extends Run {

    /** Save maps during the run. */
    public final CurrentMapsSaver currentMapsSaver;

    /** <code>MultiMap</code> holding the result and metadata for the experiment. */
    public final EbsdMMap mmap;

    /** List of pattern operations of the experiment. */
    private ArrayList<PatternOp> patternOps = new ArrayList<PatternOp>();

    /** Runtime variable for the source pattern map. */
    protected ByteMap sourcePatternMap;

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

    /** List of pattern post operations of the experiment. */
    private ArrayList<PatternPostOps> patternPostOps =
            new ArrayList<PatternPostOps>();

    /** List of pattern results operations of the experiment. */
    private ArrayList<PatternResultsOps> patternResultsOps =
            new ArrayList<PatternResultsOps>();

    /** List of Hough pre operations of the experiment. */
    private ArrayList<HoughPreOps> houghPreOps = new ArrayList<HoughPreOps>();

    /** Default Hough operation. */
    public static final HoughOp DEFAULT_HOUGH_OP = new HoughTransform();

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
            new AutomaticTopHat();

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
            new LocalCentroid();

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
    public static final IndexingOp DEFAULT_INDEXING_OP =
            new KriegerLassen1994();

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
     * experiment will execute. The <code>Operation</code>s are organized into
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
     * @param currentMapsSaver
     *            <code>SaveMaps</code> object
     * @throws NullPointerException
     *             if the <code>EbsdMMap</code> is null
     * @throws NullPointerException
     *             if the array of <code>Operation</code> is null
     * @throws NullPointerException
     *             if the <code>SaveMaps</code> is null
     * @throws NullPointerException
     *             if an <code>Operation</code> is null
     * @see EbsdMMap
     * @see CurrentMapsFileSaver
     */
    public Exp(EbsdMMap mmap, Operation[] ops, CurrentMapsSaver currentMapsSaver) {
        if (mmap == null)
            throw new NullPointerException("Ebsd multi-map cannot be null.");
        if (ops == null)
            throw new NullPointerException(
                    "The operations array cannot be null.");
        if (currentMapsSaver == null)
            throw new NullPointerException("Current maps saver cannot be null.");

        // EbsdMMap
        this.mmap = mmap;

        // Current maps saver
        this.currentMapsSaver = currentMapsSaver;

        // Operations
        for (Operation op : ops)
            addOperation(op);

        // Initialize runtime variables
        initRuntimeVariables();
    }



    /**
     * Creates a new experiment. This constructor should be used to create a
     * completely new experiment without a prior <code>EbsdMMap</code>. In other
     * words, it creates a new <code>EbsdMMap</code> for the user using the
     * specified maps' width, maps' height, <code>EbsdMetadata</code> and an
     * array of phases.
     * 
     * @param width
     *            width of the maps
     * @param height
     *            height of the maps
     * @param metadata
     *            metadata for the <code>EbsdMMap</code>
     * @param phases
     *            array of defined phases
     * @param ops
     *            operations for the experiment
     * @param currentMapsSaver
     *            <code>SaveMaps</code> object
     * @throws NullPointerException
     *             if the metadata is null
     * @throws NullPointerException
     *             if the array of <code>Operation</code> is null
     * @throws NullPointerException
     *             if the array of phases is null
     * @throws NullPointerException
     *             if the <code>SaveMaps</code> is null
     * @throws NullPointerException
     *             if an <code>Operation</code> is null
     * @throws IllegalArgumentException
     *             if the width or the height are less or equal to zero
     * @see Exp#Exp(EbsdMMap, Operation[], CurrentMapsSaver)
     * @see EbsdMetadata
     * @see CurrentMapsFileSaver
     */
    public Exp(int width, int height, ExpMetadata metadata, Crystal[] phases,
            Operation[] ops, CurrentMapsSaver currentMapsSaver) {
        if (metadata == null)
            throw new NullPointerException("Ebsd multi-map cannot be null.");
        if (phases == null)
            throw new NullPointerException("Phases cannot be null.");
        if (ops == null)
            throw new NullPointerException(
                    "The operations array cannot be null.");
        if (currentMapsSaver == null)
            throw new NullPointerException("Current maps saver cannot be null.");
        if (width <= 0)
            throw new IllegalArgumentException(
                    "The width has to be greater than 0.");
        if (height <= 0)
            throw new IllegalArgumentException(
                    "The height has to be greater than 0.");

        // EbsdMMap
        mmap = new ExpMMap(width, height, metadata);
        mmap.getPhasesMap().setPhases(phases);

        // Current maps saver
        this.currentMapsSaver = currentMapsSaver;

        // Operations
        for (Operation op : ops)
            addOperation(op);

        // Initialize runtime variables
        initRuntimeVariables();
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
                patternOps.add((PatternOp) op);
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
     * Creates a new map of the specified type.
     * 
     * @param type
     *            class of the map's type
     * @return a new map
     * @throws IllegalArgumentException
     *             if the map's type is unknown
     */
    private Map createMap(Class<? extends Map> type) {
        if (type.equals(RealMap.class)) {
            RealMap map = new RealMap(mmap.width, mmap.height);
            map.clear(Float.NaN);
            return map;
        } else if (type.equals(ByteMap.class)) {
            ByteMap map = new ByteMap(mmap.width, mmap.height);
            map.clear(0);
            return map;
        } else if (type.equals(PhasesMap.class)) {
            PhasesMap map = new PhasesMap(mmap.width, mmap.height);
            map.clear(0);
            return map;
        } else if (type.equals(BinMap.class)) {
            BinMap map = new BinMap(mmap.width, mmap.height);
            map.clear(false);
            return map;
        } else
            throw new IllegalArgumentException("Unknown type of map ("
                    + type.toString() + ").");
    }



    /**
     * Returns a list of all the defined operations in this experiment.
     * 
     * @return operations
     */
    public ArrayList<Operation> getAllOperations() {
        ArrayList<Operation> ops = new ArrayList<Operation>();

        ops.addAll(patternOps);
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
        return currentHoughMap;
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
        return currentPatternMap;
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
        return sourcePatternMap;
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
        return currentPeaks;
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
        return currentPeaksMap;
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
        return currentSolutions;
    }



    /**
     * Returns the detection operation of this experiment.
     * 
     * @return detection operation
     */
    public DetectionOp getDetectionOp() {
        return detectionOp;
    }



    /**
     * Returns the detection post operations of this experiment.
     * 
     * @return detection post operations
     */
    public DetectionPostOps[] getDetectionPostOps() {
        return detectionPostOps.toArray(new DetectionPostOps[detectionPostOps.size()]);
    }



    /**
     * Returns the detection pre operations of this experiment.
     * 
     * @return detection pre operations
     */
    public DetectionPreOps[] getDetectionPreOps() {
        return detectionPreOps.toArray(new DetectionPreOps[detectionPreOps.size()]);
    }



    /**
     * Returns the detection results operations of this experiment.
     * 
     * @return detection results operations
     */
    public DetectionResultsOps[] getDetectionResultsOps() {
        return detectionResultsOps.toArray(new DetectionResultsOps[detectionResultsOps.size()]);
    }



    /**
     * Returns the Hough operation of this experiment.
     * 
     * @return Hough operation
     */
    public HoughOp getHoughOp() {
        return houghOp;
    }



    /**
     * Returns the Hough post operations of this experiment.
     * 
     * @return Hough post operations
     */
    public HoughPostOps[] getHoughPostOps() {
        return houghPostOps.toArray(new HoughPostOps[houghPostOps.size()]);
    }



    /**
     * Returns the Hough pre operations of this experiment.
     * 
     * @return Hough pre operations
     */
    public HoughPreOps[] getHoughPreOps() {
        return houghPreOps.toArray(new HoughPreOps[houghPreOps.size()]);
    }



    /**
     * Returns the Hough results operations of this experiment.
     * 
     * @return Hough results operations
     */
    public HoughResultsOps[] getHoughResultsOps() {
        return houghResultsOps.toArray(new HoughResultsOps[houghResultsOps.size()]);
    }



    /**
     * Returns the identification operation of this experiment.
     * 
     * @return identification operation
     */
    public IdentificationOp getIdentificationOp() {
        return identificationOp;
    }



    /**
     * Returns the identification post operations of this experiment.
     * 
     * @return identification post operations
     */
    public IdentificationPostOps[] getIdentificationPostOps() {
        return identificationPostOps.toArray(new IdentificationPostOps[identificationPostOps.size()]);
    }



    /**
     * Returns the identification pre operations of this experiment.
     * 
     * @return identification pre operations
     */
    public IdentificationPreOps[] getIdentificationPreOps() {
        return identificationPreOps.toArray(new IdentificationPreOps[identificationPreOps.size()]);
    }



    /**
     * Returns the identification results operations of this experiment.
     * 
     * @return identification results operations
     */
    public IdentificationResultsOps[] getIdentificationResultsOps() {
        return identificationResultsOps.toArray(new IdentificationResultsOps[identificationResultsOps.size()]);
    }



    /**
     * Returns the indexing operation of this experiment.
     * 
     * @return indexing operation
     */
    public IndexingOp getIndexingOp() {
        return indexingOp;
    }



    /**
     * Returns the indexing post operations of this experiment.
     * 
     * @return indexing post operations
     */
    public IndexingPostOps[] getIndexingPostOps() {
        return indexingPostOps.toArray(new IndexingPostOps[indexingPostOps.size()]);
    }



    /**
     * Returns the indexing pre operations of this experiment.
     * 
     * @return indexing pre operations
     */
    public IndexingPreOps[] getIndexingPreOps() {
        return indexingPreOps.toArray(new IndexingPreOps[indexingPreOps.size()]);
    }



    /**
     * Returns the indexing results operations of this experiment.
     * 
     * @return indexing results operations
     */
    public IndexingResultsOps[] getIndexingResultsOps() {
        return indexingResultsOps.toArray(new IndexingResultsOps[indexingResultsOps.size()]);
    }



    /**
     * Returns the pattern operations of this experiment.
     * 
     * @return pattern operations
     */
    public PatternOp[] getPatternOps() {
        return patternOps.toArray(new PatternOp[patternOps.size()]);
    }



    /**
     * Returns the pattern post operations of this experiment.
     * 
     * @return pattern post operations
     */
    public PatternPostOps[] getPatternPostOps() {
        return patternPostOps.toArray(new PatternPostOps[patternPostOps.size()]);
    }



    /**
     * Returns the pattern results operations of this experiment.
     * 
     * @return pattern results operations
     */
    public PatternResultsOps[] getPatternResultsOps() {
        return patternResultsOps.toArray(new PatternResultsOps[patternResultsOps.size()]);
    }



    @Override
    public double getTaskProgress() {
        // Quick fix to notify the listeners of the EbsdMMap. This allows the
        // map to be refresh when displayed in the GUI.
        mmap.notifyListeners();

        return super.getTaskProgress();
    }



    @Override
    protected void initRuntimeVariables() {
        super.initRuntimeVariables();

        sourcePatternMap = null;
        currentPatternMap = null;
        currentHoughMap = null;
        currentPeaksMap = null;
        currentPeaks = null;
        currentSolutions = null;
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
        ArrayList<Operation> ops = getAllOperations();

        // Initialize ops
        setStatus("--- Initializing ops ---");
        for (Operation op : ops)
            op.setUp(this);

        // Loop through all the pattern operations
        int size = patternOps.size();
        for (int i = 0; i < size; i++) {
            // Increment progress
            progress = (double) i / size;

            // Interrupt
            if (isInterrupted())
                break;

            // Current pattern operation
            PatternOp patternOp = patternOps.get(i);

            // Set current index
            currentIndex = patternOp.index;

            // Run
            runOnce(patternOp);
        }

        // Flush ops
        setStatus("--- Flushing ops ---");
        for (Operation op : ops)
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
     * @throws IOException
     *             if an error occurs during the run
     */
    private void runOnce(PatternOp patternOp) throws IOException {
        // Pattern Op
        setStatus("--- Pattern Operation ---");

        setStatus("Performing " + patternOp.getName() + " (" + patternOp.index
                + ")...");

        sourcePatternMap = patternOp.load(this);
        currentPatternMap = sourcePatternMap.duplicate();

        setStatus("Performing " + patternOp.getName() + "... DONE");

        currentMapsSaver.savePatternMap(this, currentPatternMap);

        // Pattern Post Ops
        setStatus("--- Pattern Post Operations ---");
        for (PatternPostOps op : patternPostOps) {
            setStatus("Performing " + op.getName() + "...");

            currentPatternMap = op.process(this, currentPatternMap);

            setStatus("Performing " + op.getName() + "... DONE");

            currentMapsSaver.saveMap(this, op, currentPatternMap);
        }

        // Pattern Results Ops
        setStatus("--- Pattern Results Operations ---");
        for (PatternResultsOps op : patternResultsOps) {
            setStatus("Calculating " + op.getName() + "...");

            saveResults(op.calculate(this, currentPatternMap));

            setStatus("Calculating " + op.getName() + "... DONE");
        }

        // Test to continue
        if (houghResultsOps.size() > 0 || detectionResultsOps.size() > 0
                || identificationResultsOps.size() > 0
                || indexingResultsOps.size() > 0) {

            // Hough Pre Ops
            setStatus("--- Hough Pre Operations ---");
            for (HoughPreOps op : houghPreOps) {
                setStatus("Performing " + op.getName() + "...");

                currentPatternMap = op.process(this, currentPatternMap);

                setStatus("Performing " + op.getName() + "... DONE");

                currentMapsSaver.saveMap(this, op, currentPatternMap);
            }

            // Hough Op
            setStatus("--- Hough Operation ---");

            setStatus("Performing " + houghOp.getName() + "...");

            currentHoughMap = houghOp.transform(this, currentPatternMap);

            setStatus("Performing " + houghOp.getName() + "... DONE");

            currentMapsSaver.saveHoughMap(this, currentHoughMap);

            // Hough Post Ops
            setStatus("--- Hough Post Operations ---");
            for (HoughPostOps op : houghPostOps) {
                setStatus("Performing " + op.getName() + "...");

                currentHoughMap = op.process(this, currentHoughMap);

                setStatus("Performing " + op.getName() + "... DONE");

                currentMapsSaver.saveMap(this, op, currentHoughMap);
            }

            // Hough Results Ops
            setStatus("--- Hough Results Operations ---");
            for (HoughResultsOps op : houghResultsOps) {
                setStatus("Calculating " + op.getName() + "...");

                saveResults(op.calculate(this, currentHoughMap));

                setStatus("Calculating " + op.getName() + "... DONE");
            }

            // Test to continue
            if (detectionResultsOps.size() > 0
                    || identificationResultsOps.size() > 0
                    || indexingResultsOps.size() > 0) {

                // Detection Pre Ops
                setStatus("--- Detection Pre Operations ---");
                for (DetectionPreOps op : detectionPreOps) {
                    setStatus("Performing " + op.getName() + "...");

                    currentHoughMap = op.process(this, currentHoughMap);

                    setStatus("Performing " + op.getName() + "... DONE");

                    currentMapsSaver.saveMap(this, op, currentHoughMap);
                }

                // Detection Op
                setStatus("--- Detection Operation ---");

                setStatus("Performing " + detectionOp.getName() + "...");

                currentPeaksMap = detectionOp.detect(this, currentHoughMap);

                setStatus("Performing " + detectionOp.getName() + "... DONE");

                currentMapsSaver.savePeaksMap(this, currentPeaksMap);

                // Detection Post Ops
                setStatus("--- Detection Post Operations ---");
                for (DetectionPostOps op : detectionPostOps) {
                    setStatus("Performing " + op.getName() + "...");

                    currentPeaksMap = op.process(this, currentPeaksMap);

                    setStatus("Performing " + op.getName() + "... DONE");

                    currentMapsSaver.saveMap(this, op, currentPeaksMap);
                }

                // Detection Results Ops
                setStatus("--- Detection Results Operations ---");
                for (DetectionResultsOps op : detectionResultsOps) {
                    setStatus("Calculating " + op.getName() + "...");

                    saveResults(op.calculate(this, currentPeaksMap));

                    setStatus("Calculating " + op.getName() + "... DONE");
                }

                // Test to continue
                if (identificationResultsOps.size() > 0
                        || indexingResultsOps.size() > 0) {

                    // Identification Pre Ops
                    setStatus("--- Identification Pre Operations ---");
                    for (IdentificationPreOps op : identificationPreOps) {
                        setStatus("Performing " + op.getName() + "...");

                        currentPeaksMap = op.process(this, currentPeaksMap);

                        setStatus("Performing " + op.getName() + "... DONE");

                        currentMapsSaver.saveMap(this, op, currentPeaksMap);
                    }

                    // Identification Op
                    setStatus("--- Identification Operation ---");

                    setStatus("Performing " + identificationOp.getName()
                            + "...");

                    currentPeaks =
                            identificationOp.identify(this, currentPeaksMap,
                                    currentHoughMap);

                    setStatus("Performing " + identificationOp.getName()
                            + "... DONE");

                    // Identification Post Ops
                    setStatus("--- Identification Post Operations ---");
                    for (IdentificationPostOps op : identificationPostOps) {
                        setStatus("Performing " + op.getName() + "...");

                        currentPeaks = op.process(this, currentPeaks);

                        setStatus("Performing " + op.getName() + "... DONE");
                    }

                    // Identification Results Ops
                    setStatus("--- Identification Results Operations ---");
                    for (IdentificationResultsOps op : identificationResultsOps) {
                        setStatus("Calculating " + op.getName() + "...");

                        saveResults(op.calculate(this, currentPeaks));

                        setStatus("Calculating " + op.getName() + "... DONE");
                    }

                    // Test to continue
                    if (indexingResultsOps.size() > 0) {

                        // Indexing Pre Ops
                        setStatus("--- Indexing Pre Operations ---");
                        for (IndexingPreOps op : indexingPreOps) {
                            setStatus("Performing " + op.getName() + "...");

                            currentPeaks = op.process(this, currentPeaks);

                            setStatus("Performing " + op.getName() + "... DONE");
                        }

                        // Indexing Op
                        setStatus("--- Indexing Operation ---");

                        setStatus("Performing " + indexingOp.getName() + "...");

                        currentSolutions = indexingOp.index(this, currentPeaks);

                        // Sort solutions by fit
                        sort(currentSolutions, new SolutionFitComparator());
                        reverse(currentSolutions);

                        // Solution overlay
                        if (currentSolutions.length > 0)
                            currentMapsSaver.saveSolutionOverlay(this,
                                    currentSolutions[0]);

                        setStatus("Performing " + indexingOp.getName()
                                + "... DONE");

                        // Indexing Post Ops
                        setStatus("--- Indexing Post Operations ---");
                        for (IndexingPostOps op : indexingPostOps) {
                            setStatus("Performing " + op.getName() + "...");

                            currentSolutions =
                                    op.process(this, currentSolutions);

                            setStatus("Performing " + op.getName() + "... DONE");
                        }

                        // Indexing Results Ops
                        setStatus("--- Indexing Results Operations ---");
                        for (IndexingResultsOps op : indexingResultsOps) {
                            setStatus("Calculating " + op.getName() + "...");

                            saveResults(op.calculate(this, currentSolutions));

                            setStatus("Calculating " + op.getName()
                                    + "... DONE");
                        }
                    }
                }
            }
        }
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
            map = createMap(result.type);
            mmap.add(result.alias, map);
        }

        // Save value
        if (result.type.equals(RealMap.class))
            ((RealMap) map).pixArray[currentIndex] = result.value.floatValue();
        else if (result.type.equals(ByteMap.class))
            ((ByteMap) map).pixArray[currentIndex] = result.value.byteValue();
        else if (result.type.equals(PhasesMap.class))
            ((PhasesMap) map).pixArray[currentIndex] = result.value.byteValue();
        else if (result.type.equals(BinMap.class))
            ((BinMap) map).pixArray[currentIndex] =
                    (result.value.intValue() == 0) ? (byte) 0 : (byte) 1;
        else
            throw new RuntimeException("Unknown type of map ("
                    + result.type.toString() + ").");

        // Set that the pixArray of the map was modified
        map.setChanged(Map.MAP_CHANGED);
    }



    /**
     * Saves all the results in the <code>HashMap</code>. The method
     * {@link #saveResult(String, Number)} is called for each result.
     * 
     * @param results
     *            operation's results
     * @see Exp#saveResult(String, Number)
     */
    private void saveResults(OpResult[] results) {
        for (OpResult result : results)
            saveResult(result);
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
