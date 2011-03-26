
.. _peaks-average:

Peaks average
=============

This result calculates the average intensity of the identified peaks.

.. math::
   
   Q = \frac{1}{N} \sum\limits_{i=0}^{N}{I_i}

where *N* is the number of identified peaks, and :math:`I_i` is the intensity
of peak *i*.

Parameters
----------

This operation takes one parameter to select how many peaks to consider in
the average calculation.
This allows to only perform the average on the most intense peaks.
For instance if the maximum number of peaks is 3, the average will consist in
averaging the intensity of the 3 most intense peaks.
If the value of the maximum number of peaks is set to -1, all the identified
peaks are considered.
Note that if all the peaks are considered, this operation is the same as 
the :ref:`imagequality`.


