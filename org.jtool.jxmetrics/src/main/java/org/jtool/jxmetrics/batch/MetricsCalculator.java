/*
 *  Copyright 2018-2019
 *  Software Science and Technology Lab.
 *  Department of Computer Science, Ritsumeikan University
 */

package org.jtool.jxmetrics.batch;

import org.jtool.jxmetrics.core.MetricsManager;
import org.jtool.jxmetrics.core.ProjectMetrics;

import java.util.List;

import org.jtool.eclipse.batch.JavaModelBuilder;
import org.jtool.eclipse.javamodel.JavaProject;
import org.jtool.eclipse.util.Logger;
import org.jtool.eclipse.util.Options;

/**
 * Calculates metric values.
 * 
 * @author Katsuhisa Maruyama
 */
public class MetricsCalculator {
    
    private JavaModelBuilder builder;
    private String outputfile;
    
    public MetricsCalculator(String[] args) {
        Options options = new Options(args);
        outputfile = options.get("-output", null);
        
        builder = new JavaModelBuilder(args);
    }
    
    public MetricsCalculator(String name, String target) {
        this(name, target, target, target, target);
    }
    
    public MetricsCalculator(String name, String target, String classpath) {
        this(name, target, classpath, target, target);
    }
    
    public MetricsCalculator(String name, String target, String classpath, String srcpath, String binpath) {
        builder = new JavaModelBuilder(name, target, classpath, srcpath, binpath);
    }
    
    public void run() {
        List<JavaProject> jprojects = builder.build();
        MetricsManager manager = new MetricsManager();
        for (JavaProject jproject : jprojects) {
            ProjectMetrics mproject = manager.calculate(jproject);
            manager.exportXML(mproject, outputfile);
            Logger.getInstance().writeLog();
        }
        builder.unbuild();
    }
    
    public static void main(String[] args) {
        MetricsCalculator calculator = new MetricsCalculator(args);
        calculator.run();
    }
}
