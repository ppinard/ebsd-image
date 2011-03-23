
.. _houghmap:

HoughMap
========

A *HoughMap* is an 8-bit quantitization of the Hough space after performing 
a :ref:`Hough transform <houghtransform>` on a :ref:`bytemap`. 
The theta axis corresponds to the x-axis and the rho axis to the y-axis of 
the image. 
The width of a pixel in the *HoughMap* is equal to the resolution in theta and 
the height of a pixel to the resolution in rho. 
Therefore, the *HoughMap* can be understood as a 8-bit map (i.e. a 
:ref:`bytemap`) with a given resolution in x and y.

A *HoughMap* is saved in a BMP file and a property file containing the 
resolution in theta and rho.
