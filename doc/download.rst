.. _download:

Download
========

**The current version of EBSD-Image is:** |version|.
For older versions, please refer to the :sf-link:`download section <files>` 
of our SourceForge project.

EBSD-Image is licensed under the 
`GNU General Public License v3 <http://www.gnu.org/licenses/gpl.html>`_ and 
RML-Image under its own freeware license. 
For more information about the license and third-party libraries used in this 
project, please refer to the :ref:`license` page.

If you are experiencing problem running EBSD-Image, please report it in 
our :lp-link:`bug tracker <bugs>`.

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

There are always two installers available for each version. 
The first one (*Full*) holds all the files needed to run EBSD-Image along 
with the proper Java Virtual Machine. 
This makes it a much bigger download. 
You should use this installer the first time you install EBSD-Image. 
The second installer (*Update*) is much smaller as it only holds the files 
needed to run EBSD-Image. 
You should use this installer over the first one if you already have installed 
EBSD-Image. 
Also, there is no need to install intermediate updates if you missed them. 
This means that if the version of EBSD-Image on your disk is 0.1.0, you can 
install the update of 0.1.5 even if you did not install the updates for 
0.1.1, 0.1.2, 0.1.3 and 0.1.4. 

 * :sf-file:`Full version <EBSD-Image_v%(version)s_full.exe>`
 * :sf-file:`Update <EBSD-Image_v%(version)s_update.exe>`

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

  #. Download the :sf-file:`tarball <EBSD-Image_v%(version)s.tgz>`
  #. Extract the files
  #. Under Linux or Mac, make the :file:`EBSD-Image.sh` executable::
  
       chmod +x EBSD-Image.sh
       
     and execute the shell script::
  
       ./EBSD-Image.sh
  
  #. Under Windows, execute the batch script::
  
       EBSD-Image.bat
     
Source code
-----------

Please refer to the :lp-link:`LaunchPad <home>` page to access our 
`Bazaar <http://bazaar.canonical.com>`_ repository.
You can also view to code directly from your browser.

.. toctree::
   :hidden:
   :glob:
   
   download/*
   
