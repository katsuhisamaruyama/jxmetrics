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
 * Measures the value of Maximum Number of Nesting.
 * 
 * @author Katsuhisa Maruyama
 */
public class NEST extends Metric {
    
    public static final String Name = "NEST";
    private static final String Description = "Maximum Number of Nesting";
    
    public NEST() {
        super(Name, Description);
    }
    
    public double calculate(JavaMethod jmethod) {
        return (double)jmethod.getMaximumNumberOfNesting();
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
