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

### For batch-process versions

You can build the batch-process versions of JxMetrics with the following commands:

    git clone https://github.com/katsuhisamaruyama/jxplatform2/
    git clone https://github.com/katsuhisamaruyama/jxmetrics/
    cd jxmetrics/org.jtool.jxmetrics
    ./gradlew build jar shadowJar

Jar files of JxMetrics can be found in the 'build/libs' folder.

### For an Eclipse plug-in version

You can import the JxMetrics project from this site, build it, and export a deployable jar file as an Eclipse plug-in

## Usage

### As a batch-process application

`jxmetrics-1.0-all.jar` is an executable jar file. When you put Java source code (usually expanded Java source files) under the `xxx` folder, the following command calculates several metric values for the source code and writes the values into an XML file (`xxx-<time>.xml`).

    java -jar jxmetrics-1.0-all.jar -target xxx/ -classpath 'xxx/lib/*' -name xxx -logfile xxx.log

* `-classpath`: (optional) specifies class paths where needed libraries are contained 
* `-name`: (optional) specifies the name of a project managed in jxmetrics 
* `-logfile`:(optional) specifies the name of a file in which the result of analysis is written 

If your batch-process application employs JxPlatform2, you should use `jxmetrics-1.0-lib.jar` instead of `jxplatform-1.0-all.jar`.

`MetricsManager` class provides APIs for calculating metric values. It also exports metric values into an XML file and imports ones from an XML file. The following API calls write the measured values into an XML file. In this case, `jxplatform-1.0-lib.jar` is also needed.

    JavaModelBuilder builder = new JavaModelBuilder(name, target, classpath);
    JavaProject jproject = builder.build();
    MetricsManager manager = new MetricsManager();
    ProjectMetrics mproject = manager.calculate(jproject);
    manager.exportXML(mproject);
    Logger.getInstance().writeLog();
    builder.unbuild();

The following code imports metrics values from an XML file with a path name.

    String path;  // The path of an XNL file that contains metrics data
    MetricsManager manager = new MetricsManager();
    ProjectMetrics mproject = manager.importXML(path);


### As an Eclipse plug-in

`MetricsManager` class provides APIs for calculating metric values. It also exports metric values into an XML file and imports ones from an XML file. 

The following code calculates metrics values for Java elements within a specified Eclipse's project and exports them into an XML file. 

    ModelBuilderPlugin builder = new ModelBuilderPlugin();
    JavaProject jproject = builder.build(project);
    MetricsManager manager = new MetricsManager();
    ProjectMetrics mproject = manager.calculate(jproject);
    manager.exportXML(mproject);

### Samples

You can see [sample applications](<https://github.com/katsuhisamaruyama/jxmetrics/tree/master/org.jtool.jxmetrics.sample>) that imports metric values and displays them.


## Author

[Katsuhisa Maruyama](http://www.fse.cs.ritsumei.ac.jp/~maru/index.html)
