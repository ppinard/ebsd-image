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
package org.ebsdimage.core.exp.ops.identification.post;

import org.ebsdimage.core.ErrorCode;
import org.ebsdimage.core.HoughPeak;
import org.ebsdimage.core.exp.Exp;
import org.ebsdimage.core.exp.ExpError;

public class IdentificationPostOps2Mock extends IdentificationPostOps {

    public static final ErrorCode Error1 = new ErrorCode("Error1", "Desc1");



    @Override
    public ErrorCode[] getErrorCodes() {
        return new ErrorCode[] { Error1 };
    }



    @Override
    public HoughPeak[] process(Exp exp, HoughPeak[] srcPeaks) throws ExpError {
        if (exp.getCurrentIndex() == 1)
            throw new ExpError(Error1);

        return srcPeaks.clone();
    }
}
