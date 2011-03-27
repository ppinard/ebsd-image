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
package org.ebsdimage.core;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Comparator to sort the solution by their fit.
 * 
 * @author Philippe T. Pinard
 */
public class SolutionFitComparator implements Comparator<Solution>,
        Serializable {

    @Override
    public int compare(Solution sln0, Solution sln1) {
        if (sln0.fit < sln1.fit)
            return -1;
        else if (sln0.fit > sln1.fit)
            return 1;
        else
            return 0;
    }
}