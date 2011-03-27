
.. _view-patterns:

View diffraction patterns
=========================

EBSD-Image offers a feature to link an :ref:`ebsdmmap` with a 
:ref:`SMP file <smp>` to easily visualize the acquired diffraction patterns.
By moving the mouse over any pixel in a map of the EbsdMMap, the corresponding
diffraction pattern is displayed.
For example, one can manually evaluate the quality of certain diffraction
patterns or perhaps check why a certain pixel was incorrectly indexed or 
unindexed.

How to
------

  #. Open an :ref:`ebsdmmap`. It can be a :ref:`expmmap`, :ref:`hklmmap` or 
     :ref:`tslmmap`.
  
  #. Select one map of the multimap from the :ref:`multimap-tree`. 
  
  #. From the menu *PlugIns*, select *EBSD* and then the option 
     *Link SMP to map*.
     The following dialog should appear.
     
     .. figure:: /images/view_patterns/dialog.png
        :width: 60%
        
        Link SMP to map dialog.
     ..
     
  #. In the source map combo box, all the opened maps are listed. 
     Select the one you want to link with the SMP file.
     In other words, when the mouse is moved over the pixels of this map, the
     corresponding diffraction patterns will appear.
     
  #. Browse to find the SMP file of this mapping. 
     Click OK.
     
  #. A new :ref:`map-window` will appear. 
     It will be used to show the diffraction patterns.
     You can resize and move this window.
     
  #. Move the mouse over the source map. 
     The corresponding diffraction pattern will appear in the other window.
     
     .. figure:: /images/view_patterns/screenshot.png
        :width: 60%
        
        Screenshot of the interface showing the corresponding diffraction 
        pattern of a pixel in the source map.
     ..
  
  #. If you want to save a diffraction pattern, hold CTRL and click on a pixel
     of the source map. 
     A new map will appear with the diffraction pattern of the pixel you just
     click. 
     You can save this diffraction pattern as any other map using File-Save.
  
  #. To unlink the SMP file and the source map, you can either close the
     source map or the map window showing the diffraction pattern.
  