/*
 *  Copyright 2018
 *  Software Science and Technology Lab.
 *  Department of Computer Science, Ritsumeikan University
 */

package org.jtool.jxmetrics.sample.viewer;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import java.util.Map;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * A viewer that displays the transition of metric values of a specific metric measurement.
 * @author Katsuhisa Maruyama
 */
public class MetricsTransitionViewer {
    
    private Shell shell;
    private Display display;
    
    private MetricsSelector metricsSelector;
    private MetricsTransitionTable metricsTable;
    
    public MetricsTransitionViewer() {
    }
    
    public Shell getShell() {
        return shell;
    }
    
    private void createViews(String target) {
        metricsTable = new MetricsTransitionTable(this);
        metricsSelector = new MetricsSelector(this, target);
        
        FormData ldata = new FormData();
        ldata.top = new FormAttachment(0, 0);
        ldata.left = new FormAttachment(0, 0);
        ldata.right = new FormAttachment(100, 0);
        metricsSelector.setLayoutData(ldata);
        
        FormData hdata = new FormData();
        hdata.top = new FormAttachment(metricsSelector, 0);
        hdata.left = new FormAttachment(0, 0);
        hdata.right = new FormAttachment(100, 0);
        hdata.bottom = new FormAttachment(100, 0);
        metricsTable.setLayoutData(hdata);
    }
    
    public void showTitles(String[] titles) {
        metricsTable.showTitles(titles);
    }
    
    public void showData(Map<String, double[]> data) {
        metricsTable.showData(data);
    }
    
    public void exportData(String filename) {
        String str = metricsTable.exportData();
        try {
            File file = new File(filename);
            BufferedWriter bwriter = new BufferedWriter(new FileWriter(file));
            bwriter.write(str);
            bwriter.flush();
            bwriter.close();
        } catch (IOException e) {
            System.err.println("Export error occurred: " + e.getMessage() + ".");
        }
    }
    
    protected void run(String name, int width, int height) {
        run(name, width, height, null);
    }
    
    protected void run(String name, int width, int height, String target) {
        display = new Display();
        shell = new Shell(display);
        shell.setSize(width, height);
        shell.setText(name);
        shell.setLayout(new FormLayout());
        
        createViews(target);
        
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        display.dispose();
    }
    
    public static void main(String[] args) {
        MetricsTransitionViewer viewer = new MetricsTransitionViewer();
        viewer.run("Metrics Vierwer", 1400, 1000);
    }
}
