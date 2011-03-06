
.. _automaticstddev:

Automatic standard deviation
============================

The *Automatic standard deviation* operation is to detect the peaks in the 
:ref:`houghtransform` using the intensity distribution. 
First the average :math:`\mu` and the standard deviation :math:`\sigma` of all 
the pixels in the Hough transform are calculated. 
Then, the pixels which are above the average by a certain amount of times the 
standard deviation are selected as being the Hough peaks. 
In other words, the pixels above :math:`\mu + f\sigma` are detected as peaks, 
where *f* is the sigma factor.

.. figure:: /images/ops/detection/op/automaticstddev/auto_stddev.png
   :align: center
   :width: 40%

   Hough map with peaks detected with *f = 1* in red, *f = 2* in green and 
   *f == 5* in blue
..

Parameters
----------

The user must specify the sigma factor. 
Typical factor could be between 1 and 3. 
Of course, higher the sigma factor higher is the confidence that the selected 
pixels are Hough peaks. 
However, if the sigma factor is too high, no peaks will be detected.
