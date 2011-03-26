
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

Benchmarking
------------

On an Intel Xeon 3 GHz computer (3 GB of RAM, 7,200 rpm SATA hard drive) 
with the Microsoft Windows XP operating system, it takes one minute to browse 
a folder containing 50,298 images. 
The transfer of these files to a portable magnetic hard drive via a USB 2.0 
connection takes just over 18 minutes. 
On the same computer described above, the inclusion of 50,298 images into a 
SMP file takes approximately 2 minutes and 30 seconds. 
The real advantage comes when transferring the SMP file to another media format: 
it took one minute to copy the SMP file to the same portable hard drive. 
