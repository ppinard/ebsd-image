
.. _binning:

:synopsis: asfda

Binning
=======

The *Binning* operation is used to reduce the size of the diffraction patterns. 
Although binning is typically performed during the acquisition stage to reduce 
the integration time, this operation allows the user to further decrease the 
size of the diffraction pattern if needed. 
Calculations intensive tests like the :ref:`houghtransform` are faster on 
smaller patterns. 
This operation can also be used for testing and debugging algorithms.

Parameters
----------

The user needs to specify the reduction factor of the diffraction patterns. 
The factor, of binning size, must be an even number greater than 2.

.. figure:: /images/ops/pattern/post/binning/binning_original.png
   :align: center
   
   Original pattern (336x256)
..

.. figure:: /images/ops/pattern/post/binning/binning_binned.png
   :align: center
   
   Binned pattern by a factor of 4 (84x64)
..