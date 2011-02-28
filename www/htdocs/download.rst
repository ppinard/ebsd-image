.. _download:

Download
========

**The current version of EBSD-Image is |version|.**
For older versions, please refer to the 
`download section <https://sourceforge.net/projects/ebsd-image/files/>`_ of our 
SourceForge project.

EBSD-Image is licensed under the 
`GNU General Public License v3 <http://www.gnu.org/licenses/gpl.html>`_ and 
RML-Image under its own freeware :ref:`license <rmlimage_license>`. 
For more information about the license and third-party libraries used in this 
project, please refer to the :ref:`license` page.

If you are experiencing problem running EBSD-Image, please report it in 
our `bug tracker <https://bugs.launchpad.net/ebsd-image>`_.

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

 * `Full version <https://sourceforge.net/projects/ebsd-image/files/v0.1.2/EBSD-Image_v0.1.2_ebsd-full.exe/download>`_
 * `Update <https://sourceforge.net/projects/ebsd-image/files/v0.1.2/EBSD-Image_v0.1.2_ebsd-update.exe/download>`_

Mac OS
------

**Prototype version**

Since EBSD-Image runs on Java 1.6, Mac OS X 10.5+ with all the latest updates 
is required.

  1. Download the tarball containing the 
     `application <https://sourceforge.net/projects/ebsd-image/files/v0.1.0/EBSD-Image_v0.1.2_Mac.tgz/download>`_
  2. Extract the files
  3. Run the application

Linux Debian
------------

Here is the procedure to add EBSD-Image to the Synaptic Package Manager. 
The update of the software will be automatically installed by the Update Manager. 

  1. Add the following lines to your distribution to your /etc/apt/sources.list
     (trailing slash is required)::
     
       deb http://www.ebsd-image.org/repos/debian stable/

  2. To install EBSD-Image::
  
       sudo apt-get update
       sudo apt-get install ebsd-image

.. note::

   For the time being, the package is not authenticated.

Another way to install the software is by downloading and executing the 
`deb <https://sourceforge.net/projects/ebsd-image/files/v0.1.2/ebsd-image0.1.2.deb>`_
package.

Cross-platform
--------------

Java 1.6 needs to be installed before running EBSD-Image. 

  1. Download the `tarball <https://sourceforge.net/projects/ebsd-image/files/v0.1.2/EBSD-Image_v0.1.2.tgz/download>`_      
  2. Extract the files
  3. Run the following command to start EBSD-Image from the extraction 
     directory::
     
       java -jar EBSD-Image.jar

Nightly Build
-------------

An unstable version of EBSD-Image is compiled every night from the source code. 
To download it, visit `nightly build <https://sourceforge.net/projects/ebsd-image/files/nightly/ebsd-image_nightly_build.tgz/download>`_.

Source code
-----------

Please refer to the `LaunchPad <https://code.launchpad.net/ebsd-image>`_ page 
to access our `Bazaar <http://bazaar.canonical.com>`_ repository.
You can also view to code directly from your browser.

