
.. _houghcrop:

Hough crop
==========

The *Hough crop* operation is used to select a region of the 
:ref:`houghtransform`. 
The :ref:`houghmap` is cropped in :math:`\rho` to the specified value. 
In other words, the map is cropped along its height. 
This operation is particular useful to remove the edges of the Hough transform 
which could be interpreted as false peaks. 

.. figure:: /images/ops/hough/post/houghcrop/houghcrop_before.png
   :align: center

   Original Hough map
..

The user was three options when selecting the cropping :math:`\rho` value. 

  1. Crop to a specified :math:`\rho`. 
     Any value between 0 and the maximum :math:`\rho` of the Hough map is 
     allowed. 
     This option corresponds to entering a positive value in the combo box.

.. figure:: /images/ops/hough/post/houghcrop/houghcrop_140.png
   :align: center

   Crop with a *rho = 140*
..

  2. Crop to a value of :math:`\rho` equals to the radius of the 
     :ref:`maskdisc` operation. 
     This option is automatically selected if the entered value in the combo 
     box is equal to -1.

.. figure:: /images/ops/hough/post/houghcrop/houghcrop_127.png
   :align: center

   Crop with a *rho = -1*
..

  3. Crop to a value of :math:`\rho` equals to the radius of the 
     :ref:`maskdisc` operation minus a specified value. 
     This is equivalent to having a disk mask with a smaller radius. 
     For this option, the user must enter a negative value less than -1. 
     For example, a value of -2 will correspond to :math:`\rho` equals to the 
     disk mask operation's radius minus one pixel.

.. figure:: /images/ops/hough/post/houghcrop/houghcrop_90.png
   :align: center

   Crop with a *rho = -38*
..
