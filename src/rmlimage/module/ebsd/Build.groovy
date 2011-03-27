package rmlimage.module.ebsd


class Build extends rmlimage.module.Build
{
 
  private String ebsdimageDir
  private String ebsdimageSrcDir
  private String ebsdimageLibDir 
  
  
  
  Build()
  {
    super('ebsd')
    
    ebsdimageDir = getProp('ebsd-image.dir')
    ebsdimageSrcDir = getProp('ebsd-image.src.dir')
    ebsdimageLibDir = getProp('ebsd-image.lib.dir')
  }


  public void build()
  {
    compile()
    
        //Backup the development module.prop file
    File modulePropFile = new File("$classDir/rmlimage/module.prop")
    modulePropFile.createNewFile()
    ant.move(file:"$classDir/rmlimage/module.prop", todir:tmpDir, 
             overwrite:true)
    
       //Copy the module.prop file to the rmlimage directory
    ant.copy(file:"$ebsdimageSrcDir/module.prop", todir:"$classDir/rmlimage")
      
       //Build the jar file
    ant.mkdir(dir:modulesDir)
    ant.jar(destfile:"$modulesDir/ebsd.jar") {
      fileset(dir:classDir) {
        include(name:'org/ebsdimage/**')
        exclude(name:'org/ebsdimage/core/old/**')
        include(name:'crystallography/**')
        include(name:'ptpshared/**')
        include(name:'rmlimage/module.prop')
      }
    }
  
      //Restore the development module.prop file
    ant.move(file:"$tmpDir/module.prop", todir:"$classDir/rmlimage")
    
        //Copy the external libraries
    cli()
    configuration()
    jdirectorychooser()
    opencsv()
    math()
    xml()
    wizard()
  }
  
  
  
  public void buildCore()
  {
    throw new IllegalArgumentException('Use build instead')
  }
  

  
  
  
  
  public void compile()
  {
    
        //Compile the EBSD module
    ant.javac(srcdir:ebsdimageSrcDir, destdir:classDir, source:"1.5",
              extdirs:ebsdimageLibDir, failonerror:true, debug:true) {
      exclude(name:'org/ebsdimage/core/old/**')
      exclude(name:'ptpshared/math/old/**')
    }

    //Copy the plugin button icon and menu definition files
    //and the support files
    ant.copy(todir:classDir) {
      fileset(dir:ebsdimageSrcDir) {
        include(name:'**/*.gif')
        include(name:'**/*.png')
        include(name:'**/*.menu')
        include(name:'**/*.csv')
      }
    }

        //Splices all the menu list files into a master one
    ant.concat(destfile:"$classDir/rmlimage/module.prop", append:true) {
      fileset(file:"$ebsdimageSrcDir/module.prop")
    }
  }
  

  
  public void compileCore()
  {
    throw new IllegalArgumentException('Use compile instead')
  }

}
