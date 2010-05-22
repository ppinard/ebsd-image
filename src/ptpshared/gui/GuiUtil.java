package ptpshared.gui;

import java.net.URL;

import rmlshared.gui.ImageIcon;
import rmlshared.io.FileUtil;

/**
 * Miscellaneous utilities for the GUI.
 * 
 * @author ppinard
 * 
 */
public class GuiUtil {

    /**
     * Returns an <code>ImageIcon</code> from the specified icon file.
     * 
     * @param file
     *            icon file
     * 
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
