
.. _cleanedge:

Clean edge
==========

The *Clean edge* operation is to remove double peaks present in the 
:ref:`houghmap` near the left side of the map (:math:`\theta = 0`) and near the
right side of the map (:math:`\theta = \pi`). 
These peaks corresponds to the vertical or close to vertical Kikuchi bands in 
the diffraction pattern. 
To prevent these peaks to be used twice in the identification and indexing 
operations, the peaks touching the right side of the :ref:`houghmap` are 
removed by this operation.
