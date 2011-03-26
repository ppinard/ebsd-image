
.. _shapefactor:

Shape factor
============

This operation removes peaks that have an aspect ratio greater than a certain
value. 
If the :ref:`autohoughtransform` is used, the peaks in the Hough space should
have an aspect ratio close to unity as special considerations are taken to
ensure that most of the peaks are square.
This characteristic of the peaks can be used to remove wrongly detected peaks 
- peaks with an aspect ratio much larger than unity can be eliminated.
In short, this operation takes advantage of the proper selection of the Hough
transform resolutions and offers the possibility to clean the detected peaks
based on their shape.

Parameter
---------

This operation takes one parameter: the threshold value of the aspect ratio.
The aspect ratio of the peaks varies between 1 and positive infinity. 
Typical values for the threshold aspect ratio are therefore between 1.5 and 3, 
assuming that the peaks should be almost perfectly squared.