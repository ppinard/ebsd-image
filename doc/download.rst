.. _download:

Download
========

**The current version of EBSD-Image is:** |version|.

EBSD-Image is licensed under the 
`GNU General Public License v3 <http://www.gnu.org/licenses/gpl.html>`_ and 
RML-Image under its own freeware license. 
For more information about the license and third-party libraries used in this 
project, please refer to the :ref:`license` page.

.. contents::

Windows
-------

EBSD-Image comes with his own installer on Windows operating system. 
There is *no* need to have Java installed. 
The Windows version of EBSD-Image also comes with its own updater (feature 
only available under Windows operating system). 
The updater is automatically launched every time EBSD-Image is executed. 
If a new version is available, the user is warned and has the option to 
download and install it.

.. warning::

   Exception for EBSD-Image and its update may have to be set in your Antivirus 
   software to allow the software to properly check for new updates.

`ebsd-image_0.2.6.exe <https://github.com/ppinard/ebsd-image/releases/download/v0.2.6/ebsd-image_0.2.6.exe>`_

Mac OS
------

Under testing. 
Please refer to cross-platform download for the time being.

Linux Debian
------------

Under testing. 
Please refer to cross-platform download for the time being.

Cross-platform
--------------

Java 1.6 needs to be installed before running EBSD-Image. 

  #. Download the `ebsd-image_0.2.6.tgz <https://github.com/ppinard/ebsd-image/releases/download/v0.2.6/ebsd-image_0.2.6.tgz>`_
  #. Extract the files
  #. Under Linux or Mac, make the :file:`EBSD-Image.sh` executable::
  
       chmod +x EBSD-Image.sh
       
     and execute the shell script::
  
       ./EBSD-Image.sh
  
  #. Under Windows, execute the batch script::
  
       EBSD-Image.bat
     
Source code
-----------

See `https://github.com/ppinard/ebsd-image <https://github.com/ppinard/ebsd-image>`_

.. toctree::
   :hidden:
   :glob:
   
   download/*
   
