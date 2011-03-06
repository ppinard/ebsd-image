
.. selector:

Hough peak selector
===================

The *Hough peaks selector* operation to select the number of peaks to used in 
the indexing operation. 
Depending on the quality of the diffraction pattern, many peaks can be detected 
and identified. However only the most intense peaks are required to perform the 
indexing. 
This operation is therefore to select how many peaks to use. 

Parameters
----------

The user must select the minimum and maximum number of peaks to use for the 
indexing. 
The minimum number of peaks is a way to eliminate diffraction patterns that do 
not have enough peaks to perform the indexing. 
For those patterns, the indexing operation will be skipped and there will be 
assigned an non-indexed value. 

The maximum number of peaks is to specify how many peaks the indexing operation 
should used. 
Please note that if the number of peaks for a pattern is greater than the 
minimum and less than the maximum number of peaks, the indexing will be 
performed the number of available peaks.

Another important note is that the indexing operation requires at least 
3 peaks. 
Therefore, the minimum and maximum number of peaks specified by the user must 
be equal or greater than 3.
