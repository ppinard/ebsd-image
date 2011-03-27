
.. _peaks-stddev:

Peaks standard deviation
========================

This result calculates the standard deviation from the intensity of the 
identified peaks.

.. math::
   
   Q = \sqrt{\frac{1}{N-1} \sum\limits_{i=1}^N{\left( I_{i}-\bar{I} \right)^2}}

where *N* is the number of identified peaks, and :math:`I_i` is the intensity
of peak *i*.

Parameters
----------

This operation takes one parameter to select how many peaks to consider in
the standard deviation calculation.
This allows to only calculate the standard deviation on the most intense peaks.
For instance if the maximum number of peaks is 3, the standard deviation will 
be calculated using the intensity of the 3 most intense peaks.
If the value of the maximum number of peaks is set to -1, all the identified
peaks are considered.


