/*
 *  Copyright 2018
 *  Software Science and Technology Lab.
 *  Department of Computer Science, Ritsumeikan University
 */

package org.jtool.jxmetrics.measurement;

import org.jtool.jxmetrics.core.ClassMetrics;
import org.jtool.jxmetrics.core.PackageMetrics;
import org.jtool.jxmetrics.core.ProjectMetrics;
import org.jtool.jxmetrics.core.UnsupportedMetricsException;

/**
 * @author Katsuhisa Maruyama
 */
public class NOPM extends Metric {
    
    public static final String Name = "NOPM";
    private static final String Description = "Number of public methods";
    
    public NOPM() {
        super(Name, Description);
    }
    
    @Override
    public boolean isProjectMetric() {
        return true;
    }
    
    @Override
    public boolean isPackageMetric() {
        return true;
    }
    
    @Override
    public boolean isClassMetric() {
        return true;
    }
    
    @Override
    public double valueOf(ProjectMetrics mproject) throws UnsupportedMetricsException {
        return mproject.getMetricValueWithException(NUMBER_OF_PUBLIC_METHODS);
    }
    
    @Override
    public double valueOf(PackageMetrics mpackage) throws UnsupportedMetricsException {
        return mpackage.getMetricValueWithException(NUMBER_OF_PUBLIC_METHODS);
    }
    
    @Override
    public double valueOf(ClassMetrics mclass) throws UnsupportedMetricsException {
        return mclass.getMetricValueWithException(NUMBER_OF_PUBLIC_METHODS);
    }
    
    @Override
    public double maxValueIn(ProjectMetrics mproject) throws UnsupportedMetricsException {
        return mproject.getMetricValueWithException(MAX_NUMBER_OF_PUBLIC_METHODS);
    }
    
    @Override
    public double maxValueIn(PackageMetrics mpackage) throws UnsupportedMetricsException {
        return mpackage.getMetricValueWithException(MAX_NUMBER_OF_PUBLIC_METHODS);
    }
}
