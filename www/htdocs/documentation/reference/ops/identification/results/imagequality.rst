
.. _imagequality:

Image quality
=============

Quality metric that is computed from the maximum intensity of the Hough peaks. 
It represents the average intensity of the Hough peaks. 
High quality diffraction patterns have intense Hough peaks, therefore the 
average intensity will be greater than for low quality patterns.

.. math::

   Q = \frac{1}{N}\sum\limits_{i=0}^{N-1}{H_\text{max} \left( \rho_i, \theta_i \right)}
   
where *N* is the number of identified Hough peaks, 
:math:`H_\text{max}\left( \rho_i, \theta_i \right)` maximum intensity of Hough 
peak *i*.

This quality index was used by Wright and Nowell :cite:`Wright2006`. 
They summarized this quality index as:

  * Image quality differences from grain orientations are typically much 
    smaller than those due to phase, grain boundaries and strain
  * Good differentiation between phases, grain boundaries and strain

.. figure:: /images/ops/identification/results/imagequality/nicocraly_iq.png
   :align: center

   Example on NiCoCrAlY sample
..

.. bibliography::
