/*
 *  Copyright 2018
 *  Software Science and Technology Lab.
 *  Department of Computer Science, Ritsumeikan University
 */

package org.jtool.jxmetrics.measurement;

import org.jtool.jxmetrics.core.ProjectMetrics;
import org.jtool.jxmetrics.core.PackageMetrics;
import org.jtool.jxmetrics.core.ClassMetrics;
import org.jtool.jxmetrics.core.UnsupportedMetricsException;
import org.jtool.eclipse.javamodel.JavaClass;

/**
 * Measures the value of Number of Methods.
 * 
 * @author Katsuhisa Maruyama
 */
public class NOMD extends Metric {
    
    public static final String Name = "NOMD";
    private static final String Description = "Number of Methods";
    
    public NOMD() {
        super(Name, Description);
    }
    
    public double calculate(JavaClass jclass) {
        return (double)jclass.getMethods().size();
    }
    
    @Override
    public double valueOf(ProjectMetrics mproject) throws UnsupportedMetricsException {
        return mproject.getMetricValueWithException(Name);
    }
    
    @Override
    public double valueOf(PackageMetrics mpackage) throws UnsupportedMetricsException {
        return mpackage.getMetricValueWithException(Name);
    }
    
    @Override
    public double valueOf(ClassMetrics mclass) throws UnsupportedMetricsException {
        return mclass.getMetricValueWithException(Name);
    }
    
    @Override
    public double maxValueIn(ProjectMetrics mproject) throws UnsupportedMetricsException {
        return mproject.getMetricValueWithException(MAX + Name);
    }
    
    @Override
    public double maxValueIn(PackageMetrics mpackage) throws UnsupportedMetricsException {
        return mpackage.getMetricValueWithException(MAX + Name);
    }
}
