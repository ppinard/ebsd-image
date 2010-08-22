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
package org.ebsdimage.core;

import static crystallography.core.Calculations.interplanarDirectionCosine;
import static java.lang.Math.abs;

import java.util.ArrayList;

import crystallography.core.Reflector;
import crystallography.core.Reflectors;
import crystallography.core.UnitCell;

/**
 * Store many <code>InterplanarAnglePairs</code>.
 * 
 * @author Philippe T. Pinard
 */
public class InterplanarAnglePairs extends Pairs<InterplanarAnglePair> {

    /**
     * Creates a new <code>InterplanarAnglePairs</code> from all the reflectors.
     * 
     * @param refls
     *            reflectors of a phase
     * @throws NullPointerException
     *             if the reflectors is null
     * @see Reflectors
     */
    public InterplanarAnglePairs(Reflectors refls) {
        if (refls == null)
            throw new NullPointerException("Reflectors cannot be null.");

        ArrayList<InterplanarAnglePair> tmpPairs =
                new ArrayList<InterplanarAnglePair>();
        UnitCell unitCell = refls.crystal.unitCell;

        refls.sortByIntensity(true);

        for (int i = 0; i < refls.size(); i++) {
            for (int j = i + 1; j < refls.size(); j++) {
                Reflector refl0 = refls.get(i);
                Reflector refl1 = refls.get(j);

                double directionCosine =
                        abs(interplanarDirectionCosine(refl0.plane,
                                refl1.plane, unitCell));

                // Remove parallel reflectors (directionCosine == 1)
                if (abs(directionCosine - 1) <= 1e-7)
                    continue;

                tmpPairs.add(new InterplanarAnglePair(refl0, refl1,
                        directionCosine));
            }
        }

        // Initialize pairs array
        pairs = new InterplanarAnglePair[tmpPairs.size()];
        System.arraycopy(tmpPairs.toArray(), 0, pairs, 0, tmpPairs.size());

        // Sort pairs by direction cosine (i.e. angle)
        sortByDirectionCosine(false);
    }



    @Override
    public InterplanarAnglePair[] findClosestMatches(double directionCosine,
            double precision) {
        ArrayList<InterplanarAnglePair> matches =
                findClosestMatchesArrayList(directionCosine, precision);
        return matches.toArray(new InterplanarAnglePair[matches.size()]);
    }
}
