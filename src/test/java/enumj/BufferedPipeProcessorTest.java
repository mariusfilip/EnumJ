/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package enumj;

import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Marius Filip
 */
public class BufferedPipeProcessorTest {

    public BufferedPipeProcessorTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        processor = new BufferedPipeProcessor<>(minSize, maxSize);
    }
    private int minSize = 10;
    private int maxSize = 20;
    private BufferedPipeProcessor<String> processor;

    @After
    public void tearDown() {
        processor = null;
    }

    /**
     * Test of process method, of class BufferedPipeProcessor.
     */
    @Test
    public void testProcess() {
        System.out.println("process");
        for(int i=0; i<maxSize*10; ++i) {
            processor.process(Integer.toString(i));
        }
    }

    /**
     * Test of hasValue method, of class BufferedPipeProcessor.
     */
    @Test
    public void testHasValue() {
        System.out.println("hasValue");
        for(int i=0; i<minSize-1; ++i) {
            processor.process(Integer.toString(i));
            assertTrue(!processor.hasValue());
        }
        for(int i=minSize-1; i<maxSize; ++i) {
            processor.process(Integer.toString(i));
            assertTrue(processor.hasValue());
        }
        for(int i=maxSize; i<10*maxSize; ++i) {
            processor.process(Integer.toString(i));
            assertTrue(processor.hasValue());
        }
    }

    /**
     * Test of getValue method, of class BufferedPipeProcessor.
     */
    @Test
    public void testGetValue() {
        System.out.println("getValue");
        for(int i=0; i<minSize-1; ++i) {
            processor.process(Integer.toString(i));
        }
        for(int i=minSize-1; i<maxSize; ++i) {
            processor.process(Integer.toString(i));
            assertEquals(i+1, processor.getValue().size());
        }
        for(int i=maxSize; i<10*maxSize; ++i) {
            processor.process(Integer.toString(i));
            assertEquals(maxSize, processor.getValue().size());
        }
    }

    /**
     * Test of getValue method, of class BufferedPipeProcessor.
     */
    @Test
    public void testGetValues() {
        System.out.println("getValue elements");
        for(int i=0; i<minSize-1; ++i) {
            processor.process(Integer.toString(i));
        }
        for(int i=minSize-1; i<maxSize; ++i) {
            processor.process(Integer.toString(i));
            for(int j=0; j<=i; ++j) {
                assertEquals(Integer.toString(j),
                             processor.getValue().get(j));
            }
        }
        for(int i=maxSize; i<10*maxSize; ++i) {
            processor.process(Integer.toString(i));
            for(int j=0; j<maxSize; ++j) {
                assertEquals(Integer.toString(j+i-maxSize+1),
                             processor.getValue().get(j));
            }
        }
    }

    /**
     * Test of continueOnNoValue method, of class BufferedPipeProcessor.
     */
    @Test
    public void testContinueOnNoValue() {
        System.out.println("continueOnNoValue");
        for(int i=0; i<10*maxSize; ++i) {
            processor.process(Integer.toString(i));
            assertTrue(processor.continueOnNoValue());
        }
    }    
}
