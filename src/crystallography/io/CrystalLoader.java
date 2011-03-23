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
package crystallography.io;

import java.io.File;
import java.io.IOException;

import ptpshared.util.simplexml.ApacheCommonMathMatcher;
import ptpshared.util.simplexml.XmlLoader;
import rmlshared.io.FileUtil;
import rmlshared.io.Loader;
import rmlshared.io.TextFileReader;
import crystallography.core.Crystal;
import crystallography.io.simplexml.SpaceGroupMatcher;

/**
 * Loads a <code>Crystal</code> from an XML file.
 * 
 * @author Philippe T. Pinard
 */
public class CrystalLoader implements Loader {

    /** XML loader. */
    private final XmlLoader loader;



    /**
     * Creates a new <code>CrystalLoader</code>.
     */
    public CrystalLoader() {
        loader = new XmlLoader();
        loader.matchers.registerMatcher(new ApacheCommonMathMatcher());
        loader.matchers.registerMatcher(new SpaceGroupMatcher());
    }



    @Override
    public boolean canLoad(File file) {
        return getValidationMessage(file).isEmpty();
    }



    @Override
    public double getTaskProgress() {
        return loader.getTaskProgress();
    }



    /**
     * Returns the validation message. An empty string is the file is valid; a
     * error message otherwise.
     * 
     * @param file
     *            a file
     * @return error message or empty string
     */
    protected String getValidationMessage(File file) {
        // Check extension
        String ext = FileUtil.getExtension(file);
        if (!ext.equalsIgnoreCase("xml"))
            return "The extension of the file must be xml, not " + ext + ".";

        // Check header line
        String header = null;
        try {
            TextFileReader reader = new TextFileReader(file);
            header = reader.readLine();
            reader.close();
        } catch (IOException ex) {
            return ex.getMessage();
        }

        if (header == null || !header.startsWith("<crystal"))
            return "Line 2 of the XML file should start wiht '<crystal'.";

        return "";
    }



    @Override
    public Crystal load(File file) throws IOException {
        if (!canLoad(file))
            throw new IOException(getValidationMessage(file));

        return loader.load(Crystal.class, file);
    }



    @Override
    public Crystal load(File file, Object obj) throws IOException {
        return loader.load(Crystal.class, file);
    }

}
