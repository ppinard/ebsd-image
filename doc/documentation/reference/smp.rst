
.. _smp:

Stacked Maps (SMP)
==================

This file format is a proprietary file format to store a series of images. 
SMP stands for Stacked MaPs. 
In EBSD-Image, this file format is used to quickly access diffraction pattern 
images. 
This offers a solution to store the thousands diffraction pattern images output 
by HKL Channel 5 or TSL OIM.

This file format was created with the following reasoning in mind:

  * Store a large quantity of images inside one file. 
    This eliminates the operating systems' problem to list a folder with a 
    large quantity of files.
  * Easier to move all the images at once. 
    Great for backup.
  * Random access of maps in the SMP file without loading the whole SMP file 
    in memory.
  * Quickly load map from SMP file. 
    No decompression required.
