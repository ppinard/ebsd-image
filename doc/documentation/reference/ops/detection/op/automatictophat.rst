
.. _automatictophat:

Automatic top hat
=================

The *Automatic Top Hat* operation is a thresholding algorithm to detect 
Hough peaks from a :ref:`houghmap`. 
The algorithm is based on the top hat algorihm of Gonzalez and Woods 
:cite:`Gonzalez2008`. 
It consists of performing an opening of the original :ref:`houghmap` and then 
subtracting the resultant map with the original map. 
The opening allows to remove any features and "leaves only an approximation of 
the background" :cite:`Gonzalez2008`.

.. figure:: /images/ops/detection/op/automatictophat/tophat_before.png
   :align: center

   Original Hough map
..

.. figure:: /images/ops/detection/op/automatictophat/tophat_opening.png
   :align: center

   After opening
..

.. figure:: /images/ops/detection/op/automatictophat/tophat_subtraction.png
   :align: center

   Subtraction of the original Hough map and the map resulting from the opening
..

After a threshold opening is performed to detect the peaks. 
It was found that an average between the threshold value obtained from the 
minimum error :cite:`Kittler1986` and Kapur :cite:`Kapur1985` thresholding 
gives an acceptable thresholding level to detect Hough peaks.

.. figure:: /images/ops/detection/op/automatictophat/tophat_threshold.png
   :align: center

   Detected Hough peaks
..

