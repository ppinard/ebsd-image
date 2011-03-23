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
package org.ebsdimage.vendors.hkl;


/**
 * Init class for the HKL vendor.
 * 
 * @author Philippe T. Pinard
 */
public class Init extends rmlimage.module.Init {

    @Override
    public void preGUI() {
        if (rmlimage.module.Module.DEBUG)
            System.out.println("Running preGUI init of the EBSD module");

        rmlimage.io.IO.addLoader(org.ebsdimage.vendors.hkl.io.HklMMapLoader.class);
        rmlimage.io.IO.addSaver(org.ebsdimage.vendors.hkl.io.HklMMapSaver.class);
    }



    @Override
    public void postGUI() {
        rmlimage.module.stitch.MapStitcherFactory.addStitcher(org.ebsdimage.vendors.hkl.gui.HklMMapStitcher.class);
    }

}
