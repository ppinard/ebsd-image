
.. _run-cui:

How to run an experiment from the command line
==============================================

An :ref:`experiment <experiment>` designed inside the graphical interface of 
EBSD-Image can easily be run in a command prompt. 

  #. Locate the installation directory of EBSD-Image. 
     It is the directory containing *EBSD-Image.jar*. 
     Typically,
     
     * ``C:\Program Files\EBSD-Image\`` in Windows OS
     * ``/usr/share/ebsd-image/`` in Linux distros
     * Inside the ``.app`` folder in Mac OS
        
  #. Add this directory containing to the Java 
     `class path <http://java.sun.com/j2se/1.3/docs/tooldocs/win32/classpath.html>`_

  #. Add the sub-directory *module* and *ext* to the Java class path as 
     followed by replacing INSTALLATION_DIRECTORY with the path of the 
     installation directory
     
     * INSTALLATION_DIRECTORY/module/*
     * INSTALLATION_DIRECTORY/ext/*
       
  #. Create an experiment using the wizard inside the graphical interface
  
  #. In the last page of the wizard, the output page, select *Save to XML* 
     instead of running the experiment.
  #. Open a command prompt and run the following command by replacing XMLFILE 
     with the path leading to the XML file created by the wizard::
     
       java org.ebsdimage.cui.ExpRun XMLFILE

For more option on the command line runner, type::

  java org.ebsd.image.cui.ExpRun --help
