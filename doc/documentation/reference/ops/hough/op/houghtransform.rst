
.. _houghtransform:

Hough transform
===============

The Hough transform is used to identify Kikuchi bands from the diffraction 
pattern. 
It is an image processing algorithm to facilitate the detection of lines inside 
an image. 
The transformation converts a line (1 pixel thick) in the image space into a 
point in the Hough space. 

.. contents::
   :local:

Parameters
----------

The Hough transform operation takes two parameters, the two resolutions of
the Hough transform (:math:`\Delta\theta` and :math:`\Delta\rho`).

Formulation
-----------

The image space is the one of the diffraction pattern where the origin is taken 
at the center of the image.
It is a discrete space made up of a certain amount of pixels in the x and y 
directions. 
The intensity of those pixels can be seen as the third dimension. 
Similarly, the Hough space has three dimensions. 
The x and y axes of the image space are replaced by the :math:`\theta` and 
:math:`\rho` axes while the third dimension now represents the intensity of 
the Hough space. 
By definition, the Hough space is continuous since within their boundaries 
:math:`\theta` and :math:`\rho` can take any value. 
The Hough space is quantized to allow for computerized treatment.
As for the image space, the discrete Hough space has a certain amount of pixels 
in the :math:`\theta` and :math:`\rho` direction, namely :math:`\Delta\theta`
and :math:`\Delta\rho`.

Computation
-----------

The transformation is performed by calculating :math:`\rho` using the following 
equation for each pixel :math:`(x_i,y_i)` in the image space and for each 
:math:`\theta_j` in the Hough space, where the subscript *i* refers to the index
of the pixels in the image space and *j* to the index of the pixels in the Hough
space :cite:`Duda1972`.

.. math::

   \rho = x_i\cos\theta_j + y_i\sin\theta_j

Effectively, this transformation converts each pixel of the image space into a
sinusoidal curve in the Hough space.
The calculated :math:`\rho` value is rounded to the closest pixel 
:math:`\rho_j`.
The intensity of the pixels :math:`(\theta_j,\rho_j)` that are part of the 
sinusoidal curve are augmented by the intensity of the corresponding pixel
:math:`(x_i,y_i)` in the image space.
The accumulation of these intensities give rise to peaks in the Hough space
which corresponds to the :math:`\theta` and :math:`\rho` coordinates of the 
bands in the image space.

The understanding of these results is not straightforward.
An obvious question is why sinusoidal curves of individual, uncorrelated pixels
in a band intersect in the Hough space at a specific and unique position?
To answer this question, we shall refer to the following figure where
the image and Hough space are respectively shown on the left and right of the
figure.

.. figure:: /images/ops/hough/op/houghtransform/hough-expl1.png
   :width: 90%
   
   Schematic representation of the image space (left) with a band *L* and a pixel 
   *A* and the Hough space (right) with the corresponding sinusoidal curve.
..

From the definition of the Hough transform, each pixel in the image space is
transformed into a sinusoidal curve in the Hough space.
The curve represents all the possible unidimensional lines that can be passing
through that pixel in the image space.
A few lines are drawn in the figure above with their corresponding position 
in Hough space represented by circle markers.
Only a small fraction of the lines are fully contained in the band,
the rest of the lines cross it, but most of their pixels are outside the
band.

If this geometrical construction is repeated for another pixel, *B*, of
the band *L*, the same result is obtained.
In the following figure, the lines passing by *B* and their equivalent 
representation in Hough space using triangular marker.
All the lines or curves related to pixel *B* are drawn as dashed lines.

.. figure:: /images/ops/hough/op/houghtransform/hough-expl2.png
   :width: 90%
   
   Schematic representation of the image space (left) with a band *L* and 
   pixels *A* and *B*, and the Hough space (right) with the two corresponding 
   sinusoidal curves.
..

The lines inside of band *L* and passing by pixel *B* are the same lines that 
are also passing by pixel *A*.
In Hough space, these lines end up having the same coordinates :math:`\theta` 
and :math:`\rho`, forming a peak.
The intersection of the sinusoidal curves therefore corresponds to the lines
that are fully inscribed inside the band in the image space.
The intensity at this intersection is higher than the background because of
two interlinked reasons:

  * the sinusoidal curve of the pixels in the band have a higher intensity that
    the one of the pixels outside of it
  * the intensity of many sinusoidal curves is added at this intersection.

If the band would have a width of 1 px, the area covered by its corresponding 
peak in Hough space would be approximately equal to 1 px:sup:`2`
:cite:`KriegerLassen1994`.
However, the bands in a diffraction pattern are wider than 1 px.
This results in the formation of a peak covering a large area.
The center of a peak corresponds to the center of its corresponding band.
From our previous explanation, the height and width of the peak will depend on
the lines that pass through the pixels of the band and that are fully
inscribed inside it.
The operation :ref:`autohoughtransform` tries to minimize this phenomenon
by properly selecting the :math:`\Delta\rho` for a given :math:`\Delta\theta`.
More explanations are given in the operation :ref:`autohoughtransform <page>`.

Moving away from the conceptual Hough transform, the following figures show
an experimental diffraction pattern of a silicon single crystal and its 
Hough space representation.

.. figure:: /images/ops/hough/op/houghtransform/hough-exp1.png
   :width: 30%
   
   Diffraction pattern of a silicon single crystal.
..

.. figure:: /images/ops/hough/op/houghtransform/hough-exp2.png
   :width: 30%
   
   Corresponding Hough space of the diffraction pattern.
..

The location of the most intense Kikuchi bands can be clearly identified in 
Hough space by the bright peaks while other peaks are more faint and barely
noticeable.
It is the task of the peak detection algorithm to segment out the high intensity
peaks from the background and disregard possible false peaks.
The segmentation of the Hough space is shown in the following figure:

.. figure:: /images/ops/hough/op/houghtransform/hough-exp3.png
   :width: 30%
   
   Segmentation of the peaks in Hough space.
..

To evaluate the result, the corresponding line of each peak in the previous 
figure is overlaid on the original diffraction pattern. 
The lines and the peaks are colour-coded to illustrate their relationship.

.. figure:: /images/ops/hough/op/houghtransform/hough-exp4.png
   :width: 30%
   
   Overlay of the corresponding lines of the segmented peaks on the
   diffraction pattern.
..

Specifications
--------------

In EBSD-Image, :math:`\theta` is varied between :math:`[0, \pi[` and 
:math:`\rho` can take value between :math:`]-\mathbb{R}, \mathbb{R}[`.
The width and height of the :ref:`houghmap` are adjusted according to these
ranges.

To prevent biasing effects as reported by Tao & Eades (2005) :cite:`Tao2005`, 
the intensity at each coordinate :math:`\theta` and :math:`\rho` in the Hough 
space is equal to the average (instead of the sum as originally described by 
Krieger Lassen :cite:`KriegerLassen1994`) intensity of all the sinusoidal 
functions passing through this coordinate. 
The intensity of a coordinate in the Hough space is therefore the average 
intensity of the pixels along its corresponding line in the image space. 

.. bibliography::

