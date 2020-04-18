
.. _doublepeakscleanup:

Double peaks clean-up
=====================

.. warning::

   This operation must be used in tandem with :ref:`thetaexpand` operation in
   the peak detection pre-operations.

The representation of the continuous Hough space in an image leads to another 
type of artifact: double peaks.
For example, a perfectly vertical Kikuchi band in a diffraction pattern located
near the left side of the pattern has two solutions after the Hough
transformation: one near 0 deg with a negative :math:`\rho` or another near
180 deg with a positive :math:`\rho`.
In reality the Hough space is circular in :math:`\theta`, but when represented 
in an image it is bounded between 0 and 180 deg.
These two positions are therefore equivalent.
If this effect is not taken into consideration, two Kikuchi bands will be
detected instead of one.
There is also the possibility that the detection algorithm will fail to detect
any peak since they are incomplete.

To prevent this problem for vertical Kikuchi bands, two operations must be
performed in tandem.
First, the :ref:`houghmap` is expanded in :math:`\theta` pass 180 deg by a 
certain amount, typically 5 to 10 deg by the :ref:`thetaexpand` operation.
One half of the split peaks near 180 deg is now complete and can be properly
detected on its own.

.. figure:: /images/ops/positioning/post/doublepeakscleanup/expand-before.png
   :width: 40%
   
   :ref:`houghmap` before expansion in :math:`\theta`. 
   A peak is split in two.
..

.. figure:: /images/ops/positioning/post/doublepeakscleanup/expand-after.png
   :width: 40%
   
   Expansion of the :ref:`houghmap` in :math:`\theta`. 
   The split peak is reconstructed.
..

Then, after identifying the position of all peaks in the :ref:`houghmap`, peaks
found in the expanded region of the :ref:`houghmap` are brought back
inside the original :math:`\theta` range (0 to 180 deg).
This is automatically performed when the :ref:`houghpeak` are created as the
:math:`\theta` value of the peaks must be between 0 and 180 deg.

The *Double peaks clean-up* operation removed peaks that are approximately
located at the same position. 

Parameters
----------

A peak is said to be approximately located at the same position of another 
peak if the amount of pixels in the :math:`\theta` and :math:`\rho` direction
is less than a certain threshold. 
This operation therefore takes two parameters: the maximum spacing in 
:math:`\theta` and :math:`\rho` between two peaks to consider them as 
separate peaks.
