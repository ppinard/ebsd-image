
.. _peaks-sum:

Peaks sum
=========

This result calculates the sum of the intensity of the identified peaks.

.. math::
   
   Q = \sum\limits_{i=1}^N{I_{i}}

where *N* is the number of identified peaks, and :math:`I_i` is the intensity
of peak *i*.

Parameters
----------

This operation takes one parameter to select how many peaks to consider in
the sum calculation.
This allows to only calculate the sum on the most intense peaks.
For instance if the maximum number of peaks is 3, the sum consists in 
adding the intensity of the 3 most intense peaks.
If the value of the maximum number of peaks is set to -1, all the identified
peaks are considered.


