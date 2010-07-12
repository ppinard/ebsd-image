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
package org.ebsdimage;

import rmlimage.RMLImage;

/**
 * Init class for the EBSD module.
 * 
 * @author Philippe T. Pinard
 */
public class Init extends rmlimage.module.Init {

    @Override
    public void preGUI() {
        if (rmlimage.module.Module.DEBUG)
            System.out.println("Running preGUI init of the EBSD module");

        // Conversion
        rmlimage.core.Conversion.addHandler(org.ebsdimage.core.Conversion.class);

        // IO
        rmlimage.io.IO.addHandler(org.ebsdimage.io.IO.class);

        // Edit
        rmlimage.core.Edit.addHandler(org.ebsdimage.core.Edit.class);

        // MapMath
        rmlimage.core.MapMath.addHandler(org.ebsdimage.core.MapMath.class);
    }



    @Override
    public void postGUI() {
        // Activate the Cancel button
        rmlimage.ui.Desktop desktop = RMLImage.getDesktop();
        if (desktop instanceof rmlimage.gui.Desktop)
            ((rmlimage.gui.Desktop) desktop).showCancelButton(true);

        rmlimage.module.stitch.MapStitcherFactory.addStitcher(org.ebsdimage.gui.exp.ExpMMapStitcher.class);
    }

}
