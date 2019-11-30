# JxMetrics 

JxMetrics is a module that calculates metrics values for elements within Java source code. 

### Source Code Model 

JxMetrics calculates several metric values for a project, package, class, method, and field and stores such information into the following classes: 

* ProjectMetrics - Stores metric information on a project 
* PackageMetrics - Stores metric information on a package 
* ClassMetrics - Stores metric information on a class 
* MethodMetrics - Stores metrics information on a method 
* FieldMetrics - Stores metrics information on a field 

The measurement metrics are shown [here](<https://github.com/katsuhisamaruyama/jxmetrics/tree/master/org.jtool.jxmetrics/src/main/java/org/jtool/jxmetrics/measurement>).

MetricsManager class provides APIs for calculating metric values. It also exports metric values into an XML file and imports ones from an XML file.

## Requirement

JDK 1.8 
[Eclipse](https://www.eclipse.org/) 4.7 (Oxygen) and later  

## License 

[Eclipse Public License 1.0 (EPL-1.0)](<https://opensource.org/licenses/eclipse-1.0.php>) 

## Installation

### As batch-process application

JxMetrics requires [JxPlatform2](<https://github.com/katsuhisamaruyama/jxplatform2>).
You can build the batch-process versions of JxMetrics with the following commands:

    git clone https://github.com/katsuhisamaruyama/jxplatform2/
    git clone https://github.com/katsuhisamaruyama/jxmetrics/
    cd jxmetrics/org.jtool.jxmetrics
    ./gradlew build jar shadowJar

Jar files of JxMetrics can be found in the 'build/libs' folder.

### As a library

JxMetrics requires the following library files.

* `org.eclipse.core.contenttype-*.jar`
* `org.eclipse.core.jobs-*.jar`
* `org.eclipse.core.resources-*.jar`
* `org.eclipse.core.runtime-*.jar`
* `org.eclipse.equinox.common-*.jar`
* `org.eclipse.equinox.preferences-*.jar`
* `org.eclipse.jdt.core-*.jar`
* `org.eclipse.osgi-*.jar`
* `javassist.jar`
* `jxplatform-1.0.jar`

JxMetrics (`jxmetrics-1.0-lib.jar`) and the above jar files must be included in the build path and the runtime classpath under your development environment. When using the Eclipse, see the "Build Path" settings of a project.

### As an Eclipse plug-in

You download `jxmetrics-1.0-lib.jar` from [here](<https://github.com/katsuhisamaruyama/jxmetrics/tree/master/org.jtool.jxmetrics/releases>), and put it into the plug-in folder of your Eclipse. Eclipse needs to be restarted. 

The following plug-ins are listed on "Required plug-ins" of the plug-in configuration for your application. 

* `org.eclipse.core.resources`
* `org.eclipse.core.runtime`
* `org.eclipse.jdt.core`
* `org.eclipse.ui`
* `org.eclipse.ui.console`
* `org.eclipse.text`
* `org.jtool.eclipse`
* `org.jtool.jxmetrics`


## Usage

### As a batch-process application

`jxmetrics-1.0-all.jar` is an executable jar file. When you put Java source code (usually expanded Java source files) under the `xxx` folder, the following command calculates several metric values for the source code and writes the values into an XML file (`xxx-<time>.xml`).

    java -jar  jxmetrics-1.0-all.jar -target xxx/ -classpath "xxx/lib/*:xxx/libs/*" -srcpath "xxx/src:xxx/test" -binpath "xxx/bin" -name name -logfile xxx.log

* `-target`: (optional) specifies the path of a target project (default: the current directory)
* `-classpath`: (optional) specifies class paths where needed libraries are contained 
* `-srcpath`: (optional) specifies paths where source files are contained 
* `-binpath`: (optional) specifies paths where binary files are generated 
* `-name`: (optional) specifies the name of a target project 
* `-logfile`:(optional) specifies the name of a file in which the result of analysis is written 

If a path does not start with the char '/', the path of the target project is added prior to the speciv.


### Building an applicatio/plug-in embedding JxMetrics

`MetricsManager` class provides APIs for calculating metric values. It also exports metric values into an XML file and imports ones from an XML file. The following API calls write the measured values into an XML file. 

    /* Use the library version of jxplatform2 */
    // JavaModelBuilder builder = new JavaModelBuilder(name, target, classpath, srcpath, binpath)
    // JavaProject jproject = builder.build();
    
    /* Use the plug-in version of jxplatform2 */
    // ModelBuilderPlugin builder = new ModelBuilderPlugin();
    // JavaProject jproject = builder.build(project);
    
    String path;  // The path of an XNL file that will contain metrics data
    MetricsManager manager = new MetricsManager();
    ProjectMetrics mproject = manager.calculate(jproject);
    manager.exportXML(mproject, path);
    Logger.getInstance().writeLog();
    builder.unbuild();

The following code imports metrics values from an XML file with a path name.

    String path;  // The path of an XNL file that contains metrics data
    MetricsManager manager = new MetricsManager();
    ProjectMetrics mproject = manager.importXML(path);

`MetricsManager` class provides APIs for calculating metric values. It also exports metric values into an XML file and imports ones from an XML file. 

### Samples

You can see [samples](<https://github.com/katsuhisamaruyama/jxmetrics/tree/master/org.jtool.jxmetrics.sample>) that imports metric values and displays them.


## Author

[Katsuhisa Maruyama](http://www.fse.cs.ritsumei.ac.jp/~maru/index.html)
