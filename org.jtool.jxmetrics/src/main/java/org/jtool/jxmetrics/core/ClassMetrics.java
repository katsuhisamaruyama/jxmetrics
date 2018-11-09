/*
 *  Copyright 2018
 *  Software Science and Technology Lab.
 *  Department of Computer Science, Ritsumeikan University
 */

package org.jtool.jxmetrics.core;

import org.jtool.jxmetrics.measurement.LOC;
import org.jtool.jxmetrics.measurement.Metric;
import org.jtool.jxmetrics.measurement.NOST;
import org.jtool.jxmetrics.measurement.CBO;
import org.jtool.jxmetrics.measurement.DIT;
import org.jtool.jxmetrics.measurement.NOC;
import org.jtool.jxmetrics.measurement.RFC;
import org.jtool.jxmetrics.measurement.WMC;
import org.jtool.jxmetrics.measurement.LCOM;
import org.jtool.jxmetrics.measurement.ATFD;
import org.jtool.jxmetrics.measurement.TCC;
import org.jtool.jxmetrics.measurement.NOACL;
import org.jtool.jxmetrics.measurement.NOECL;
import org.jtool.jxmetrics.measurement.NOMD;
import org.jtool.jxmetrics.measurement.NOFD;
import org.jtool.jxmetrics.measurement.NOPMD;
import org.jtool.jxmetrics.measurement.NOPFD;
import org.jtool.jxmetrics.measurement.NOMDFD;
import org.jtool.jxmetrics.measurement.NOAMD;
import org.jtool.jxmetrics.measurement.NOEMD;
import org.jtool.jxmetrics.measurement.NOEFD;
import org.jtool.jxmetrics.measurement.CYCLO;
import org.jtool.jxmetrics.measurement.NEST;
import org.jtool.jxmetrics.measurement.PAR;
import org.jtool.jxmetrics.measurement.LVAR;
import org.jtool.eclipse.javamodel.JavaProject;
import org.jtool.eclipse.javamodel.JavaClass;
import org.jtool.eclipse.javamodel.JavaField;
import org.jtool.eclipse.javamodel.JavaMethod;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.io.File;
import java.math.BigDecimal;

/**
 * Stores metric information on a class.
 * 
 * @author Katsuhisa Maruyama
 */
public class ClassMetrics extends CommonMetrics {
    
    public static final String Id = "ClassMetrics";
    
    protected JavaClass.Kind kind;
    protected PackageMetrics packageMetrics;
    protected String superClassName;
    protected List<String> superInterfaceNames = new ArrayList<String>();
    protected String path;
    
    protected List<MethodMetrics> methods = new ArrayList<MethodMetrics>();
    protected List<FieldMetrics> fields = new ArrayList<FieldMetrics>();
    protected List<String> afferentClassNames = new ArrayList<String>();
    protected List<String> efferentClassNames = new ArrayList<String>();
    
    protected ClassMetrics(String fqn, String name, String type, int modifiers) {
        super(fqn, name, type, modifiers);
    }
    
    public ClassMetrics(JavaProject jproject, JavaClass jclass, PackageMetrics mpackage) {
        this(jclass.getQualifiedName(), jclass.getName(), jclass.getQualifiedName(), jclass.getModifiers());
        
        kind = jclass.getKind();
        packageMetrics = mpackage;
        superClassName = jclass.getSuperClassName();
        superInterfaceNames.addAll(jclass.getSuperInterfaceNames());
        path = jclass.getFile().getRelativePath();
        setCodeProperties(jclass);
        
        for (JavaMethod jmethod : jclass.getMethods()) {
            methods.add(new MethodMetrics(jproject, jmethod, this));
        }
        for (JavaField jfield: jclass.getFields()) {
            fields.add(new FieldMetrics(jproject, jfield, this));
        }
        
        for (JavaClass jc : jclass.getAfferentClasses()) {
            addAfferentClass(jc.getQualifiedName());
        }
        for (JavaClass jc : jclass.getEfferentClasses()) {
            addEfferentClass(jc.getQualifiedName());
        }
        
        MethodMetrics.sort(methods);
        FieldMetrics.sort(fields);
        sortNames(afferentClassNames);
        sortNames(efferentClassNames);
        collectMetrics(jproject, jclass);
        collectMetricsMax();
    }
    
    public ClassMetrics(String fqn, String name, int modifiers, String kindStr, String path, PackageMetrics mpackage) {
        super(fqn, name, fqn, modifiers);
        kind = JavaClass.Kind.valueOf(kindStr);
        packageMetrics = mpackage;
        this.path = path;
    }
    
    public JavaClass.Kind getKind() {
        return kind;
    }
    
    public String getPath() {
        return path;
    }
    
    public String getFullPath() {
        return packageMetrics.getProject().getPath() + File.separatorChar + path;
    }
    
    public boolean isClass() {
        return kind == JavaClass.Kind.J_CLASS;
    }
    
    public boolean isInterface() {
        return kind == JavaClass.Kind.J_INTERFACE;
    }
    
    public boolean isEnum() {
        return kind == JavaClass.Kind.J_ENUM;
    }
    
    public boolean isLambda() {
        return kind == JavaClass.Kind.J_LAMBDA;
    }
    
    public PackageMetrics getPackage() {
        return packageMetrics;
    }
    
    public String getPackageName() {
        return packageMetrics.getName();
    }
    
    public void setSuperClass(String name) {
        superClassName = name;
    }
    
    public String getSuperClassName() {
        return superClassName;
    }
    
    public void addSuperInterface(String name) {
        superInterfaceNames.add(name);
    }
    
    public List<String> getSuperInterfaceNames() {
        return superInterfaceNames;
    }
    
    public void addMethod(MethodMetrics mmethod) {
        if (!methods.contains(mmethod)) {
            methods.add(mmethod);
        }
    }
    
    public List<MethodMetrics> getMethods() {
        return methods;
    }
    
    public void sortMethods() {
        MethodMetrics.sort(methods);
    }
    
    public void addField(FieldMetrics mfield) {
        if (!fields.contains(mfield)) {
            fields.add(mfield);
        }
    }
    
    public List<FieldMetrics> getFields() {
        return fields;
    }
    
    public void sortFields() {
        FieldMetrics.sort(fields);
    }
    
    public void addAfferentClass(String name) {
        if (!afferentClassNames.contains(name)) {
            afferentClassNames.add(name);
        }
    }
    
    public List<String> getAfferentClasses() {
        return afferentClassNames;
    }
    
    public void addEfferentClass(String name) {
        if (!efferentClassNames.contains(name)) {
            efferentClassNames.add(name);
        }
    }
    
    public List<String> getEfferentClasses() {
        return efferentClassNames;
    }
    
    public String getSourceCode() {
        return super.getSourceCode(getFullPath());
    }
    
    protected void collectMetrics(JavaProject jproject, JavaClass jclass) {
        putMetricValue(LOC.Name, sum(LOC.Name));
        putMetricValue(NOST.Name, sum(NOST.Name));
        
        putMetricValue(NOMD.Name, new NOMD().calculate(jclass));
        putMetricValue(NOFD.Name, new NOFD().calculate(jclass));
        putMetricValue(NOMDFD.Name, new NOMDFD().calculate(jclass));
        putMetricValue(NOPMD.Name, new NOPMD().calculate(jclass));
        putMetricValue(NOPFD.Name, new NOPFD().calculate(jclass));
        
        putMetricValue(NOACL.Name, new NOACL().calculate(jclass));
        putMetricValue(NOECL.Name, new NOECL().calculate(jclass));
        putSumMetricValue(NOAMD.Name);
        putSumMetricValue(NOEMD.Name);
        putSumMetricValue(NOEFD.Name);
        
        putMetricValue(CBO.Name, new CBO().calculate(jclass));
        putMetricValue(DIT.Name, new DIT().calculate(jclass));
        putMetricValue(NOC.Name, new NOC().calculate(jclass));
        putMetricValue(RFC.Name, new RFC().calculate(jclass));
        putMetricValue(WMC.Name, new WMC().calculate(jclass));
        putMetricValue(LCOM.Name, new LCOM().calculate(jclass));
        
        putSumMetricValue(ATFD.Name);
        putMetricValue(TCC.Name, new TCC().calculate(jproject, jclass));
    }
    
    protected void collectMetricsMax() {
        putMaxMetricValue(LOC.Name);
        putMaxMetricValue(NOST.Name);
        
        putMaxMetricValue(NOAMD.Name);
        putMaxMetricValue(NOEMD.Name);
        putMaxMetricValue(NOEFD.Name);
        
        putMaxMetricValue(CYCLO.Name);
        putMaxMetricValue(NEST.Name);
        putMaxMetricValue(LVAR.Name);
        putMaxMetricValue(PAR.Name);
        
        putMaxMetricValue(ATFD.Name);
    }
    
    private void putSumMetricValue(String sort) {
        metricValues.put(sort, sum(sort));
    }
    
    private void putMaxMetricValue(String sort) {
        metricValues.put(Metric.MAX + sort, max(sort));
    }
    
    protected double sum(String sort) {
        double value = 0;
        for (MethodMetrics mmethod : methods) {
            value = value + mmethod.getMetricValue(sort);
        }
        for (FieldMetrics mfield : fields) {
            value = value + mfield.getMetricValue(sort);
        }
        return new BigDecimal(value).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
    
    protected Double max(String sort) {
        double value = 0;
        for (MethodMetrics mmethod : methods) {
            value = Math.max(value, mmethod.getMetricValue(sort));
        }
        for (FieldMetrics mfield : fields) {
            value = Math.max(value, mfield.getMetricValue(sort));
        }
        return new BigDecimal(value).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
    
    protected Double maxForMethods(String sort) {
        double value = 0;
        for (MethodMetrics mmethod : methods) {
            value = Math.max(value, mmethod.getMetricValue(sort));
        }
        return new BigDecimal(value).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
    
    public static List<String> getAccessedFields(JavaProject jproject, JavaClass jclass, JavaMethod jmethod) {
        List<String> fieldNames = new ArrayList<String>();
        for (JavaField jf : jmethod.getAccessedFieldsInProject()) {
            if (jclass.equals(jf.getDeclaringClass())) {
                fieldNames.add(jf.getName());
            }
        }
        
        for (JavaMethod jm : jmethod.getCalledMethodsInProject()) {
            if (jclass.equals(jm.getDeclaringClass())) {
                JavaField jf = MethodMetrics.getFieldbyAccessor(jproject, jm);
                if (jf != null) {
                    fieldNames.add(jf.getName());
                }
            }
        }
        return fieldNames;
    }
    
    public void collectMetricsAfterXMLImport() {
        MethodMetrics.sort(methods);
        FieldMetrics.sort(fields);
        sortNames(afferentClassNames);
        sortNames(efferentClassNames);
    }
    
    public static void sort(List<ClassMetrics> classes) {
        Collections.sort(classes, new Comparator<ClassMetrics>() {
            
            @Override
            public int compare(ClassMetrics mclass1, ClassMetrics mclass2) {
                return mclass1.getQualifiedName().compareTo(mclass2.getQualifiedName());
            }
        });
    }
}
