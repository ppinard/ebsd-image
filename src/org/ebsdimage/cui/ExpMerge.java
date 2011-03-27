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
package org.ebsdimage.cui;

import java.io.File;
import java.io.IOException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.ebsdimage.core.exp.ExpMMap;
import org.ebsdimage.io.exp.ExpMMapLoader;
import org.ebsdimage.io.exp.ExpMMapSaver;

import ptpshared.cui.BaseCUI;
import rmlimage.core.Map;
import rmlimage.core.MapMath;
import rmlimage.module.real.core.RealMap;
import rmlshared.cui.ErrorDialog;
import rmlshared.cui.MessageDialog;
import rmlshared.io.FileUtil;
import rmlshared.util.ArrayList;
import edu.umd.cs.findbugs.annotations.CheckForNull;

/**
 * Command line interface to merge experiment multimap results that were split
 * using <code>ExpSplit</code>.
 * 
 * @author Philippe T. Pinard
 */
public class ExpMerge extends BaseCUI {

    static {
        MapMath.addHandler(rmlimage.module.multi.core.MapMath.class);
        MapMath.addHandler(rmlimage.module.real.core.MapMath.class);
        MapMath.addHandler(org.ebsdimage.core.MapMath.class);
    }



    /**
     * Main entry.
     * 
     * @param args
     *            arguments passed to the program
     * @throws IOException
     *             if an exception occurs while executing the program
     */
    public static void main(String[] args) throws IOException {
        new ExpMerge().parse(args);
    }



    /**
     * Returns the directory containing all the experiments.
     * 
     * @param cmdLine
     *            command line arguments
     * @return base directory for the experiments
     * @throws IOException
     *             if an error occurs while validating the file
     */
    @CheckForNull
    private File getInputDir(CommandLine cmdLine) throws IOException {
        if (cmdLine.getArgs().length != 1) {
            console.println("Please specify only the base directory ("
                    + cmdLine.getArgs().length + "directory(ies) were given).");
            return null;
        }

        File dir = new File(cmdLine.getArgs()[0]);

        if (!dir.exists()) {
            console.println("Specified directory does not exist.");
            return null;
        }

        if (!dir.isDirectory()) {
            console.println("The specified path is not a directory.");
            return null;
        }

        MessageDialog.show("Base directory is: " + dir);
        return dir;
    }



    /**
     * Returns the command line options.
     * 
     * @return command line options
     */
    @Override
    protected Options getOptions() {
        Options options = super.getOptions();

        options.addOption(new Option("o", "output", true,
                "Output multimap results file"));

        return options;
    }



    /**
     * Returns the output multimap results file.
     * 
     * @param cmdLine
     *            command line arguments
     * @return results file
     * @throws IOException
     *             if an error occurs while validating the file
     */
    @CheckForNull
    private File getOutputFile(CommandLine cmdLine) throws IOException {
        if (cmdLine.hasOption("output")) {
            if (cmdLine.getOptionValue("output") == null) {
                ErrorDialog.show("Please specify an output file.");
                return null;
            } else {
                File file = new File(cmdLine.getOptionValue("output"));

                file = FileUtil.setExtension(file, "zip");

                MessageDialog.show("Output file is: " + file);
                return file;
            }
        } else {
            ErrorDialog.show("The output file is required.");
            return null;
        }
    }



    /**
     * Lists all the experiment multimap files located in the directories inside
     * the specified base directory. The method only searches in one level deep
     * from the base directory.
     * 
     * @param dir
     *            base directory
     * @return experiment multimap files
     */
    private File[] listExpMMapFiles(File dir) {
        ArrayList<File> expDirs = new ArrayList<File>();

        ExpMMapLoader loader = new ExpMMapLoader();
        for (File expDir : FileUtil.listDirectories(dir)) {
            // List all files in directory
            for (File file : FileUtil.listFilesOnly(expDir))
                // Look for Exp multimap zip files
                if (loader.canLoad(file))
                    expDirs.add(file);
        }

        return expDirs.toArray(new File[0]);
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

        // Parse input base directory
        File dir = getInputDir(cmdLine);
        if (dir == null)
            return;

        // Parse output file
        File outputFile = getOutputFile(cmdLine);
        if (outputFile == null)
            return;

        // List experiment multimap files
        MessageDialog.show("Listing experiment multimaps...");

        File[] zipFiles = listExpMMapFiles(dir);

        if (zipFiles.length == 0) {
            ErrorDialog.show("No experiment multimap zip files were found.");
            return;
        }

        MessageDialog.show("Listing experiment multimaps... DONE");

        // Load first experiment multimap files to create an empty multimap
        MessageDialog.show("Creating an empty multimap...");

        ExpMMap dest;
        try {
            dest = new ExpMMapLoader().load(zipFiles[0]);
        } catch (Exception ex) {
            ErrorDialog.show(ex.getMessage());
            return;
        }

        // Create empty map
        dest = dest.createMap(dest.width, dest.height);

        // Clear NaN to 0.0f
        for (Map map : dest.getMaps())
            if (map instanceof RealMap)
                ((RealMap) map).clearNaN(0.0f);

        MessageDialog.show("Creating an empty multimap... DONE");

        // Load other experiment multimap files
        ExpMMap mmap;
        for (int i = 0; i < zipFiles.length; i++) {
            MessageDialog.show("Adding experiment multimap " + (i + 1) + "...");

            // Load multimap
            try {
                mmap = new ExpMMapLoader().load(zipFiles[i]);
            } catch (Exception ex) {
                ErrorDialog.show(ex.getMessage());
                return;
            }

            // Perform addition
            try {
                MapMath.addition(dest, mmap, 1.0, 0.0, dest);
            } catch (Exception ex) {
                ErrorDialog.show(ex.getMessage());
                return;
            }

            MessageDialog.show("Adding experiment multimap " + (i + 1)
                    + "... DONE");
        }

        // Save final experiment multimap
        MessageDialog.show("Saving final experiment multimap...");

        new ExpMMapSaver().save(dest, outputFile);

        MessageDialog.show("Saving final experiment multimap... DONE");
    }



    /**
     * Prints the help of the program.
     */
    @Override
    protected void printHelp() {
        HelpFormatter formatter = new HelpFormatter();

        String header = "ExpMerge -- Merge experiments multimap results";
        formatter.printHelp("ExpMerge [options] <baseDirectory>", header,
                getOptions(), "");
    }
}
