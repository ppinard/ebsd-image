
.. _radial-noise:

Radial noise
============

The *Radial noise* operation is an operation to add noise to the diffraction 
patterns. 
In opposite of the operation :ref:`noise`, the intensity of the noise is not 
uniformed throughout a diffraction pattern.  
It allows to have a diffraction pattern with more noise around the edges than 
in the center. 

Parameters
^^^^^^^^^^

The parameters of this operation can be divided into two categories. 
The first is to define the two dimensional distribution of the noise on the 
diffraction pattern. 
The user can select the location of the center (X and Y) and the width and 
height with the standard deviation in X and Y. 

The second category is to define the initial and final intensity of the noise 
level. 
This corresponds to the standard deviation of the random gaussian noise. 
The greater the standard deviation the greater the noise in the diffraction 
patterns will be. The initial intensity corresponds to the intensity at the 
center.
