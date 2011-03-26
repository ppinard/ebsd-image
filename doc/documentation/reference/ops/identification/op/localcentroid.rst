
.. _localcentroid:

Local centroid
==============

This operation is used to identify the Hough peaks in the peaks map 
(:ref:`binmap`). 
For a given peak, a :ref:`houghpeak` is identified by calculating the 
geometric centroid of this peak. 
Its intensity is taken as the value of the closest pixel to the centroid.