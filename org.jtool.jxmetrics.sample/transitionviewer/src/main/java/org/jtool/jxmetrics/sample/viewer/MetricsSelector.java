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
import org.jtool.jxmetrics.measurement.IMetric;
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
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Combo;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.io.File;

/**
 * Selects a metric measurement of interest.
 * 
 * @author Katsuhisa Maruyama
 */
public class MetricsSelector extends Composite {
    private MetricsTransitionViewer viewer;
    private String target;
    
    private List<ProjectMetrics> projects = new ArrayList<ProjectMetrics>();
    
    public MetricsSelector(MetricsTransitionViewer viewer, String target) {
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
        layout.numColumns = 4;
        composite.setLayout(layout);
        
        List<String> names = Metrics.getAllMetricNames();
        names.add(0, " - ");
        String[] items = names.toArray(new String[0]);
        
        Combo metricSel = new Combo(composite, SWT.FLAT);
        metricSel.setItems(items);
        metricSel.select(0);
        metricSel.setEnabled(false);
        
        metricSel.addSelectionListener(new SelectionListener() {
            
            public void widgetDefaultSelected(SelectionEvent evt) {
            }
            
            public void widgetSelected(SelectionEvent evt) {
                Combo combo = (Combo)evt.getSource();
                String name = combo.getItem(combo.getSelectionIndex());
                Map<String, double[]> data = extract(name);
                viewer.showData(data);
            }
        });
        
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
            metricSel.select(0);
            metricSel.setEnabled(true);
        } else {
            locLabel.setText("Not specified");
        }
        
        openButton.addSelectionListener(new SelectionListener() {
            
            public void widgetDefaultSelected(SelectionEvent evt) {
            }
            
            public void widgetSelected(SelectionEvent evt) {
                DirectoryDialog dialog = new DirectoryDialog(parent.getShell(), SWT.OPEN);
                dialog.setFilterPath(target);
                String dirname = dialog.open();
                if (collectProjectMetrics(dirname)) {
                    String[] titles = getTitles();
                    target = dirname;
                    locLabel.setText(target);
                    viewer.showTitles(titles);
                    metricSel.select(0);
                    metricSel.setEnabled(true);
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
                dialog.setFilterPath(target);
                String name = dialog.open();
                if (name != null) {
                    viewer.exportData(name);
                }
            }
        });
        
        return composite;
    }
    
    private boolean collectProjectMetrics(String dirname) {
        if (dirname == null || dirname.length() <= 0) {
            return false;
        }
        
        projects.clear();
        MetricsManager metricsManager = new MetricsManager();
        File dir = new File(dirname);
        if (dir != null && dir.isDirectory()) {
            for (File file : dir.listFiles()) {
                ProjectMetrics mproject = metricsManager.importXML(file);
                if (mproject != null) {
                    projects.add(mproject);
                }
            }
            ProjectMetrics.sort(projects);
            return true;
        }
        return false;
    }
    
    private String[] getTitles() {
        String[] titles = new String[projects.size()];
        for (int i = 0; i < projects.size(); i++) {
            titles[i] = String.valueOf(projects.get(i).getName());
        }
        return titles;
    }
    
    public Map<String, double[]> extract(String name) {
        IMetric metric = Metrics.getMetric(name);
        if (metric != null) {
            return extract(metric);
        }
        return new HashMap<String, double[]>();
    }
    
    private Map<String, double[]> extract(IMetric metric) {
        Map<String, double[]> data = new HashMap<String, double[]>();
        for (int i = 0; i < projects.size(); i++) {
            ProjectMetrics mproject = projects.get(i);
            for (ClassMetrics mclass : mproject.getClasses()) {
                String fqn = mclass.getQualifiedName();
                if (mclass.isPublic() && fqn.indexOf('$') == -1) {
                    try {
                        double value = metric.valueOf(mclass);
                        
                        double[] values = data.get(fqn);
                        if (values == null) {
                            values = new double[projects.size()];
                            data.put(fqn, values);
                        }
                        values[i] = value;
                    } catch (UnsupportedMetricsException e) { /* empty */ }
                }
            }
        }
        return data;
    }
}
