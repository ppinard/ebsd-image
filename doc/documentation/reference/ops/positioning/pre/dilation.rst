
.. _dilation:

Dilation
========

The *Dilation* operation is used to increase the size of the detected Hough 
peaks before the positioning operation. 
After the detection operation such as the :ref:`automatictophat`, it often 
happens that the area detected to represent a peak does not cover the whole 
peak. 
By increasing this area, one can make sure that the positioning operation 
will be able to properly identify the location of the peaks and its intensity.

Parameters
----------

This operation takes two parameters to define:

  * minimum number of *ON* neighbour to an *OFF* pixel for it to be turned to 
    *ON*.
  * Maximum number of *ON* neighbour to an *OFF* pixel for it to be turned to 
    *ON*.
