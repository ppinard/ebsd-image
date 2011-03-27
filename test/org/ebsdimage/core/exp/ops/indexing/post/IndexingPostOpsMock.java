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
package org.ebsdimage.core.exp.ops.indexing.post;

import org.ebsdimage.core.Solution;
import org.ebsdimage.core.exp.Exp;

public class IndexingPostOpsMock extends IndexingPostOps {

    @Override
    public Solution[] process(Exp exp, Solution[] srcSolutions) {
        Solution[] destSolutions = new Solution[srcSolutions.length];

        for (int i = 0; i < destSolutions.length; i++)
            destSolutions[i] =
                    new Solution(srcSolutions[i].phase,
                            srcSolutions[i].rotation, srcSolutions[i].fit / 2);

        return destSolutions;
    }

}
