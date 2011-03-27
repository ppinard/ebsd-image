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
package ptpshared.cui;

import java.util.logging.Logger;

import org.apache.commons.cli.*;

import ptpshared.util.LoggerUtil;
import rmlshared.cui.Console;
import rmlshared.cui.ErrorDialog;
import rmlshared.cui.MessageDialog;
import edu.umd.cs.findbugs.annotations.CheckForNull;

/**
 * Abstract class to create a command line interface.
 * 
 * @author Philippe T. Pinard
 */
public abstract class BaseCUI {

    /** Command line interface. */
    protected Console console;



    /**
     * Creates a new <code>BaseCUI</code> by initializing the console.
     */
    public BaseCUI() {
        console = new Console();
    }



    /**
     * Returns if the option is part of the arguments. In other words, if it is
     * <code>true</code>.
     * 
     * @param cmdLine
     *            command line (parsed arguments)
     * @param option
     *            name of the option
     * @return boolean value
     */
    protected boolean getBoolean(CommandLine cmdLine, String option) {
        return cmdLine.hasOption(option);
    }



    /**
     * Returns a double value from an option of the command line. Returns NaN if
     * the option doesn't exist.
     * 
     * @param cmdLine
     *            command line (parsed arguments)
     * @param option
     *            name of the option
     * @return double value
     */
    protected double getDouble(CommandLine cmdLine, String option) {
        if (!cmdLine.hasOption(option)) {
            ErrorDialog.show("No " + option + " is specified.");
            return Double.NaN;
        }

        if (cmdLine.getOptionValue(option) == null) {
            ErrorDialog.show("Please specify a " + option + ".");
            return Double.NaN;
        }

        double value;

        try {
            value = Double.parseDouble(cmdLine.getOptionValue(option));
            MessageDialog.show(option + ": " + value);
        } catch (NumberFormatException ex) {
            ErrorDialog.show(option + " is not a double value.");
            value = Double.NaN;
        }

        return value;
    }



    /**
     * Returns an integer value from an option of the command line. Returns -1
     * if the option doesn't exist.
     * 
     * @param cmdLine
     *            command line (parsed arguments)
     * @param option
     *            name of the option
     * @return integer value
     */
    protected int getInteger(CommandLine cmdLine, String option) {
        if (!cmdLine.hasOption(option)) {
            ErrorDialog.show("No " + option + " is specified.");
            return -1;
        }

        if (cmdLine.getOptionValue(option) == null) {
            ErrorDialog.show("Please specify a " + option + ".");
            return -1;
        }

        int value;

        try {
            value = Integer.parseInt(cmdLine.getOptionValue(option));
            MessageDialog.show(option + ": " + value);
        } catch (NumberFormatException ex) {
            ErrorDialog.show(option + " is not an integer value.");
            value = -1;
        }

        return value;
    }



    /**
     * Returns the command line options.
     * 
     * @return command line options
     */
    protected Options getOptions() {
        Options options = new Options();

        options.addOption("h", "help", false, "Prints this message");
        options.addOption(new Option("l", "log", false,
                "Show the logger information."));

        return options;
    }



    /**
     * Returns a string value from an option of the command line. Returns null
     * if the option doesn't exist.
     * 
     * @param cmdLine
     *            command line (parsed arguments)
     * @param option
     *            name of the option
     * @return string value
     */
    @CheckForNull
    protected String getString(CommandLine cmdLine, String option) {
        if (!cmdLine.hasOption(option)) {
            ErrorDialog.show("No " + option + " is specified.");
            return null;
        }

        if (cmdLine.getOptionValue(option) == null) {
            ErrorDialog.show("Please specify a " + option + ".");
            return null;
        }

        String value = cmdLine.getOptionValue(option);
        MessageDialog.show(option + ": " + value);

        return value;
    }



    /**
     * Parses the arguments and returns a <code>CommandLine</code> object. The
     * help is shown if no arguments are given or if the help argument is
     * passed.
     * 
     * @param args
     *            arguments
     * @return command line to get values from the arguments
     */
    @CheckForNull
    protected CommandLine parseArguments(String[] args) {
        // Print help if no argument is provided
        if (args.length == 0) {
            printHelp();
            return null;
        }

        // Parse the command line
        CommandLineParser parser = new PosixParser();
        CommandLine cmdLine;
        try {
            cmdLine = parser.parse(getOptions(), args);
        } catch (ParseException e) {
            return null;
        }

        // Print help
        if (cmdLine.hasOption("help")) {
            printHelp();
            return null;
        }

        return cmdLine;
    }



    /**
     * Prints the help of the program.
     */
    protected abstract void printHelp();



    /**
     * Parses the command line arguments to verify if the logger must be turn on
     * or off.
     * 
     * @param cmdLine
     *            command line arguments
     */
    protected void setLogger(CommandLine cmdLine) {
        if (!cmdLine.hasOption("log")) {
            MessageDialog.show("Logger is turn off");
            LoggerUtil.turnOffLogger(Logger.getLogger("ebsd"));
        }
    }
}
