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
package org.ebsdimage.cui;

import java.io.File;
import java.io.IOException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.ebsdimage.core.exp.Exp;
import org.ebsdimage.core.exp.ExpUtil;
import org.ebsdimage.core.exp.ops.pattern.op.PatternSmpLoader;
import org.ebsdimage.io.SmpCreator;
import org.ebsdimage.io.exp.ExpLoader;
import org.ebsdimage.io.exp.ExpSaver;

import ptpshared.cui.BaseCUI;
import rmlshared.cui.ErrorDialog;
import rmlshared.cui.MessageDialog;
import rmlshared.io.FileUtil;
import edu.umd.cs.findbugs.annotations.CheckForNull;

/**
 * Command line interface to split an experiment into smaller experiments.
 * 
 * @author Philippe T. Pinard
 */
public class ExpSplit extends BaseCUI {
    /**
     * Main entry.
     * 
     * @param args
     *            arguments passed to the program
     * @throws IOException
     *             if an exception occurs while executing the program
     */
    public static void main(String[] args) throws IOException {
        new ExpSplit().parse(args);
    }



    /**
     * Returns the command line options.
     * 
     * @return command line options
     */
    @Override
    protected Options getOptions() {
        Options options = super.getOptions();

        options.addOption(new Option("d", "dir", true,
                "Base directory for the experiment"));
        options.addOption(new Option("s", "split", true,
                "Split count (i.e. number of smaller experiments wanted)"));

        return options;
    }



    /**
     * Parses the command line arguments.
     * 
     * @param args
     *            command line arguments
     * @throws IOException
     *             if an error occurs while executing
     */
    private void parse(String[] args) throws IOException {
        CommandLine cmdLine = parseArguments(args);
        if (cmdLine == null)
            return;

        // Logger
        setLogger(cmdLine);

        // Parse input XML file
        File file = getInputFile(cmdLine);
        if (file == null)
            return;

        // Parse base directory
        File dir = getDir(cmdLine);
        if (dir == null)
            return;

        // Parse split count
        int splitCount = getSplitCount(cmdLine);
        if (splitCount < 1)
            return;

        // Load experiment
        MessageDialog.show("Loading experiment...");

        Exp exp = null;
        try {
            exp = new ExpLoader().load(file);
        } catch (IOException ex) {
            ErrorDialog.show(ex.getMessage());
            return;
        }

        MessageDialog.show("Loading experiment... DONE");

        // Split
        MessageDialog.show("Splitting experiment...");

        Exp[] exps;
        try {
            exps = ExpUtil.splitExp(exp, splitCount);
        } catch (Exception ex) {
            ErrorDialog.show(ex.getMessage());
            return;
        }

        MessageDialog.show("Splitting experiment... DONE");

        // Save experiment in directory
        MessageDialog.show("Saving smaller experiments...");

        File splitExpDir;
        Exp splitExp;
        ExpSaver saver = new ExpSaver();
        SmpCreator smpCreator = new SmpCreator();

        for (int i = 0; i < exps.length; i++) {
            MessageDialog.show("Saving split experiment " + (i + 1) + "...");

            splitExp = exps[i];

            // Experiment directory
            splitExpDir = new File(dir, splitExp.getName());

            // Create directory
            if (!splitExpDir.mkdirs()) {
                ErrorDialog.show("Cannot create experiment directory: "
                        + splitExpDir);
                return;
            }

            // Set new working directory
            splitExp.setDir(splitExpDir);

            // Save
            File splitExpFile = new File(splitExpDir, splitExp.getName());
            splitExpFile = FileUtil.setExtension(splitExpFile, "xml");
            saver.save(splitExp, splitExpFile);

            MessageDialog.show("Saving split experiment " + (i + 1)
                    + "... DONE");

            // Smp split
            if (splitExp.getPatternOp() instanceof PatternSmpLoader) {
                MessageDialog.show("Saving split smp " + (i + 1) + "...");

                PatternSmpLoader op =
                        (PatternSmpLoader) splitExp.getPatternOp();

                // Start and end index
                int startIndex = op.startIndex;
                int endIndex = op.startIndex + op.size - 1;

                // Source smp file
                File srcSmpFile;
                try {
                    srcSmpFile = op.getFile(dir);
                } catch (IOException ex) {
                    ErrorDialog.show("Cannot find file (" + op.filename
                            + ") in the specified "
                            + "directory or working directory.");
                    return;
                }

                // Destination smp file
                File destSmpFile = new File(splitExpDir, op.filename);

                // Extract
                smpCreator.extract(srcSmpFile, startIndex, endIndex,
                        destSmpFile);

                MessageDialog.show("Saving split smp " + (i + 1) + "... DONE");
            }
        }

        MessageDialog.show("Saving smaller experiments... DONE");
    }



    /**
     * Returns the experiment input file. The specified file is validated.
     * 
     * @param cmdLine
     *            command line arguments
     * @return experiment input file
     * @throws IOException
     *             if an error occurs while validating the file
     */
    @CheckForNull
    private File getInputFile(CommandLine cmdLine) throws IOException {
        if (cmdLine.getArgs().length != 1) {
            ErrorDialog.show("Please specify only one file ("
                    + cmdLine.getArgs().length + "file(s) were given).");
            return null;
        }

        File file = new File(cmdLine.getArgs()[0]);

        if (!file.exists()) {
            ErrorDialog.show("Specified file does not exist.");
            return null;
        }

        if (!"xml".equalsIgnoreCase(FileUtil.getExtension(file))) {
            ErrorDialog.show("The specified file must have an XML extension.");
            return null;
        }

        MessageDialog.show("Loading xml file: " + file);
        return file;
    }



    /**
     * Returns the base directory where to save the smaller experiments.
     * 
     * @param cmdLine
     *            command line arguments
     * @return directory
     * @throws IOException
     *             if an error occurs while validating the file
     */
    @CheckForNull
    private File getDir(CommandLine cmdLine) throws IOException {
        if (cmdLine.hasOption("dir")) {
            if (cmdLine.getOptionValue("dir") == null) {
                ErrorDialog.show("Please specify a base directory.");
                return null;
            } else {
                File dir = new File(cmdLine.getOptionValue("dir"));

                // Create directory if it doesn't exist
                if (!dir.exists())
                    if (!dir.mkdirs()) {
                        ErrorDialog.show("Cannot create directory.");
                        return null;
                    }

                MessageDialog.show("Base directory is: " + dir);
                return dir;
            }
        }

        return null;
    }



    /**
     * Returns the split count from the arguments.
     * 
     * @param cmdLine
     *            command line arguments
     * @return split count
     * @throws IOException
     *             if an error occurs
     */
    private int getSplitCount(CommandLine cmdLine) throws IOException {
        if (cmdLine.hasOption("split")) {
            if (cmdLine.getOptionValue("split") == null) {
                ErrorDialog.show("Please specify a number of split.");
                return -1;
            } else {
                int splitCount =
                        Integer.parseInt(cmdLine.getOptionValue("split"));
                MessageDialog.show("Split count: " + splitCount);
                return splitCount;
            }
        } else {
            ErrorDialog.show("The split count is required.");
            return -1;
        }
    }



    /**
     * Prints the help of the program.
     */
    @Override
    protected void printHelp() {
        HelpFormatter formatter = new HelpFormatter();

        String header = "ExpSplit -- Load, split and save experiments";
        formatter.printHelp("ExpSplit [options] <xmlFile>", header,
                getOptions(), "");
    }
}
