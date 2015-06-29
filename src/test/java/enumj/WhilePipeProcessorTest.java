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
public class WhilePipeProcessorTest {

    public WhilePipeProcessorTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        processor = new WhilePipeProcessor<>(i -> i > 0);
    }

    WhilePipeProcessor<Integer> processor;

    @After
    public void tearDown() {
        processor = null;
    }

    /**
     * Test of process method, of class WhilePipeProcessor.
     */
    @Test
    public void testProcess() {
        System.out.println("process");
        processor.process(1);
        assertTrue(processor.hasValue());
    }

    /**
     * Test of hasValue method, of class WhilePipeProcessor.
     */
    @Test
    public void testHasValue() {
        System.out.println("hasValue");
        processor.process(-1);
        assertFalse(processor.hasValue());
        assertFalse(processor.hasValue());
    }

    /**
     * Test of getValue method, of class WhilePipeProcessor.
     */
    @Test
    public void testGetValue() {
        System.out.println("getValue");
        processor.process(1);
        assertEquals(1, processor.getValue().intValue());
    }

    /**
     * Test of nextElementOnNoValue method, of class WhilePipeProcessor.
     */
    @Test
    public void testNextOnNoValue() {
        System.out.println("nextOnNoValue");
        assertFalse(processor.nextElementOnNoValue());
    }

    /**
     * Test of hasNextNeedsValue method, of class WhilePipeProcessor.
     */
    @Test
    public void testHasNextNeedsValue() {
        System.out.println("hasNextNeedsValue");
        assertTrue(processor.hasNextNeedsValue());
    }    
}
