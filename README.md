# Pttrt

**Pttrt** (/ˈpɛtrɪt/, or **P**ass **T**hem **T**o **R**un-**t**ime) is a [sbt](http://www.scala-sbt.org/) plugin,
designed to pass data from compile-time to run-time.

## Motive

Many times, my programs need some information, which is generated at compile-time and used at run-time.
For example, Subversion's revision, JARs's dependencies, configure files, and poor man's macro ;-), etc.

It is really heavyweight to deal with these situation in sbt.
You need to create a plugin for each type of files you generated,
to define many `SettingKey`s in each plugin's `.scala` files,
and to set values for each of these `SettingKey`s in your `.sbt` files.

I just want a lightweight way like `Makefile`:

    generated.properties:
    	echo my.base.dir=$(PWD) > $@

That's why I created `Pttrt`.

## Usage

### Step 1: Install `Pttrt` into your project

Add following lines to your `project/project/pluginsBuild.scala`

    import sbt._
    object ProjectBuild extends Build {
      lazy val pttrt = RootProject(uri("git://github.com/Atry/pttrt.git"))
      lazy val root = Project(id = "root", base = file(".")).dependsOn(pttrt)
    }

And add `pttrtSettings` to `project/plugins.sbt`:

    pttrtSettings

### Step 2: Export the data that will be passed to run-time

For example, if you want to know the building version on run-time,
you need to add following lines at `project/plugins.sbt`:

    pttrtSettings
    
    version := "1.2.3-SNAPSHOT"
    
    PttrtKeys.pttrtData <+= version map { v =>
      "org.yourHost.yourProject.YourSingleton" -> Map("Version" -> TypedExpression(v))
    }

### Step 3: Access the data on run-time

Create `PttrtExample.scala`:

    object PttrtExample {
      def main(args: Array[String]) {
        println("Building version is " + org.yourHost.yourProject.YourSingleton.Version)
      }
    }

### Step 4: Run it!

    $ sbt run-main PttrtExample

You will see:

    Building version is 1.2.3-SNAPSHOT