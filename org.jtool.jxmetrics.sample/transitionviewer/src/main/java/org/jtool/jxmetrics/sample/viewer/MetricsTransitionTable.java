/*
 *  Copyright 2018
 *  Software Science and Technology Lab.
 *  Department of Computer Science, Ritsumeikan University
 */

package org.jtool.jxmetrics.sample.viewer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.TableColumn;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Collections;
import java.util.Comparator;

/**
 * Displays metric values of a specific metric measurement.
 * 
 * @author Katsuhisa Maruyama
 */
public class MetricsTransitionTable extends Composite {
    
    private Table table;
    
    public MetricsTransitionTable(MetricsTransitionViewer viewer) {
        super(viewer.getShell(), SWT.BORDER);
        
        Composite composite = createTable(this);
        
        setLayout(new FormLayout());
        FormData data = new FormData();
        data.top = new FormAttachment(0, 0);
        data.bottom = new FormAttachment(100, 0);
        data.left = new FormAttachment(0, 0);
        data.right = new FormAttachment(100, 0);
        composite.setLayoutData(data);
    }
    
    private Composite createTable(Composite parent) {
        table = new Table(parent, SWT.BORDER | SWT.SINGLE | SWT.VIRTUAL | SWT.H_SCROLL | SWT.V_SCROLL);
        table.setLinesVisible(true);
        table.setHeaderVisible(true);
        
        TableColumn nameColumn = new TableColumn(table, SWT.LEFT);
        nameColumn.setText("class name");
        nameColumn.setWidth(300);
        nameColumn.setResizable(true);
        return table;
    }
    
    @Override
    public void dispose() {
        table.dispose();
    }
    
    public void showTitles(String[] titles) {
        table.setRedraw(false);
        while (table.getColumnCount() > 1) {
            table.getColumns()[1].dispose();
        }
        table.setRedraw(true);
        
        table.removeAll();
        for (int i = 0; i < titles.length; i++) {
            TableColumn nameColumn = new TableColumn(table, SWT.RIGHT);
            nameColumn.setText(titles[i]);
            nameColumn.setWidth(70);
            nameColumn.setResizable(true);
        }
    }
    
    private List<OutputData> outputData;
    
    public void showData(Map<String, double[]> data) {
        table.removeAll();
        
        outputData = new ArrayList<OutputData>();
        for (String fqn : data.keySet()) {
            outputData.add(new OutputData(fqn, data.get(fqn)));
        }
        sort(outputData);
        
        for (OutputData odata : outputData) {
            TableItem item = new TableItem(table, SWT.NONE);
            item.setText(0, odata.fqn);
            for (int i = 0; i < odata.values.length; i++) {
                item.setText(i + 1, String.valueOf(odata.values[i]));
            }
        }
        table.update();
    }
    
    public String exportData() {
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < table.getColumnCount(); i++) {
            buf.append(table.getColumn(i).getText() + ", ");
        }
        buf.append("\n");
        for (OutputData odata : outputData) {
            buf.append(odata.fqn + ", ");
            for (int i = 0; i < odata.values.length; i++) {
                buf.append(String.valueOf(odata.values[i]) + ", ");
            }
            buf.append("\n");
        }
        return buf.toString();
    }
    
    class OutputData {
        String fqn;
        double[] values;
        
        OutputData(String fqn, double[] values) {
            this.fqn = fqn;
            this.values = values;
        }
    }
    
    public void sort(List<OutputData> data) {
        Collections.sort(data, new Comparator<OutputData>() {
            
            public int compare(OutputData data1, OutputData data2) {
                return data1.fqn.compareTo(data2.fqn);
            }
        });
    }
}
