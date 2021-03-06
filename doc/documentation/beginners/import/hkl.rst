
.. _import-hkl:

Import HKL Channel 5 projects
=============================

To import an EBSD acquisition from HKL Channel 5 [#f1]_, EBSD-Image requires the 
project CPR and CTF file. 
These two files are transformed into EBSD-Image's :ref:`hklmmap`.
From the importer wizard, the user can also convert the diffraction pattern 
images to a :ref:`smp`. 
For a typical map of around 50,000 pixels, the conversion takes about 10 
minutes.

Generate a CTF file
-------------------

  1. Open the HKL Channel 5 Manager |hklmanager|
  2. Open an EBSD acquisition project (CPR file)
  3. From the *Project* menu, select *Export* and *as Channel Text File 
     (*.CTF)...*.

.. image:: /images/hkl/ctf_manager.png
   :align: center

.. |hklmanager| image:: /images/hkl/hklmanager.png

Wizard
------

In EBSD-Image, from the *Plugins* menu, select *EBSD* and *Import from HKL* or 
simply click on |button_hkl| in the :ref:`plugins-toolbar`.

.. |button_hkl| image:: /images/plugins_toolbar/importhkl.png

Start
^^^^^

Select the CPR file to import.
The CTF file must have the same filename as the CPR file and be located in the
same directory.

.. warning::

   After clicking *Next*, it may take a few seconds before the next page of the 
   wizard is displayed. 
   During that time, the header containing the information about the EBSD 
   acquisition are loaded.

Microscope configuration
^^^^^^^^^^^^^^^^^^^^^^^^

This page is to add and modify the microscope configuration.
All the information except the working distance are directly loaded from the
CPR/CTF files.
The working distance refers to microscope's working distance used for the
EBSD acquisition.
This information is optional.

Phases
^^^^^^

The phases information (unit cell parameters, atom positions and symmetry) 
cannot be loaded from the CPR or CTF since the atom positions are missing. 
Therefore, the user must manually create the phases. 
The number of phases and their respective name in the CTF is given in the 
warning section of the wizard page. 
The list of current phases must match the number of phases in the CTF as well 
as their order.

For more information on how to create a new phase, please refer to the 
:ref:`phase` guide. 

Diffraction patterns
^^^^^^^^^^^^^^^^^^^^

This wizard page is to automatically convert the diffraction patterns saved 
during an HKL acquisition to a :ref:`smp` file. 
If this step is skipped, only the information in the CTF file will be converted 
to an :ref:`hklmmap`. 
If the diffraction patterns images have not being moved since their acquisition 
by HKL Flamenco, the wizard can automatically find where the images are located, 
however it is often easier to manually select the folder containing the images 
(typically *ProjectXXXImages*). 
Be careful not to enter the folder when you are selecting it since the listing 
of the images files by the operating system is a lengthy process.

Output
^^^^^^

Finally, the location where to save the :ref:`hklmmap` and :ref:`smp` file must 
be specified. 
Please note that only the location and name of the :ref:`hklmmap` is required 
since the :ref:`smp` file will be saved in the same folder with the same name; 
only the extension is changed to SMP.

-----------

.. [#f1] HKL Channel 5 is a trademark of Oxford Instruments plc.