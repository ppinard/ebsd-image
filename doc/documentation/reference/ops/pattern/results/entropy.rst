
.. _pattern-entropy:

Pattern entropy
===============

Quality metric that is computed from the entropy of all the pixels in the 
diffraction pattern. 
The pixels with a value of zero are ignored, so that the pixels removed from 
the :ref:`maskdisc` operation are ignored (Note the sum starts at 1 instead 
of 0).

.. math::
   
   Q = -\sum\limits_{i=1}^{255}{P_i\ln{P_i}}

where :math:`P_i` is the probability of having a pixel with the color *i*. 

This quality index was used by Wright and Nowell :cite:`Wright2006` and 
Tao and Eades :cite:`Tao2005`. 
They summarized this quality index as:

  * Related to the compressability of information
  * Describe the 'business' of an image
    
    * Low entropy image: Low contrast, large number of pixels with same or 
      similar values
 
  * Similar results than :ref:`imagequality`
  * Similar results than :ref:`pattern-stddev`

.. figure:: /images/ops/pattern/results/entropy/nicocraly_entropy.png
   :align: center
   
   Example on NiCoCrAlY sample
..
