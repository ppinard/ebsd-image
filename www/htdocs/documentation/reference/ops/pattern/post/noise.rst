
.. _noise:

Noise
=====

The *Noise* operation is used to add random Gaussian noise on the diffraction 
patterns. 
See :ref:`radial-noise` operation for another type of operation to add noise 
to the diffraction pattern.

Parameters
^^^^^^^^^^

The standard deviation of the Gaussian distribution must be specified by the 
user. 
The greater the standard deviation the greater the noise in the diffraction 
patterns will be.

.. figure:: /images/ops/pattern/post/noise/noise_original.png
   :align: center
   
   Original pattern (no added noise)
..

.. figure:: /images/ops/pattern/post/noise/noise_75.png
   :align: center
   
   Pattern with noise (standard deviation of 75)
..

.. figure:: /images/ops/pattern/post/noise/noise_150.png
   :align: center
   
   Pattern with noise (standard deviation of 150)
..
