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
package org.ebsdimage.core.run;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

import ptpshared.util.LoggerUtil;
import rmlshared.io.FileUtil;
import rmlshared.ui.Monitorable;

/**
 * Abstract class for <code>Exp</code> and <code>Sim</code>.
 * 
 * @author Philippe T. Pinard
 */
@Root
public abstract class Run implements Monitorable {

    /** Default directory of the run. */
    public static final File DEFAULT_DIR = FileUtil.getCurrentDirFile();

    /** Directory where to output the results from the run. */
    protected File dir = DEFAULT_DIR;

    /** Default name of the run. */
    public static final String DEFAULT_NAME = "Untitled";

    /** Name of the run. */
    @Attribute
    protected String name = DEFAULT_NAME;

    /** Progress value. */
    protected double progress = 0.0;

    /** Progress status. */
    protected String status = "";

    /** Flag indicating if the operation should be interrupted. */
    protected boolean isInterrupted = false;

    /** Logger. */
    private Logger logger = Logger.getLogger("ebsd");

    /** Runtime variable of the index of the current run. */
    protected int currentIndex = -1;



    /**
     * Adds an <code>Operation</code>. The type of <code>Operation</code> is
     * automatically deduced from its super class.
     * 
     * @param op
     *            operation to be added.
     * @throws IllegalArgumentException
     *             if the operation doesn't have a known super class.
     */
    protected abstract void addOperation(Operation op);



    /**
     * Creates the working directory.
     * 
     * @throws RuntimeException
     *             if the directory cannot be created
     */
    protected void createDir() {
        if (!dir.exists())
            if (!(dir.mkdirs()))
                throw new RuntimeException("Could not create path "
                        + dir.toString());
    }



    /**
     * Returns the index of the current run. Only valid when the experiment is
     * running.
     * 
     * @return current index
     */
    public int getCurrentIndex() {
        if (currentIndex < 0)
            throw new RuntimeException(
                    "The experiment is not running, there is no index.");
        return currentIndex;
    }



    /**
     * Returns the working directory.
     * 
     * @return directory for outputs
     */
    public File getDir() {
        return dir;
    }



    /**
     * Returns the working directory.
     * 
     * @return directory for outputs
     */
    @Attribute(name = "dir")
    public String getDirPath() {
        return getDir().getPath();
    }



    /**
     * Returns the name of this experiment.
     * 
     * @return name
     */
    public String getName() {
        return name;
    }



    @Override
    public double getTaskProgress() {
        return progress;
    }



    @Override
    public String getTaskStatus() {
        return status;
    }



    /**
     * Initializes the runtime variables. These are the variables that are only
     * used during the execution of the run to keep track of certain values and
     * objects.
     */
    protected void initRuntimeVariables() {
        currentIndex = -1;
    }



    /**
     * Interrupts the experiment.
     */
    public synchronized void interrupt() {
        isInterrupted = true;
    }



    /**
     * Checks if the experiment should be interrupted. This method must be
     * synchronized because interrupt() may be called from any thread.
     * 
     * @return <code>true</code> if the operation is interrupted,
     *         <code>false</code> otherwise
     */
    protected synchronized boolean isInterrupted() {
        return isInterrupted;
    }



    /**
     * Runs and save input in the cache.
     * 
     * @throws IOException
     *             if an error occurs during the run
     */
    public abstract void run() throws IOException;



    /**
     * Sets the working directory. The outputs of all the runs are saved in this
     * directory.
     * 
     * @see #getDir()
     * @param dir
     *            path of the run
     */
    public void setDir(File dir) {
        if (dir == null)
            throw new NullPointerException("The path cannot be null");

        this.dir = dir;
    }



    /**
     * Sets the path of the run's directory.
     * 
     * @param dir
     *            working directory
     */
    @Attribute(name = "dir")
    public void setDirPath(String dir) {
        setDir(new File(dir));
    }



    /**
     * Sets the name of this run.
     * 
     * @param name
     *            name identifying the run
     * @throws NullPointerException
     *             if the name is null
     */
    public void setName(String name) {
        if (name == null)
            throw new NullPointerException("Name cannot be null.");
        this.name = name;
    }



    /**
     * Sets the status for the monitorable implementation and log the status in
     * the logger.
     * 
     * @param status
     *            descriptive text
     */
    protected void setStatus(String status) {
        if (status != null) {
            logger.info(status);
            this.status = currentIndex + " - " + status;
        }
    }



    /**
     * Turn off the logger.
     */
    public void turnOffLogger() {
        LoggerUtil.turnOffLogger(logger);
    }

}
