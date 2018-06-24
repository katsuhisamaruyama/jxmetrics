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

### From a release

You can directly download jar files of jxplatform2 on [GitHub](<https://github.com/katsuhisamaruyama/jxmetrics/tree/master/org.jtool.eclipse/releases>). 

### From the sources

You can build jxplatform2 with the following commands: 

    git clone https://github.com/katsuhisamaruyama/jxmetrics/
    cd jxmetrics/org.jtool.jxmetrics
    ./gradlew build jar shadowJar

Jar files of JxMetrics can be found in the 'build/libs' folder.

## Usage

### As a stand-alone application

`jxmetrics-1.0-all.jar` is an executable jar file.

When you put Java source code (usually expanded Java source files) under the `xxx` folder, the following command calculates several metric values for the source code and writes the values into XML a file (`xxx-<time>.xml`).

    java -jar jxmetrics-1.0-all -target xxx/ -classpath 'xxx/lib/*' -name xxx -logfile xxx.log

* `-classpath` specifies class paths where needed libraries are contained 
* `-name` specifies the name of a project managed in jxmetrics 
* `-logfile` specifies the name of a file in which the result of analysis is written 

These three options can be eliminated if they are needless. 

### As an Eclipse plug-in

You put `jxmetrics-1.0-lib.jar` in the 'plug-ins' directory under the Eclipse. Eclipse needs to be restarted. 

MetricsManager class provides APIs for calculating metric values. It also exports metric values into an XML file and imports ones from an XML file. 

The following code calculates metrics values for Java elements within an Eclipse's project and exports them into an XML file. 

    IJavaProject project;
    JavaProject jproject = ProjectManager.getInstance().build(project);
    MetricsManager manager = new MetricsManager();
    ProjectMetrics mproject = manager.calculate(jproject);
    manager.exportXML(mproject);

The following code imports metrics values from an XML file with a path name.

    String path;
    MetricsManager manager = new MetricsManager();
    ProjectMetrics mproject = manager.importXML(path);

### Samples

You can see [sample applications](<https://github.com/katsuhisamaruyama/jxmetrics/tree/master/org.jtool.jxmetrics.sample>) that imports metric values and displays them.


## Author

[Katsuhisa Maruyama](http://www.fse.cs.ritsumei.ac.jp/~maru/index.html)
