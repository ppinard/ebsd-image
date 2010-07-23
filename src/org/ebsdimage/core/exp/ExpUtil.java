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

import org.ebsdimage.core.EbsdMMap;
import org.ebsdimage.core.exp.ops.pattern.op.PatternFilesLoader;
import org.ebsdimage.core.exp.ops.pattern.op.PatternOp;
import org.ebsdimage.core.exp.ops.pattern.op.PatternSmpLoader;
import org.ebsdimage.core.run.Operation;
import org.ebsdimage.io.SmpInputStream;

import rmlshared.io.FileUtil;

/**
 * Utilities to create experiments.
 * 
 * @author Philippe T. Pinard
 */
public class ExpUtil {

    /**
     * Returns an array of <code>Exp</code>s created from the specified
     * <code>ExpsGenerator</code> and an array of <code>PatternOp</code>s.
     * 
     * @param mmap
     *            base <code>EbsdMMap</code> for all the experiments
     * @param saveMaps
     *            <code>SaveMaps</code> for all the experiments
     * @param generator
     *            experiment generator to generate combinations of
     *            <code>Operation</code>
     * @param inputs
     *            array of <code>PatternOp</code>
     * @return an array of <code>Exp</code>
     */
    public static Exp[] createExps(EbsdMMap mmap, CurrentMapsSaver saveMaps,
            ExpsGenerator generator, PatternOp[] inputs) {
        if (inputs.length < 1)
            throw new IllegalArgumentException(
                    "At least one input must be defined in the inputs.");

        ArrayList<Operation[]> combsOps = generator.getCombinationsOperations();

        Exp[] exps = new Exp[combsOps.size()];

        int i = 0;
        for (Operation[] combOps : combsOps) {
            // Merge operations from combination and inputs
            ArrayList<Operation> ops =
                    new ArrayList<Operation>(Arrays.asList(combOps));
            ops.addAll(Arrays.asList(inputs));

            // Create an experiment with the operations
            Exp exp =
                    new Exp(mmap, ops.toArray(new Operation[ops.size()]),
                            saveMaps);

            exps[i] = exp;
            i++;
        }

        return exps;
    }



    /**
     * Creates pattern operations from an smp file. For each file in the smp, a
     * <code>PatternSmpFile</code> is created.
     * 
     * @param smpFile
     *            smp file
     * @return array of operations
     * @throws IOException
     *             if an error occurs while reading the smp file
     */
    public static PatternSmpLoader createPatternOpFromSmp(File smpFile)
            throws IOException {
        SmpInputStream smp = new SmpInputStream(smpFile);

        return new PatternSmpLoader(smp.getStartIndex(), smp.getMapCount(),
                smpFile);
    }



    /**
     * Creates pattern operations from all the jpg and bmp files inside the
     * specified directory. For each file, a <code>PatternFileLoader</code> is
     * created.
     * 
     * @param dir
     *            directory where to search for jpg and bmp files
     * @return array of operations
     */
    public static PatternFilesLoader createPatternOpFromDir(File dir) {
        File[] jpgFiles = FileUtil.listFilesOnly(dir, "*.jpg");
        File[] bmpFiles = FileUtil.listFilesOnly(dir, "*.bmp");

        File[] files = new File[jpgFiles.length + bmpFiles.length];

        System.arraycopy(jpgFiles, 0, files, 0, jpgFiles.length);
        System.arraycopy(bmpFiles, 0, files, jpgFiles.length, bmpFiles.length);

        return new PatternFilesLoader(0, files);
    }



    /**
     * Splits a specified experiment into smaller experiments.
     * 
     * @param exp
     *            experiment to be split
     * @param splitCount
     *            number of smaller experiments wanted
     * @return array of the smaller experiments
     * @throws NullPointerException
     *             if the experiment is null
     * @throws IllegalArgumentException
     *             if the split count is less or equal to zero
     * @throws IllegalArgumentException
     *             if the split count exceeds the number of pattern operations
     *             in the experiment
     */
    public static Exp[] splitExp(Exp exp, int splitCount) {
        if (exp == null)
            throw new NullPointerException("Experiment cannot be null.");
        if (splitCount < 1)
            throw new IllegalArgumentException("The split count (" + splitCount
                    + ") must be greater than 0.");
        if (splitCount > exp.getPatternOp().size)
            throw new IllegalArgumentException("The split count (" + splitCount
                    + ") cannot be greater than the "
                    + "number of pattern operations in the experiment ("
                    + exp.getPatternOp().size + ").");

        ArrayList<Exp> exps = new ArrayList<Exp>();

        // List all the operations except PatternOp
        ArrayList<Operation> otherOps = new ArrayList<Operation>();
        for (Operation op : exp.getAllOperations())
            if (!(op instanceof PatternOp))
                otherOps.add(op);

        // Calculate the number of pattern operation in each split
        int originalSize = exp.getPatternOp().size;
        int size = (int) Math.ceil((double) originalSize / splitCount);

        PatternOp patternOp;
        ArrayList<Operation> ops = new ArrayList<Operation>();
        int startIndex;
        int endIndex;
        Exp splitExp;
        for (int n = 0; n < splitCount; n++) {
            // Lower and upper bound of the copy range
            startIndex = Math.min(n * size, originalSize);
            endIndex = Math.min(n * size + size, originalSize) - 1;

            if (startIndex > endIndex)
                continue;

            // Pattern operation for this split
            patternOp = exp.getPatternOp().split(startIndex, endIndex);

            // Combine pattern operations and other operations
            ops.clear();
            ops.addAll(otherOps);
            ops.add(patternOp);

            // Create and add new experiment to the array
            splitExp =
                    new Exp(exp.mmap.duplicate(),
                            ops.toArray(new Operation[0]), exp.currentMapsSaver);
            splitExp.setName(exp.getName() + "_" + n);
            exps.add(splitExp);
        }

        return exps.toArray(new Exp[0]);
    }
}
