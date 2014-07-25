# Pttrt

**Pttrt** (/ˈpɛtrɪt/, or **P**<wbr/>ass **T**<wbr/>hem **T**<wbr/>o **R**<wbr/>un-**t**<wbr/>ime) is a [sbt](http://www.scala-sbt.org/) plugin,
designed to pass data from compile-time to run-time.

## Motive

Many times, my programs need some information, which is generated at compile-time and used at run-time.
For example, Subversion's revision, JARs's dependencies, configuration files, and poor man's macro ;-), etc.

It is really heavyweight to deal with these situation in sbt.
You need to create a plugin for each type of files you generated,
to define many `SettingKey`s in each plugin's `.scala` files,
and to set values for each of these `SettingKey`s in your `.sbt` files.

There are some plugins, like [xsbt-reflect](https://github.com/ritschwumm/xsbt-reflect),
also able to generate some infomation for run-time. But they cannot fit every variant cases I met.
Another plugin, [sbt-buildinfo](https://github.com/sbt/sbt-buildinfo) has similar feature as `Pttrt`,
but `sbt-buildinfo` cannot generate multiply files, preventing itself to be used for code generation from multiply
separate plugins.

I just want a lightweight and general-purpose way like `Makefile`:

    generated.properties:
    	echo my.base.dir=$(PWD) > $@

That's why I created `Pttrt`.

## Usage

### Step 1: Install `Pttrt` into your project

Add the following line to your `project/plugins.sbt`:

    addSbtPlugin("com.dongxiguo" % "pttrt" % "0.1.0")

And add `pttrtSettings` to your `build.sbt`:

    pttrtSettings

### Step 2: Export the data that will be passed to run-time

For example, if you want to know the building version on run-time,
you need to add following lines at `build.sbt`:

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

    $ sbt
    > run-main PttrtExample

You will see:

    Building version is 1.2.3-SNAPSHOT

See https://github.com/Atry/pttrt/tree/master/pttrt-test for more example.

## Addition requirement

 * `Pttrt` is for sbt 0.12
 * Any value being passed to run-time must be a primary type (`Int`, `Double`, `Boolean`, etc) or `java.io.Serializable`
 * The value's type must be found in the classpath for both compile-time and run-time.
