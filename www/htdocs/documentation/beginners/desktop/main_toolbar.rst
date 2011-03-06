
.. _main-toolbar:

Main toolbar
============

The main tool bar holds icons for the most often used imaging functions in 
EBSD-Image.

.. image:: /images/main_toolbar/toolbar.png
   :align: center
   
---------

============= ================= =================================================
Icon          Operation         Description
============= ================= =================================================
|open|        Open              Shows a File dialog that allows for the user to 
                                select files to load on the desktop. 
                                Same as File-Open.
|reload|      Reload            Reload the selected :ref:`map <maps>` from the 
                                disk. Same as File-Reload. 
|save|        Save              Saves the selected :ref:`map <maps>` to disk 
                                with its default name. Same as File-Save. 
|saveas|      Save As           Saves the selected :ref:`map <maps>` to disk 
                                with a user specified name. Same as File-Save As.
|close|       Close             Closes the selected :ref:`map <maps>`. 
                                Same as File-Close.
|closeall|    Close All         Closes all the :ref:`maps <maps>`. 
                                Save as File-Close All.
|undo|        Undo              Undoes the last operation. Same as Edit-Undo. 
|histogram|   Histogram         Shows or hides the histogram of the selected 
                                :ref:`map <maps>`. Same as View-Histogram. 
|threshold|   Thresholding      Shows the panel for manual thresholding of the 
                                selected :ref:`map <maps>`. 
                                For a :ref:`PhaseMap`, thresholding is used to 
                                select one phase. Same as Threshold-Manual. 
|erosion|     Erosion           Turns OFF any ON pixel that has some OFF 
                                neighbors. Same as MathMorph-Erosion. 
|dilation|    Dilation          Turns ON any OFF pixel that has some ON 
                                neighbors. Same as MathMorph-Dilation. 
|opening|     Opening           Does an erosion followed by a dilation. 
                                Same as MathMorph-Opening. 
|closing|     Closing           Does a dilation followed by an erosion. 
                                Same as MathMorph-Closing. 
|invert|      Invert            Inverts the value of every pixel. 
                                Same as MathMorph-Invert.
|mapmath|     Map Math          Does different types of mathematical operations 
                                on :ref:`maps <maps>`. Same as MapMath-Math. 
|fillholes|   Fill Holes        Fills the holes in objects. 
                                Same as MathMorph-Fill Holes. 
|grid|        Grid              Creates a grid. Same as Utilities-Grid. 
|qcoverlay|   QC Overlay        Will overlay the selected :ref:`binmap` directly 
                                on the specified :ref:`bytemap`. 
                                Same as Utilities-QC Overlay. 
|qcoutline|   QC Outline        Will overlay the selected :ref:`binmap` outline 
                                directly on the specified :ref:`ByteMap`. 
                                Same as Utilities-QC Outline. 
|identify|    Identify          Identifies groups of touching pixels as part of 
                                the same object. Same as Analysis-Identify. 
|measure|     Measure           Measure the size, aspect ratio, centroid of the 
                                object in an :ref:`identmap`. 
                                Save as Analysis-Measure. 
|saveresults| Save Results      Saves the results calculated to file. 
                                Same as Analysis-Save Measurements.
|scaletofit|  Scale to Fit      Adjusts the display scaling factor for the 
                                :ref:`map <maps>` to entirely fit in the window. 
                                Same as View-Scale to Fit.
|nozoom|      No Zoom           Adjusts the display scaling factor for the 
                                :ref:`map <maps>` to 100%. Same as View-No Zoom.
============= ================= =================================================

.. |close| image:: /images/main_toolbar/close.png

.. |closeall| image:: /images/main_toolbar/closeall.png

.. |closing| image:: /images/main_toolbar/closing.png

.. |dilation| image:: /images/main_toolbar/dilation.png

.. |erosion| image:: /images/main_toolbar/erosion.png

.. |fillholes| image:: /images/main_toolbar/fillholes.png

.. |grid| image:: /images/main_toolbar/grid.png

.. |histogram| image:: /images/main_toolbar/histogram.png

.. |identify| image:: /images/main_toolbar/identify.png

.. |invert| image:: /images/main_toolbar/invert.png

.. |mapmath| image:: /images/main_toolbar/mapmath.png

.. |measure| image:: /images/main_toolbar/measure.png

.. |nozoom| image:: /images/main_toolbar/nozoom.png

.. |open| image:: /images/main_toolbar/open.png

.. |opening| image:: /images/main_toolbar/opening.png

.. |qcoutline| image:: /images/main_toolbar/qcoutline.png

.. |qcoverlay| image:: /images/main_toolbar/qcoverlay.png

.. |reload| image:: /images/main_toolbar/reload.png

.. |save| image:: /images/main_toolbar/save.png

.. |saveas| image:: /images/main_toolbar/saveas.png

.. |saveresults| image:: /images/main_toolbar/saveresults.png

.. |scaletofit| image:: /images/main_toolbar/scaletofit.png

.. |threshold| image:: /images/main_toolbar/threshold.png

.. |undo| image:: /images/main_toolbar/undo.png

