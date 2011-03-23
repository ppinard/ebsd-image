
.. _hough-range:

Hough range
===========

Quality metric that is computed from the minimum and maximum intensity of the
:ref:`houghtransform`. 
The operation is performed before any enhanced of the Hough transform such as 
the :ref:`butterfly` convolution. 
It is equal to the subtraction of the most intense pixel of the Hough transform 
by the least intense pixel of the Hough transform. 
The rational is that low quality patterns have a lower dispersion since no 
peaks are present.

.. math:: 

   Q = H_\text{max} - H_\text{min}

where *H* is the Hough transform space.

.. figure:: /images/ops/hough/results/range/nicocraly_houghrange.png
   :align: center

   Example on NiCoCrAlY sample
..
