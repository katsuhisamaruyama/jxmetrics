/*
 *  Copyright 2018
 *  Software Science and Technology Lab.
 *  Department of Computer Science, Ritsumeikan University
 */

package org.jtool.jxmetrics.measurement;

import org.jtool.jxmetrics.core.ClassMetrics;
import org.jtool.jxmetrics.core.MethodMetrics;
import org.jtool.jxmetrics.core.UnsupportedMetricsException;
import org.jtool.eclipse.javamodel.JavaProject;
import org.jtool.eclipse.javamodel.JavaMethod;

import java.math.BigDecimal;
import java.util.Set;

/**
 * Measures the value of Locality of Attribute Accesses.
 * 
 * @author Katsuhisa Maruyama
 */
public class LAA extends Metric {
    
    public static final String Name = "LAA";
    private static final String Description = "Locality of Attribute Accesses";
    
    public LAA() {
        super(Name, Description);
    }
    
    public double calculate(JavaProject jproject, JavaMethod jmethod) {
        Set<String> accessedFields = MethodMetrics.getAccessedFields(jproject, jmethod);
        int num = accessedFields.size();
        double value = 0;
        if (num > 0) {
            value = (double)jmethod.getDeclaringClass().getFields().size() / num;
        }
        return new BigDecimal(value).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
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
