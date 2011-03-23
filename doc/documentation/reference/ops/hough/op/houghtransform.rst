
.. _houghtransform:

Hough transform
===============

The Hough transform is used to identify Kikuchi bands from the diffraction 
pattern. 
It is an image processing algorithm to facilitate the detection of lines inside 
an image. 
The transformation converts a line (1 pixel thick) in the image space into a 
point in the Hough space. 
In EBSD diffraction pattern, the Kikuchi bands are converted to a peak shaped 
as a butterfly. 

Computation
-----------

The transformation is the conversion of the image space with axes *x* and *y* 
to the Hough space with axes :math:`\theta` and :math:`\rho`. 
Each pixel (*x*, *y*) of the image is represented as a sinusoidal curve in the 
Hough space.

.. math::

   \rho = x \cos\theta + y \sin\theta

Schematically, :math:`\rho` is the shortest distance between the origin (center 
of the image) and the pixel (*x*, *y*). 
By varying :math:`\theta` between 0 and :math:`\pi` the sinusoidal curve is 
drawn in the Hough space with the intensity corresponding to the intensity of 
the pixel in the image. 
This process is repeated for every pixel in the image. 
The intensities of the sinusoidal curves are added. 

.. note::

   In EBSD-Image, :math:`\theta \in [0, \pi[` and :math:`\rho \in ]-\mathbb{R}, \mathbb{R}[`.

The result is that the sinusoidal curves calculated from the pixels on a line 
in the image will intersect at a specific point in the Hough space. 
If *AB* corresponds to the shortest segment between the origin and the line in 
the image space, the coordinates (:math:`\theta`, :math:`\rho`) are 
respectively the angle between this segment and the *x* axis of the image, and 
the length of this segment.

Note that the values of :math:`\cos\theta` and :math:`\sin\theta` between 
:math:`\theta \in [0, \pi[` are pre-calculated since they are the same for all 
the pixels.

Resolution
----------

Resolution of the Hough transform is an important parameter since it doesn't 
not only affects the precision but also the time required to compute the 
transformation. 
In its pure form, the Hough transform converts the image, a quantized space 
(image consists of pixels), into the Hough space, a continuous space. 
However, this continuous space must be converted back to a quantized space to 
be analyzed and stored as an image, in occurrence a :ref:`houghmap`. 
The resolution of the Hough transform is therefore how to quantize the two axes 
of the Hough space. 
We shall refer to :math:`\Delta\theta` and :math:`\Delta\rho` for the 
resolution in :math:`\theta` and :math:`\rho` respectively.

*TODO Complete explanation*

In Hough transform operation in EBSD-Image, the user is only selecting the 
:math:`\theta` resolution. 
In other words, the width of the :ref:`houghmap`. 
The :math:`\rho` resolution is automatically calculated such as the region of 
the biggest inscribed :ref:`mask disc <maskdisc>` will give a square region in 
the :ref:`houghmap`. 
In other words, :math:`\Delta\rho` is linked with :math:`\Delta\theta` and the 
size of the image, i.e. diffraction pattern.

