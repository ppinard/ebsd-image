
.. _autohoughtransform:

Auto Hough transform
====================

The Auto Hough transform operation is a special case of the :ref:`houghtransform`
operation. For more general information about the Hough transform, refer to this
:ref:`houghtransform`.

As demonstrated by Krieger Lassen (1994) :cite:`KriegerLassen1994`, the size
and shape of the peaks in Hough space vary based on the width of a Kikuchi 
band and position of its peak in Hough space as well as the selected 
resolutions.
The Auto Hough transform is designed to maintain the aspect ratio of the peaks
close to unity for a large portion of the Hough space. 
Having square peaks is important if the :ref:`butterfly` operation is used to
enhance the peaks in Hough space.
The convolution mask (butterfly mask) used in this operation has a square 
shape.
The enhancement effect of this operation is therefore optimized if the shape
of the peaks matches the one of the convolution mask.

The following paragraphs explain how the Auto Hough transform operation ensures
that peaks have an aspect ratio close to unity for typical widths of Kikuchi 
bands and for a large portion of the Hough space. 

.. contents::
   :local:

Parameters
----------

The only parameter of this operation is the :math:`\theta` resolution 
(:math:`\Delta\theta`).
The Auto Hough transform internally calculates the :math:`\Delta\rho` to 
obtain square peaks.
We found it intuitive to ask the user to specify the :math:`\Delta\theta` and 
calculate the resultant :math:`\Delta\rho`, as the execution time is 
proportional to the former. 

Effect of mask
--------------

To analyze the influence of peak position in the Hough space on the aspect 
ratio, simulated diffraction patterns (rectangular patterns, 672 x 512 px) 
containing only one Kikuchi band (width *b* of 40 px) were used.

.. figure:: /images/ops/hough/op/autohoughtransform/pattern_annotated.png
   :width: 30%
   
   A simulated diffraction pattern
..

The slopes and positions of the simulated Kikuchi bands covered the whole 
Hough space. 
The Hough transform was performed using a :math:`\Delta\theta` resolution of 
1 deg/px and a :math:`\Delta\rho` resolution of 1 px/px. 
Finally, the single peak in Hough space was thresholded and its dimensions 
were used to calculate the aspect ratio (height *h* / width *w*). 

.. figure:: /images/ops/hough/op/autohoughtransform/hough_annotated.png
   :width: 30%
   
   Hough space of the simulated diffraction pattern. 
..

To visualize the variation in aspect ratio, values were colour-coded and 
plotted as a function of the peak position in the Hough space. 

.. figure:: /images/ops/hough/op/autohoughtransform/nomask.png
   :width: 40%
   
   Variation of the aspect ratio for the peaks in Hough space for a rectangular 
   diffraction pattern (672 x 512 px) 
..

Using the same color scale, the analysis was repeated using a circular pattern 
(radius *R* of 256 px) for the region of interest. 

.. figure:: /images/ops/hough/op/autohoughtransform/mask.png
   :width: 40%
   
   Variation of the aspect ratio for the peaks in Hough space for a circular 
   diffraction pattern (radius of 256 px). 
..

The aspect ratio using a circular mask has a much more uniform distribution as 
a function of :math:`\theta` than the one calculated without a mask.
The comparison of these two figures illustrates the importance of selecting a 
circular region of interest from rectangular patterns to eliminate the 
variation of aspect ratio as a function of :math:`\theta`. 

.. note ::

   The :ref:`maskdisc` operation in the Pattern Post operations allows the user
   to apply a circular mask on each diffraction pattern before performing the
   Hough transform.

This variation can be explained by the different possible band lengths in the 
diffraction pattern. 
Oblique bands crossing the centre of a rectangular diffraction pattern are 
longer than horizontal or vertical bands crossing the centre or those near 
the edges. 
This effect is removed by using a circular pattern: the maximum length of the 
bands is determined by the diameter of the pattern. 
The variation as a function of ρ is due to a decrease in the length of the 
bands as they are located further away from the centre of the pattern. 
These results highlight the importance of the circular mask.

Computation
-----------

Krieger Lassen (1994) :cite:`KriegerLassen1994` derived two equations to 
express the height and width of peaks in Hough space for circular diffraction 
patterns. 
For an aspect ratio of unity, the relationship between :math:`\Delta\theta` 
and :math:`\Delta\rho` can be written as:

.. math::

   \Delta\rho = \frac{b}{2\arctan\left(\frac{b}{2\sqrt{R^2 - \rho^2}}\right)} \Delta\theta

where *b* is the width of a Kikuchi band, *R* is the radius of the circular 
pattern and :math:`\rho` is the coordinate of the peak in Hough space . 
The latter is bounded between *-R* and *R*. 
Given the position of the EBSD camera with respect to the sample (pattern 
centre and detector distance), the accelerating voltage and the phases present 
in the sample, the theoretic range of *b* can be determined. 
For example, the width of the ten most intense Kikuchi bands of a pure copper 
sample varies between 16 and 67 px (calculated from 100 random orientations at 
20 keV with a diffraction pattern of 672 by 512 px). 
With these boundaries, an approximation of the proportionality constant between 
:math:`\Delta\theta` and :math:`\Delta\rho` can be calculated by numerically 
integrating the above equation between the width (:math:`b_0 = 16 px` to 
:math:`b_1 = 67 px`) and :math:`\rho` (:math:`\rho_0 = -256 px` to 
:math:`\rho_1 = 256 px`) ranges: 

.. math::

   \Delta\rho = \frac{1}{2(\rho_1-\rho_0)(b_1-b_0)} 
   \int\limits^{b_1}_{b_0}{
     \int\limits^{\rho_1}_{\rho_0}{
       \frac{b}{2\arctan\left(\frac{b}{2\sqrt{R^2 - \rho^2}}\right)} db d\rho
     }
   }
   \Delta\theta

The latter equation ensures that the aspect ratio will be close to unity for 
a large portion of the Hough space, independently of the selected 
:math:`\Delta\theta` resolution and dimensions of the diffraction patterns.

.. warning::

   In the current implementation of the Auto Hough transform operation, the
   integration boundaries are between: :math:`b \in [0.01R, 0.25R]` and
   :math:`\rho \in [-0.9R, 0.9R]`, where *R* is the radius of the circular mask.
   The actual calculation of the boundaries based on the theoretical average
   range of the Kikuchi width will be implemented in future version.

If the experiment with the simulated patterns performed to see the variation
of the aspect ratio is repeated using the :math:`\Delta\rho` calculated by
the previous equation, the following result is obtained:

.. figure:: /images/ops/hough/op/autohoughtransform/integral.png
   :width: 40%
   
   Variation of the aspect ratio of the peaks in Hough space for a diffraction
   pattern of 672 by 512 px, a Kikuchi band with a width of 40 px, 
   :math:`\Delta\theta = 0.1^\circ /px` and a circular mask with a diameter of 
   512 px.
..

The aspect ratio of the peaks for a large portion of the Hough space is close
to unity. 
