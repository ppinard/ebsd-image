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
package org.ebsdimage.io;

import java.io.File;
import java.io.IOException;

import rmlshared.io.Loader;
import rmlshared.io.TsvReader;
import rmlshared.util.Properties;

public class RplLoader implements Loader {

    public double getTaskProgress() {
        return 0;
    }



    public Object load(File file) throws IOException {
        return load(file, new Properties());
    }



    public Object load(File file, Object arg) throws IOException {
        if (arg == null)
            return load(file, (Properties) arg);

        if (arg instanceof Properties)
            return load(file, (Properties) arg);

        throw new IOException("Arg is of type " + arg.getClass().getName()
                + ". It must be of type " + Properties.class.getName());
    }



    public Properties load(File file, Properties props) throws IOException {
        TsvReader reader = new TsvReader(file);

        reader.skipLine(); // Skip the header

        String[] line;
        while (true) {
            line = reader.readLine();
            if (line == null)
                break;

            if (line.length != 2)
                throw new IOException("Incorrect number of values ("
                        + line.length + ")(" + line[0] + ") on line #"
                        + reader.getLineReadCount() + '\n' + "in file " + file
                        + ".\n" + "It should be 2.");

            props.setProperty(line[0], line[1]);
        }

        return props;
    }

}
