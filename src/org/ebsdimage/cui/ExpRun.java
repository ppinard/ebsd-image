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
import org.ebsdimage.core.exp.Exp;
import org.ebsdimage.io.exp.ExpLoader;
import org.ebsdimage.io.exp.ExpMMapSaver;

import ptpshared.cui.BaseCUI;
import rmlshared.cui.ErrorDialog;
import rmlshared.cui.MessageDialog;
import rmlshared.cui.ProgressBar;
import rmlshared.io.FileUtil;
import edu.umd.cs.findbugs.annotations.CheckForNull;

/**
 * Command line interface to run an experiment.
 * 
 * @author Philippe T. Pinard
 */
public class ExpRun extends BaseCUI {

    /**
     * Main entry.
     * 
     * @param args
     *            arguments passed to the program
     * @throws IOException
     *             if an exception occurs while executing the program
     */
    public static void main(String[] args) throws IOException {
        new ExpRun().parse(args);
    }

    /** Command line progress bar. */
    private ProgressBar progressBar;



    /**
     * Creates a new <code>ExpRun</code>.
     */
    public ExpRun() {
        progressBar = new ProgressBar(console);
    }



    /**
     * Changes the working directory of the experiment if this option is
     * specified in the arguments.
     * 
     * @param cmdLine
     *            command line arguments
     * @param exp
     *            experiment
     * @throws IOException
     *             if an error occurs
     */
    private void changeDir(CommandLine cmdLine, Exp exp) throws IOException {
        if (cmdLine.hasOption("dir")) {
            if (cmdLine.getOptionValue("dir") == null) {
                ErrorDialog.show("Please specify a working directory.");
                return;
            } else {
                File dir = new File(cmdLine.getOptionValue("dir"));
                exp.setDir(dir);
                MessageDialog.show("Working directory changed to: " + dir);
            }
        }
    }



    /**
     * Changes the name of the experiment if this option is specified in the
     * arguments.
     * 
     * @param cmdLine
     *            command line arguments
     * @param exp
     *            experiment
     * @throws IOException
     *             if an error occurs
     */
    private void changeName(CommandLine cmdLine, Exp exp) throws IOException {
        if (cmdLine.hasOption("name")) {
            if (cmdLine.getOptionValue("name") == null) {
                ErrorDialog.show("Please specify a name.");
                return;
            } else {
                String name = cmdLine.getOptionValue("name");
                exp.setName(name);
                MessageDialog.show("Experiment name changed to: " + name);
            }
        }
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
     * Returns the command line options.
     * 
     * @return command line options
     */
    @Override
    protected Options getOptions() {
        Options options = super.getOptions();

        options.addOption(new Option("d", "dir", true,
                "Different working directory for the experiment"));
        options.addOption(new Option("n", "name", true,
                "Different name for the experiment"));

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

        // Parse input XML file
        File file = getInputFile(cmdLine);
        if (file == null)
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

        // Change name if this option is specified
        changeName(cmdLine, exp);

        // Change directory if this option is specified
        changeDir(cmdLine, exp);

        // Logger
        setLogger(cmdLine);
        setLogger(cmdLine, exp);

        // Run experiment
        MessageDialog.show("Running experiment...");

        progressBar.addMonitorable(exp);

        progressBar.start();

        try {
            exp.run();
        } catch (Exception ex) {
            ErrorDialog.show(ex.getMessage());
            return;
        }

        progressBar.stop();

        MessageDialog.show("Running experiment... DONE");

        // Save results
        MessageDialog.show("Saving experiment multimap...");

        File mmapFile = new File(exp.getDir(), exp.getName() + ".zip");
        new ExpMMapSaver().save(exp.mmap, mmapFile);

        MessageDialog.show("Saving experiment multimap... DONE");
    }



    /**
     * Prints the help of the program.
     */
    @Override
    protected void printHelp() {
        HelpFormatter formatter = new HelpFormatter();

        String header = "ExpRun -- Load, run and save an experiment";
        formatter.printHelp("ExpRun [options] <xmlFile>", header, getOptions(),
                "");
    }



    /**
     * Turn off the logger of the experiment.
     * 
     * @param cmdLine
     *            command line arguments
     * @param exp
     *            an <code>Exp</code>
     */
    protected void setLogger(CommandLine cmdLine, Exp exp) {
        if (!cmdLine.hasOption("log")) {
            MessageDialog.show("Experiement logger is turn off");
            exp.turnOffLogger();
        }
    }

}
