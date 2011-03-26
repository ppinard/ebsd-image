
.. _distributed-interface:

Distributed interface
=====================

Overview
--------

The EBSD analysis requires computer-intensive calculations, such as the
:ref:`houghtransform`, to be performed on tens of thousands of images.
With the current quest for the fastest acquisition camera, the number of 
diffraction patterns to analyze for a given mapping will keep on increasing, 
thus requiring more and more computing power.
If one wants to benefit from more elaborate, but perhaps slower algorithms to
analyze these diffraction patterns, processing large quantities of data can
easily become a problem, or at the very least a limitation.
Distributed computing on a grid or cluster of computers can be a solution to
this problem.
A distributed system consists of several processors connected together via an
internal network.
A program can either use several processors simultaneously (parallel
computing) or be run on several processors independently (distributed
computing).
The latter is particularly well fitted for EBSD analysis since each diffraction
pattern is completely independent from the others.
The processing of a diffraction pattern does not require knowledge of
the other diffraction patterns in the mapping.
The same set of algorithms can be independently applied on each individual
diffraction pattern and the results combined only at the end.
The main advantage to this kind of system is that the time required to analyze
a series of diffraction patterns can be reduced by a factor approximately equal
to the number of processors used.
An analysis that would take two hours on a single processor computer can be
reduced to ten minutes if a distributed system of twelve processors is used.

To facilitate the use of such a system, special routines were developed in
EBSD-Image.
The distributed interface of EBSD-Image requires the same inputs as the
graphical interface, a :ref:`SMP <smp>` file and a series of operations.
As such, we encourage the user to use the graphical interface to create the SMP
file from the acquired stored EBSPs and to set-up the 
:ref:`experiment <experiment>` file.
The output of the distributed interface is also the same as the graphical
interface, an :ref:`ebsdmmap` saved in a ZIP which can later be opened inside 
the graphical interface.

To accommodate different types of distributed systems, the distributed interface
consists of three main programs.
They perform the following tasks:

  * split the diffraction patterns and the experiment set-up file for each
    processor
  * run the analysis on each processor
  * merge the results into a single output.

The first task is necessary to give each processor a unique set of diffraction
patterns.
Technically, the single :ref:`SMP <smp>` file is split into smaller SMP files.
If all the processors were loading the diffraction patterns from the same SMP
file, this could create concurrency problems and slow down the analysis.
The experiment set-up file is automatically adjusted to tell each processor
which SMP file to use and which diffraction patterns to analyze.
The result of this first task is a series of completely independent
experiments.
Each experiment is then run in the analysis engine on different processors.
An :ref:`ebsdmmap` is created for each experiment.
Once the analysis of all the experiments is completed, the results are merged
by combining the pixels from the different EbsdMMap's.
The following diagram summarizes the operation of the distributed interface:

.. figure:: /images/distributed_interface/diagram.png
   :width: 65%
   
   Operation diagram of the distributed interface.
..

This implementation was successfully tested on the computing cluster of the
Hydro-Québec Research Institute which consists of 1000 processors
(AMD Opteron 2218 processor, 8 Gb of RAM memory per processor, Linux Centos 
4.4 operating system, Sun Grid Engine nodes management system).
The results presented in the following chapter on the applications of EBSD-Image
were all calculated using this cluster.

How to?
-------

  #. Follow the instructions to setup the Java class path (1 to 3, inclusive) 
     of :ref:`run-cui`.
  
  #. Create an experiment using the wizard inside the graphical interface
  
  #. In the last page of the wizard, the output page, select *Save to XML* 
     instead of running the experiment.
     
  #. Copy this XML file and the :ref:`SMP <smp>` file containing the diffraction
     patterns to a folder on the main node of your computing grid.
     
  #. The next step is to split the experiment and SMP file into smaller 
     experiments and SMP files. Each "split" experiment will be run on one
     processor. In the folder where you copied the XML and SMP file, run the 
     following command::
  
        java org.ebsdimage.cui.ExpSplit -d run1 -s 50 exp.xml
     
     This command will split the experiment's XML file (:file:`exp.xml`) into
     50 smaller experiments. The files will be placed in the :file:`run1` 
     folder inside the current directory.
     
  #. If you browse the :file:`run1` directory, you will find 50 folders each 
     containing one XML and one SMP file. Using the following command, you can
     start the execution of the first "split" experiment (e.g. 
     :file:`exp_1.xml`)::
     
       java org.ebsdimage.cui.ExpRun exp_1.xml
       
     Depending on your distributed computing system, this command will probably
     be part of a large script to assign it to a processor on the grid. 
     Also, this command should be part of a batch file to start the execution
     of all "split" experiment in the folder :file:`run1`.
     
  #. When the execution of all the "split" experiments is completed, run the
     following command to merge the :ref:`ebsdmmap`'s::
     
       java org.ebsdimage.cui.ExpMerge -o exp.zip run1
     
     assuming you run this command in the directory when the original XML and
     SMP file are located.
     This command will merge all the ZIP files inside the folder :file:`run1`
     and saves the result in :file:`exp.zip`.
     
   #. The final EbsdMMap can be open inside EBSD-Image where the results can be
      analyzed.
      