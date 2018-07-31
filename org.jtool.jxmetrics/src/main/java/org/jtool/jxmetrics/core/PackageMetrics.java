/*
 *  Copyright 2018
 *  Software Science and Technology Lab.
 *  Department of Computer Science, Ritsumeikan University
 */

package org.jtool.jxmetrics.core;

import org.jtool.eclipse.javamodel.JavaPackage;
import org.jtool.eclipse.javamodel.JavaClass;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Stores metric information on a package.
 * @author Katsuhisa Maruyama
 */
public class PackageMetrics extends Metrics implements MetricsSort {
    
    public static final String Id = "PackageMetrics";
    
    private ProjectMetrics projectMetrics;
    
    protected List<ClassMetrics> classes = new ArrayList<ClassMetrics>();
    protected List<String> afferentPackageNames = new ArrayList<String>();
    protected List<String> efferentPackageNames = new ArrayList<String>();
    
    public PackageMetrics(JavaPackage jpackage, ProjectMetrics mproject) {
        super(jpackage.getName());
        
        projectMetrics = mproject;
        for (JavaClass jclass : jpackage.getClasses()) {
            ClassMetrics mclass = new ClassMetrics(jclass, this);
            classes.add(mclass);
        }
        
        for (JavaPackage jp : jpackage.getAfferentJavaPackages()) {
            addAfferentPackage(jp.getName());
        }
        for (JavaPackage jp : jpackage.getEfferentJavaPackages()) {
            addEfferentPackage(jp.getName());
        }
        
        ClassMetrics.sort(classes);
        sortNames(afferentPackageNames);
        sortNames(efferentPackageNames);
        collectMetrics(jpackage);
        collectMetricsMax();
    }
    
    public String getName() {
        return fqn;
    }
    
    public PackageMetrics(String name, ProjectMetrics mproject) {
        super(name);
        projectMetrics = mproject;
    }
    
    public ProjectMetrics getProject() {
        return projectMetrics;
    }
    
    protected void addClass(ClassMetrics mclass) {
        classes.add(mclass);
    }
    
    public List<ClassMetrics> getClasses() {
        return classes;
    }
    
    public void sortClasses() {
        ClassMetrics.sort(classes);
    }
    
    protected void addAfferentPackage(String name) {
        if (!afferentPackageNames.contains(name)) {
            afferentPackageNames.add(name);
        }
    }
    
    public List<String> getAfferentPackages() {
        return afferentPackageNames;
    }
    
    protected void addEfferentPackage(String name) {
        if (!efferentPackageNames.contains(name)) {
            efferentPackageNames.add(name);
        }
    }
    
    public List<String> getEfferentPackages() {
        return efferentPackageNames;
    }
    
    protected void collectMetrics(JavaPackage jpackage) {
        putMetricValue(LINES_OF_CODE, sum(LINES_OF_CODE));
        putMetricValue(NUMBER_OF_STATEMENTS, sum(NUMBER_OF_STATEMENTS));
        
        putMetricValue(NUMBER_OF_CLASSES, new Double(jpackage.getClasses().size()));
        putMetricValue(NUMBER_OF_METHODS, sum(NUMBER_OF_METHODS));
        putMetricValue(NUMBER_OF_FIELDS, sum(NUMBER_OF_FIELDS));
        putMetricValue(NUMBER_OF_METHODS_AND_FIELDS, sum(NUMBER_OF_METHODS_AND_FIELDS));
        putMetricValue(NUMBER_OF_PUBLIC_METHODS, sum(NUMBER_OF_PUBLIC_METHODS));
        putMetricValue(NUMBER_OF_PUBLIC_FIELDS, sum(NUMBER_OF_PUBLIC_FIELDS));
        
        putMetricValue(NUMBER_OF_AFFERENT_PACKAGES, new Double(jpackage.getAfferentJavaPackages().size()));
        putMetricValue(NUMBER_OF_EFFERENT_PACKAGES, new Double(jpackage.getEfferentJavaPackages().size()));
        
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
    
    protected Double sum(String sort) {
        double value = 0;
        for (ClassMetrics mclass : classes) {
            value = value + mclass.getMetricValue(sort);
        }
        return new Double(value);
    }
    
    protected Double max(String sort) {
        double value = 0;
        for (ClassMetrics mclass : classes) {
            value = Math.max(value, mclass.getMetricValue(sort));
        }
        return new Double(value);
    }
    
    public static void sort(List<PackageMetrics> packages) {
        Collections.sort(packages, new Comparator<PackageMetrics>() {
            public int compare(PackageMetrics mpackage1, PackageMetrics mpackage2) {
                return mpackage1.getName().compareTo(mpackage2.getName());
            }
        });
    }
    
    public void collectMetricsAfterXMLImport() {
        ClassMetrics.sort(classes);
        sortNames(afferentPackageNames);
        sortNames(efferentPackageNames);
        for (ClassMetrics mclass : classes) {
            mclass.collectMetricsAfterXMLImport();
        }
    }
}
