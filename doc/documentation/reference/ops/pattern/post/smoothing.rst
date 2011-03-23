
.. _smoothing:

Smoothing
=========

The *Smoothing* operation is used to blur the diffraction patterns. 
It consists in applying an uniformly filled convolution kernel on the 
diffraction pattern. 

Parameters
^^^^^^^^^^

The size of the kernel is specified by the user. 
It must be an odd value greater than 1. 
The greater the kernel size the blurrier will be the diffraction pattern. 

.. figure:: /images/ops/pattern/post/smoothing/smoothing_original.png
   :align: center
   
   Original pattern
..

.. figure:: /images/ops/pattern/post/smoothing/smoothing_11.png
   :align: center
   
   Smoothed pattern with a convolution kernel of 11x11
..
