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
package ptpshared.util.simplexml;

import net.sf.magnitude.core.Magnitude;

import org.simpleframework.xml.transform.Transform;

/**
 * Simple XML transform for the Magnitude library.
 * 
 * @author Philippe T. Pinard
 */
public class MagnitudeTransform implements Transform<Magnitude> {

    @Override
    public Magnitude read(String value) throws Exception {
        String[] parts = value.split(" ", 2);
        return new Magnitude(Double.parseDouble(parts[0]), parts[1]);
    }



    @Override
    public String write(Magnitude value) throws Exception {
        return value.toString();
    }

}
