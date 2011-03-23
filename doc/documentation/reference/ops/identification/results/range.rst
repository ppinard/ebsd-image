
.. _houghpeak-range:

Hough peak range
================

Quality metric that is computed from the minimum and maximum intensity peaks of 
the :ref:`houghtransform`. 
This operation is different from the :ref:`hough-range` operation since the 
subtraction is performed between the intensity of the Hough peaks instead of 
the intensity of any pixels in the Hough transform. 
However, the same rational applies.
The difference between the most intense peak and the least intense peak should 
be greater for a high quality diffraction pattern since there should be a 
higher contrast between the Kikuchi bands.

By ordering the Hough peaks in descending order,

.. math::

   Q = H_\text{max}(\rho_0, \theta_0) - H_\text{max}(\rho_{(N-1)}, \theta_{(N-1)}
   
where :math:`H_\text{max}(\rho_i, \theta_i)` is the maximum intensity of the 
Hough peak *i*.

.. figure:: /images/ops/identification/results/range/nicocraly_houghpeakrange.png
   :align: center

   Example on NiCoCrAlY sample
..
