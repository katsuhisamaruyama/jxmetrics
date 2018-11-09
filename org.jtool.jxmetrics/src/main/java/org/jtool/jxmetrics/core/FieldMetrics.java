/*
 *  Copyright 2018
 *  Software Science and Technology Lab.
 *  Department of Computer Science, Ritsumeikan University
 */

package org.jtool.jxmetrics.core;

import org.jtool.jxmetrics.measurement.LOC;
import org.jtool.jxmetrics.measurement.NOST;
import org.jtool.eclipse.javamodel.JavaProject;
import org.jtool.eclipse.javamodel.JavaField;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;

/**
 * Stores metric information on a field.
 * 
 * @author Katsuhisa Maruyama
 */
public class FieldMetrics extends CommonMetrics {
    
    public static final String Id = "FieldMetrics";
    
    protected JavaField.Kind kind;
    protected ClassMetrics classMetrics;
    
    protected FieldMetrics(String fqn, String name, String type, int modifiers) {
        super(fqn, name, type, modifiers);
    }
    
    public FieldMetrics(JavaProject jproject, JavaField jfield, ClassMetrics mclass) {
        this(jfield.getQualifiedName(), jfield.getName(), jfield.getType(), jfield.getModifiers());
        
        kind = jfield.getKind();
        classMetrics = mclass;
        
        int start = jfield.getCodeRange().getStartPosition();
        int end = jfield.getCodeRange().getEndPosition();
        int upper = jfield.getCodeRange().getUpperLineNumber();
        int bottom = jfield.getCodeRange().getBottomLineNumber();
        setCodeProperties(start, end, upper, bottom);
        
        collectMetrics(jproject, jfield);
    }
    
    public FieldMetrics(String fqn, String name, String type, int modifiers, String kindStr, ClassMetrics mclass) {
        super(fqn, name, type, modifiers);
        
        this.kind = JavaField.Kind.valueOf(kindStr);
        classMetrics = mclass;
    }
    
    public ClassMetrics getDeclaringClass() {
        return classMetrics;
    }
    
    public String getDeclaringClassName() {
        return classMetrics.getQualifiedName();
    }
    
    public JavaField.Kind getKind() {
        return kind;
    }
    
    public boolean isField() {
        return kind == JavaField.Kind.J_FIELD;
    }
    
    public boolean isEnumConstant() {
        return kind == JavaField.Kind.J_ENUM_CONSTANT;
    }
    
    public String getSourceCode() {
        return super.getSourceCode(classMetrics.getFullPath());
    }
    
    protected void collectMetrics(JavaProject jproject, JavaField jfield) {
        putMetricValue(LOC.Name, new LOC().calculate(jfield));
        putMetricValue(NOST.Name,new NOST().calculate(jfield));
    }
    
    public static void sort(List<FieldMetrics> mfields) {
        Collections.sort(mfields, new Comparator<FieldMetrics>() {
            
            @Override
            public int compare(FieldMetrics mfield1, FieldMetrics mfield2) {
                return mfield1.getName().compareTo(mfield2.getName());
            }
        });
    }
}
