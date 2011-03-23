
.. _inversion-division:

Inversion / Division
====================

The *Inversion Division* operation is used to increase the contrast of the 
peaks in the :ref:`houghmap`. 
It consists in dividing the Hough Map by its inverse and multiplying all the 
pixels by 128. 

.. figure:: /images/ops/detection/pre/inversion_division/invdiv_before.png
   :align: center

   Original pattern
..

.. figure:: /images/ops/detection/pre/inversion_division/invdiv_inv.png
   :align: center

   Inversion of the original pattern
..

.. figure:: /images/ops/detection/pre/inversion_division/invdiv_after.png
   :align: center

   Final result, division of the original by the inverted pattern
..
