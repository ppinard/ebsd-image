
.. _hough-average:

Hough average
=============

This result calculates the average intensity of the pixels in the 
:ref:`houghmap`.
The pixels with a value of zero are ignored.

.. math::
   
   Q = \frac{1}{W\cdot H} \sum\limits_{i=0}^{W}{\sum\limits_{j=0}^{H}{I_{ij}}}

where *W* is the width, *H* is the height of the :ref:`houghmap` and 
:math:`I_{ij}` the pixel value at position (*i*, *j*).

Results from this quality metric are similar to the :ref:`pattern-average`
operation.