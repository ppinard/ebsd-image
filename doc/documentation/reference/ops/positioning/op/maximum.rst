
.. _maximum:

Maximum
=======

This operation is used to identify the Hough peaks in the peaks map 
(:ref:`binmap`). 
For a given peak, a :ref:`houghpeak` is identified by finding the position of 
the pixel with the highest intensity value inside this peak.
Its intensity of the :ref:`houghpeak` is taken as the intensity of this 
most intense pixel.