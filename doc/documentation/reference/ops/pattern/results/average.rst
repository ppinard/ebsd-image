
.. _pattern-average:

Pattern average
===============

Quality metric that is computed from the average of all the pixels in the 
diffraction pattern. 
The pixels with a value of zero are ignored, so that the pixels removed from 
the :ref:`maskdisc` operation are ignored.

.. math::
   
   Q = \frac{1}{W\cdot H} \sum\limits_{i=0}^{W}{\sum\limits_{j=0}^{H}{I_{ij}}}

where *W* is the width of the pattern, *H* is the height of the pattern and 
:math:`I_{ij}` the pixel value at position (*i*, *j*).

This quality index was used by Wright and Nowell :cite:`Wright2006`. 
They summarized this quality index as:

  * a measure of the overall backscatter yield
  * related to surface topography
  * show scratches, microtwins and small strain levels
  * affected by contamination, gain, contrast settings and current drift

.. figure:: /images/ops/pattern/results/average/nicocraly_average.png
   :align: center
   
   Example on NiCoCrAlY sample
..

