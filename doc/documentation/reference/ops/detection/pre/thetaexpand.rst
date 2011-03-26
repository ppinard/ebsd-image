

.. _thetaexpand:

Theta expand
============

.. warning::

   This operation must be used in tandem with :ref:`doublepeakscleanup` 
   operation in the peak identification post-operations.
   
This operation expands the right side of the :ref:`houghmap` pass 180 deg.
No new :ref:`houghtransform` is performed. 
The :math:`\Delta\theta` and :math:`\Delta\rho` resolution are unaffected.
The expansion duplicates and rearranges the information present on the left
side of the :ref:`houghmap`. 

Parameters
----------

This operation takes one parameter: the angular increment to add on the right
side of the :ref:`houghmap`.
In other words, by how many degrees the original :math:`\theta` range of the 
HoughMap (0 to 180 deg) must be extended.