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

import java.io.File;
import java.util.ArrayList;

import org.ebsdimage.core.exp.CurrentMapsFileSaver;
import org.ebsdimage.core.exp.Exp;
import org.ebsdimage.core.exp.ExpMetadata;
import org.ebsdimage.core.run.Operation;
import org.ebsdimage.core.run.Run;
import org.ebsdimage.io.PhasesMapXmlLoader;
import org.ebsdimage.io.PhasesMapXmlTags;
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
import org.ebsdimage.io.run.RunXmlLoader;
import org.jdom.Element;
import org.jdom.IllegalNameException;

import ptpshared.io.FileUtil;
import ptpshared.utility.xml.JDomUtil;
import ptpshared.utility.xml.ObjectXmlLoader;
import rmlshared.thread.Reflection;
import crystallography.core.Crystal;

/**
 * XML loader for an <code>Exp</code>.
 * 
 * @author Philippe T. Pinard
 */
public class ExpXmlLoader extends RunXmlLoader {

    /**
     * Loads an <code>Exp</code> from an XML <code>Element</code>.
     * 
     * @param element
     *            an XML <code>Element</code>
     * @return an <code>Exp</code>
     * @throws IllegalNameException
     *             if the <code>Element</code> tag name is incorrect.
     */
    @Override
    public Exp load(Element element) {
        if (!element.getName().equals(TAG_NAME))
            throw new IllegalNameException("Name of the element should be "
                    + TAG_NAME + " not " + element.getName() + ".");

        // Metadata
        Element child = element.getChild(ExpMetadataXmlTags.TAG_NAME);

        ExpMetadata metadata;
        if (child != null)
            metadata = new ExpMetadataXmlLoader().load(child);
        else
            metadata = new ExpMetadata();

        // Phases
        child = element.getChild(PhasesMapXmlTags.TAG_PHASES);

        Crystal[] phases;
        if (child != null)
            phases = new PhasesMapXmlLoader().load(child);
        else
            phases = new Crystal[0];

        // Save maps
        child = element.getChild(CurrentMapsFileSaverXmlTags.TAG_NAME);

        CurrentMapsFileSaver saveMaps;
        if (child != null)
            saveMaps = new CurrentMapsFileSaverXmlLoader().load(child);
        else
            saveMaps = new CurrentMapsFileSaver();

        // Name
        String name =
                JDomUtil.getStringFromAttributeDefault(element, ATTR_NAME,
                        Run.DEFAULT_NAME);

        // Path
        File path =
                new File(JDomUtil.getStringFromAttributeDefault(element,
                        ATTR_PATH, Run.DEFAULT_DIR.toString()));

        // Dimensions
        int width = JDomUtil.getIntegerFromAttribute(element, ATTR_WIDTH);
        int height = JDomUtil.getIntegerFromAttribute(element, ATTR_HEIGHT);

        /* Operations */
        ArrayList<Operation> ops = new ArrayList<Operation>();

        // Pattern Op
        String tagName = PatternOpXmlTags.TAG_NAME;
        String packageName =
                FileUtil.joinPackageNames(PATTERN_IO_PACKAGE, "op");
        ops.add(loadOperation(element, tagName, packageName));

        // Pattern Post Ops
        tagName = PatternPostOpsXmlTags.TAG_NAME;
        packageName = FileUtil.joinPackageNames(PATTERN_IO_PACKAGE, "post");
        ops.addAll(loadOperations(element, tagName, packageName));

        // Pattern Results Ops
        tagName = PatternResultsOpsXmlTags.TAG_NAME;
        packageName = FileUtil.joinPackageNames(PATTERN_IO_PACKAGE, "results");
        ops.addAll(loadOperations(element, tagName, packageName));

        // Hough Pre Ops
        tagName = HoughPreOpsXmlTags.TAG_NAME;
        packageName = FileUtil.joinPackageNames(HOUGH_IO_PACKAGE, "pre");
        ops.addAll(loadOperations(element, tagName, packageName));

        // Hough Op
        tagName = HoughOpXmlTags.TAG_NAME;
        packageName = FileUtil.joinPackageNames(HOUGH_IO_PACKAGE, "op");
        ops.add(loadOperation(element, tagName, packageName));

        // Hough Post Ops
        tagName = HoughPostOpsXmlTags.TAG_NAME;
        packageName = FileUtil.joinPackageNames(HOUGH_IO_PACKAGE, "post");
        ops.addAll(loadOperations(element, tagName, packageName));

        // Hough Results Ops
        tagName = HoughResultsOpsXmlTags.TAG_NAME;
        packageName = FileUtil.joinPackageNames(HOUGH_IO_PACKAGE, "results");
        ops.addAll(loadOperations(element, tagName, packageName));

        // Detection Pre Ops
        tagName = DetectionPreOpsXmlTags.TAG_NAME;
        packageName = FileUtil.joinPackageNames(DETECTION_IO_PACKAGE, "pre");
        ops.addAll(loadOperations(element, tagName, packageName));

        // Detection Op
        tagName = DetectionOpXmlTags.TAG_NAME;
        packageName = FileUtil.joinPackageNames(DETECTION_IO_PACKAGE, "op");
        ops.add(loadOperation(element, tagName, packageName));

        // Detection Post Ops
        tagName = DetectionPostOpsXmlTags.TAG_NAME;
        packageName = FileUtil.joinPackageNames(DETECTION_IO_PACKAGE, "post");
        ops.addAll(loadOperations(element, tagName, packageName));

        // Detection Results Ops
        tagName = DetectionResultsOpsXmlTags.TAG_NAME;
        packageName =
                FileUtil.joinPackageNames(DETECTION_IO_PACKAGE, "results");
        ops.addAll(loadOperations(element, tagName, packageName));

        // Identification Pre Ops
        tagName = IdentificationPreOpsXmlTags.TAG_NAME;
        packageName =
                FileUtil.joinPackageNames(IDENTIFICATION_IO_PACKAGE, "pre");
        ops.addAll(loadOperations(element, tagName, packageName));

        // Identification Op
        tagName = IdentificationOpXmlTags.TAG_NAME;
        packageName =
                FileUtil.joinPackageNames(IDENTIFICATION_IO_PACKAGE, "op");
        ops.add(loadOperation(element, tagName, packageName));

        // Identification Post Ops
        tagName = IdentificationPostOpsXmlTags.TAG_NAME;
        packageName =
                FileUtil.joinPackageNames(IDENTIFICATION_IO_PACKAGE, "post");
        ops.addAll(loadOperations(element, tagName, packageName));

        // Identification Results Ops
        tagName = IdentificationResultsOpsXmlTags.TAG_NAME;
        packageName =
                FileUtil.joinPackageNames(IDENTIFICATION_IO_PACKAGE, "results");
        ops.addAll(loadOperations(element, tagName, packageName));

        // Indexing Pre Ops
        tagName = IndexingPreOpsXmlTags.TAG_NAME;
        packageName = FileUtil.joinPackageNames(INDEXING_IO_PACKAGE, "pre");
        ops.addAll(loadOperations(element, tagName, packageName));

        // Indexing Op
        tagName = IndexingOpXmlTags.TAG_NAME;
        packageName = FileUtil.joinPackageNames(INDEXING_IO_PACKAGE, "op");
        ops.add(loadOperation(element, tagName, packageName));

        // Indexing Post Ops
        tagName = IndexingPostOpsXmlTags.TAG_NAME;
        packageName = FileUtil.joinPackageNames(INDEXING_IO_PACKAGE, "post");
        ops.addAll(loadOperations(element, tagName, packageName));

        // Indexing Results Ops
        tagName = IndexingResultsOpsXmlTags.TAG_NAME;
        packageName = FileUtil.joinPackageNames(INDEXING_IO_PACKAGE, "results");
        ops.addAll(loadOperations(element, tagName, packageName));

        // Constructor experiment
        Exp exp =
                new Exp(width, height, metadata, phases,
                        ops.toArray(new Operation[ops.size()]), saveMaps);
        exp.setName(name);
        exp.setDir(path);

        return exp;
    }



    @Override
    protected ObjectXmlLoader getOperationLoader(Element child,
            String packageName) {
        String childName = child.getName();
        String classLoaderName =
                FileUtil.joinPackageNames(packageName, childName + "XmlLoader");
        return (ObjectXmlLoader) Reflection.newInstance(classLoaderName);
    }

}
