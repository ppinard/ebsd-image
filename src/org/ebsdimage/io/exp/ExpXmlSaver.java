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
package org.ebsdimage.io.exp;

import static org.ebsdimage.core.exp.ExpConstants.*;
import static org.ebsdimage.io.exp.ExpXmlTags.ATTR_HEIGHT;
import static org.ebsdimage.io.exp.ExpXmlTags.ATTR_WIDTH;
import static org.ebsdimage.io.exp.ExpXmlTags.TAG_NAME;
import static org.ebsdimage.io.run.RunXmlTags.ATTR_NAME;
import static org.ebsdimage.io.run.RunXmlTags.ATTR_PATH;

import org.ebsdimage.core.exp.Exp;
import org.ebsdimage.core.run.Operation;
import org.ebsdimage.io.PhasesMapXmlSaver;
import org.ebsdimage.io.exp.ops.detection.op.DetectionOpXmlTags;
import org.ebsdimage.io.exp.ops.detection.post.DetectionPostOpsXmlTags;
import org.ebsdimage.io.exp.ops.detection.pre.DetectionPreOpsXmlTags;
import org.ebsdimage.io.exp.ops.detection.results.DetectionResultsOpsXmlTags;
import org.ebsdimage.io.exp.ops.hough.op.HoughOpXmlTags;
import org.ebsdimage.io.exp.ops.hough.post.HoughPostOpsXmlTags;
import org.ebsdimage.io.exp.ops.hough.pre.HoughPreOpsXmlTags;
import org.ebsdimage.io.exp.ops.hough.results.HoughResultsOpsXmlTags;
import org.ebsdimage.io.exp.ops.identification.op.IdentificationOpXmlTags;
import org.ebsdimage.io.exp.ops.identification.post.IdentificationPostOpsXmlTags;
import org.ebsdimage.io.exp.ops.identification.pre.IdentificationPreOpsXmlTags;
import org.ebsdimage.io.exp.ops.identification.results.IdentificationResultsOpsXmlTags;
import org.ebsdimage.io.exp.ops.indexing.op.IndexingOpXmlTags;
import org.ebsdimage.io.exp.ops.indexing.post.IndexingPostOpsXmlTags;
import org.ebsdimage.io.exp.ops.indexing.pre.IndexingPreOpsXmlTags;
import org.ebsdimage.io.exp.ops.indexing.results.IndexingResultsOpsXmlTags;
import org.ebsdimage.io.exp.ops.pattern.op.PatternOpXmlTags;
import org.ebsdimage.io.exp.ops.pattern.post.PatternPostOpsXmlTags;
import org.ebsdimage.io.exp.ops.pattern.results.PatternResultsOpsXmlTags;
import org.ebsdimage.io.run.RunXmlSaver;
import org.jdom.Element;

import ptpshared.io.FileUtil;
import ptpshared.utility.xml.ObjectXml;
import ptpshared.utility.xml.ObjectXmlSaver;
import rmlshared.thread.Reflection;

/**
 * XML saver for an <code>Exp</code>.
 * 
 * @author Philippe T. Pinard
 * 
 */
public class ExpXmlSaver extends RunXmlSaver {

    /**
     * {@inheritDoc}
     * 
     * @see #save(Exp)
     */
    @Override
    public Element save(ObjectXml obj) {
        return save((Exp) obj);
    }



    /**
     * Saves an <code>Exp</code> to an XML <code>Element</code>.
     * 
     * @param exp
     *            an <code>Exp</code>
     * @return an XML <code>Element</code>
     */
    public Element save(Exp exp) {
        Element element = new Element(TAG_NAME);

        /* Attributes and parameters */
        element.setAttribute(ATTR_NAME, exp.getName());
        element.setAttribute(ATTR_PATH, exp.getDir().toString());

        // Dimensions
        element.setAttribute(ATTR_WIDTH, Integer.toString(exp.mmap.width));
        element.setAttribute(ATTR_HEIGHT, Integer.toString(exp.mmap.height));

        // Metadata
        element.addContent(new ExpMetadataXmlSaver().save(exp.mmap
                .getMetadata()));

        // Phases
        element.addContent(new PhasesMapXmlSaver().save(exp.mmap.getPhases()));

        // Save Maps
        element.addContent(new CurrentMapsFileSaverXmlSaver()
                .save(exp.currentMapsSaver));

        /* Operations */
        // Pattern Op
        String tagName = PatternOpXmlTags.TAG_NAME;
        String packageName =
                FileUtil.joinPackageNames(PATTERN_IO_PACKAGE, "op");
        Operation[] ops = exp.getPatternOps();
        element.addContent(createOpsElement(ops, tagName, packageName));

        // Pattern Post Ops
        tagName = PatternPostOpsXmlTags.TAG_NAME;
        packageName = FileUtil.joinPackageNames(PATTERN_IO_PACKAGE, "post");
        ops = exp.getPatternPostOps();
        element.addContent(createOpsElement(ops, tagName, packageName));

        // Pattern Results Ops
        tagName = PatternResultsOpsXmlTags.TAG_NAME;
        packageName = FileUtil.joinPackageNames(PATTERN_IO_PACKAGE, "results");
        ops = exp.getPatternResultsOps();
        element.addContent(createOpsElement(ops, tagName, packageName));

        // Hough Pre Ops
        tagName = HoughPreOpsXmlTags.TAG_NAME;
        packageName = FileUtil.joinPackageNames(HOUGH_IO_PACKAGE, "pre");
        ops = exp.getHoughPreOps();
        element.addContent(createOpsElement(ops, tagName, packageName));

        // Hough Op
        tagName = HoughOpXmlTags.TAG_NAME;
        packageName = FileUtil.joinPackageNames(HOUGH_IO_PACKAGE, "op");
        Operation op = exp.getHoughOp();
        if (op != null)
            element.addContent(createOpElement(op, tagName, packageName));

        // Hough Post Ops
        tagName = HoughPostOpsXmlTags.TAG_NAME;
        packageName = FileUtil.joinPackageNames(HOUGH_IO_PACKAGE, "post");
        ops = exp.getHoughPostOps();
        element.addContent(createOpsElement(ops, tagName, packageName));

        // Hough Results Ops
        tagName = HoughResultsOpsXmlTags.TAG_NAME;
        packageName = FileUtil.joinPackageNames(HOUGH_IO_PACKAGE, "results");
        ops = exp.getHoughResultsOps();
        element.addContent(createOpsElement(ops, tagName, packageName));

        // Detection Pre Ops
        tagName = DetectionPreOpsXmlTags.TAG_NAME;
        packageName = FileUtil.joinPackageNames(DETECTION_IO_PACKAGE, "pre");
        ops = exp.getDetectionPreOps();
        element.addContent(createOpsElement(ops, tagName, packageName));

        // Detection Op
        tagName = DetectionOpXmlTags.TAG_NAME;
        packageName = FileUtil.joinPackageNames(DETECTION_IO_PACKAGE, "op");
        op = exp.getDetectionOp();
        if (op != null)
            element.addContent(createOpElement(op, tagName, packageName));

        // Detection Post Ops
        tagName = DetectionPostOpsXmlTags.TAG_NAME;
        packageName = FileUtil.joinPackageNames(DETECTION_IO_PACKAGE, "post");
        ops = exp.getDetectionPostOps();
        element.addContent(createOpsElement(ops, tagName, packageName));

        // Detection Results Ops
        tagName = DetectionResultsOpsXmlTags.TAG_NAME;
        packageName =
                FileUtil.joinPackageNames(DETECTION_IO_PACKAGE, "results");
        ops = exp.getDetectionResultsOps();
        element.addContent(createOpsElement(ops, tagName, packageName));

        // Identification Pre Ops
        tagName = IdentificationPreOpsXmlTags.TAG_NAME;
        packageName =
                FileUtil.joinPackageNames(IDENTIFICATION_IO_PACKAGE, "pre");
        ops = exp.getIdentificationPreOps();
        element.addContent(createOpsElement(ops, tagName, packageName));

        // Identification Op
        tagName = IdentificationOpXmlTags.TAG_NAME;
        packageName =
                FileUtil.joinPackageNames(IDENTIFICATION_IO_PACKAGE, "op");
        op = exp.getIdentificationOp();
        if (op != null)
            element.addContent(createOpElement(op, tagName, packageName));

        // Identification Post Ops
        tagName = IdentificationPostOpsXmlTags.TAG_NAME;
        packageName =
                FileUtil.joinPackageNames(IDENTIFICATION_IO_PACKAGE, "post");
        ops = exp.getIdentificationPostOps();
        element.addContent(createOpsElement(ops, tagName, packageName));

        // Identification Results Ops
        tagName = IdentificationResultsOpsXmlTags.TAG_NAME;
        packageName =
                FileUtil.joinPackageNames(IDENTIFICATION_IO_PACKAGE, "results");
        ops = exp.getIdentificationResultsOps();
        element.addContent(createOpsElement(ops, tagName, packageName));

        // Indexing Pre Ops
        tagName = IndexingPreOpsXmlTags.TAG_NAME;
        packageName = FileUtil.joinPackageNames(INDEXING_IO_PACKAGE, "pre");
        ops = exp.getIndexingPreOps();
        element.addContent(createOpsElement(ops, tagName, packageName));

        // Indexing Op
        tagName = IndexingOpXmlTags.TAG_NAME;
        packageName = FileUtil.joinPackageNames(INDEXING_IO_PACKAGE, "op");
        op = exp.getIndexingOp();
        if (op != null)
            element.addContent(createOpElement(op, tagName, packageName));

        // Indexing Post Ops
        tagName = IndexingPostOpsXmlTags.TAG_NAME;
        packageName = FileUtil.joinPackageNames(INDEXING_IO_PACKAGE, "post");
        ops = exp.getIndexingPostOps();
        element.addContent(createOpsElement(ops, tagName, packageName));

        // Indexing Results Ops
        tagName = IndexingResultsOpsXmlTags.TAG_NAME;
        packageName = FileUtil.joinPackageNames(INDEXING_IO_PACKAGE, "results");
        ops = exp.getIndexingResultsOps();
        element.addContent(createOpsElement(ops, tagName, packageName));

        return element;
    }



    @Override
    protected ObjectXmlSaver getOperationSaver(Operation op, String packageName) {
        String opName = op.getName();
        String classLoaderName =
                FileUtil.joinPackageNames(packageName, opName + "XmlSaver");
        return (ObjectXmlSaver) Reflection.newInstance(classLoaderName);
    }

}
