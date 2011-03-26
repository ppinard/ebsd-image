
.. _hough-entropy:

Hough entropy
=============

This result calculates the entropy from the pixels' intensity of the
:ref:`houghmap`.
The pixels with a value of zero are ignored.

.. math::
   
   Q = -\sum\limits_{i=1}^{255}{P_i\ln{P_i}}

where :math:`P_i` is the probability of having a pixel with the color *i* in the
:ref:`houghmap`. 