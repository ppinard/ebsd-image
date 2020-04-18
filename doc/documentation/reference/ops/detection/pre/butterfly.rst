
.. _butterfly:

Butterfly
=========

The *Butterfly* operation is used to increase the contrast of the peaks in the
:ref:`houghtransform` by applying a special convolution kernel (shaped as a 
butterfly). 
The convolution kernel is designed to match the shape of a typical Hough peak 
(bright region in the middle and dark regions on top and bottom). 

.. figure:: /images/ops/detection/pre/butterfly/butterfly_before.png
   :align: center

   Original Hough transform
..

.. figure:: /images/ops/detection/pre/butterfly/butterfly_after.png
   :align: center

   Hough transform after the butterfly operation
..

Since the dimension of the peaks depend on the resolution as well as the 
location of the peaks in the Hough transform, it is impossible to have a 
perfect convolution kernel. 
Currently only a 9x9 convolution kernel is available. 
The kernel was taken from Krieger Lassen Ph.D Thesis :cite:`KriegerLassen1994`.

.. math::
   
   \left[ \begin{array}{ccccccccc} 
   -10 & -15 & -22 & -22& -22 & -22 & -22 & -15 & -10 \\ 
   1 & -6 & -13 & -22 & -22 & -22 & -13 & -6 & -1 \\ 
   3 & 6 & 4 & -3 & -22 & -3 & 4 & 6 & 3 \\ 
   3 & 11 & 19 & 28 & 42 & 28 & 19 & 11 & 3 \\ 
   3 & 11 & 27 & 42 & 42 & 42 & 27 & 11 & 3 \\ 
   3 & 11 & 19 & 28 & 42 & 28 & 19 & 11 & 3 \\ 
   3 & 6 & 4 & -3 & -22 & -3 & 4 & 6 & 3 \\ 
   -1 & -6 & -13 & -22 & -22 & -22 & -13 & -6 & -1 \\ 
   -10 & -15 & -22 & -22& -22 & -22 & -22 & -15 & -10 \\ 
   \end{array} 
   \right]

Parameters
----------

This operation has three parameters: the size of the convolution kernel 
(locked at 9x9 in the present version) and two limits. 
The convolution creates an image (:ref:`realmap`) with negative and positive 
values that can potentially range from negative infinity to positive infinity. 
To bring this :ref:`realmap` to a conventional :ref:`houghmap` (ranging from 0 
to 255), a lower and upper limit are set by the user. 
All values lower than the specified lower limit are set to 0 and all values 
higher than the specified upper limit to 255. 
This procedure is called flattening of the :ref:`realmap`. 
The limits depend on the values of the convolution kernel. 
Values of -800 and 800 for the lower and upper limits are typically a good 
choice for the 9x9 convolution kernel.
