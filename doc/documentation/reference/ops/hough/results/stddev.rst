
.. _hough-stddev:

Hough standard deviation
========================

This result calculates the standard deviation from the pixels' intensity of the
:ref:`houghmap`.
The pixels with a value of zero are ignored.

.. math::

   Q = \sqrt{\frac{1}{WH-1}\sum\limits_{i=1}^W{\sum\limits_{j=1}^{H}{\left( I_{ij}-\bar{I} \right)^2}}}

where *W* and *H* are respectively the width and the height of the 
:ref:`houghmap`, :math:`I_{ij}` the intensity at pixel (*i*,*j*) and 
:math:`\bar{I})` the average intensity of all the pixels.