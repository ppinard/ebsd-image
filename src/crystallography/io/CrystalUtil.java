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
import java.util.ArrayList;
import java.util.Arrays;

import ptpshared.util.xml.XmlLoader;
import rmlshared.io.FileUtil;
import crystallography.core.Crystal;

/**
 * Utilities related to <code>Crystal</code>.
 * 
 * @author Philippe T. Pinard
 */
public class CrystalUtil {

    /**
     * Loads and lists all the crystals in the specified directory.
     * 
     * @param dir
     *            directory to search for crystals
     * @return list of crystals
     */
    public static Crystal[] listCrystals(File dir) {
        if (dir == null)
            return new Crystal[0];

        ArrayList<Crystal> crystals = new ArrayList<Crystal>();

        File[] crystalFiles = FileUtil.listFilesOnly(dir, "*.xml");
        Arrays.sort(crystalFiles);

        XmlLoader loader = new XmlLoader();
        for (File crystalFile : crystalFiles) {
            Crystal crystal;

            try {
                crystal = loader.load(Crystal.class, crystalFile);
            } catch (Exception e) {
                continue;
            }

            crystals.add(crystal);
        }

        return crystals.toArray(new Crystal[crystals.size()]);
    }
}
