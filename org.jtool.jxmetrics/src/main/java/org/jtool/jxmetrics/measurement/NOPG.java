/*
 *  Copyright 2018
 *  Software Science and Technology Lab.
 *  Department of Computer Science, Ritsumeikan University
 */

package org.jtool.jxmetrics.measurement;

import org.jtool.jxmetrics.core.ProjectMetrics;
import org.jtool.jxmetrics.core.UnsupportedMetricsException;

/**
 * Measures the value of Number of Packages.
 * 
 * @author Katsuhisa Maruyama
 */
public class NOPG extends Metric {
    
    public static final String Name = "NOPG";
    private static final String Description = "Number of packages";
    
    public NOPG() {
        super(Name, Description);
    }
    
    @Override
    public boolean isProjectMetric() {
        return true;
    }
    
    @Override
    public double valueOf(ProjectMetrics mproject) throws UnsupportedMetricsException {
        return mproject.getMetricValueWithException(NUMBER_OF_PACKAGES);
    }
}
