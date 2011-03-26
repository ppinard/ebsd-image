
.. _ebsd-software:

Other third-party EBSD software
===============================

Apart from commercial software, there are several third-party software that can
be found for post-processing of EBSD mappings using the different output files
of commercial software.
A quick overview of these software and their applications is detailed in this
section.

*VMAP* designed by Humphreys :cite:`Humphreys2004` is available upon
request.
It is a complement to the HKL post-treatment software.
Orientation maps, pole figures, identification of high and low angle
boundaries, and mis-orientation are some of its features.

Another visualization program is `MTEX <http://code.google.com/p/mtex/>`_, 
a quantitative texture analysis toolbox for MATLAB :cite:`Hielscher2008`.
It uses MATLAB computing and plotting capabilities to calculate pole figures,
orientation distribution functions (ODF) and different representations of EBSD
maps from Euler angles, Rodriguez, axis-angle, \etc.
Scripting to process many data sets is possible via the MATLAB interface.
This toolbox is an active open-source project.

Another open source project is `open-ebsd <http://code.google.com/p/open-ebsd/>`_.
It is dedicated to the analysis and visualization of three dimensional EBSD data;
3D EBSD consists of the acquisition of several slices of the same area.
This is often performed with a dual beam microscope (combination of a focused
ion beam column with a SEM).
It implements correction algorithms for misindexed data points, misalignment
between slices, clean-up procedures, and grain detection for 3D data sets.
%instead of the and, you could end this sentence with an etc

*ARPGE* (Automatic Reconstruction of Parent Grains from EBSD data) is a
program to automatically reconstruct parent grains from orientation
relationships :cite:`Cayron2004`.
For example, prior austenite grains of a bainitic steel using the
Nishiyama-Wassermann orientation relationship.
This reconstruction is performed in three steps:

  * identification of the grains in the EBSD mapping (referred to as daughter 
    grains)
  * nucleation of parent grains sites according to the selected orientation 
    relationship(s)
  * growth of the parent grains up to a specified tolerance angle.

The software is sold by the French Atomic Energy and Alternative Energies
Commission (CEA).

`CrossCourt <http://www.blgproductions.co.uk/>`_ is the software developed 
following the publication of the article on lattice strain measurement by 
Wilkinson and Dingley :cite:`Wilkinson2006`.
Using a cross-correlation between the a priori recorded diffraction patterns,
the software calculates the strain and lattice rotation tensor as well as the
local strain to a precision of 1e-4 radians.
*CrossCourt 3.0* is commercially distributed by the BLG Productions company.

.. bibliography::
