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

import java.io.File;
import java.io.IOException;

import org.ebsdimage.core.exp.Exp;

import ptpshared.util.simplexml.ApacheCommonMathMatcher;
import ptpshared.util.simplexml.XmlSaver;
import rmlshared.io.Saver;
import crystallography.io.simplexml.SpaceGroupMatcher;

/**
 * Saves an <code>Exp</code> to an XML file.
 * 
 * @author Philippe T. Pinard
 */
public class ExpSaver implements Saver {

    /** XML saver. */
    private final XmlSaver saver;



    /**
     * Creates a new <code>ExpSaver</code>.
     */
    public ExpSaver() {
        saver = new XmlSaver();
        saver.matchers.registerMatcher(new ApacheCommonMathMatcher());
        saver.matchers.registerMatcher(new SpaceGroupMatcher());
    }



    @Override
    public boolean canSave(Object obj, String fileFormat) {
        return (obj instanceof Exp) && fileFormat.equalsIgnoreCase("xml");
    }



    @Override
    public double getTaskProgress() {
        return saver.getTaskProgress();
    }



    /**
     * Saves the specified <code>Exp</code> to disk.
     * 
     * @param exp
     *            experiment to save
     * @param file
     *            location where to save the experiment
     * @throws IOException
     *             if the experiment could not be saved to disk for any reason
     */
    public void save(Exp exp, File file) throws IOException {
        saver.save(exp, file);
    }



    @Override
    public void save(Object obj, File file) throws IOException {
        save((Exp) obj, file);
    }

}
