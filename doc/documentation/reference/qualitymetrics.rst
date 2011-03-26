
.. _qualitymetrics:

Quality metrics
===============

One method of obtaining more information from diffraction patterns is the
calculation of quality metrics to evaluate how well a sample is diffracting. 
The following paragraphs give the definition of common quality metrics and 
summarize some of their applications published in the literature.

Definition
----------

From an EBSD point of view, *quality* is a parameter that quantifies the 
crystallographic uniformity within the interaction volume.
Different features of a diffraction pattern can be evaluated to assess its
quality: intensity, contrast, sharpness of Kikuchi bands, noise level, etc.
As such, different quality metrics can be designed to highlight these features
for each diffraction pattern in a mapping.
The metrics evaluate the quality of a diffraction pattern by calculating a
numerical value representing the amount, strength or intensity of one or many
specific features.
An interesting parallel can be made between different quality metrics
obtained from diffraction patterns and the different electron signals found in
modern SEMs, namely secondary (upper or lower detector) and backscattered
electrons detector (low and high angle).
Electron signals give different ways of evaluating the interaction of the
electron beams with the sample, whereas quality metrics offer a similar
evaluation of the diffraction of the primary electrons with the
crystallographic structure. 

Quality metrics are complementary information to the crystallographic
orientation obtained by pattern indexing.
Other applications of quality metrics will be detailed in this section.
They include examples for:

  * evaluating the strain level
  * discriminating crystallographically similar phases
  * estimating the accuracy of the measurements.

Then we shall give the formal definition and current understanding of several
quality metrics that will be used later in this work.

Area of use
-----------

When a crystal is deformed, the dimensions of its lattice are distorted.
This non-uniformity in the lattice leads to a greater distribution of the
angles at which a crystallographic plane diffracts.
Effectively, the Kossel cones emanating from the sample have a greater
variation.
On the diffraction pattern, this is manifested by blurring the Kikuchi
bands edges  :cite:`Wilkinson1991`, :cite:`Keller2004`.
Furthermore, from the Bloch waves theory, this distortion inhibits the
propagation of the Bloch waves resulting in a decrease in contrast of the
Kikuchi bands.
Wilkinson and Dingley :cite:`Wilkinson1991` observed this phenomenon by
comparing the intensity profile of the Kikuchi band of specific planes from
undeformed and strained specimens.
Although they found that specimen contamination in the SEM was a significant
source of error, the Kikuchi band contrast and sharpness were found to decrease
as the amount of deformation increased.
A similar study was performed by Keller et al. :cite:`Keller2004` using the
:ref:`imagequality` value calculated by the TSL software.
The image quality is a measure of the Kikuchi bands' intensities which is linked
with the sharpness of the bands.
Their measurements of the strain fields around an AlGaAs layer inside a
GaAs matrix correlates with their finite element predictions.
These two examples show that quality metrics based on the Kikuchi band
profile or total intensity can evaluate the amount of deformation.

The analysis of the microstructure of commercial steels is a common application
for EBSD.
Although diffraction patterns of ferrite grains (body centered cubic crystal
structure) can easily be differentiated from retained austenite grains (face
centered cubic), EBSD is incapable of separating martensite or bainite regions
from the ferrite grains.
Martensite or bainite are obtained by rapid cooling from the austenite phase
leaving the carbon atoms (soluble in the austenite phase) trapped inside a body
centered cubic crystal structure.
This result is the formation of a metastable body centered tetragonal structure.
The *c/a* ratio of the tetragonal lattice is very close to unity 
:cite:`Ryde2006` and is therefore very close to a cubic lattice.
This slight variation of the *c/a* ratio is undetectable in normal EBSD
operating conditions.
The crystal structure of martensite, bainite and ferrite are indistinguishable
from one another.

However, the dislocation density of bainite or martensite is higher than that
of ferrite. :cite:`Petrov2007`
The higher dislocation density deteriorates the quality of the diffraction
pattern due to the higher lattice distortion.
On average, diffraction patterns of ferrite therefore have a higher quality
than those of bainite and martensite.
This principle was verified via nano and microhardness measurements by Wu et
al. :cite:`Wu2005`.
The separation of bainite from martensite is more complex since the difference
in their dislocation densities is smaller.
Ryde concludes that the discrimination between these two phases could only be
performed by "analyzing the directions of the carbide precipitates with a high
resolution microscope". :cite:`Ryde2006`

Szabo and Szalai :cite:`Szabo2005`, Wu et al. :cite:`Wu2005`, Ryde
:cite:`Ryde2006`, and Petrov et al. :cite:`Petrov2007` used a quality metric
related to the average intensity of the Kikuchi bands to successfully segment
the ferrite from the martensite in duplex or TRIP-assisted steels.
They used different methods to achieve the segmentation, but each of them
consist of assigning the pixels with a quality lower than a threshold value to
martensite and the others to ferrite.
The quality metric was either the image quality as calculated by the TSL OIM
software, the band contrast as calculated by the Oxford HKL Channel 5 software
or the band slope also calculated from Oxford's software.
The band contrast is said to be closely related to the image quality.
The band slope is related to the "slope of the intensity change between the
background of the pattern and the band":cite:`Ryde2006`.

As mentioned by Wu et al. :cite:`Wu2005`, the effect of grain boundaries on
phase segmentation must be taken into account.
As with martensite or bainite, diffraction patterns near or at grain boundaries
also have a lower quality.
Wu et al. proposed a normalizing method combined with a multi-peak model for
evaluating the area fraction of the different phases.
The accuracy of the technique is, however, influenced by the presence of residual
strain which would lower the quality of certain regions.
Petrov et al. did a similar study to Wu et al. on a TRIP-assisted steel.
They compared their EBSD results with optical micrographs and magnetic
measurements.
The area fractions of the retained austenite, bainite and ferrite calculated
for these techniques were in good agreement with each other.
They attributed the discrepancies to the variability in the size of the
observed areas (largest for the magnetic measurements and smallest for EBSD).
Finally, Ryde used the average image quality inside each grain to separate the
martensite from the ferrite grains.
This method was in good agreement with the microstructure observation.
For the same sample, he also found that the band slope gave a better
discrimination than the image quality.

The use of quality metrics for phase discrimination in steels was also studied
by Wright and Nowell :cite:`Wright2006` as well as the effect of quality metrics
on grain boundaries determination and strain measurements.
Aside from :ref:`imagequality`, they looked at the variation of the 
:ref:`average <pattern-average>`, :ref:`standard deviation <pattern-stddev>` 
and :ref:`entropy <pattern-entropy>` of the diffraction patterns as originally 
proposed by Tao :cite:`Tao2003`.
Their observations on an Al-Cu, extruded copper and steel samples showed
that the image quality is the best quality metric to differentiate grain
boundaries and strain while the pattern intensity average provides better phase
contrast.
The authors also noted an important point about quality metrics: their
dependence on the crystallographic orientation.
Two grains of the same phase and with the same strain level can have two
different values of a quality metric. 
This difference is due to the variation of the Kikuchi bands' intensity from
one set of crystallographic planes to another.
However, this difference is "generally much smaller than the one due to phase,
grain boundaries or strain" :cite:`Wright2006`.

.. bibliography::
