
.. _fourier:

Fourier transform
=================

Quality metric that is computed from the Fourier transform. 
This is based on the method described in Krieger Lassen Ph.D. thesis 
:cite:`KriegerLassen1994`. 
It evaluates the noise present in the diffraction pattern by measuring the 
amount of low frequencies. 
The rational is that high quality patterns should have less noise therefore 
less high frequencies in the spectrum of the Fourier transform. 
Krieger Lassen used this quality index to identify recrystallized and 
non-recrystallized grains in an aluminum sample.

From the Fourier spectrum *S(u,v)* defined as the magnitude of the complex two 
dimensional Fourier transform,

.. math::

   S(u,v) = \left| F(u,v) \right| = \sqrt{\mathbb{R}(F(u,v))^2 + \mathbb{C}(F(u,v))^2}

.. figure:: /images/ops/pattern/results/fourier/fourier_spectrum.png
   :align: center 
   
   Fourier spectrum
..

a measure of the high frequencies can be calculated by assigning a large weight 
to these high frequencies:
 
.. math::

   I = \frac{\sum\limits_{u=-n/2}^{n/2-1}{\sum\limits_{v=-n/2}^{n/2-1}{S(u,v)(u^2+v^2)}}} {\sum\limits_{u=-n/2}^{n/2-1}{\sum\limits_{v=-n/2}^{n/2-1}{S(u,v)}}}
   
In the latter equation, :math:`u^2+v^2` term in the numerator is the weight 
factor. 
The high frequencies, located far from the center, are multiplied by a greater 
factor. It can be seen graphically as a radial mask where the intensity goes 
from 0 at the center to :math:`(\text{width}/2)^2 + (\text{height}/2)^2` at the 
corner of the mask.

.. figure:: /images/ops/pattern/results/fourier/fourier_radialmask.png
   :align: center 
   
   Radial mask
..

This intensity *I* is then normalized by the maximum theoretical intensity 
:math:`I_\text{max}`

.. math::

   I_\text{max} = \frac{1}{n^2} \sum\limits_{u=-n/2}^{n/2-1}{\sum\limits_{v=-n/2}^{n/2-1}{(u^2 + v^2)}}

which is the average of the radial mask.

The result is that the ratio between the intensity and maximum intensity varies 
between 0 for an uniform image and 1 for a white noise image. 
Since it is more intuitive to think of a quality index varying between 0 for a 
noisy pattern and 1 for a sharp pattern, the Fourier quality index is calculated 
as followed:

.. math::

   Q = 1 - \frac{I}{I_\text{max}}
   
.. figure:: /images/ops/pattern/results/fourier/nicocraly_fourier.png
   :align: center 
   
   Example on NiCoCrAlY sample
..

.. bibliography::
