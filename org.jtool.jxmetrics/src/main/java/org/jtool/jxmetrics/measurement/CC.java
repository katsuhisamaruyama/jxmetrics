/*
 *  Copyright 2018
 *  Software Science and Technology Lab.
 *  Department of Computer Science, Ritsumeikan University
 */

package org.jtool.jxmetrics.measurement;

import org.jtool.jxmetrics.core.ClassMetrics;
import org.jtool.jxmetrics.core.MethodMetrics;
import org.jtool.jxmetrics.core.UnsupportedMetricsException;

/**
 * @author Katsuhisa Maruyama
 */
public class CC extends Metric {
    
    public static final String Name = "CC";
    private static final String Description = "Cyclomatic complexity";
    
    public CC() {
        super(Name, Description);
    }
    
    @Override
    public boolean isMethodMetric() {
        return true;
    }
    
    @Override
    public double valueOf(MethodMetrics mmethod) throws UnsupportedMetricsException {
        return mmethod.getMetricValueWithException(CYCLOMATIC_COMPLEXITY);
    }
    
    @Override
    public double maxValueIn(ClassMetrics mclass) throws UnsupportedMetricsException {
        return mclass.getMetricValueWithException(MAX_CYCLOMATIC_COMPLEXITY);
    }
}
