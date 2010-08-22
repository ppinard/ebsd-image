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
package org.ebsdimage.plugin;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.ebsdimage.io.RawLoader;

import rmlimage.core.ByteMap;
import rmlimage.core.Edit;
import rmlimage.gui.BasicDialog;
import rmlimage.module.integer.core.Conversion;
import rmlimage.module.integer.core.IntMap;
import rmlimage.plugin.PlugIn;
import rmlshared.enums.YesNo;
import rmlshared.geom.shape.String2D;
import rmlshared.gui.ColumnPanel;
import rmlshared.gui.FileNameField;
import rmlshared.gui.OkCancelDialog;
import rmlshared.io.FileUtil;
import rmlshared.ui.Monitorable;
import edu.umd.cs.findbugs.annotations.CheckForNull;

/**
 * Plug-in to unwrap diffraction patterns based on x and y unwarping maps. The
 * unwarping maps are created using Lispix.
 * 
 * @author Marin Lagac&eacute;
 */
public class UnWarp extends PlugIn implements Monitorable {

    /**
     * Dialog to select the source and destination directory for the diffraction
     * patterns. The user must also select the directory containing the
     * unwarping maps.
     * 
     * @author Marin Lagac&eacute;
     */
    private class Dialog extends BasicDialog {

        /** Field to select the source directory for the diffraction patterns. */
        private FileNameField srcDirField;

        /**
         * Field to select the destination directory for the diffraction
         * patterns.
         */
        private FileNameField destDirField;

        /** Field to select the directory containing the un-warping maps. */
        private FileNameField warpDirField;



        /**
         * Creates a new dialog to select the three directories.
         */
        public Dialog() {
            super("UnWarp");

            ColumnPanel cPanel = new ColumnPanel(2);

            // Create the src dir field
            srcDirField = new FileNameField("Source Directory", true);
            srcDirField.setFileSelectionMode(FileNameField.FILES_AND_DIRECTORIES);
            cPanel.add("Source Directory: ", srcDirField);

            // Create the dest dir field
            destDirField = new FileNameField("Destination Directory", true);
            destDirField.setFileSelectionMode(FileNameField.FILES_AND_DIRECTORIES);
            cPanel.add("Destination Directory: ", destDirField);

            // Create the wapr dir field
            warpDirField = new FileNameField("Warp Maps Directory", true);
            warpDirField.setFileSelectionMode(FileNameField.FILES_AND_DIRECTORIES);
            cPanel.add("Warp Maps Directory: ", warpDirField);

            setMainComponent(cPanel);

            setName("UnWarp");
            setPreferences(getPlugIn().getPreferences().node("EBSD"));

        }



        /**
         * Returns the destination directory for the unwarped diffraction
         * patterns.
         * 
         * @return directory
         */
        public File getDestDir() {
            return destDirField.getFileBFR();
        }



        /**
         * Returns the source directory containing the warped diffraction
         * patterns.
         * 
         * @return directory
         */
        public File getSrcDir() {
            return srcDirField.getFileBFR();
        }



        /**
         * Returns the directory containing the x and y unwarping maps created
         * using Lispix.
         * 
         * @return directory
         */
        public File getWarpDir() {
            return warpDirField.getFileBFR();
        }



        @Override
        public boolean isCorrect() {
            if (!super.isCorrect())
                return false;

            // Checks if the src and dest dir are really directories
            File srcDir = srcDirField.getFile();
            if (!srcDir.isDirectory()) {
                showErrorDialog(srcDir + " is not a directory.");
                return false;
            }
            File destDir = destDirField.getFile();
            if (!destDir.isDirectory()) {
                showErrorDialog(destDir + " is not a directory.");
                return false;
            }
            File warpDir = warpDirField.getFile();
            if (!warpDir.isDirectory()) {
                showErrorDialog(warpDir + " is not a directory.");
                return false;
            }

            return true;
        }

    }

    /** Cached grayMap. */
    private BufferedImage buffGrayMap = null;



    /**
     * Checks if the unwarping is done properly by showing the user the result
     * of the first diffraction pattern.
     * 
     * @param srcFile
     *            file of the first diffraction pattern
     * @param xWarpFile
     *            file to x unwarping map
     * @param yWarpFile
     *            file to y unwarping map
     * @return <code>true</code> if the user accepts the unwarping,
     *         <code>false</code> otherwise
     * @throws IOException
     *             if an error occurs while opening the files
     */
    private boolean areWarpMapsCorrect(File srcFile, File xWarpFile,
            File yWarpFile) throws IOException {
        // Load the original map
        ByteMap srcMap = load(srcFile, null);

        // Create the test image
        ByteMap map = new ByteMap(srcMap.width * 2, srcMap.height * 2);

        // Paste the x warp map to the upper left corner
        // and add the label
        IntMap xWarpMap = (IntMap) new RawLoader().load(xWarpFile);
        ByteMap xWarpByteMap = Conversion.toByteMap(xWarpMap);
        Edit.copy(xWarpByteMap, xWarpByteMap.getROI(), map, 0, 0);
        String2D str = new String2D("X Warp");
        str.setFont("default", String2D.Style.BOLD, 16);
        str.setColor(Color.RED);
        str.setLocation(map.width / 4, 0);
        str.setAlignment(String2D.Alignment.SOUTH);
        map.add(str);

        // Paste the y warp map to the upper right corner
        // and add the label
        IntMap yWarpMap = (IntMap) new RawLoader().load(yWarpFile);
        ByteMap yWarpByteMap = Conversion.toByteMap(yWarpMap);
        Edit.copy(yWarpByteMap, yWarpByteMap.getROI(), map, map.width / 2, 0);
        str = new String2D("Y Warp");
        str.setFont("default", String2D.Style.BOLD, 16);
        str.setColor(Color.RED);
        str.setLocation(map.width * 3 / 4, 0);
        str.setAlignment(String2D.Alignment.SOUTH);
        map.add(str);

        // Paste the src map to the lower left corner
        // and add the label
        Edit.copy(srcMap, srcMap.getROI(), map, 0, map.height / 2);
        str = new String2D("SRC");
        str.setFont("default", String2D.Style.BOLD, 16);
        str.setColor(Color.RED);
        str.setLocation(map.width / 4, map.height / 2);
        str.setAlignment(String2D.Alignment.SOUTH);
        map.add(str);

        // Unwarp the map
        ByteMap destMap = new ByteMap(srcMap.width, srcMap.height);
        unwarp(srcMap, xWarpMap, yWarpMap, destMap);

        // Paste the unwarped map to the lower right corner
        // and add the label
        Edit.copy(destMap, destMap.getROI(), map, map.width / 2, map.height / 2);
        str = new String2D("DEST");
        str.setFont("default", String2D.Style.BOLD, 16);
        str.setColor(Color.RED);
        str.setLocation(map.width * 3 / 4, map.height / 2);
        str.setAlignment(String2D.Alignment.SOUTH);
        map.add(str);

        map.shouldSave(false);
        add(map); // Add the test map to the desktop

        // Ask the user if it is correct
        // If not stop
        YesNo answer = showQuestionDialog("UnWarp", "Is it correct?", YesNo.NO);
        return (answer == YesNo.NO) ? false : true;
    }



    /**
     * Gets the number of digits needed to form the image file name.
     * 
     * @param srcDir
     *            source directory containing the warped diffraction patterns
     * @return number of digits
     */
    protected int getDigitCount(File srcDir) {
        String genericName = getGenericName(srcDir);
        if (genericName == null)
            return -1; // If error (message already appeared)

        File genericFile = new File(srcDir, getGenericName(srcDir) + ".jpg");

        for (int nbDigits = 1; nbDigits < 10; nbDigits++) {
            String fNo = rmlshared.math.Long.format(1, nbDigits);
            File file = FileUtil.append(genericFile, fNo);
            // System.out.println("Testing for" + file);
            if (file.exists())
                return nbDigits;
        }

        showErrorDialog("Could not find the number of digits for the files in "
                + srcDir);
        return -1;
    }



    /**
     * Determine the image's generic file name from their directory name.
     * 
     * @param srcDir
     *            source directory containing the warped diffraction patterns
     * @return generic file name
     */
    @CheckForNull
    protected String getGenericName(File srcDir) {
        String dirName = srcDir.getName();
        if (!dirName.endsWith("Images")) {
            showErrorDialog("The source directory name (" + srcDir
                    + ") should end with Images");
            return null;
        } else {
            return dirName.substring(0, dirName.length() - "Images".length());
        }
    }



    /**
     * Creates a buffered image with the specified dimensions, if it is not
     * already created. Otherwise, it returns the current buffered image.
     * 
     * @param width
     *            width of the buffered image
     * @param height
     *            height of the buffered image
     * @return buffered image
     */
    private BufferedImage getGrayMap(int width, int height) {
        if (buffGrayMap == null || width != buffGrayMap.getWidth()
                || height != buffGrayMap.getHeight()) {
            buffGrayMap =
                    new BufferedImage(width, height,
                            BufferedImage.TYPE_BYTE_GRAY);
            return buffGrayMap;
        } else {
            return buffGrayMap;
        }
    }



    /**
     * Loads a source diffraction pattern.
     * 
     * @param file
     *            file of the diffraction pattern
     * @param destMap
     *            map where to put the pixArray of the loaded diffraction
     *            patterns
     * @return the destination map
     * @throws IOException
     *             if an error occurs while loading the image
     */
    protected ByteMap load(File file, ByteMap destMap) throws IOException {
        if (!file.exists())
            throw new FileNotFoundException(file.getPath() + " not found.");

        // Setup the reader
        ImageReader reader = ImageIO.getImageReadersBySuffix("jpeg").next();
        ImageInputStream inStream = ImageIO.createImageInputStream(file);
        reader.setInput(inStream);

        // Get the number of images in the file
        int nbImages = reader.getNumImages(true);
        if (nbImages != 1)
            throw new IOException("Invalid number of images (" + nbImages
                    + ") in " + file);

        // Get the index of the image
        int index = reader.getMinIndex();

        // Get the dimensions of the image
        int width = reader.getWidth(index);
        int height = reader.getHeight(index);

        // Read the image as a grayscale image
        BufferedImage grayMap = getGrayMap(width, height);
        ImageReadParam param = reader.getDefaultReadParam();
        param.setDestination(grayMap);
        grayMap = reader.read(index, param);

        // Close the stream
        inStream.close();

        // Check that the image read is really a greyscale image
        DataBuffer dbuff = grayMap.getData().getDataBuffer();
        if (!(dbuff instanceof DataBufferByte))
            throw new IOException(file + " is not a grayscale jpeg.");

        // If no destination map was specified or it is the wrong size,
        // create a new one
        if (destMap == null || destMap.width != grayMap.getWidth()
                || destMap.height != grayMap.getHeight())
            destMap = new ByteMap(width, height);

        // Copy the pixArray from the grayMap to the destination map
        System.arraycopy(((DataBufferByte) dbuff).getData(), 0,
                destMap.pixArray, 0, destMap.size);

        return destMap;
    }



    /**
     * Save the unwarped diffraction pattern in the destination directory.
     * 
     * @param map
     *            map to save
     * @throws IOException
     *             if an error occurs while saving the image
     */
    private void save(ByteMap map) throws IOException {
        BufferedImage img =
                new BufferedImage(map.width, map.height,
                        BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D g = img.createGraphics();
        g.drawRenderedImage(map.getImage(), null);
        g.dispose();
        // ImageWriter writer = (ImageWriter)
        // ImageIO.getImageWritersBySuffix("jpeg").next();
        // ImageWriteParam param = writer.getDefaultWriteParam();
        // param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        // writer.setOutput(ImageIO.createImageOutputStream(destFiles[n]));
        // writer.write(null, new IIOImage(bw, null, null), param);
        ImageIO.write(img, "jpg", map.getFile());
    }



    /**
     * Unwarp one diffraction pattern.
     * 
     * @param srcMap
     *            warped diffraction pattern
     * @param xWarpMap
     *            x unwarping map
     * @param yWarpMap
     *            y unwarping map
     * @param destMap
     *            unwarped diffraction pattern
     * @return unwarped diffraction pattern
     */
    protected ByteMap unwarp(ByteMap srcMap, IntMap xWarpMap, IntMap yWarpMap,
            ByteMap destMap) {
        // Check that all the maps are of the same size
        if (!xWarpMap.isSameSize(yWarpMap))
            throw new IllegalArgumentException("xWarpMap size ("
                    + xWarpMap.getName() + ")(" + xWarpMap.getDimensionLabel()
                    + ") must be the same size as yWarpMap size ("
                    + yWarpMap.getName() + ")(" + yWarpMap.getDimensionLabel()
                    + ").");
        if (!xWarpMap.isSameSize(srcMap))
            throw new IllegalArgumentException("xWarpMap size ("
                    + xWarpMap.getName() + ")(" + xWarpMap.getDimensionLabel()
                    + ") must be the same size as srcMap size ("
                    + srcMap.getName() + ")(" + srcMap.getDimensionLabel()
                    + ").");
        if (!srcMap.isSameSize(destMap))
            throw new IllegalArgumentException("srcMap size ("
                    + srcMap.getName() + ")(" + srcMap.getDimensionLabel()
                    + ") must be the same size as destMap size ("
                    + destMap.getName() + ")(" + destMap.getDimensionLabel()
                    + ").");

        int[] xWarpPixArray = xWarpMap.pixArray; // For performance
        int[] yWarpPixArray = yWarpMap.pixArray;
        byte[] srcPixArray = srcMap.pixArray;
        byte[] destPixArray = destMap.pixArray;

        int width = srcMap.width;
        int height = srcMap.height;
        int x;
        int y;
        int index;
        int destX;
        int destY;
        int destIndex;
        for (y = 0; y < height; y++) {
            for (x = 0; x < width; x++) {
                index = y * width + x;

                // Calculate the new x coordinate in the unwarped image
                destX = x + xWarpPixArray[index];
                if (destX < 0)
                    continue; // Skip if outside image
                if (destX >= width)
                    continue;

                // Calculate the new y coordinate in the unwarped image
                destY = y + yWarpPixArray[index];
                if (destY < 0)
                    continue; // Skip if outside image
                if (destY >= height)
                    continue;

                // The the new index in the unwarped image
                destIndex = destY * width + destX;

                // Copy the src pixel at its new position in the unwarped image
                destPixArray[index] = srcPixArray[destIndex];
            }
        }

        return destMap;

    }



    /**
     * Unwarp the diffraction patterns in the source directory.
     * 
     * @param srcFiles
     *            array of all diffraction patterns in the source directory
     * @param xWarpFile
     *            file to x unwarping map
     * @param yWarpFile
     *            file to y unwarping map
     * @param destFiles
     *            array of the unwarped diffraction patterns in the destination
     *            directory
     * @throws IOException
     *             if an error occurs while loading and saving the images
     */
    private void unwarp(File[] srcFiles, File xWarpFile, File yWarpFile,
            File[] destFiles) throws IOException {
        if (srcFiles.length != destFiles.length)
            throw new IllegalArgumentException("srcFiles array length ("
                    + srcFiles.length
                    + ") must be the same as destFiles array length ("
                    + destFiles.length + ").");

        // Load the warp maps
        IntMap xWarpMap = (IntMap) new RawLoader().load(xWarpFile);
        IntMap yWarpMap = (IntMap) new RawLoader().load(yWarpFile);

        ByteMap srcMap = null;
        ByteMap destMap = null;
        for (int n = 0; n < srcFiles.length; n++) {
            setTaskProgress((double) n / srcFiles.length, "Processing "
                    + srcFiles[n].getName());

            srcMap = load(srcFiles[n], srcMap); // Load the original map

            // Create a byteMap of the correct size if necessary
            if (destMap == null || !srcMap.isSameSize(destMap))
                destMap = new ByteMap(srcMap.width, srcMap.height);

            unwarp(srcMap, xWarpMap, yWarpMap, destMap); // Unwarp

            destMap.setFile(destFiles[n]); // Rename the file

            save(destMap); // Save the unwarped map
        }
    }



    @Override
    public void xRun() throws IOException {
        Dialog dialog = new Dialog();
        if (dialog.show() == OkCancelDialog.CANCEL)
            return;

        // Get the src and dest directories
        File srcDir = dialog.getSrcDir();
        if (!srcDir.exists()) {
            showErrorDialog(srcDir + " not found.");
            return;
        }
        File destDir = dialog.getDestDir();
        if (!destDir.exists()) {
            showErrorDialog(destDir + " not found.");
            return;
        }
        File warpDir = dialog.getWarpDir();
        if (!warpDir.exists()) {
            showErrorDialog(warpDir + " not found.");
            return;
        }

        // Check if the warp files exist
        // First check in the destination directory (for speed)(Request #43)
        // If not found, ask the user if he wants to check in the source dir
        File xWarpFile = new File(warpDir, "warp-x-map.raw");
        if (!xWarpFile.exists()) {
            showErrorDialog("warp-x-map.raw not found in " + warpDir);
            return;
        }
        File yWarpFile = new File(warpDir, "warp-y-map.raw");
        if (!yWarpFile.exists()) {
            showErrorDialog("warp-y-map.raw not found in " + warpDir);
            return;
        }

        // Build the name of the first image

        // Get the number of digits of the file number
        int nbDigits = getDigitCount(srcDir);
        if (nbDigits < 0)
            return; // If bad src dir

        // Build the file name
        StringBuilder fileName = new StringBuilder(128);
        fileName.append(getGenericName(srcDir));
        for (int n = 0; n < nbDigits - 1; n++)
            fileName.append('0');
        fileName.append("1.jpg");
        File srcFile = new File(srcDir, fileName.toString());

        showMessageDialog("Unwarp", "Nb of digits = " + nbDigits + '\n'
                + "File name = " + srcFile);

        // Test the warp maps
        if (!areWarpMapsCorrect(srcFile, xWarpFile, yWarpFile))
            return;

        // Get the images to unwarp
        File[] srcFiles = FileUtil.listFilesOnly(srcDir, "*.jpg");

        // Build the destination file names
        File[] destFiles = new File[srcFiles.length];
        for (int n = 0; n < srcFiles.length; n++)
            // destFiles[n] = FileUtil.setDir(
            // FileUtil.setExtension(srcFiles[n], "bmp"), destDir);
            destFiles[n] = FileUtil.setDir(srcFiles[n], destDir);

        unwarp(srcFiles, xWarpFile, yWarpFile, destFiles);

        // Inform the user that the job is finished (Request #42)
        showMessageDialog("UnWarp", "Completed!!!");

    }

}
