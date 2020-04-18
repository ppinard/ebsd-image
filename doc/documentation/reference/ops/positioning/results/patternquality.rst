
.. _patternquality:

Pattern quality
===============

Quality metric that is defined in the Oxford's EBSD explained brochure. 
:cite:`Oxford2004`
It consists in the average of the 3 most intense :ref:`houghpeak`'s divided by 
the standard deviation of the :ref:`houghmap`.

By ordering the Hough peaks in descending order,

.. math::

   Q = \frac{1}{3\sigma_h} \sum\limits_{i=1}^3{I_i}
   
where :math:`\sigma_h` is the standard deviation of the :ref:`houghmap` and 
:math:`I_i` is the intensity of peak *i*.
