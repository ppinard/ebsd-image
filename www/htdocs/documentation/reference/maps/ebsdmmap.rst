
.. _ebsdmmap:

EbsdMMap
========

The *EbsdMMap* is the main type of :ref:`map <maps>` of the EBSD-Image module. 
It is a child of a :ref:`multimap` in the sense that it contains several maps 
of the same dimensions. 
All these maps corresponds to the various results obtained from an EBSD 
acquisition (image quality, phases, orientation, etc.). 
The *EbsdMMap* also contains the acquisition parameters (beam energy, 
step size, etc.) of the EBSD acquisition. 

Although many different type of results can be saved in the *EbsdMMap*, five 
are required by default. 
Four of them are for the four coefficients of the quaternion representing the 
orientation. 
The other one is a :ref:`phasemap` to identify the location of the indexed 
phases.

The acquisition parameters stored in the *EbsdMMap* are as followed:

  * Beam energy
  * Magnification
  * Tilt of the sample
  * Working distance
  * Step size in x and y
  * Rotation of the sample with respect to the camera
  * Calibration (pattern center and detector distance)

An *EbsdMMap* is saved in a ZIP as a :ref:`multimap`.
