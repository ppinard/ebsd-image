
.. _peaks-count:

Peaks count
===========

Quality metric that is computed from the number of bands/peaks identified in 
the Hough transform. 
The rational is that the Kikuchi bands of high quality diffraction patterns 
should be easier to identify in the Hough transform than those of lower quality 
patterns. 
This quality is however dependent on the thresholding and peaks identification 
operations. 
It is also orientation dependent and is largely influence by false positive 
peaks.

.. math::

   Q = N
   
where *N* is the number of peaks identified in the Hough transform

.. figure:: /images/ops/identification/results/count/nicocraly_peakscount.png
   :align: center

   Example on NiCoCrAlY sample
..
