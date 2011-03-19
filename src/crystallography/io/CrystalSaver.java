package crystallography.io;

import java.io.File;
import java.io.IOException;

import ptpshared.util.simplexml.ApacheCommonMathMatcher;
import ptpshared.util.simplexml.XmlSaver;
import rmlshared.io.Saver;
import crystallography.core.Crystal;
import crystallography.io.simplexml.SpaceGroupMatcher;

/**
 * Saves a <code>Crystal</code> to an XML file.
 * 
 * @author Philippe T. Pinard
 */
public class CrystalSaver implements Saver {

    /** XML saver. */
    private final XmlSaver saver;



    /**
     * Creates a new <code>CrystalSaver</code>.
     */
    public CrystalSaver() {
        saver = new XmlSaver();
        saver.matchers.registerMatcher(new ApacheCommonMathMatcher());
        saver.matchers.registerMatcher(new SpaceGroupMatcher());
    }



    @Override
    public void save(Object obj, File file) throws IOException {
        save((Crystal) obj, file);
    }



    /**
     * Saves the specified <code>Crystal</code> to disk.
     * 
     * @param crystal
     *            crystal to save
     * @param file
     *            location where to save the crystal
     * @throws IOException
     *             if the crystal could not be saved to disk for any reason
     */
    public void save(Crystal crystal, File file) throws IOException {
        saver.save(crystal, file);
    }



    @Override
    public double getTaskProgress() {
        return saver.getTaskProgress();
    }



    @Override
    public boolean canSave(Object obj, String fileFormat) {
        return (obj instanceof Crystal) && fileFormat.equalsIgnoreCase("xml");
    }

}
