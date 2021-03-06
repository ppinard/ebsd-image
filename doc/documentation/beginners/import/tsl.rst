
.. _import-tsl:

Import TSL OIM projects
=======================

To import an EBSD acquisition from TSL OIM [#f1]_, EBSD-Image requires an 
ANG file. 
The result is an :ref:`tslmmap`.

Generate an ANG file
--------------------

.. todo::

   Describe how to generate ANG file

Wizard
------

In EBSD-Image, from the *Plugins* menu, select *EBSD* and *Import from TSL* or 
simply click on |button_tsl| in the :ref:`plugins-toolbar`.

.. |button_tsl| image:: /images/plugins_toolbar/importtsl.png

Start
^^^^^

Select the ANG file to import.

.. warning::
   
   After clicking *Next*, it may take a few seconds before the next page of the 
   wizard is displayed. 
   During that time, the header containing the information about the EBSD 
   acquisition are loaded.

Microscope configuration
^^^^^^^^^^^^^^^^^^^^^^^^

Some metadata related to the EBSD acquisition can not be loaded directly from 
the ANG file. 
These information are optional. 

 * Beam energy: 
   Acceleration voltage of the electron beam used during the EBSD acquisition.
 * Magnification: 
   Magnification of the EBSD acquisition.
 * Tilt angle: 
   Tilt of the sample (typically 70 deg).
 * Sample rotation: 
   Rotation of the sample with respect to the sample frame. 
   It can be used to rotate the sample in a particular direction (e.g. align
   the rolling direction with the y-axis). 
   The sample rotation is defined using the three Euler angles expressed in the 
   Bunge convention.

Phases
^^^^^^

The phases information (unit cell parameters, atom positions and symmetry) 
cannot be loaded from the ANG since the atom positions are missing. 
Therefore, the user must create the phases used in the ANG manually. 
The number of phases and their respective name in the ANG is given in the 
warning section of the wizard page. 
The list of current phases must match the number of phases in the ANG as well 
as their order.

For more information on how to create a new phase, please refer to the 
:ref:`phase` guide.  


Diffraction patterns
^^^^^^^^^^^^^^^^^^^^

This wizard page is to automatically convert the diffraction patterns saved 
during a TSL acquisition to a :ref:`smp` file. 
If this step is skipped, only the information in the ANG file will be converted 
to an :ref:`tslmmap`. 
If the diffraction patterns images have not being moved since their acquisition 
by TSL OIM, the wizard can automatically find where the images are located, 
however it is often easier to manually select the folder containing the images.
This folder typically has the same name as the project's filename.
Be careful not to enter the folder when you are selecting it since the listing 
of the images files by the operating system is a lengthy process.

Output
^^^^^^

Finally, the location where to save the :ref:`tslmmap` and :ref:`smp` file must 
be specified. 
Please note that only the location and name of the :ref:`tslmmap` is required 
since the :ref:`smp` file will be saved in the same folder with the same name; 
only the extension is changed to SMP.

-----------

.. [#f1] TSL OIM is a trademark of Ametek Inc.