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
public class AbstractPipeProcessorTest {
    
    public AbstractPipeProcessorTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        processor = new AbstractPipeProcessorImpl<>();
    }
    AbstractPipeProcessorImpl<String> processor;
    
    @After
    public void tearDown() {
        processor = null;
    }

    /**
     * Test of getNext method, of class AbstractPipeProcessor.
     */
    @Test
    public void testGetNext() {
        System.out.println("getNext");
        assertNull(processor.getNext());
    }

    /**
     * Test of setNext method, of class AbstractPipeProcessor.
     */
    @Test
    public void testSetNext() {
        System.out.println("setNext");
        processor.setNext(new AbstractPipeProcessorImpl());
        assertNotNull(processor.getNext());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetNextTwice() {
        System.out.println("setNextTwice");
        processor.setNext(new AbstractPipeProcessorImpl());
        processor.setNext(new AbstractPipeProcessorImpl());
    }

    /**
     * Test of getSource method, of class AbstractPipeProcessor.
     */
    @Test
    public void testGetSource() {
        System.out.println("getSource");
        assertNull(processor.getSource());
    }

    /**
     * Test of setSource method, of class AbstractPipeProcessor.
     */
    @Test
    public void testSetSource() {
        System.out.println("setSource");
        processor.setSource(new PipeSource(Enumerator.empty()));
        assertNotNull(processor.getSource());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetSourceTwice() {
        System.out.println("setSourceTwice");
        processor.setSource(new PipeSource(Enumerator.empty()));
        processor.setSource(new PipeSource(Enumerator.empty()));
    }
    
    /**
     * Test of pushFrontMap method, of class AbstractPipeProcessor.
     */
    @Test
    public void testPushFrontMap() {
        System.out.println("pushFrontMap");
        assertFalse(processor.pushFrontMap(x -> x));
    }

    /**
     * Test of enqueueMap method, of class AbstractPipeProcessor.
     */
    @Test
    public void testEnqueueMap() {
        System.out.println("enqueueMap");
        assertFalse(processor.enqueueMap(x -> x));
    }

    /**
     * Test of processInputValue method, of class AbstractPipeProcessor.
     */
    @Test
    public void testProcessInputValue() {
        System.out.println("processInputValue");
        processor.processInputValue("titi");
        assertTrue(processor.hasOutputValue());
    }

    /**
     * Test of hasOutputValue method, of class AbstractPipeProcessor.
     */
    @Test
    public void testHasOutputValue() {
        System.out.println("hasOutputValue");
        assertFalse(processor.hasOutputValue());
    }

    /**
     * Test of getOutputValue method, of class AbstractPipeProcessor.
     */
    @Test
    public void testGetOutputValue() {
        System.out.println("getOutputValue");
        processor.processInputValue("titi");
        assertEquals("titi", processor.getOutputValue());
    }

    /**
     * Test of retrieveOutputValue method, of class AbstractPipeProcessor.
     */
    @Test
    public void testRetrieveOutputValue() {
        System.out.println("retrieveOutputValue");
        processor.processInputValue("titi");
        assertEquals("titi", processor.retrieveOutputValue());
    }

    /**
     * Test of clearOutputValue method, of class AbstractPipeProcessor.
     */
    @Test
    public void testClearOutputValue() {
        System.out.println("clearOutputValue");
        processor.processInputValue("titi");
        processor.clearOutputValue();
        assertFalse(processor.hasOutputValue());
    }

    /**
     * Test of isInactive method, of class AbstractPipeProcessor.
     */
    @Test
    public void testIsInactive() {
        System.out.println("isInactive");
        assertFalse(processor.isInactive());
    }

    public class AbstractPipeProcessorImpl<T>
                 extends AbstractPipeProcessor<T,T> {
        
        private T value;

        public AbstractPipeProcessorImpl() {
            super(false, false);
        }

        public void processInputValue(T value) {
            this.value = value;
        }

        public boolean hasOutputValue() {
            return value != null;
        }

        public T retrieveOutputValue() {
            return value;
        }

        public void clearOutputValue() {
            value = null;
        }

        public boolean isInactive() {
            return false;
        }
    }
}
