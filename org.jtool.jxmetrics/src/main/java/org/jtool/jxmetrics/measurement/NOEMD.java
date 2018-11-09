/*
 *  Copyright 2018
 *  Software Science and Technology Lab.
 *  Department of Computer Science, Ritsumeikan University
 */

package org.jtool.jxmetrics.measurement;

import org.jtool.jxmetrics.core.ClassMetrics;
import org.jtool.jxmetrics.core.MethodMetrics;
import org.jtool.jxmetrics.core.UnsupportedMetricsException;
import org.jtool.eclipse.javamodel.JavaMethod;

/**
 * Measures the value of Number of Efferent Methods.
 * 
 * @author Katsuhisa Maruyama
 */
public class NOEMD extends Metric {
    
    public static final String Name = "NOEMD";
    private static final String Description = "Number of Efferent Methods";
    
    public NOEMD() {
        super(Name, Description);
    }
    
    public double calculate(JavaMethod jmethod) {
        return (double)jmethod.getCalledMethods().size();
    }
    
    @Override
    public double valueOf(ClassMetrics mclass) throws UnsupportedMetricsException {
        return mclass.getMetricValueWithException(Name);
    }
    
    @Override
    public double valueOf(MethodMetrics mmethod) throws UnsupportedMetricsException {
        return mmethod.getMetricValueWithException(Name);
    }
    
    @Override
    public double maxValueIn(ClassMetrics mclass) throws UnsupportedMetricsException {
        return mclass.getMetricValueWithException(MAX + Name);
    }
}
