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
package ptpshared.cui;

import java.util.logging.Logger;

import org.apache.commons.cli.*;

import ptpshared.utility.LoggerUtil;
import rmlshared.cui.Console;
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
}
