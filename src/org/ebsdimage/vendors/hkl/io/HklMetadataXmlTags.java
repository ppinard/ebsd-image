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
package org.ebsdimage.vendors.hkl.io;

import org.ebsdimage.vendors.hkl.core.HklMetadata;

/**
 * Tags for <code>HklMetadata</code>'s XML <code>Element</code>.
 * 
 * @author Philippe T. Pinard
 */
public class HklMetadataXmlTags {
    /** XML tag name for <code>HklMetadata</code>. */
    public static final String TAG_NAME = HklMetadata.class.getSimpleName();

    /** XML tag name for the project's name. */
    public static final String PROJECT_NAME_TAG = "ProjectName";

    /** XML tag name for the project's path. */
    public static final String PROJECT_PATH_TAG = "ProjectPath";
}
