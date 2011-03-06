
.. _maskdisc:

Mask disc
=========

The *Mask disc* operation is used to apply a circular mask on the diffraction 
pattern before performing the :ref:`houghtransform`. 
This allows to remove the noise located around the diffraction pattern by 
selecting a region of interest containing high diffraction information.

Parameters
^^^^^^^^^^

The mask is defined by the position (X and Y) of the circle's center and the 
circle's radius. 
The X position is along the width of the pattern and the Y position is along 
the height of the pattern. 

.. figure:: /images/ops/pattern/post/maskdisc/maskdisc1.png
   :align: center
   
   Pattern dimensions: 336x256, mask disc centered at (70, 80), 
   radius of 90 pixels
..

If a value of -1 is specified for the X and Y position, the circular mask will 
be located at the center of the pattern. 
If the radius has a value of -1, the radius will be calculated as being the 
radius of the greatest circle that can be inscribed in the pattern image 
(width/2 or height/2, depending on the aspect ratio).

.. figure:: /images/ops/pattern/post/maskdisc/maskdisc2.png
   :align: center
   
   Pattern dimensions: 336x256, mask disc X position = -1 = 336/2, 
   Y position = -1 = 256/2, radius = -1 = 256/2
..

When a radius less than -1 is specified, the specified value is subtracted from 
the radius calculated for a value of -1. 
For example, a radius of -3 is equivalent to the radius for a value of -1 
minus two pixels.

.. figure:: /images/ops/pattern/post/maskdisc/maskdisc3.png
   :align: center
   
   Pattern dimensions: 336x256, mask disc X position = -1 = 336/2, 
   Y position = -1 = 256/2, radius = -15 = 256/2 - 14
..

