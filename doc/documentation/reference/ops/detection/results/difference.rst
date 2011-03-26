
.. _localdifference:

Local difference
================

This operation gives 4 results based on the difference between the maximum and 
minimum value inside each detected peak, i.e. the *range* operation is 
applied on every detected peak.
With the difference values of each peak, the 4 results are calculated:
 
  * average
  * standard deviation
  * maximum
  * minimum

Background
----------

From the Bloch wave theory, Kikuchi bands are characterized by a bright center
and dark edges. :cite:`Wilkinson1997`
The difference between the center of a band and its edges is a measure of the
sharpness of the band, and is therefore an estimation of the deformation level.
In Hough space, this is translated into an intense peak corresponding to the
center of the band and two dark regions corresponding to the dark edges.
The difference between the maximum and minimum value inside a peak is therefore
equivalent to the contrast between the center and edges of a band.
It is important for these calculations that the area of the detected peaks
includes both the bright and dark regions of the peak.

.. note::

   The :ref:`opening` operation can be used to increase the area of the detected
   peaks.

From the difference values obtained for each detected peak, the average and
standard deviation can be used to evaluate the overall diffraction quality.
As the deformation level increases, the average difference should decrease as
the band sharpness decreases.
A similar trend is expected for the standard deviation.
For a high quality, undeformed diffraction pattern, the sharpness of the bands
will have a large dispersion due to the variation of intensity in the
Kikuchi bands.
The Kikuchi bands of crystallographic planes with a high diffraction intensity
have greater contrast than those of lower symmetry planes.
However, for a diffraction pattern from a deformed region, the sharpness of all
Kikuchi bands are decreased, thus decreasing the dispersion and lowering the
standard deviation.
Other quality metrics from these difference values can be the maximum and
minimum difference between all the peaks.
We shall refer to this group of four quality metrics as the local difference
metric.