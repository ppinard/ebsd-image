
.. _plugins-toolbar:

Plugins toolbar
===============

This toolbar holds functions related to EBSD-Image and other modules.

============= ================= =================================================
Icon          Operation         Description
============= ================= =================================================
|contrastexp| Contrast          Stretch the histogram of a :ref:`bytemap` 
              Expansion         
|hough|       Hough Transform   Perform the Hough transform on the selected
                                :ref:`bytemap`. A dialog asks the user for the
                                :math:`\Delta\theta` resolution.
|threshold|   Hough Auto        Threshold the peaks from the selected 
              Thresholding      :ref:`houghmap`
|qcoverlay|   QC Overlay Lines  From thresholded peaks (:ref:`binmap`), overlay 
                                the position of these peaks on the original 
                                diffraction pattern (:ref:`bytemap`)
|maskdisc|    Mask Disc         Create a circular mask (:ref:`binmap`) to select 
                                a region of interest from a diffraction pattern
|experiment|  Run an Experiment Launch the wizard to setup and run an experiment
|simulation|  Run a Simulation  Launch the wizard to setup and simulate 
                                diffraction patterns
|eulers|      Euler Map         Create a :ref:`rgbmap` where the colors are
                                defined by the Euler angles of a :ref:`ebsdmmap`
|importhkl|   Import from HKL   Import a mapping and diffraction patterns from 
                                HKL Channel 5
|exporthkl|   Export to HKL     Export a :ref:`hklmmap` back to HKL Channel 5
                                file formats
|importtsl|   Import from TSL   Import a mapping and diffraction patterns from 
                                TSL OIM
|stitch|      Stitch EBSPs      Utility to stitch EBSD mappings into one mapping
|micronbar|   Micron Bar        Add a micron bar to the selected 
                                :ref:`map <maps>`
============= ================= =================================================

.. |contrastexp| image:: /images/plugins_toolbar/contrastexp.png

.. |eulers| image:: /images/plugins_toolbar/eulers.png

.. |experiment| image:: /images/plugins_toolbar/experiment.png

.. |exporthkl| image:: /images/plugins_toolbar/exporthkl.png

.. |hough| image:: /images/plugins_toolbar/hough.png

.. |importhkl| image:: /images/plugins_toolbar/importhkl.png

.. |importtsl| image:: /images/plugins_toolbar/importtsl.png

.. |maskdisc| image:: /images/plugins_toolbar/maskdisc.png

.. |micronbar| image:: /images/plugins_toolbar/micronbar.png

.. |qcoverlay| image:: /images/plugins_toolbar/qcoverlay.png

.. |simulation| image:: /images/plugins_toolbar/simulation.png

.. |stitch| image:: /images/plugins_toolbar/stitch.png

.. |threshold| image:: /images/plugins_toolbar/threshold.png