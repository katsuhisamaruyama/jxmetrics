/*
 *  Copyright 2018
 *  Software Science and Technology Lab.
 *  Department of Computer Science, Ritsumeikan University
 */

package org.jtool.jxmetrics.core;

import org.jtool.eclipse.javamodel.JavaProject;
import org.jtool.eclipse.util.TimeInfo;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.time.ZonedDateTime;

/**
 * Stores metric information on a project.
 * 
 * @author Katsuhisa Maruyama
 */
public class ProjectMetrics extends Metrics implements MetricsSort {
    
    public static final String Id = "ProjectMetrics";
    
    protected String path;
    protected String dir;
    protected ZonedDateTime time;
    
    protected List<PackageMetrics> packages = new ArrayList<PackageMetrics>();
    
    public ProjectMetrics(JavaProject jproject, ZonedDateTime time) {
        super(jproject.getName());
        
        path = jproject.getPath();
        dir = jproject.getDir();
        this.time = time;
    }
    
    public void collect(JavaProject jproject) {
        PackageMetrics.sort(packages);
        collectMetrics(jproject);
        collectMetricsMax();
    }
    
    public ProjectMetrics(String name, String path, ZonedDateTime time) {
        super(name);
        this.path = path;
        this.time = time;
    }
    
    public String getName() {
        return fqn;
    }
    
    public String getPath() {
        return path;
    }
    
    public String getDir() {
        return dir;
    }
    
    public ZonedDateTime getTime() {
        return time;
    }
    
    public long getTimeAsLong() {
        return TimeInfo.getTimeAsLong(time);
    }
    
    public String getTimeAsString() {
        return TimeInfo.getTimeAsISOString(time);
    }
    
    public String getFormatedDate() {
        return TimeInfo.getFormatedDate(time);
    }
    
    public void addPackage(PackageMetrics mpackage) {
        if (!packages.contains(mpackage)) {
            packages.add(mpackage);
        }
    }
    
    public List<PackageMetrics> getPackages() {
        return packages;
    }
    
    public void sortPackages() {
        PackageMetrics.sort(packages);
    }
    
    public List<ClassMetrics> getClasses() {
        List<ClassMetrics> classes = new ArrayList<ClassMetrics>();
        for (PackageMetrics mpackage : getPackages()) {
            classes.addAll(mpackage.getClasses());
        }
        ClassMetrics.sort(classes);
        return classes;
    }
    
    private void collectMetrics(JavaProject jproject) {
        putMetricValue(LINES_OF_CODE, sum(LINES_OF_CODE));
        putMetricValue(NUMBER_OF_STATEMENTS, sum(NUMBER_OF_STATEMENTS));
        
        putMetricValue(NUMBER_OF_FILES, new Double(jproject.getFiles().size()));
        putMetricValue(NUMBER_OF_PACKAGES, new Double(jproject.getPackages().size()));
        
        putMetricValue(NUMBER_OF_CLASSES, sum(NUMBER_OF_CLASSES));
        putMetricValue(NUMBER_OF_METHODS, sum(NUMBER_OF_METHODS));
        putMetricValue(NUMBER_OF_FIELDS, sum(NUMBER_OF_FIELDS));
        putMetricValue(NUMBER_OF_METHODS_AND_FIELDS, sum(NUMBER_OF_METHODS_AND_FIELDS));
        putMetricValue(NUMBER_OF_PUBLIC_METHODS, sum(NUMBER_OF_PUBLIC_METHODS));
        putMetricValue(NUMBER_OF_PUBLIC_FIELDS, sum(NUMBER_OF_PUBLIC_FIELDS));
        
        putMetricValue(NUMBER_OF_AFFERENT_CLASSES, sum(NUMBER_OF_AFFERENT_CLASSES));
        putMetricValue(NUMBER_OF_EFFERENT_CLASSES, sum(NUMBER_OF_EFFERENT_CLASSES));
        
        putMetricValue(COUPLING_BETWEEN_OBJECTS, sum(COUPLING_BETWEEN_OBJECTS));
        putMetricValue(DEPTH_OF_INHERITANCE_TREE, sum(DEPTH_OF_INHERITANCE_TREE));
        putMetricValue(NUMBER_OF_CHILDREN, sum(NUMBER_OF_CHILDREN));
        putMetricValue(RESPONSE_FOR_CLASS, sum(RESPONSE_FOR_CLASS));
        putMetricValue(WEIGHTED_METHODS_PER_CLASS, sum(WEIGHTED_METHODS_PER_CLASS));
        putMetricValue(LACK_OF_COHESION_OF_METHODS, sum(LACK_OF_COHESION_OF_METHODS));
    }
    
    protected void collectMetricsMax() {
        putMetricValue(MAX_LINES_OF_CODE, max(LINES_OF_CODE));
        putMetricValue(MAX_NUMBER_OF_STATEMENTS, max(NUMBER_OF_STATEMENTS));
        
        putMetricValue(MAX_NUMBER_OF_CLASSES, max(NUMBER_OF_CLASSES));
        putMetricValue(MAX_NUMBER_OF_METHODS, max(NUMBER_OF_METHODS));
        putMetricValue(MAX_NUMBER_OF_FIELDS, max(NUMBER_OF_FIELDS));
        putMetricValue(MAX_NUMBER_OF_METHODS_AND_FIELDS, max(NUMBER_OF_METHODS_AND_FIELDS));
        putMetricValue(MAX_NUMBER_OF_PUBLIC_METHODS, max(NUMBER_OF_PUBLIC_METHODS));
        putMetricValue(MAX_NUMBER_OF_PUBLIC_FIELDS, max(NUMBER_OF_PUBLIC_FIELDS));
        
        putMetricValue(MAX_NUMBER_OF_AFFERENT_CLASSES, max(NUMBER_OF_AFFERENT_CLASSES));
        putMetricValue(MAX_NUMBER_OF_EFFERENT_CLASSES, max(NUMBER_OF_EFFERENT_CLASSES));
        
        putMetricValue(MAX_COUPLING_BETWEEN_OBJECTS, max(COUPLING_BETWEEN_OBJECTS));
        putMetricValue(MAX_DEPTH_OF_INHERITANCE_TREE, max(DEPTH_OF_INHERITANCE_TREE));
        putMetricValue(MAX_NUMBER_OF_CHILDREN, max(NUMBER_OF_CHILDREN));
        putMetricValue(MAX_RESPONSE_FOR_CLASS, max(RESPONSE_FOR_CLASS));
        putMetricValue(MAX_WEIGHTED_METHODS_PER_CLASS, max(WEIGHTED_METHODS_PER_CLASS));
        putMetricValue(MAX_LACK_OF_COHESION_OF_METHODS, max(LACK_OF_COHESION_OF_METHODS));
    }
    
    private Double sum(String sort) {
        double value = 0;
        for (PackageMetrics mpackage : packages) {
            value = value + mpackage.getMetricValue(sort);
        }
        return new Double(value);
    }
    
    protected Double max(String sort) {
        double value = 0;
        for (PackageMetrics mpackage : packages) {
            for (ClassMetrics mclass : mpackage.getClasses()) {
                value = Math.max(value, mclass.getMetricValue(sort));
            }
        }
        return new Double(value);
    }
    
    public void collectMetricsAfterXMLImport() {
        PackageMetrics.sort(packages);
        for (PackageMetrics mpackage : packages) {
            mpackage.collectMetricsAfterXMLImport();
        }
    }
    
    public static void sort(List<ProjectMetrics> projects) {
        Collections.sort(projects, new Comparator<ProjectMetrics>() {
            
            public int compare(ProjectMetrics project1, ProjectMetrics project2) {
                long time1 = project1.getTimeAsLong();
                long time2 = project2.getTimeAsLong();
                if (time1 > time2) {
                    return 1;
                } else if (time1 < time2) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });
    }
}
