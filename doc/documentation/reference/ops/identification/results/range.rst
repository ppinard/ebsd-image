
.. _peaks-range:

Peaks range
===========

Quality metric that is computed from the difference between the intensity of
the most intense and the least intense peak. 
The difference between the most intense peak and the least intense peak should 
be greater for a high quality diffraction pattern since there should be a 
higher contrast between the Kikuchi bands.

By ordering the Hough peaks in descending order,

.. math::

   Q = I_i - I_{N-1}
   
where *N* is the number of identified peaks, and :math:`I_i` is the intensity
of peak *i*.

Parameters
----------

This operation takes one parameter to select the least intense peak. 
This allows to discard peaks with a very low intensity as they may or may not
be peaks.
For instance if the maximum number of peaks is 3, the range will consist in the
difference between the most intense peak and the 3rd most intense peak.
If the value of the maximum number of peaks is set to -1, all the identified
peaks are considered.

.. figure:: /images/ops/identification/results/range/nicocraly_houghpeakrange.png
   :align: center

   Example on NiCoCrAlY sample
..
