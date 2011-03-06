
.. _multimap:

MultiMap
========

A *MultiMap* is a special kind of :ref:`maps <maps>` in the sense that it 
contains several maps having the same dimensions (width and height). 
The maps can be of different types. 
In other words, a *MultiMap* is a container of many maps. 

This type of map is especially useful when comparing different data coming 
from the same mapping, in occurrence an EBSD mapping (see :ref:`ebsdmmap`). 
A *MultiMap* is stored in a ZIP file containing the files of all the internal 
maps. 
The user can therefore extract or view a given map without using EBSD-Image. 
In many operating system such as Windows, the files can be viewed directly 
within the ZIP.
