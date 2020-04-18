
.. _pattern-stddev:

Pattern standard deviation
==========================

Quality metric that is computed from the standard deviation of all the pixels 
in the diffraction pattern. 
The pixels with a value of zero are ignored, so that the pixels removed from 
the :ref:`maskdisc` operation are ignored.

.. math::

   Q = \sqrt{\frac{1}{WH-1}\sum\limits_{i=1}^W{\sum\limits_{j=1}^{H}{\left( I_{ij}-\bar{I} \right)^2}}}

where *W* and *H* are respectively the width and the height of the diffraction 
pattern, :math:`I_{ij}` the intensity at pixel (*i*,*j*) and 
:math:`\bar{I})` the average intensity of all the pixels.

This quality index was used by Wright and Nowell :cite:`Wright2006` and 
Tao and Eades :cite:`Tao2005`. 
They summarized this quality index as:

  * Measure of the image contrast
  * Affected by contamination, gain, contrast settings and noise

.. figure:: /images/ops/pattern/results/stddev/nicocraly_stddev.png
   :align: center
   
   Example on NiCoCrAlY sample
..
