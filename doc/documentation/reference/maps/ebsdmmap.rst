
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

Although many different type of results can be saved in the *EbsdMMap*, six 
are required by default. 
Four of them are for the four coefficients of the quaternion representing the 
orientation. 
The other two are a :ref:`phasemap` and an :ref:`errormap`.
The :ref:`phasemap` identifies the location of the indexed phases and unindexed
pixels.
The :ref:`errormap` is used to report any error occurs during the processing
of diffraction patterns.

The acquisition parameters stored in the *EbsdMMap* are as followed:

  * Beam energy
  * Magnification
  * Tilt of the sample
  * Working distance
  * Rotation of the sample
  * Pattern center and detector distance

An *EbsdMMap* is saved in a ZIP as a :ref:`multimap`.
