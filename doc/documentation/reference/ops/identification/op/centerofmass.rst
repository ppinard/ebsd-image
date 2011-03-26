
.. _centerofmass:

Center of mass
==============

This operation is used to identify the Hough peaks in the peaks map 
(:ref:`binmap`). 
It was proposed by Krieger Lassen (1998). :cite:`KriegerLassen1998`
The center of mass is calculated using the intensity of the pixels as follow:

.. math::

    \left<\left(\theta, \rho\right)\right> =
    \left(\frac{\sum\limits_i^N{\theta_iI_i}} {\sum\limits_i^N{I_i}},
    \frac{\sum\limits_i^N{\rho_iI_i}} {\sum\limits_i^N{I_i}}\right)

Its intensity is taken as the value of the closest pixel to the center of mass.

.. bibliography::
