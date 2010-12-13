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
import java.util.Arrays;

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
import org.ebsdimage.core.exp.ops.indexing.op.KriegerLassen1994;
import org.ebsdimage.core.exp.ops.indexing.post.IndexingPostOps;
import org.ebsdimage.core.exp.ops.indexing.pre.IndexingPreOps;
import org.ebsdimage.core.exp.ops.indexing.results.IndexingResultsOps;
import org.ebsdimage.core.exp.ops.pattern.op.PatternOp;
import org.ebsdimage.core.exp.ops.pattern.post.PatternPostOps;
import org.ebsdimage.core.exp.ops.pattern.results.PatternResultsOps;
import org.ebsdimage.core.run.Operation;
import org.ebsdimage.core.run.Run;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.core.Persist;
import org.simpleframework.xml.core.Validate;

import ptpshared.io.FileUtil;
import rmlimage.core.BinMap;
import rmlimage.core.ByteMap;
import rmlimage.core.Map;
import rmlimage.module.real.core.RealMap;
import crystallography.core.Crystal;
import static org.ebsdimage.core.exp.ExpConstants.*;

/**
 * Experiment engine.
 * 
 * @author Philippe T. Pinard
 */
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
    @Element(name = "patternOp")
    private PatternOp patternOp = null;

    /** List of pattern post operations of the experiment. */
    @ElementList(name = "patternPostOps")
    private ArrayList<PatternPostOps> patternPostOps =
            new ArrayList<PatternPostOps>();

    /** List of pattern results operations of the experiment. */
    @ElementList(name = "patternResultsOps")
    private ArrayList<PatternResultsOps> patternResultsOps =
            new ArrayList<PatternResultsOps>();

    /** List of Hough pre operations of the experiment. */
    @ElementList(name = "houghPreOps")
    private ArrayList<HoughPreOps> houghPreOps = new ArrayList<HoughPreOps>();

    /** Default Hough operation. */
    public static final HoughOp DEFAULT_HOUGH_OP = AutoHoughTransform.DEFAULT;

    /** Hough operation. */
    @Element(name = "houghOp")
    private HoughOp houghOp = DEFAULT_HOUGH_OP;

    /** List of Hough post operations of the experiment. */
    @ElementList(name = "houghPostOps")
    private ArrayList<HoughPostOps> houghPostOps =
            new ArrayList<HoughPostOps>();

    /** List of Hough results operations of the experiment. */
    @ElementList(name = "houghResultsOps")
    private ArrayList<HoughResultsOps> houghResultsOps =
            new ArrayList<HoughResultsOps>();

    /** List of detection pre operations of the experiment. */
    @ElementList(name = "detectionPreOps")
    private ArrayList<DetectionPreOps> detectionPreOps =
            new ArrayList<DetectionPreOps>();

    /** Default detection operation. */
    public static final DetectionOp DEFAULT_DETECTION_OP =
            AutomaticTopHat.DEFAULT;

    /** Detection operation. */
    @Element(name = "detectionOp")
    private DetectionOp detectionOp = DEFAULT_DETECTION_OP;

    /** List of detection post operations of the experiment. */
    @ElementList(name = "detectionPostOps")
    private ArrayList<DetectionPostOps> detectionPostOps =
            new ArrayList<DetectionPostOps>();

    /** List of detection results operations of the experiment. */
    @ElementList(name = "detectionResultsOps")
    private ArrayList<DetectionResultsOps> detectionResultsOps =
            new ArrayList<DetectionResultsOps>();

    /** List of identification pre operations of the experiment. */
    @ElementList(name = "identificationPreOps")
    private ArrayList<IdentificationPreOps> identificationPreOps =
            new ArrayList<IdentificationPreOps>();

    /** Default identification operation. */
    public static final IdentificationOp DEFAULT_IDENTIFICATION_OP =
            CenterOfMass.DEFAULT;

    /** Identification operation. */
    @Element(name = "identificationOp")
    private IdentificationOp identificationOp = DEFAULT_IDENTIFICATION_OP;

    /** List of identification post operations of the experiment. */
    @ElementList(name = "identificationPostOps")
    private ArrayList<IdentificationPostOps> identificationPostOps =
            new ArrayList<IdentificationPostOps>();

    /** List of identification results operations of the experiment. */
    @ElementList(name = "identificationResultsOps")
    private ArrayList<IdentificationResultsOps> identificationResultsOps =
            new ArrayList<IdentificationResultsOps>();

    /** List of indexing pre operations of the experiment. */
    @ElementList(name = "indexingPreOps")
    private ArrayList<IndexingPreOps> indexingPreOps =
            new ArrayList<IndexingPreOps>();

    /** Default indexing operation. */
    public static final IndexingOp DEFAULT_INDEXING_OP =
            KriegerLassen1994.DEFAULT;

    /** Indexing operation. */
    @Element(name = "indexingOp")
    private IndexingOp indexingOp = DEFAULT_INDEXING_OP;

    /** List of indexing post operations of the experiment. */
    @ElementList(name = "indexingPostOps")
    private ArrayList<IndexingPostOps> indexingPostOps =
            new ArrayList<IndexingPostOps>();

    /** List of indexing results operations of the experiment. */
    @ElementList(name = "indexingResultsOps")
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
            @ElementList(name = "phases") ArrayList<Crystal> phases,
            @ElementList(name = "listeners") ArrayList<ExpListener> listeners,
            @Element(name = "patternOp") PatternOp patternOp,
            @ElementList(name = "patternPostOps") ArrayList<PatternPostOps> patternPostOps,
            @ElementList(name = "patternResultsOps") ArrayList<PatternResultsOps> patternResultsOps,
            @ElementList(name = "houghPreOps") ArrayList<HoughPreOps> houghPreOps,
            @Element(name = "houghOp") HoughOp houghOp,
            @ElementList(name = "houghPostOps") ArrayList<HoughPostOps> houghPostOps,
            @ElementList(name = "houghResultsOps") ArrayList<HoughResultsOps> houghResultsOps,
            @ElementList(name = "detectionPreOps") ArrayList<DetectionPreOps> detectionPreOps,
            @Element(name = "detectionOp") DetectionOp detectionOp,
            @ElementList(name = "detectionPostOps") ArrayList<DetectionPostOps> detectionPostOps,
            @ElementList(name = "detectionResultsOps") ArrayList<DetectionResultsOps> detectionResultsOps,
            @ElementList(name = "identificationPreOps") ArrayList<IdentificationPreOps> identificationPreOps,
            @Element(name = "identificationOp") IdentificationOp identificationOp,
            @ElementList(name = "identificationPostOps") ArrayList<IdentificationPostOps> identificationPostOps,
            @ElementList(name = "identificationResultsOps") ArrayList<IdentificationResultsOps> identificationResultsOps,
            @ElementList(name = "indexingPreOps") ArrayList<IndexingPreOps> indexingPreOps,
            @Element(name = "indexingOp") IndexingOp indexingOp,
            @ElementList(name = "indexingPostOps") ArrayList<IndexingPostOps> indexingPostOps,
            @ElementList(name = "indexingResultsOps") ArrayList<IndexingResultsOps> indexingResultsOps) {
        mmap = new ExpMMap(width, height);
        mmap.setMetadata(metadata);

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
     * Adds a list of operations. The method {@link #addOperation(Operation)} is
     * called for each operation.
     * 
     * @param ops
     *            operations
     */
    protected void addOperations(ArrayList<? extends Operation> ops) {
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
     * Fires detection operation action.
     * 
     * @param op
     *            operation
     * @param peaksMap
     *            peaks map
     */
    private void fireDetectionOpPerformed(DetectionOp op, BinMap peaksMap) {
        for (ExpListener listener : listeners)
            listener.detectionOpPerformed(this, op, peaksMap);
    }



    /**
     * Fires detection post-operation action.
     * 
     * @param op
     *            operation
     * @param peaksMap
     *            peaks map
     */
    private void fireDetectionPostPerformed(DetectionPostOps op, BinMap peaksMap) {
        for (ExpListener listener : listeners)
            listener.detectionPostPerformed(this, op, peaksMap);
    }



    /**
     * Fires Hough pre-operation action.
     * 
     * @param op
     *            operation
     * @param houghMap
     *            Hough map
     */
    private void fireDetectionPrePerformed(DetectionPreOps op, HoughMap houghMap) {
        for (ExpListener listener : listeners)
            listener.detectionPrePerformed(this, op, houghMap);
    }



    /**
     * Fires detection result action.
     * 
     * @param op
     *            operation
     * @param results
     *            operation result(s)
     */
    private void fireDetectionResultPerformed(DetectionResultsOps op,
            OpResult[] results) {
        for (ExpListener listener : listeners)
            for (OpResult result : results)
                listener.detectionResultsPerformed(this, op, result);
    }



    /**
     * Fires Hough operation action.
     * 
     * @param op
     *            operation
     * @param houghMap
     *            Hough map
     */
    private void fireHoughOpPerformed(HoughOp op, HoughMap houghMap) {
        for (ExpListener listener : listeners)
            listener.houghOpPerformed(this, op, houghMap);
    }



    /**
     * Fires Hough post-operation action.
     * 
     * @param op
     *            operation
     * @param houghMap
     *            Hough map
     */
    private void fireHoughPostPerformed(HoughPostOps op, HoughMap houghMap) {
        for (ExpListener listener : listeners)
            listener.houghPostPerformed(this, op, houghMap);
    }



    /**
     * Fires Hough pre-operation action.
     * 
     * @param op
     *            operation
     * @param patternMap
     *            pattern map
     */
    private void fireHoughPrePerformed(HoughPreOps op, ByteMap patternMap) {
        for (ExpListener listener : listeners)
            listener.houghPrePerformed(this, op, patternMap);
    }



    /**
     * Fires Hough result action.
     * 
     * @param op
     *            operation
     * @param results
     *            operation result(s)
     */
    private void fireHoughResultPerformed(HoughResultsOps op, OpResult[] results) {
        for (ExpListener listener : listeners)
            for (OpResult result : results)
                listener.houghResultsPerformed(this, op, result);
    }



    /**
     * Fires identification operation action.
     * 
     * @param op
     *            operation
     * @param peaks
     *            Hough peaks
     */
    private void fireIdentificationOpPerformed(IdentificationOp op,
            HoughPeak[] peaks) {
        for (ExpListener listener : listeners)
            listener.identificationOpPerformed(this, op, peaks);
    }



    /**
     * Fires identification post-operation action.
     * 
     * @param op
     *            operation
     * @param peaks
     *            Hough peaks
     */
    private void fireIdentificationPostPerformed(IdentificationPostOps op,
            HoughPeak[] peaks) {
        for (ExpListener listener : listeners)
            listener.identificationPostPerformed(this, op, peaks);
    }



    /**
     * Fires identification pre-operation action.
     * 
     * @param op
     *            operation
     * @param peaksMap
     *            peaks map
     */
    private void fireIdentificationPrePerformed(IdentificationPreOps op,
            BinMap peaksMap) {
        for (ExpListener listener : listeners)
            listener.identificationPrePerformed(this, op, peaksMap);
    }



    /**
     * Fires identification result action.
     * 
     * @param op
     *            operation
     * @param results
     *            operation result(s)
     */
    private void fireIdentificationResultPerformed(IdentificationResultsOps op,
            OpResult[] results) {
        for (ExpListener listener : listeners)
            for (OpResult result : results)
                listener.identificationResultsPerformed(this, op, result);
    }



    /**
     * Fires indexing operation action.
     * 
     * @param op
     *            operation
     * @param solutions
     *            solutions
     */
    private void fireIndexingOpPerformed(IndexingOp op, Solution[] solutions) {
        for (ExpListener listener : listeners)
            listener.indexingOpPerformed(this, op, solutions);
    }



    /**
     * Fires indexing post-operation action.
     * 
     * @param op
     *            operation
     * @param solutions
     *            solutions
     */
    private void fireIndexingPostPerformed(IndexingPostOps op,
            Solution[] solutions) {
        for (ExpListener listener : listeners)
            listener.indexingPostPerformed(this, op, solutions);
    }



    /**
     * Fires indexing pre-operation action.
     * 
     * @param op
     *            operation
     * @param peaks
     *            Hough peaks
     */
    private void fireIndexingPrePerformed(IndexingPreOps op, HoughPeak[] peaks) {
        for (ExpListener listener : listeners)
            listener.indexingPrePerformed(this, op, peaks);
    }



    /**
     * Fires indexing result action.
     * 
     * @param op
     *            operation
     * @param results
     *            operation result(s)
     */
    private void fireIndexingResultPerformed(IndexingResultsOps op,
            OpResult[] results) {
        for (ExpListener listener : listeners)
            for (OpResult result : results)
                listener.indexingResultsPerformed(this, op, result);
    }



    /**
     * Fires pattern operation action.
     * 
     * @param op
     *            operation
     * @param patternMap
     *            pattern map
     */
    private void firePatternOpPerformed(PatternOp op, ByteMap patternMap) {
        for (ExpListener listener : listeners)
            listener.patternOpPerformed(this, op, patternMap);
    }



    /**
     * Fires pattern post-operation action.
     * 
     * @param op
     *            operation
     * @param patternMap
     *            pattern map
     */
    private void firePatternPostPerformed(PatternPostOps op, ByteMap patternMap) {
        for (ExpListener listener : listeners)
            listener.patternPostPerformed(this, op, patternMap);
    }



    /**
     * Fires pattern result action.
     * 
     * @param op
     *            operation
     * @param results
     *            operation result(s)
     */
    private void firePatternResultPerformed(PatternResultsOps op,
            OpResult[] results) {
        for (ExpListener listener : listeners)
            for (OpResult result : results)
                listener.patternResultsPerformed(this, op, result);
    }



    /**
     * Returns a list of all the defined operations in this experiment.
     * 
     * @return operations
     */
    public ArrayList<Operation> getAllOperations() {
        ArrayList<Operation> ops = new ArrayList<Operation>();

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
    public PatternOp getPatternOp() {
        return patternOp;
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



    /**
     * Returns the phases defined in the multimap. A copy of the phases is
     * returned.
     * 
     * @return phases
     */
    @ElementList(name = "phases")
    public ArrayList<Crystal> getPhases() {
        return new ArrayList<Crystal>(Arrays.asList(mmap.getPhases().clone()));
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
     */
    private void hasMapSizeChanged(Map source, Map current) {
        if (source == null)
            source = current.duplicate();

        if (!source.isSameSize(current))
            source = current.duplicate();
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
     * Prepares the operations to be serialized by setting their index as their
     * position within each category's array.
     */
    @SuppressWarnings("unused")
    @Persist
    private void prepare() {
        prepare(patternPostOps);
        prepare(patternResultsOps);

        prepare(houghPreOps);
        prepare(houghPostOps);
        prepare(houghResultsOps);

        prepare(detectionPreOps);
        prepare(detectionPostOps);
        prepare(detectionResultsOps);

        prepare(identificationPreOps);
        prepare(identificationPostOps);
        prepare(identificationResultsOps);

        prepare(indexingPreOps);
        prepare(indexingPostOps);
        prepare(indexingResultsOps);
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
        ArrayList<Operation> ops = getAllOperations();

        // Initialize ops
        setStatus("--- Initializing ops ---");
        for (Operation op : ops)
            op.setUp(this);

        int size = patternOp.size;
        int startIndex = patternOp.startIndex;
        for (int index = startIndex; index < startIndex + size; index++) {
            // Increment progress
            progress = (double) (index - startIndex) / size;

            // Interrupt
            if (isInterrupted())
                break;

            // Set current index
            currentIndex = index;

            // Run
            runOnce(patternOp, index);
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
     * @param index
     *            index of the pattern to load from the pattern operation
     * @throws IOException
     *             if an error occurs during the run
     */
    private void runOnce(PatternOp patternOp, int index) throws IOException {
        OpResult[] results;

        // Pattern Op
        setStatus("--- Pattern Operation ---");

        setStatus("Performing " + patternOp.getName() + " (" + index + ")...");

        currentPatternMap = patternOp.load(this, index);
        hasMapSizeChanged(sourcePatternMap, currentPatternMap);

        setStatus("Performing " + patternOp.getName() + "... DONE");

        firePatternOpPerformed(patternOp, getCurrentPatternMap());

        // Pattern Post Ops
        setStatus("--- Pattern Post Operations ---");
        for (PatternPostOps op : patternPostOps) {
            setStatus("Performing " + op.getName() + "...");

            currentPatternMap = op.process(this, currentPatternMap);
            hasMapSizeChanged(sourcePatternMap, currentPatternMap);

            setStatus("Performing " + op.getName() + "... DONE");

            firePatternPostPerformed(op, getCurrentPatternMap());
        }

        // Pattern Results Ops
        setStatus("--- Pattern Results Operations ---");
        for (PatternResultsOps op : patternResultsOps) {
            setStatus("Calculating " + op.getName() + "...");

            results = op.calculate(this, getCurrentPatternMap());
            saveResults(results);

            setStatus("Calculating " + op.getName() + "... DONE");

            firePatternResultPerformed(op, results);
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

                fireHoughPrePerformed(op, getCurrentPatternMap());
            }

            // Hough Op
            setStatus("--- Hough Operation ---");

            setStatus("Performing " + houghOp.getName() + "...");

            currentHoughMap = houghOp.transform(this, currentPatternMap);
            hasMapSizeChanged(sourceHoughMap, currentHoughMap);

            setStatus("Performing " + houghOp.getName() + "... DONE");

            fireHoughOpPerformed(houghOp, getCurrentHoughMap());

            // Hough Post Ops
            setStatus("--- Hough Post Operations ---");
            for (HoughPostOps op : houghPostOps) {
                setStatus("Performing " + op.getName() + "...");

                currentHoughMap = op.process(this, currentHoughMap);
                hasMapSizeChanged(sourceHoughMap, currentHoughMap);

                setStatus("Performing " + op.getName() + "... DONE");

                fireHoughPostPerformed(op, getCurrentHoughMap());
            }

            // Hough Results Ops
            setStatus("--- Hough Results Operations ---");
            for (HoughResultsOps op : houghResultsOps) {
                setStatus("Calculating " + op.getName() + "...");

                results = op.calculate(this, currentHoughMap);
                saveResults(results);

                setStatus("Calculating " + op.getName() + "... DONE");

                fireHoughResultPerformed(op, results);
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

                    fireDetectionPrePerformed(op, getCurrentHoughMap());
                }

                // Detection Op
                setStatus("--- Detection Operation ---");

                setStatus("Performing " + detectionOp.getName() + "...");

                currentPeaksMap = detectionOp.detect(this, currentHoughMap);
                hasMapSizeChanged(sourcePeaksMap, currentPeaksMap);

                setStatus("Performing " + detectionOp.getName() + "... DONE");

                fireDetectionOpPerformed(detectionOp, getCurrentPeaksMap());

                // Detection Post Ops
                setStatus("--- Detection Post Operations ---");
                for (DetectionPostOps op : detectionPostOps) {
                    setStatus("Performing " + op.getName() + "...");

                    currentPeaksMap = op.process(this, currentPeaksMap);
                    hasMapSizeChanged(sourcePeaksMap, currentPeaksMap);

                    setStatus("Performing " + op.getName() + "... DONE");

                    fireDetectionPostPerformed(op, getCurrentPeaksMap());
                }

                // Detection Results Ops
                setStatus("--- Detection Results Operations ---");
                for (DetectionResultsOps op : detectionResultsOps) {
                    setStatus("Calculating " + op.getName() + "...");

                    results = op.calculate(this, currentPeaksMap);
                    saveResults(results);

                    setStatus("Calculating " + op.getName() + "... DONE");

                    fireDetectionResultPerformed(op, results);
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

                        fireIdentificationPrePerformed(op, getCurrentPeaksMap());
                    }

                    // Identification Op
                    setStatus("--- Identification Operation ---");

                    setStatus("Performing " + identificationOp.getName()
                            + "...");

                    currentPeaks =
                            identificationOp.identify(this, currentPeaksMap,
                                    sourceHoughMap);

                    setStatus("Performing " + identificationOp.getName()
                            + "... DONE");

                    fireIdentificationOpPerformed(identificationOp,
                            getCurrentPeaks());

                    // Identification Post Ops
                    setStatus("--- Identification Post Operations ---");
                    for (IdentificationPostOps op : identificationPostOps) {
                        setStatus("Performing " + op.getName() + "...");

                        currentPeaks = op.process(this, currentPeaks);

                        setStatus("Performing " + op.getName() + "... DONE");

                        fireIdentificationPostPerformed(op, getCurrentPeaks());
                    }

                    // Identification Results Ops
                    setStatus("--- Identification Results Operations ---");
                    for (IdentificationResultsOps op : identificationResultsOps) {
                        setStatus("Calculating " + op.getName() + "...");

                        results = op.calculate(this, currentPeaks);
                        saveResults(results);

                        setStatus("Calculating " + op.getName() + "... DONE");

                        fireIdentificationResultPerformed(op, results);
                    }

                    // Test to continue
                    if (indexingResultsOps.size() > 0) {

                        // Indexing Pre Ops
                        setStatus("--- Indexing Pre Operations ---");
                        for (IndexingPreOps op : indexingPreOps) {
                            setStatus("Performing " + op.getName() + "...");

                            currentPeaks = op.process(this, currentPeaks);

                            setStatus("Performing " + op.getName() + "... DONE");

                            fireIndexingPrePerformed(op, getCurrentPeaks());
                        }

                        // Indexing Op
                        setStatus("--- Indexing Operation ---");

                        setStatus("Performing " + indexingOp.getName() + "...");

                        currentSolutions = indexingOp.index(this, currentPeaks);

                        fireIndexingOpPerformed(indexingOp,
                                getCurrentSolutions());

                        setStatus("Performing " + indexingOp.getName()
                                + "... DONE");

                        // Indexing Post Ops
                        setStatus("--- Indexing Post Operations ---");
                        for (IndexingPostOps op : indexingPostOps) {
                            setStatus("Performing " + op.getName() + "...");

                            currentSolutions =
                                    op.process(this, currentSolutions);

                            setStatus("Performing " + op.getName() + "... DONE");

                            fireIndexingPostPerformed(op, getCurrentSolutions());
                        }

                        // Indexing Results Ops
                        setStatus("--- Indexing Results Operations ---");
                        for (IndexingResultsOps op : indexingResultsOps) {
                            setStatus("Calculating " + op.getName() + "...");

                            results = op.calculate(this, currentSolutions);
                            saveResults(results);

                            setStatus("Calculating " + op.getName()
                                    + "... DONE");

                            fireIndexingResultPerformed(op, results);
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



    /**
     * Validates the operations after the deserialization to check that there is
     * not two operations with the same index.
     * 
     * @throws Exception
     *             if two operations have the same index
     */
    @SuppressWarnings("unused")
    @Validate
    private void validate() throws Exception {
        validate(patternPostOps, PatternPostOps.class);
        validate(patternResultsOps, PatternResultsOps.class);

        validate(houghPreOps, HoughPreOps.class);
        validate(houghPostOps, HoughPostOps.class);
        validate(houghResultsOps, HoughResultsOps.class);

        validate(detectionPreOps, DetectionPreOps.class);
        validate(detectionPostOps, DetectionPostOps.class);
        validate(detectionResultsOps, DetectionResultsOps.class);

        validate(identificationPreOps, IdentificationPreOps.class);
        validate(identificationPostOps, IdentificationPostOps.class);
        validate(identificationResultsOps, IdentificationResultsOps.class);

        validate(indexingPreOps, IndexingPreOps.class);
        validate(indexingPostOps, IndexingPostOps.class);
        validate(indexingResultsOps, IndexingResultsOps.class);
    }

}
