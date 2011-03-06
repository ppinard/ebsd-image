
.. _opening:

Opening
=======

The *Opening* operation is used to perform an binary opening on the detected 
peaks. 
By definition, an opening is an erosion followed by a dilation. 
This results in removing small peaks without decreasing the size of the other 
peaks. 
In EBSD-Image, this corresponds to doing one opening with a minimum and maximum 
number of neighbors to 2 and 8 respectively. 
