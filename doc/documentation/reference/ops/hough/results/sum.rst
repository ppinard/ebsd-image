
.. _hough-sum:

Hough sum
=========

This result calculates the total intensity of the pixels in the :ref:`houghmap`.
In other words, it sums the intensity of every pixels.

.. math::
   
   Q = \sum\limits_{i=0}^{W}{\sum\limits_{j=0}^{H}{I_{ij}}}

where *W* is the width, *H* is the height of the :ref:`houghmap` and 
:math:`I_{ij}` the pixel value at position (*i*, *j*).

Results from this quality metric are similar to the :ref:`pattern-sum`
operation.