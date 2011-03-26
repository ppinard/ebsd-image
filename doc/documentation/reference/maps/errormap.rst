
.. _errormap:

ErrorMap
========

The *ErrorMap* contains information about errors that may have occured during
the processing of diffraction patterns. 
In some :ref:`operations <operations>`, unexpected behavior may prevent the
normal execution of the operations. 
These errors are pattern dependent; they only occur for specific diffraction
patterns, but not for others.
For example, not enough :ref:`houghpeak` are detected for a diffraction pattern
and an operation can not be performed as a result.
The *ErrorMap* is designed to report to the users these anomalies.

An *ErrorMap* is a 8-bit map (:ref:`bytemap`) with a list of error codes
saved in a XML file.
The *ErrorMap* can contain up to 255 error codes.
Pixels with no error have a value of 0.

