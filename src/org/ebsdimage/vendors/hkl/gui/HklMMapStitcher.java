package org.ebsdimage.vendors.hkl.gui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.ebsdimage.io.SmpStitcher;
import org.ebsdimage.vendors.hkl.io.HklMMapLoader;
import org.ebsdimage.vendors.hkl.io.HklMMapSaver;

import rmlimage.core.Map;
import rmlimage.gui.FileDialog;
import rmlimage.module.stitch.Tessera;
import rmlshared.enums.YesNo;
import rmlshared.gui.YesNoDialog;
import rmlshared.io.FileUtil;
import rmlshared.io.WildcardFileFilter;

/**
 * Multimap stitcher for HKL EBSD multimaps.
 * 
 * @author ppinard
 */
public class HklMMapStitcher extends rmlimage.module.multi.gui.MultiMapStitcher {

    /** Stitcher of smp's. */
    private SmpStitcher smpStitcher = null;



    /**
     * Creates a new <code>HklMMapStitcher</code>.
     */
    public HklMMapStitcher() {
        setInterruptable(true);
    }



    /**
     * Creates a new array of <code>Tessera</code> containing smp files and
     * regions of interest.
     * 
     * @param mmapTesserae
     *            <code>Tessera</code> used to do the multimap stitching
     * @return array of <code>Tesserae</code> to be used to stitch the smp files
     *         together
     * @throws FileNotFoundException
     *             if an smp file cannot be found
     */
    private Tessera[] createSmpTesserae(Tessera[][] mmapTesserae)
            throws FileNotFoundException {
        ArrayList<Tessera> smpTesserae = new ArrayList<Tessera>();

        int nbRows = mmapTesserae.length;
        int nbColumns = mmapTesserae[0].length;

        File smpFile;
        Tessera tessera;
        for (int row = 0; row < nbRows; row++)
            for (int column = 0; column < nbColumns; column++) {
                tessera = mmapTesserae[row][column];
                smpFile = FileUtil.setExtension(tessera.file, "smp");

                // Check if smp exists
                if (!smpFile.exists())
                    throw new FileNotFoundException("Smp file (" + smpFile
                            + ") for EBSD multimap (" + tessera.file
                            + ") does not exist.");

                // Create new tessera with smp file and previous roi
                smpTesserae.add(new Tessera(smpFile, tessera.roi));
            }

        return smpTesserae.toArray(new Tessera[0]);
    }



    @Override
    public double getTaskProgress() {
        if (smpStitcher == null)
            return super.getTaskProgress();
        else
            return smpStitcher.getTaskProgress();
    }



    @Override
    public String getTaskStatus() {
        if (smpStitcher == null)
            return super.getTaskStatus();
        else
            return smpStitcher.getTaskStatus();
    }



    @Override
    public void interrupt() {
        System.out.println("interrupt");
        if (smpStitcher != null) {
            super.interrupt();
            smpStitcher.interrupt();
        }
    }



    @Override
    public boolean isCorrect(Tessera[][] tesserae) {
        int nbRows = tesserae.length;
        int nbColumns = tesserae[0].length;

        for (int row = 0; row < nbRows; row++)
            for (int column = 0; column < nbColumns; column++)
                if (!HklMMapLoader.isHklMMap(tesserae[row][column].file))
                    return false;

        return true;
    }



    @Override
    public Map run(Tessera[][] tesserae) throws IOException {
        Map mmap = super.run(tesserae);

        // Ask to stitch smp's
        String message =
                "Do you want to stitch SMP files associated with the EBSD multimaps?";
        if (YesNoDialog.show("Stitch SMP files", message, YesNo.NO) == YesNo.NO)
            return mmap;

        // Setup smp stitching
        Tessera[] smpTesserae = createSmpTesserae(tesserae);

        smpStitcher = new SmpStitcher(smpTesserae, mmap.width, mmap.height);

        // Ask where to save the zip
        FileDialog.setTitle("Saving EBSD multimap");
        FileDialog.setFilter(new WildcardFileFilter("*.zip"));
        FileDialog.setMultipleSelection(false);
        FileDialog.setFileSelectionMode(FileDialog.FILES_AND_DIRECTORIES);
        FileDialog.setSelectedFile(mmap.getFile());

        if (FileDialog.showSaveDialog() == FileDialog.CANCEL)
            return mmap;

        // Saving multimap
        File mmapFile =
                FileUtil.setExtension(FileDialog.getSelectedFile(), "zip");
        mmap.setFile(mmapFile);
        new HklMMapSaver().save(mmap, mmapFile);

        File smpFile = FileUtil.setExtension(mmapFile, "smp");

        // Create smp
        smpStitcher.stitch(smpFile);

        // Done
        smpStitcher = null;

        return mmap;
    }

}
