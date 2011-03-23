
.. _phasemap:

PhaseMap
========

A *PhaseMap* is a map to identify the location on different phases in a EBSD 
mapping as well as non-indexed pixels. 
Internally, a *PhaseMap* is saved in a 8-bit map (:ref:`bytemap` and an 
XML file). 
All pixels corresponding to a phase have the same value. 
Up to 255 phases can be saved. 
The first phase has a value of 1. 
All non-indexed pixels have a value of 0. 
The XML file contains the description of each phase: name, unit cell, 
atom positions and symmetry.

.. figure:: /images/maps/phasemap.png
   :width: 500
   :align: center
   
   Phase 1 in brown, phase 2 in pink, non-indexed pixels in black
..
