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
 * Measures the value of Number of Efferent Fields.
 * 
 * @author Katsuhisa Maruyama
 */
public class NOEFD extends Metric {
    
    public static final String Name = "NOEFD";
    private static final String Description = "Number of efferent fields";
    
    public NOEFD() {
        super(Name, Description);
    }
    
    @Override
    public boolean isClassMetric() {
        return true;
    }
    
    @Override
    public boolean isFieldMetric() {
        return true;
    }
    
    @Override
    public double valueOf(ClassMetrics mclass) throws UnsupportedMetricsException {
        return mclass.getMetricValueWithException(NUMBER_OF_EFFERENT_FIELDS);
    }
    
    @Override
    public double valueOf(MethodMetrics mmethod) throws UnsupportedMetricsException {
        return mmethod.getMetricValueWithException(NUMBER_OF_EFFERENT_FIELDS);
    }
    
    @Override
    public double maxValueIn(ClassMetrics mclass) throws UnsupportedMetricsException {
        return mclass.getMetricValueWithException(MAX_NUMBER_OF_EFFERENT_FIELDS);
    }
}
