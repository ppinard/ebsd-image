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
package org.ebsdimage.core.exp;

import org.ebsdimage.core.run.Operation;
import org.ebsdimage.core.run.RunsGenerator;

/**
 * Experiments generator based on <code>RunsGenerator</code>.
 * 
 * @see RunsGenerator
 * @author Philippe T. Pinard
 */
public class ExpsGenerator extends RunsGenerator {

    /**
     * Creates a new generator that stores a list of items. This allows to
     * generate many <code>Experiment</code>s based on the given items. Use
     * {@link ExpsGenerator#addItem(int, Operation)} to add items.
     */
    public ExpsGenerator() {
    }

}
