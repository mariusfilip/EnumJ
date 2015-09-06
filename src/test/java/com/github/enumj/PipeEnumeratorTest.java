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
package com.github.enumj;

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

    @Test
    public void testOf() {
        System.out.println("of");
        assertNotNull(pipe);
    }

    @Test
    public void testEnqueueProcessor_AbstractPipeProcessor() {
        System.out.println("enqueueProcessor");
        assertNotNull(pipe.map(x -> x));
    }

    @Test(expected=IllegalStateException.class)
    public void testEnqueueProcessor_PipeMultiProcessor() {
        System.out.println("enqueueProcessor");
        assertNotNull(pipe.flatMap(x -> Enumerator.on(x)));
        final ThrowingMultiLastPipeEnumerator<Integer> throwEn =
                new ThrowingMultiLastPipeEnumerator<>(pipe);
        assertNotNull(throwEn.flatMap(x -> Enumerator.on(x)));
    }

    @Test(expected=IllegalStateException.class)
    public void testPushFrontProcessor_AbstractPipeProcessor() {
        System.out.println("pushFrontProcessor");
        assertNotNull(pipe.flatMap(x -> Enumerator.on(x)));
        final ThrowingFirstPipeEnumerator<Integer> throwEn =
                new ThrowingFirstPipeEnumerator<>(pipe);
        assertNotNull(throwEn.reversedFlatMap(x -> Enumerator.on(x)));
    }

    @Test(expected=IllegalStateException.class)
    public void testPushFrontProcessor_PipeMultiProcessor() {
        System.out.println("pushFrontProcessor");
        assertNotNull(pipe.flatMap(x -> Enumerator.on(x)));
        final ThrowingMultiFirstPipeEnumerator<Integer> throwEn =
                new ThrowingMultiFirstPipeEnumerator<>(pipe);
        assertNotNull(throwEn.reversedFlatMap(x -> Enumerator.on(x)));
    }

    class ThrowingMultiLastPipeEnumerator<E> extends PipeEnumerator<E> {
        public ThrowingMultiLastPipeEnumerator(Enumerator<E> source) {
            super(source);
        }
        @Override
        protected <X> void multiPipelineAddLast(
            AbstractPipeMultiProcessor<? super E, ? extends X> processor) {
            throw new IllegalStateException();
        }
    }

    class ThrowingFirstPipeEnumerator<E> extends PipeEnumerator<E> {
        public ThrowingFirstPipeEnumerator(Enumerator<E> source) {
            super(source);
        }
        @Override
        protected <X> void safePipelineAddFirst(
            AbstractPipeProcessor<? super X, ?> processor) {
            throw new IllegalStateException();
        }
    }

    class ThrowingMultiFirstPipeEnumerator<E> extends PipeEnumerator<E> {
        public ThrowingMultiFirstPipeEnumerator(Enumerator<E> source) {
            super(source);
        }
        @Override
        protected <X> void multiPipelineAddFirst(
            AbstractPipeMultiProcessor<? super X, ?> processor) {
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

    class FailingMultiPipeProcessor<In,Out>
          extends AbstractPipeMultiProcessor<In,Out> {
        private Out value;
        private boolean inactive;

        FailingMultiPipeProcessor() {
            super(true,true);
        }

        @Override
        public boolean needsValue() {
            return value == null;
        }
        @Override
        public void processInputValue(In value) {
            this.value = (Out)value;
        }
        @Override
        public boolean hasOutputValue() {
            return value != null;
        }
        @Override
        protected Out retrieveOutputValue() {
            return value;
        }
        @Override
        protected void clearOutputValue() {
            value = null;
            inactive = true;
        }
        @Override
        public boolean isInactive() {
            return inactive;
        }
    }

    class PipeEnumeratorWithFailingMulti extends PipeEnumerator<Integer> {

        public PipeEnumeratorWithFailingMulti() {
            super(Enumerator.on(1, 2, 3));
            this.enqueueProcessor(new FailingMultiPipeProcessor());
        }
    }

    @Test
    public void testTryPipelineInWithFailingMulti() {
        System.out.println("testTryPipelineInWithFailingMulti");
        final PipeEnumeratorWithFailingMulti pipe =
                new PipeEnumeratorWithFailingMulti();
        assertTrue(pipe.elementsEqual(Enumerator.on(1)));
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

    @Test
    public void testPipelineAddLast() {
        System.out.println("pipelineAddLast");
    }

    @Test
    public void testMultiPipelineAddLast() {
        System.out.println("multiPipelineAddLast");
    }

    @Test
    public void testInternalHasNext() {
        System.out.println("internalHasNext");
    }

    @Test
    public void testInternalNext() {
        System.out.println("internalNext");
    }

    @Test
    public void testCleanup() {
        System.out.println("cleanup");
    }

    @Test
    public void testStraightHasNext() {
        System.out.println("straightHasNext");
    }

    @Test
    public void testTryGetNext() {
        System.out.println("tryGetNext");
    }

    @Test
    public void testConcat() {
        System.out.println("concat");
    }

    @Test
    public void testFilter() {
        System.out.println("filter");
    }

    @Test
    public void testFlatMap() {
        System.out.println("flatMap");
    }

    @Test
    public void testMap() {
        System.out.println("map");
    }

    @Test
    public void testTakeWhile() {
    }

    @Test
    public void testZipAll_Iterator_IteratorArr() {
        System.out.println("zipAll");
    }

    @Test
    public void testZipAll_Iterator_List() {
        System.out.println("zipAll");
    }

    @Test
    public void testDequeueSourceWithProcessors() {
        System.out.println("dequeueSourceWithProcessors");
    }

    @Test
    public void testDequeueSourceProcessors() {
        System.out.println("dequeueSourceProcessors");
    }

    @Test
    public void testDequeueProcessor() {
        System.out.println("dequeueProcessor");
    }

    @Test
    public void testPipelineAddFirst() {
        System.out.println("pipelineAddFirst");
    }

    @Test
    public void testMultiPipelineAddFirst() {
        System.out.println("multiPipelineAddFirst");
    }

    @Test
    public void testSetSource() {
        System.out.println("setSource");
    }

    @Test
    public void testReversedConcat() {
        System.out.println("reversedConcat");
    }

    @Test
    public void testReversedFilter() {
        System.out.println("reversedFilter");
    }

    @Test
    public void testReversedFlatMap() {
        System.out.println("reversedFlatMap");
        source = Enumerator.on(1, 3, 5);
        pipe = PipeEnumerator.of(source)
                .reversedFlatMap(x -> {
                    final Integer y = (Integer)x;
                    return Enumerator.on(y, y+1);
                })
                .reversedFlatMap(x -> Enumerator.on(x));
        assertTrue(pipe.elementsEqual(
                Enumerator.on(1, 2, 3, 4, 5, 6)));
    }

    @Test
    public void testReversedMap() {
        System.out.println("reversedMap");
        source = Enumerator.on(1, 3, 5);
        pipe = PipeEnumerator.of(source)
                .reversedMap(x -> x)
                .reversedFlatMap(x -> {
                    final Integer y = (Integer)x;
                    return Enumerator.on(y, y+1);
                })
                .reversedMap(x -> x)
                .reversedFlatMap(x -> Enumerator.on(x))
                .reversedMap(x -> x);
        assertTrue(pipe.elementsEqual(
                Enumerator.on(1, 2, 3, 4, 5, 6)));
    }

    @Test
    public void testReversedTakeWhile() {
        System.out.println("reversedTakeWhile");
    }

    @Test
    public void testReversedZipAll() {
        System.out.println("reversedZipAll");
    }

    @Test
    public void testEnqueueProcessor_AbstractPipeMultiProcessor() {
        System.out.println("enqueueProcessor");
    }

    @Test
    public void testPushFrontProcessor_AbstractPipeMultiProcessor() {
        System.out.println("pushFrontProcessor");
    }

    @Test
    public void testEnqueueMapProcessor() {
        System.out.println("enqueueMapProcessor");
    }

    @Test
    public void testPushFrontMapProcessor() {
        System.out.println("pushFrontMapProcessor");
    }

    @Test
    public void testSafePipelineAddLast() {
        System.out.println("safePipelineAddLast");
    }

    @Test
    public void testSafePipelineAddFirst() {
        System.out.println("safePipelineAddFirst");
    }

    @Test
    public void testDequeueSourcesUpToProcessor() {
        System.out.println("dequeueSourcesUpToProcessor");
    }

    @Test
    public void testLimit() {
        System.out.println("limit");
    }

    @Test
    public void testPrepend() {
        System.out.println("prepend");
        assertTrue(Enumerator.on(5, 6)
                             .map(x -> x)
                             .prepend(Enumerator.on(1, 2, 3, 4))
                             .elementsEqual(Enumerator.on(1, 2, 3, 4, 5, 6)));
    }

    @Test
    public void testSkip() {
        System.out.println("skip");
    }

    @Test
    public void testSkipWhile() {
        System.out.println("skipWhile");
    }

    @Test
    public void testReversedLimit() {
        System.out.println("reversedLimit");
    }

    @Test
    public void testReversedSkip() {
        System.out.println("reversedSkip");
    }

    @Test
    public void testReversedSkipWhile() {
        System.out.println("reversedSkipWhile");
    }
}
