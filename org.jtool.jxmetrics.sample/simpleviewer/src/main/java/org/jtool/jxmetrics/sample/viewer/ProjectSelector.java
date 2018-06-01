/*
 *  Copyright 2018
 *  Software Science and Technology Lab.
 *  Department of Computer Science, Ritsumeikan University
 */

package org.jtool.jxmetrics.sample.viewer;

import org.jtool.jxmetrics.core.ClassMetrics;
import org.jtool.jxmetrics.core.Metrics;
import org.jtool.jxmetrics.core.MetricsManager;
import org.jtool.jxmetrics.core.ProjectMetrics;
import org.jtool.jxmetrics.core.UnsupportedMetricsException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.io.File;

/**
 * Selects a project of interest.
 * @author Katsuhisa Maruyama
 */
public class ProjectSelector extends Composite {
    private MetricsViewer viewer;
    private String target;
    
    private ProjectMetrics project;
    
    public ProjectSelector(MetricsViewer viewer, String target) {
        super(viewer.getShell(), SWT.BORDER);
        this.viewer = viewer;
        this.target = target;
        
        Composite composite = createRepositoryControl(this);
        
        setLayout(new FormLayout());
        FormData data = new FormData();
        data.top = new FormAttachment(0, 0);
        data.left = new FormAttachment(0, 0);
        data.right = new FormAttachment(100, 0);
        data.bottom = new FormAttachment(100, 0);
        composite.setLayoutData(data);
    }
    
    protected Composite createRepositoryControl(Composite parent) {
        Composite composite = new Composite(parent, SWT.NONE);
        
        GridLayout layout = new GridLayout();
        layout.numColumns = 3;
        composite.setLayout(layout);
        
        Button exportButton = new Button(composite, SWT.FLAT);
        exportButton.setText("Export");
        exportButton.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));
        
        Button openButton = new Button(composite, SWT.FLAT);
        openButton.setText("Open");
        openButton.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));
        
        Label locLabel = new Label(composite, SWT.NONE);
        locLabel.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING | GridData.FILL_HORIZONTAL));
        if (collectProjectMetrics(target)) {
            String[] titles = getTitles();
            locLabel.setText(target);
            viewer.showTitles(titles);
            viewer.showData(extract());
        } else {
            locLabel.setText("Not specified");
        }
        
        openButton.addSelectionListener(new SelectionListener() {
            
            public void widgetDefaultSelected(SelectionEvent evt) {
            }
            
            public void widgetSelected(SelectionEvent evt) {
                FileDialog dialog = new FileDialog(parent.getShell(), SWT.OPEN);
                dialog.setFilterPath(target);
                String filename = dialog.open();
                if (collectProjectMetrics(filename)) {
                    String[] titles = getTitles();
                    target = filename;
                    locLabel.setText(target);
                    viewer.showTitles(titles);
                    viewer.showData(extract());
                } else {
                    locLabel.setText("Not specified");
                }
            }
        });
        
        exportButton.addSelectionListener(new SelectionListener() {
            
            public void widgetDefaultSelected(SelectionEvent evt) {
            }
            
            public void widgetSelected(SelectionEvent evt) {
                FileDialog dialog = new FileDialog(parent.getShell(), SWT.SAVE);
                int index = target.lastIndexOf(File.separatorChar);
                dialog.setFilterPath(target.substring(0, index));
                String name = dialog.open();
                if (name != null) {
                    viewer.exportData(name);
                }
            }
        });
        
        return composite;
    }
    
    private boolean collectProjectMetrics(String filename) {
        if (filename == null || filename.length() <= 0) {
            return false;
        }
        
        MetricsManager metricsManager = new MetricsManager();
        File file = new File(filename);
        if (file != null && file.isFile()) {
            ProjectMetrics mproject = metricsManager.importXML(file);
            if (mproject != null) {
                project = mproject;
                return true;
            }
        }
        return false;
    }
    
    private String[] getTitles() {
        List<String> names = Metrics.getAllMetricNames();
        return names.toArray(new String[0]);
    }
    
    public Map<String, double[]> extract() {
        Map<String, double[]> data = new HashMap<String, double[]>();
        List<String> names = Metrics.getAllMetricNames();
        for (ClassMetrics mclass : project.getClasses()) {
            double[] values = new double[names.size()];
            for (int i = 0; i < names.size(); i++) {
                try {
                    String name = names.get(i);
                    values[i] = mclass.getMetricValueWithException(name);
                } catch (UnsupportedMetricsException e) { /* empty */ }
            }
            String fqn = mclass.getQualifiedName();
            data.put(fqn, values);
        }
        return data;
    }
}
