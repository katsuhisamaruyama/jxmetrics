/*
 *  Copyright 2018-2019
 *  Software Science and Technology Lab.
 *  Department of Computer Science, Ritsumeikan University
 */

package org.jtool.jxmetrics.batch;

import org.jtool.jxmetrics.core.MetricsManager;
import org.jtool.jxmetrics.core.ProjectMetrics;
import org.jtool.eclipse.batch.JavaModelBuilder;
import org.jtool.eclipse.javamodel.JavaProject;
import org.jtool.eclipse.util.Logger;

/**
 * Calculates metric values.
 * 
 * @author Katsuhisa Maruyama
 */
public class MetricsCalculator {
    
    private JavaModelBuilder builder;
    
    public MetricsCalculator(String[] args) {
        builder = new JavaModelBuilder(args);
    }
    
    public MetricsCalculator(String name, String target) {
        this(name, target, target);
    }
    
    public MetricsCalculator(String name, String target, String classpath) {
        builder = new JavaModelBuilder(name, target, classpath);
    }
    
    public void run() {
        JavaProject jproject = builder.build();
        MetricsManager manager = new MetricsManager();
        ProjectMetrics mproject = manager.calculate(jproject);
        manager.exportXML(mproject);
        Logger.getInstance().writeLog();
        builder.unbuild();
    }
    
    public static void main(String[] args) {
        MetricsCalculator calculator = new MetricsCalculator(args);
        calculator.run();
    }
}
