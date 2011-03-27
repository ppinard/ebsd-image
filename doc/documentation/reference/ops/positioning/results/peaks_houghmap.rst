
.. _peaks-houghmap:

Peaks HoughMap
==============

This operation saves the identified :ref:`houghpeak`'s as a :ref:`houghmap`
where each peak is represented by a white pixel.
In other words, the resultant HoughMap is similar to a :ref:`binmap` where
the background is black and the peaks are white.
The size and resolution of the resultant HoughMap are the same as the
HoughMap used to detect the peaks.
The HoughMap is saved in the :ref:`experiment <experiment>` directory.

Parameters
----------

This operation takes one parameter to select how many peaks to save in the 
:ref:`houghmap`.
This allows to discard peaks with a very low intensity as they may or may not
be peaks.
For instance if the maximum number of peaks is 3, only the 3 most intense peaks
will appear in the HoughMap.
If the value of the maximum number of peaks is set to -1, all the identified
peaks are considered.
