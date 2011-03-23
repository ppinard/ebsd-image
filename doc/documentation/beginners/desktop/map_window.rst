
.. _map-window:

Map window
==========

Map windows can contain the following objects:

.. contents::
   :local:

Map itself
----------

If the Map is bigger than the window itself, scroll bars will appear. 
The map can be scrolled using these bars. It can also be scrolled by using 
the scroll wheel on your mouse (if it has one). 
Vertical scrolling is done by the scroll wheel on your mouse and horizontal 
scrolling is done by pressing and holding the *CTRL* key while using the scroll 
wheel on your mouse.

Look-up table (LUT)
-------------------

The LUT shows the range of color of the pixel values. 
For :ref:`bytemap` and :ref:`realmap`, it can be modified by the options in
the *LUT* menu.

Map's toolbar
-------------

Here is a list of common toolbar buttons. 
The available buttons will change depending on the type of :ref:`maps <maps>`.

============= ================= =================================================
Icon          Operation         Description
============= ================= =================================================
|zoom|        Zooms in or out   To zoom in, left click on the map. 
                                To zoom out, right click on the map.
|roi|         Region of         Selects a Region of Interest(ROI). 
              Interest (ROI)    Just click and drag to select the ROI. 
                                A red rectangle will appear (known bug). 
                                The ROI is used in only two operations: 
                                Crop and Histogram. 
                                All other operations are always done on the 
                                whole map even if an ROI is defined. 
                                To remove the ROI, simply click at one point 
                                and do not drag.
|crop|        Cropping          Crops the map to the desired dimension. 
                                If no ROI is selected, a dialog will appear 
                                asking to enter the coordinate of the crop 
                                window.
|paint|       Paint             Inverts the pixel at the clicked location and 
                                all the pixels of the same state that touches it. 
                                Great way to remove objects in :ref:`binmap`.
|erase|       Erase             Erases a selected region. 
                                To erase, simply click and drag over the region 
                                to erase. 
                                Every pixel in the selection will be set to 0.
|line|        Line              Draws a line of color 0. 
                                The thickness of the line can be selected by 
                                right clicking on the button. 
                                When you select a new size, the button will 
                                become unpressed even if it was pressed before.
============= ================= =================================================

Cursor info bar
---------------

Typical information showed:

  * Coordinates of the mouse cursor on the map:
    These are map's coordinates, not display coordinates. 
    So it takes into account the scaling factor. 
    The origin (0; 0) is at the top left corner of the map. 
  * Scale factor of the display:
    When the scaling factor changes, the map stays the same in memory. 
    Only its display changes.
  * Color index of the pixel under the cursor or its value

.. |crop| image:: /images/map_window/crop.png

.. |erase| image:: /images/map_window/erase.png

.. |line| image:: /images/map_window/line.png

.. |paint| image:: /images/map_window/paint.png

.. |roi| image:: /images/map_window/roi.png

.. |zoom| image:: /images/map_window/zoom.png
