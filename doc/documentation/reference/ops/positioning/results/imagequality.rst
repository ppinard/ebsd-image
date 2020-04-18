
.. _imagequality:

Image quality
=============

Quality metric that is computed from the maximum intensity of the Hough peaks. 
It represents the average intensity of the Hough peaks. 
High quality diffraction patterns have intense Hough peaks, therefore the 
average intensity will be greater than for low quality patterns.

.. math::

   Q = \frac{1}{N}\sum\limits_{i=0}^{N-1}{I_i}

where *N* is the number of identified peaks, and :math:`I_i` is the intensity
of peak *i*.

This quality index was used by Wright and Nowell :cite:`Wright2006`. 
They summarized this quality index as:

  * Image quality differences from grain orientations are typically much 
    smaller than those due to phase, grain boundaries and strain
  * Good differentiation between phases, grain boundaries and strain

.. figure:: /images/ops/positioning/results/imagequality/nicocraly_iq.png
   :align: center

   Example on NiCoCrAlY sample
..
