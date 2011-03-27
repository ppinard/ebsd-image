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
package org.ebsdimage.vendors.hkl.core;

import java.io.File;

import org.ebsdimage.core.EbsdMetadata;
import org.ebsdimage.core.Microscope;
import org.simpleframework.xml.Element;

/**
 * Metadata held in a <code>HklMMap</code>.
 * 
 * @author Philippe T. Pinard
 */
public class HklMetadata extends EbsdMetadata {

    /** Name of the project. */
    @Element(name = "projectName")
    private String projectName;

    /** Directory where the project is located. */
    @Element(name = "projectDir")
    private File projectDir;



    /**
     * Creates a <code>HklMetadata</code> with the required parameters. All
     * values are validated.
     * 
     * @param microscope
     *            microscope configuration
     * @param projectName
     *            name of the project
     * @param projectDir
     *            directory where the project is located
     */
    public HklMetadata(@Element(name = "microscope") Microscope microscope,
            @Element(name = "projectName") String projectName,
            @Element(name = "projectDir") File projectDir) {
        super(microscope);

        setProjectName(projectName);
        setProjectDir(projectDir);
    }



    @Override
    public boolean equals(Object obj, Object precision) {
        if (!super.equals(obj, precision))
            return false;

        HklMetadata other = (HklMetadata) obj;
        if (!projectName.equals(other.projectName))
            return false;
        if (!projectDir.equals(other.projectDir))
            return false;

        return true;
    }



    /**
     * Returns the directory where the project is located.
     * 
     * @return project's directory
     */
    public File getProjectDir() {
        return projectDir;
    }



    /**
     * Returns the name of the project.
     * 
     * @return project's name
     */
    public String getProjectName() {
        return projectName;
    }



    /**
     * Sets the directory where the project is located.
     * 
     * @param dir
     *            project's directory
     */
    public void setProjectDir(File dir) {
        if (dir == null)
            throw new NullPointerException("Undefined project dir");
        this.projectDir = dir.getAbsoluteFile();
    }



    /**
     * Sets the name of the project.
     * 
     * @param projectName
     *            project's name
     */
    public void setProjectName(String projectName) {
        if (projectName == null)
            throw new NullPointerException("Undefined project name");
        this.projectName = projectName;
    }

}
