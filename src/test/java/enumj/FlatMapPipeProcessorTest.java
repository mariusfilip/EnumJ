/*
 * The MIT License
 *
 * Copyright 2015 Marius Filip.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package enumj;

import java.util.Iterator;
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
public class FlatMapPipeProcessorTest {

    public FlatMapPipeProcessorTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        processor0 = new FlatMapPipeProcessor<>(x -> Enumerator.empty());
        processor = new FlatMapPipeProcessor<>(x -> 
                Enumerator.on(x, x+1, x+2));
    }

    FlatMapPipeProcessor<Integer,Integer> processor0;
    FlatMapPipeProcessor<Integer,Integer> processor;

    @After
    public void tearDown() {
        processor0 = null;
        processor = null;
    }

    /**
     * Test of needsValue method, of class FlatMapPipeProcessor.
     */
    @Test
    public void testNeedsValue() {
        System.out.println("needsValue");
        assertTrue(processor.needsValue());
    }

    /**
     * Test of process method, of class FlatMapPipeProcessor.
     */
    @Test
    public void testProcess() {
        System.out.println("process");
        processor0.process(0);
        assertFalse(processor0.hasValue());
        processor.process(1);
        assertTrue(processor.hasValue());
    }

    /**
     * Test of hasValue method, of class FlatMapPipeProcessor.
     */
    @Test
    public void testHasValue() {
        System.out.println("hasValue");
        assertFalse(processor0.hasValue());
        assertFalse(processor.hasValue());
    }

    /**
     * Test of getValue method, of class FlatMapPipeProcessor.
     */
    @Test
    public void testGetValue() {
        System.out.println("getValue");
        processor.process(1);
        assertEquals(1, processor.getValue().intValue());
        assertEquals(2, processor.getValue().intValue());
        assertEquals(3, processor.getValue().intValue());
    }

    /**
     * Test of nextElementOnNoValue method, of class FlatMapPipeProcessor.
     */
    @Test
    public void testNextOnNoValue() {
        System.out.println("nextOnNoValue");
        assertTrue(processor.nextElementOnNoValue());
    }

    /**
     * Test of hasNextNeedsValue method, of class FlatMapPipeProcessor.
     */
    @Test
    public void testHasNextNeedsValue() {
        System.out.println("hasNextNeedsValue");
        assertTrue(processor.hasNextNeedsValue());
    }
}
