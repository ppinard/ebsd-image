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

import static org.ebsdimage.io.exp.ExpXmlTags.ATTR_HEIGHT;
import static org.ebsdimage.io.exp.ExpXmlTags.ATTR_WIDTH;
import static org.ebsdimage.io.exp.ExpXmlTags.TAG_NAME;
import static org.ebsdimage.io.run.RunXmlTags.ATTR_NAME;
import static org.ebsdimage.io.run.RunXmlTags.ATTR_PATH;

import org.ebsdimage.core.exp.CurrentMapsFileSaver;
import org.ebsdimage.core.exp.ExpTester;
import org.ebsdimage.core.exp.ops.detection.op.DetectionOpMock;
import org.ebsdimage.core.exp.ops.detection.post.DetectionPostOpsMock;
import org.ebsdimage.core.exp.ops.detection.pre.DetectionPreOpsMock;
import org.ebsdimage.core.exp.ops.detection.results.DetectionResultsOpsMock;
import org.ebsdimage.core.exp.ops.hough.op.HoughOpMock;
import org.ebsdimage.core.exp.ops.hough.post.HoughPostOpsMock;
import org.ebsdimage.core.exp.ops.hough.pre.HoughPreOpsMock;
import org.ebsdimage.core.exp.ops.hough.results.HoughResultsOpsMock;
import org.ebsdimage.core.exp.ops.identification.op.IdentificationOpMock;
import org.ebsdimage.core.exp.ops.identification.post.IdentificationPostOpsMock;
import org.ebsdimage.core.exp.ops.identification.pre.IdentificationPreOpsMock;
import org.ebsdimage.core.exp.ops.identification.results.IdentificationResultsOpsMock;
import org.ebsdimage.core.exp.ops.indexing.op.IndexingOpMock;
import org.ebsdimage.core.exp.ops.indexing.post.IndexingPostOpsMock;
import org.ebsdimage.core.exp.ops.indexing.pre.IndexingPreOpsMock;
import org.ebsdimage.core.exp.ops.indexing.results.IndexingResultsOpsMock;
import org.ebsdimage.core.exp.ops.pattern.op.PatternOpMock;
import org.ebsdimage.core.exp.ops.pattern.post.PatternPostOpsMock;
import org.ebsdimage.core.exp.ops.pattern.results.PatternResultsOpsMock;
import org.ebsdimage.io.PhasesMapXmlSaver;
import org.ebsdimage.io.exp.ops.detection.op.DetectionOpMockXmlSaver;
import org.ebsdimage.io.exp.ops.detection.op.DetectionOpXmlTags;
import org.ebsdimage.io.exp.ops.detection.post.DetectionPostOpsMockXmlSaver;
import org.ebsdimage.io.exp.ops.detection.post.DetectionPostOpsXmlTags;
import org.ebsdimage.io.exp.ops.detection.pre.DetectionPreOpsMockXmlSaver;
import org.ebsdimage.io.exp.ops.detection.pre.DetectionPreOpsXmlTags;
import org.ebsdimage.io.exp.ops.detection.results.DetectionResultsOpsMockXmlSaver;
import org.ebsdimage.io.exp.ops.detection.results.DetectionResultsOpsXmlTags;
import org.ebsdimage.io.exp.ops.hough.op.HoughOpMockXmlSaver;
import org.ebsdimage.io.exp.ops.hough.op.HoughOpXmlTags;
import org.ebsdimage.io.exp.ops.hough.post.HoughPostOpsMockXmlSaver;
import org.ebsdimage.io.exp.ops.hough.post.HoughPostOpsXmlTags;
import org.ebsdimage.io.exp.ops.hough.pre.HoughPreOpsMockXmlSaver;
import org.ebsdimage.io.exp.ops.hough.pre.HoughPreOpsXmlTags;
import org.ebsdimage.io.exp.ops.hough.results.HoughResultsOpsMockXmlSaver;
import org.ebsdimage.io.exp.ops.hough.results.HoughResultsOpsXmlTags;
import org.ebsdimage.io.exp.ops.identification.op.IdentificationOpMockXmlSaver;
import org.ebsdimage.io.exp.ops.identification.op.IdentificationOpXmlTags;
import org.ebsdimage.io.exp.ops.identification.post.IdentificationPostOpsMockXmlSaver;
import org.ebsdimage.io.exp.ops.identification.post.IdentificationPostOpsXmlTags;
import org.ebsdimage.io.exp.ops.identification.pre.IdentificationPreOpsMockXmlSaver;
import org.ebsdimage.io.exp.ops.identification.pre.IdentificationPreOpsXmlTags;
import org.ebsdimage.io.exp.ops.identification.results.IdentificationResultsOpsMockXmlSaver;
import org.ebsdimage.io.exp.ops.identification.results.IdentificationResultsOpsXmlTags;
import org.ebsdimage.io.exp.ops.indexing.op.IndexingOpMockXmlSaver;
import org.ebsdimage.io.exp.ops.indexing.op.IndexingOpXmlTags;
import org.ebsdimage.io.exp.ops.indexing.post.IndexingPostOpsMockXmlSaver;
import org.ebsdimage.io.exp.ops.indexing.post.IndexingPostOpsXmlTags;
import org.ebsdimage.io.exp.ops.indexing.pre.IndexingPreOpsMockXmlSaver;
import org.ebsdimage.io.exp.ops.indexing.pre.IndexingPreOpsXmlTags;
import org.ebsdimage.io.exp.ops.indexing.results.IndexingResultsOpsMockXmlSaver;
import org.ebsdimage.io.exp.ops.indexing.results.IndexingResultsOpsXmlTags;
import org.ebsdimage.io.exp.ops.pattern.op.PatternOpMockXmlSaver;
import org.ebsdimage.io.exp.ops.pattern.op.PatternOpXmlTags;
import org.ebsdimage.io.exp.ops.pattern.post.PatternPostOpsMockXmlSaver;
import org.ebsdimage.io.exp.ops.pattern.post.PatternPostOpsXmlTags;
import org.ebsdimage.io.exp.ops.pattern.results.PatternResultsOpsMockXmlSaver;
import org.ebsdimage.io.exp.ops.pattern.results.PatternResultsOpsXmlTags;
import org.jdom.Element;

public abstract class ExpXmlTester extends ExpTester {

    public static Element createElement() {
        Element element = new Element(TAG_NAME);

        element.setAttribute(ATTR_NAME, "ExpTester");
        element.setAttribute(ATTR_PATH, expPath.getAbsolutePath());

        element.setAttribute(ATTR_WIDTH, Integer.toString(2));
        element.setAttribute(ATTR_HEIGHT, Integer.toString(1));

        // Save Maps
        element.addContent(new CurrentMapsFileSaverXmlSaver().save(new CurrentMapsFileSaver(
                true, true, true, true, true, true)));

        // Phases
        element.addContent(new PhasesMapXmlSaver().save(ExpTester.createPhases()));

        // Metadata
        element.addContent(new ExpMetadataXmlSaver().save(ExpTester.createMetadata()));

        /* Operations */
        Element parent;

        // Pattern Op
        parent = new Element(PatternOpXmlTags.TAG_NAME);
        parent.addContent(new PatternOpMockXmlSaver().save(new PatternOpMock(2)).setText(
                "1"));
        element.addContent(parent);

        // Pattern Post Ops
        parent = new Element(PatternPostOpsXmlTags.TAG_NAME);
        parent.addContent(new PatternPostOpsMockXmlSaver().save(new PatternPostOpsMock()));
        element.addContent(parent);

        // Pattern Results Ops
        parent = new Element(PatternResultsOpsXmlTags.TAG_NAME);
        parent.addContent(new PatternResultsOpsMockXmlSaver().save(new PatternResultsOpsMock()));
        element.addContent(parent);

        // Hough Pre Ops
        parent = new Element(HoughPreOpsXmlTags.TAG_NAME);
        parent.addContent(new HoughPreOpsMockXmlSaver().save(new HoughPreOpsMock()));
        element.addContent(parent);

        // Hough Op
        parent = new Element(HoughOpXmlTags.TAG_NAME);
        parent.addContent(new HoughOpMockXmlSaver().save(new HoughOpMock()));
        element.addContent(parent);

        // Hough Post Ops
        parent = new Element(HoughPostOpsXmlTags.TAG_NAME);
        parent.addContent(new HoughPostOpsMockXmlSaver().save(new HoughPostOpsMock()));
        element.addContent(parent);

        // Hough Results Ops
        parent = new Element(HoughResultsOpsXmlTags.TAG_NAME);
        parent.addContent(new HoughResultsOpsMockXmlSaver().save(new HoughResultsOpsMock()));
        element.addContent(parent);

        // Detection Pre Ops
        parent = new Element(DetectionPreOpsXmlTags.TAG_NAME);
        parent.addContent(new DetectionPreOpsMockXmlSaver().save(new DetectionPreOpsMock()));
        element.addContent(parent);

        // Detection Op
        parent = new Element(DetectionOpXmlTags.TAG_NAME);
        parent.addContent(new DetectionOpMockXmlSaver().save(new DetectionOpMock()));
        element.addContent(parent);

        // Detection Post Ops
        parent = new Element(DetectionPostOpsXmlTags.TAG_NAME);
        parent.addContent(new DetectionPostOpsMockXmlSaver().save(new DetectionPostOpsMock()));
        element.addContent(parent);

        // Detection Results Ops
        parent = new Element(DetectionResultsOpsXmlTags.TAG_NAME);
        parent.addContent(new DetectionResultsOpsMockXmlSaver().save(new DetectionResultsOpsMock()));
        element.addContent(parent);

        // Identification Pre Ops
        parent = new Element(IdentificationPreOpsXmlTags.TAG_NAME);
        parent.addContent(new IdentificationPreOpsMockXmlSaver().save(new IdentificationPreOpsMock()));
        element.addContent(parent);

        // Identification Op
        parent = new Element(IdentificationOpXmlTags.TAG_NAME);
        parent.addContent(new IdentificationOpMockXmlSaver().save(new IdentificationOpMock()));
        element.addContent(parent);

        // Identification Post Ops
        parent = new Element(IdentificationPostOpsXmlTags.TAG_NAME);
        parent.addContent(new IdentificationPostOpsMockXmlSaver().save(new IdentificationPostOpsMock()));
        element.addContent(parent);

        // Identification Results Ops
        parent = new Element(IdentificationResultsOpsXmlTags.TAG_NAME);
        parent.addContent(new IdentificationResultsOpsMockXmlSaver().save(new IdentificationResultsOpsMock()));
        element.addContent(parent);

        // Indexing Pre Ops
        parent = new Element(IndexingPreOpsXmlTags.TAG_NAME);
        parent.addContent(new IndexingPreOpsMockXmlSaver().save(new IndexingPreOpsMock()));
        element.addContent(parent);

        // Indexing Op
        parent = new Element(IndexingOpXmlTags.TAG_NAME);
        parent.addContent(new IndexingOpMockXmlSaver().save(new IndexingOpMock()));
        element.addContent(parent);

        // Indexing Post Ops
        parent = new Element(IndexingPostOpsXmlTags.TAG_NAME);
        parent.addContent(new IndexingPostOpsMockXmlSaver().save(new IndexingPostOpsMock()));
        element.addContent(parent);

        // Indexing Results Ops
        parent = new Element(IndexingResultsOpsXmlTags.TAG_NAME);
        parent.addContent(new IndexingResultsOpsMockXmlSaver().save(new IndexingResultsOpsMock()));
        element.addContent(parent);

        return element;
    }

}
