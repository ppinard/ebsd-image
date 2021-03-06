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
package ptpshared.gui;

import java.net.URL;

import rmlshared.gui.ImageIcon;
import rmlshared.io.FileUtil;

/**
 * Miscellaneous utilities for the GUI.
 * 
 * @author Philippe T. Pinard
 */
public class GuiUtil {

    /**
     * Returns an <code>ImageIcon</code> from the specified icon file.
     * 
     * @param file
     *            icon file
     * @return <code>ImageIcon</code>
     */
    public static ImageIcon loadIcon(String file) {
        URL url = FileUtil.getURL(file);

        if (url == null)
            throw new RuntimeException("Icon located at \"" + file
                    + "\" cannot be found.");

        return new ImageIcon(url);
    }
}
