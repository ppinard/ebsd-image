
.. _run:

How to run an experiment
========================

This page provides a quick guide on how to run an :ref:`experiment <experiment>`
from the EBSD-Image desktop (graphical interface). 
Please refer to the :ref:`run-cui` page to run an experiment from a command 
line prompt.

.. contents::
   :local:

Import
------

The first step before running an experiment is to import the diffraction 
pattern images and/or the EBSD acquisition parameters and results from:

  * HKL Channel 5: Refer to the :ref:`import-hkl` guide.
  * TSL OIM: Refer to the :ref:`import-tsl` guide.
  * For other software, please send us a 
    `request <https://answers.launchpad.net/ebsd-image>`_ to create an importer 
    for this software.
  * Any diffraction patterns, refer to the :ref:`import-smp` guide.

.. toctree::
   :hidden:
   
   import/hkl.rst
   import/tsl.rst
   import/smp.rst

Wizard
------

All the parameters and operations of an :ref:`experiment <experiment>` are 
setup using a wizard. 
Once you have EBSD-Image open, click on button in the plug-ins toolbar to 
launch the wizard.

.. figure:: /images/run/wizard_button.png
   :width: 50%
   :align: center
   
   Experiment wizard button in RML-Image
..

The wizard has 11 steps. 
The first steps are to setup the parameters such as the working directory, 
phases and calibration. Other information about the EBSD acquisition such as 
the beam energy, magnification, working distance, etc. can also be specified. 
All these information are saved with the final results. 
The latter steps are to select the :ref:`operations <operations>`.

Start
^^^^^

The *Start* page is made to import data from previous experiments or from 
previous EBSD acquisition. 
It is therefore optional, but could be very useful to facilitate the creation
of a new experiment. 

.. figure:: /images/run/wizard_start.png
   :width: 50%
   :align: center
   
   Screenshot of the wizard
..

By importing the metadata from a previous mapping, the following metadata 
are loaded:
 
  * Beam energy
  * Magnification
  * Tilt angle
  * Working distance
  * Horizontal step size
  * Vertical step size
  * Sample rotation with respect to the microscope frame
  * Calibration
  * Width of the mapping
  * Height of the mapping

.. note ::
   
   Please note that it is not all these information that are directly used in 
   the experiment. 
   However, all of them are saved in the results for future use or simply as 
   reference. 

Previous mappings are derivatives from a :ref:`ebsdmmap` (i.e. they are zip 
files containing the results maps). 
They can be previous results from an experiment, mapping imported from 
HKL Channel 5 or TSL OIM. 

Operations from a previous experiment can also be imported. 
The imported operations will appears in the currently selected operations in 
the latter steps. 

Info
^^^^

This page is to define the name and the working directory of the experiment. 
The name is used to identify the experiment and the working directory is where 
all the files created during and after the experiment are automatically saved. 

.. warning::
   
   The experiment will automatically overwrite previous files in the working 
   directory.

Acquisition metadata
^^^^^^^^^^^^^^^^^^^^

The next step is to input the information about the acquisition. 
As mentioned previously, it is not all the metadata that are used in the 
experiment. 
The metadata used depends on the operations selected by the user. 
For example, the calibration is not required if no indexing operation is 
selected. 
It is however beneficial to input all the metadata to keep in reference all the 
information related to the EBSD acquisition. 
Here is a quick description of each metadatum:

Beam energy: 
  Acceleration voltage of the electron beam used during the EBSD acquisition.
Magnification: 
  Magnification of the EBSD acquisition.
Tilt angle: 
  Tilt of the sample (typically 70 deg).
Working distance: 
  Working distance used for the EBSD acquisition.
Horizontal step size: 
  Width of each pixel, step size in the X direction.
Vertical step size: 
  Height of each pixel, step size in the Y direction (typically equal to the 
  horizontal step size).
Sample rotation: 
  Rotation between the pattern frame (camera) and the sample frame. 
  It can be used to rotate the sample in a particular direction. 
  The sample rotation is defined using the three Euler angles expressed in the 
  Bunge convention.
Calibration: 
  Calibration of the camera. 
  The calibration is related to the position of the camera with respect to 
  the sample. 
  It consists of two parameters: the pattern center and the detector distance. 
  All values are expressed as a fraction of the camera's width. 
  The (0, 0) position of the pattern center is defined as the center of the 
  diffraction pattern. 
 
Phases
^^^^^^
 
.. note::
   
   Phases are only required if an indexing operation is selected in the 
   operations. 

To facilitate the selection of the phases, it is recommended to save all the 
user defined phases in a specific directory. 
This phases directory will contain several XML files defining the phases. 
The user only needs to define once a phase. 

Therefore, the first thing is to select using the ''Browse'' button the phases 
directory. 
Then using the new phase button the ''New Phase'' dialog will appear. 
A phase is defined by a name (the name is used as the filename of the XML file), 
a crystal system, a symmetry, unit cell parameters and atom positions. 
A few key points are:

  * The symmetry is defined as the Laue group.
  * For a given crystal system, the lattice parameters and angles are 
    automatically selected to match the definition of this crystal system. 
    For instance, only the lattice parameter ''a'' needs to be defined for a 
    cubic lattice.
  * The position of the atoms are relative to the crystal axes. 
    They should be between 0 and 1.
  * The symbol of the atom correspond to the element symbol (e.g. Al). 
    It is case-insentive.
  * To modify an atom site, it should be removed and re-entered.
 
The order of the phases in the ''Current phases'' list will corresponds to the 
index of the phases in the :ref:`phasesmap`.

Patterns
^^^^^^^^

This page is to select how the diffraction pattern images are loaded. 
The user has three options:

  * From a :ref:`smp`
  * From a folder containing diffraction pattern images
  * From a single diffraction pattern image
 
For the first two methods, one needs to specify the width and height of the 
mapping. 
This will correspond to the dimensions of the results :ref:`maps <maps>`.

.. note::

   The :ref:`smp` option is preferred since it provides a faster access to the 
   diffraction pattern images. 

Operations
^^^^^^^^^^

The next 5 steps are related to the 5 major steps of the experiment's 
operations (see :ref:`experiment`). 
For each category, a list of available operations are given to the user 
(list on the right). 
By clicking the ''Add'' button, the operation is created and added to the 
current selected operations (list on the left). 
Some operations requires that the user set-up for parameters while others don't. 
A dialog will appear in the former case. 
Please refer to the :ref:`operations` to the specific operation wiki pages for 
a description of each operation as well as a detailed explanation of the 
parameters.

.. figure:: /images/run/wizard_ops.png
   :width: 50%
   :align: center
   
   Screenshot of the operations' wizard page
..

Depending on the results that the user's wants, many categories of operations 
can be left empty without any operation selected. 
It is however important to realize that the experiment will only execute the 
operations that are required to obtain the selected results. 
For example, if a Hough transform operation is selected, but no results 
operation requires it to be performed, the Hough transform will not be executed. 
In other words, results operations are an essential part of an experiment.

Output
^^^^^^

Finally, the output page is to decided how the experiment will be run. 
The preview mode allows to check that all the operations are performed 
correctly. 
All the operations are executed on diffraction pattern and the resultant 
:ref:`map <maps>` of each operation is temporarily displayed in EBSD-Image. 
The diffraction pattern is selected by specifying the index of the desired 
diffraction pattern in the mapping. 
Please note that the results of operations that do not create a map are not 
displayed. 
After verifying the results, the user can either launch the experiment on all 
the diffraction pattern or return to the wizard to modify some operations. 
By going back to the wizard, the user can also select another diffraction 
pattern to preview.

.. warning::
   
   No image analysis routines can be performed in EBSD-Image in the preview 
   mode. 

Another output option is to save the experiment to an XML file. 
This can be used to launch the experiment in a command line prompt instead 
than in the graphical interface. 
It is also useful to archive the operations selected.

Finally, if the run option is selected, the wizard will close and the 
experiment will be executed in the RML-Image desktop. 

Execution
---------

When an experiment is being run, the progress bar in the bottom left corner 
indicates the progress of the experiment. 
An experiment can be stopped by clicking the *Stop* button |stop|. 
At the moment, no pause function is available.

.. |stop| image:: /images/run/stop_button.png
          :height: 15px

During the execution, the results maps can be viewed as they are getting filled 
with new results by selecting a map from the multimap tree (on the left). 
The maps are refreshed very second.

----------

.. [#f1] HKL Channel 5 is a trademark of Oxford Instruments plc.

.. [#f2] TSL OIM is a trademark of Ametek Inc.