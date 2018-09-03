/*
 *  Copyright 2018
 *  Software Science and Technology Lab.
 *  Department of Computer Science, Ritsumeikan University
 */
 
package org.jtool.jxmetrics.plugin;

import org.jtool.eclipse.javamodel.JavaProject;
import org.jtool.eclipse.plugin.ModelBuilderPlugin;
import org.jtool.jxmetrics.core.MetricsManager;
import org.jtool.jxmetrics.core.ProjectMetrics;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.ui.handlers.HandlerUtil;

/**
 * Performs an action for displaying a code forest view by newly building java models of elements within a project.
 * 
 * @author Katsuhisa Maruyama
 */
public class ExportAction extends AbstractHandler {
    
    public ExportAction() {
    }
    
    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        ISelection selection = HandlerUtil.getActiveMenuSelection(event);
        if (selection instanceof IStructuredSelection) {
            IStructuredSelection structured = (IStructuredSelection)selection;
            Object elem = structured.getFirstElement();
            
            IJavaProject project = null;
            if (elem instanceof IJavaProject) {
                project = (IJavaProject)elem;
            } else if (elem instanceof IProject) {
                project = (IJavaProject)JavaCore.create((IProject)elem);
            }
            
            if (project != null) {
                ModelBuilderPlugin modelBuilder = new ModelBuilderPlugin();
                JavaProject jproject = modelBuilder.build(project);
                MetricsManager manager = new MetricsManager();
                ProjectMetrics mproject = manager.calculate(jproject);
                manager.exportXML(mproject);
            }
        }
        return null;
    }
}
