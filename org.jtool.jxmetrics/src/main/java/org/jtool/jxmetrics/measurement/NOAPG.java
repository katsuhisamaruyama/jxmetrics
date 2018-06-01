/*
 *  Copyright 2018
 *  Software Science and Technology Lab.
 *  Department of Computer Science, Ritsumeikan University
 */

package org.jtool.jxmetrics.measurement;

import org.jtool.jxmetrics.core.PackageMetrics;
import org.jtool.jxmetrics.core.ProjectMetrics;
import org.jtool.jxmetrics.core.UnsupportedMetricsException;

/**
 * @author Katsuhisa Maruyama
 */
public class NOAPG extends Metric {
    
    public static final String Name = "NOAPG";
    private static final String Description = "Number of afferent packages";
    
    public NOAPG() {
        super(Name, Description);
    }
    
    @Override
    public boolean isPackageMetric() {
        return true;
    }
    
    @Override
    public double valueOf(PackageMetrics mpackage) throws UnsupportedMetricsException {
        return mpackage.getMetricValueWithException(NUMBER_OF_AFFERENT_PACKAGES);
    }
    
    @Override
    public double maxValueIn(ProjectMetrics mproject) throws UnsupportedMetricsException {
        return mproject.getMetricValueWithException(MAX_NUMBER_OF_AFFERENT_PACKAGES);
    }
}
