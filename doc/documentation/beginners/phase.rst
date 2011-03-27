
.. _phase:

Create a new phase
==================

In EBSD-Image, a phase is defined by:

  * unit cell's parameters (length of axes and angles between them)
  * positions and type of atoms inside the unit cell
  * space group
  
A phase can be created directly inside the experiment's :ref:`wizard <run>` or
from the menu *PlugIns - EBSD - Create a new phase*.
Phases are saved as XML files.
It is recommended to save all your phases in the same directory so you can
easily import them.

How to
------

In this tutorial, we will create the phase for :math:`\alpha`-ferrite.
When creating a new phase, the following dialog appears:

.. figure:: /images/phase/dialog.png
   :width: 45%
   
   New phase dialog.
.. 

  #. Enter the name of the phase (e.g. ``ferrite``)
  
  #. Select the crystal system (triclinic, monoclinic, orthorhombic, etc.).
     In our example, we select ``cubic``.
     
  #. Based on your crystal system selection, the Laue group combo box is 
     updated to only give the Laue groups for a cubic crystal system.
     Ferrite has a ``m3m`` Laue group.
     
  #. The space group combo box is updated based on the Laue group selection.
     This simplifies the selection instead of browsing through the 230 possible
     space groups.
     The space group for ferrite is 229 or ``Im-3m``.
     
  #. The next step is to define the unit cell.
     For a given crystal system, only the required parameters must be entered.
     For instance, only the length of *a* axis is required for a cubic 
     crystal system.
     The length of the *b* and *c* axes are automatically set to be equal to the
     length of *a*.
  
  #. Finally, you must specify the atoms present in the unit cell. 
     First, enter the symbol of an atom (case insensitive) and then the 
     position of that atom in the unit cell.
     The position are relative to the *a*, *b* and *c* axes. 
     For example, an atom located at the extremities of the unit cell will have
     a position :math:`(1,1,1)`.
     
     Only the general positions must be defined as the symmetrical equivalent
     positions are automatically calculated based on the selected space group.
     To see the final list of atom sites (general and symmetrical equivalent
     positions), press on the |calc| button.
     
  #. After pressing OK, the phase will be saved in the phase directory you 
     selected in the wizard or a file dialog will appear to select where the
     phase should be saved.
     
.. |calc| image:: /images/phase/calc.png

Import from CIF
---------------

From the experiment :ref:`wizard <run>`'s phase page, one option is to create
a new phase by importing the data from a Crystallographic Information File 
(CIF) [#f1]_.
The status of this feature is at an experimental level.
It may not support all CIF files.
If you cannot open a CIF file, report it in the :lp-link:`bug tracker <bugs>`.
Please attach the CIF file to the bug.

In the bottom right corner, click on the *Import a phase from a CIF file* 
button |cif|.
A file dialog will appear to select the CIF file.
If the file is correctly loaded, the *New phase dialog* will appear with the
loaded data.
Check that the information to make sure they are correct.

CIF files are the standard file format to exchange crystallographic information.
CIF of several phases can be found in commercial or free database.
Here are some free crystallographic available online:

  * `Crystallography Open Database <http://www.crystallography.net/>`_
  * `American Mineralogist Crystal Structure Database <http://rruff.geo.arizona.edu/AMS/amcsd.php>`_

.. |cif| image:: /images/phase/cif.png

-----------

.. [#f1] Data exchange standard file format for crystallographic information
         maintained by the `International Union of Crystallography <http://www.iucr.org/resources/cif>`_. 