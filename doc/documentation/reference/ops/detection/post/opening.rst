
.. _opening:

Opening
=======

The *Opening* operation is used to perform an binary opening on the detected 
peaks. 
By definition, an opening is an erosion followed by a dilation. 
This results in removing small peaks without decreasing the size of the other 
peaks. 

Parameters
----------

This operation takes two parameters to define:

  * minimum number of *OFF* neighbour to an *ON* pixel for it to be turned to 
    *OFF*.
  * Maximum number of *OFF* neighbour to an *ON* pixel for it to be turned to 
    *OFF*.