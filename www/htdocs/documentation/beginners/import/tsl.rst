
.. _import-tsl:

Import TSL OIM projects
=======================

To import an EBSD acquisition from TSL OIM, EBSD-Image requires an ANG file. 
The result is an :ref:`tslmmap`.

Generate an ang file
--------------------

*Under construction*

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

Missing data
^^^^^^^^^^^^

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
   Rotation between the pattern frame (camera) and the sample frame. 
   It can be used to rotate the sample in a particular direction. 
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

Output
^^^^^^

Finally, the location where to save the :ref:`tslmmap` must be specified. 
