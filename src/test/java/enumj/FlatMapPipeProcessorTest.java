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

import java.util.function.Function;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

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
        processor0 = new FlatMapPipeProcessor<>(
                x -> Enumerator.empty(), Value.GENERIC);
        processor1 = new FlatMapPipeProcessor<>(x -> null, Value.GENERIC);
        processor = new FlatMapPipeProcessor<>(
                x -> Enumerator.on(x, x+1, x+2),
                Value.GENERIC);
    }

    FlatMapPipeProcessor<Integer,Integer> processor0;
    FlatMapPipeProcessor<Integer,Integer> processor1;
    FlatMapPipeProcessor<Integer,Integer> processor;

    @After
    public void tearDown() {
        processor0 = null;
        processor = null;
    }

    @Test
    public void testNeedsValue() {
        System.out.println("needsValue");
        assertTrue(processor.needsValue());
    }

    @Test
    public void testProcessInputValue() {
        System.out.println("process");
        processor0.processInputValue(new InOut(0));
        assertFalse(processor0.hasOutputValue());
        processor1.processInputValue(new InOut(1));
        assertFalse(processor1.hasOutputValue());
        processor.processInputValue(new InOut(1));
        assertTrue(processor.hasOutputValue());
    }

    @Test
    public void testHasValue() {
        System.out.println("hasValue");
        assertFalse(processor0.hasOutputValue());
        assertFalse(processor1.hasOutputValue());
        assertFalse(processor.hasOutputValue());
    }

    @Test
    public void testGetValue() {
        System.out.println("getValue");
        processor.processInputValue(new InOut(1));
        final Out<Integer> out = new InOut();
        processor.getOutputValue(out);
        assertEquals(1, out.getInt());
        processor.getOutputValue(out);
        assertEquals(2, out.getInt());
        processor.getOutputValue(out);
        assertEquals(3, out.getInt());
    }

    @Test
    public void testNextOnNoValue() {
        System.out.println("nextOnNoValue");
        assertTrue(processor.nextOnSameSourceOnNoValue);
    }

    @Test
    public void testHasNextNeedsValue() {
        System.out.println("hasNextNeedsValue");
        assertTrue(processor.hasNextNeedsValue);
    }
}
