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

import java.util.concurrent.atomic.AtomicBoolean;
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
public class TolerantEnumeratorTest {

    public TolerantEnumeratorTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        it123 = Enumerator.on(1, 2, 3).asTolerant(ex -> {});
        it123_throw_once_no_retry = Enumerator.on(1, 2, 3)
                .filter(i -> {
                    if (!flag1.get()) {
                        throw new UnsupportedOperationException();
                    }
                    return true;
                }).asTolerant(ex -> {});
        it123_throw_once_1_retry = Enumerator.on(1, 2, 3)
                .filter(i -> {
                    if (!flag1.get()) {
                        flag1.set(true);
                        throw new UnsupportedOperationException();
                    }
                    return true;
                }).asTolerant(ex -> {}, 1);
        it123_throw_handler = Enumerator.on(1, 2, 3)
                .filter(i -> {
                    if (!flag1.get()) {
                        throw new UnsupportedOperationException();
                    }
                    return true;
                })
                .asTolerant(ex -> {
                    if (flag2.get()) {
                        throw new UnsupportedOperationException();
                    } else {
                        flag2.set(true);
                    }
                }, 2);
        it123_throw_next = Enumerator.on(1, 2, 3)
                .map(i -> {
                    if (i == 2) {
                        throw new UnsupportedOperationException();
                    }
                    return i;
                })
                .asTolerant(ex -> {});
        it123_throw_next_and_handler = Enumerator.on(1, 2, 3)
                .map(i -> {
                    if (i > 0) {
                        throw new UnsupportedOperationException();
                    }
                    return i;
                })
                .asTolerant(ex -> { 
                    throw new RuntimeException(ex); 
                }, 2);
        it123_throw_hasNext_and_handler = Enumerator.on(1, 2, 3)
                .filter(i -> {
                    if (i > 0) {
                        throw new UnsupportedOperationException();
                    }
                    return true;
                })
                .asTolerant(ex -> { 
                    throw new RuntimeException(ex); 
                }, 2);
        it123_throw_hasNext_and_handler_2nd_time = Enumerator.on(1, 2, 3)
                .filter(i -> {
                    if (i > 0) {
                        throw new UnsupportedOperationException();
                    }
                    return true;
                })
                .asTolerant(ex -> {
                    if (flag1.get()) {
                        throw new RuntimeException(ex); 
                    } else {
                        flag1.set(true);
                    }
                }, 2);
        it123_throw_hasNext = Enumerator.on(1, 2, 3)
                .filter(i -> {
                    if (i > 0) {
                        throw new UnsupportedOperationException();
                    }
                    return true;
                })
                .asTolerant(ex -> {}, 2);
    }
    Enumerator<Integer> it123;
    final AtomicBoolean flag1 = new AtomicBoolean(false);
    Enumerator<Integer> it123_throw_once_no_retry;
    Enumerator<Integer> it123_throw_once_1_retry;
    final AtomicBoolean flag2 = new AtomicBoolean(false);
    Enumerator<Integer> it123_throw_handler;
    Enumerator<Integer> it123_throw_next;
    Enumerator<Integer> it123_throw_next_and_handler;
    Enumerator<Integer> it123_throw_hasNext_and_handler;
    Enumerator<Integer> it123_throw_hasNext_and_handler_2nd_time;
    Enumerator<Integer> it123_throw_hasNext;

    private void resetFlags() {
        flag1.set(false);
        flag2.set(false);
    }

    @After
    public void tearDown() {
        it123 = null;
        it123_throw_once_no_retry = null;
        it123_throw_once_1_retry = null;
        it123_throw_handler = null;
        it123_throw_next = null;
        it123_throw_next_and_handler = null;
        it123_throw_hasNext_and_handler = null;
        it123_throw_hasNext_and_handler_2nd_time = null;
        it123_throw_hasNext = null;
    }

    /**
     * Test of internalHasNext method, of class TolerantEnumerator.
     */
    @Test
    public void testInternalHasNext() {
        System.out.println("internalHasNext");

        resetFlags();
        assertTrue(it123.hasNext() && it123.hasNext());

        resetFlags();
        assertFalse(it123_throw_once_no_retry.hasNext());

        resetFlags();
        assertTrue(it123_throw_once_1_retry.hasNext()
                   && it123_throw_once_1_retry.hasNext());

        resetFlags();
        assertFalse(it123_throw_handler.hasNext());

        resetFlags();
        assertTrue(it123_throw_next.elementsEqual(Enumerator.on(1, 3)));

        resetFlags();
        assertFalse(it123_throw_next_and_handler.hasNext());

        resetFlags();
        assertFalse(it123_throw_hasNext_and_handler.hasNext());

        resetFlags();
        assertFalse(it123_throw_hasNext_and_handler_2nd_time.hasNext());

        resetFlags();
        assertFalse(it123_throw_hasNext.hasNext());
    }

    /**
     * Test of internalNext method, of class TolerantEnumerator.
     */
    @Test
    public void testInternalNext() {
        System.out.println("internalNext");
        assertNotNull(Enumerator.on(1, 2, 3)
                                .asTolerant(ex -> {})
                                .elementsEqual(Enumerator.on(1, 2, 3)));
    }

    /**
     * Test of cleanup method, of class TolerantEnumerator.
     */
    @Test
    public void testCleanup() {
        System.out.println("cleanup");
        assertNotNull(Enumerator.on(1, 2, 3).asTolerant(ex -> {}));
    }
}
