
.. _imagequality-inca:

Image quality (INCA)
====================

Quality metric that is computed from the 7 most intense Hough peaks. 
Out of these peaks, the most and the least intense peaks are found and the 
difference between them is calculated. 
This calculation is used in Oxford INCA crystal :cite:`Dicks2009`.
It is very similar to the :ref:`houghpeak-range` calculations except that the 
latter calculates the difference between the least intense peak instead of 
the 7th one.

By ordering the Hough peaks in descending order,

.. math::

   Q = I_1 - I_7
   
where :math:`I_i` is the intensity of peak *i*.

.. bibliography::
