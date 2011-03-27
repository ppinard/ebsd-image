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
package org.ebsdimage.io.sim;

import java.io.File;
import java.io.IOException;

import org.ebsdimage.core.sim.Sim;

import ptpshared.util.simplexml.ApacheCommonMathMatcher;
import ptpshared.util.simplexml.XmlSaver;
import rmlshared.io.Saver;
import crystallography.io.simplexml.SpaceGroupMatcher;

/**
 * Saves a <code>Sim</code> to an XML file.
 * 
 * @author Philippe T. Pinard
 */
public class SimSaver implements Saver {

    /** XMl saver. */
    private final XmlSaver saver;



    /**
     * Creates a new <code>SimSaver</code>.
     */
    public SimSaver() {
        saver = new XmlSaver();
        saver.matchers.registerMatcher(new ApacheCommonMathMatcher());
        saver.matchers.registerMatcher(new SpaceGroupMatcher());
    }



    @Override
    public boolean canSave(Object obj, String fileFormat) {
        return (obj instanceof Sim) && fileFormat.equalsIgnoreCase("xml");
    }



    @Override
    public double getTaskProgress() {
        return saver.getTaskProgress();
    }



    @Override
    public void save(Object obj, File file) throws IOException {
        save((Sim) obj, file);
    }



    /**
     * Saves the specified <code>Sim</code> to disk.
     * 
     * @param sim
     *            simulation to save
     * @param file
     *            file to save to
     * @throws IOException
     *             if the simulation could not be saved to disk for any reason
     */
    public void save(Sim sim, File file) throws IOException {
        saver.save(sim, file);
    }

}
