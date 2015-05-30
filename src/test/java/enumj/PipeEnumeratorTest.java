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

public class PipeEnumeratorTest {

    public PipeEnumeratorTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        source = Enumerator.rangeInt(-100, 100);
        pipe = PipeEnumerator.of(source);
    }

    Enumerator<Integer> source;
    PipeEnumerator<Integer> pipe;

    @After
    public void tearDown() {
        source = null;
        pipe = null;
    }

    /**
     * Test of of method, of class PipeEnumerator.
     */
    @Test
    public void testOf() {
        System.out.println("of");
        assertNotNull(pipe);
    }

    /**
     * Test of enqueueProcessor method, of class PipeEnumerator.
     */
    @Test
    public void testEnqueue_AbstractPipeProcessor() {
        System.out.println("enqueue");
        assertNotNull(pipe.map(x -> x));
    }

    /**
     * Test of enqueueProcessor method, of class PipeEnumerator.
     */
    @Test(expected=IllegalStateException.class)
    public void testEnqueue_PipeMultiProcessor() {
        System.out.println("enqueue");
        assertNotNull(pipe.flatMap(x -> Enumerator.on(x)));
        final ThrowingPipeEnumerator<Integer> ten =
                new ThrowingPipeEnumerator<>(pipe);
        assertNotNull(ten.flatMap(x -> Enumerator.on(x)));
    }

    class ThrowingPipeEnumerator<E> extends PipeEnumerator<E> {
        public ThrowingPipeEnumerator(Iterator<E> source) {
            super(source);
        }
        @Override
        protected <X> void multiPipelineAddLast(
            PipeMultiProcessor<? super E, ? extends X> processor) {
            throw new IllegalStateException();
        }
    }

    @Test
    public void testDequeue() {
        System.out.println("dequeue");
        assertTrue(pipe.map(x -> -x)
                       .filter(x -> true)
                       .map(x -> -x)
                       .elementsEqual(Enumerator.rangeInt(-100, 100)));
    }

    @Test
    public void testTryPipelineIn() {
        System.out.println("tryPipelineIn");
        source = Enumerator.on(-2, -1, 0, 1, 2);
        pipe = PipeEnumerator.of(source);
        assertTrue(pipe.filter(x -> true)
                       .map(x -> x)
                       .elementsEqual(Enumerator.rangeInt(-2, 3)));
    }

    @Test
    public void testTryPipelineOut() {
        System.out.println("tryPipelineOut");
        source = Enumerator.on(1, 3, 5, 7, 9);
        pipe = (PipeEnumerator<Integer>)PipeEnumerator.of(source)
                .flatMap(x -> Enumerator.on(x, x+1))
                .flatMap(x -> Enumerator.on(x))
                .concat(Enumerator.on(11, 12));
        assertTrue(pipe.elementsEqual(
                Enumerator.on(1,2,3,4,5,6,7,8,9,10,11,12)));
    }

    /**
     * Test of pipelineAddLast method, of class PipeEnumerator.
     */
    @Test
    public void testPipelineAddLast() {
        System.out.println("pipelineAddLast");
    }

    /**
     * Test of multiPipelineAddLast method, of class PipeEnumerator.
     */
    @Test
    public void testMultiPipelineAddLast() {
        System.out.println("multiPipelineAddLast");
    }

    /**
     * Test of internalHasNext method, of class PipeEnumerator.
     */
    @Test
    public void testInternalHasNext() {
        System.out.println("internalHasNext");
    }

    /**
     * Test of internalNext method, of class PipeEnumerator.
     */
    @Test
    public void testInternalNext() {
        System.out.println("internalNext");
    }

    /**
     * Test of cleanup method, of class PipeEnumerator.
     */
    @Test
    public void testCleanup() {
        System.out.println("cleanup");
    }

    /**
     * Test of straightHasNext method, of class PipeEnumerator.
     */
    @Test
    public void testStraightHasNext() {
        System.out.println("straightHasNext");
    }

    /**
     * Test of tryGetNext method, of class PipeEnumerator.
     */
    @Test
    public void testTryGetNext() {
        System.out.println("tryGetNext");
    }

    /**
     * Test of concat method, of class PipeEnumerator.
     */
    @Test
    public void testConcat() {
        System.out.println("concat");
    }

    /**
     * Test of filter method, of class PipeEnumerator.
     */
    @Test
    public void testFilter() {
        System.out.println("filter");
    }

    /**
     * Test of flatMap method, of class PipeEnumerator.
     */
    @Test
    public void testFlatMap() {
        System.out.println("flatMap");
    }

    /**
     * Test of map method, of class PipeEnumerator.
     */
    @Test
    public void testMap() {
        System.out.println("map");
    }

    /**
     * Test of takeWhile method, of class PipeEnumerator.
     */
    @Test
    public void testTakeWhile() {
    }

    /**
     * Test of zipAll method, of class PipeEnumerator.
     */
    @Test
    public void testZipAll_Iterator_IteratorArr() {
        System.out.println("zipAll");
    }

    /**
     * Test of zipAll method, of class PipeEnumerator.
     */
    @Test
    public void testZipAll_Iterator_List() {
        System.out.println("zipAll");
    }    
}
