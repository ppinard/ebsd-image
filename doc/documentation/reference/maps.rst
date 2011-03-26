
.. _maps:

Maps
====

The term *Map* is an abstraction used to describe any kind of image or two 
dimensional array of data loaded in EBSD-Image. 
By definition, a map has a specified width and height and contains an array 
of values (pixels). 
Different type of maps exist depending on the type of values stored in them.

RML-Image and EBSD-Image define various types of map, each of them having 
their own distinctive features and purposes. 
The user should get familiar with these types of map since the distinctions 
between them is the core of this image analysis software.

===================================== =================== ====================================
Map                                   Type of pixel       Uses
===================================== =================== ====================================
:ref:`binmap` (Binary map)            Binary (0 or 1)     Thresholding, masks 
:ref:`bytemap` (Byte map)             8-bit (0 to 255)    Greyscale images
:ref:`identmap` (Identification map)  16-bit (0 to 65534) Objects map 
:ref:`rgbmap` (Color map)             24-bit              Any color image 
:ref:`phasemap` (Phase map)           8-bit (0 to 255)    Identify phases in EBSD acquisition
:ref:`errormap` (Error map)           8-bit (0 to 255)    Report error(s) occured during the 
                                                          processing of diffraction patterns
:ref:`realmap` (Real map)             32-bit float        Store real values in a map 
:ref:`houghmap` (Hough map)           8 bit (0 to 255)    Map resulting from the 
                                                          :ref:`houghtransform` 
:ref:`multimap` (Multimap)            N/A                 Aggregate of other maps all having 
                                                          the same dimensions 
:ref:`ebsdmmap` (EBSD multimap)       N/A                 A multimap with extra features 
                                                          for EBSD 
:ref:`hklmmap` (HKL Channel 5)        N/A                 An EBSD multimap adapted for HKL 
                                                          Channel 5 mappings 
:ref:`tslmmap` (TSL OIM)              N/A                 An EBSD multimap adapted for TSL 
                                                          OIM mappings 
===================================== =================== ====================================

.. toctree::
   :hidden:
   :glob:
   
   maps/*