/*
 *  Copyright 2018
 *  Software Science and Technology Lab.
 *  Department of Computer Science, Ritsumeikan University
 */

package org.jtool.eclipse.test;

import org.jtool.jxmetrics.standalone.MetricsCalculator;
import org.junit.Test;

/**
 * Tests a class that calculates.
 * @author Katsuhisa Maruyama
 */
public class MetricsCalculatorTest {
    
    private final String TEST_PROECT_DIR = "/Users/maru/Desktop/TestSamples/";
    
    @Test
    public void testSimple() {
        String target = TEST_PROECT_DIR + "Simple/";
        MetricsCalculator calculator = new MetricsCalculator("Simple", target);
        calculator.run();
    }
    
    @Test
    public void testTetris() {
        String target = TEST_PROECT_DIR + "Tetris/";
        MetricsCalculator calculator = new MetricsCalculator("Tetris", target);
        calculator.run();
    }
    
    @Test
    public void testDrawTool() {
        String target = TEST_PROECT_DIR + "DrawTool/";
        MetricsCalculator calculator = new MetricsCalculator("DrawTool", target);
        calculator.run();
    }
    
    @Test
    public void testCSSample() {
        String target = TEST_PROECT_DIR + "CS-Sample/";
        String classpath = TEST_PROECT_DIR + "CS-Sample/lib/*";
        MetricsCalculator calculator = new MetricsCalculator("CS-Sample", target, classpath);
        calculator.run();
    }
    
    @Test
    public void testFindbugs() {
        String target = TEST_PROECT_DIR + "findbugs/";
        String classpath= TEST_PROECT_DIR + "findbugs/lib/*";
        MetricsCalculator calculator = new MetricsCalculator("findbugs", target, classpath);
        calculator.run();
    }
    
    @Test
    public void testApacheAnt() {
        String target = TEST_PROECT_DIR + "apache-ant/";
        String classpath = TEST_PROECT_DIR + "apache-ant/lib/*";
        MetricsCalculator calculator = new MetricsCalculator("apache-ant", target, classpath);
        calculator.run();
    }
    
    public void notestJdk8() {
        String target = TEST_PROECT_DIR + "jdk1.8.0_131/";
        String classpath = TEST_PROECT_DIR + "jdk1.8.0_131/lib/*";
        MetricsCalculator calculator = new MetricsCalculator("jdk1.8.0_131", target, classpath);
        calculator.run();
    }
    
    public static void main(String[] args) {
        MetricsCalculatorTest tester = new MetricsCalculatorTest();
        tester.testSimple();
        tester.testTetris();
        tester.testDrawTool();
        tester.testCSSample();
        tester.testFindbugs();
        tester.testApacheAnt();
        // tester.notestJdk8();
    }
}
