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
public class LCOM extends Metric {
    
    public static final String Name = "LCOM";
    private static final String Description = "Lack of cohesion methods";
    
    public LCOM() {
        super(Name, Description);
    }
    
    @Override
    public boolean isClassMetric() {
        return true;
    }
    
    @Override
    public double valueOf(ClassMetrics mclass) throws UnsupportedMetricsException {
        return mclass.getMetricValueWithException(LACK_OF_COHESION_OF_METHODS);
    }
    
    @Override
    public double maxValueIn(ProjectMetrics mproject) throws UnsupportedMetricsException {
        return mproject.getMetricValueWithException(MAX_LACK_OF_COHESION_OF_METHODS);
    }
    
    @Override
    public double maxValueIn(PackageMetrics mpackage) throws UnsupportedMetricsException {
        return mpackage.getMetricValueWithException(MAX_LACK_OF_COHESION_OF_METHODS);
    }
}
