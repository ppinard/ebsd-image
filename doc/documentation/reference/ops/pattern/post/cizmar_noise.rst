
.. _cizmar-noise:

Cizmar noise
============

This operation adds noise to the diffraction pattern following the definition
of Cizmar et al. (2008) :cite:`Cizmar2008`.
It consists of the combination of Poisson noise, representing the noise from 
the electron interactions statistics; and Gaussian noise, attributed to the 
camera's electronics:

.. math:

   C_2 = C_1 + \left( \sigma + P\sqrt{C_1} \right) Z

where :math:`C_1` is the original intensity of a pixel, :math:`C_2` the final 
intensity, :math:`\sigma` the standard deviation of Gaussian noise, *P* the 
amplitude of Poisson noise and *Z* a random number normally distributed.

.. figure:: /images/ops/pattern/post/cizmar_noise/noise1.png
   :width: 35%
   
   Example of a simulated diffraction pattern when the Cizmar noise was added.
   The standard deviation for Gaussian noise is 25 and the amplitude of 
   Poisson noise is 8.
.. 

Parameters
----------

The user must defined the standard deviation of Gaussian noise (:math:`\sigma`)
and the amplitude of Poisson noise (*P*).
